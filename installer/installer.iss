; Inno Setup Script for Campus Media Management System
; 校园宣传素材智能管理系统 Windows 一键安装脚本
; 使用前请先下载 Inno Setup Compiler: https://jrsoftware.org/isdl.php

#define MyAppName "校园宣传素材智能管理系统"
#define MyAppNameEn "Campus Media Management System"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "Campus Media System"
#define MyAppURL "https://github.com/dofoe/campus-media-system"
#define MyAppExeName "start.bat"

[Setup]
; 应用基本信息
AppId={{8E3F9A4B-1C5D-4F2A-9B3E-7D8C6F4A2B1C}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=yes
LicenseFile=..\LICENSE
OutputDir=Output
OutputBaseFilename=CampusMediaSystemSetup-{#MyAppVersion}
SetupIconFile=app.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin
PrivilegesRequiredOverridesAllowed=dialog
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
UninstallDisplayIcon={app}\app.ico
UninstallDisplayName={#MyAppName}

[Languages]
Name: "chinesesimp"; MessagesFile: "compiler:Languages\ChineseSimplified.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "service"; Description: "注册为Windows服务（开机自启）"; GroupDescription: "其他任务:"; Flags: checked

[Files]
; 主程序JAR包
Source: "..\backend\target\campus-media-system-1.0.0.jar"; DestDir: "{app}"; DestName: "app.jar"; Flags: ignoreversion
; 启动脚本
Source: "start.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "stop.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "install-service.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "uninstall-service.bat"; DestDir: "{app}"; Flags: ignoreversion
; Windows服务工具
Source: "winsw.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "CampusMediaService.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "CampusMediaService.xml"; DestDir: "{app}"; Flags: ignoreversion
; 文档
Source: "README.txt"; DestDir: "{app}"; Flags: ignoreversion
Source: "使用说明.txt"; DestDir: "{app}"; Flags: ignoreversion
; 注意：runtime 目录（JRE）需手动复制或单独下载

[Dirs]
Name: "{app}\data"
Name: "{app}\logs"
Name: "{app}\uploads"

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\app.ico"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon; IconFilename: "{app}\app.ico"

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#MyAppName}}"; Flags: nowait postinstall skipifsilent
Filename: "{app}\install-service.bat"; Tasks: service; Flags: runhidden

[UninstallRun]
Filename: "{app}\uninstall-service.bat"; Flags: runhidden

[UninstallDelete]
Type: filesandordirs; Name: "{app}\logs"
Type: filesandordirs; Name: "{app}\uploads"
Type: filesandordirs; Name: "{app}\data"
; 注意：删除上述目录将清除所有业务数据

[Code]
// 检查是否已安装
function IsUpgrade(): Boolean;
var
  ResultCode: Integer;
begin
  Result := RegKeyExists(HKLM, 'SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\{#SetupSetting("AppId")}_is1');
end;

// 安装前检查端口
function InitializeSetup(): Boolean;
var
  ResultCode: Integer;
begin
  Result := True;
  if IsUpgrade() then
  begin
    if MsgBox('检测到已安装的版本，是否升级？' + #13#10 +
              '升级将保留现有数据。', mbConfirmation, MB_YESNO) = IDNO then
    begin
      Result := False;
    end;
  end;
end;
