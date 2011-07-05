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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.mongoj.exception.SystemException;
import org.mongoj.exception.UpdateException;

import com.example.demo.model.Car;
import com.example.demo.model.User;
import com.example.demo.service.CarLocalServiceUtil;
import com.example.demo.service.UserLocalServiceUtil;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class SearchTest extends TestBase {
	
	@org.junit.Test
	public void searchUsers() throws SystemException, UpdateException {
		//add first user	
		User user = createUser("Joe", "Black", true, 
			1234L, null, new Date(), "33333333", "1 Main St.", 12345);
		
		//verify first user added
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		assertTrue(user != null);

		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 1, 29);
		Date date = calendar.getTime();	
	
		//add second user	
		user = createUser("James", "White", true, 5678L, 
			null, date, "11111111", "10 Market St.", 67891);
				
		//verify second user added
		user = UserLocalServiceUtil.getByUserId(user.getUserId());
		assertTrue(user != null);
		
		//retain second user's id
		String id = user.getId();

		//since the return type is User in service definition, this should
		//return the first found
		user = UserLocalServiceUtil.getByFN_LN("James", "White");
		
		//verify second user's id and the id of user found by FN_LN matches
		assertTrue(user != null &&  user.getId().equals(id));
		
		//add third user with same FN and DOB as second user
		user = createUser("James", "Brown", false, 222L, 
			null, date, "22222222", "781 Jan St.", 77777);
		
		//search by first name and dob
		List<User> usersWithFN_DOB = UserLocalServiceUtil.getByFN_DOB(
			"James", date);
		
		//verify that it returns 2 users
		assertTrue(usersWithFN_DOB != null && usersWithFN_DOB.size() == 2);
	}

	@org.junit.Test
	public void finderTest() throws SystemException, UpdateException {
		//create 500 cars
		createManyCars(500);
		
		//default findAll should return 100 results
		List<Car> cars = CarLocalServiceUtil.findAll();
		
		//verify
		assertTrue(cars != null && cars.size() == 100);
		
		//try end = 1000
		cars = CarLocalServiceUtil.findAll(0, 1000);
		
		//verify it got 500, as many as created
		assertTrue(cars != null && cars.size() == 500);
		
		//try start-end
		cars = CarLocalServiceUtil.findAll(100, 101);
		
		System.out.println(cars);
		
		//verify gets 100th product
		assertTrue(cars != null && cars.size() == 1 &&
			cars.get(0).getMake().equals("Make - 100"));
	}
	
}
