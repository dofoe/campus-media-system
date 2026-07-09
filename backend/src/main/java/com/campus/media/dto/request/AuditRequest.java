package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuditRequest {
    
    @NotBlank(message = "审核状态不能为空")
    private String auditStatus;
    
    private String auditComment;
    
    public AuditRequest() {
    }
    
    public String getAuditStatus() {
        return auditStatus;
    }
    
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    
    public String getAuditComment() {
        return auditComment;
    }
    
    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
}