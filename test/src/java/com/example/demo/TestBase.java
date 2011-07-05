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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongoj.db.DBFactoryUtil;
import org.mongoj.exception.SystemException;
import org.mongoj.exception.UpdateException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.demo.model.Car;
import com.example.demo.model.CarModel.RegInfo;
import com.example.demo.model.impl.CarImpl;
import com.example.demo.model.User;
import com.example.demo.service.CarLocalServiceUtil;
import com.example.demo.service.UserLocalServiceUtil;
import com.mongodb.DB;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class TestBase {

	public TestBase() {
		ApplicationContext context = 
			new ClassPathXmlApplicationContext("spring.xml");		
	}
	
	/*
	public static void main(String[] args) throws Exception {
		Result result = JUnitCore.runClasses(TestBase.class);
		
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.getDescription());
		}
	}
	*/
	
	//@org.junit.After
	@org.junit.Before
	public void cleanupDB() {
		DB db = DBFactoryUtil.getDBFactory().getDB();
		
		db.dropDatabase();
	}

	public User createUser(String firstName, String lastName, boolean active,
		long userId, byte[] image, Date dob, String phone, 
		String street, int zip) 
		throws SystemException, UpdateException {
		User user = UserLocalServiceUtil.createUser();
		user.setFirstName(firstName);
		user.setLastName(lastName);	
		user.setActive(active);
		user.setUserId(userId);
		user.setImage(image);

		User.Info info = user.getInfo();
		User.Info.Address address = info.getAddress();
		address.setStreet(street);
		address.setZip(zip);
		info.setDob(dob);
		info.setAddress(address);
		info.setPhone(phone);
		
		List<Date> reminders = new ArrayList<Date>() {{
			add(new Date());
			add(new Date(10000000));
			add(new Date(100000000000L));
		}};
		
		info.setReminders(reminders);
		
		user = UserLocalServiceUtil.addUser(user);

		return user;
	}

	public Car createCar(String make, String model,
		String style, int year, int mileage, String color, 
		List<byte[]> photos, byte[] thumbnail, boolean automatic,
		String VIN, double price, List<RegInfo> history, int likes) 
		throws SystemException, UpdateException {
		
		Car car = CarLocalServiceUtil.createCar();
		
		car.setMake(make);
		car.setModel(model);
		car.setStyle(style);
		car.setYear(year);
		car.setMileage(mileage);
		car.setColor(color);
		car.setPhotos(photos);
		car.setThumbnail(thumbnail);
		car.setAutomatic(automatic);
		car.setVIN(VIN);
		car.setPrice(price);
		car.setHistory(history);
		car.setLikes(likes);
		
		car = CarLocalServiceUtil.addCar(car);
		
		return car;
	}
	
	public Car createCar(int suffix) throws SystemException, UpdateException {
		List<Car.RegInfo.ServiceDetails> svcDetailsList = 
			new ArrayList<Car.RegInfo.ServiceDetails>(); 
		
		Car.RegInfo.ServiceDetails serviceDetails  = 
			new CarImpl.RegInfoImpl.ServiceDetailsImpl();
		
		serviceDetails.setServiceDate(new Date(suffix));
		serviceDetails.setNotes("note - " + 1);
		
		svcDetailsList.add(serviceDetails);
		
		serviceDetails  = 
			new CarImpl.RegInfoImpl.ServiceDetailsImpl();
		
		serviceDetails.setServiceDate(new Date(2 * suffix));
		serviceDetails.setNotes("note - " + 2 );
		
		svcDetailsList.add(serviceDetails);
		
		List<Car.RegInfo> regInfoList = new ArrayList<Car.RegInfo>();
		
		Car.RegInfo regInfo = new CarImpl.RegInfoImpl();
		regInfo.setRegDate(new Date(suffix));
		regInfo.setMileage(suffix);
		regInfo.setState("ca " + 1);
		regInfo.setServiceRecord(svcDetailsList);
		
		regInfoList.add(regInfo);
		
		regInfo = new CarImpl.RegInfoImpl();
		regInfo.setRegDate(new Date());
		regInfo.setMileage(2 * suffix);
		regInfo.setState("ca " + 2);
		regInfo.setServiceRecord(svcDetailsList);
		
		regInfoList.add(regInfo);
		
		return	createCar("Make - " + suffix,
			"Model - " + suffix,
			"Style - " + suffix,
			2000 + suffix,
			5000 + suffix,
			"Color - " + suffix,
			new ArrayList<byte[]>(),
			new byte[] {},
			true,
			"VIN - " + suffix,
			getRandomPrice(), 
			regInfoList,
			suffix);
	}
	
	public void createManyCars(int howMany) 
		throws SystemException, UpdateException {
		for (int i = 0; i < howMany ; i++) {
			createCar(i);		
		}
	}

	public double getRandomPrice() {
		return new BigDecimal(Math.random() * 100)
			.setScale(2, BigDecimal.ROUND_HALF_UP)
			.doubleValue();
	}
	
}
