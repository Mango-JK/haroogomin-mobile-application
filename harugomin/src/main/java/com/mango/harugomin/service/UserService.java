package com.mango.harugomin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.entity.UserHashtag;
import com.mango.harugomin.domain.repository.UserHashtagRepository;
import com.mango.harugomin.domain.repository.UserRepository;
import com.mango.harugomin.dto.UserSignUpRequestDto;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import com.mango.harugomin.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final HashtagService hashtagService;
	private final JwtService jwtService;
	private final TokenService tokenService;
	private final NaverApiService naverApiService;

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
		User newUser = userRepository.save(user);

		updateUserHashtag(newUser, requestDto.getUserHashtags());

		try {
			String jwt = jwtService.create("user", newUser, "user");
			tokenService.save(newUser.getUserId(), jwt);
			JsonObject jsonJwt = new JsonObject();
			jsonJwt.addProperty("jwt", jwt);
			return jsonJwt.toString();
		} catch (Exception e) {
			log.error("JWT Create Token Error : " + e);
			return "JWT Token Create Exception";
		}
	}

	public void updateUserHashtag(User user, String[] hashtags) {
		user.initHashtagInfo();

		for (String tagName : hashtags) {
			Hashtag hashtag = hashtagService.findByTagName(tagName);
			UserHashtag newUserHashtag = new UserHashtag(user, hashtag);
			hashtagService.userHashtagCountPlusOne(hashtag.getTagId());
			user.addHashtag(newUserHashtag);
		}
	}

	public String login(String id, String password) {
		Optional<User> user = findByUserLoginId(id);
		if (!user.isEmpty()) {
			User loginUser = user.get();
			if (!password.equals(loginUser.getPassword())) {
				return "비밀번호가 일치하지 않습니다.";
			}
		} else {
			return "ID가 존재하지 않습니다.";
		}
		Long userId = user.get().getUserId();
		String jwt = tokenService.findById(userId).get().getJwt();
		JsonObject jsonJwt = new JsonObject();
		jsonJwt.addProperty("jwt", jwt);
		return jsonJwt.toString();
	}

	@Transactional(readOnly = true)
	public boolean duplicationCheckId(String id) {
		if (userRepository.countByUserLoginId(id) > 0) {
			return false;
		}
		return true;
	}

	@Transactional(readOnly = true)
	public boolean duplicationCheck(String nickname) {
		if (userRepository.countByNickname(nickname) > 0) {
			return false;
		} else {
			return true;
		}
	}

	public String naverLogin(HttpServletRequest request) {
		String accessToken = request.getHeader("accessToken");
		JsonNode json = naverApiService.getNaverUserInfo(accessToken);

		String result = null;
		JsonObject data = new JsonObject();
		try {
			result = naverApiService.redirectToken(json);
		} catch (Exception e) {
			data.addProperty("status", String.valueOf(HttpStatus.BAD_REQUEST));
			return data.toString();
		}
		data.addProperty("jwt", result);
		data.addProperty("status", String.valueOf(HttpStatus.OK));
		return data.toString();
	}

	@Transactional
	public void updateUser(UserUpdateRequestDto requestDto) {
		User user = findById(requestDto.getUserId()).get();
		user.updateUserInfo(requestDto);
		updateUserHashtag(user, requestDto.getUserHashtags());
//		userRepository.save(user);
	}

	@Transactional
	public void deleteById(Long userId) {
		userRepository.deleteUser(userId);
	}

}
