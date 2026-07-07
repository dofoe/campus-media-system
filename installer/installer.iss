; Inno Setup Script for Campus Media Management System
; Windows One-Click Installer
; Download Inno Setup: https://jrsoftware.org/isdl.php

#define MyAppName "Campus Media System"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "Campus Media System"
#define MyAppURL "https://github.com/dofoe/campus-media-system"

[Setup]
AppId={{8E3F9A4B-1C5D-4F2A-9B3E-7D8C6F4A2B1C}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=yes
OutputDir=Output
OutputBaseFilename=CampusMediaSystemSetup-{#MyAppVersion}
Compression=lzma
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "Create desktop icon"; GroupDescription: "Additional icons"; Flags: unchecked
Name: "service"; Description: "Register as Windows Service (auto-start)"; GroupDescription: "Additional tasks"; Flags: checked

[Files]
Source: "app.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "start.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "stop.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "install-service.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "uninstall-service.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "winsw.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "CampusMediaService.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "CampusMediaService.xml"; DestDir: "{app}"; Flags: ignoreversion
Source: "README.txt"; DestDir: "{app}"; Flags: ignoreversion
Source: "runtime\*"; DestDir: "{app}\runtime"; Flags: ignoreversion recursesubdirs createallsubdirs

[Dirs]
Name: "{app}\data"
Name: "{app}\logs"
Name: "{app}\uploads"

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\start.bat"
Name: "{group}\Uninstall {#MyAppName}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\start.bat"; Tasks: desktopicon

[Run]
Filename: "{app}\start.bat"; Description: "Launch {#MyAppName}"; Flags: nowait postinstall skipifsilent
Filename: "{app}\install-service.bat"; Tasks: service; Flags: runhidden

[UninstallRun]
Filename: "{app}\uninstall-service.bat"; Flags: runhidden

[UninstallDelete]
Type: filesandordirs; Name: "{app}\logs"
Type: filesandordirs; Name: "{app}\uploads"
