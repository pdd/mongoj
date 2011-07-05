package ${package}.service.base;

import org.mongoj.exception.UpdateException;
import org.mongoj.exception.SystemException;
<#if liferayService>
	import com.liferay.portal.kernel.annotation.BeanReference;
<#else>
	import org.springframework.beans.factory.annotation.Autowired;
</#if>

<#list references as tempDocument>
	import ${package}.service.${tempDocument.name}LocalService;
	import ${package}.service.persistence.${tempDocument.name}Persistence;
	//import ${package}.service.persistence.${tempDocument.name}Util;
</#list>

import ${package}.model.${document.name};
import ${package}.model.impl.${document.name}Impl;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */	
public abstract class ${document.name}LocalServiceBaseImpl implements ${document.name}LocalService {

	public ${document.name} add${document.name}(${document.name} ${document.variableName}) 
		throws UpdateException, SystemException {
		return ${document.variableName}Persistence.update(${document.variableName});
	}

	public ${document.name} create${document.name}() {
		return ${document.variableName}Persistence.create();
	}

	public void delete${document.name}(String id) 
		throws UpdateException, SystemException {
		${document.variableName}Persistence.remove(id);
	}

	public void delete${document.name}(${document.name} ${document.variableName}) 
		throws UpdateException, SystemException {
		${document.variableName}Persistence.remove(${document.variableName});
	}

	public ${document.name} get${document.name}(String id) throws SystemException {
		return ${document.variableName}Persistence.fetchByPrimaryKey(id);
	}

	public long get${document.name}Count() throws SystemException {
		return ${document.variableName}Persistence.countAll();
	}

	public ${document.name} update${document.name}(${document.name} ${document.variableName}) 
		throws UpdateException, SystemException {
		return ${document.variableName}Persistence.update(${document.variableName});
	}

<#list references as tempDocument>
	<#if tempDocument.name != document.name>
		<#if liferayService>		
			@BeanReference(type = ${tempDocument.name}LocalService.class)			
		<#else>
			@Autowired
		</#if>
		protected ${tempDocument.name}LocalService ${tempDocument.variableName}LocalService;
	</#if>
	<#if liferayService>		
		@BeanReference(type = ${tempDocument.name}Persistence.class)
	<#else>
		@Autowired
	</#if>
	protected ${tempDocument.name}Persistence ${tempDocument.variableName}Persistence;
</#list>

}