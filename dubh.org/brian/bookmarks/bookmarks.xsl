<?xml version="1.0"?>
<!-- 
==============================================================================
Bookmarks Stylesheet
==============================================================================
Author: Brian.Duff@oracle.com
Last Updated: 21 January 2001
==============================================================================
This stylesheet formats my bookmarks XML thang.
==============================================================================
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    version="1.0">
   
   <xsl:output method="html" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" />
   
   <!-- Parameter that controls whether we are in edit mode. In edit mode, there
        are a number of controls available for editing the underlying XML document
   -->
   <xsl:param name="editmode">false</xsl:param>
   
   <!-- Certain characters in identifies can't be used in javascript for object
        ids. We strip these offending characters out of certain strings before
        converting them into IDs. -->
   <xsl:variable name="badchars"> ().,-&amp;/?:"</xsl:variable>
      
   <xsl:variable name="bgcolor">#ffffdd</xsl:variable>
   <xsl:variable name="topicbg">#a0a0e0</xsl:variable>
   <xsl:variable name="togglebg">#c0c0f0</xsl:variable>
   <xsl:variable name="tbButtonSize">16</xsl:variable>
   
   <xsl:template match="bookmarks">
      <html>
         <head>
         
            <title><xsl:value-of select="@owner" />'s Bookmarks</title>
            <script type="text/javascript" src="bookmarks.js" />
            
            <link rel="stylesheet" href="bookmarks.css" type="text/css" />
            
            <!-- We remember the collapsed state of each topic, and restore it on document load -->
            <script for="window" event="onload" type="text/javascript">
               restoreCollapseState();
            </script>
            
         </head>
         <body>
            <h1>
               Brian's Bookmarks
               <xsl:choose>
                  <xsl:when test="$editmode = 'true'">
                     [ <a href="javascript:setEditMode(false)">Switch Edit Mode Off</a> ]
                  </xsl:when>
                  <xsl:otherwise>
                     [ <a href="javascript:setEditMode(true)">Switch to Edit Mode</a> ]
                  </xsl:otherwise>
               </xsl:choose>
            </h1>
            
            <xsl:apply-templates select="category" />
         
         </body>
      </html>
   </xsl:template>
   
   <xsl:template match="category">
      <xsl:variable name="catTitle">
         <xsl:value-of select="translate(@title, $badchars, '')" />
      </xsl:variable>
      
      <!-- Indent 16 pixels for each ancestor category of this one -->
      <xsl:variable name="inset"><xsl:value-of select="count(ancestor::category) * 16" /></xsl:variable>
     
      <blockquote style="margin-left: {$inset}px; margin-right: 0px; margin-top: 0px; margin-bottom: 0px;">
         <p class="category">
            <xsl:attribute name="onclick">
               javascript:titleClicked("tableBody<xsl:value-of select="$catTitle" />")
            </xsl:attribute>
            
            <img width="9" height="9" src="minus.gif" border="0" alt="Collapse"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="@title" />
            
            <xsl:if test="$editmode='true'">
               <xsl:text> [ </xsl:text>
               <a>
                  <xsl:attribute name="href">javascript:addToCategory(&quot;<xsl:value-of select="@id" />&quot;,&quot;<xsl:value-of select="@title" />&quot;);</xsl:attribute>
                  Add
               </a>
               <xsl:text> | </xsl:text>
               <a>
                  <xsl:attribute name="href">javascript:editCategory(&quot;<xsl:value-of select="@id" />&quot;,&quot;<xsl:value-of select="@title" />&quot;);</xsl:attribute>
                  Edit
               </a>               
               <xsl:text> | </xsl:text>
               <a>
                  <xsl:attribute name="href">javascript:deleteCategory(&quot;<xsl:value-of select="@id" />&quot;,&quot;<xsl:value-of select="@title" />&quot;);</xsl:attribute>
                  Delete
               </a>
               <xsl:text> ] </xsl:text>
            </xsl:if>
               
         </p>
   
         <div id="tableBody{$catTitle}">
         
            <xsl:if test="link">
               <xsl:for-each select="link">
                     
                  <img src="x1navy.gif" width="4" height="4" alt="Bullet"/><xsl:text> </xsl:text>
                  <xsl:if test="$editmode='true'">
                     <xsl:call-template name="itemEditButtons" />
                  </xsl:if>          
                          

                  <a href="{@href}" target="_content"><xsl:value-of select="@title" /></a>
                  <xsl:if test="position() != last()">
                     <br />
                  </xsl:if>
               </xsl:for-each>
            </xsl:if>
            
            <xsl:if test="category">
   
               <xsl:apply-templates select="category" />
   
            </xsl:if>
         </div>
      </blockquote>


   </xsl:template>
   
   
   <xsl:template name="itemEditButtons">
   
      <xsl:variable name="itemId">
         <xsl:value-of select="translate(@title, $badchars, '')" />
      </xsl:variable>
      
      <xsl:text> [ </xsl:text>
      <a>
        <xsl:attribute name="href">javascript:editBookmark(&quot;<xsl:value-of select="@id" />&quot;,&quot;<xsl:value-of select="@title" />&quot;,&quot;<xsl:value-of select="@href" />&quot;);</xsl:attribute>
        Edit
      </a>

      <xsl:text> | </xsl:text>
      <a>
        <xsl:attribute name="href">javascript:deleteBookmark(&quot;<xsl:value-of select="@id" />&quot;,&quot;<xsl:value-of select="@title" />&quot;);</xsl:attribute>
        Delete
      </a>
      <xsl:text> ] </xsl:text>
              
   </xsl:template>
   
    
</xsl:stylesheet>