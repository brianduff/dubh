<?xml version="1.0"?>

<!-- XSL for handling XSQL errors -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="xsql-error">

    <span class="xsql-error">
      <h1>An Error Has Occurred</h1>

      <p>
        An error occurred on the server while processing this document:
      </p>
      <p>
        <b><xsl:value-of select="@code" />:</b>
           <xsl:value-of select="message" />
      </p>
      <p>
        Please report the error to Brian.Duff@oracle.com, including what
        you were doing when the error happened, and quoting the error
        message above.
      </p>
    </span>

  </xsl:template>
  
  
</xsl:stylesheet>