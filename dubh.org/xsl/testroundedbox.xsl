<?xml version="1.0"?>

<!-- Copyright (C) 2000, 2001 Brian Duff / Dubh.Org -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" /> 
   
  <xsl:include href="roundedbox.xsl" /> 
  
  <xsl:template match="testrb"> 
    <html> 
      <head>
        <title>Test Rounded  Box</title>  
      </head> 
      <body>
        <xsl:for-each select="box">
          <p>
          <xsl:call-template name="RoundedBox"> 
            <xsl:with-param name="Title"><xsl:value-of select="title" /></xsl:with-param>
            <xsl:with-param name="Contents">
              <xsl:copy-of select="contents" />
            </xsl:with-param>
          </xsl:call-template>
          </p>
        </xsl:for-each>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>  