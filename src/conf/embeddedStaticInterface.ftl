public static interface ${parentField.listTypeName} extends Serializable {
	<#list fields as field>
		<#if field.type == "Object">
			${serviceBuilder.getEmbeddedInterface(field)}
			public ${field.typeName} get${field.methodName}();
			
			public void set${field.methodName}(${field.typeName} ${field.name});
		<#else>
			public ${field.type} get${field.methodName}();
			
			public void set${field.methodName}(${field.type} ${field.name});
		</#if>
		<#if field.listType>
			<#if field.customListType && field.fields?has_content>
				${serviceBuilder.getEmbeddedStaticInterface(field)}
			</#if>
		</#if>
	</#list>
	
	public Map<String, Object> toMap();
	
}