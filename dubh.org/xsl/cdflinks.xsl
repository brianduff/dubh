<!-- Copyright (C) 2000, 2001 Brian Duff / Dubh.Org -->
<!-- Stylesheet to transform an internet explorer CDF file into a 
     list of links.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" />
  
  <xsl:param name="verbose">false</xsl:param>  
  
  <xsl:template match="cdf">
    <xsl:apply-templates select="channel" />
  </xsl:template>
  
  <xsl:template match="CHANNEL">    
    <div>
      <ul>
        <xsl:for-each select="ITEM">
          <li>
            <a href="{@href}" target="artdet">
              <xsl:value-of select="TITLE" />
            </a>
            <xsl:if test="$verbose='true'">
              <p>
              <xsl:value-of select="ABSTRACT" />
              </p>
            </xsl:if>
          </li>
        </xsl:for-each>
      </ul>
      <i>
        News provided by <a href="{@HREF}"><xsl:value-of select="@HREF" /></a>
      </i>
    </div>
  </xsl:template>
  
</xsl:stylesheet>