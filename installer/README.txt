========================================
校园宣传素材智能管理系统
Campus Media Management System v1.0.0
========================================

【快速开始】

1. 双击 "start.bat" 启动系统
2. 浏览器访问: http://localhost:8080
3. 默认账号: admin
4. 默认密码: admin

【注册为Windows服务】

1. 右键 "install-service.bat" → 以管理员身份运行
2. 服务将自动启动并设为开机自启

【卸载Windows服务】

1. 右键 "uninstall-service.bat" → 以管理员身份运行

【目录说明】

- app.jar          应用程序包
- runtime/         Java运行环境
- data/            数据库文件
- logs/            日志文件
- uploads/         上传文件
- winsw.exe        Windows服务管理工具
- start.bat        启动脚本
- stop.bat         停止脚本
- install-service.bat    安装服务
- uninstall-service.bat  卸载服务

【常见问题】

Q: 启动失败，提示端口被占用？
A: 修改端口或停止占用8080端口的程序

Q: 忘记管理员密码？
A: 删除 data 目录下的数据库文件，重新启动系统

Q: 数据如何备份？
A: 复制 data 和 uploads 目录到安全位置即可

【技术支持】

GitHub: https://github.com/dofoe/campus-media-system

========================================
