public class ${parentField.typeName}Impl implements ${parentField.typeName} {
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

	public ${parentField.typeName}Impl() {
	}

	public ${parentField.typeName}Impl(Map<String, Object> map) {
		<#list fields as field>
			<#if field.type == "Object">
				_${field.name} = new ${field.typeName}Impl((Map<String, Object>)map.get("${field.name}")); 
			<#elseif field.primitiveType>
				_${field.name} = (${field.wrapperType})map.get("${field.name}");
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
				
				setMap.put("${field.getFQN()}", ${field.name});
			}
		</#if>
				
		<#if field.listType>
			public void appendTo${field.methodName}(${field.listTypeName} ${field.name}) {
				_${field.name}.add(${field.name});
				
				updateAppendMap("${field.getFQN()}", ${field.name});
			}
			
			public void appendTo${field.methodName}(${field.type} ${field.name}) {
				_${field.name}.addAll(${field.name});
				
				updateAppendMap("${field.getFQN()}", ${field.name});
			}

			public void addTo${field.methodName}(${field.listTypeName} ${field.name}) {
				if (!_${field.name}.contains(${field.name})) {
					_${field.name}.add(${field.name});
					
					updateAddMap("${field.getFQN()}", ${field.name});
				}
			}
						
			public void addTo${field.methodName}(${field.type} ${field.name}) {
				for(${field.listTypeName} object : ${field.name}) {
					if (!_${field.name}.contains(object)) {
						_${field.name}.add(object);
						
						updateAddMap("${field.getFQN()}", object);
					}
				}
			}
			
			public void removeFrom${field.methodName}(${field.listTypeName} ${field.name}) {
				while(_${field.name}.remove(${field.name}));
				
				updateRemoveMap("${field.getFQN()}", ${field.name});
			}
			
			public void removeFrom${field.methodName}(${field.type} ${field.name}) {
				_${field.name}.removeAll(${field.name});
				
				updateRemoveMap("${field.getFQN()}", ${field.name});
			}
		</#if>
	</#list>

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		 
		<#list fields as field>
			<#if field.type == "Object">
				map.put("${field.name}", _${field.name}.toMap());
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