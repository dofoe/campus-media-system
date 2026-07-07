package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditRequest {
    
    @NotBlank(message = "审核状态不能为空")
    private String auditStatus;
    
    private String auditComment;
}
