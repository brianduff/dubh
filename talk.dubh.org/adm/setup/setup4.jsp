<%
/**
 * Jive Setup Tool
 * November 28, 2000
 */
%>

<%@ page import="java.io.*,
                 java.util.*,
				 java.sql.*,
                 com.coolservlets.forum.*,
				 com.coolservlets.forum.util.*,
				 com.coolservlets.forum.database.*"%>
		
<% try { %>
				 
<%	boolean setupError = false;
	String errorMessage = "";
	//Make sure the install has not already been completed.
	String setup = PropertyManager.getProperty("setup");
	if( setup != null && setup.equals("true") ) {
%>
	<html>
<head>
	<title>Jive Setup - Step 4</title>
		<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<img src="images/setup.gif" width="210" height="38" alt="Jive Setup" border="0">
<hr size="0"><p>

	<font color="Red">Error!</font>
	<p><font size=2>
	
	Jive setup appears to have already been completed. If you'd like to re-run 
	this tool, delete the 'setup=true' property from your jive.properties file.
	
	</font>
		
<%	
	}
	else {
	
		boolean error = false;
		String jiveHome = ParamUtils.getParameter(request,"jiveHome");
		if (jiveHome == null) {
		jiveHome = PropertyManager.getProperty("jiveHome");
		}
		boolean setJiveHome = ParamUtils.getBooleanParameter(request,"setJiveHome");
		//Look for error case, but only give a new error message if there isn't
		//already an error.
		if(setJiveHome && jiveHome == null ) {
			error = true;
			errorMessage = "No value was entered for Jive Home. Please enter a path.";
		}
	%>
	<%	if( !error && setJiveHome ) {
			// chomp a trailing "/" or "\\"
			while( jiveHome.length() > 0 
					&& jiveHome.charAt(jiveHome.length()-1) == '/'
					|| jiveHome.charAt(jiveHome.length()-1) == '\\' )
			{
				jiveHome = jiveHome.substring(0,jiveHome.length()-1);
			}
			// check if the app server can write to that file
			File jiveHomeDir = new File(jiveHome);
			error = !jiveHomeDir.exists();
			if( error ) {
				errorMessage = "The directory you entered doesn't exist. Be sure to " +
					"create the Jive Home directory on your filesystem, and then try again.";
			} else {
				error = !jiveHomeDir.canRead();
				if( error ) {
					errorMessage = "The directory you entered exists, but you don't "+
						"have read access for it. Please fix the problem and try again.";
				} else {
					error = !jiveHomeDir.canWrite();
					if( error ) {
						errorMessage = "The directory you entered exists, buy you " +
							"don't have have write access for it. Please fix the " +
							"problem and try again.";
					} else {
						error = !jiveHomeDir.isAbsolute();
						errorMessage = "You didn't enter an absolute path for the Jive Home " +
						 	"directory (e.g., starting the path with '/' in Unix, or 'c:\' " +
							"in Windows). Please fix the problem and try again.";
					}
				}
			}
			
			// at this point, the path exists and we can read & write to it
			// so create the file
			if( !error ) {
				// create the search directory
				File searchDir = new File( jiveHome + File.separator + "search" );
				if (!searchDir.exists()) {
					searchDir.mkdir();
				}
				// set the jiveHome property in the jive.properties file
				PropertyManager.setProperty("jiveHome",jiveHome);
				// redirect
				response.sendRedirect("setup5.jsp");
				return;
			}
		}
%>


<html>
<head>
	<title>Jive Setup - Step 4</title>
		<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<img src="images/setup.gif" width="210" height="38" alt="Jive Setup" border="0">
<hr size="0"><p>

<b>Jive Home Directory</b>

<ul>

<font size="2">
	Jive needs a place to store data on your filesystem. The directory where this
	data is stored is called "Jive Home". This step in the setup tool will help
	you create this directory.
	<p>
	First decide where you'd like Jive Home to exist. This could be "/usr/local/jiveHome"
	in Unix, or "c:\jiveHome" in Windows. Naming the directory "jiveHome" is not
	required, but is recommended. After creating the directory, make sure that
	your application server has read and write access to it. Now, enter the full
	path to the directory you created below.<p>

<%
	if (error) {
%>

	<font color="Red">Error:</font></font>	<i><%= errorMessage %></i>

	<p>

<%	} %>
	
	
	<form action="setup4.jsp" method="post">
	<input type="hidden" name="setJiveHome" value="true">
		
	<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<td><font size="-1">Jive Home:</font></td>
		<td><input type="text" size="50" name="jiveHome" value="<%= (jiveHome!=null)?jiveHome:"" %>"></td>
	</tr>
	</table>

</ul>

<center>
<input type="submit" value="Continue">
</center>
</form>

<% } //end else of setupError %>


<p>
<hr size="0">
<center><font size="-1"><i>www.coolservlets.com/jive</i></font></center>
</font>
</body>
</html>

<%	} catch (Exception e ) {
		e.printStackTrace();
	}
%>
