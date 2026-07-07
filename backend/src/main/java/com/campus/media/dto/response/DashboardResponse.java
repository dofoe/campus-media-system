package com.campus.media.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DashboardResponse {
    
    private Stats stats;
    
    private List<TrendItem> uploadTrend;
    
    private List<CategoryDistItem> categoryDist;
    
    private List<TypeDistItem> typeDist;
    
    private List<HotMediaItem> hotMedia;
    
    private List<ActiveDeptItem> activeDepts;
    
    @Data
    public static class Stats {
        private Integer todayUpload;
        private Integer todayDownload;
        private String storageUsed;
        private Integer totalMedia;
    }
    
    @Data
    public static class TrendItem {
        private String label;
        private Integer count;
        private Integer value;
    }
    
    @Data
    public static class CategoryDistItem {
        private String name;
        private Integer count;
        private Integer percentage;
        private String color;
    }
    
    @Data
    public static class TypeDistItem {
        private String name;
        private Integer count;
        private Integer percentage;
    }
    
    @Data
    public static class HotMediaItem {
        private Integer rank;
        private Long id;
        private String fileName;
        private String thumbnailUrl;
        private String category;
        private Integer downloadCount;
    }
    
    @Data
    public static class ActiveDeptItem {
        private Integer rank;
        private String name;
        private Integer uploadCount;
        private Integer downloadCount;
        private Integer contribution;
    }
}
