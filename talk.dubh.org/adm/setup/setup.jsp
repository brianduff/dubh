<%
/**
 * Jive Setup Tool
 * November 28, 2000
 */
%>

<%@ page import="java.io.*, java.util.*, java.lang.reflect.* "%>
<html>
<head>
	<title>Jive Setup</title>
		<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">
<img src="images/setup.gif" width="210" height="38" alt="Jive Setup" border="0">

<hr size="0"><p>

<b>Welcome to Jive Setup</b>
<p>

We've detected that your system needs to be setup for Jive. This tool will
guide you through making a connection to your database and setting up an administrator
account. After completing setup, you'll be able to use the admin tool to create
forums and start using Jive.
<p>
If you encounter problems with the setup process, please report them to 
<a href="mailto:matt@coolservlets.com">matt@coolservlets.com</a>.
If you'd like to return here at some point in the future after
completing the intial setup, delete the 
"setup=true" field from your jive.properties file.
<p>
<b>Setup Checklist</b>
<ul>
<table border=0>
		
	<tr><td valign=top><img src="images/check.gif" width="13" height="13"></td><td>
		Setup has detected that you are running <%= application.getServerInfo() %>.
	</td></tr>
	
	<%
		//See if they have Java2 or later installed by trying to load java.util.HashMap.
		boolean isJava2 = true;
		try {
			Class.forName("java.util.HashMap");
		}
		catch (ClassNotFoundException cnfe) {
			isJava2 = false;
		}
		if (isJava2) {
	%>	
	<tr><td valign=top><img src="images/check.gif" width="13" height="13"></td><td>
		You are running Java 2 or later.
	</td></tr>
	<%
		}
		else {
	%>
	<tr><td valign=top><img src="images/x.gif" width="13" height="13"></td><td>
		<font color="red">
		You do not appear to be running Java 2 or later. Therefore, setup cannot continue. 
		If possible, upgrade your version of Java and restart this process.
		</font>
	</td></tr>	
	<%
		}
	%>
	
	<%
		//See if the Jive classes are installed
		boolean jiveInstalled = true;
		try {
			Class.forName("com.coolservlets.forum.Forum");
		}
		catch (ClassNotFoundException cnfe) {
			jiveInstalled = false;
		}
		if (jiveInstalled) {
	%>
	<tr><td valign=top><img src="images/check.gif" width="13" height="13"></td><td>
		The Jive application files are installed.
	</td></tr>
	<%
		}
		else {
	%>
	<tr><td valign=top><img src="images/x.gif" width="13" height="13"></td><td>
		<font color="red">
		The Jive application files could not be loaded. Follow the installation documentation
		instructions and ensure that the class files are in the classpath of your
		application server.
		</font>
	</td></tr>	
	<%
		}
	%>
		
	<%
		//See if the Lucene classes are installed
		boolean luceneInstalled = true;
		try {
			Class.forName("com.lucene.document.Document");
		}
		catch (ClassNotFoundException cnfe) {
			luceneInstalled = false;
		}
		if (jiveInstalled) {
	%>
	<tr><td valign=top><img src="images/check.gif" width="13" height="13"></td><td>
		The Lucene application files are installed.
	</td></tr>
	<%
		}
		else {
	%>
	<tr><td valign=top><img src="images/x.gif" width="13" height="13"></td><td>
		<font color="red">
		The Lucene application files could not be loaded. Follow the installation 
		documentation instructions and ensure that the class files are in the classpath 
		of your	application server.
		</font>
	</td></tr>	
	<%
		}
	%>
		
	<%
		//Check status of jive.properties file. Because some servlet engines have seperate class
		//loaders for JSP and other Java classes, we need to use the Jive PropertyManager to
		//get the properties. We use reflection to call methods on PropertyManager so that we don't 
		//have to do an import of that class and screw up the error messages for failure to load Jive classes.
		
		boolean propError = false;
		String errorMessage = null;
		String path = null;
		try {
			Class propManager = Class.forName("com.coolservlets.forum.PropertyManager");
			
			Method propReadable = propManager.getMethod("propertyFileIsReadable", null);
			if ( ((Boolean)propReadable.invoke(null,null)).booleanValue() ) {
						
				//Now, get property
				Method getProperty = propManager.getMethod("getProperty", new Class[] { Class.forName("java.lang.String") } );
				path = (String)getProperty.invoke(null, new Object[] { "path" } );
				if (path == null || path.equals("")) {
					propError = true;
					errorMessage = "The Jive properties were successfully loaded. However, the " +
						"path field was not set which prevents Jive from being able to save " +
						"properties to the filesystem. Edit the properties file and add a path " +
						"field that looks something like: 'path=c:&#92;&#92;path&#92;&#92;to&#92;&#92;jive.properties' " +
						"or 'path=/path/to/jive.properties'.";
				}
				//Otherwise, see if the file exists
				else {
					Method propExists = propManager.getMethod("propertyFileExists", null);
					if ( ((Boolean)propExists.invoke(null,null)).booleanValue() ) {
						//See if we can write to the file
						Method propWritable = propManager.getMethod("propertyFileIsWritable", null);
						if ( !((Boolean)propWritable.invoke(null,null)).booleanValue() ) {
							propError = true;
							errorMessage = "The Jive properties were successfully loaded and found at &quot;<code>" +
								path + "</code>&quot;. However, the application server does not have write permission " +
								"on the file.";
						}
					}
					//The file doesn't exist
					else {
						propError = true;
						errorMessage = "The Jive properties were successfully loaded. However, the path &quot;<code>" +
							path + "</code>&quot; does not appear to exist. Edit the jive.properties file and make sure " +
							"that the path field points to exactly where the properties file exists on your " +
							"filesystem.";
					}
				}
        	}
        	else {
           		propError = true;
           		errorMessage = "The jive.properties file could not be loaded. Make sure that it is " +
					"in the classpath of your application server.";
        	}
		}
		catch (Exception e) {
			e.printStackTrace();
			propError = true;
           	errorMessage = "There was a general error loading the Jive properties. Be sure that you " +
				"have installed the latest Jive code.";
		}
		if (!propError) {
	%>
	<tr><td valign=top><img src="images/check.gif" width="13" height="13"></td><td>
		The Jive properties were successfully loaded and found at <%= path %>.
	</td></tr>
	<%
		}
		else {
	%>
	<tr><td valign=top><img src="images/x.gif" width="13" height="13"></td><td>
		<font color="red">
		<%= errorMessage %>
		</font>
	</td></tr>	
	<%
		}
	%>
	
</table>
</ul>

<% 
	if (propError || !jiveInstalled || !isJava2) {
%>
	<font color="red"><b>One or more errors occured in the initial setup checklist. Please correct 
	them, restart your application server, and then reload this page.</b></font>
<%
	}
	else {
%>
</font>

<form action="setup2.jsp" method=post>

<center>
<input type="submit" value="Continue &gt;">
</center>

<%
	}
%>

<p>
<hr size="0">
<center><font size="-1"><i>www.coolservlets.com/jive</i></font></center>
</font>
</body>
</html>

