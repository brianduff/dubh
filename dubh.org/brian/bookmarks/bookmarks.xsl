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
    
   <!-- Parameter that controls whether we are in edit mode. In edit mode, there
        are a number of controls available for editing the underlying XML document
   -->
   <xsl:param name="editmode">false</xsl:param>
   
   <!-- Certain characters in identifies can't be used in javascript for object
        ids. We strip these offending characters out of certain strings before
        converting them into IDs. -->
   <xsl:variable name="badchars"> ().,-&amp;/?:</xsl:variable>
      
   <xsl:variable name="bgcolor">#ffffdd</xsl:variable>
   <xsl:variable name="topicbg">#a0a0e0</xsl:variable>
   <xsl:variable name="togglebg">#c0c0f0</xsl:variable>
   <xsl:variable name="tbButtonSize">16</xsl:variable>
   
   <xsl:template match="bookmarks">
      <html>
         <head>
            
         
            <title><xsl:value-of select="@owner" />'s Bookmarks</title>
            <script language="javascript" src="bookmarks.js" />
            
            <!-- Could use an external CSS here, but I want to avoid hardcoding some
                 color values & image size defaults -->
            <style type="text/css">

               body {
                  background-color: <xsl:value-of select="$bgcolor" />;
                  font-family: Tahoma, Verdana, sans-serif;
                  font-size: 8pt;
               }

               td {
                  font-family: Verdana, Tahoma, sans-serif;
                  font-size: 7pt;
               }

               th {
                  text-align: left;
                  font-size: 8pt;
                  font-family: Tahoma, Verdana, sans-serif;
                  background-color: <xsl:value-of select="$topicbg" />;   
               }

               h1 {
                  font-size: 10pt;
                  font-family: Verdana, sans-serif;
                  background-color: <xsl:value-of select="$topicbg" />;
               }

               img.toolbarFlat {
                  position: relative;
                  left: 1;
                  top: 1;
                  margin-bottom: 2;
                  margin-right: 2;

               }

               img.toolbarHover {
                  border-style: outset;
                  border-width: 1px;  
               }

               img.toolbarPressed {
                  border-style: inset;
                  border-width: 1px;
                  position: relative;
                  left: 1;
                  top: 1;
               }

               img.toolbarTogglePressed {
                  border-style: inset;
                  border-width: 1px;
                  background-color: <xsl:value-of select="$togglebg" />;
                  position: relative;
                  left: 1;
                  top: 1;
               }                  

               table.leftBorder {
                  border-left-style: solid;
                  border-left-width: <xsl:value-of select="$tbButtonSize" />px;
                  border-left-color: <xsl:value-of select="$bgcolor" />;
               }

               .emptyIcon {
                  width: <xsl:value-of select="$tbButtonSize + 2" />px;
                  height: <xsl:value-of select="$tbButtonSize + 2" />px;
               }

            </style>      

            
            <!-- We remember the collapsed state of each topic, and restore it on document load -->
            <script for="window" event="onload" language="javascript">
               restoreCollapseState();
            </script>
            
         </head>
         <body>
            <h1>
               Brian's Bookmarks
               <xsl:choose>
                  <xsl:when test="$editmode = 'true'">
                     <img id="editModeToggle" src="editNormal.gif" class="toolbarTogglePressed"  width="{$tbButtonSize}" height="{$tbButtonSize}"
                        onMouseOver="javascript:toggleHoverIn(editModeToggle, 'editHover.gif')" 
                        onMouseOut="javascript:toggleHoverOut(editModeToggle, 'editNormal.gif')"
                        onMouseDown="javascript:imageMouseDown(editModeToggle)"
                        onMouseUp="javascript:imageMouseUp(editModeToggle)"
                        onClick="javascript:setEditMode(false)"
                        alt="Switch edit mode off"
                     />                                             
                  </xsl:when>
                  <xsl:otherwise>
                     <img id="editModeToggle" src="editNormal.gif" class="toolbarFlat"  width="{$tbButtonSize}" height="{$tbButtonSize}"
                        onMouseOver="javascript:imageHoverIn(editModeToggle, 'editHover.gif')" 
                        onMouseOut="javascript:imageHoverOut(editModeToggle, 'editNormal.gif')"
                        onMouseDown="javascript:imageMouseDown(editModeToggle)"
                        onClick="javascript:setEditMode(true)"
                        alt="Switch edit mode on"
                     />    
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
      
      <xsl:variable name="tableStyle"><xsl:choose><xsl:when test="name(..)='bookmarks'"></xsl:when><xsl:otherwise>leftBorder</xsl:otherwise></xsl:choose></xsl:variable>
   
      <table class="{$tableStyle}" width="100%" border="0" cellspacing="0" cellpadding="2" id="catTable{$catTitle}">   
      <thead id="catHead{$catTitle}">
         <tr class="category">
            <th width="75%" onClick="javascript:titleClicked(tableBody{$catTitle})">
               <img width="9" height="9" src="minus.gif" />
               <xsl:text> </xsl:text>
               <xsl:value-of select="@title" />
            </th>
            <th>
               <xsl:if test="$editmode='true'">
                  <p align="right">                                    
                     <xsl:call-template name="toolBarButton">
                        <xsl:with-param name="id"><xsl:value-of select="$catTitle" /></xsl:with-param>
                        <xsl:with-param name="class">plus</xsl:with-param>
                        <xsl:with-param name="clickArgs">'<xsl:value-of select="$catTitle" />'</xsl:with-param>
                        <xsl:with-param name="toolTip">Add a new URL to <xsl:value-of select="@title" /></xsl:with-param>
                     </xsl:call-template>
                     <xsl:call-template name="toolBarButton">
                        <xsl:with-param name="id"><xsl:value-of select="$catTitle" /></xsl:with-param>
                        <xsl:with-param name="class">configure</xsl:with-param>
                        <xsl:with-param name="clickArgs">'<xsl:value-of select="$catTitle" />'</xsl:with-param>
                        <xsl:with-param name="toolTip">Change the title of this category, or add subcategories</xsl:with-param>
                     </xsl:call-template>                     
                  </p>
               </xsl:if>
            </th>
         </tr>
      </thead>

      <tbody id="tableBody{$catTitle}">
         <xsl:if test="link">
            <tr>
               <td colspan="2">
                  <xsl:for-each select="link">
                  
                     <xsl:if test="$editmode='true'">
                        <xsl:variable name="itemId">
                           <xsl:value-of select="translate(@title, $badchars, '')" />
                        </xsl:variable>
                        
                        
                        <xsl:call-template name="toolBarButton">
                           <xsl:with-param name="id">link<xsl:value-of select="$itemId" /></xsl:with-param>
                           <xsl:with-param name="class">delete</xsl:with-param>
                           <xsl:with-param name="clickArgs">'<xsl:value-of select="$itemId" />'</xsl:with-param>
                           <xsl:with-param name="toolTip">Remove <xsl:value-of select="@title" /> from bookmarks</xsl:with-param>
                        </xsl:call-template>
                        
                        
                        <!-- Don't display the move down button for the last item in the list -->
                        <xsl:choose>
                           <xsl:when test="position() != last()">
                              
                              <xsl:call-template name="toolBarButton">
                                 <xsl:with-param name="id">link<xsl:value-of select="$itemId" /></xsl:with-param>
                                 <xsl:with-param name="class">moveDown</xsl:with-param>
                                 <xsl:with-param name="clickArgs">'<xsl:value-of select="$itemId" />'</xsl:with-param>
                                 <xsl:with-param name="toolTip">Move <xsl:value-of select="@title" /> down in the <xsl:value-of select="../@title" /> category</xsl:with-param>
                              </xsl:call-template> 
                              
                              
                           </xsl:when>
                           <xsl:otherwise>
                              <span class="emptyIcon" />
                           </xsl:otherwise>
                        </xsl:choose>
                        
                        <!-- Don't display move up button for the first item in the list -->
                        <xsl:choose>
                           <xsl:when test="position() != 1">
                              
                              <xsl:call-template name="toolBarButton">
                                 <xsl:with-param name="id">link<xsl:value-of select="$itemId" /></xsl:with-param>
                                 <xsl:with-param name="class">moveUp</xsl:with-param>
                                 <xsl:with-param name="clickArgs">'<xsl:value-of select="$itemId" />'</xsl:with-param>
                                 <xsl:with-param name="toolTip">Move <xsl:value-of select="@title" /> up in the <xsl:value-of select="../@title" /> category</xsl:with-param>
                              </xsl:call-template>
                              
                           </xsl:when>
                           <xsl:otherwise>
                              <span class="emptyIcon" />
                           </xsl:otherwise>
                        </xsl:choose>
                        
                        <xsl:call-template name="toolBarButton">
                           <xsl:with-param name="id">link<xsl:value-of select="$itemId" /></xsl:with-param>
                           <xsl:with-param name="class">configure</xsl:with-param>
                           <xsl:with-param name="clickArgs">'<xsl:value-of select="$itemId" />'</xsl:with-param>
                           <xsl:with-param name="toolTip">Change the URL or Title of <xsl:value-of select="@title" /></xsl:with-param>
                        </xsl:call-template>

                        <xsl:text> </xsl:text>
                     </xsl:if>                  
                     <img src="x1navy.gif" width="4" height="4"/><xsl:text> </xsl:text>
                     <a href="{@href}"><xsl:value-of select="@title" /></a>
                     

                     
                     <br />
                  </xsl:for-each>
               </td>
            </tr>
         </xsl:if>
         <xsl:if test="category">
            <tr>
               <td colspan="2">
                  <xsl:apply-templates select="category" />
               </td>
            </tr>
         </xsl:if>
      </tbody>
      </table>

   </xsl:template>
   
   <!-- A toolbar button "component". -->
   <xsl:template name="toolBarButton">
      <xsl:param name="id" />
      <xsl:param name="class" />
      <xsl:param name="clickArgs" />
      <xsl:param name="toolTip" />
      
      
      <img id="{$class}{$id}" src="{$class}Normal.gif" class="toolbarFlat"  width="{$tbButtonSize}" height="{$tbButtonSize}"
         onMouseOver="javascript:imageHoverIn({$class}{$id}, '{$class}Hover.gif')" 
         onMouseOut="javascript:imageHoverOut({$class}{$id}, '{$class}Normal.gif')"
         onMouseDown="javascript:imageMouseDown({$class}{$id})"
         onMouseUp="javascript:imageMouseUp({$class}{$id})"
         onClick="javascript:{$class}Clicked({$clickArgs})"
         alt="{$toolTip}"
      />  
      
      
   </xsl:template>
    
</xsl:stylesheet>