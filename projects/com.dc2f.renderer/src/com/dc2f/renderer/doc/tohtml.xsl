<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8" />

	<!-- simply copy the message to the result tree -->
	<xsl:template match="/">
		<html>
			<style>
				body {
					background-color: #ccc;
				}
				h1 {
					border-top: 1px solid black;
				}
				p {
					margin-left: 1em;
				}
				table {
					border: 1px solid gray;
					padding: 1em;
					margin: 1em;
					width: 80%;
				}
			</style>
			<xsl:apply-templates />
		</html>
	</xsl:template>
	
	<xsl:template match="nodetype">
		<h1><xsl:value-of select="@name" /></h1>
		<xsl:if test="@extends"><p>extends <xsl:value-of select="@extends" /></p></xsl:if>
		<p><xsl:value-of select="@comment" /></p>
		<xsl:if test="count(property) > 0">
			<h2>Properties</h2>
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
	</xsl:template>
	<xsl:template match="property">
		<tr>
			<td><xsl:value-of select="@name" /></td>
			<td><xsl:value-of select="@type" /></td>
			<td><xsl:value-of select="@comment" /></td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
