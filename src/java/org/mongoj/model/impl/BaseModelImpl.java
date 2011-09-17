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

package org.mongoj.model.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mongoj.model.BaseModel;

/**
 * 
 * @author Prashant Dighe
 *
 */
public abstract class BaseModelImpl<T> implements BaseModel<T> {

	public static final String ID = "_id";
	public static final String COMMA = ",";
	public static final String SINGLE_QUOTE = "'";
	public static final String COLON = " : ";
	public static final String OPEN_BRACE = "{";
	public static final String CLOSE_BRACE = "}";
	public static final String OPEN_BRACKET = "[";
	public static final String CLOSE_BRACKET = "]";
	
	public Map<String, Object> setMap = new HashMap<String, Object>();
	public Map<String, List<Object>> appendMap = 
		new HashMap<String, List<Object>>();
	public Map<String, List<Object>> addMap = 
		new HashMap<String, List<Object>>();
	public Map<String, List<Object>> removeMap = 
		new HashMap<String, List<Object>>();
	
	public boolean isNew() {
		return _new;
	}
	
	public void setNew(boolean n) {
		_new = n;
	}
	
	public String getId() {
		return _id;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	/**
	 * BSON can not serialize List<CustomType> type of Lists. It throws
	 * serialization exception even though the CustomType is Serializable
	 * and can be successfully serialized using Java serialization.
	 * Hence this workaround.
	 * 
	 * @param List<CustomType>
	 * @return List<Map<String, Object>> or null if List can not be constructed
	 */
	public static List<Map<String, Object>> objectListToMapList(
		List<?> objectList) {
		if (objectList == null) {
			return null;
		}
		
		try {
			List<Map<String, Object>> mapList = 
				new ArrayList<Map<String, Object>>();
	
			if (objectList.isEmpty()) {
				return mapList;
			}
			
			Class<?> clazz = objectList.get(0).getClass();
			Method method = clazz.getMethod("toMap", new Class[0]);
			
			for (Object object : objectList) {
				mapList.add(
					(Map<String, Object>)method.invoke(object, new Object[0]));
			}
			
			return mapList;
		}
		catch (Exception e) {
			return null;
		}
	}

	public void updateAppendMap(String fieldName, Object fieldValue) {
		List<Object> appendList = (List<Object>)appendMap.get(fieldName);

		if (appendList == null) {
			appendList = new ArrayList<Object>();
		}

		if (fieldValue instanceof List) {
			appendList.addAll((List)fieldValue);
		}
		else {
			appendList.add(fieldValue);
		}

		appendMap.put(fieldName, appendList);
	}
	
	public void updateAppendMap(String fieldName, List<Object> appendList) {
		for (Object object : appendList) {
			updateAddMap(fieldName, object);
		}
	}

	public void updateAddMap(String fieldName, Object fieldValue) {
		List<Object> addList = (List<Object>)addMap.get(fieldName);

		if (addList == null) {
			addList = new ArrayList<Object>();
		}

		addList.add(fieldValue);

		addMap.put(fieldName, addList);
	}
	
	public void updateAddMap(String fieldName, List<Object> addList) {
		for (Object object : addList) {
			updateAddMap(fieldName, object);
		}
	}

	public void updateRemoveMap(String fieldName, Object fieldValue) {
		List<Object> removeList = (List<Object>)removeMap.get(fieldName);

		if (removeList == null) {
			removeList = new ArrayList<Object>();
		}

		if (fieldValue instanceof List) {
			removeList.addAll((List)fieldValue);
		}
		else {
			removeList.add(fieldValue);
		}

		removeMap.put(fieldName, removeList);
	}
	
	public void updateRemoveMap(String fieldName, List<Object> removeList) {
		for (Object object : removeList) {
			updateRemoveMap(fieldName, object);
		}		
	}

	public String toJSON() {
		StringBuilder jsonSB = new StringBuilder();
		
		Map<String, Object> map = toMap();
		
		map.put(ID, _id);
		
		_appendJSON(jsonSB, map);
		
		return jsonSB.toString();
	}

	private void _appendJSON(StringBuilder sb, Map<String, Object> map) {
		sb.append(OPEN_BRACE);
		
		Iterator<String> keys = map.keySet().iterator();
			
		while(keys.hasNext()) {
			String key = keys.next();
			
			Object object = map.get(key);

			sb.append(SINGLE_QUOTE)
				.append(key)
				.append(SINGLE_QUOTE)
				.append(COLON);

			_appendJSON(sb, object);
			
			if (keys.hasNext()) {
				sb.append(COMMA);
			}
		}
		
		sb.append(CLOSE_BRACE);
	}
	
	private void _appendJSON(StringBuilder sb, List<Object> list) {
		sb.append(OPEN_BRACKET);
		
		Iterator<Object> objects = list.iterator();
		
		while(objects.hasNext()) {			
			Object object = objects.next();
			
			_appendJSON(sb, object);
			
			if (objects.hasNext()) {
				sb.append(COMMA);
			}
		}
		
		sb.append(CLOSE_BRACKET);
	}
	
	@SuppressWarnings("unchecked")
	private void _appendJSON(StringBuilder sb, Object object) {
		if (object instanceof Map) {
			_appendJSON(sb, (Map<String, Object>)object);
		}
		else if (object instanceof List) {
			_appendJSON(sb, (List<Object>)object);
		}
		else if (object instanceof byte[]) {
			sb.append(SINGLE_QUOTE)
				.append(new sun.misc.BASE64Encoder().encode((byte[])object))
				.append(SINGLE_QUOTE);
		}
		else if (object instanceof Date) {
			sb.append(SINGLE_QUOTE)
				.append(((Date)object).toString())
				.append(SINGLE_QUOTE);
		}
		else {
			if (object instanceof String) {
				sb.append(SINGLE_QUOTE);
			}
	
			sb.append(object.toString());
			
			if (object instanceof String) {
				sb.append(SINGLE_QUOTE);
			}
		}
	}
	
	protected String _id = null;
	
	private boolean _new = false;

	private static final long serialVersionUID = 1L;
}