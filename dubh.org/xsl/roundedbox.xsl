<?xml version="1.0"?>

<!-- Copyright (C) 2000, 2001 Brian Duff / Dubh.Org -->
<!-- XML stylesheet for a box with rounded corners-->
<!-- Portions of this stylesheet were based on Example 17-10 in
     "Building Oracle XML Applications" by Steve Muench
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" />
  
  
  <xsl:template name="RoundedBox">
    <xsl:param name="Title" />
    <xsl:param name="Contents" />
    <xsl:param name="TitleColor">#CCCC99</xsl:param>
    <xsl:param name="TitleFgColor">#000000</xsl:param>
    <xsl:param name="ContentsColor">#F7F7E7</xsl:param>
    <xsl:param name="ContentsFgColor">#000000</xsl:param>
    
    
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td width="1%" valign="top" align="left" bgcolor="{$TitleColor}">
          <img src="/images/TL.gif" width="15" height="15" alt="Rounded Corner, Top Left" />
        </td>
        <th nowrap="nowrap" width="97%" align="left" valign="middle" bgcolor="{$TitleColor}">
         
          <xsl:text>&#160;&#160;</xsl:text>
          
          <xsl:copy-of select="$Title" />
        </th>
        <td width="1%" valign="top" align="left" bgcolor="{$TitleColor}">
          <xsl:text>&#160;&#160;&#160;</xsl:text>
        </td>
        <td width="1%" valign="top" align="right" bgcolor="{$TitleColor}">
          <img src="/images/TR.gif" width="15" height="15" alt="Rounded Corner, Top Right" />
        </td>
      </tr>
      <tr>
        <td width="1%" align="right" valign="top" bgcolor="{$ContentsColor}">
          <xsl:text>&#160;&#160;&#160;</xsl:text>
        </td>
        <td bgcolor="{$ContentsColor}" colspan="3">
          <xsl:copy-of select="$Contents" />
        </td>
      </tr>
      <tr>
        <td width="1%" align="left" valign="bottom" bgcolor="{$ContentsColor}" style="margin-bottom:0px; padding-bottom: 0px;">
                            <xsl:text>&#160;&#160;&#160;</xsl:text>
          <!--<img src="/images/BL.gif" width="15" height="15" alt="Rounded Corner, Bottom Left" style="margin-bottom:0px; padding-bottom: 0px;"/>-->
        </td>
        <td colspan="2" bgcolor="{$ContentsColor}" width="98%"  style="margin-bottom:0px; padding-bottom: 0px;">
                    <xsl:text>&#160;&#160;&#160;</xsl:text>
        </td>
        <td width="1%" align="right" valign="bottom" bgcolor="{$ContentsColor}"  style="margin-bottom:0px; padding-bottom: 0px;">
                            <xsl:text>&#160;&#160;&#160;</xsl:text>
          <!--<img src="/images/BR.gif" width="15" height="15" alt="Rounded Corner, Bottom Right" style="margin-bottom:0px; padding-bottom: 0px;"/> -->
        </td>
      </tr>
    </table>
    
  </xsl:template>
  
</xsl:stylesheet>