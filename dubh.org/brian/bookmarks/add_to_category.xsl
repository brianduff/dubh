<?xml version="1.0"?>
<!-- 
==============================================================================
Add to Category Form
==============================================================================
Author: Brian.Duff@oracle.com
==============================================================================
This Stylesheet transforms into the "add category" form.
==============================================================================
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    version="1.0">
    
   <!-- 
      Parameter that is passed in to indicate the id of the category
      this form was invoked on. This is passed through when the user submits
      the form
   -->
   <xsl:param name="catid" />
   
   <!--
      Parameter that is passed in to indicate the name of the category
      this form was invoked on. This is used to display the category name
      in the title
   -->
   <xsl:param name="catname" />
   

   <xsl:template match="/">
      <html>
         <head>
            <title>Add to Category <xsl:value-of select="$catname" /></title>
            <link rel="stylesheet" type="text/css" href="bookmarks.css" />

            <script language="javascript" src="add_to_category.js" />

         </head>

         <body onload="javascript:init()">

         <form method="POST">
            <h1>Add to Category <xsl:value-of select="$catname" /></h1>
            <table border="0">
               <tr>
                  <td colspan="3">
                     <input id="categoryId" type="hidden" value="{$catid}" />
                  </td>
               </tr>
               <tr>
                  <td colspan="3">
                     <input id="bookmarkRadio" type="radio" value="bookmark" 
                        name="rbType"
                        onclick="javascript:bookmarkRadioSelected(true)" />
                     <label for="bookmarkRadio">Add a Bookmark</label>
                  </td>
               </tr>
               <tr>
                  <td width="20px" />
                  <td>
                     <label for="bookmarkTitle" id="bookmarkTitleLabel">
                        Title:
                     </label>
                  </td>
                  <td>
                     <input type="text" id="bookmarkTitle" name="bookmarkTitle" 
                        size="30" />
                  </td>
               </tr>
               <tr>
                  <td width="20px" />
                  <td>
                     <label for="bookmarkURL" id="bookmarkURLLabel">
                        URL:
                     </label>
                  </td>
                  <td>
                     <input id="bookmarkURL" type="text" name="bookmarkURL" 
                        size="30" />
                  </td>
               </tr>
               <tr>
                  <td colspan="3" />
               </tr>
               <tr>
                  <td colspan="3">
                     <input id="categoryRadio" type="radio" name="rbType" 
                        value="category"
                        onclick="javascript:bookmarkRadioSelected(false)" />
                     <label for="categoryRadio">Add a New Subcategory</label>
                  </td>
               </tr>
               <tr>
                  <td width="20px" />
                  <td>
                     <label id="categoryTitleLabel" for="categoryTitle">
                        Title:
                     </label>
                  </td>
                  <td>
                     <input id="categoryTitle" type="text" name="categoryTitle" 
                        size="30" />
                  </td>
               </tr>
               <tr>
                  <td align="right" colspan="3">
                     <input onclick="javascript:doSubmit(); return false;" type="submit" 
                        value="      Add      " name="addButton" />
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
