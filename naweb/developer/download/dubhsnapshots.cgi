#!/bin/sh
#
# $Id: dubhsnapshots.cgi,v 1.2 1999-06-08 22:42:35 briand Exp $

DOWNLOAD_DIR=/home/briand/public_html/newsagent/developer/download
NAID=na.1.1.0
NAJAR=na-1.1.0.jar
DUID=dju.1.1.0
DUJAR=dju-1.1.0.jar
DMPID=dmp.1.0.0
DMPJAR=dmp-1.0.0.jar


echo "Content-Type: text/html"
echo ""
echo "<html><head>"
echo "<link rel=stylesheet type=text/css href=../../style/newsagent.css>"
echo "<title>Source Snapshots for NewsAgent</title>"
echo "</head>"

echo "<body>"

echo "<h1>NewsAgent Source Code</h1>"
echo "<p>To become a contributor, and gain access to the CVS repository, "
echo "or find out more information about the files available here, "
echo "please see the <a href=../contributors>"
echo "Contributors Page</a>. These files are development snapshots of"
echo "NewsAgent, DJU and DMP source code. This page is updated automatically"
echo "every night if the source code has been altered since the previous"
echo "night.</p>"

#
# NewsAgent
#

echo "<h2>NewsAgent (NA)</h2>"

NASIZE=`ls -l $NAJAR.zip | cut -f13 -d " "`
KNASIZE=$[$NASIZE/1024]

echo "<p>Latest compiled <a href=$NAJAR.zip>$NAJAR</a> (zipped, $KNASIZE k)</p>"

echo "<p><a href=nalog.txt>CVS Log</a> of Changes in latest build</p>"

cd $DOWNLOAD_DIR
ALLFILES=`ls -t *.htm | grep $NAID`

echo "<ul>"
for thisfile in $ALLFILES; do
   echo "<li>"
   cat $thisfile
   echo "</li>"
done
echo "</ul>"

#
# DJU
# 

echo "<h2>Dubh Java Utilities (DJU)</h2>"

DJUSIZE=`ls -l $DUJAR.zip | cut -f13 -d " "`
KDJUSIZE=$[$DJUSIZE/1024]

echo "<p>Latest compiled <a href=$DUJAR.zip>$DUJAR</a> (zipped, $KDJUSIZE k)</p>"


echo "<p><a href=djulog.txt>CVS Log</a> of changes in latest build</p>"

ALLFILES=`ls -t *.htm | grep $DUID`
echo "<ul>"
for thisfile in $ALLFILES; do
   echo "<li>"
   cat $thisfile
   echo "</li>"
done
echo "</ul>"


#
# DMP
# 

echo "<h2>Dubh Mail Protocols (DMP)</h2>"

DMPSIZE=`ls -l $DMPJAR.zip | cut -f13 -d " "`
KDMPSIZE=$[$DMPSIZE/1024]

echo "<p>Latest compiled <a href=$DMPJAR.zip>$DMPJAR</a> (zipped, $KDMPSIZE k)</p>"

echo "<p><a href=dmplog.txt>CVS Log</a> of changes in latest build</p>"

ALLFILES=`ls -t *.htm | grep $DMPID`
echo "<ul>"
for thisfile in $ALLFILES; do
   echo "<li>"
   cat $thisfile
   echo "</li>"
done
echo "</ul>"

echo "<hr>"
echo "</body></html>"


#
# $Log: not supported by cvs2svn $
#