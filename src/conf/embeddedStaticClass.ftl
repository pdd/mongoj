public static class ${parentField.listTypeName}Impl implements ${parentField.listTypeName} {
	<#compress>
		public final Object[][] FIELDS = {
		<#list fields as field>
			{"${field.name}", 
			<#if field.type == "Object">
				"${field.typeName}"
			<#else>
				"${field.type}" 
			</#if>
			}
			<#if field_has_next>
			,
			</#if>
		</#list>
		};
	</#compress>

	public ${parentField.listTypeName}Impl() {
	}

	public ${parentField.listTypeName}Impl(Map<String, Object> map) {
		<#list fields as field>
			<#if field.type == "Object">
				_${field.name} = new ${field.typeName}Impl((Map<String, Object>)map.get("${field.name}")); 
			<#elseif field.primitiveType>
				_${field.name} = (${field.wrapperType})map.get("${field.name}");
			<#elseif field.customListType>
				List<${field.listTypeName}> objectList = new ArrayList<${field.listTypeName}>();
				
				List<Map<String, Object>> mapList = 
					(List<Map<String, Object>>)map.get("${field.name}");
			
				if (mapList != null) {
					for (Map<String, Object> object : mapList) {
						objectList.add(new ${field.customListTypeImpl}(object));
					}
				}
				
				_${field.name} = objectList;
			<#else>
				_${field.name} = (${field.type})map.get("${field.name}");
			</#if>
		</#list>
	}

	<#list fields as field>
		<#if field.type == "Object">
			${serviceBuilder.getEmbeddedClass(field)}
			
			public ${field.typeName} get${field.methodName}() {
				return _${field.name};	
			}
			
			public void set${field.methodName}(${field.typeName} ${field.name}) {
				_${field.name} = ${field.name};
			}
		<#else>
			public ${field.type} get${field.methodName}() {
				return _${field.name};	
			}
			
			public void set${field.methodName}(${field.type} ${field.name}) {
				_${field.name} = ${field.name};
			}
		</#if>
				
		<#if field.listType>
			<#if field.customListType && field.fields?has_content>
				${serviceBuilder.getEmbeddedStaticClass(field)}
			</#if>
		</#if>
	</#list>

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		 
		<#list fields as field>
			<#if field.type == "Object">
				map.put("${field.name}", _${field.name}.toMap());
			<#elseif field.customListType>
				map.put("${field.name}", objectListToMapList(get${field.methodName}()));
			<#else>
				map.put("${field.name}", get${field.methodName}()); 
			</#if>
		</#list>
		
		return map;
	}
	
	<#list fields as field>
		<#if field.type == "Object">
			private ${field.typeName} _${field.name} = new ${field.typeName}Impl();
		<#elseif field.listType >
			private ${field.type} _${field.name} = new ArrayList<${field.listTypeName}>();
		<#else>
			private ${field.type} _${field.name}; 
		</#if>
	</#list>
}