<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8" />

	<!-- simply copy the message to the result tree -->
	<xsl:template match="/">
		<html>
			<head>
				<style>
				body {
					background-color: #fff;
					margin: 1em;
					font-family: sans;
					margin: 0px;
					padding: 0px;
				}
				* {
				}
				header {
					background-color: #eee;
					padding: 1em;
					padding-left: 2em;
					border-bottom: 1px solid black;
				}
				p {
					margin-left: 1em;
				}
				section.nodetype {
					border: 5px solid #ddd;
					padding: 1em;
					margin: 1em;
					margin-top: 2em;
				}
				table {
					border: 1px solid #ddd;
					padding: 1em;
					margin: 1em;
					width: 80%;
					border-collapse: collapse;
				}
				table td {
					border: 1px solid #ddd;
					padding: 4px;
				}
				a {
					color: #590;
					text-decoration: none;
				}
				</style>
				<title>DC2F Node Types</title>
			</head>
			<body>
				<header>
					<h1>DC2F Node Types</h1>
				</header>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="nodetype">
		<section class="nodetype">
			<xsl:element name="h2">
				<xsl:attribute name="id"><xsl:value-of select="@path" /></xsl:attribute>
				<xsl:value-of select="@name" />
			</xsl:element>
			<xsl:if test="@extends"><p>extends <xsl:element name="a"><xsl:attribute name="href">#<xsl:value-of select="@extends" /></xsl:attribute><xsl:value-of select="@extends" /></xsl:element></p></xsl:if>
			<p><xsl:value-of select="@comment" /></p>
			<xsl:variable name="test" select="@path" />
			<xsl:if test="count(//nodetype[@extends=$test]) > 0">
				Subtypes:
				<ul>
					<xsl:for-each select="//nodetype[@extends=$test]">
						<li>
							<xsl:element name="a">
								<xsl:attribute name="href">#<xsl:value-of select="@path" /></xsl:attribute>
								<xsl:value-of select="@name" />
							</xsl:element>
						</li>
					</xsl:for-each>
				</ul>
			</xsl:if>
			<xsl:if test="count(property) > 0">
				<h3>Properties</h3>
				<table>
					<thead>
						<tr>
							<th>Name</th>
							<th>Type</th>
							<th>Comment</th>
						</tr>
					</thead>
					<tbody>
						<xsl:apply-templates />
					</tbody>
				</table>
			</xsl:if>
		</section>
	</xsl:template>
	<xsl:template match="property">
		<tr>
			<td><xsl:value-of select="@name" /></td>
			<td><xsl:value-of select="@type" /></td>
			<td><xsl:value-of select="@comment" /></td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
