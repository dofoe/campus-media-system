========================================
Campus Media System v1.0.0
========================================

QUICK START:

1. Double click "start.bat" to launch the system
2. Open browser: http://localhost:8080
3. Login with: admin / admin

REGISTER AS WINDOWS SERVICE:

1. Right-click "install-service.bat"
2. Select "Run as administrator"
3. Service will auto-start on boot

UNINSTALL SERVICE:

1. Right-click "uninstall-service.bat"
2. Select "Run as administrator"

DIRECTORY STRUCTURE:
- app.jar          Application package
- runtime/         Java runtime environment
- data/            Database files
- logs/            Log files
- uploads/         Uploaded files
- winsw.exe        Windows service wrapper
- start.bat        Start script
- stop.bat         Stop script

COMMON ISSUES:

Q: Failed to start, port occupied?
A: Stop the program using port 8080 or modify the port

Q: Forgot admin password?
A: Delete files in data folder and restart

Q: How to backup data?
A: Copy data and uploads folders to safe location

SUPPORT:
https://github.com/dofoe/campus-media-system

========================================