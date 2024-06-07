package com.sparta.icy.Service;

import com.sparta.icy.Dto.NewsfeedDto;
import com.sparta.icy.Dto.NewsfeedResponseDto;
import com.sparta.icy.Entity.Newsfeed;
import com.sparta.icy.Entity.User;
import com.sparta.icy.Repository.NewsfeedRepository;
import com.sparta.icy.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsfeedService {
    private final NewsfeedRepository newsfeedRepository;

    @Autowired
    public NewsfeedService(NewsfeedRepository newsfeedRepository) {
        this.newsfeedRepository = newsfeedRepository;
    }

    public void createNewsfeed(NewsfeedDto newsfeedDto) {
        User currentUser = getUser();

        String content = newsfeedDto.getContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("뉴스피드 내용이 비어있습니다.");
        }

        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setContent(content);

        LocalDateTime now = LocalDateTime.now();
        newsfeed.setCreated_at(now);
        newsfeed.setUpdated_at(now);

        newsfeed.setUser(currentUser);

        newsfeedRepository.save(newsfeed);
    }

    private static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        // Principal이 UserDetailsImpl 타입인지 확인
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            throw new IllegalStateException("사용자 정보를 가져올 수 없습니다.");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        User currentUser = userDetails.getUser();
        return currentUser;
    }

    // 특정 게시물 조회 메서드
    public NewsfeedResponseDto getNewsfeed(Long id) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물을 찾을 수 없습니다: " + id));
        NewsfeedResponseDto newsfeedResponseDto = new NewsfeedResponseDto();
        newsfeedResponseDto.setId(newsfeed.getId());
        newsfeedResponseDto.setContent(newsfeed.getContent());
        newsfeedResponseDto.setCreated_at(newsfeed.getCreated_at());
        newsfeedResponseDto.setUpdated_at(newsfeed.getUpdated_at());
        newsfeedResponseDto.setUser_id(newsfeed.getUser().getId());
        return newsfeedResponseDto;
    }

    // 게시물 수정 메서드
    public void updateNewsfeed(Long feed_id, NewsfeedDto newsfeedDto) {
        User currentUser = getUser();
        Newsfeed newsfeed = newsfeedRepository.findById(feed_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물을 찾을 수 없습니다: " + feed_id));

        if (!currentUser.getUsername().equals(newsfeed.getUser().getUsername())) {
            throw new IllegalArgumentException("게시물 업데이트 권한이 없습니다.");
        }
        newsfeed.setContent(newsfeedDto.getContent());
        newsfeed.setUpdated_at(LocalDateTime.now());
        newsfeedRepository.save(newsfeed);
    }

    // 게시물 삭제 메서드
    public void deleteNewsfeed(Long id) {
        User currentUser = getUser();
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물을 찾을 수 없습니다: " + id));

        if (!currentUser.getUsername().equals(newsfeed.getUser().getUsername())) {
            throw new IllegalArgumentException("게시물 삭제 권한이 없습니다.");
        }
        newsfeedRepository.delete(newsfeed);
    }

    // 모든 게시물 조회 메서드
    public List<NewsfeedResponseDto> getAllNewsfeed() {
        List<Newsfeed> newsfeeds = newsfeedRepository.findAll();

        // 게시물을 생성일 기준으로 내림차순으로 정렬
        newsfeeds.sort(Comparator.comparing(Newsfeed::getCreated_at).reversed());

        return newsfeeds.stream()
                .map(newsfeed -> {
                    NewsfeedResponseDto newsfeedResponseDto = new NewsfeedResponseDto();
                    newsfeedResponseDto.setId(newsfeed.getId());
                    newsfeedResponseDto.setContent(newsfeed.getContent());
                    newsfeedResponseDto.setCreated_at(newsfeed.getCreated_at());
                    newsfeedResponseDto.setUpdated_at(newsfeed.getUpdated_at());
                    newsfeedResponseDto.setUser_id(newsfeed.getUser().getId());
                    return newsfeedResponseDto;
                })
                .collect(Collectors.toList());
    }
}
