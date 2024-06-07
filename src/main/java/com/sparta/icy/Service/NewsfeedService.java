package com.sparta.icy.Service;

import com.sparta.icy.Dto.NewsfeedDto;
import com.sparta.icy.Entity.Newsfeed;
import com.sparta.icy.Repository.NewsfeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setContent(newsfeedDto.getContent());
        newsfeed.setCreated_at(LocalDateTime.now());
        newsfeed.setCreated_at(LocalDateTime.now());
        newsfeedRepository.save(newsfeed);
    }

    // 특정 게시물 조회 메서드
    public NewsfeedDto getNewsfeed(Long id) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물을 찾을 수 없습니다: " + id));
        NewsfeedDto newsfeedDto = new NewsfeedDto();
        newsfeedDto.setId(newsfeed.getId());
        newsfeedDto.setContent(newsfeed.getContent());
        newsfeedDto.setCreated_at(newsfeed.getCreated_at());
        newsfeedDto.setUpdated_at(newsfeed.getUpdated_at());
        newsfeedDto.setUser_id(newsfeed.getUser().getId());
        return newsfeedDto;
    }

    // 게시물 수정 메서드
    public void updateNewsfeed(Long id, NewsfeedDto newsfeedDto) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물을 찾을 수 없습니다: " + id));
        newsfeed.setContent(newsfeedDto.getContent());
        newsfeed.setUpdated_at(LocalDateTime.now());
        newsfeedRepository.save(newsfeed);
    }

    // 게시물 삭제 메서드
    public void deleteNewsfeed(Long id) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물을 찾을 수 없습니다: " + id));
        newsfeedRepository.delete(newsfeed);
    }

    // 모든 게시물 조회 메서드
    public List<NewsfeedDto> getAllNewsfeed() {
        List<Newsfeed> boards = newsfeedRepository.findAll();

        // 게시물을 생성일 기준으로 내림차순으로 정렬
        boards.sort(Comparator.comparing(Newsfeed::getCreated_at).reversed());

        return boards.stream()
                .map(board -> {
                    NewsfeedDto newsfeedDto = new NewsfeedDto();
                    newsfeedDto.setId(board.getId());
                    newsfeedDto.setContent(board.getContent());
                    newsfeedDto.setCreated_at(board.getCreated_at());
                    newsfeedDto.setUpdated_at(board.getUpdated_at());
                    newsfeedDto.setUser_id(board.getUser().getId());
                    return newsfeedDto;
                })
                .collect(Collectors.toList());
    }
}
