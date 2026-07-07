-- H2数据库初始化脚本（嵌入式模式）

-- 角色数据
INSERT INTO roles (id, name, code, description, data_scope, status, created_at, updated_at) VALUES
(1, '超级管理员', 'admin', '系统超级管理员，拥有所有权限', 'all', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '部门管理员', 'dept_admin', '部门管理员，管理本部门素材', 'dept', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '普通用户', 'user', '普通用户，可上传和下载素材', 'self', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 部门数据
INSERT INTO departments (id, name, parent_id, sort_order, status, created_at, updated_at) VALUES
(1, '宣传部', NULL, 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '计算机学院', NULL, 2, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '文学院', NULL, 3, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '商学院', NULL, 4, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, '理工学院', NULL, 5, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 分类数据
INSERT INTO categories (id, name, parent_id, sort_order, icon, status, created_at, updated_at) VALUES
(1, '校园活动', NULL, 1, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '校园风景', NULL, 2, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '师生风采', NULL, 3, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '新闻资讯', NULL, 4, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, '教学科研', NULL, 5, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, '会议资料', NULL, 6, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, '开学典礼', 1, 1, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, '毕业典礼', 1, 2, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, '运动会', 1, 3, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, '实验室', 5, 1, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, '科研成果', 5, 2, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, '奖学金', 3, 1, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, '优秀教师', 3, 2, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, '建筑景观', 2, 1, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 用户数据（密码：admin，BCrypt加密后）
INSERT INTO users (id, username, password, name, dept_id, role_id, email, phone, status, data_scope, created_at, updated_at) VALUES
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '系统管理员', 1, 1, 'admin@campus.edu.cn', '13800138000', TRUE, 'all', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'dept_admin1', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '张主任', 2, 2, 'zhang@campus.edu.cn', '13900139001', TRUE, 'dept', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'user1', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '王小明', 2, 3, 'wang1@campus.edu.cn', '13800138001', TRUE, 'self', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 标签数据
INSERT INTO tags (id, name, type, color, count, created_at) VALUES
(1, '校园', 'manual', '#409eff', 0, CURRENT_TIMESTAMP),
(2, '宣传', 'manual', '#67c23a', 0, CURRENT_TIMESTAMP),
(3, '活动', 'manual', '#e6a23c', 0, CURRENT_TIMESTAMP),
(4, '风景', 'manual', '#f56c6c', 0, CURRENT_TIMESTAMP),
(5, '人物', 'manual', '#909399', 0, CURRENT_TIMESTAMP),
(6, '新闻', 'manual', '#409eff', 0, CURRENT_TIMESTAMP),
(7, '教学', 'manual', '#67c23a', 0, CURRENT_TIMESTAMP),
(8, '会议', 'manual', '#e6a23c', 0, CURRENT_TIMESTAMP);

-- 素材数据
INSERT INTO media (id, file_name, file_path, file_size, file_type, thumbnail_url, category_id, storage_path, md5_hash, ai_tags, description, copyright_info, usage_count, download_count, status, audit_status, upload_user_id, upload_time, updated_at) VALUES
(1, '校园运动会开幕.jpg', '/uploads/image/1.jpg', 2048576, 'image', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=campus%20sports%20event&image_size=square', 1, '/uploads/image/1.jpg', 'abc123def456ghi789jkl012mno345pq', '["运动", "校园", "活动"]', '2024年校园运动会开幕式精彩瞬间', '校园宣传部 © 2024', 156, 320, 'published', 'approved', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '图书馆春景.jpg', '/uploads/image/2.jpg', 3145728, 'image', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=campus%20library%20spring&image_size=square', 2, '/uploads/image/2.jpg', 'def456ghi789jkl012mno345pqr678st', '["校园", "风景", "建筑"]', '图书馆前樱花盛开，春意盎然', '校园宣传部 © 2024', 89, 178, 'published', 'approved', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '优秀教师表彰.jpg', '/uploads/image/3.jpg', 1572864, 'image', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=teacher%20award%20ceremony&image_size=square', 3, '/uploads/image/3.jpg', 'ghi789jkl012mno345pqr678stu901vw', '["教师", "表彰", "荣誉"]', '2024年度优秀教师表彰大会', '人事处 © 2024', 234, 512, 'published', 'approved', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '科研实验室.jpg', '/uploads/image/4.jpg', 2621440, 'image', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=research%20laboratory&image_size=square', 10, '/uploads/image/4.jpg', 'jkl012mno345pqr678stu901vwx234yz', '["科研", "实验室", "教学"]', '学生们在实验室进行科研工作', '教务处 © 2024', 67, 145, 'published', 'approved', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, '校园航拍全景.mp4', '/uploads/video/5.mp4', 52428800, 'video', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=campus%20aerial%20view&image_size=square', 2, '/uploads/video/5.mp4', 'mno345pqr678stu901vwx234yza567bc', '["校园", "航拍", "风景"]', '校园全景航拍视频', '宣传部 © 2024', 45, 89, 'published', 'approved', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 审核规则数据
INSERT INTO audit_rules (id, name, type, condition, action, description, status, created_by, created_at, updated_at) VALUES
(1, '运动会素材自动分类', 'keyword', '{"field":"tags","operator":"contains","value":"运动会"}', 'categorize', '自动将包含运动会标签的素材归入校园活动分类', TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '奖学金素材自动分类', 'keyword', '{"field":"tags","operator":"contains","value":"奖学金"}', 'categorize', '自动将包含奖学金标签的素材归入荣誉表彰分类', TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '敏感内容人工审核', 'keyword', '{"field":"content","operator":"contains","value":"敏感词"}', 'manual_review', '标记需要人工复核的内容', TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 重置H2序列
ALTER TABLE roles ALTER COLUMN id RESTART WITH 100;
ALTER TABLE departments ALTER COLUMN id RESTART WITH 100;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 100;
ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tags ALTER COLUMN id RESTART WITH 100;
ALTER TABLE media ALTER COLUMN id RESTART WITH 100;
ALTER TABLE audit_rules ALTER COLUMN id RESTART WITH 100;
