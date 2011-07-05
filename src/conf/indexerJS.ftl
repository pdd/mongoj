
<#list indices?keys as collection>
	<#list indices[collection] as fields>
db.${collection}.ensureIndex({<#list fields as field>"${field}" : 1<#if field_has_next>, </#if></#list>});
	</#list>
</#list>

<#list uniqueIndices?keys as collection>
	<#list uniqueIndices[collection] as fields>
db.${collection}.ensureIndex({<#list fields as field>"${field}" : 1<#if field_has_next>, </#if></#list>}, {"unique" : true});
	</#list>
</#list>