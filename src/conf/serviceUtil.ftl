package ${package}.service;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */
public class ${document.name}LocalServiceUtil {

	<#list methods as method>
		<#if !method.isConstructor() && !method.isStatic() && method.isPublic() && serviceBuilder.isCustomMethod(method)>
			public static ${serviceBuilder.getTypeGenericsName(method.returns)} ${method.name}(

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

				getService().${method.name}(

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


	public static void clearService() {
		_service = null;
	}

	public static ${document.name}LocalService getService() {
		if (_service == null) {
			throw new NullPointerException("Service is null");
		}

		return _service;
	}

	public void setService(${document.name}LocalService service) {
		_service = service;
	}

	private static ${document.name}LocalService _service;

}