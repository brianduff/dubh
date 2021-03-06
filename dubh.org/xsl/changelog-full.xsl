<?xml version="1.0"?>

<!--
 + Stylesheet that shows the full changelog in glorious HTML
 + $Id: changelog-full.xsl,v 1.1 2001-06-25 23:20:34 briand Exp $
 + (C) 2001 Brian Duff
 -->
 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="dates.xsl" />
  <xsl:output method="html" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
    doctype-system="http://www.w3.org/TR/html4/loose.dtd" />



	<xsl:template match="changelog">
		<div>
		<h1>Changes to Dubh.Org</h1>
		
		<xsl:for-each select="change">
			<!-- dodgy way to sort by date -->
			<xsl:sort select="concat(date/@year, concat(date/@month, date/@day))"
					order="descending" data-type="number" />
					
			<h2><xsl:apply-templates select="date" /></h2>
			
			<xsl:apply-templates select="description" />
			
		</xsl:for-each>
		</div>
	</xsl:template>

	<xsl:template match="description">
		<xsl:copy-of select="node()" />
	</xsl:template>
	
</xsl:stylesheet>