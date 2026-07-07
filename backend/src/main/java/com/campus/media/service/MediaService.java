package com.campus.media.service;

import com.campus.media.dto.request.AuditRequest;
import com.campus.media.dto.request.MediaUpdateRequest;
import com.campus.media.dto.request.UploadCompleteRequest;
import com.campus.media.dto.request.UploadInitRequest;
import com.campus.media.dto.response.PageResponse;
import com.campus.media.entity.Media;
import com.campus.media.entity.User;
import com.campus.media.repository.DownloadLogRepository;
import com.campus.media.repository.MediaRepository;
import com.campus.media.entity.DownloadLog;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MediaService {
    
    private final MediaRepository mediaRepository;
    private final DownloadLogRepository downloadLogRepository;
    private final Map<String, UploadContext> uploadContexts = new ConcurrentHashMap<>();
    
    public MediaService(MediaRepository mediaRepository, DownloadLogRepository downloadLogRepository) {
        this.mediaRepository = mediaRepository;
        this.downloadLogRepository = downloadLogRepository;
    }
    
    public PageResponse<Media> search(String keyword, Long categoryId, String fileType,
                                      String tag, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "uploadTime"));
        Page<Media> page = mediaRepository.search(keyword, categoryId, fileType, "published", pageable);
        return PageResponse.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }
    
    public Media getDetail(Long id) {
        return mediaRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new RuntimeException("素材不存在"));
    }
    
    public Map<String, Object> initUpload(UploadInitRequest request, User user) {
        String uploadId = "upload_" + System.currentTimeMillis();
        
        uploadContexts.put(uploadId, new UploadContext(
            request.getFileName(),
            request.getFileSize(),
            request.getFileType(),
            request.getMd5Hash(),
            request.getCategoryId(),
            user.getId(),
            request.getDescription()
        ));
        
        return Map.of(
            "uploadId", uploadId,
            "chunkSize", 5 * 1024 * 1024
        );
    }
    
    @Transactional
    public Media completeUpload(UploadCompleteRequest request, User user) {
        UploadContext context = uploadContexts.remove(request.getUploadId());
        if (context == null) {
            throw new RuntimeException("上传上下文不存在");
        }
        
        String fileType = context.fileType();
        String storagePath = "/uploads/" + fileType + "/" + context.fileName();
        
        Media media = new Media();
        media.setFileName(context.fileName());
        media.setFilePath(storagePath);
        media.setFileSize(context.fileSize());
        media.setFileType(fileType);
        media.setThumbnailUrl(request.getThumbnailUrl());
        media.setCategoryId(context.categoryId());
        media.setStoragePath(storagePath);
        media.setMd5Hash(context.md5Hash());
        media.setAiTags("[\"AI识别标签1\", \"AI识别标签2\", \"AI识别标签3\"]");
        media.setDescription(request.getDescription() != null ? request.getDescription() : context.description());
        media.setCopyrightInfo(request.getCopyrightInfo());
        media.setUploadUserId(user.getId());
        media.setStatus("pending");
        media.setAuditStatus("pending");
        
        return mediaRepository.save(media);
    }
    
    @Transactional
    public Map<String, Object> getDownloadUrl(Long id, User user, HttpServletRequest request) {
        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("素材不存在"));
        
        media.setDownloadCount(media.getDownloadCount() + 1);
        mediaRepository.save(media);
        
        DownloadLog log = new DownloadLog();
        log.setMediaId(id);
        log.setUserId(user.getId());
        log.setClientIp(getClientIp(request));
        log.setUserAgent(request.getHeader("User-Agent"));
        downloadLogRepository.save(log);
        
        return Map.of(
            "downloadUrl", media.getFilePath(),
            "fileName", media.getFileName()
        );
    }
    
    @Transactional
    public void deleteMedia(Long id) {
        if (!mediaRepository.existsById(id)) {
            throw new RuntimeException("素材不存在");
        }
        mediaRepository.deleteById(id);
    }
    
    @Transactional
    public void batchDeleteMedia(Long[] ids) {
        for (Long id : ids) {
            mediaRepository.deleteById(id);
        }
    }
    
    @Transactional
    public Media updateMedia(Long id, MediaUpdateRequest request) {
        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("素材不存在"));
        
        if (request.getFileName() != null) {
            media.setFileName(request.getFileName());
        }
        if (request.getCategoryId() != null) {
            media.setCategoryId(request.getCategoryId());
        }
        if (request.getDescription() != null) {
            media.setDescription(request.getDescription());
        }
        if (request.getCopyrightInfo() != null) {
            media.setCopyrightInfo(request.getCopyrightInfo());
        }
        if (request.getStatus() != null) {
            media.setStatus(request.getStatus());
        }
        
        return mediaRepository.save(media);
    }
    
    public PageResponse<Media> getPendingAudit(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "uploadTime"));
        Page<Media> page = mediaRepository.findByAuditStatus("pending", pageable);
        return PageResponse.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }
    
    @Transactional
    public Media auditMedia(Long id, AuditRequest request, User user) {
        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("素材不存在"));
        
        media.setAuditStatus(request.getAuditStatus());
        media.setAuditComment(request.getAuditComment());
        media.setAuditUserId(user.getId());
        media.setAuditTime(LocalDateTime.now());
        
        if ("approved".equals(request.getAuditStatus())) {
            media.setStatus("published");
        } else if ("rejected".equals(request.getAuditStatus())) {
            media.setStatus("rejected");
        }
        
        return mediaRepository.save(media);
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
    
    private record UploadContext(
        String fileName,
        Long fileSize,
        String fileType,
        String md5Hash,
        Long categoryId,
        Long userId,
        String description
    ) {}
}
