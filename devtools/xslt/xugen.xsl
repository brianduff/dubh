<?xml version="1.0"?>

<!-- Generates java source code for UI specified in an XML file 

(C) 2000 Brian Duff -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="text" />

<xsl:template match="xugen">
  <xsl:processing-instruction name="cocoon-format">type="text/plain"</xsl:processing-instruction>
<text>// @(#)<xsl:value-of select="@name" />.java 
// 
//
// DO NOT MODIFY THIS FILE: It is generated from an XML document using 
// a transforming stylesheet.
//
// uigen.xsl (C) 2000 Brian Duff
//
package <xsl:value-of select="@package" />;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;   
<xsl:apply-templates select="import" />

/**
<xsl:apply-templates select="description" />
<xsl:if test="boolean(author)"> 
 *
 * @author <xsl:value-of select="author" /></xsl:if>
 */
<xsl:value-of select="@access" /> class <xsl:value-of select="@name" /> extends JPanel
{
<xsl:apply-templates select="//component" mode="declarations" />
<xsl:apply-templates select="//jbutton" mode="declarations" />
<xsl:apply-templates select="//jlabel" mode="declarations" />
<xsl:apply-templates select="//table" mode="declarations" />

<xsl:apply-templates select="//field" />
   
  /**
   * Constructor for <xsl:value-of select="@name" />
   */
  public <xsl:value-of select="@name" />()
  {
    super();
    setLayout(new BorderLayout());
    <xsl:apply-templates select="//component" mode="constructors" />
    <xsl:apply-templates select="//jbutton" mode="constructors" />
    <xsl:apply-templates select="//jlabel" mode="constructors" />
    <xsl:apply-templates select="//table" mode="constructors" />
    setComponentProperties();
    layEverythingOut();
    
    <xsl:apply-templates select="init" />
  }
  
  /**
   * Lay out all components
   */
  private void layEverythingOut()
  {
    GridBagConstraints gbc;
    Container thisContainer;
    int rowNum;
    int cellSpacing;
    
    this.add(m_<xsl:value-of select="table/@name"/>, BorderLayout.CENTER);
    
    <xsl:apply-templates select="//table" mode="layout" />
  }
  
  /**
   * Set up initial properties of all components
   */
  private void setComponentProperties()
  {
    <xsl:apply-templates select="//component" mode="properties" />
    <xsl:apply-templates select="//jbutton" mode="properties" />
    <xsl:apply-templates select="//jlabel" mode="properties" />
    <xsl:apply-templates select="//table" mode="properties" />
  }
  
  /**
   * This method tests the UI, displaying it in a JFrame.
   */
  protected static void testUI()
  {
    javax.swing.JFrame jf = new javax.swing.JFrame("<xsl:value-of select="@name" /> Test Harness");
    jf.getContentPane().setLayout(new java.awt.BorderLayout());
    <xsl:value-of select="@name" /> me = new <xsl:value-of select="@name" />();
    jf.getContentPane().add(me, java.awt.BorderLayout.CENTER);
    jf.pack();
    jf.setVisible(true);
    
  }
  
  /**
   * The main method just runs testUI(). It is commented out in
   * generated code, you can uncomment it to run <xsl:value-of select="@name" />
   * as a standalone application for testing.
   *
  public static void main(String[] args)
  {
    testUI();
  }
  */
  
  <xsl:apply-templates select="//method" mode="define" />
  
  
  <xsl:apply-templates select="//component" mode="getters" />
  <xsl:apply-templates select="//jlabel" mode="getters" />
  <xsl:apply-templates select="//jbutton" mode="getters" />
  <xsl:apply-templates select="//table" mode="getters" />
  
  
   
}
   
   
   </text>
</xsl:template>



<xsl:template match="import">import <xsl:value-of select="." />;
</xsl:template>

<xsl:template match="description"> * <xsl:value-of select="." /></xsl:template>

<xsl:template match="component" mode="declarations"> <!-- can I use | here ? -->
  private <xsl:value-of select="@class" /> m_<xsl:value-of select="@name"/>;</xsl:template>
  
<xsl:template match="jlabel" mode="declarations">
  private JLabel m_<xsl:value-of select="@name"/>;</xsl:template>

<xsl:template match="jbutton" mode="declarations">
  private JButton m_<xsl:value-of select="@name"/>;</xsl:template>
  
<xsl:template match="table" mode="declarations">
  <xsl:choose><xsl:when test="boolean(@class)">
  private <xsl:value-of select="@class" /></xsl:when>
  <xsl:otherwise>private JPanel</xsl:otherwise></xsl:choose> m_<xsl:value-of select="@name" />;</xsl:template>

<xsl:template match="component" mode="constructors">
    m_<xsl:value-of select="@name"/> = new <xsl:value-of select="@class"/>();</xsl:template>

<xsl:template match="jlabel" mode="constructors">
    m_<xsl:value-of select="@name"/> = new JLabel();</xsl:template>

<xsl:template match="jbutton" mode="constructors">
    m_<xsl:value-of select="@name" /> = new JButton();</xsl:template>
    
<xsl:template match="table" mode="constructors">
    m_<xsl:value-of select="@name" /> = 
      <xsl:choose><xsl:when test="boolean(@class)">new <xsl:value-of select="@class" />();</xsl:when>
      <xsl:otherwise>new JPanel();</xsl:otherwise></xsl:choose></xsl:template>

<xsl:template match="component" mode="getters">
  /**
   * Get hold of the <xsl:value-of select="@name"/> component. 
   * @return a UI component for <xsl:value-of select="@name"/>
   */
  public <xsl:value-of select="@class"/> get<xsl:value-of select="@name"/>()
  {
    return m_<xsl:value-of select="@name" />;
  }
</xsl:template>

<xsl:template match="jlabel" mode="getters">
  /**
   * Get hold of the <xsl:value-of select="@name"/> label.
   * @return a JLabel for <xsl:value-of select="@name"/>
   */
  public JLabel get<xsl:value-of select="@name"/>()
  {
    return m_<xsl:value-of select="@name" />;
  }
</xsl:template>

<xsl:template match="jbutton" mode="getters">
  /**
   * Get hold of the <xsl:value-of select="@name"/> button.
   * @return a JButton for <xsl:value-of select="@name"/>
   */
  public JButton get<xsl:value-of select="@name"/>()
  {
    return m_<xsl:value-of select="@name" />;
  }
</xsl:template>

<xsl:template match="table" mode="getters">
  /**
   * Get hold of the <xsl:value-of select="@name"/> table / container.
   * @return a Container for <xsl:value-of select="@name"/>
   */
  protected Container get<xsl:value-of select="@name"/>()
  {
    return (Container)m_<xsl:value-of select="@name" />;
  }
</xsl:template>

<xsl:template match="table" mode="layout">
      thisContainer = (Container)m_<xsl:value-of select="@name"/>;
      rowNum = -1;
      <xsl:choose><xsl:when test="boolean(@cellspacing)">cellSpacing = <xsl:value-of select="@cellspacing"/>;</xsl:when>
      <xsl:otherwise>cellSpacing = 2;</xsl:otherwise></xsl:choose>
  <xsl:for-each select="tr">
      rowNum++;
    <xsl:for-each select="td">
      gbc = new GridBagConstraints();
      gbc.gridx = <xsl:value-of select="position()-1"/>;
      gbc.gridy = rowNum;
      <xsl:choose><xsl:when test="boolean(@colspan)">gbc.gridwidth = <xsl:value-of select="@colspan" />;</xsl:when>
      <xsl:otherwise>gbc.gridwidth = 1;</xsl:otherwise></xsl:choose>
      gbc.gridheight = 1;
      gbc.weighty = 0.0;
      <xsl:choose><xsl:when test="boolean(@width)">gbc.weightx = <xsl:value-of select="@width" />;</xsl:when>
      <xsl:otherwise>gbc.weightx = 0.0;</xsl:otherwise></xsl:choose>
      gbc.fill = GridBagConstraints.BOTH;
      gbc.anchor = GridBagConstraints.NORTHWEST; // TODO: Use align/valign
      gbc.insets = new Insets(cellSpacing, cellSpacing, cellSpacing, cellSpacing);
      thisContainer.<xsl:choose>
      <xsl:when test="boolean(component)">add(m_<xsl:value-of select="component/@name"/>, gbc);
      </xsl:when>
      <xsl:when test="boolean(jlabel)">add(m_<xsl:value-of select="jlabel/@name"/>, gbc);
      </xsl:when>
      <xsl:when test="boolean(jbutton)">add(m_<xsl:value-of select="jbutton/@name"/>, gbc);
      </xsl:when>
      <xsl:when test="boolean(table)">add(m_<xsl:value-of select="table/@name"/>, gbc);
      </xsl:when>
      </xsl:choose>
    </xsl:for-each>
  </xsl:for-each>
</xsl:template>

<xsl:template match="component" mode="properties">
<xsl:for-each select="property">
    m_<xsl:value-of select="../@name"/>.set<xsl:value-of select="@name"/>(<xsl:value-of select="@value"/>);
</xsl:for-each>
</xsl:template>

<xsl:template match="jlabel" mode="properties">
    <xsl:if test="boolean(@text)">m_<xsl:value-of select="@name"/>.setText(<xsl:value-of select="@text"/>);
    </xsl:if>
    <xsl:for-each select="property">
    m_<xsl:value-of select="../@name"/>.set<xsl:value-of select="@name"/>(<xsl:value-of select="@value"/>);
    </xsl:for-each>
</xsl:template>

<xsl:template match="jbutton" mode="properties">
    <xsl:if test="boolean(@text)">m_<xsl:value-of select="@name"/>.setText(<xsl:value-of select="@text"/>);
    </xsl:if>
    <xsl:for-each select="property">
    m_<xsl:value-of select="../@name"/>.set<xsl:value-of select="@name"/>(<xsl:value-of select="@value"/>);
    </xsl:for-each>
</xsl:template>

<xsl:template match="table" mode="properties">
    <xsl:if test="boolean(@borderTitle)">    m_<xsl:value-of select="@name"/>.setBorder(BorderFactory.createTitledBorder(
      <xsl:value-of select="@borderTitle"/>
    ));</xsl:if>
    <xsl:for-each select="property">
    m_<xsl:value-of select="../@name"/>.set<xsl:value-of select="@name"/>(<xsl:value-of select="@value"/>);
    </xsl:for-each>
    m_<xsl:value-of select="@name"/>.setLayout(new GridBagLayout());
    
</xsl:template>

<xsl:template match="method" mode="define">
  /**
   * <xsl:value-of select="@description"/><xsl:for-each select="param">
   *
   * @param <xsl:value-of select="@name"/><xsl:text> </xsl:text><xsl:value-of select="@description"/>
   </xsl:for-each>
   <xsl:if test="@return!='void'">
   * @return <xsl:value-of select="@retdesc" /></xsl:if>
   */
  <xsl:value-of select="@declarators" /><xsl:text> </xsl:text><xsl:choose><xsl:when test="boolean(@return)"><xsl:value-of select="@return"/><xsl:text> </xsl:text></xsl:when>
  <xsl:otherwise>void </xsl:otherwise></xsl:choose> <xsl:value-of select="@name" />(<xsl:for-each select="param"><xsl:value-of select="@type"/><xsl:text> </xsl:text><xsl:value-of select="@name" /><xsl:if test="not(position()=last())">, </xsl:if></xsl:for-each>)
  {
    <xsl:value-of select="." />
  }
</xsl:template>

<xsl:template match="field">
  /**
   * <xsl:value-of select="@description" />
   */
  <xsl:value-of select="@declarators" /><xsl:text> </xsl:text><xsl:value-of select="@type"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>;
</xsl:template>

<xsl:template match="init">
  // Extra initialization
  <xsl:value-of select="." />
</xsl:template>

</xsl:stylesheet>