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
      <table width="100%" border="0" cellpadding="3" cellspacing="0">
        <xsl:for-each select="ITEM">
          <tr>
            <td valign="top">
              <img src="/images/x1navy.gif" alt="Bullet" />
            </td>
            <td width="100%" valign="top">
              <a href="{@href}" target="artdet">
                <xsl:value-of select="TITLE" />
              </a>
              <xsl:if test="$verbose='true'">
                <p>
                <xsl:value-of select="ABSTRACT" />
                </p>
              </xsl:if>
            </td>
          </tr>
        </xsl:for-each>
      </table>
      <p>
        <i>
          News provided by <a href="{@HREF}"><xsl:value-of select="@HREF" /></a>
        </i>
      </p>
    </div>
  </xsl:template>
  
</xsl:stylesheet>