<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:view="urn:ru:piano:backend:test:view"
    exclude-result-prefixes="xsl xsd"
    version="1.0">

    <xsl:output
        media-type="application/xhtml+xml"
        method="xml"
        encoding="UTF-8"
        indent="yes"
        omit-xml-declaration="no"
        doctype-public="-//W3C//DTD XHTML 1.1//EN"
        doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" />

    <xsl:param name="absolute.path"/>
    <xsl:param name="base.path"/>
    <xsl:param name="relative.path"/>
    <xsl:param name="webroot.path" select="substring-before($base.path, '/web/')"/>

    <xsl:strip-space elements="*" />

    <xsl:include href="common.xsl"/>

    <xsl:template match="/">
        <html xml:lang="ru">
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                <title>Stackexchange API</title>
                <xsl:call-template name="comon_css_and_js_includes">
                    <xsl:with-param name="webroot.path" select="$webroot.path"/>
                </xsl:call-template>
            </head>
            <body>
                <div class="outside header">
                    <div class="inside">
                        <xsl:call-template name="common_stackexchange_logo"/>
                    </div>
                </div>

                <div class="outside content">
                    <div class="inside">
                        <div>
                            <div>
                                <div class="subheader">
                                    <h1>Usage of  /search </h1>
                                </div>

                                <form method="get" action="{$absolute.path}">
                                    <div class="parameters">
                                        <table>
                                            <tbody>
                                                <tr>
                                                    <td style="width:25%;">
                                                        <label for="param-page">page</label>
                                                    </td>
                                                    <td style="width:25%;">
                                                        <input class="parameter-input number-type" id="param-page" name="page" type="text" />
                                                    </td>
                                                    <td style="width:25%;">
                                                        <label for="param-pagesize">pagesize</label>
                                                    </td>
                                                    <td style="width:25%;">
                                                        <input class="parameter-input number-type" id="param-pagesize" name="pagesize" type="text" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="width:25%;">
                                                        <label for="param-order">order</label>
                                                    </td>
                                                    <td style="width:25%;">
                                                        <select name="order" id="param-order" class="parameter-select">
                                                            <option>desc</option>
                                                            <option>asc</option>
                                                        </select>
                                                    </td>
                                                    <td style="width:25%;">
                                                        <label for="param-sort">sort</label>
                                                    </td>
                                                    <td style="width:25%;">
                                                        <select name="sort" id="param-sort" class="parameter-select">
                                                            <option>activity</option>
                                                            <option>votes</option>
                                                            <option>creation</option>
                                                            <option>relevance</option>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="width:25%;">
                                                        <label for="param-intitle">intitle</label>
                                                    </td>
                                                    <td colspan="3">
                                                        <input class="parameter-input" id="param-intitle" name="intitle" type="text" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="4">
                                                        <input type="submit" value="RUN" />
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </form>
                            </div>

                            <br/>

                            <xsl:apply-templates/>

                        </div>
                    </div>
                </div>

                <!--
                <table border="1">
                    <caption>XSLT PARAMS</caption>
                    <tr>
                        <td>absolute.path</td>
                        <td><xsl:value-of select="$absolute.path"/></td>
                    </tr>
                    <tr>
                        <td>base.path</td>
                        <td><xsl:value-of select="$base.path"/></td>
                    </tr>
                    <tr>
                        <td>relative.path</td>
                        <td><xsl:value-of select="$relative.path"/></td>
                    </tr>
                    <tr>
                        <td>webroot.path</td>
                        <td><xsl:value-of select="$webroot.path"/></td>
                    </tr>
                </table>
                -->
            </body>
        </html>
    </xsl:template>

    <xsl:template match="view:questions">
        <div>
            <div class="subheader">
                <h1>List of questions</h1>
            </div>

            <div class="parameters">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Title</th>
                            <th>Owner</th>
                            <th>Link</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates/>
                    </tbody>
                </table>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="view:question">
            <tr>
                <xsl:if test="view:is_answered='true'">
                    <xsl:attribute name="style">background-color:palegreen;</xsl:attribute>
                </xsl:if>
                <td style="width:20%;">
                    <xsl:value-of select="translate(view:creation_date,'T',' ')"/>
                </td>
                <td style="width:50%;">
                    <xsl:value-of select="view:title"/>
                </td>
                <td style="width:20%;">
                    <a href="{view:owner/view:link}">
                        <xsl:value-of select="view:owner/view:display_name"/>
                    </a>
                </td>
                <td style="width:10%;">
                    <div class="permalink">
                        <a href="{view:link}">link</a>
                    </div>
                </td>
            </tr>
    </xsl:template>

</xsl:stylesheet>
