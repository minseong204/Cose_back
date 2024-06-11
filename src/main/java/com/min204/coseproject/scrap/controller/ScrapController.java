package com.min204.coseproject.scrap.controller;

import com.min204.coseproject.scrap.dto.ScrapDto;
import com.min204.coseproject.scrap.service.ScrapService;
import com.min204.coseproject.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scraps")
@RequiredArgsConstructor
public class ScrapController {
    private final ScrapService scrapService;

    @PostMapping("/{contentId}")
    public ResponseEntity<Void> scrapContent(@PathVariable Long contentId) {
        scrapService.scrapContent(contentId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> unscriptContent(@PathVariable Long contentId) {
        scrapService.unscriptContent(contentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<SingleResponseDto<List<ScrapDto>>> getUserScraps() {
        List<ScrapDto> scraps = scrapService.getUserScraps();
        return new ResponseEntity<>(new SingleResponseDto<>(scraps), HttpStatus.OK);
    }
}