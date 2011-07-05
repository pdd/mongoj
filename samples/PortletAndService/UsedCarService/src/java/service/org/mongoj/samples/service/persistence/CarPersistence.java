/**
* Copyright (c) 2011 Prashant Dighe
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

package org.mongoj.samples.service.persistence;

import org.mongoj.samples.model.Car;

import org.mongoj.service.persistence.BasePersistence;

/**
 * @author Prashant Dighe
 */
public interface CarPersistence extends BasePersistence<Car> {
	public org.mongoj.samples.model.Car create();

	public long countAll() throws org.mongoj.exception.SystemException;

	public org.mongoj.samples.model.Car findByVIN(java.lang.String vIN)
		throws org.mongoj.exception.SystemException;

	public java.util.List<org.mongoj.samples.model.Car> findByMake(
		java.lang.String make) throws org.mongoj.exception.SystemException;

	public java.util.List<org.mongoj.samples.model.Car> findByMake(
		java.lang.String make, int start, int end)
		throws org.mongoj.exception.SystemException;

	public java.util.List<org.mongoj.samples.model.Car> findAll()
		throws org.mongoj.exception.SystemException;

	public java.util.List<org.mongoj.samples.model.Car> findAll(int start,
		int end) throws org.mongoj.exception.SystemException;
}