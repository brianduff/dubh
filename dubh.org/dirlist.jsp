<%@ page  contentType="text/html;charset=windows-1252"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>
Downloads
</title>

<link rel="stylesheet" href="/styles.css" type="text/css" >

</head>
<body>
<h1>
  Downloads
</h1>
<p>


<table width="100%" border="0">
  <tr>
    <th align="left">Date</th>
    <th align="left">Size</th>
  </tr>
  <% 
  String path = request.getRealPath( "" );
  java.io.File[] children = new java.io.File( path ).listFiles();

  // Sort the files by their date.
  java.util.Arrays.sort( children, new java.util.Comparator() {
    public int compare( Object o1, Object o2 )
    {
      long f1 = ((File)o1).lastModified();
      long f2 = ((File)o2).lastModified();

      if ( f1 < f2 )
      {
        return 1;
      }
      else if ( f1 > f2 )
      {
        return -1;
      }
      else
      {
        return 0;
      }
    }
  });

  for ( int i=0; i < children.length; i++ )
  {
  java.io.File f = children[i];
  long length = f.length();
  %>
  <tr>

  <td>
  <a href="<%=f.getName() %>">
  <%= new Date(f.lastModified()).toString() %>
  </a>
  </td>

  <td>
  <% if ( length > 1024 * 1024 ) { %>
    <%= "" + length / 1024 / 1024 + " MB" %>
  <% } else if ( length > 1024 ) { %>
    <%= "" + length / 1024 + " KB" %>
  <% } else { %>
    <%= "" + length + " bytes" %>
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
