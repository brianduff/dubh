NewsAgent v1.0beta

UNIX Distribution with Swingall.jar (v1.1) - 26/04/98

(C) 1997-8 Brian Duff. All rights reserved.

This program is FREE

Thanks to Philip Brown (phil@bolthole.com) for suggestions on improving
the UNIX distributions.

See the web site for specific licencing information:
	http://st-and.compsoc.org.uk/~briand/newsagent

o Installing the UNIX distribution   **************************************

 - Change into your common packages directory, wherever that may be (e.g.
   /local/packages or /~/packages if you are just installing for your own use)

 - decompress the distribution file, using the following commands:

	gunzip na_unix_10b_s.tar.gz
	tar -xvf na_unix_10b_s.tar
	rm na_unix_10b_s.tar

	This will create a directory called newsagent.

 - Set up a NEWSAGENT_HOME environment variable pointing to the directory in
   which you installed newsagent. If you use tcsh, you need to do:
	setenv NEWSAGENT_HOME /local/packages/newsagent/
   with bash, use:
	NEWSAGENT_HOME=/local/packages/newsagent/
   You might want to put this command in your .tcshrc, .cshrc or .bashrc.
   If you don't set up a NEWSAGENT_HOME variable, a sensible one will be
   used by default.

 - Make sure you have the JDK or JRE 1.1.5 installed, and IN YOUR PATH. See
   http://java.sun.com/ for more information.

 - run /local/packages/newsagent/newsagent

 - Wait. It might take a while before anything happens.

o Troubleshooting Problems ***********************************************

 - You have not set up a NEWSAGENT_HOME environment variable: see the above
   instructions.

 - java or jre couldn't be found in your PATH: Make sure you have correctly
   installed Java. See http://java.sun.com/ for more details.

 - Class not found: Check that there are two files with the extension .jar
   in the newsagent directory. One is called swingall.jar and the other is
   newsagentXXX.jar where XXX is the version number.

 - Exception trying to display the main window: Make sure your DISPLAY
   environment variable is set up properly, and you have permission to 
   display on the remote machine.

 - NewsAgent is slow: Get a faster machine / Java Runtime implementation / 
   complain to JavaSoft.

 - Anything else?  Email bd@st-andrews.ac.uk.
