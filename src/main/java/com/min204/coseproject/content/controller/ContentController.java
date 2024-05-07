package com.min204.coseproject.content.controller;

import com.min204.coseproject.content.dto.ContentPatchDto;
import com.min204.coseproject.content.dto.ContentPostDto;
import com.min204.coseproject.content.dto.ContentResponseDto;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.mapper.ContentMapper;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.content.service.ContentService;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.course.service.CourseService;
import com.min204.coseproject.response.MultiResponseDto;
import com.min204.coseproject.response.SingleResponseDto;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentService;
    private final ContentMapper contentMapper;
    private final ContentRepository contentRepository;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    public ContentController(ContentService contentService, ContentMapper contentMapper, ContentRepository contentRepository, CourseService courseService, CourseRepository courseRepository) {
        this.contentService = contentService;
        this.contentMapper = contentMapper;
        this.contentRepository = contentRepository;
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }


    /*
     * 게시글 생성
     * */
    @PostMapping
    public ResponseEntity postContent(@Valid @RequestBody ContentPostDto requestBody) {

        Content content = contentService.createContent(contentMapper.contentPostDtoToContent(requestBody));
        ContentResponseDto contentResponse = contentMapper.contentToContentResponse(content);

        return new ResponseEntity<>(
                new SingleResponseDto<>(contentResponse), HttpStatus.OK
        );
    }

    /*
     * 게시글 단일 조회
     * */
    @GetMapping("/{contentId}")
    public ResponseEntity getContent(@PathVariable("contentId") Long contentId) {
        Content content = contentService.findContent(contentId);
        int viewCount = content.getViewCount();
        content.setViewCount(++viewCount);
        contentService.updateViewCount(content);

        return contentService.detail(content);
    }

    /*
     * 게시글 전체 조회
     * */
    @GetMapping
    public ResponseEntity getContents(@RequestParam("page") int page,
                                      @RequestParam("size") int size) {
        Page<Content> pageContents = contentService.findContents(page - 1, size);
        List<Content> contents = pageContents.getContent();
        contents.stream().forEach(
                content -> content.setCourses(courseService.findCoursesByContentId(content.getContentId()))
        );

        return new ResponseEntity<>(
                new MultiResponseDto<>(
                        contentMapper.contentsToContentResponse(contents),
                        pageContents),
                HttpStatus.OK
        );
    }

    /*
     * 게시글 수정
     * */
    @PatchMapping("/{contentId}")
    public ResponseEntity patchContent(@RequestBody ContentPatchDto requestBody,
                                       @PathVariable("contentId") Long contentId) {

        requestBody.updateId(contentId);
        Content content = contentService.updateContent(
                contentMapper.contentPatchDtoToContent(requestBody));

        ContentResponseDto contentResponse = contentMapper.contentToContentResponse(content);

        return new ResponseEntity<>(contentResponse, HttpStatus.OK);
    }

    /*
     * 게시글 삭제
     * */
    @DeleteMapping("/{contentId}")
    public ResponseEntity deleteContent(@PathVariable("contentId") Long contentId) {
        contentService.deleteContent(contentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
