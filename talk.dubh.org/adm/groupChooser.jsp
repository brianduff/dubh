
<%
/**
 *	$RCSfile: groupChooser.jsp,v $
 *	$Revision: 1.1 $
 *	$Date: 2001-06-03 05:21:47 $
 */
%>

<%	/////////////////////
	// get an iterator of groups and display a list
	
	Iterator groupIterator = manager.groups();
	if( !groupIterator.hasNext() ) {
%>
		No groups to edit!
<%
	}
%>

<form action="<%= formAction %>Group.jsp">
<select name="group">
<%	while( groupIterator.hasNext() ) {
		Group group = (Group)groupIterator.next();
%>
		<option value="<%= group.getID() %>"><%= group.getName() %>
<%	}
%>
</select>
<%	if( formAction.equals("edit") ) { %>
	<input type="submit" value="Edit This Group...">
<%	} else if( formAction.equals("remove") ) { %>
	<input type="submit" value="Remove This Group...">
<%	} %>
</form>
