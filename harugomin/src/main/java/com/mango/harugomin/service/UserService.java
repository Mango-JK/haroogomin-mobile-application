package com.mango.harugomin.service;

import com.google.gson.JsonObject;
import com.mango.harugomin.domain.entity.*;
import com.mango.harugomin.domain.repository.*;
import com.mango.harugomin.dto.UserResponseDto;
import com.mango.harugomin.dto.UserSignUpRequestDto;
import com.mango.harugomin.dto.UserTokenResponseDto;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import com.mango.harugomin.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final JwtService jwtService;
	private final S3Service s3Service;

	private final TokenRepository tokenRepository;
	private final HashtagRepository hashtagRepository;
	private final CommentRepository commentRepository;
	private final HistoryRepository historyRepository;
	private final LikerRepository likerRepository;
	private final UserHashtagRepository userHashtagRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public Optional<User> findById(long userId) {
		return userRepository.findById(userId);
	}

	@Transactional(readOnly = true)
	public Optional<User> findByUserLoginId(String id) {
		return userRepository.findByUserLoginId(id);
	}

	@Transactional
	public String signup(UserSignUpRequestDto requestDto) {
		User user = User.builder()
			.userLoginId(requestDto.getUserLoginId())
			.password(requestDto.getPassword())
			.nickname(requestDto.getNickname())
			.profileImage(requestDto.getProfileImage())
			.ageRange(requestDto.getAgeRange())
			.build();
		if(user.getProfileImage() == null)
			user.setDefaultProfile("https://hago-bucket.s3.ap-northeast-2.amazonaws.com/default01.png");
		User newUser = userRepository.save(user);
		updateUserHashtag(newUser, requestDto.getUserhashtags());

		try {
			String jwt = jwtService.create("user", newUser, "user");
			Token token = new Token(newUser.getUserId(), jwt);
			tokenRepository.save(token);
			JsonObject jsonJwt = new JsonObject();
			jsonJwt.addProperty("jwt", jwt);
			return jsonJwt.toString();
		} catch (Exception e) {
			log.error("JWT Create Token Error : " + e);
			return "JWT Token Create Exception";
		}
	}

	public String login(String id, String password) {
		Optional<User> user = findByUserLoginId(id);
		if (user.isPresent()) {
			User loginUser = user.get();
			if (!password.equals(loginUser.getPassword())) {
				return "비밀번호가 일치하지 않습니다.";
			}
		} else {
			return "ID가 존재하지 않습니다.";
		}
		Long userId = user.get().getUserId();
		String jwt = tokenRepository.findById(userId).get().getJwt();
		JsonObject jsonJwt = new JsonObject();
		jsonJwt.addProperty("jwt", jwt);
		return jsonJwt.toString();
	}

	public ResponseEntity checkToken(String jwt) {
		User user = null;
		if (jwtService.isUsable(jwt)) {
			Object obj = jwtService.get("user");
			user = findById(Long.parseLong(obj.toString())).get();
		} else {
			return new ResponseEntity("유효하지 않은 토큰입니다.", HttpStatus.NOT_FOUND);
		}
		UserTokenResponseDto result = new UserTokenResponseDto(user);
		return new ResponseEntity(result, HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	public ResponseEntity duplicationCheckById(String id) {
		JsonObject data = new JsonObject();
		if (userRepository.countByUserLoginId(id) > 0)
			data.addProperty("flag", false);
		else
			data.addProperty("flag", true);

		return new ResponseEntity<>(data.toString(), HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	public ResponseEntity duplicationCheckByNickname(String nickname) {
		JsonObject data = new JsonObject();
		if (userRepository.countByNickname(nickname) > 0) {
			data.addProperty("flag", false);
		} else {
			data.addProperty("flag", true);
		}
		return new ResponseEntity(data.toString(), HttpStatus.OK);
	}

	// TODO NAVER LOGIN
//	public String naverLogin(HttpServletRequest request) {
//		String accessToken = request.getHeader("accessToken");
//		JsonNode json = naverApiService.getNaverUserInfo(accessToken);
//
//		String result = null;
//		JsonObject data = new JsonObject();
//		try {
//			result = naverApiService.redirectToken(json);
//		} catch (Exception e) {
//			data.addProperty("status", String.valueOf(HttpStatus.BAD_REQUEST));
//			return data.toString();
//		}
//		data.addProperty("jwt", result);
//		data.addProperty("status", String.valueOf(HttpStatus.OK));
//		return data.toString();
//	}

	@Transactional
	public void deleteById(Long userId) {
		userRepository.deleteUser(userId);
	}

	@Transactional
	public String updateUserProfile(Long userId, MultipartFile file) throws IOException {
		User user = userRepository.findById(userId).get();
		String imgPath = S3Service.CLOUD_FRONT_DOMAIN_NAME + s3Service.upload(user.getProfileImage(), file);
		user.updateUserImage(imgPath);
		userRepository.save(user);
		JsonObject data = new JsonObject();
		data.addProperty("imgPath", imgPath);
		data.addProperty("status", String.valueOf(HttpStatus.OK));

		return data.toString();
	}

	@Transactional
	public ResponseEntity<UserResponseDto> updateUserInfo(UserUpdateRequestDto requestDto) {
		Optional<User> user = findById(requestDto.getUserId());
		if (!user.isPresent())
			return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
		updateUser(user.get(), requestDto);
		return new ResponseEntity<>(new UserResponseDto(user.get()), HttpStatus.OK);
	}

	@Transactional
	public void updateUser(User user, UserUpdateRequestDto requestDto) {
		user.updateUserInfo(requestDto);
		updateUserHashtag(user, requestDto.getUserHashtags());
	}

	@Transactional
	public void updateUserHashtag(User user, List<String> hashtags) {
		user.initHashtagInfo();

		if(hashtags == null || hashtags.size() < 1)
			return;

		for (String tagName : hashtags) {
			Hashtag hashtag = hashtagRepository.findByTagName(tagName);
			UserHashtag newUserHashtag = new UserHashtag(user, hashtag);
			user.addHashtag(newUserHashtag);
			userHashtagRepository.save(newUserHashtag).toString();
		}
	}

	@Transactional
	public ResponseEntity<UserResponseDto> updateUserHashtagInfo(Long userId, List<String> hashtags) {
		Optional<User> user = findById(userId);
		if (!user.isPresent())
			return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
		updateUserHashtag(user.get(), hashtags);
		return new ResponseEntity<>(new UserResponseDto(user.get()), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<Long> deleteUser(Long userId) {
		postRepository.foreignkeyOpen();
		historyRepository.deleteAllByUsers(userId);
		commentRepository.deleteAllByUserId(userId);
		likerRepository.deleteAllbyUserId(userId);
		userHashtagRepository.deleteAllByUserId(userId);
		postRepository.deleteAllByUserUserId(userId);
		deleteById(userId);
		tokenRepository.delete(tokenRepository.findById(userId).get());
		postRepository.foreignkeyClose();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity userAnnouncingPosts(Long userId, int pageNum) {
		PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
		Page<Post> result = postRepository.findAllByUserUserId(userId, pageRequest);
		if (result.isEmpty())
			return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
		return new ResponseEntity(result.getContent(), HttpStatus.OK);
	}

	public ResponseEntity userHistoryPosts(Long userId, int pageNum) {
		PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
		Page<History> result = historyRepository.findAllByUserUserId(userId, pageRequest);
		if (result.isEmpty())
			return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
		return new ResponseEntity<>(result.getContent(), HttpStatus.OK);
	}
}
