<?xml version="1.0"?>

<!--
 + Stylesheet with various date-related utilities.
 + $Id: dates.xsl,v 1.1 2001-06-25 23:20:19 briand Exp $
 + (C) 2001 Brian Duff
 -->
 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<!-- Template that matches dates, which should have three
	 		attributes, day, year and month. The first two are both two digit
	 		numbers the third is a four digit number. Displays the date
	 		in "brian's favourite date format": "dd Mon YYYY"
	-->
	<xsl:template match="date">
		<xsl:value-of select="@day" />
		<xsl:text> </xsl:text>
		<xsl:choose>
			<xsl:when test="@month='01'">
				Jan
			</xsl:when>
			<xsl:when test="@month='02'">
				Feb
			</xsl:when>
			<xsl:when test="@month='03'">
				Mar
			</xsl:when>
			<xsl:when test="@month='04'">
				Apr
			</xsl:when>
			<xsl:when test="@month='05'">
				May
			</xsl:when>
			<xsl:when test="@month='06'">
				Jun
			</xsl:when>
			<xsl:when test="@month='07'">
				Jul
			</xsl:when>
			<xsl:when test="@month='08'">
				Aug
			</xsl:when>
			<xsl:when test="@month='09'">
				Sep
			</xsl:when>
			<xsl:when test="@month='10'">
				Oct
			</xsl:when>
			<xsl:when test="@month='11'">
				Nov
			</xsl:when>
			<xsl:when test="@month='12'">
				Dec
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@month" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:value-of select="@year" />
	</xsl:template>
	
</xsl:stylesheet>