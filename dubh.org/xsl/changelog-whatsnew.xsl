<?xml version="1.0"?>

<!--
 + Stylesheet that shows a cut down change log
 + $Id: changelog-whatsnew.xsl,v 1.1 2001-06-25 23:20:34 briand Exp $
 + (C) 2001 Brian Duff
 -->
 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="dates.xsl" />
  <xsl:output method="html" doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
    doctype-system="http://www.w3.org/TR/html4/loose.dtd" />



	<xsl:template match="changelog">
		<div>
		<xsl:for-each select="change">
			<!-- dodgy way to sort by date -->
			<xsl:sort select="concat(date/@year, concat(date/@month, date/@day))"
					order="descending" data-type="number" />
			
			<xsl:if test="position() &lt;= 3">	<!-- show three items -->
				<h3><xsl:apply-templates select="date" /></h3>
			
				<xsl:apply-templates select="description" />
			</xsl:if>
			
		</xsl:for-each>
		</div>
	</xsl:template>

	<xsl:template match="description">
		<xsl:copy-of select="node()" />
	</xsl:template>
	
</xsl:stylesheet>