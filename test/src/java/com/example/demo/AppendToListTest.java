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
import java.util.Date;
import java.util.List;

import org.mongoj.exception.SystemException;
import org.mongoj.exception.UpdateException;

import com.example.demo.model.Car;
import com.example.demo.model.User;
import com.example.demo.service.UserLocalServiceUtil;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class AppendToListTest extends TestBase {

	@org.junit.Test
	public void appendToList() throws SystemException, UpdateException {
		//create 3 cars
		Car car1 = createCar(1);

		Car car2 = createCar(2);

		Car car3 = createCar(3);

		//create one user
		User user = createUser("Joe", "Black", true, 
			1234L, null, new Date(), "1111111111", "1 Main St.", 12345);
		
		String id = user.getId();
		
		//add one single car id to likes
		user.appendToLikedCars(car1.getId());
		UserLocalServiceUtil.updateUser(user);
		
		//append same carId again
		user = UserLocalServiceUtil.getUser(id);
		user.appendToLikedCars(car1.getId());
		UserLocalServiceUtil.updateUser(user);
		
		//verify liked cars contains 2 items
		user = UserLocalServiceUtil.getUser(id);
		List<String> likedCar = user.getLikedCars();
		assertTrue(likedCar.size() == 2 &&
			likedCar.get(0).equals(car1.getId()) &&
			likedCar.get(1).equals(car1.getId()));
		
		//create a list of car ids
		likedCar = new ArrayList<String>();
		likedCar.add(car1.getId());
		likedCar.add(car2.getId());
		likedCar.add(car3.getId());
		
		//also set one non-list field
		user.setFirstName("James");
		
		//append the whole list to likes (car1 in duplicate)
		user.appendToLikedCars(likedCar);
		UserLocalServiceUtil.updateUser(user);
		
		//verify there are only 3 likes
		user = UserLocalServiceUtil.getUser(id);
		likedCar = user.getLikedCars();
		assertTrue(likedCar.size() == 5 &&
			likedCar.get(0).equals(car1.getId()) &&
			likedCar.get(1).equals(car1.getId()) &&
			likedCar.get(2).equals(car1.getId()) &&
			likedCar.get(3).equals(car2.getId()) &&
			likedCar.get(4).equals(car3.getId()) &&
			user.getFirstName().equals("James"));
		
		user = UserLocalServiceUtil.getUser(id);
		
		//append to list of an embedded object
		Date date = new Date();
		user.getInfo().appendToReminders(date);
		
		//update
		UserLocalServiceUtil.updateUser(user);
		
		user = UserLocalServiceUtil.getUser(id);
		
		List<Date> reminders = user.getInfo().getReminders();
		
		//create set 3 reminders and we added 1
		assertTrue(reminders.size() == 4 &&
			reminders.get(3).equals(date));
		
		user = UserLocalServiceUtil.getUser(id);
		
		//append a list to list of an embedded object
		reminders = new ArrayList<Date>();
		reminders.add(new Date());
		reminders.add(new Date());
		user.getInfo().appendToReminders(reminders);
		
		//update
		UserLocalServiceUtil.updateUser(user);
		
		user = UserLocalServiceUtil.getUser(id);
		
		reminders = user.getInfo().getReminders();
		
		assertTrue(reminders.size() == 6);
	}

}
