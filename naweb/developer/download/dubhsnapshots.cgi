#!/bin/sh
#
#

DOWNLOAD_DIR=/home/briand/public_html/newsagent/developer/download
NAID=na.1.1.0
DUID=dju.1.1.0


echo "Content-Type: text/html"
echo ""
echo "<html><head>"
echo "<link rel=stylesheet type=text/css href=../../style/newsagent.css>"
echo "<title>Source Snapshots for NewsAgent</title>"
echo "</head>"

echo "<body>"

echo "<h1>NewsAgent / Dubh Utils Source Code</h1>"
echo "<p>To become a contributor, and gain access to the CVS repository, "
echo "please see the <a href=../contributors>"
echo "Contributors Page</a>. These files are development snapshots of"
echo "NewsAgent and DJU source code. This page is updated automatically"
echo "every night if the source code has been altered since the previous"
echo "night.</p>"

echo "<h2>NewsAgent</h2>"

echo "<p>Latest compiled <a href=na-1.1.0.jar.zip>na-1.1.0.jar</a> (zipped)</p>"

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

echo "<h2>Dubh Java Utilities</h2>"

echo "<p>Latest compiled <a href=dju-1.1.0.jar.zip>dju-1.1.0.jar</a> (zipped)</p>"

echo "<p><a href=djulog.txt>CVS Log</a> of changes in latest build</p>"

ALLFILES=`ls -t *.htm | grep $DUID`
echo "<ul>"
for thisfile in $ALLFILES; do
	echo "<li>"
	cat $thisfile
	echo "</li>"
done
echo "</ul>"

echo "<hr>"
echo "</body></html>"
