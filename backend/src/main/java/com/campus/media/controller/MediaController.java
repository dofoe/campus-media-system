package com.campus.media.controller;

import com.campus.media.dto.request.*;
import com.campus.media.dto.response.ApiResponse;
import com.campus.media.dto.response.PageResponse;
import com.campus.media.entity.Media;
import com.campus.media.entity.User;
import com.campus.media.service.MediaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/media")
public class MediaController {
    
    private final MediaService mediaService;
    
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }
    
    @GetMapping("/search")
    public ApiResponse<PageResponse<Media>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResponse<Media> response = mediaService.search(keyword, categoryId, fileType, tag, pageNum, pageSize);
        return ApiResponse.success(response);
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Media> getMediaDetail(@PathVariable Long id) {
        Media media = mediaService.getDetail(id);
        return ApiResponse.success(media);
    }
    
    @PostMapping("/upload/init")
    public ApiResponse<Map<String, Object>> initUpload(@Valid @RequestBody UploadInitRequest request,
                                                       @AuthenticationPrincipal User user) {
        Map<String, Object> response = mediaService.initUpload(request, user);
        return ApiResponse.success(response);
    }
    
    @PostMapping("/upload/complete")
    public ApiResponse<Media> completeUpload(@Valid @RequestBody UploadCompleteRequest request,
                                             @AuthenticationPrincipal User user) {
        Media media = mediaService.completeUpload(request, user);
        return ApiResponse.success(media);
    }
    
    @PostMapping("/{id}/download")
    public ApiResponse<Map<String, Object>> getDownloadUrl(@PathVariable Long id,
                                                           @AuthenticationPrincipal User user,
                                                           HttpServletRequest request) {
        Map<String, Object> response = mediaService.getDownloadUrl(id, user, request);
        return ApiResponse.success(response);
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ApiResponse.success();
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Media> updateMedia(@PathVariable Long id, @RequestBody MediaUpdateRequest request) {
        Media media = mediaService.updateMedia(id, request);
        return ApiResponse.success(media);
    }
    
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDeleteMedia(@RequestBody Map<String, Long[]> body) {
        Long[] ids = body.get("ids");
        mediaService.batchDeleteMedia(ids);
        return ApiResponse.success();
    }
    
    @GetMapping("/audit/pending")
    public ApiResponse<PageResponse<Media>> getPendingAudit(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResponse<Media> response = mediaService.getPendingAudit(pageNum, pageSize);
        return ApiResponse.success(response);
    }
    
    @PostMapping("/audit/{id}")
    public ApiResponse<Media> auditMedia(@PathVariable Long id,
                                         @Valid @RequestBody AuditRequest request,
                                         @AuthenticationPrincipal User user) {
        Media media = mediaService.auditMedia(id, request, user);
        return ApiResponse.success(media);
    }
}
