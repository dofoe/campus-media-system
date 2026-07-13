package com.campus.media.dto.response;

import java.util.List;

public class DashboardResponse {

    private Stats stats;
    private List<TrendItem> uploadTrend;
    private List<CategoryDistItem> categoryDist;
    private List<TypeDistItem> typeDist;
    private List<HotMediaItem> hotMedia;
    private List<ActiveDeptItem> activeDepts;

    public DashboardResponse() {}

    public Stats getStats() { return stats; }
    public void setStats(Stats stats) { this.stats = stats; }

    public List<TrendItem> getUploadTrend() { return uploadTrend; }
    public void setUploadTrend(List<TrendItem> uploadTrend) { this.uploadTrend = uploadTrend; }

    public List<CategoryDistItem> getCategoryDist() { return categoryDist; }
    public void setCategoryDist(List<CategoryDistItem> categoryDist) { this.categoryDist = categoryDist; }

    public List<TypeDistItem> getTypeDist() { return typeDist; }
    public void setTypeDist(List<TypeDistItem> typeDist) { this.typeDist = typeDist; }

    public List<HotMediaItem> getHotMedia() { return hotMedia; }
    public void setHotMedia(List<HotMediaItem> hotMedia) { this.hotMedia = hotMedia; }

    public List<ActiveDeptItem> getActiveDepts() { return activeDepts; }
    public void setActiveDepts(List<ActiveDeptItem> activeDepts) { this.activeDepts = activeDepts; }

    public static class Stats {
        private Integer todayUpload;
        private Integer todayDownload;
        private String storageUsed;
        private Integer totalMedia;

        public Stats() {}

        public Integer getTodayUpload() { return todayUpload; }
        public void setTodayUpload(Integer todayUpload) { this.todayUpload = todayUpload; }

        public Integer getTodayDownload() { return todayDownload; }
        public void setTodayDownload(Integer todayDownload) { this.todayDownload = todayDownload; }

        public String getStorageUsed() { return storageUsed; }
        public void setStorageUsed(String storageUsed) { this.storageUsed = storageUsed; }

        public Integer getTotalMedia() { return totalMedia; }
        public void setTotalMedia(Integer totalMedia) { this.totalMedia = totalMedia; }
    }

    public static class TrendItem {
        private String label;
        private Integer count;
        private Integer value;

        public TrendItem() {}

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }

        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }

        public Integer getValue() { return value; }
        public void setValue(Integer value) { this.value = value; }
    }

    public static class CategoryDistItem {
        private String name;
        private Integer count;
        private Integer percentage;
        private String color;

        public CategoryDistItem() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }

        public Integer getPercentage() { return percentage; }
        public void setPercentage(Integer percentage) { this.percentage = percentage; }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
    }

    public static class TypeDistItem {
        private String name;
        private Integer count;
        private Integer percentage;

        public TypeDistItem() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }

        public Integer getPercentage() { return percentage; }
        public void setPercentage(Integer percentage) { this.percentage = percentage; }
    }

    public static class HotMediaItem {
        private Integer rank;
        private Long id;
        private String fileName;
        private String thumbnailUrl;
        private String category;
        private Integer downloadCount;

        public HotMediaItem() {}

        public Integer getRank() { return rank; }
        public void setRank(Integer rank) { this.rank = rank; }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getThumbnailUrl() { return thumbnailUrl; }
        public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public Integer getDownloadCount() { return downloadCount; }
        public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
    }

    public static class ActiveDeptItem {
        private Integer rank;
        private String name;
        private Integer uploadCount;
        private Integer downloadCount;
        private Integer contribution;

        public ActiveDeptItem() {}

        public Integer getRank() { return rank; }
        public void setRank(Integer rank) { this.rank = rank; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getUploadCount() { return uploadCount; }
        public void setUploadCount(Integer uploadCount) { this.uploadCount = uploadCount; }

        public Integer getDownloadCount() { return downloadCount; }
        public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }

        public Integer getContribution() { return contribution; }
        public void setContribution(Integer contribution) { this.contribution = contribution; }
    }
}
