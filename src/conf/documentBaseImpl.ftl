package ${package}.model.impl;

import org.mongoj.model.impl.BaseModelImpl;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import java.io.Serializable;

import ${package}.model.${document.name};
import ${package}.model.${document.name}Model;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */
public class ${document.name}ModelImpl extends BaseModelImpl<${document.name}> 
	implements ${document.name}Model {
	public static final String COLLECTION_NAME = "${collection}";

	<#compress>	
		public static final Object[][] FIELDS = {
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
	
	public ${document.name}ModelImpl() {
	}

	public ${document.name}ModelImpl(Map<String, Object> map) {
		_id = map.get("_id").toString();
		
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
				
				<#if field.customListType>
					setMap.put("${field.name}", objectListToMapList(${field.name}));
				<#else>
					setMap.put("${field.name}", ${field.name});
				</#if>
			}
		</#if>
				
		<#if field.listType>
			<#if field.customListType && field.fields?has_content>
				${serviceBuilder.getEmbeddedStaticClass(field)}
			</#if>
		
			public void appendTo${field.methodName}(${field.listTypeName} ${field.name}) {
				_${field.name}.add(${field.name});
				
				<#if field.customListType>
					updateAppendMap("${field.name}", ${field.name}.toMap());
				<#else>
					updateAppendMap("${field.name}", ${field.name});
				</#if>	
			}
			
			public void appendTo${field.methodName}(${field.type} ${field.name}) {
				_${field.name}.addAll(${field.name});
				
				<#if field.customListType>
					updateAppendMap("${field.name}", objectListToMapList(${field.name}));
				<#else>
					updateAppendMap("${field.name}", ${field.name});
				</#if>
			}
			
			public void addTo${field.methodName}(${field.listTypeName} ${field.name}) {
				if (!_${field.name}.contains(${field.name})) {
					_${field.name}.add(${field.name});
					
					<#if field.customListType>
						updateAddMap("${field.name}", ${field.name}.toMap());
					<#else>
						updateAddMap("${field.name}", ${field.name});
					</#if>
				}
			}
			
			public void addTo${field.methodName}(${field.type} ${field.name}) {
				for(${field.listTypeName} object : ${field.name}) {
					if (!_${field.name}.contains(object)) {
						_${field.name}.add(object);
						
						<#if field.customListType>
							updateAddMap("${field.name}", object.toMap());
						<#else>
							updateAddMap("${field.name}", object);
						</#if>
					}
				}
			}
			
			public void removeFrom${field.methodName}(${field.listTypeName} ${field.name}) {
				while(_${field.name}.remove(${field.name}));
				
				<#if field.customListType>
					updateRemoveMap("${field.name}", ${field.name}.toMap());
				<#else>
					updateRemoveMap("${field.name}", ${field.name});
				</#if>
				
			}
			
			public void removeFrom${field.methodName}(${field.type} ${field.name}) {
				_${field.name}.removeAll(${field.name});
				
				<#if field.customListType>
					updateRemoveMap("${field.name}", objectListToMapList(${field.name}));
				<#else>
					updateRemoveMap("${field.name}", ${field.name});
				</#if>
			}
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
	
	public String toString() {
		return toMap().toString();
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
	private static final long serialVersionUID = 1L;
}