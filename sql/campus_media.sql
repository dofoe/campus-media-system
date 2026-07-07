CREATE TABLE IF NOT EXISTS departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    parent_id INT DEFAULT NULL,
    sort_order INT DEFAULT 0,
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    data_scope VARCHAR(20) DEFAULT 'self',
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    dept_id INT,
    role_id INT NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar_url VARCHAR(500),
    status BOOLEAN DEFAULT TRUE,
    data_scope VARCHAR(20) DEFAULT 'self',
    last_login_time TIMESTAMP,
    last_login_ip VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES departments(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    parent_id INT DEFAULT NULL,
    sort_order INT DEFAULT 0,
    icon VARCHAR(100),
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS tags (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(20) DEFAULT 'manual',
    color VARCHAR(20) DEFAULT '#409eff',
    count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS media (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(500) NOT NULL,
    file_path VARCHAR(1000) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    thumbnail_url VARCHAR(1000),
    category_id INT,
    storage_path VARCHAR(1000) NOT NULL,
    md5_hash VARCHAR(32) NOT NULL,
    ai_tags JSONB DEFAULT '[]',
    description TEXT,
    copyright_info TEXT,
    usage_count INT DEFAULT 0,
    download_count INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'pending',
    audit_status VARCHAR(20) DEFAULT 'pending',
    audit_comment TEXT,
    audit_user_id INT,
    audit_time TIMESTAMP,
    upload_user_id INT NOT NULL,
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (audit_user_id) REFERENCES users(id),
    FOREIGN KEY (upload_user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS media_tags (
    id SERIAL PRIMARY KEY,
    media_id INT NOT NULL,
    tag_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (media_id) REFERENCES media(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id),
    UNIQUE (media_id, tag_id)
);

CREATE TABLE IF NOT EXISTS download_logs (
    id SERIAL PRIMARY KEY,
    media_id INT NOT NULL,
    user_id INT NOT NULL,
    download_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_ip VARCHAR(50),
    user_agent TEXT,
    FOREIGN KEY (media_id) REFERENCES media(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS operation_logs (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    operation VARCHAR(50) NOT NULL,
    target VARCHAR(200),
    target_id INT,
    detail TEXT,
    client_ip VARCHAR(50),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS audit_rules (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) DEFAULT 'keyword',
    condition JSONB NOT NULL,
    action VARCHAR(20) DEFAULT 'reject',
    description TEXT,
    status BOOLEAN DEFAULT TRUE,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

INSERT INTO roles (name, code, description, data_scope, status) VALUES
('超级管理员', 'admin', '系统超级管理员，拥有所有权限', 'all', TRUE),
('部门管理员', 'dept_admin', '部门管理员，管理本部门素材', 'dept', TRUE),
('普通用户', 'user', '普通用户，可上传和下载素材', 'self', TRUE);

INSERT INTO departments (name, parent_id, sort_order, status) VALUES
('宣传部', NULL, 1, TRUE),
('计算机学院', NULL, 2, TRUE),
('文学院', NULL, 3, TRUE),
('商学院', NULL, 4, TRUE),
('理工学院', NULL, 5, TRUE);

INSERT INTO categories (name, parent_id, sort_order, status) VALUES
('校园活动', NULL, 1, TRUE),
('校园风景', NULL, 2, TRUE),
('师生风采', NULL, 3, TRUE),
('新闻资讯', NULL, 4, TRUE),
('教学科研', NULL, 5, TRUE),
('会议资料', NULL, 6, TRUE);

INSERT INTO users (username, password, name, dept_id, role_id, email, phone, status, data_scope) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '系统管理员', 1, 1, 'admin@campus.edu.cn', '13800138000', TRUE, 'all');
