# Microsoft Developer Studio Project File - Name="nawin32" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 5.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Application" 0x0101

CFG=nawin32 - Win32 Debug With Swing
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "nawin32.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "nawin32.mak" CFG="nawin32 - Win32 Debug With Swing"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "nawin32 - Win32 Release With Swing" (based on\
 "Win32 (x86) Application")
!MESSAGE "nawin32 - Win32 Debug With Swing" (based on\
 "Win32 (x86) Application")
!MESSAGE 

# Begin Project
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "nawin32 - Win32 Release With Swing"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release With Swing"
# PROP BASE Intermediate_Dir "Release With Swing"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release With Swing"
# PROP Intermediate_Dir "Release With Swing"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /YX /FD /c
# ADD CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "SWING_RELEASE" /YX /FD /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /o NUL /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /o NUL /win32
# ADD BASE RSC /l 0x809 /d "NDEBUG"
# ADD RSC /l 0x809 /d "NDEBUG" /d "SWING_RELEASE"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /machine:I386
# ADD LINK32 kernel32.lib user32.lib advapi32.lib /nologo /subsystem:windows /pdb:none /machine:I386 /out:"Release With Swing/newsagent.exe"

!ELSEIF  "$(CFG)" == "nawin32 - Win32 Debug With Swing"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "nawin32_"
# PROP BASE Intermediate_Dir "nawin32_"
# PROP BASE Ignore_Export_Lib 0
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug With Swing"
# PROP Intermediate_Dir "Debug With Swing"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /YX /FD /c
# ADD CPP /nologo /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "SWING_RELEASE" /YX /FD /c
# ADD BASE MTL /nologo /D "_DEBUG" /mktyplib203 /o NUL /win32
# ADD MTL /nologo /D "_DEBUG" /mktyplib203 /o NUL /win32
# ADD BASE RSC /l 0x809 /d "_DEBUG"
# ADD RSC /l 0x809 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /debug /machine:I386 /pdbtype:sept
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /debug /machine:I386 /out:"Debug With Swing/newsagent_g.exe" /pdbtype:sept

!ENDIF 

# Begin Target

# Name "nawin32 - Win32 Release With Swing"
# Name "nawin32 - Win32 Debug With Swing"
# Begin Source File

SOURCE=.\icon1.ico

!IF  "$(CFG)" == "nawin32 - Win32 Release With Swing"

# PROP Intermediate_Dir "Release With Swing"

!ELSEIF  "$(CFG)" == "nawin32 - Win32 Debug With Swing"

# PROP Intermediate_Dir "Debug With Swing"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\nawin32.c

!IF  "$(CFG)" == "nawin32 - Win32 Release With Swing"

# PROP Intermediate_Dir "Release With Swing"

!ELSEIF  "$(CFG)" == "nawin32 - Win32 Debug With Swing"

# PROP Intermediate_Dir "Debug With Swing"

!ENDIF 

# End Source File
# Begin Source File

SOURCE=.\resource.rc

!IF  "$(CFG)" == "nawin32 - Win32 Release With Swing"

# PROP Intermediate_Dir "Release With Swing"
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809

!ELSEIF  "$(CFG)" == "nawin32 - Win32 Debug With Swing"

# PROP Intermediate_Dir "Debug With Swing"
# ADD BASE RSC /l 0x809
# ADD RSC /l 0x809

!ENDIF 

# End Source File
# End Target
# End Project
