# Microsoft Developer Studio Generated NMAKE File, Based on nawin32.dsp
!IF "$(CFG)" == ""
CFG=nawin32 - Win32 Debug With Swing
!MESSAGE No configuration specified. Defaulting to nawin32 - Win32 Debug With\
 Swing.
!ENDIF 

!IF "$(CFG)" != "nawin32 - Win32 Release With Swing" && "$(CFG)" !=\
 "nawin32 - Win32 Debug With Swing"
!MESSAGE Invalid configuration "$(CFG)" specified.
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
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "nawin32 - Win32 Release With Swing"

OUTDIR=.\Release With Swing
INTDIR=.\Release With Swing
# Begin Custom Macros
OutDir=.\Release With Swing
# End Custom Macros

!IF "$(RECURSE)" == "0" 

ALL : "$(OUTDIR)\newsagent.exe"

!ELSE 

ALL : "$(OUTDIR)\newsagent.exe"

!ENDIF 

CLEAN :
	-@erase "$(INTDIR)\vc50.idb"
	-@erase "$(OUTDIR)\newsagent.exe"
	-@erase ".\Release With Swing\nawin32.obj"
	-@erase ".\Release With Swing\resource.res"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP_PROJ=/nologo /ML /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D\
 "SWING_RELEASE" /Fp"$(INTDIR)\nawin32.pch" /YX /Fo"$(INTDIR)\\"\
 /Fd"$(INTDIR)\\" /FD /c 
CPP_OBJS=".\Release With Swing/"
CPP_SBRS=.
MTL_PROJ=/nologo /D "NDEBUG" /mktyplib203 /o NUL /win32 
RSC_PROJ=/l 0x809 /fo"$(INTDIR)\resource.res" /d "NDEBUG" /d "SWING_RELEASE" 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\nawin32.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=kernel32.lib user32.lib advapi32.lib /nologo /subsystem:windows\
 /pdb:none /machine:I386 /out:"$(OUTDIR)\newsagent.exe" 
LINK32_OBJS= \
	".\Release With Swing\nawin32.obj" \
	".\Release With Swing\resource.res"

"$(OUTDIR)\newsagent.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "nawin32 - Win32 Debug With Swing"

OUTDIR=.\Debug With Swing
INTDIR=.\Debug With Swing
# Begin Custom Macros
OutDir=.\Debug With Swing
# End Custom Macros

!IF "$(RECURSE)" == "0" 

ALL : "$(OUTDIR)\newsagent_g.exe"

!ELSE 

ALL : "$(OUTDIR)\newsagent_g.exe"

!ENDIF 

CLEAN :
	-@erase "$(INTDIR)\vc50.idb"
	-@erase "$(INTDIR)\vc50.pdb"
	-@erase "$(OUTDIR)\newsagent_g.exe"
	-@erase "$(OUTDIR)\newsagent_g.ilk"
	-@erase "$(OUTDIR)\newsagent_g.pdb"
	-@erase ".\Debug With Swing\nawin32.obj"
	-@erase ".\Debug With Swing\resource.res"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP_PROJ=/nologo /MLd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS"\
 /D "SWING_RELEASE" /Fp"$(INTDIR)\nawin32.pch" /YX /Fo"$(INTDIR)\\"\
 /Fd"$(INTDIR)\\" /FD /c 
CPP_OBJS=".\Debug With Swing/"
CPP_SBRS=.
MTL_PROJ=/nologo /D "_DEBUG" /mktyplib203 /o NUL /win32 
RSC_PROJ=/l 0x809 /fo"$(INTDIR)\resource.res" /d "_DEBUG" 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\nawin32.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib\
 advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib\
 odbccp32.lib /nologo /subsystem:windows /incremental:yes\
 /pdb:"$(OUTDIR)\newsagent_g.pdb" /debug /machine:I386\
 /out:"$(OUTDIR)\newsagent_g.exe" /pdbtype:sept 
LINK32_OBJS= \
	".\Debug With Swing\nawin32.obj" \
	".\Debug With Swing\resource.res"

"$(OUTDIR)\newsagent_g.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 

.c{$(CPP_OBJS)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(CPP_OBJS)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(CPP_OBJS)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(CPP_SBRS)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(CPP_SBRS)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(CPP_SBRS)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<


!IF "$(CFG)" == "nawin32 - Win32 Release With Swing" || "$(CFG)" ==\
 "nawin32 - Win32 Debug With Swing"
INTDIR_SRC=.\Release With Swing
SOURCE=.\nawin32.c
INTDIR_SRC=.\Release With Swing

!IF  "$(CFG)" == "nawin32 - Win32 Release With Swing"

CPP_SWITCHES=/nologo /ML /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D\
 "SWING_RELEASE" /Fp"$(INTDIR_SRC)\nawin32.pch" /YX /Fo"$(INTDIR_SRC)\\"\
 /Fd"$(INTDIR_SRC)\\" /FD /c 

".\Release With Swing\nawin32.obj" : $(SOURCE) "$(INTDIR_SRC)"
	$(CPP) @<<
  $(CPP_SWITCHES) $(SOURCE)
<<


!ELSEIF  "$(CFG)" == "nawin32 - Win32 Debug With Swing"

CPP_SWITCHES=/nologo /MLd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D\
 "_WINDOWS" /D "SWING_RELEASE" /Fp"$(INTDIR_SRC)\nawin32.pch" /YX\
 /Fo"$(INTDIR_SRC)\\" /Fd"$(INTDIR_SRC)\\" /FD /c 

".\Debug With Swing\nawin32.obj" : $(SOURCE) "$(INTDIR_SRC)"
	$(CPP) @<<
  $(CPP_SWITCHES) $(SOURCE)
<<


!ENDIF 

SOURCE=.\resource.rc
DEP_RSC_RESOU=\
	".\icon1.ico"\
	
INTDIR_SRC=.\Release With Swing

!IF  "$(CFG)" == "nawin32 - Win32 Release With Swing"


".\Release With Swing\resource.res" : $(SOURCE) $(DEP_RSC_RESOU)\
 "$(INTDIR_SRC)"
	$(RSC) /l 0x809 /fo"$(INTDIR_SRC)\resource.res" /d "NDEBUG" /d "SWING_RELEASE"\
 $(SOURCE)


!ELSEIF  "$(CFG)" == "nawin32 - Win32 Debug With Swing"


".\Debug With Swing\resource.res" : $(SOURCE) $(DEP_RSC_RESOU) "$(INTDIR_SRC)"
	$(RSC) /l 0x809 /fo"$(INTDIR_SRC)\resource.res" /d "_DEBUG" $(SOURCE)


!ENDIF 


!ENDIF 

