<!-- Copyright (C) 2000, 2001 Brian Duff / Dubh.Org -->
<!-- Stylesheet to transform a moreover.com XML file into a 
     list of links.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" />
  
  
  <xsl:template match="moreovernews">    
    <div>
      <table width="100%" border="0" cellpadding="3" cellspacing="0">

      <xsl:for-each select="article">
        <tr>
          <td valign="top">
            <img src="/images/x1navy.gif" alt="Bullet" />
          </td>
          <td width="100%" valign="top">          
            <a href="{url}" target="artdet">
              <xsl:value-of select="headline_text" />
            </a>
          </td>
        </tr>
      </xsl:for-each>
      </table>
      <p>
        <i>
          News provided by <a href="http://moreover.com">Moreover.com</a>
        </i>
      </p>
    </div>
  </xsl:template>
  
</xsl:stylesheet>