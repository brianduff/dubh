

<!-- Copyright (C) 2000, 2001 Brian Duff / Dubh.Org -->
<!-- XML stylesheet for a "dubh page" -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" />
  
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
    <xsl:choose>
    
      <xsl:when test="@type='columns'">
        <xsl:call-template name="column-content" />
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
  
  
  <xsl:template match="box">
    <p>
      <xsl:call-template name="RoundedBox">
        <xsl:with-param name="Title"><xsl:value-of select="@title" /></xsl:with-param>
        <xsl:with-param name="Contents">
          <xsl:copy-of select="." />
        </xsl:with-param>
      </xsl:call-template>
    </p>
  </xsl:template>
  
  <xsl:template match="html">
    <xsl:copy-of select="." />
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
      <span class="dubhPageFooter">
        <table width="100%" border="0">
          <tr>
            <td width="70%" valign="top">
              <p class="dubhPageFooter">
                Page Author: <xsl:call-template name="print-author" /><br />
                <xsl:apply-templates select="/dubhpage/cvs-info" />
                <!-- If there is no CVS info in the top level page, try the content -->
                
              </p>
            </td>
            <td width="30%" valign="top">
              <p class="dubhPageFooter">
                Powered by <a href="http://www.oracle.com">Oracle XML</a>
                and <a href="http://www.apache.org">Apache Tomcat</a><br />
                Copyright &#169; 1994 - 2001 Brian Duff<br />
              </p>
            </td>
          </tr>
        </table>
      </span>
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
        Currently (15 April 200) assigned and open. This means, the 
        redirect attribute is currently relative to the STYLESHEET, not the
        XML document.
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
        <xsl:text> </xsl:text>by 
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