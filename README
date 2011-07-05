### What is mongoj?

__Short answer:__   
It is a Java persistence service generator and ORM (for lack of better term) 
for [mongoDB](http://www.mongodb.org/ "mongoDB").

__Long answer:__  
If you know or have already decided how you want to structure the data 
stored in the DB, or in other words, you have designed the schema, 
then you should be mostly done.   
But that "mostly" is quite elusive.  

1. Design data structure or schema - done
2. Write the model or value object or transfer object POJOs
3. Map them to the DB using some ORM 
4. Develop persistence layer following some pattern like DAO 
5. If using spring, wire the dependencies, either by annotating or via spring xml.
6. Start on developing the business logic

mongoj has a ServiceBuilder tool that helps you skip steps 1 to 5 by 
generating the code for you.  
In addition to generating the persistent POJOs, or the models, it also 
generates the DAO layer and spring managed services.

Moreover, when you make changes to schema later, e.g. add a new field, you do 
not need to partially repeat 2-3-4 again. Instead, just re-run 
ServiceBuilder and start using the new fields by jumping over to step 6.

--------------

Although _schema-less_ and _NoSQL_ are the buzzwords, most people will want to 
define and stick to the structure and/or schema, to avoid chaos later. 
So you start with data, define schema/structure in JSON 
and run service builder on the service definition.
It will generate all the boilerplate code and then you
can spend time on the business logic and avoid mundane model/POJO/DAO/service 
development and maintenance.

mongoj's ServiceBuilder for mongoDB is modeled after [Liferay's](http://www.liferay.com/ "Liferay") 
ServiceBuilder. It uses the same concepts, follows same patterns, except, 
the model and persistence layers are entirely different for obvious 
reasons (RDBMS vs NoSQL).

It uses Spring at the service layer and can be used within a 
Servlet, or a Portlet or with standalone java application.

Those familiar with Liferay should feel at home if using within a Liferay portal.
You can look at a detailed example in 
samples/PortletWithService/UsedCarStorePortlet

__Overview__  
Let's assume we want to have a user object in mongoDB as shown below,  

	user = {
		"firstName" : "Joe",
		"lastName" : "Black",
		"image" : <binary image data>,
		"info" : {
			"dob" : <date object>,
			"address" : {
				"street" : "10 main street",
				"zip" : 12345
			},
			"phone" : "102345679",
			"reminders" : [
				<date1>, <date2>,...
			]
		},
		"active" : true
	}

This object is represented as a service definition in a JSON file. 

__service.json__

	{
		"author" : "Your Name",
		"package" : "com.example.user",
		 
		"documents" : [
			{	
				"name" :  "User",
				"collection" : "users",
				"fields" : [
					{	
						"name" : "firstName",
					 	"type" : "String"
					},
					{	
						"name" : "lastName",
					 	"type" : "String"
					},
					{
						"name" : "image",
						"type" : "byte[]"						
					},
					{
						"name" : "info",
						"type" : "Object",
						"fields" : [
							{
								"name" : "dob",
								"type" : "Date"
							},
							{	"name" : "address",
							 	"type" : "Object",
								"fields" : [
									{
										"name" : "street",
										"type" : "String"
									},
									{
										"name" : "zip",
										"type" : "int"
									}
								]
							},
							{
								"name" : "phone",
								"type" : "String"
							},
							{ 	
								"name" : "reminders",
								"type" : "List<Date>"
							}
						]
					},
					{
						"name" : "active",
						"type" : "boolean"
					}
				]	
			}
		]
	}


When you run ServiceBuilder on service.json, following files are generated,

__Persistence layer__  
- UserPersistence.java -persistence interface (always re-generated)  
- UserPersistenceImpl.java - implementation of the persistence interface (always re-generated)  

__Model layer__  
- UserModel.java - base interface for the model/transfer object/value object (always re-generated)  
- UserModelImpl.java - base implementation for model interface (always re-generated)  
- User.java - extension of base model interface for user customization (always re-generated)  
- UserImpl.java - extension of base model for user customization (never re-generated)  

__Service layer__  
- UserLocalService.java - interface for service (always re-generated)  
- UserLocalServiceBaseImpl.java - base implementation of service (always re-generated)  
- UserLocalServiceUtil.java - utility class with static methods (always re-generated)  
- UserLocalServiceImpl.java - extension of base for customization. This is where you write your business logic. (never re-generated)  

__Miscellaneous__  
- spring.xml - holds the spring wiring for all beans (re-generated but customizable)  
- indexer.js - a JS script that you can run in mogodb shell for indexing (always re-generated)  
- indexes.properties - contains index meta data if auto indexing is configured (always re-generated)  

Building a service is an iterative process. You define service and run ServiceBuilder.  
If you make modifications to the service definition, then you re-run 
ServiceBuilder.That is why some files are "re" generated.  
You may customize the model by adding any methods to UserImpl.java. The you 
re-run ServiceBuilder so it generates interface declaration for your new methods 
and propagates them to the interface (User.java).  
Similarly, you write the business logic for the service in UserLocalServiceImpl,  
and re-run ServiceBuilder. It will then propagate the methods to service  
interfaces and service utility.

See [wiki](https://github.com/pdd/mongoj/wiki "Wiki") for more details.
