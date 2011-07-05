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

package com.example.demo;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.mongoj.exception.SystemException;
import org.mongoj.exception.UpdateException;

import com.example.demo.model.Car;
import com.example.demo.model.User;
import com.example.demo.model.impl.CarImpl;
import com.example.demo.service.CarLocalServiceUtil;
import com.example.demo.service.UserLocalServiceUtil;

/**
 * 
 * @author Prashant Dighe
 *
 * This is a test for field updates which are not List types.
 * The List types are tested separately for add/append/remove.
 * 
 */
public class UpdateTest extends TestBase {
	
	@org.junit.Test
	public void setCarFields() throws SystemException, UpdateException {
		Car car = createCar(1);
		
		List<Car.RegInfo.ServiceDetails> svcDetailsList = 
			new ArrayList<Car.RegInfo.ServiceDetails>(); 
		
		Car.RegInfo.ServiceDetails serviceDetails  = 
			new CarImpl.RegInfoImpl.ServiceDetailsImpl();

		serviceDetails.setServiceDate(new Date());
		serviceDetails.setNotes("note - update");
		
		svcDetailsList.add(serviceDetails);
				
		List<Car.RegInfo> regInfoList = new ArrayList<Car.RegInfo>();
		
		Car.RegInfo regInfo = new CarImpl.RegInfoImpl();
		regInfo.setRegDate(new Date());
		regInfo.setMileage(5000);
		regInfo.setState("VA");
		regInfo.setServiceRecord(svcDetailsList);
		
		regInfoList.add(regInfo);
		
		car.setHistory(regInfoList);
		
		car.setMake("ToyHonda");
		
		//update existing
		CarLocalServiceUtil.updateCar(car);
		
		//get from DB
		car = CarLocalServiceUtil.getCar(car.getId());
		
		System.out.println(car);
	}
	
//	@org.junit.Test
	public void setUserFields() throws SystemException, UpdateException {
		//create one user
		User user = createUser("Joe", "Black", true, 
			1234L, null, new Date(), "1111111111", "1 Main St.", 12345);
		
		//get user from db
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		
		//set only one field
		user.setFirstName("Moe");
		
		//update
		UserLocalServiceUtil.updateUser(user);
		
		//get from db again
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		
		//verify the field value
		assertTrue(user != null &&
			"Moe".equals(user.getFirstName()));

		//this time set 2 fields
		user.setFirstName("Joe");
		user.setLastName("Sixpack");
		
		//update
		UserLocalServiceUtil.updateUser(user);
		
		//get from db again
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		
		//verify 2 fields
		assertTrue(user != null &&
			"Joe".equals(user.getFirstName()) &&
			"Sixpack".equals(user.getLastName()));
		
		//get from db again
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		
		//test for embedded object
		User.Info info = user.getInfo();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 1, 29);
		Date date = calendar.getTime();	
		
		info.setDob(date);
		
		//update
		UserLocalServiceUtil.updateUser(user);		
		
		//get from db again
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		Date dob = user.getInfo().getDob();
		
		assertTrue(date.equals(dob));
		
		//get from db again
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		
		//test for fields in embedded object
		User.Info.Address address = user.getInfo().getAddress();
		address.setZip(10000);
		
		//update
		UserLocalServiceUtil.updateUser(user);
		
		//get from db again
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		
		int zip = user.getInfo().getAddress().getZip();
		
		//verify
		assertTrue(zip == 10000);
		
	}
	
}