<?xml version="1.0"?>

<!-- Copyright (C) 2000 Brian Duff / Dubh.Org -->
<!-- XML stylesheet for project list -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="projectlist">
    <table border="0">
      <xsl:apply-templates select="project" />
    </table>
  </xsl:template>
  
  <xsl:template match="project">
    <tr>
      <td background="/images/dubh.codebg_purple.gif">
        <font size="+1" face="tahoma,arial,helvetica,sans-serif">
          <b>
            <a href="{@href}"><xsl:value-of select="@name" /></a>
          </b>
        </font>
      </td>
    </tr>
    <tr>
      <td>
        <xsl:apply-templates select="description" />
      </td>
    </tr>
  </xsl:template>
  
  <!-- Can be in html -->
  <xsl:template match="description">
    <xsl:copy-of select="node()" />
  </xsl:template>
  
</xsl:stylesheet>