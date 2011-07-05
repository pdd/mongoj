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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class ServiceDefinition {

	public static final String TYPE_BINARY = "byte[]";

	public static final String TYPE_BOOLEAN = "boolean";
	
	public static final String TYPE_DATE = "Date";
	
	public static final String TYPE_DOUBLE = "double";
		
	public static final String TYPE_INT = "int";
	
	public static final String TYPE_LIST ="List";
	
	public static final String TYPE_LONG ="long";
	
	public static final String TYPE_MAP = "Map<String, Object>";
	
	public static final String TYPE_OBJECT = "Object";
	
	public static final String TYPE_STRING = "String";
	
	public static final String TYPE_COLLECTION = "Collection";
	
	public static class Document {
	
		public static class Field {
	
			public String getName() {
				return _name;
			}
			
			public void setName(String name) {
				if (name == null ||
					name.startsWith("$") ||
					name.indexOf('.') != -1) {
					throw new RuntimeException("Invalid name " + name);
				}
				
				_name = name;
			}

			public String getCustomListTypeImpl() {
				if (!isCustomListType()) {
					throw new RuntimeException("Field " + _name +
						" is not a custom List");
				}
				
				String listTypeName = getListTypeName();
				
				if (_fields == null && listTypeName.indexOf('.') == -1) {
					//External Custom List MUST have a fully qualified 
					//package name
					throw new RuntimeException(
						"Custom List type "+ _name + 
							" MUST be specified with " +
								"fully qualified package name.");
				}
				
				String implName = null;

				if (_fields == null) {
					// using external class
					String packageName = 
						listTypeName.substring(
							0, listTypeName.lastIndexOf('.')) +
								".impl.";
					
					String className = 
						listTypeName.substring(
							listTypeName.lastIndexOf('.') + 1) +
								"Impl";
					
					implName = packageName + className;
				}
				else {
					implName = getListTypeName() + "Impl";
				}
				
				return implName;
			}
			
			public String getFQN() {
				return _fqn;
			}
			
			public void setFQN(String fqn) {
				_fqn = fqn;
			}
			
			/*
			 * When type is Object, getter/setter returns 
			 * capitalized field name as a type
			 */
			public String getTypeName() {
				if (_type.equals(TYPE_OBJECT)) {
					return StringUtils.capitalize(_name);
				}
				
				return _type;
			}
			
			public String getVariableName() {
				return StringUtils.uncapitalize(_name);
			}
			
			public String getMethodName() {
				return StringUtils.capitalize(_name);
			}

			public String getType() {
				return _type;
			}
			
			public String getWrapperType() {
				if (!isPrimitiveType()) {
					throw new RuntimeException(
						"Type is not primitive and does not have a " +
							"corresponding Wrapper type");
				}
				
				if (_type.equals(TYPE_INT)) {
					return "Integer";
				}
				else {
					return StringUtils.capitalize(_type);
				}
			}
			
			public boolean isBinaryType() {
				return _type.equals(TYPE_BINARY);
			}
			
			public boolean isDateType() {
				return _type.equals(TYPE_DATE);
			}

			public boolean isObjectType() {
				return _type.equals(TYPE_OBJECT);
			}

			public boolean isMapType() {
				return _type.equals(TYPE_MAP);
			}

			public boolean isListType() {
				return _type.startsWith(TYPE_LIST);
			}
			
			public boolean isCustomListType() {
				return _customListType;
			}
			
			public boolean isPrimitiveType() {
				return _validPrimitiveTypes.contains(_type);
			}
			
			public void setType(String type) {
				_validateType(type);
				_type = type;
			}
						
			public String getListTypeName() {
				if (!isListType()) {
					throw new RuntimeException("Field is not a List"); 
				}
				
				return _type.substring(
					_type.indexOf('<') + 1, _type.lastIndexOf('>'));
			}
			
			public Field getField(String fieldName) {
				return _fieldMap.get(fieldName);
			}
			
			public List<Field> getFields() {
				return _fields;
			}
			
			public void setFields(List<Field> fields) {
				_fields = fields;
				
				_fieldMap = new HashMap<String, Field>();
				
				for (Field field : fields) {
					_fieldMap.put(field.getName(), field);				
				}
			}
			
			private boolean _validateType(String type) {
				if (type == null) {
					throw new NullPointerException("type is null");
				}
				
				if (type.startsWith(TYPE_LIST)) {
					String listType = type.substring(
						type.indexOf('<') + 1, type.lastIndexOf('>'));
					
					if (!_validListTypes.contains(listType)) {
						_customListType = true;
					}
				}
				else  if(!_validTypes.contains(type)) {
					throw new TypeNotPresentException(type, null);
				}
				
				return true;
			}
	
			private String _name = null;
			private String _fqn = null;
			private String _type = null;
			private boolean _customListType = false;
			private List<Field> _fields = null;
			private Map<String, Field> _fieldMap = null;
					
			private Set<String> _validTypes = new HashSet<String>(
				Arrays.asList(new String[] {
					TYPE_BINARY, 
					TYPE_BOOLEAN, 
					TYPE_DOUBLE,
					TYPE_DATE, 
					TYPE_INT,
					TYPE_LONG,
					TYPE_OBJECT,
					TYPE_STRING
				}));

			private Set<String> _validListTypes = new HashSet<String>(
				Arrays.asList(new String[] {
					TYPE_BINARY, 
					"Boolean", 
					"Double", 
					TYPE_DATE, 
					"Integer", 
					"Long", 
					TYPE_STRING, 
					TYPE_MAP
				}));
			
			private Set<String> _validPrimitiveTypes = new HashSet<String>(
				Arrays.asList(new String[] {
					TYPE_BOOLEAN, 
					TYPE_DOUBLE, 
					TYPE_INT, 
					TYPE_LONG, 
					TYPE_STRING
				}));
		}
		
		public static class Finder {

			public String getName() {
				return _name;
			}
						
			public void setName(String name) {
				_name = name;
			}
			
			public String getReturnType() {
				return _returnType;
			}
						
			public void setReturnType(String returnType) {
				_returnType = returnType;
			}
			
			public boolean isCollection() {
				return _returnType.equals(TYPE_COLLECTION);
			}

			public List<String> getFinderFields() {
				return _finderFields == null ? 
					new ArrayList<String>() : _finderFields; 
			}
			
			public void setFinderFields(List<String> finderFields) {
				_finderFields = finderFields;
			}
			
			public List<Field> getFields() {
				return _fields == null ? 
					new ArrayList<Field>() : _fields; 
			}

			public void setFields(List<Field> fields) {
				_fields = fields;
			}
			
			private String _name = null;
			private String _returnType = null;
			private List<String> _finderFields = null;
			private List<Field> _fields = null;
		}
		
		public String getCollection() {
			return _collection;
		}
		
		public void setCollection(String collection) {
			_collection = collection;
		}
				
		public List<Field> getFields() {
			return _fields;
		}
		
		public void setFields(List<Field> fields) {
			_fields = fields;
			
			_fieldMap = new HashMap<String, Field>();
			
			for (Field field : fields) {
				_fieldMap.put(field.getName(), field);				
			}
		}
		
		public Field getField(String fieldName) {
			return _fieldMap.get(fieldName);
		}
		
		public List<Finder> getFinders() {
			return _finders;
		}

		public void setFinders(List<Finder> finders) {
			_finders = finders;
		}	
		
		public String getName() {
			return _name;
		}
		
		public void setName(String name) {
			_name = name;
		}
		
		public String getVariableName() {
			return StringUtils.uncapitalize(_name);
		}

		public String getPersistenceClassName() {
			return _persistenceClassName;
		}
				
		public void setPersistenceClassName(String persistenceClassName) {
			_persistenceClassName = persistenceClassName;
		}
		
		public void setUniqueKeys(List<List<String>> uniqueKeys) {
			_uniqueKeys = uniqueKeys;
		}

		public List<List<String>> getUniqueKeys() {
			return _uniqueKeys;
		}

		private String _collection = null;
		private List<Field> _fields = null;
		private List<Finder> _finders = null;
		private String _name = null;
		private String _persistenceClassName = null;
		private Map<String, Field> _fieldMap = null;
		private List<List<String>> _uniqueKeys = null;
	}
	
	public String getAuthor() {
		return _author;
	}

	public void setAuthor(String author) {
		_author = author;
	}
	
	public List<Document> getDocuments() {
		return _documents;
	}

	public void setDocuments(List<Document> documents) {
		_documents = documents;
	}

	public String getPackage() {
		return _package;
	}

	public void setPackage(String packagePath) {
		_package = packagePath;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		sb.append("{")
			.append("author : ")
			.append(getAuthor())
			.append(",\n")
			.append("package : ")
			.append(getPackage())
			.append(",\n")
			.append("documents : [\n");
		
		for (Document document : _documents) {
			sb.append("{\n")
				.append("name : ")
				.append(document.getName())
				.append(",\n")
				.append("collection : ")
				.append(document.getCollection())
				.append(",\n")
				.append("fields : [\n");
			
				for (Document.Field field : document.getFields()) {
					sb.append(field.toString());
				}
				
			sb.append("\n]\n")
				.append("\n},\n");
		}
		
		sb.append("\n]\n")
			.append("\n},\n");
		
		return sb.toString();
	}
	
	private String _author = "Prashant Dighe";
	private List<Document> _documents = null;
	private String _package = null;
	
}