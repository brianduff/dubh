Freeciv java client.

For more info about freeciv check http://www.freeciv.org.

To run this code you need JDK 1.2 or 1.3(but it has some problems with keybindings).

You have to check out code from CVS.

CVSROOT=:pserver:anoncvs@cvs.gjt.org:/gjt/cvsroot
passwd: anoncvs

modules to checkout:

java/com/sixlegs/image/png
java/org/freeciv
java/org/gjt/abies

If you don't know what CVS is, checkout http://www.cyclic.com and/or www.gjt.org.

To run this thing you need to compile all sources and either copy/move
org/freeciv/data directory to top directory (java/ in this case) or add
-Dfreeciv.datadir=some/path/data on java commandline (before
org.freeciv.client.Client) (with default checkout it is -Dfreeciv.datadir=org/freeciv/data)

When you run it first time image cache will be created - about 4MB of disk
space. You should see connection dialog - choose server and connect.

You can move units by right clicking on them and then leftclicking
on destination or using numeric keypad(some problems on linux). 

If you will build city you can check it's minimap by
left clicking on it.
Sometimes mouse click seems to go unnoticed - it is because of focus problems
(main frame lose focus often).

If you want to use sounds, you need a copy of civilization II game. Get all .wav files
from it and copy them to some directory together with org/freeciv/sound/sndconf file. Then
add -Dfreeciv.sound=/path/to/dir option on command line (after java, before org.freeciv.client.Client).
It is easiest to just put wavs into org/freeciv/data/sound and then run
java -Dfreeciv.sound=org/freeciv/data/sound org.freeciv.client.Client

Artur
<abies@pg.gda.pl>
