package com.campus.media.config;

import com.campus.media.entity.*;
import com.campus.media.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
                               DepartmentRepository departmentRepository,
                               CategoryRepository categoryRepository,
                               UserRepository userRepository,
                               TagRepository tagRepository,
                               MediaRepository mediaRepository,
                               AuditRuleRepository auditRuleRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.count() > 0) {
                return;
            }

            LocalDateTime now = LocalDateTime.now();

            Role adminRole = createRole(1L, "超级管理员", "admin", "系统超级管理员，拥有所有权限", "all", now);
            Role deptAdminRole = createRole(2L, "部门管理员", "dept_admin", "部门管理员，管理本部门素材", "dept", now);
            Role userRole = createRole(3L, "普通用户", "user", "普通用户，可上传和下载素材", "self", now);
            roleRepository.saveAll(List.of(adminRole, deptAdminRole, userRole));

            Department dept1 = createDept(1L, "宣传部", null, 1, now);
            Department dept2 = createDept(2L, "计算机学院", null, 2, now);
            Department dept3 = createDept(3L, "文学院", null, 3, now);
            Department dept4 = createDept(4L, "商学院", null, 4, now);
            Department dept5 = createDept(5L, "理工学院", null, 5, now);
            departmentRepository.saveAll(List.of(dept1, dept2, dept3, dept4, dept5));

            Category cat1 = createCategory(1L, "校园活动", null, 1, now);
            Category cat2 = createCategory(2L, "校园风景", null, 2, now);
            Category cat3 = createCategory(3L, "师生风采", null, 3, now);
            Category cat4 = createCategory(4L, "新闻资讯", null, 4, now);
            Category cat5 = createCategory(5L, "教学科研", null, 5, now);
            Category cat6 = createCategory(6L, "会议资料", null, 6, now);
            Category cat7 = createCategory(7L, "开学典礼", 1L, 1, now);
            Category cat8 = createCategory(8L, "毕业典礼", 1L, 2, now);
            Category cat9 = createCategory(9L, "运动会", 1L, 3, now);
            Category cat10 = createCategory(10L, "实验室", 5L, 1, now);
            Category cat11 = createCategory(11L, "科研成果", 5L, 2, now);
            Category cat12 = createCategory(12L, "奖学金", 3L, 1, now);
            Category cat13 = createCategory(13L, "优秀教师", 3L, 2, now);
            Category cat14 = createCategory(14L, "建筑景观", 2L, 1, now);
            categoryRepository.saveAll(List.of(cat1, cat2, cat3, cat4, cat5, cat6, cat7, cat8, cat9, cat10, cat11, cat12, cat13, cat14));

            String pw = passwordEncoder.encode("admin");
            User admin = createUser(1L, "admin", pw, "系统管理员", 1L, 1L, "admin@campus.edu.cn", "13800138000", "all", now);
            User deptAdmin = createUser(2L, "dept_admin1", pw, "张主任", 2L, 2L, "zhang@campus.edu.cn", "13900139001", "dept", now);
            User user1 = createUser(3L, "user1", pw, "王小明", 2L, 3L, "wang1@campus.edu.cn", "13800138001", "self", now);
            userRepository.saveAll(List.of(admin, deptAdmin, user1));

            Tag tag1 = createTag(1L, "校园", "manual", "#409eff", 0, now);
            Tag tag2 = createTag(2L, "宣传", "manual", "#67c23a", 0, now);
            Tag tag3 = createTag(3L, "活动", "manual", "#e6a23c", 0, now);
            Tag tag4 = createTag(4L, "风景", "manual", "#f56c6c", 0, now);
            Tag tag5 = createTag(5L, "人物", "manual", "#909399", 0, now);
            Tag tag6 = createTag(6L, "新闻", "manual", "#409eff", 0, now);
            Tag tag7 = createTag(7L, "教学", "manual", "#67c23a", 0, now);
            Tag tag8 = createTag(8L, "会议", "manual", "#e6a23c", 0, now);
            tagRepository.saveAll(List.of(tag1, tag2, tag3, tag4, tag5, tag6, tag7, tag8));

            String thumb = "https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=";
            Media m1 = createMedia(1L, "校园运动会开幕.jpg", "/uploads/image/1.jpg", 2048576L, "image",
                thumb + "campus%20sports%20event&image_size=square", 1L,
                "abc123def456ghi789jkl012mno345pq", "[\"运动\", \"校园\", \"活动\"]",
                "2024年校园运动会开幕式精彩瞬间", "校园宣传部 © 2024", 156, 320, 1L, now);
            Media m2 = createMedia(2L, "图书馆春景.jpg", "/uploads/image/2.jpg", 3145728L, "image",
                thumb + "campus%20library%20spring&image_size=square", 2L,
                "def456ghi789jkl012mno345pqr678st", "[\"校园\", \"风景\", \"建筑\"]",
                "图书馆前樱花盛开，春意盎然", "校园宣传部 © 2024", 89, 178, 1L, now);
            Media m3 = createMedia(3L, "优秀教师表彰.jpg", "/uploads/image/3.jpg", 1572864L, "image",
                thumb + "teacher%20award%20ceremony&image_size=square", 3L,
                "ghi789jkl012mno345pqr678stu901vw", "[\"教师\", \"表彰\", \"荣誉\"]",
                "2024年度优秀教师表彰大会", "人事处 © 2024", 234, 512, 1L, now);
            Media m4 = createMedia(4L, "科研实验室.jpg", "/uploads/image/4.jpg", 2621440L, "image",
                thumb + "research%20laboratory&image_size=square", 10L,
                "jkl012mno345pqr678stu901vwx234yz", "[\"科研\", \"实验室\", \"教学\"]",
                "学生们在实验室进行科研工作", "教务处 © 2024", 67, 145, 1L, now);
            Media m5 = createMedia(5L, "校园航拍全景.mp4", "/uploads/video/5.mp4", 52428800L, "video",
                thumb + "campus%20aerial%20view&image_size=square", 2L,
                "mno345pqr678stu901vwx234yza567bc", "[\"校园\", \"航拍\", \"风景\"]",
                "校园全景航拍视频", "宣传部 © 2024", 45, 89, 1L, now);
            mediaRepository.saveAll(List.of(m1, m2, m3, m4, m5));

            AuditRule r1 = createRule(1L, "运动会素材自动分类", "keyword",
                "{\"field\":\"tags\",\"operator\":\"contains\",\"value\":\"运动会\"}",
                "categorize", "自动将包含运动会标签的素材归入校园活动分类", 1L, now);
            AuditRule r2 = createRule(2L, "奖学金素材自动分类", "keyword",
                "{\"field\":\"tags\",\"operator\":\"contains\",\"value\":\"奖学金\"}",
                "categorize", "自动将包含奖学金标签的素材归入荣誉表彰分类", 1L, now);
            AuditRule r3 = createRule(3L, "敏感内容人工审核", "keyword",
                "{\"field\":\"content\",\"operator\":\"contains\",\"value\":\"敏感词\"}",
                "manual_review", "标记需要人工复核的内容", 1L, now);
            auditRuleRepository.saveAll(List.of(r1, r2, r3));
        };
    }

    private Role createRole(Long id, String name, String code, String desc, String scope, LocalDateTime now) {
        Role r = new Role();
        r.setId(id);
        r.setName(name);
        r.setCode(code);
        r.setDescription(desc);
        r.setDataScope(scope);
        r.setStatus(true);
        r.setCreatedAt(now);
        r.setUpdatedAt(now);
        return r;
    }

    private Department createDept(Long id, String name, Long parentId, int sort, LocalDateTime now) {
        Department d = new Department();
        d.setId(id);
        d.setName(name);
        d.setParentId(parentId);
        d.setSortOrder(sort);
        d.setStatus(true);
        d.setCreatedAt(now);
        d.setUpdatedAt(now);
        return d;
    }

    private Category createCategory(Long id, String name, Long parentId, int sort, LocalDateTime now) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        c.setParentId(parentId);
        c.setSortOrder(sort);
        c.setStatus(true);
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        return c;
    }

    private User createUser(Long id, String username, String password, String name, Long deptId, Long roleId,
                            String email, String phone, String scope, LocalDateTime now) {
        User u = new User();
        u.setId(id);
        u.setUsername(username);
        u.setPassword(password);
        u.setName(name);
        u.setDeptId(deptId);
        u.setRoleId(roleId);
        u.setEmail(email);
        u.setPhone(phone);
        u.setStatus(true);
        u.setDataScope(scope);
        u.setCreatedAt(now);
        u.setUpdatedAt(now);
        return u;
    }

    private Tag createTag(Long id, String name, String type, String color, int count, LocalDateTime now) {
        Tag t = new Tag();
        t.setId(id);
        t.setName(name);
        t.setType(type);
        t.setColor(color);
        t.setCount(count);
        t.setCreatedAt(now);
        return t;
    }

    private Media createMedia(Long id, String fileName, String filePath, Long fileSize, String fileType,
                              String thumb, Long categoryId, String md5, String aiTags,
                              String description, String copyright, int usageCount, int downloadCount,
                              Long userId, LocalDateTime now) {
        Media m = new Media();
        m.setId(id);
        m.setFileName(fileName);
        m.setFilePath(filePath);
        m.setFileSize(fileSize);
        m.setFileType(fileType);
        m.setThumbnailUrl(thumb);
        m.setCategoryId(categoryId);
        m.setStoragePath(filePath);
        m.setMd5Hash(md5);
        m.setAiTags(aiTags);
        m.setDescription(description);
        m.setCopyrightInfo(copyright);
        m.setUsageCount(usageCount);
        m.setDownloadCount(downloadCount);
        m.setStatus("published");
        m.setAuditStatus("approved");
        m.setUploadUserId(userId);
        m.setUploadTime(now);
        m.setUpdatedAt(now);
        return m;
    }

    private AuditRule createRule(Long id, String name, String type, String condition, String action,
                                 String description, Long createdBy, LocalDateTime now) {
        AuditRule r = new AuditRule();
        r.setId(id);
        r.setName(name);
        r.setType(type);
        r.setCondition(condition);
        r.setAction(action);
        r.setDescription(description);
        r.setStatus(true);
        r.setCreatedBy(createdBy);
        r.setCreatedAt(now);
        r.setUpdatedAt(now);
        return r;
    }
}
