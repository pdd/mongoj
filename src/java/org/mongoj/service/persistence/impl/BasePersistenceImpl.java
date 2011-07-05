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

package org.mongoj.service.persistence.impl;

import java.io.Serializable;

import org.mongoj.db.DBFactory;
import org.mongoj.exception.UpdateException;
import org.mongoj.exception.SystemException;
import org.mongoj.model.BaseModel;
import org.mongoj.service.persistence.BasePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class BasePersistenceImpl<T extends BaseModel<T>>
	implements BasePersistence<T> {

	@Override
	public T fetchByPrimaryKey(Serializable primaryKey) throws SystemException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(String id) throws UpdateException, SystemException {
		removeImpl(id);
	}

	@Override
	public T remove(T model) throws UpdateException, SystemException {
		return removeImpl(model);
	}
	
	@Override
	public T update(T model) throws UpdateException, SystemException {
		return updateImpl(model);
	}

	protected void removeImpl(String id) 
		throws UpdateException, SystemException {
		throw new UnsupportedOperationException();
	}
	
	protected T removeImpl(T model) throws UpdateException, SystemException {
		throw new UnsupportedOperationException();
	}

	protected T updateImpl(T model) throws UpdateException, SystemException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DB getDB() {
		return _dbFactory.getDB();
	}
	
	@Override
	public void setDBFactory(DBFactory dbFactory) {
		_dbFactory = dbFactory;
	}

	private DBFactory _dbFactory;

	Logger _log = LoggerFactory.getLogger(BasePersistenceImpl.class);
	
}