<?xml version="1.0"?>

<!-- Copyright (C) 2000 Brian Duff / Dubh.Org -->
<!-- XML stylesheet for navigators -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  
  <!-- This (optional) parameter is used to select the current item in the navigator -->
  <xsl:param name="me" select="''"/>
  
  <xsl:template match="navigator">
    
    <table border="0">
      <tr>
        <td>
          <a href="/index.xml">
            <img src="{@titleimg}" alt="{@title}" border="0" />
          </a>
        </td>
      </tr>
      <tr>
        <td style="background: url(&quot;{@bgimg}&quot;)">
          <div class="navbar">
            <xsl:apply-templates select="group" />
          </div>
        </td>
      </tr>
    </table>
    
  </xsl:template>


  <xsl:template match="group">
    <p>
      <xsl:apply-templates select="entry" />
    </p>
  </xsl:template>
  
  <xsl:template match="entry">
    <!-- put in some non breaking spaces to indent.-->
    <xsl:for-each select="ancestor::entry">
      &#160;
    </xsl:for-each>
    <xsl:choose>
      <xsl:when test="$me=@id">
        <b>
          <xsl:value-of select="@title" />
        </b>
      </xsl:when>
      <xsl:otherwise>
        <a href="{@href}">
          <xsl:value-of select="@title" />
        </a>
      </xsl:otherwise>
    </xsl:choose>
    <br/>
    
    <xsl:apply-templates select="entry" />

  </xsl:template>

</xsl:stylesheet>