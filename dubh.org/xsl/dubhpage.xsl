<!-- Copyright (C) 2000, 2001 Brian Duff / Dubh.Org -->
<!-- XML stylesheet for a "dubh page" -->
 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
    doctype-system="http://www.w3.org/TR/html4/loose.dtd" />

  
  <!-- The URL of the default CSS stylesheet to use if none is specified -->
  <xsl:variable name="defaultcss">/styles.css</xsl:variable>

  <!-- The contact email of the author of these pages, if no author is specified -->
  <xsl:variable name="default-author-email">Brian.Duff@oracle.com</xsl:variable>

  <!-- The contact name of the author of these pages, if no author is specified -->
  <xsl:variable name="default-author-name">Brian Duff</xsl:variable>

  <xsl:include href="roundedbox.xsl" />

  <xsl:template match="dubhpage">
    <xsl:call-template name="main" />
  </xsl:template>
  
  <xsl:template name="main">
    <html>
      <head>
        
        <!-- Various optional meta tags -->
        <xsl:if test="boolean(keywords)">
          <meta name="keywords" content="{keywords}" />
        </xsl:if>
        <xsl:if test="boolean(description)">
          <meta name="description" content="{description}" />
        </xsl:if>
        
        <xsl:choose>
          <xsl:when test="boolean(/dubhpage/author/@name)">
            <meta name="author" content="{/dubhpage/author/@name}" />
          </xsl:when>
          
          <xsl:otherwise>
            <meta name="author" content="{$default-author-name}" />
          </xsl:otherwise>
        </xsl:choose>
        
        <!-- Allow pages to provide an (overriding) customized sheet -->
        <xsl:choose>
          <xsl:when test="boolean(stylesheet)">
            <link rel="stylesheet" type="text/css" href="{stylesheet}" />
          </xsl:when>

        </xsl:choose>
        
        <!-- always include the site default stylesheet -->
        <link rel="stylesheet" type="text/css" href="{$defaultcss}" />    
        
        <title>
          <xsl:value-of select="title" />
        </title>
        
      </head>
      
      <body>
        <!--
            If there is a navigator on the page, the overall structure is slightly
            different
        -->
        <xsl:choose>
          <xsl:when test="boolean(navigator)">
            <xsl:call-template name="navigator-body" />
          </xsl:when>
          <xsl:when test="boolean(content)">
            <xsl:call-template name="no-navigator-body" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="no-body" />
          </xsl:otherwise>
        </xsl:choose>
        
        <xsl:call-template name="footer" />
      </body>
    </html>
  </xsl:template>
  
  <!-- 
      A page with a navigator. This is displayed as two table columns, with
      the navigator on the left.
  -->
  <xsl:template name="navigator-body">
    <table width="100%" border="0">
      <tr>
        <td valign="top">
          <xsl:apply-templates select="navigator" />
        </td>
        <td valign="top" width="100%">
          <xsl:apply-templates select="content" />
        </td>
      </tr>
    </table>
  </xsl:template>
  
  <!--
      A page with no navigator. We just render the content of the document
      directly.
  -->
  <xsl:template name="no-navigator-body">
    <xsl:apply-templates select="content" />
  </xsl:template>
  
  <!--
      A page with no body. This usually means Brian hasn't finished writing
      the page :) So, we just display an error message.
  -->
  <xsl:template name="no-body">
    <p>
      <b>This page has no content!</b>
    </p>
    <p>
      It's likely that this page isn't finished yet. You should contact the
      author of this page, <xsl:call-template name="print-author" />.
    </p>
    <p>
      Click <a href="javascript:back()">here</a> to go back.
    </p>
  </xsl:template>  
  
  <!--
      The actual content of the page.
  -->
  <xsl:template match="content">
  
    <xsl:apply-templates select="../trail" />
  
    <xsl:choose>
    
      <xsl:when test="@type='columns'">
        <xsl:call-template name="column-content" />
      </xsl:when>
      
      <xsl:when test="@type='mixed'">
        <xsl:call-template name="mixed-content" />
      </xsl:when>
      
      <xsl:otherwise>
        <xsl:copy-of select="node()" />
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>
  
  <xsl:template name="column-content">
    <h1><xsl:value-of select="header" /></h1>
    
    <table width="100%" cellpadding="2" cellspacing="0" border="0">
      <tr>
        <xsl:for-each select="column">
          <td valign="top" align="left">
            <xsl:if test="@width">
              <xsl:attribute name="width"><xsl:value-of select="@width" /></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="box" />  
            <xsl:apply-templates select="html" />              
          </td>
        </xsl:for-each>
      </tr>
    </table>
    
  </xsl:template>
  

  <!-- Template for a "trail" of links to this page -->
  <xsl:template match="trail">
    <div class="trail" align="right">
      [
        <xsl:for-each select="ancestor">
          <a href="{@href}"><xsl:value-of select="@title" /></a>
          <xsl:if test="not(position() = last())">
            <xsl:text> | </xsl:text>
          </xsl:if>
        </xsl:for-each>
      ]
    </div>
  </xsl:template>
  
  
  <xsl:template match="box">
      <xsl:call-template name="RoundedBox">
        <xsl:with-param name="Title"><xsl:value-of select="@title" /></xsl:with-param>
        <xsl:with-param name="Contents">
          <xsl:copy-of select="node()" />
        </xsl:with-param>
      </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="mixed-content">
    <xsl:apply-templates />
  </xsl:template>  
  
  <xsl:template match="html">
    <xsl:copy-of select="node()" />
  </xsl:template>
  
  <xsl:template match="figure">
    <div align="center">
      <div class="thinborder">
        <img src="{@src}" alt="{@caption}">
          <xsl:if test="boolean(@width)">
            <xsl:attribute name="width"><xsl:value-of select="@width" /></xsl:attribute>
          </xsl:if>
          <xsl:if test="boolean(@height)">
            <xsl:attribute name="height"><xsl:value-of select="@height" /></xsl:attribute> 
          </xsl:if>
        </img>
      </div>
      <div class="caption">
        Figure <xsl:value-of select="count(preceding::figure)+1" />: <xsl:value-of select="@caption" />
      </div>
    </div>    
  </xsl:template>
  
  <xsl:template match="codesnip">
    <div align="center">
      <pre class="code">
        <xsl:choose>
          <xsl:when test="boolean(@src)">
            <!-- Need to figure out how to do this -->
          </xsl:when>
          <xsl:otherwise>
<xsl:value-of select="." />
          </xsl:otherwise>
        </xsl:choose>
      </pre>
      <div class="caption">
        Code Snip <xsl:value-of select="count(preceding::codesnip)+1" />: <xsl:value-of select="@caption" />
      </div>      
    </div>
  </xsl:template>
  
  <!--
      The navigator.
  -->
  <xsl:template match="navigator">
    <xsl:copy-of select="node()" />
  </xsl:template>
  
  <!--
      The page footer. You can stop this appearing by including a
      nofooter tag in your source document as a child of dubhpage
  -->
  <xsl:template name="footer">
    <xsl:if test="not(boolean(/dubhpage/nofooter))">
      <hr />
      <div class="dubhPageFooter">
        <table width="100%" border="0">
          <tr>
            <td valign="top">
              <p class="dubhPageFooter">
                Page Author: <xsl:call-template name="print-author" /><br />
                <xsl:apply-templates select="/dubhpage/cvs-info" />
                <!-- If there is no CVS info in the top level page, try the content -->
                
              </p>
            </td>
            <xsl:if test="not(boolean(/dubhpage/notvalidhtml))">
              <td align="center">
                <a href="http://validator.w3.org/check/referer">
                  <img border="0" src="/images/valid-html401.png" width="88" height="31" 
                    alt="This page is valid HTML 4.01 Transitional" />
                </a>
              </td>
            </xsl:if>
            <td valign="top">
              <div align="right" class="dubhPageFooter"> 
                Powered by <a href="http://otn.oracle.com/tech/java/oc4j/content.html">Oracle OC4J</a>
                and <a href="http://otn.oracle.com/tech/xml/xdk_java/content.html">Oracle XDK</a><br />
                Copyright (C) 1994 - 2002 Brian Duff<br />
              </div>
            </td>
          </tr>
        </table>
      </div>
    </xsl:if>
  </xsl:template>
  
  <!--
      Print the CVS info for the page if present
  -->
  <xsl:template match="cvs-info">
    
    <xsl:if test="boolean(@redirect)">
      <!--
        Oracle bug# 1722555 with the XSL processor
        CONFORMANCE: 2-ARGUMENT DOCUMENT() FUNCTION NOT RECOGNIZED
        Currently (15 April 2001) assigned and open. This means, the 
        redirect attribute is currently relative to the STYLESHEET, not the
        XML document.

        BDUFF: This bug is now fixed, bug I don't want to change the code
        for fear of breaking stuff...
      
      -->
      <xsl:variable name="redir" select="document(@redirect)" />
      <xsl:apply-templates select="$redir//cvs-info" />
    </xsl:if>
    
    <xsl:variable name="cvsdatetag">$Date: </xsl:variable>
    <xsl:variable name="cvsauthortag">$Author: </xsl:variable>
    <xsl:variable name="cvsrevisiontag">$Revision: </xsl:variable>
  
    <xsl:if test="boolean(date)">
      Last Updated: 
        <xsl:value-of select="substring-before(substring-after(date, $cvsdatetag), '$')" /> 
      <xsl:if test="boolean(author)">
        GMT <xsl:text> </xsl:text>by 
          <xsl:value-of select="substring-before(substring-after(author, $cvsauthortag), '$')" />
      </xsl:if>
      
      <br />
    </xsl:if>
    <xsl:if test="boolean(revision)">
      Revision: 
        <xsl:value-of select="substring-before(substring-after(revision, $cvsrevisiontag), '$')" />
    </xsl:if>
  </xsl:template>

  <!--
      Print the author of this page.
  -->
  <xsl:template name="print-author">
    <xsl:choose>
      <xsl:when test="boolean(/dubhpage/author)">
        <a href="mailto:{/dubhpage/author/@email}">
          <xsl:value-of select="/dubhpage/author/@name" />
        </a>
        <xsl:text> </xsl:text>
        (<xsl:value-of select="/dubhpage/author/@email" />)
      </xsl:when>
      
      <xsl:otherwise>
        <a href="mailto:{$default-author-email}">
          <xsl:value-of select="$default-author-name" />
        </a>
        <xsl:text> </xsl:text>
        (<xsl:value-of select="$default-author-email" />)
      </xsl:otherwise>
      
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>
