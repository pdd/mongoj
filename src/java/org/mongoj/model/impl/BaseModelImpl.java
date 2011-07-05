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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mongoj.model.BaseModel;

/**
 * 
 * @author Prashant Dighe
 *
 */
public abstract class BaseModelImpl<T> implements BaseModel<T> {

	private static final long serialVersionUID = 1L;
	
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

	private boolean _new = false;
	
}