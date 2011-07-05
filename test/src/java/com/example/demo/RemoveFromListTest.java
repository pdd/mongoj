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
import com.example.demo.model.CarModel.RegInfo;
import com.example.demo.service.UserLocalServiceUtil;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class RemoveFromListTest extends TestBase {

	@org.junit.Test
	public void removeFromList() throws SystemException, UpdateException {
		//create 3 cars
		Car car1 = createCar(1);

		Car car2 = createCar(2);

		Car car3 = createCar(3);

		//create one user
		User user = createUser("Joe", "Black", true, 
			1234L, null, new Date(), "1111111111", "1 Main St.", 12345);
		
		String id = user.getId();
				
		//create a list of car ids
		List<String> likedCars = new ArrayList<String>();
		likedCars.add(car1.getId());
		likedCars.add(car1.getId());
		likedCars.add(car1.getId());
		likedCars.add(car2.getId());
		likedCars.add(car3.getId());
				
		//append the whole list to likes (car1 in duplicate)
		user.appendToLikedCars(likedCars);
		UserLocalServiceUtil.updateUser(user);
		
		//get from db
		user = UserLocalServiceUtil.getUser(id);
		
		//remove all occurences of car1's id from list (3 occurences)
		user.removeFromLikedCars(car1.getId());
		
		//update
		UserLocalServiceUtil.updateUser(user);
		
		//get from db
		user = UserLocalServiceUtil.getUser(id);
		
		likedCars = user.getLikedCars();
		
		//verify there are only 2 likes
		assertTrue(likedCars.size() == 2 &&
			likedCars.get(0).equals(car2.getId()) &&
			likedCars.get(1).equals(car3.getId()));
		
		//list of 2 cars ids
		likedCars = new ArrayList<String>();
		likedCars.add(car2.getId());
		likedCars.add(car3.getId());
		
		//get from db
		user = UserLocalServiceUtil.getUser(id);
		
		//remove all occurences of all in the list
		user.removeFromLikedCars(likedCars);
		
		//update
		UserLocalServiceUtil.updateUser(user);
		
		//get from db
		user = UserLocalServiceUtil.getUser(id);
		
		likedCars = user.getLikedCars();
		
		//verify there are only 2 likes
		assertTrue(likedCars.size() == 0);

		//
		//test remove from list in embedded object
		//
		
		user = UserLocalServiceUtil.getUser(id);
		
		//append a list to list of an embedded object
		List<Date> reminders = user.getInfo().getReminders();
		reminders = new ArrayList<Date>();
		Date date = new Date();
		reminders.add(date);
		reminders.add(date);
		user.getInfo().appendToReminders(reminders);
		
		//update
		UserLocalServiceUtil.updateUser(user);

		user = UserLocalServiceUtil.getUser(id);
		
		user.getInfo().removeFromReminders(date);
		
		//update
		UserLocalServiceUtil.updateUser(user);

		//get from db
		user = UserLocalServiceUtil.getUser(id);
		
		
		//verify there are only 3 original reminders added by create
		assertTrue(user.getInfo().getReminders().size() == 3);
	}

}