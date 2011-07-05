package ${package}.service.persistence;

import ${package}.model.${document.name};

import org.mongoj.exception.SystemException;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */
public class ${document.name}Util {

	public static ${document.name} remove(${document.name} ${document.variableName}) throws SystemException {
		return getPersistence().remove(${document.variableName});
	}

	public static ${document.name} update(${document.name} ${document.variableName}) throws SystemException {
		return getPersistence().update(${document.variableName});
	}

	<#list methods as method>
		<#if !method.isConstructor() && method.isPublic() && serviceBuilder.isCustomMethod(method) && !serviceBuilder.isBasePersistenceMethod(method)>
			public static ${serviceBuilder.getTypeGenericsName(method.returns)} ${method.name} (

			<#list method.parameters as parameter>
				${serviceBuilder.getTypeGenericsName(parameter.type)} ${parameter.name}

				<#if parameter_has_next>
					,
				</#if>
			</#list>

			)

			<#list method.exceptions as exception>
				<#if exception_index == 0>
					throws
				</#if>

				${exception.value}

				<#if exception_has_next>
					,
				</#if>
			</#list>

			{
				<#if method.returns.value != "void">
					return
				</#if>

				getPersistence().${method.name}(

				<#list method.parameters as parameter>
					${parameter.name}

					<#if parameter_has_next>
						,
					</#if>
				</#list>

				);
			}
		</#if>
	</#list>

	public static ${document.name}Persistence getPersistence() {
		return _persistence;
	}

	public void setPersistence(${document.name}Persistence persistence) {
		_persistence = persistence;
	}

	private static ${document.name}Persistence _persistence;

}