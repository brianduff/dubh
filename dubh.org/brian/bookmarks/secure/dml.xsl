<?xml version="1.0"?>
<!-- 
==============================================================================
Stylesheet for DML operations.
==============================================================================
Author: Brian.Duff@oracle.com
==============================================================================
This stylesheet is used for all "raw" DML insertions, updates and deletes.
Each one of these triggers some SQL, which may fail or succeed. If the SQL
fails, an error message from the XSQL source document is displayed. If the
SQL succeeds, a success message is displayed and the document is redirected
back to the bookmarks page in edit mode.
==============================================================================
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    version="1.0">
    
  <xsl:template match="add">
    <html>
      <head>
        <link rel="stylesheet" href="../bookmarks.css" type="text/css" />
        <xsl:choose>
          <xsl:when test="boolean(xsql-error)">
            <title><xsl:value-of select="error-messages/short" /></title>
          </xsl:when>
          <xsl:otherwise>
            <title><xsl:value-of select="success-messages/short" /></title>
            <meta http-equiv="refresh" content="0; URL=../showbookmarks.xsql?editmode=true" />
          </xsl:otherwise>
        </xsl:choose>
      </head>
     
      <body>
        <xsl:choose>
          <xsl:when test="boolean(xsql-error)">
            <h1><xsl:value-of select="error-messages/short" /></h1>
            <p>
              <xsl:value-of select="error-messages/long" />
            </p>
            <pre>
              <xsl:value-of select="xsql-error/message" />
            </pre>
            <p>
              <a href="../showbookmarks.xsql?editmode=true">Click to return</a>.
            </p>
            
          </xsl:when>
          <xsl:otherwise>
            <h1><xsl:value-of select="success-messages/short" /></h1>
            
            <p>
              <xsl:value-of select="success-messages/long" />
            </p>
            
            <p>
              <a href="../showbookmarks.xsql?editmode=true">Click to return</a>.
            </p>
          </xsl:otherwise>
        </xsl:choose>
      </body>
    </html>
  </xsl:template>
    
</xsl:stylesheet>    