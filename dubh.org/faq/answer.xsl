<?xml version="1.0"?>

<!--
============================================================================
  Define several entities
  Maybe move some of this to a cascading stylesheet
============================================================================
-->
<!DOCTYPE xsl:stylesheet [
<!ENTITY nbsp "&#106;">
]>

<!--
============================================================================
  XSLT stylesheet for displaying an answer.
============================================================================
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:import href="xsqlerror.xsl" />

  <xsl:variable name="leftColWidth">80</xsl:variable>
  
  
  <xsl:template match="answer">
    <html>
      <table width="100%" border="0" cellpadding="3" cellspacing="0">
        <xsl:apply-templates select="question" />
        <xsl:apply-templates select="answers" />
      </table>
    </html>
  </xsl:template>

  <xsl:template match="question">
    <tr>
      <td width="{$leftColWidth}" class="bigHead" align="right">
        FAQ:
      </td>
      <td class="bigHead">
        <xsl:value-of select="title" />
      </td>
    </tr>

    <tr>
      <td width="{$leftColWidth}" class="smallHead" align="right">
        Category:
      </td>
      <td class="questionField">
        <xsl:value-of select="category" />
      </td>
    </tr>

    <tr>
      <td width="{$leftColWidth}" class="smallHead">&nbsp;</td>
      <td>&nbsp;</td>
    </tr>

    <tr>
      <td width="{$leftColWidth}" class="smallHead" align="right">
        Author:
      </td>
      <td class="questionField">
        <a href="mailto:{user_email}">
          <xsl:value-of select="user_forenames" />
          <xsl:text> </xsl:text>
          <xsl:value-of select="user_surname" />
        </a>
      </td>
    </tr>

    <tr>
      <td width="{$leftColWidth}" class="smallHead" align="right">
        Keywords:
      </td>
      <td class="questionField">
        <xsl:value-of select="keywords" />
      </td>
    </tr>

    <tr>
      <td width="{$leftColWidth}" class="smallHead">&nbsp;</td>
      <td>&nbsp;</td>
    </tr>    

    <xsl:if test="not(description='')">
      <tr>
        <td width="{$leftColWidth}" class="bigHead" valign="top" align="right">
          Q:
        </td>
        <td class="questionField" valign="top">
          <xsl:value-of select="description" />
        </td>
      </tr>

      <tr>
        <td width="{$leftColWidth}" class="smallHead">&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </xsl:if>

  </xsl:template>
  
  <xsl:template match="answers">
    <xsl:for-each select="answer">
      <tr>
        <td width="{$leftColWidth}" class="bigHead" valign="top" align="right">
          A:
        </td>
        <td class="answerField" valign="top">
          <pre>
            <xsl:value-of select="body" />
          </pre>
          <p class="answerAuthor">
            Answer Submitted by 
            <a href="mailto:{user_email}">
              <xsl:value-of select="user_forenames" />
              <xsl:text> </xsl:text>
              <xsl:value-of select="user_surname" />
            </a>
            <xsl:text> </xsl:text>on<xsl:text> </xsl:text>
            <xsl:value-of select="create_date" />
          </p>
        </td>
      </tr>
    </xsl:for-each>
  </xsl:template>
  
  <cvs-info>
    <date>
      $Date: 2001-04-03 22:17:09 $
    </date>
    <author>
      $Author: briand $
    </author>
    <revision>
      $Revision: 1.1 $
    </revision>
  </cvs-info>
  
</xsl:stylesheet>