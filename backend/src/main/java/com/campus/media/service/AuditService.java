package com.campus.media.service;

import com.campus.media.dto.response.PageResponse;
import com.campus.media.entity.DownloadLog;
import com.campus.media.entity.OperationLog;
import com.campus.media.repository.DownloadLogRepository;
import com.campus.media.repository.OperationLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    
    private final DownloadLogRepository downloadLogRepository;
    private final OperationLogRepository operationLogRepository;
    
    public AuditService(DownloadLogRepository downloadLogRepository, 
                        OperationLogRepository operationLogRepository) {
        this.downloadLogRepository = downloadLogRepository;
        this.operationLogRepository = operationLogRepository;
    }
    
    public PageResponse<DownloadLog> getDownloadLogs(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "downloadTime"));
        Page<DownloadLog> page = downloadLogRepository.search(keyword, pageable);
        return PageResponse.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }
    
    public PageResponse<OperationLog> getOperationLogs(String keyword, String operation, 
                                                        Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<OperationLog> page = operationLogRepository.search(keyword, operation, pageable);
        return PageResponse.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }
}
