<%@ page  contentType="text/html;charset=windows-1252"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>
Hello World
</title>
</head>
<body>
<h2>
The current time is: 
</h2>
<p>


<table width="100%" border="0">
  <tr>
    <th>Date</th>
    <th>Size</th>
  </tr>
  <% 
  String path = request.getRealPath( "" );
  java.io.File f = new java.io.File( path );
  java.io.File[] children = f.listFiles();

  for ( int i=0; i < children.length; i++ )
  {
  %>
  <tr>

  <td>
  <%= new Date(children[i].lastModified()).toString() %>
  </td>

  <td>
  <% if ( children[i].length() > 1024 * 1024 ) { %>
    <%= "" + children[i].length() / 1024 / 1024 + " MB" %>
  <% } else if ( children[i].length() > 1024 ) { %>
    <%= "" + children[i].length() / 1024 + " KB" %>
  <% } else { %>
    <%= "" + children[i].length() + " bytes" %>
  <% } %>
  </td>
  
  </tr>
  <%
  }
  %>

</table>
</p>
</body>
</html>
