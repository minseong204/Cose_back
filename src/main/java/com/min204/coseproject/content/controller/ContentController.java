package com.min204.coseproject.content.controller;

import com.min204.coseproject.content.dto.ContentPatchDto;
import com.min204.coseproject.content.dto.ContentPostDto;
import com.min204.coseproject.content.dto.ContentResponseDto;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.mapper.ContentMapper;
import com.min204.coseproject.content.service.ContentService;
import com.min204.coseproject.course.service.CourseService;
import com.min204.coseproject.response.MultiResponseDto;
import com.min204.coseproject.response.SingleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentService;
    private final ContentMapper contentMapper;
    private final CourseService courseService;

    public ContentController(ContentService contentService, ContentMapper contentMapper, CourseService courseService) {
        this.contentService = contentService;
        this.contentMapper = contentMapper;
        this.courseService = courseService;
    }

    /*
     * 게시글 생성
     * */
    @PostMapping
    public ResponseEntity<SingleResponseDto<ContentResponseDto>> postContent(@Valid @RequestBody ContentPostDto requestBody) {
        Content content = contentService.createContent(requestBody);
        ContentResponseDto contentResponse = contentMapper.contentToContentResponse(content);

        return new ResponseEntity<>(new SingleResponseDto<>(contentResponse), HttpStatus.OK);
    }

    /*
     * 게시글과 코스 연결
     * */
    @PostMapping("/{contentId}/course/{courseId}")
    public ResponseEntity<Void> linkCourse(@PathVariable("contentId") Long contentId,
                                           @PathVariable("courseId") Long courseId) {
        contentService.linkCourse(contentId, courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
     * 게시글 단일 조회
     * */
    @GetMapping("/{contentId}")
    public ResponseEntity<SingleResponseDto<ContentResponseDto>> getContent(@PathVariable("contentId") Long contentId) {
        Content content = contentService.findContent(contentId);
        int viewCount = content.getViewCount();
        content.setViewCount(++viewCount);
        contentService.updateViewCount(content);

        ContentResponseDto contentResponse = contentMapper.contentToContentResponse(content);
        return new ResponseEntity<>(new SingleResponseDto<>(contentResponse), HttpStatus.OK);
    }

    /*
     * 게시글 전체 조회
     * */
    @GetMapping
    public ResponseEntity<MultiResponseDto<ContentResponseDto>> getContents(@RequestParam("page") int page,
                                                                            @RequestParam("size") int size) {
        Page<Content> pageContents = contentService.findContents(page - 1, size);
        List<Content> contents = pageContents.getContent();

        contents.forEach(content -> content.setCourses(courseService.findCoursesByContentId(content.getContentId())));
        List<ContentResponseDto> contentResponses = contentMapper.contentsToContentResponse(contents);

        return new ResponseEntity<>(new MultiResponseDto<>(contentResponses, pageContents), HttpStatus.OK);
    }

    /*
     * 게시글 수정
     * */
    @PatchMapping("/{contentId}")
    public ResponseEntity<SingleResponseDto<ContentResponseDto>> patchContent(@RequestBody ContentPatchDto requestBody,
                                                                              @PathVariable("contentId") Long contentId) {
        requestBody.updateId(contentId);
        Content content = contentService.updateContent(contentMapper.contentPatchDtoToContent(requestBody));
        ContentResponseDto contentResponse = contentMapper.contentToContentResponse(content);

        return new ResponseEntity<>(new SingleResponseDto<>(contentResponse), HttpStatus.OK);
    }

    /*
     * 게시글 삭제
     * */
    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> deleteContent(@PathVariable("contentId") Long contentId) {
        contentService.deleteContent(contentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
