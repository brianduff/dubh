<?xml version="1.0"?>

<!-- Copyright (C) 2000 Brian Duff / Dubh.Org -->
<!-- XML stylesheet for project list -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="sectionedpage">
    <table border="0" width="100%">
      <xsl:apply-templates select="section" />
    </table>
  </xsl:template>
  
  <xsl:template match="section">
    <tr>
      <td background="/images/dubh.codebg_purple.gif">
        <font size="+1" face="tahoma,arial,helvetica,sans-serif">
          <b>
            <a name="@id"><xsl:value-of select="@name" /></a>
          </b>
        </font>
      </td>
    </tr>
    <tr>
      <td>
        <xsl:apply-templates select="html" />
      </td>
    </tr>
  </xsl:template>
  
  <xsl:template match="html">
    <xsl:copy-of select="node()" />
  </xsl:template>
  
</xsl:stylesheet>