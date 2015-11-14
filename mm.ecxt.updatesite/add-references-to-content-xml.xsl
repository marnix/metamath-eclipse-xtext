<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="repository">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
			<references size='2'>
				<repository
					uri='http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/'
					url='http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/'
					type='0' options='1' />
				<repository
					uri='http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/'
					url='http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/'
					type='1' options='1' />
			</references>

		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>