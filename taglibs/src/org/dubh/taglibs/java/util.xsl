<?xml version="1.0"?>
<!--
  This is a tag library that contains some useful utilities.
-->

<!-- Brian Duff (C) 2000 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsp="http://www.apache.org/1999/XSP/Core"
  xmlns:dtl="http://www.dubh.org/2000/DubhTagLib"
>
  <xsl:template match="xsp:page">
    <xsp:page>
      <xsl:apply-templates select="@*"/>

      <xsp:structure>
        <xsp:include>java.net.URL</xsp:include>
        <xsp:include>java.util.Date</xsp:include>
        <xsp:include>java.text.SimpleDateFormat</xsp:include>
        <xsp:include>org.xml.sax.SAXException</xsp:include>
        <xsp:include>org.apache.xalan.xslt.XSLTProcessorFactory</xsp:include>
        <xsp:include>org.apache.xalan.xslt.XSLTInputSource</xsp:include>
        <xsp:include>org.apache.xalan.xslt.XSLTResultTarget</xsp:include>
        <xsp:include>org.apache.xalan.xslt.XSLTProcessor</xsp:include>
        <xsp:include>org.w3c.dom.Document</xsp:include>
      </xsp:structure>
      
      <xsp:logic>
        private Document transformXML(String xmlFile, String xslFile, 
          HttpServletRequest request, ServletContext context, String params )
          throws Exception
        {
          XSLTProcessor processor = XSLTProcessorFactory.getProcessor();
          Document out = new org.apache.xerces.dom.DocumentImpl();
          XSLTResultTarget resultTarget = new XSLTResultTarget(out);
          
          
          System.out.println("Params is "+params);
          if (params != null)
          {
            
            StringTokenizer pToker = new StringTokenizer(params, ",");
            while (pToker.hasMoreTokens())
            {
              String thisToken = pToker.nextToken();
              System.out.println("Token: "+thisToken);
              StringTokenizer equalToker = new StringTokenizer(thisToken, "=");
              String pName = equalToker.nextToken();
              String pValue = equalToker.nextToken();
              System.out.println("Set stylesheet parameter: "+pName+" = "+pValue);
              processor.setStylesheetParam(pName, pValue);
            }
          }


          processor.process(
            new XSLTInputSource(
              new FileReader(
                XSPUtil.relativeFilename(
                  xmlFile,
                  request,
                  context
                )
              )
            ),
            new XSLTInputSource(
              new FileReader(
                XSPUtil.relativeFilename(
                  xslFile,
                  request,
                  context
                )
              )
            ),
            resultTarget
          );

          return out;
        }
      </xsp:logic>

      <xsl:apply-templates/>
    </xsp:page>
  </xsl:template>

  <!-- Apply an XSL transformation to an XML document and include the resulting tree in the output  -->
  <xsl:template match="dtl:apply-xslt">
     <xsl:variable name="xmlfile">
       <xsl:choose>
         <xsl:when test="@xmlfile">"<xsl:value-of select="@xmlfile"/>"</xsl:when>
         <xsl:when test="dtl:xmlfile">
           <xsl:call-template name="get-nested-content">
             <xsl:with-param name="content" select="dtl:xmlfile"/>
           </xsl:call-template>
         </xsl:when>
       </xsl:choose>
     </xsl:variable>  
  
     <xsl:variable name="xslfile">
       <xsl:choose>
         <xsl:when test="@xslfile">"<xsl:value-of select="@xslfile"/>"</xsl:when>
         <xsl:when test="dtl:xslfile">
           <xsl:call-template name="get-nested-content">
             <xsl:with-param name="content" select="dtl:xslfile"/>
           </xsl:call-template>
         </xsl:when>
       </xsl:choose>
     </xsl:variable>  
  
     <xsl:variable name="params">
       <xsl:choose>
         <xsl:when test="@params">"<xsl:value-of select="@params"/>"</xsl:when>
         <xsl:when test="dtl:params">
           <xsl:call-template name="get-nested-content">
             <xsl:with-param name="content" select="dtl:params"/>
           </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>null</xsl:otherwise>
       </xsl:choose>
    </xsl:variable> 
  
    <xsp:logic>
      xspCurrentNode.appendChild(
        XSPUtil.cloneNode(
          transformXML(
            <xsl:copy-of select="$xmlfile"/>,
            <xsl:copy-of select="$xslfile"/>,
            request,
            (ServletContext) context,
            <xsl:copy-of select="$params" />
          ).getDocumentElement(),
          document
        )
      );
    </xsp:logic>
  </xsl:template>


  <!-- Standard Templates -->
  <xsl:template name="get-nested-content">
    <xsl:param name="content"/>
    <xsl:choose>
      <xsl:when test="$content/*">
        <xsl:apply-templates select="$content/*"/>
      </xsl:when>
      <xsl:otherwise>"<xsl:value-of select="$content"/>"</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="@*|*|text()|processing-instruction()">
    <xsl:copy>
      <xsl:apply-templates select="@*|*|text()|processing-instruction()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
