Freeciv java client.

For more info about freeciv check http://www.freeciv.org.
The Freeciv java client web page is at http://www.dubh.org/projects/jfreeciv/index.xml

To run this code you can use JDK 1.3 or JDK 1.4

Instructions on getting the source code are here:

http://cvs.dubh.org/access.html

You need to check out the jfreeciv module.

If you don't know what CVS is, read http://www.cvshome.com

To run this thing you need to compile all sources and either copy/move
org/freeciv/data directory to top directory (java/ in this case) or add
-Dfreeciv.datadir=some/path/data on java commandline (before
org.freeciv.client.Client) (with default checkout it is -Dfreeciv.datadir=org/freeciv/data)

Note, the code is currently in a pretty disfunctional state.

Artur                           Brian
<abies@pg.gda.pl>               <Brian.Duff@dubh.org>


