package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.*;
import com.mango.harugomin.domain.repository.HistoryRepository;
import com.mango.harugomin.domain.repository.LikerRepository;
import com.mango.harugomin.domain.repository.PostRepository;
import com.mango.harugomin.dto.PostResponseDto;
import com.mango.harugomin.dto.PostSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

	private final UserService userService;
	private final HashtagService hashtagService;
	private final HistoryService historyService;
	private final PostRepository postRepository;
	private final HistoryRepository historyRepository;
	private final LikerRepository likerRepository;

	@Transactional
	public Post save(PostSaveRequestDto requestDto) {
		User user = userService.findById(requestDto.getUserId()).get();
		Hashtag hashtag = hashtagService.findByTagName(requestDto.getTagName());
		hashtagService.userHashtagCountPlusOne(hashtag.getTagId());

		return postRepository.save(Post.builder()
			.user(user)
			.title(requestDto.getTitle())
			.content(requestDto.getContent())
			.tagName(requestDto.getTagName())
			.postImage(requestDto.getPostImage())
			.hits(0)
			.commentNum(0)
			.build()
		);
	}

	@Transactional
	public ResponseEntity addPost(PostSaveRequestDto requestDto) {
		Post post = null;
		if (requestDto.getPostId() == -1) {
			post = save(requestDto);
		} else
			post = updatePost(requestDto);
		return new ResponseEntity(new PostResponseDto(post), HttpStatus.OK);
	}

	@Transactional
	public Post updatePost(PostSaveRequestDto requestDto) {
		Post post = postRepository.findById(requestDto.getPostId()).get();
		post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getTagName(), requestDto.getPostImage());
		return post;
	}

	@Transactional
	public ResponseEntity deletePost(Long postId) {
		Optional<Post> post = postRepository.findById(postId);
		if (!post.isPresent())
			return ResponseEntity.notFound().build();
		postRepository.delete(post.get());
		return new ResponseEntity(HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	public Page<Post> findAllPosts(PageRequest pageRequest) {
		return postRepository.findAll(pageRequest);
	}

	@Transactional
	public Optional<Post> findById(Long postId) {
		postRepository.postHits(postId);
		return postRepository.findById(postId);
	}

	public ResponseEntity getPostDetails(Long postId) {
		Optional<Post> post = findById(postId);
		if (!post.isPresent()) {
			Optional<History> history = historyService.findById(postId);
			if (!history.isPresent())
				return ResponseEntity.notFound().build();
			return new ResponseEntity(history.get(), HttpStatus.OK);
		}
		PostResponseDto result = new PostResponseDto(post.get());
		LocalDateTime currentTime = LocalDateTime.now();
		Duration duration = Duration.between(post.get().getCreatedDate(), currentTime);
		if (duration.getSeconds() >= 86300) {
			postToHistory(post.get().getPostId());
		}

		return new ResponseEntity(result, HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	public Page<Post> findAllByHashtag(String tagName, PageRequest pageRequest) {
		return postRepository.findAllByTagName(tagName, pageRequest);
	}

	@Transactional
	public void postHits(Long postId) {
		postRepository.postHits(postId);
	}

	@Transactional
	public void postToHistory(Long postId) {
		Post post = postRepository.findById(postId).get();
		History history = new History(post);
		historyRepository.save(history);
		likerRepository.deleteAllByPostId(postId);
		postRepository.deleteById(post.getPostId());
	}

	public ResponseEntity getHashtagByPostingCount() {
		PageRequest tagRequest = PageRequest.of(0, 12, Sort.by("postingCount").descending());
		List<Hashtag> topTags = null;
		topTags = hashtagService.findAllTags(tagRequest).getContent();
		return new ResponseEntity(topTags, HttpStatus.OK);
	}

	public ResponseEntity getStoryPosts() {
		LocalDateTime currentTime = LocalDateTime.now();
		PageRequest storyRequest = PageRequest.of(0, 13, Sort.by("createdDate"));
		List<Post> data = findAllPosts(storyRequest).getContent();
		if (data == null)
			return ResponseEntity.notFound().build();

		List<Post> story = new ArrayList<>();
		for (Post post : data) {
			Duration duration = Duration.between(post.getCreatedDate(), currentTime);
			if (duration.getSeconds() >= 86300) {
				postToHistory(post.getPostId());
				continue;
			} else {
				story.add(post);
			}
			if (story.size() >= 10)
				break;
		}
		return new ResponseEntity(story, HttpStatus.OK);
	}

	public ResponseEntity getPostsByHashtag(String tagName, int pageNum) {
		Page<Post> result = null;
		PageRequest pageRequest = null;
		if (tagName.equals("전체")) {
			pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
			Page<Post> list = findAllPosts(pageRequest);
			if (list.isEmpty())
				return ResponseEntity.notFound().build();
			return new ResponseEntity(list.getContent(), HttpStatus.OK);
		}
		pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
		result = findAllByHashtag(tagName, pageRequest);
		if (result.isEmpty())
			return ResponseEntity.notFound().build();
		return new ResponseEntity(result.getContent(), HttpStatus.OK);
	}

	public ResponseEntity searchPostsByKeyword(String keyword, int pageNum) {
		PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("created_date").descending());
		Page<Post> result = null;
		result = postRepository.searchAllPosts(keyword, pageRequest);
		if (result.isEmpty())
			return ResponseEntity.notFound().build();
		return new ResponseEntity(result.getContent(), HttpStatus.OK);
	}

	public ResponseEntity getMainPosts(Long userId) {
		Optional<User> user = userService.findById(userId);
		if (user.isEmpty())
			return ResponseEntity.notFound().build();
		String userHashString = "";
		List<UserHashtag> userHashtags = user.get().getUserHashtags();

		// 1. hit 많은 고민글 15개 조회
		PageRequest pageRequest = PageRequest.of(0, 15, Sort.by("hits").descending());
		Page<Post> data = null;
		List<Post> result = new ArrayList<>();
		int i = 0;
		data = findAllPosts(pageRequest);

		// 비로그인 사용자 또는 해시태그가 없는 사용자에게는 hit수 높은 고민글 3개 출력
		if (userId == -1 || userHashtags.size() < 1) {
			while (result.size() < 3) {
				result.add(data.getContent().get(i));
				i++;
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

		for (UserHashtag userHashtag : userHashtags) {
			userHashString += userHashtag.getHashtag().getTagName();
		}

		for (Post post : data.getContent()) {
			if (result.size() > 2)
				break;
			if (userHashString.contains(post.getTagName())) {
				result.add(post);
			}
		}

		for (Post post : data.getContent()) {
			if (result.size() > 2)
				break;
			if (!result.contains(post)) {
				result.add(post);
			}
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public Page<Post> findAllByUserId(Long userId, PageRequest pageRequest) {
		return postRepository.findAllByUserUserId(userId, pageRequest);
	}

	@Transactional
	public void deleteUserPosts(Long userId) {
		postRepository.deleteAllByUserUserId(userId);
	}

	@Transactional
	public void foreignkeyOpen() {
		postRepository.foreignkeyOpen();
	}

	@Transactional
	public void foreignkeyClose() {
		postRepository.foreignkeyClose();
	}
}
