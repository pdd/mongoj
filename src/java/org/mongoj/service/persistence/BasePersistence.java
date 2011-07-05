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

package org.mongoj.service.persistence;

import java.io.Serializable;

import org.mongoj.db.DBFactory;
import org.mongoj.exception.UpdateException;
import org.mongoj.exception.SystemException;
import org.mongoj.model.BaseModel;

import com.mongodb.DB;

/**
 * 
 * @author Prashant Dighe
 *
 */
public interface BasePersistence<T extends BaseModel<T>> {

	public static final String SET_OPERATOR = "$set";
	
	public static final String PUSH_OPERATOR = "$push";
	
	public static final String PUSH_ALL_OPERATOR = "$pushAll";
	
	public static final String PULL_OPERATOR = "$pull";
	
	public static final String PULL_ALL_OPERATOR = "$pullAll";
	
	public static final String ADD_TO_SET_OPERATOR = "$addToSet";
	
	public static final String EACH_OPERATOR = "$each";
	
	public static final int LIMIT = 100;
	
	public T fetchByPrimaryKey(Serializable primaryKey) throws SystemException;

	public void remove(String id) throws UpdateException, SystemException;
	
	public T remove(T model) throws UpdateException, SystemException;
	
	public void setDBFactory(DBFactory dbFactory);

	public T update(T model) throws UpdateException, SystemException;

	public DB getDB();

}