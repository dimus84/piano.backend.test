<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="lang" select="'en'"/>
    
    <xsl:variable name="locale-strings">
        <xsl:text>locales/</xsl:text>
        <xsl:value-of select="$lang"/>
        <xsl:text>.xml</xsl:text>
    </xsl:variable>
    <xsl:variable name="locales" select="document($locale-strings)/locales"/>
    
    <xsl:template name="locales-translate">
        <xsl:param name="source"/>
        <xsl:choose>
            <xsl:when test="$locales/locale[@name=$source]">
                <xsl:value-of select="$locales/locale[@name=$source]/@value"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$source"/>
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>
</xsl:stylesheet>
