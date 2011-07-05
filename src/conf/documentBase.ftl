package ${package}.model;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import java.io.Serializable;

import org.mongoj.model.BaseModel;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */
public interface ${document.name}Model extends BaseModel<${document.name}> {
	
	public String getId();

	public void setId(String id);

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
			
			/**
			* Appends to the end of List, irrespective of value already existing.
			* See addTo${field.methodName} to add without duplicating.
			*/
			public void appendTo${field.methodName}(${field.listTypeName} ${field.name});
			
			/**
			* Appends to the end of List, irrespective of value already existing.
			* See addTo${field.methodName} to add without duplicating.
			*/
			public void appendTo${field.methodName}(${field.type} ${field.name});
			
			/**
			* Adds to the List by treating List as a Set. 
			* Thus adds only if not already exists in the List and avoids duplication.
			* See appendTo${field.methodName} to append to List.
			*/
			public void addTo${field.methodName}(${field.listTypeName} ${field.name});
			
			/**
			* Adds to the List by treating List as a Set. 
			* Thus adds only if not already exists in the List and avoids duplication.
			* See appendTo${field.methodName} to append to List.
			*/
			public void addTo${field.methodName}(${field.type} ${field.name});
			
			/**
			* Removes all occurences of value from the List
			*/
			public void removeFrom${field.methodName}(${field.listTypeName} ${field.name});
			
			/**
			* Removes all occurences of all values from the List
			*/
			public void removeFrom${field.methodName}(${field.type} ${field.name});
		</#if>
	</#list>

}