<?xml version="1.0"?>

<!--
============================================================================
  Stylesheet for generating a list of questions
============================================================================
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:import href="xsqlerror.xsl" />

  <!--
  ============================================================================
    Top level template, just used to define the order in which the other
    templates in this stylesheet are called. First we display the category
    heading, then the list of questions
  ============================================================================
  -->
  <xsl:template match="question-info">
    <span class="questioninfo">
      <xsl:apply-templates select="heading" />
      <xsl:apply-templates select="question-list" />
    </span>
  </xsl:template>

  <!--
  ============================================================================
    The heading that is displayed above the list of questions
  ============================================================================
  -->
  <xsl:template match="heading">
    <h2>
      <xsl:value-of select="." />
    </h2>
  </xsl:template>


  <!--
  ============================================================================
    Renders the actual list of questions in this category
  ============================================================================
  -->
  <xsl:template match="question-list">
    <xsl:choose>
      <xsl:when test="boolean(question)">
        <table width="100%" border="0">
          <tr>
            <th>Question</th>
          </tr>

          <xsl:for-each select="question">
            <tr>
              <td>
                <a href="answers.xml?qid={id}">
                  <xsl:value-of select="title" />
                </a>
              </td>
            </tr>
          </xsl:for-each>

        </table>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="not(/question-info/catid='')">
          <xsl:choose>
            <xsl:when test="boolean(../noquestions-msg)">
              <xsl:copy-of select="../noquestions-msg" />
            </xsl:when>
            <xsl:otherwise>
              <p>
                There are no questions to display.
              </p>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  
</xsl:stylesheet>