<!-- Copyright (C) 2000, 2001 Brian Duff / Dubh.Org -->
<!-- Stylesheet to transform a moreover.com XML file into a 
     list of links.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" />
  
  
  <xsl:template match="moreovernews">    
    <div>
      <ul>
        <xsl:for-each select="article">
          <li>
            <a href="{url}" target="artdet">
              <xsl:value-of select="headline_text" />
            </a>
          </li>
        </xsl:for-each>
      </ul>
      <i>
        News provided by <a href="http://moreover.com">Moreover.com</a>
      </i>
    </div>
  </xsl:template>
  
</xsl:stylesheet>