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
    
    <div class="sectionheading">
    <h2>
      <xsl:if test="boolean(@id)">
        <a name="{@id}" />
      </xsl:if>
      <xsl:choose>
        <xsl:when test="boolean(@link)">
          <a href="{@link}">
            <xsl:value-of select="@name" />
          </a>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@name" />
        </xsl:otherwise>
      </xsl:choose>
    </h2>
    </div>

    <xsl:apply-templates select="html" />

  </xsl:template>
  
  <xsl:template match="html">
    <xsl:copy-of select="node()" />
  </xsl:template>
  
</xsl:stylesheet>