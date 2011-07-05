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

package org.mongoj.samples.service.base;

import com.liferay.portal.kernel.annotation.BeanReference;

import org.mongoj.exception.SystemException;
import org.mongoj.exception.UpdateException;

import org.mongoj.samples.model.Car;
import org.mongoj.samples.model.impl.CarImpl;
import org.mongoj.samples.service.CarLocalService;
import org.mongoj.samples.service.UserLocalService;
import org.mongoj.samples.service.persistence.CarPersistence;
import org.mongoj.samples.service.persistence.UserPersistence;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */
public abstract class CarLocalServiceBaseImpl implements CarLocalService {
	public Car addCar(Car car) throws UpdateException, SystemException {
		return carPersistence.update(car);
	}

	public Car createCar() {
		return carPersistence.create();
	}

	public void deleteCar(String id) throws UpdateException, SystemException {
		carPersistence.remove(id);
	}

	public void deleteCar(Car car) throws UpdateException, SystemException {
		carPersistence.remove(car);
	}

	public Car getCar(String id) throws SystemException {
		return carPersistence.fetchByPrimaryKey(id);
	}

	public long getCarCount() throws SystemException {
		return carPersistence.countAll();
	}

	public Car updateCar(Car car) throws UpdateException, SystemException {
		return carPersistence.update(car);
	}

	@BeanReference(type = CarPersistence.class)
	protected CarPersistence carPersistence;
	@BeanReference(type = UserLocalService.class)
	protected UserLocalService userLocalService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
}