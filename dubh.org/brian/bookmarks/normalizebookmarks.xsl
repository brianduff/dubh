<!-- 
 = 
 = Rather convoluted stylesheet that puts the results of bookmarks.xsql back
 = into the same format as the old bookmarks.xml.
 =
 = Copyright (C) 2001 Brian.Duff@oracle.com
 = $Id: normalizebookmarks.xsl,v 1.1 2001-04-29 17:54:53 briand Exp $
 = 
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <!-- Root template -->
   
   <!--
      = We directly process the bookmarks element only
   -->
   <xsl:template match="/">
      <xsl:apply-templates select="bookmarks" />
   </xsl:template>
   
   <!--
      = We generate out a bookmarks tag and apply the categories template
   -->
   <xsl:template match="bookmarks">
      <bookmarks>
         <xsl:apply-templates select="categories" />
      </bookmarks>
   </xsl:template>
   
   <!--
      = The categories template simply applys the category template in
      = topcats mode to category subelements
   -->
   <xsl:template match="categories">
      <xsl:apply-templates select="category" mode="topcats" />
   </xsl:template>
   
   <!--
      = This just calls the subcategory template, but only on top level 
      = categories (i.e. where parent_cat does not exist)
   -->
   <xsl:template match="category" mode="topcats">
      <xsl:if test="not(boolean(parent_cat))">
         <xsl:call-template name="subcategory" />
      </xsl:if>
   </xsl:template>
   
   <!--
      = Recursive template that outputs a category element and link 
      = elements for each link in this category, then calls itself for
      = all categories with this category as their parent_cat. Phew.
   -->
   <xsl:template name="subcategory">
      <xsl:variable name="my-id"><xsl:value-of select="id" /></xsl:variable>
      
      <category title="{title}" id="{id}">
      
         <!-- Links for this category -->
         <xsl:apply-templates select="../../links/link[category=$my-id]" />
      
         
         <!-- subcategories for this category -->
         <xsl:for-each select="../category[parent_cat=$my-id]">
            <xsl:call-template name="subcategory" />
         </xsl:for-each>
      
      </category>
   
   </xsl:template>
   
   <!--
      = Simple template for links
   -->
   <xsl:template match="link">
      <link href="{url}" title="{title}" id="{id}"/>
   </xsl:template>
   
</xsl:stylesheet>
