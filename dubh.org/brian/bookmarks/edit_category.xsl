<?xml version="1.0"?>
<!-- 
==============================================================================
Edit Category Form
==============================================================================
Author: Brian.Duff@oracle.com
==============================================================================
This Stylesheet transforms into the "edit bookmark" form
==============================================================================
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    version="1.0">
    
   <!-- 
      Parameter that is passed in to indicate the id of the bookmark
   -->
   <xsl:param name="id" />
   
   <!--
      Parameter that is passed in to indicate the title of the 
      bookmark
   -->
   <xsl:param name="title" />
   

   <xsl:template match="/">
      <html>
         <head>
            <title>Edit Bookmark</title>
            <link rel="stylesheet" type="text/css" href="bookmarks.css" />

            <script language="javascript" src="edit_category.js" />

         </head>

         <body onload="javascript:init()">

         <form method="POST">
            <h1>Edit Category ID <xsl:value-of select="$id" /></h1>
            <table border="0">
               <tr>
                  <td colspan="2">
                     <input id="catId" type="hidden" value="{$id}" />
                  </td>
               </tr>
               <tr>
                  <td>
                     <label for="categoryTitle" id="categoryTitleLabel">
                        Title:
                     </label>
                  </td>
                  <td>
                     <input type="text" id="categoryTitle" name="categoryTitle" 
                        size="30" value="{$title}" />
                  </td>
               </tr>
               <tr>
                  <td align="right" colspan="2">
                     <input onclick="javascript:doSubmit(); return false;" type="submit" 
                        value="   Update   " name="addButton" />
                     <input type="button" value="   Cancel   " name="cancelButton"
                        onclick="javascript:returnToBookmarks();" />
                  </td>
               </tr>
            </table>
         </form>
         </body>
      </html>
   </xsl:template>
</xsl:stylesheet>
