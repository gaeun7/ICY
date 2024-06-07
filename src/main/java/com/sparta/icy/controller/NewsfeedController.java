package com.sparta.icy.controller;

import com.sparta.icy.Dto.NewsfeedDto;
import com.sparta.icy.Dto.NewsfeedResponseDto;
import com.sparta.icy.Service.NewsfeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/newsfeed")
public class NewsfeedController {
    private final NewsfeedService newsfeedService;

    @Autowired
    public NewsfeedController(NewsfeedService newsfeedService) {
        this.newsfeedService = newsfeedService;
    }

    @PostMapping
    public void createNewsfeed(@RequestBody NewsfeedDto newsfeedDto) {
        newsfeedService.createNewsfeed(newsfeedDto);
    }

    @GetMapping("/{id}")
    public NewsfeedResponseDto getNewsfeed(@PathVariable Long id) {
        return newsfeedService.getNewsfeed(id);
    }

    @PutMapping("/{feed_id}")
    public void updateNewsfeed(@PathVariable Long feed_id, @RequestBody NewsfeedDto newsfeedDto) {
        newsfeedService.updateNewsfeed(feed_id, newsfeedDto);
    }

    @DeleteMapping("/{feed_id}")
    public void deleteNewsfeed(@PathVariable Long feed_id) {
        newsfeedService.deleteNewsfeed(feed_id);
    }

    @GetMapping
    public ResponseEntity<?> getAllNewsfeed() {
        List<NewsfeedResponseDto> newsfeedDtos = newsfeedService.getAllNewsfeed();
        if (newsfeedDtos.isEmpty()) {
            return ResponseEntity.ok("먼저 작성하여 소식을 알려보세요!");
        } else {
            return ResponseEntity.ok(newsfeedDtos);
        }
    }
}
