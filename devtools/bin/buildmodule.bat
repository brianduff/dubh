rem BuildModule, the NT version
rem
rem $Id: buildmodule.bat,v 1.4 2000-08-21 00:29:41 briand Exp $

set BUILD_DIR=.
set DT=devtools/lib
# All this stuff is needed to build Dubh tools - most of it is for the XSLT
# transformations for UI and RTS.
set EXTRA_CLASSPATH=%DT%\devtools-1.0.jar;%DT%\jaxp.jar;%DT%\parser.jar:%DT%\ant.jar:%DT%\xalan_1_0_1.jar:%DT%\optional.jar:%DT%\xerces_1_0_3.jar
set ANT_HOME=e:\jakarta-ant
set ANT_MAIN=org.apache.tools.ant.Main

set CVSROOT=:pserver:briand@dubh.org:/usr/local/cvsroot

rem ANT_OPTIONS="-logfile $1.build.log"

cd %BUILD_DIR%
rem rmdir /s/q %1
cvs checkout %1/build.xml
set CLASSPATH=%EXTRA_CLASSPATH%:%CLASSPATH%
java -Dant.home=%ANT_HOME% %ANT_MAIN% %ANT_OPTIONS% -buildfile %1/build.xml %2

# TODO: add logging etc.

