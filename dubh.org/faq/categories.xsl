<?xml version="1.0"?>

<!-- XSL for displaying the categories list -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:import href="xsqlerror.xsl" />

  <xsl:template match="category-info">
    <span class="catinfo">
      <xsl:apply-templates select="parentcat" />
      <xsl:apply-templates select="category-list" />
    </span>
  </xsl:template>

  <xsl:template match="parentcat">
    <xsl:if test="boolean(../category-list/category)">
      <h2>
        <xsl:choose>
          <xsl:when test="not(text()='')">
            Subcategories in <xsl:value-of select="." />
          </xsl:when>
          <xsl:otherwise>
            Main Categories
          </xsl:otherwise>
        </xsl:choose>
      </h2>
    </xsl:if>
  </xsl:template>

  <xsl:template match="category-list">
    <xsl:if test="boolean(category)">
      <table width="100%" border="0">
        <tr>
          <th>Category</th>
          <th>Description</th>
          <th>Creator</th>
        </tr>

        <xsl:for-each select="category">
          <tr>
            <td>
              <a href="categories.xml?catid={id}">
                <xsl:value-of select="title" />
              </a>
            </td>
            <td><xsl:value-of select="description" /></td>
            <td><xsl:value-of select="created_by" /></td>
          </tr>
        </xsl:for-each>

      </table>
    </xsl:if>

  </xsl:template>
  
  
</xsl:stylesheet>