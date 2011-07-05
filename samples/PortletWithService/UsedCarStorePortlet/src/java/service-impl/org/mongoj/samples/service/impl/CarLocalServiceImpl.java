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

package org.mongoj.samples.service.impl;

import java.util.List;

import org.mongoj.exception.SystemException;
import org.mongoj.samples.service.base.CarLocalServiceBaseImpl;
import org.mongoj.samples.model.Car;

/**
 * @author Prashant Dighe
 */
public class CarLocalServiceImpl extends CarLocalServiceBaseImpl {
	
	public Car findByVIN(String VIN) throws SystemException {
		return carPersistence.findByVIN(VIN);
	}

	public List<Car> findAll() throws SystemException {
		return carPersistence.findAll();
	}
	
	public List<Car> findAll(int start, int end) throws SystemException {
		return carPersistence.findAll(start, end);
	}
}