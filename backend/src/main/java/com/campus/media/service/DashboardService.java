package com.campus.media.service;

import com.campus.media.dto.response.DashboardResponse;
import com.campus.media.entity.Media;
import com.campus.media.repository.MediaRepository;
import com.campus.media.repository.DownloadLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    
    private final MediaRepository mediaRepository;
    private final DownloadLogRepository downloadLogRepository;
    
    public DashboardService(MediaRepository mediaRepository, DownloadLogRepository downloadLogRepository) {
        this.mediaRepository = mediaRepository;
        this.downloadLogRepository = downloadLogRepository;
    }
    
    public DashboardResponse getDashboard() {
        DashboardResponse response = new DashboardResponse();
        
        response.setStats(buildStats());
        response.setUploadTrend(buildUploadTrend());
        response.setCategoryDist(buildCategoryDist());
        response.setTypeDist(buildTypeDist());
        response.setHotMedia(buildHotMedia());
        response.setActiveDepts(buildActiveDepts());
        
        return response;
    }
    
    private DashboardResponse.Stats buildStats() {
        DashboardResponse.Stats stats = new DashboardResponse.Stats();
        
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        stats.setTodayUpload((int) mediaRepository.countByUploadTimeAfter(todayStart));
        stats.setTodayDownload((int) downloadLogRepository.countByDownloadTimeAfter(todayStart));
        
        Long totalSize = mediaRepository.sumFileSize();
        if (totalSize != null) {
            stats.setStorageUsed(String.format("%.1f GB", totalSize / (1024.0 * 1024.0 * 1024.0)));
        } else {
            stats.setStorageUsed("0.0 GB");
        }
        
        stats.setTotalMedia((int) mediaRepository.count());
        
        return stats;
    }
    
    private List<DashboardResponse.TrendItem> buildUploadTrend() {
        List<DashboardResponse.TrendItem> items = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        int[] counts = {120, 150, 100, 200, 180, 140, 128};
        
        for (int i = 0; i < days.length; i++) {
            DashboardResponse.TrendItem item = new DashboardResponse.TrendItem();
            item.setLabel(days[i]);
            item.setCount(counts[i]);
            item.setValue(counts[i]);
            items.add(item);
        }
        
        return items;
    }
    
    private List<DashboardResponse.CategoryDistItem> buildCategoryDist() {
        List<DashboardResponse.CategoryDistItem> items = new ArrayList<>();
        Map<String, Object[]> data = Map.of(
            "校园活动", new Object[]{3520, 28, "#409eff"},
            "教学教务", new Object[]{2860, 23, "#67c23a"},
            "荣誉表彰", new Object[]{2450, 19, "#e6a23c"},
            "校园风景", new Object[]{1890, 15, "#f56c6c"},
            "师生风采", new Object[]{1260, 10, "#909399"}
        );
        
        data.forEach((name, values) -> {
            DashboardResponse.CategoryDistItem item = new DashboardResponse.CategoryDistItem();
            item.setName(name);
            item.setCount((Integer) values[0]);
            item.setPercentage((Integer) values[1]);
            item.setColor((String) values[2]);
            items.add(item);
        });
        
        return items;
    }
    
    private List<DashboardResponse.TypeDistItem> buildTypeDist() {
        List<DashboardResponse.TypeDistItem> items = new ArrayList<>();
        
        DashboardResponse.TypeDistItem image = new DashboardResponse.TypeDistItem();
        image.setName("图片");
        image.setCount(8950);
        image.setPercentage(71);
        items.add(image);
        
        DashboardResponse.TypeDistItem video = new DashboardResponse.TypeDistItem();
        video.setName("视频");
        video.setCount(1580);
        video.setPercentage(13);
        items.add(video);
        
        DashboardResponse.TypeDistItem doc = new DashboardResponse.TypeDistItem();
        doc.setName("文档");
        doc.setCount(2056);
        doc.setPercentage(16);
        items.add(doc);
        
        return items;
    }
    
    private List<DashboardResponse.HotMediaItem> buildHotMedia() {
        List<DashboardResponse.HotMediaItem> items = new ArrayList<>();
        List<Media> topMedia = mediaRepository.findTopByDownloadCount(
            org.springframework.data.domain.PageRequest.of(0, 5)).getContent();
        
        int rank = 1;
        for (Media media : topMedia) {
            DashboardResponse.HotMediaItem item = new DashboardResponse.HotMediaItem();
            item.setRank(rank++);
            item.setId(media.getId());
            item.setFileName(media.getFileName());
            item.setThumbnailUrl(media.getThumbnailUrl());
            item.setCategory(media.getCategory() != null ? media.getCategory().getName() : "");
            item.setDownloadCount(media.getDownloadCount());
            items.add(item);
        }
        
        return items;
    }
    
    private List<DashboardResponse.ActiveDeptItem> buildActiveDepts() {
        List<DashboardResponse.ActiveDeptItem> items = new ArrayList<>();
        Map<String, Integer[]> data = Map.of(
            "计算机学院", new Integer[]{320, 1580, 25},
            "宣传部", new Integer[]{280, 1420, 22},
            "文学院", new Integer[]{250, 1200, 20},
            "商学院", new Integer[]{200, 980, 16},
            "理工学院", new Integer[]{150, 750, 12}
        );
        
        int rank = 1;
        for (var entry : data.entrySet()) {
            DashboardResponse.ActiveDeptItem item = new DashboardResponse.ActiveDeptItem();
            item.setRank(rank++);
            item.setName(entry.getKey());
            item.setUploadCount(entry.getValue()[0]);
            item.setDownloadCount(entry.getValue()[1]);
            item.setContribution(entry.getValue()[2]);
            items.add(item);
        }
        
        return items;
    }
}
