
<#list indices?keys as collection>
<#assign i = 0>
	<#list indices[collection] as fields>
${collection}.${i}=<#list fields as field>${field}<#if field_has_next>,</#if></#list>
		<#assign i = i + 1>
	</#list>
</#list>

<#list uniqueIndices?keys as collection>
<#assign i = 0>
	<#list uniqueIndices[collection] as fields>
unique.${collection}.${i}=<#list fields as field>${field}<#if field_has_next>,</#if></#list>
		<#assign i = i + 1>	
	</#list>
</#list>