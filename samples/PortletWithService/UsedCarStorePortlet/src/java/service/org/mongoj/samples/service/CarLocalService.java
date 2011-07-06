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

package org.mongoj.samples.service;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 * Edit CarLocalServiceImpl and re-generate using ServiceBuilder.
 */
public interface CarLocalService {
	public org.mongoj.samples.model.Car addCar(org.mongoj.samples.model.Car car)
		throws org.mongoj.exception.UpdateException,
			org.mongoj.exception.SystemException;

	public org.mongoj.samples.model.Car createCar();

	public void deleteCar(java.lang.String id)
		throws org.mongoj.exception.UpdateException,
			org.mongoj.exception.SystemException;

	public void deleteCar(org.mongoj.samples.model.Car car)
		throws org.mongoj.exception.UpdateException,
			org.mongoj.exception.SystemException;

	public org.mongoj.samples.model.Car getCar(java.lang.String id)
		throws org.mongoj.exception.SystemException;

	public long getCarCount() throws org.mongoj.exception.SystemException;

	public org.mongoj.samples.model.Car updateCar(
		org.mongoj.samples.model.Car car)
		throws org.mongoj.exception.UpdateException,
			org.mongoj.exception.SystemException;

	public org.mongoj.samples.model.Car findByVIN(java.lang.String VIN)
		throws org.mongoj.exception.SystemException;

	public java.util.List<org.mongoj.samples.model.Car> findAll()
		throws org.mongoj.exception.SystemException;

	public java.util.List<org.mongoj.samples.model.Car> findAll(int start,
		int end) throws org.mongoj.exception.SystemException;
}