[Setup]
AppName=Deimos
AppPublisher=Farkas Peter
AppVersion=1.0
DefaultDirName=C:\Games\Deimos
SetupIconFile=..\icon.ico
OutputBaseFilename=installer
OutputDir=build
Compression=lzma2
SolidCompression=yes

[Files]
Source: "build\deimos.exe"; DestDir: "{app}"

[Icons]
Name: "{commonprograms}\Deimos"; Filename: "{app}\deimos.exe"; WorkingDir: "{app}"
