What is JFreeCiv?
-----------------

JFreeCiv is a java version of Freeciv. Freeciv is an open source version of
the popular strategy game Civilization II.

For more information about JFreeCiv, visit:

http://www.dubh.org/projects/jfreeciv

For more information about FreeCiv, visit:

http://www.freeciv.org

Which Version of FreeCiv is JFreeCiv Based On?
----------------------------------------------

Currently, the network capability string used by the client is +1.11.16. 
This is the same capability string as the 1.12.0 Freeciv client: the JFreeciv
client can connect to a 1.12.0 Freeciv server with no problems.

What Do I Need to Run JFreeCiv?
-------------------------------

You'll need to install the Java Development Kit or Java Runtime Environment 
1.3 or 1.4. You can download these from http://java.sun.com.

You'll also need to download the data files used by Freeciv. These currently 
aren't part of either the source or binary distributions of JFreeCiv. You 
can download them at:

http://balveda.dubh.org/build/jfreeciv/jfreeciv-data.tar.gz [914 KB]

JFreeCiv is only a client; you'll need a freeciv server to connect to. Visit 
http://www.freeciv.org to get the binaries for your platform.

Finally, you'll need plenty of memory and a pretty fast machine. Expect
JFreeCiv to consume around 30-50 MB of memory. This is not unusual for a
large graphical Java appplication.

How Do I Run JFreeCiv?
----------------------

If you downloaded the binary distribution:

o Make sure java.exe is in your PATH (if you installed JDK or JRE, this will
already be the case on most operating systems)

o Run a command prompt (DOS prompt), or bring up a UNIX shell.

o Change into the top directory (the directory containing this readme file)

o Type:

java -cp lib/jfreeciv-1.0.jar -Dfreeciv.datadir=path/to/datadir org.freeciv.client.Client

That's all one line, and you need to replace path/to/datadir with the 
directory you downloaded the data files to ( see "What Do I Need to Run 
JFreeCiv?" above )

How Do I Build JFreeCiv?
------------------------

We maintain nightly source snapshots of the JFreeCiv source code on dubh.org.
These are guaranteed to build successfully (but not necessarily run). You
can download them from http://balveda.dubh.org/build/jfreeciv/src

If you want to live dangerously, or see code that hasn't made it into a
nightly snapshot yet, you can use CVS to check out the latest source. 
Information on accessing the CVS repository are available at 
http://cvs.dubh.org. The jfreeciv module contains all the jfreeciv code.

Once you have the code, you need to compile it. You can do this using your
favorite IDE or using command line javac, or jikes or whatever. Basically
all the code in src has to be compiled, and any *.png, *.gif or *.properties
files have to be copied to the output directory.

You may find the easiest way to build is to use Ant. Ant is similar to the
make tool available on many systems, but is based on Java and XML. You should
download Ant 1.4 or later from http://jakarta.apache.org/ant/index.html. Once
ant is installed and in your PATH, you can compile jfreeciv by changing into
the top directory (the one that contains this Readme.txt file), and typing:

ant

Easy, huh?


What License Is JFreeCiv Distributed Under?
-------------------------------------------

The GNU General Public License, because JFreeCiv is a derivative of Freeciv.
More information is in the copying.txt file distributed in the source and
binary distributions.

How Much of It Works?
---------------------

That's the thing, the client is far from finished at the moment. In fact, the
client is capable of doing a lot (in particular, the map component is quite
advanced and can display most of the freeciv map). However, a lot of the UI
required to interact with the game is still missing.

You can use the C version of FreeCiv to advance the game to a certain state,
disconnect, then reconnect with the same user name with the Java client to
test out the map support. Other than that, until we get some interaction UI
rolling, you won't exactly be able to conquer the world yet.

Which leads me nicely to...

How Can I Help?
---------------

JFreeCiv, like most open source projects, can only succeed through small
gifts of time by people who like messing around with these things in their
free time.

If you're interested in helping out, send some mail to the freeciv java
mailing list (see below).

Where Can I Send Feedback / Bug Reports / Questions
---------------------------------------------------

We discuss the client on the freeciv-java mailing list. To subscribe to this
list, send some mail to listar@freeciv.org with a blank subject, and the
following in the body of the message:

subscribe "freeciv-java"

The list is also archived on the web at http://arch.freeciv.org (so you can
get an idea about the level of traffic etc.)

