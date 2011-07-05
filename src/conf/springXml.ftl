<#list documents as document>
	<bean id="${package}.service.${document.name}LocalService" class="${package}.service.impl.${document.name}LocalServiceImpl" />
	<bean id="${package}.service.${document.name}LocalServiceUtil" class="${package}.service.${document.name}LocalServiceUtil">
		<property name="service" ref="${package}.service.${document.name}LocalService" />
	</bean>
	<bean id="${package}.service.persistence.${document.name}Persistence" class="${document.persistenceClassName}" parent="org.mongoj.BasePersistence" />
</#list>