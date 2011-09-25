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

package org.mongoj.samples.model;

import org.mongoj.model.BaseModel;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */
public interface UserModel extends BaseModel<User> {
	public long getUserId();

	public void setUserId(long userId);

	public String getFirstName();

	public void setFirstName(String firstName);

	public String getLastName();

	public void setLastName(String lastName);

	public String getPassword();

	public void setPassword(String password);

	public boolean getActive();

	public void setActive(boolean active);

	public byte[] getImage();

	public void setImage(byte[] image);

	public interface Info extends Serializable {
		public Date getDob();

		public void setDob(Date dob);

		public interface Address extends Serializable {
			public String getStreet();

			public void setStreet(String street);

			public int getZip();

			public void setZip(int zip);

			public Map<String, Object> toMap();
		}

		public Address getAddress();

		public void setAddress(Address address);

		public String getPhone();

		public void setPhone(String phone);

		public List<Date> getReminders();

		public void setReminders(List<Date> reminders);

		/**
		* Appends to the end of List, irrespective of value already existing.
		* See addToReminders to add without duplicating.
		*/
		public void appendToReminders(Date reminders);

		/**
		* Appends to the end of List, irrespective of value already existing.
		* See addToReminders to add without duplicating.
		*/
		public void appendToReminders(List<Date> reminders);

		/**
		* Adds to the List by treating List as a Set.
		* Thus adds only if not already exists in the List and avoids duplication.
		* See appendToReminders to append to List.
		*/
		public void addToReminders(Date reminders);

		/**
		* Adds to the List by treating List as a Set.
		* Thus adds only if not already exists in the List and avoids duplication.
		* See appendToReminders to append to List.
		*/
		public void addToReminders(List<Date> reminders);

		/**
		* Removes all occurences of value from the List
		*/
		public void removeFromReminders(Date reminders);

		/**
		* Removes all occurences of all values from the List
		*/
		public void removeFromReminders(List<Date> reminders);

		public Map<String, Object> toMap();
	}

	public Info getInfo();

	public void setInfo(Info info);

	public List<String> getLikedCars();

	public void setLikedCars(List<String> likedCars);

	/**
	* Appends to the end of List, irrespective of value already existing.
	* See addToLikedCars to add without duplicating.
	*/
	public void appendToLikedCars(String likedCars);

	/**
	* Appends to the end of List, irrespective of value already existing.
	* See addToLikedCars to add without duplicating.
	*/
	public void appendToLikedCars(List<String> likedCars);

	/**
	* Adds to the List by treating List as a Set.
	* Thus adds only if not already exists in the List and avoids duplication.
	* See appendToLikedCars to append to List.
	*/
	public void addToLikedCars(String likedCars);

	/**
	* Adds to the List by treating List as a Set.
	* Thus adds only if not already exists in the List and avoids duplication.
	* See appendToLikedCars to append to List.
	*/
	public void addToLikedCars(List<String> likedCars);

	/**
	* Removes all occurences of value from the List
	*/
	public void removeFromLikedCars(String likedCars);

	/**
	* Removes all occurences of all values from the List
	*/
	public void removeFromLikedCars(List<String> likedCars);

	public List<String> getFollowedUsers();

	public void setFollowedUsers(List<String> followedUsers);

	/**
	* Appends to the end of List, irrespective of value already existing.
	* See addToFollowedUsers to add without duplicating.
	*/
	public void appendToFollowedUsers(String followedUsers);

	/**
	* Appends to the end of List, irrespective of value already existing.
	* See addToFollowedUsers to add without duplicating.
	*/
	public void appendToFollowedUsers(List<String> followedUsers);

	/**
	* Adds to the List by treating List as a Set.
	* Thus adds only if not already exists in the List and avoids duplication.
	* See appendToFollowedUsers to append to List.
	*/
	public void addToFollowedUsers(String followedUsers);

	/**
	* Adds to the List by treating List as a Set.
	* Thus adds only if not already exists in the List and avoids duplication.
	* See appendToFollowedUsers to append to List.
	*/
	public void addToFollowedUsers(List<String> followedUsers);

	/**
	* Removes all occurences of value from the List
	*/
	public void removeFromFollowedUsers(String followedUsers);

	/**
	* Removes all occurences of all values from the List
	*/
	public void removeFromFollowedUsers(List<String> followedUsers);
}