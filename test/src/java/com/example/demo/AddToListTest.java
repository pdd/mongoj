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
import com.example.demo.model.RegisteredDriver;
import com.example.demo.model.User;
import com.example.demo.model.impl.RegisteredDriverImpl;
import com.example.demo.service.UserLocalServiceUtil;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class AddToListTest extends TestBase {

	//@org.junit.Test
	public void addToList() throws SystemException, UpdateException {
		//create 3 cars
		Car car1 = createCar(1);
		
		Car car2 = createCar(2);

		Car car3 = createCar(3);

		//create one user
		User user = createUser("Joe", "Black", true, 
			1234L, null, new Date(), "11111111", "1 Main St.", 12345);
		
		String id = user.getId();
		
		//add one single car id to likes
		user.addToLikedCars(car1.getId());
		UserLocalServiceUtil.updateUser(user);
		
		//add same carId again
		user = UserLocalServiceUtil.getUser(id);
		user.addToLikedCars(car1.getId());
		UserLocalServiceUtil.updateUser(user);
		
		//verify liked cars contains one item
		user = UserLocalServiceUtil.getUser(id);
		List<String> likedCars = user.getLikedCars();
		assertTrue(likedCars.size() == 1 &&
			likedCars.get(0).equals(car1.getId()));

		//create a list of car ids
		likedCars = new ArrayList<String>();
		likedCars.add(car1.getId());
		likedCars.add(car2.getId());
		likedCars.add(car3.getId());
		
		//also set one non-list field
		user.setFirstName("James");

		//add the whole list to likes (car1 in duplicate)
		user.addToLikedCars(likedCars);
		UserLocalServiceUtil.updateUser(user);
		
		//verify there are only 3 likes
		user = UserLocalServiceUtil.getUser(id);
		likedCars = user.getLikedCars();
		assertTrue(likedCars.size() == 3 &&
			likedCars.get(0).equals(car1.getId()) &&
			likedCars.get(1).equals(car2.getId()) &&
			likedCars.get(2).equals(car3.getId()) &&
			user.getFirstName().equals("James"));

		user = UserLocalServiceUtil.getUser(id);
		
		//add 2 values to list of an embedded object
		Date date = new Date();
		user.getInfo().addToReminders(date);
		user.getInfo().addToReminders(date);
		
		//update
		UserLocalServiceUtil.updateUser(user);

		user = UserLocalServiceUtil.getUser(id);
		
		List<Date> reminders = user.getInfo().getReminders();

		//createUser adds 3 reminders already
		//we added 2 more but model should have added only 1 and skip duplicate 
		//verify the list contains only 4 values
		assertTrue(reminders.size() == 4 &&
			reminders.get(3).equals(date));
		
		user = UserLocalServiceUtil.getUser(id);
		
		//append a list to list of an embedded object
		reminders = new ArrayList<Date>();
		reminders.add(date);		//add same date (should skip)

		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 1, 29);
		date = calendar.getTime();
		
		reminders.add(date);		//add one more
		reminders.add(date);		//add same date (should skip)
		user.getInfo().addToReminders(reminders);
		
		//update
		UserLocalServiceUtil.updateUser(user);
		
		user = UserLocalServiceUtil.getUser(id);
		
		reminders = user.getInfo().getReminders();
		
		assertTrue(reminders.size() == 5);
	}
	
	@org.junit.Test
	public void addToCustomList() throws SystemException, UpdateException {
		//create one user
		User user = createUser("Joe", "Black", true, 
			1234L, null, new Date(), "11111111", "1 Main St.", 12345);

		String id = user.getId();
		
		//Custom List
		List<RegisteredDriver> drivers = new ArrayList<RegisteredDriver>();
		
		RegisteredDriver driver = new RegisteredDriverImpl();
		
		driver.setName("Spouse");
		driver.setAge(35);
		
		drivers.add(driver);
		
		driver = new RegisteredDriverImpl();
		
		driver.setName("Daughter");
		driver.setAge(18);
		
		drivers.add(driver);
		
		user.addToRegisteredDrivers(drivers);
		
		UserLocalServiceUtil.updateUser(user);

		//update
		UserLocalServiceUtil.updateUser(user);
		
		user = UserLocalServiceUtil.getUser(id);
		
		drivers = user.getRegisteredDrivers();
		
		assertTrue(drivers.size() == 2);
	}
}
