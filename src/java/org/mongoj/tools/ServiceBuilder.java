/**
 * Copyright (c) 2011 Prashant Dighe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 * 	The above copyright notice and this permission notice shall be included 
 * 	in all copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package org.mongoj.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import org.codehaus.jackson.map.ObjectMapper;

import org.mongoj.tools.ServiceDefinition.Document;
import org.mongoj.util.ConversionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.storage.ConventionKeys;
import de.hunsicker.jalopy.storage.Environment;
import freemarker.ext.beans.BeansWrapper;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class ServiceBuilder {
	
	public static void main(String[] args) {
		
		String serviceFileName = "src/conf/service.json";
		String serviceApiDir = "build/src/java/service";
		String serviceImplDir = "build/src/java/service-impl";
		String springFileName = "build/META-INF/spring.xml";
		String indexerFileName = "build/indexer.js";
		
		boolean liferayService = false; 
		
		if (args.length == 5) {
			serviceFileName = args[0];
			serviceApiDir = args[1];
			serviceImplDir = args[2];
			springFileName = args[3];
			indexerFileName = args[4];
			liferayService = ConversionUtil.getboolean(args[5]);
			
		}
		else {
			serviceFileName = System.getProperty("service.file.name");
			serviceApiDir = System.getProperty("service.api.dir");
			serviceImplDir = System.getProperty("service.impl.dir");
			springFileName = System.getProperty("service.spring.file");
			indexerFileName = System.getProperty("service.indexer.file");
			liferayService = ConversionUtil.getboolean( 
				System.getProperty("liferay.service"));
		}
		
		new ServiceBuilder(
			serviceFileName, serviceApiDir, serviceImplDir, springFileName,
			indexerFileName, liferayService);
	}

	public ServiceBuilder(String serviceFileName, String serviceApiDir,
		String serviceImplDir, String springFileName, 
		String indexerFileName, boolean liferayService) {
		_serviceApiDir = serviceApiDir;
		_serviceImplDir = serviceImplDir;
		_springFileName = springFileName;
		_indexerFileName = indexerFileName;
		_liferayService = liferayService;

		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			ServiceDefinition serviceDefinition = mapper.readValue(
				new File(serviceFileName), ServiceDefinition.class);
			
			_documents = serviceDefinition.getDocuments();

			_processFields();
			_processFinders();
			
			_packagePath = serviceDefinition.getPackage();

			_implOutputPath =
				_serviceImplDir + "/" + _packagePath.replace(".", "/");

			_serviceOutputPath =
				_serviceApiDir + "/" + _packagePath.replace(".", "/");
			
			for (ServiceDefinition.Document document : _documents) {
				Map<String, Object> rootMap = new HashMap<String, Object>();
				
				String name = document.getName();
				
				List<List<String>> uniqueKeys = document.getUniqueKeys();
				
				if (uniqueKeys != null && !uniqueKeys.isEmpty()) {
					_uniqueIndices.put(document.getCollection(), uniqueKeys);
				}
					
				rootMap.put("serviceBuilder", this);
				rootMap.put("package", serviceDefinition.getPackage());
				rootMap.put("author", serviceDefinition.getAuthor());
				rootMap.put("documents", _documents);
				rootMap.put("document", document);
				rootMap.put(
					"tempMap", wrapper.wrap(new HashMap<String, Object>()));
				rootMap.put("references", _documents);			
				rootMap.put("collection", document.getCollection());
				rootMap.put("fields", document.getFields());
				rootMap.put("liferayService", _liferayService);
								
				String persistenceClassName = 
					document.getPersistenceClassName();
				
				if (persistenceClassName == null) {
					persistenceClassName = _packagePath +
						".service.persistence." + name + "PersistenceImpl";
					
					document.setPersistenceClassName(persistenceClassName);
				}

				System.out.println("Building " + name);
				
				_createPersistenceImpl(rootMap, name);
				_createPersistence(rootMap, name);
				
				// Don't need this now, but keep for later, just in case
				//_createPersistenceUtil(rootMap, name);
				
				_createModelBaseImpl(rootMap, name);				
				_createModelImpl(rootMap, name);
				_createModelBase(rootMap, name);
				_createModel(rootMap, name);
				_createServiceImpl(rootMap, name);
				_createServiceBaseImpl(rootMap, name);
				_createService(rootMap, name);
				_createServiceUtil(rootMap, name);
			}

			Map<String, Object> rootMap = new HashMap<String, Object>();
			
			rootMap.put("package", serviceDefinition.getPackage());
			rootMap.put("documents", _documents);
			
			_createSpringXml(rootMap);
			
			_createIndexers();
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isDuplicateMethod(
		JavaMethod method, Map<String, Object> tempMap) {

		StringBuilder sb = new StringBuilder();

		sb.append("isDuplicateMethod ");
		sb.append(getTypeGenericsName(method.getReturns()));
		sb.append(" ");
		sb.append(method.getName());
		sb.append("(");

		JavaParameter[] parameters = method.getParameters();

		for (int i = 0; i < parameters.length; i++) {
			JavaParameter javaParameter = parameters[i];

			sb.append(getTypeGenericsName(javaParameter.getType()));

			if ((i + 1) != parameters.length) {
				sb.append(",");
			}
		}

		sb.append(")");

		String key = sb.toString();

		if (tempMap.containsKey(key)) {
			return true;
		}
		else {
			tempMap.put(key, key);

			return false;
		}
	}
	
	public String getTypeGenericsName(Type type) {
		StringBuilder sb = new StringBuilder();

		sb.append(type.getValue());

		Type[] actualTypeArguments = type.getActualTypeArguments();

		if (actualTypeArguments != null) {
			sb.append('<');

			for (int i = 0; i < actualTypeArguments.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}

				sb.append(getTypeGenericsName(actualTypeArguments[i]));
			}

			sb.append('>');
		}

		sb.append(getDimensions(type.getDimensions()));

		return sb.toString();
	}

	public String getDimensions(String dims) {
		return getDimensions(Integer.parseInt(dims));
	}

	public String getDimensions(int dims) {
		String dimensions = "";

		for (int i = 0; i < dims; i++) {
			dimensions += "[]";
		}

		return dimensions;
	}
	
	public boolean isBasePersistenceMethod(JavaMethod method) {
		String methodName = method.getName();

		if (methodName.equals("fetchByPrimaryKey") ||
			methodName.equals("remove")) {

			JavaParameter[] parameters = method.getParameters();

			if ((parameters.length == 1) &&
				(parameters[0].getName().equals("primaryKey"))) {

				return true;
			}

			if (methodName.equals("remove")) {
				Type[] methodExceptions = method.getExceptions();

				for (Type methodException : methodExceptions) {
					String exception = methodException.getValue();

					if (exception.contains("NoSuch")) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	public boolean isCustomMethod(JavaMethod method) {
		String methodName = method.getName();

		if (methodName.equals("equals") ||
			methodName.equals("getClass") ||
			methodName.equals("hashCode") ||
			methodName.equals("notify") ||
			methodName.equals("notifyAll") ||
			methodName.equals("toString") ||
			methodName.equals("wait")) {

			return false;
		}
		else if ((methodName.endsWith("Persistence")) &&
				 (methodName.startsWith("get") ||
				  methodName.startsWith("set"))) {

			return false;
		}
		else if ((methodName.endsWith("Service")) &&
				 (methodName.startsWith("get") ||
				  methodName.startsWith("set"))) {

			return false;
		}
		else {
			return true;
		}
	}	
		
	public String getEmbeddedInterface(ServiceDefinition.Document.Field field) 
		throws Exception {
		Map<String, Object> rootMap = new HashMap<String, Object>(); 
		
		rootMap.put("serviceBuilder", this);
		rootMap.put("fields", field.getFields());
		rootMap.put("parentField", field);
		String content = 
			FreeMarkerUtil.processTemplate(rootMap, "embeddedInterface.ftl");
		
		return content; 
	}

	public String getEmbeddedStaticInterface(
		ServiceDefinition.Document.Field field) throws Exception {
		Map<String, Object> rootMap = new HashMap<String, Object>(); 
		
		rootMap.put("serviceBuilder", this);
		rootMap.put("fields", field.getFields());
		rootMap.put("parentField", field);
		String content = FreeMarkerUtil.processTemplate(
			rootMap, "embeddedStaticInterface.ftl");
		
		return content; 
	}

	public String getEmbeddedClass(ServiceDefinition.Document.Field field) 
		throws Exception {
		Map<String, Object> rootMap = new HashMap<String, Object>(); 
		
		rootMap.put("serviceBuilder", this);
		rootMap.put("fields", field.getFields());
		rootMap.put("parentField", field);
		String content = 
			FreeMarkerUtil.processTemplate(rootMap, "embeddedClass.ftl");
		
		return content; 
	}
	
	public String getEmbeddedStaticClass(
		ServiceDefinition.Document.Field field) throws Exception {
		Map<String, Object> rootMap = new HashMap<String, Object>(); 
		
		rootMap.put("serviceBuilder", this);
		rootMap.put("fields", field.getFields());
		rootMap.put("parentField", field);
		String content = 
			FreeMarkerUtil.processTemplate(rootMap, "embeddedStaticClass.ftl");
		
		return content; 
	}
	private JavaClass _getJavaClass(String fileName) throws IOException {
		int pos = fileName.indexOf(_serviceImplDir + "/");

		if (pos != -1) {
			pos += _serviceImplDir.length();
		}
		else {
			pos = fileName.indexOf(_serviceApiDir + "/") + 
				_serviceApiDir.length();
		}

		String srcFile = fileName.substring(pos + 1, fileName.length());
		String className = srcFile.substring(0, srcFile.length() - 5)
			.replace("/", ".");

		JavaDocBuilder builder = new JavaDocBuilder();

		File file = new File(fileName);

		if (!file.exists()) {
			return null;
		}

		builder.addSource(file);

		return builder.getClassByName(className);
	}
	
	private JavaMethod[] _getMethods(JavaClass javaClass) {
		return _getMethods(javaClass, false);
	}

	private JavaMethod[] _getMethods(
		JavaClass javaClass, boolean superclasses) {

		JavaMethod[] methods = javaClass.getMethods(superclasses);

		for (JavaMethod method : methods) {
			Arrays.sort(method.getExceptions());
		}

		return methods;
	}	

	public void _createPersistence(Map<String, Object> rootMap, 
		String name) throws Exception {
		JavaClass javaClass = _getJavaClass(
			_implOutputPath + "/service/persistence/" + name +
				"PersistenceImpl.java");
		
		rootMap.put("methods", _getMethods(javaClass));
		
		String fileContents =
			FreeMarkerUtil.processTemplate(rootMap, "persistence.ftl");
		
		File file = new File(
			_serviceOutputPath + "/service/persistence/" + name + 
				"Persistence.java");
		
		_writeFile(file, fileContents, (String)rootMap.get("author"));		
	}
		
	private void _createPersistenceImpl(Map<String, Object> rootMap, 
		String name) throws Exception {
		String fileContents = 
			FreeMarkerUtil.processTemplate(rootMap, "persistenceImpl.ftl");

		File file = new File(
			_implOutputPath + "/service/persistence/" + name + 
				"PersistenceImpl.java");

		_writeFile(file, fileContents, (String)rootMap.get("author"));	
	}
	
	private void _createPersistenceUtil(Map<String, Object> rootMap, 
		String name) throws Exception {
		JavaClass javaClass = _getJavaClass(
			_implOutputPath + "/service/persistence/" + name +
				"PersistenceImpl.java");
		
		rootMap.put("methods", _getMethods(javaClass));
		
		String fileContents = 
			FreeMarkerUtil.processTemplate(rootMap, "persistenceUtil.ftl");

		File file = new File(
			_serviceOutputPath + "/service/persistence/" + name + "Util.java");

		_writeFile(file, fileContents, (String)rootMap.get("author"));
	}
	
	private void _createModel(Map<String, Object> rootMap, String name) 
		throws Exception {
		String fileContents =
			FreeMarkerUtil.processTemplate(rootMap, "document.ftl");
		
		File file = new File(
			_serviceOutputPath + "/model/" + name + ".java");
		
		_writeFile(file, fileContents, (String)rootMap.get("author"));
	}

	private void _createModelBase(Map<String, Object> rootMap, String name) 
		throws Exception {
		String fileContents =
			FreeMarkerUtil.processTemplate(rootMap, "documentBase.ftl");
		
		File file = new File(
			_serviceOutputPath + "/model/" + name + "Model.java");
		
		_writeFile(file, fileContents, (String)rootMap.get("author"));		
	}
	
	private void _createModelImpl(
		Map<String, Object> rootMap, String name) 
		throws Exception {
		String fileContents =
			FreeMarkerUtil.processTemplate(rootMap, "documentImpl.ftl");
		
		File file = new File(
			_implOutputPath + "/model/impl/" + name + "Impl.java");
		
		_writeFile(file, fileContents, (String)rootMap.get("author"));		
	}
	
	private void _createModelBaseImpl(
		Map<String, Object> rootMap, String name) 
		throws Exception {
		String fileContents =
			FreeMarkerUtil.processTemplate(rootMap, "documentBaseImpl.ftl");
		
		File file = new File(
			_implOutputPath + "/model/impl/" + name + "ModelImpl.java");
		
		_writeFile(file, fileContents, (String)rootMap.get("author"));		
	}
	
	private void _createServiceImpl(Map<String, Object> rootMap, String name)
		throws Exception {
		File file = new File(
			_implOutputPath + "/service/impl/" + name + 
				"LocalServiceImpl.java");
		
		if (!file.exists()) {
			String fileContents =
				FreeMarkerUtil.processTemplate(rootMap, "serviceImpl.ftl");

			_writeFile(file, fileContents, (String)rootMap.get("author"));
		}
	}
	
	private void _createServiceBaseImpl(
		Map<String, Object> rootMap, String name) throws Exception {
	
		JavaClass javaClass = _getJavaClass(
			_implOutputPath + "/service/impl/" + name + 
				"LocalServiceImpl.java");
	
		JavaMethod[] methods = _getMethods(javaClass);
	
		rootMap.put("methods", methods);
		rootMap.put("referenceList", _documents);
	
		String fileContents =
			FreeMarkerUtil.processTemplate(rootMap, "serviceBaseImpl.ftl");
		
		File file = new File(
			_implOutputPath + "/service/base/" + name + 
				"LocalServiceBaseImpl.java");
	
		_writeFile(file, fileContents, (String)rootMap.get("author"));
	}

	
	private void _createService(
		Map<String, Object> rootMap, String name) throws Exception {
	
		JavaClass javaClass = _getJavaClass(
			_implOutputPath + "/service/impl/" + name + 
				"LocalServiceImpl.java");
	
		JavaMethod[] methods = _getMethods(javaClass);
	
		if (javaClass.getSuperClass().getValue().endsWith(
				name + "LocalServiceBaseImpl")) {

			JavaClass parentJavaClass = _getJavaClass(
				_implOutputPath + "/service/base/" + name +
					"LocalServiceBaseImpl.java");

			JavaMethod[] parentMethods = parentJavaClass.getMethods();

			JavaMethod[] allMethods = 
				(JavaMethod[]) ArrayUtils.addAll(parentMethods, methods);
			
			methods = allMethods;
		}

		rootMap.put("methods", methods);
	
		String fileContents =
			FreeMarkerUtil.processTemplate(rootMap, "service.ftl");
	
		File file = new File(
			_serviceOutputPath + "/service/" + name + "LocalService.java");
	
		_writeFile(file, fileContents, (String)rootMap.get("author"));		
	}
	
	private void _createServiceUtil(
		Map<String, Object> rootMap, String name) throws Exception {
	
		JavaClass javaClass = _getJavaClass(
			_serviceOutputPath + "/service/" + name + "LocalService.java");
	
		rootMap.put("methods", _getMethods(javaClass));

		String fileContents =
			FreeMarkerUtil.processTemplate(rootMap, "serviceUtil.ftl");
	
		File file = new File(
			_serviceOutputPath + "/service/" + name + "LocalServiceUtil.java");
	
		_writeFile(file, fileContents, (String)rootMap.get("author"));	
	}
	
	private void _createSpringXml(Map<String, Object> rootMap) 
		throws Exception {
		String content =
			FreeMarkerUtil.processTemplate(rootMap, "springXml.ftl");

		File xmlFile = new File(_springFileName);

		if (!xmlFile.exists()) {
			InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream("springBase.xml");
			
			FileUtil.write(xmlFile, FileUtil.read(stream));
		}

		String oldContent = FileUtil.read(xmlFile);
		
		String newContent = oldContent;

		int x = oldContent.indexOf("<beans");
		int y = oldContent.lastIndexOf("</beans>");

		int firstSession = newContent.indexOf(
			"<bean id=\"" + _packagePath + ".service.", x);

		int lastSession = newContent.lastIndexOf(
			"<bean id=\"" + _packagePath + ".service.", y);

		if ((firstSession == -1) || (firstSession > y)) {
			x = newContent.indexOf("</beans>");

			newContent =
				newContent.substring(0, x) + content +
					newContent.substring(x, newContent.length());
		}
		else {
			firstSession = newContent.lastIndexOf("<bean", firstSession) - 1;

			int tempLastSession = newContent.indexOf(
				"<bean id=\"", lastSession + 1);

			if (tempLastSession == -1) {
				tempLastSession = newContent.indexOf("</beans>", lastSession);
			}

			lastSession = tempLastSession;

			newContent =
				newContent.substring(0, firstSession) + content +
					newContent.substring(lastSession, newContent.length());
		}

		newContent = _formatXml(newContent);

		_writeFile(xmlFile, newContent);		
	}
	
	private String _formatXml(String xml) {
		//TODO: implement this
		return xml;
	}
	
	private void _createIndexers() throws Exception {
		Map<String, List<List<String>>> indices = 
			new HashMap<String, List<List<String>>>();
		
		Properties properties = new Properties();
		
		int i = 0;
		
		//eliminate duplicate indexes from indices if they are also unique
		for (String collection : _indices.keySet()) {
			for (List<String> ilist : _indices.get(collection)) {
				if (_uniqueIndices.containsKey(collection)) {
					List<List<String>> ulists = _uniqueIndices.get(collection);
					
					if (!ulists.contains(ilist)) {						
						List<List<String>> list = indices.get(collection);
						
						if (list == null) {
							list = new ArrayList<List<String>>();
							
							indices.put(collection, list);
						}
						
						list.add(ilist);
						
						properties.put(
							collection + "." + i++, ilist.toString());
					}
				}
				else {
					List<List<String>> list = indices.get(collection);
					
					if (list == null) {
						list = new ArrayList<List<String>>();
						
						indices.put(collection, list);
					}
					
					list.add(ilist);
					
					properties.put(collection + "." + i++, ilist.toString());
				}
			}
		}
		
		Map<String, Object> rootMap = new HashMap<String, Object>();
		
		rootMap.put("indices", indices);
		rootMap.put("uniqueIndices", _uniqueIndices);
		
		String content =
			FreeMarkerUtil.processTemplate(rootMap, "indexerJS.ftl");
		
		File jsFile = new File(_indexerFileName);
		
		_writeFile(jsFile, content);
		
		content =
			FreeMarkerUtil.processTemplate(rootMap, "indexes.ftl");
		
		File indexProps = new File(_serviceImplDir + "/indexes.properties");
		
		_writeFile(indexProps, content);
	}
	
	//Finders specify fully qualified names for each of the finder fields.
	//service.json processing will only set the list of finder field names but
	//the "List<Field> _fields" is not set at that time (by jackson).
	//This method iterates through all finders and sets "_fields" member of 
	//each finder by extracting individual Field from its FQN.
	//Also it populates the required indices. 
	private void _processFinders() throws Exception {
		for (ServiceDefinition.Document document : _documents) {
			List<ServiceDefinition.Document.Finder> finders = 
				document.getFinders();
						
			for (ServiceDefinition.Document.Finder finder : finders) {
				String returnType = finder.getReturnType();
				
				if (!returnType.equals("Collection") &&
					!returnType.equals(document.getName())) {
					throw new Exception("Invalid return type " +
						returnType + " for finder " + finder.getName());
				}
				
				List<String> fieldNames =  finder.getFinderFields();
				
				List<List<String>> indices = 
					_indices.get(document.getCollection());
				
				if (indices == null) {
					indices = new ArrayList<List<String>>();
					
					_indices.put(document.getCollection(), indices);
				}
				
				indices.add(fieldNames);
				
				List<Document.Field> fields = new ArrayList<Document.Field>();
				
				for (String fieldName : fieldNames) {
					Document.Field field = null;
					
					if (fieldName.indexOf('.') != -1) {
						String[] hierarchy = fieldName.split("\\.");
						
						int i = 0;
						
						field = document.getField(hierarchy[i++]);
						
						if (field == null) {
							throw new Exception("Finder field " + fieldName + 
								" not found.");
						}

						while (i < hierarchy.length &&
							field.getType().equals(
								ServiceDefinition.TYPE_OBJECT)) {
							
							Document.Field f = field.getField(hierarchy[i++]);
							
							if (f == null) {
								throw new Exception("Finder field " + 
									field.getName() + " not found in " + 
										fieldName);
							}
							
							field = f;
						}
					}
					else {
						field = document.getField(fieldName);
					}
					
					if (field == null) {
						throw new Exception("Finder field " + fieldName + 
							" not found.");
					}
					
					fields.add(field);
				}
				
				finder.setFields(fields);
			}
		}
	}

	private void _processFields() throws Exception {
		for (ServiceDefinition.Document document : _documents) {
			List<ServiceDefinition.Document.Field> fields =
				document.getFields();
			
			for (Document.Field field : fields) {
				_processField(field, StringUtils.EMPTY);
			}
			
		}
	}
	
	//Currently, this method only sets the Fully Qualified Name (FQN) for
	//each field, but the idea is to do other field specific processing
	//here, whenever the need arises (like shorten field names?)
	private void _processField(Document.Field field, String parentFQN) {
		if (parentFQN.isEmpty()) {
			field.setFQN(field.getName());
		}
		else {
			field.setFQN(parentFQN + "." + field.getName());
			
		}
		
		List<Document.Field> fields = field.getFields();
		
		if (fields != null) {
			for (Document.Field subField : fields) {
				_processField(subField, field.getFQN());
			}
		}
	}
	
	private static void _writeFile(File file, String content)
		throws IOException {
		
		// Write file if and only if the file has changed
		
		String oldContent = null;
	
		if (file.exists()) {
			oldContent = FileUtil.read(file);
		}
		
		if (oldContent == null || !oldContent.equals(content)) {
			FileUtil.write(file, content);
	
			System.out.println("Writing " + file);
		}
	}
	
	private static void _writeFile(File file, String content, String author)
		throws IOException {		
		File tempFile = new File("ServiceBuilder.temp");
		
		FileUtil.write(tempFile, content);
	
		// Beautify
	
		StringBuffer sb = new StringBuffer();
	
		Jalopy jalopy = new Jalopy();
	
		jalopy.setFileFormat(FileFormat.UNIX);
		jalopy.setInput(tempFile);
		jalopy.setOutput(sb);
	
		URL jalopyConvention = 
			ServiceBuilder.class.getClassLoader().getResource("jalopy.xml");
		
		try {
			Jalopy.setConvention(jalopyConvention);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		Environment env = Environment.getInstance();

		env.set("author", author);

		Convention convention = Convention.getInstance();

		String classMask = "/**\n" +
			" * @author $author$\n" +
			"*/";

		convention.put(
			ConventionKeys.COMMENT_JAVADOC_TEMPLATE_CLASS,
			env.interpolate(classMask));

		convention.put(
			ConventionKeys.COMMENT_JAVADOC_TEMPLATE_INTERFACE,
			env.interpolate(classMask));
		
		jalopy.format();
	
		String newContent = sb.toString();
	
		// Remove double blank lines after the package or last import
	
		newContent = newContent.replaceFirst(
			"(?m)^[ \t]*((?:package|import) .*;)\\s*^[ \t]*/\\*\\*",
			"$1\n\n/**");
	
		// Write file if and only if the file has changed
	
		String oldContent = null;
	
		if (file.exists()) {
			oldContent = FileUtil.read(file);
			
			if (oldContent != null) {
				int oldPkgStart = oldContent.indexOf("package");
				
				String oldPkgPrefix = oldContent.substring(0, oldPkgStart);
	
				int newPkgStart = newContent.indexOf("package");
				
				newContent = oldPkgPrefix + newContent.substring(newPkgStart);
			}
		}
		
		if (oldContent == null || !oldContent.equals(newContent)) {
			FileUtil.write(file, newContent);
	
			System.out.println("Writing " + file);
		}
	
		tempFile.deleteOnExit();
	}

	private Map<String, List<List<String>>> _indices = 
		new HashMap<String, List<List<String>>>();
	private Map<String, List<List<String>>> _uniqueIndices = 
		new HashMap<String, List<List<String>>>();
	private String _packagePath;
	private String _serviceOutputPath;
	private String _implOutputPath;
	private String _serviceApiDir;	
	private String _serviceImplDir;
	private String _springFileName;
	private String _indexerFileName;
	private boolean _liferayService;
	private List<ServiceDefinition.Document> _documents = null;
	
	private static Logger _log = LoggerFactory.getLogger(ServiceBuilder.class);
	
}