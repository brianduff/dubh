
<%
/**
 *	$RCSfile: error.jsp,v $
 *	$Revision: 1.1 $
 *	$Date: 2001-06-03 05:20:07 $
 */
%>

<%@ page 
	import="java.io.*,
	        java.util.*,
			com.coolservlets.forum.*,
			com.coolservlets.util.*"
	isErrorPage="true"
%>

<%	// Get parameter values
	String 	message	= ParamUtils.getParameter(request, "message");
%>

<%	/////////////////
	// header include
	// Provide the header.jsp with its variables
	Authorization authToken = null;
	ForumFactory forumFactory = null;
	int 	forumID = -1;

	String title = "Error";
%>
<%@	include file="header.jsp" %>

<font face="verdana" size=2>
<b>Error</b>
</font>

<p>

<ul>
<li><b>
	<%	if (exception != null) { %>
		Runtime exception.
	<%	} else { %>
		General error.
	<%	} %>
</b></li>
</ul>

<p>

<pre>
<%	if (exception != null) { %>
	<% StringWriter sout = new StringWriter();
		PrintWriter pout = new PrintWriter(sout);
		exception.printStackTrace(pout); %>
	<%= sout.toString() %>
<%	} else { %>
		<font color="#ff0000"><%= message %></font>
<%	} %>
</pre>

<%@	include file="footer.jsp" %>
