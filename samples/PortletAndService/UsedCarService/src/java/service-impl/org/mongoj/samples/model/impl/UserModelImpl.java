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

package org.mongoj.samples.model.impl;

import org.mongoj.model.impl.BaseModelImpl;

import org.mongoj.samples.model.User;
import org.mongoj.samples.model.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* This impl has to evolve to support following modifier updates when the
* type of the field allows:
*     $inc, $set, $unset, $push, $pushAll, $addToSet, $pop, $pull, $pullAll
*     $rename, $bit
*/
public class UserModelImpl extends BaseModelImpl<User> implements UserModel {
	public static final String COLLECTION_NAME = "testuser";
	public static final Object[][] FIELDS = {
			{ "userId", "String" },
			{ "firstName", "String" },
			{ "lastName", "String" },
			{ "password", "String" },
			{ "active", "boolean" },
			{ "image", "byte[]" },
			{ "info", "Info" },
			{ "likedCars", "List<String>" },
			{ "followedUsers", "List<String>" },
			{ "someList", "List<Map<String, Object>>" }
		};

	public UserModelImpl() {
	}

	public UserModelImpl(Map<String, Object> map) {
		_id = map.get("_id").toString();

		_userId = (String)map.get("userId");
		_firstName = (String)map.get("firstName");
		_lastName = (String)map.get("lastName");
		_password = (String)map.get("password");
		_active = (Boolean)map.get("active");
		_image = (byte[])map.get("image");
		_info = new InfoImpl((Map<String, Object>)map.get("info"));
		_likedCars = (List<String>)map.get("likedCars");
		_followedUsers = (List<String>)map.get("followedUsers");
		_someList = (List<Map<String, Object>>)map.get("someList");
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public String getUserId() {
		return _userId;
	}

	public void setUserId(String userId) {
		_userId = userId;

		setMap.put("userId", userId);
	}

	public String getFirstName() {
		return _firstName;
	}

	public void setFirstName(String firstName) {
		_firstName = firstName;

		setMap.put("firstName", firstName);
	}

	public String getLastName() {
		return _lastName;
	}

	public void setLastName(String lastName) {
		_lastName = lastName;

		setMap.put("lastName", lastName);
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		_password = password;

		setMap.put("password", password);
	}

	public boolean getActive() {
		return _active;
	}

	public void setActive(boolean active) {
		_active = active;

		setMap.put("active", active);
	}

	public byte[] getImage() {
		return _image;
	}

	public void setImage(byte[] image) {
		_image = image;

		setMap.put("image", image);
	}

	public class InfoImpl implements Info {
		public final Object[][] FIELDS = {
				{ "dob", "Date" },
				{ "address", "Address" },
				{ "phone", "String" },
				{ "reminders", "List<Date>" }
			};

		public InfoImpl() {
		}

		public InfoImpl(Map<String, Object> map) {
			_dob = (Date)map.get("dob");
			_address = new AddressImpl((Map<String, Object>)map.get("address"));
			_phone = (String)map.get("phone");
			_reminders = (List<Date>)map.get("reminders");
		}

		public Date getDob() {
			return _dob;
		}

		public void setDob(Date dob) {
			_dob = dob;

			setMap.put("info.dob", dob);
		}

		public class AddressImpl implements Address {
			public final Object[][] FIELDS = {
					{ "street", "String" },
					{ "zip", "int" }
				};

			public AddressImpl() {
			}

			public AddressImpl(Map<String, Object> map) {
				_street = (String)map.get("street");
				_zip = (Integer)map.get("zip");
			}

			public String getStreet() {
				return _street;
			}

			public void setStreet(String street) {
				_street = street;

				setMap.put("info.address.street", street);
			}

			public int getZip() {
				return _zip;
			}

			public void setZip(int zip) {
				_zip = zip;

				setMap.put("info.address.zip", zip);
			}

			public Map<String, Object> toMap() {
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("street", getStreet());
				map.put("zip", getZip());

				return map;
			}

			private String _street;
			private int _zip;
		}

		public Address getAddress() {
			return _address;
		}

		public void setAddress(Address address) {
			_address = address;
		}

		public String getPhone() {
			return _phone;
		}

		public void setPhone(String phone) {
			_phone = phone;

			setMap.put("info.phone", phone);
		}

		public List<Date> getReminders() {
			return _reminders;
		}

		public void setReminders(List<Date> reminders) {
			_reminders = reminders;

			setMap.put("info.reminders", reminders);
		}

		public void appendToReminders(Date reminders) {
			_reminders.add(reminders);

			_updateAppendMap("info.reminders", reminders);
		}

		public void appendToReminders(List<Date> reminders) {
			_reminders.addAll(reminders);

			_updateAppendMap("info.reminders", reminders);
		}

		public void addToReminders(Date reminders) {
			if (!_reminders.contains(reminders)) {
				_reminders.add(reminders);

				_updateAddMap("info.reminders", reminders);
			}
		}

		public void addToReminders(List<Date> reminders) {
			for (Date object : reminders) {
				if (!_reminders.contains(object)) {
					_reminders.add(object);

					_updateAddMap("info.reminders", object);
				}
			}
		}

		public void removeFromReminders(Date reminders) {
			while (_reminders.remove(reminders))
				;

			_updateRemoveMap("info.reminders", reminders);
		}

		public void removeFromReminders(List<Date> reminders) {
			_reminders.removeAll(reminders);

			_updateRemoveMap("info.reminders", reminders);
		}

		public Map<String, Object> toMap() {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("dob", getDob());
			map.put("address", _address.toMap());
			map.put("phone", getPhone());
			map.put("reminders", getReminders());

			return map;
		}

		private Date _dob;
		private Address _address = new AddressImpl();
		private String _phone;
		private List<Date> _reminders = new ArrayList<Date>();
	}

	public Info getInfo() {
		return _info;
	}

	public void setInfo(Info info) {
		_info = info;
	}

	public List<String> getLikedCars() {
		return _likedCars;
	}

	public void setLikedCars(List<String> likedCars) {
		_likedCars = likedCars;

		setMap.put("likedCars", likedCars);
	}

	public void appendToLikedCars(String likedCars) {
		_likedCars.add(likedCars);

		_updateAppendMap("likedCars", likedCars);
	}

	public void appendToLikedCars(List<String> likedCars) {
		_likedCars.addAll(likedCars);

		_updateAppendMap("likedCars", likedCars);
	}

	public void addToLikedCars(String likedCars) {
		if (!_likedCars.contains(likedCars)) {
			_likedCars.add(likedCars);

			_updateAddMap("likedCars", likedCars);
		}
	}

	public void addToLikedCars(List<String> likedCars) {
		for (String object : likedCars) {
			if (!_likedCars.contains(object)) {
				_likedCars.add(object);

				_updateAddMap("likedCars", object);
			}
		}
	}

	public void removeFromLikedCars(String likedCars) {
		while (_likedCars.remove(likedCars))
			;

		_updateRemoveMap("likedCars", likedCars);
	}

	public void removeFromLikedCars(List<String> likedCars) {
		_likedCars.removeAll(likedCars);

		_updateRemoveMap("likedCars", likedCars);
	}

	public List<String> getFollowedUsers() {
		return _followedUsers;
	}

	public void setFollowedUsers(List<String> followedUsers) {
		_followedUsers = followedUsers;

		setMap.put("followedUsers", followedUsers);
	}

	public void appendToFollowedUsers(String followedUsers) {
		_followedUsers.add(followedUsers);

		_updateAppendMap("followedUsers", followedUsers);
	}

	public void appendToFollowedUsers(List<String> followedUsers) {
		_followedUsers.addAll(followedUsers);

		_updateAppendMap("followedUsers", followedUsers);
	}

	public void addToFollowedUsers(String followedUsers) {
		if (!_followedUsers.contains(followedUsers)) {
			_followedUsers.add(followedUsers);

			_updateAddMap("followedUsers", followedUsers);
		}
	}

	public void addToFollowedUsers(List<String> followedUsers) {
		for (String object : followedUsers) {
			if (!_followedUsers.contains(object)) {
				_followedUsers.add(object);

				_updateAddMap("followedUsers", object);
			}
		}
	}

	public void removeFromFollowedUsers(String followedUsers) {
		while (_followedUsers.remove(followedUsers))
			;

		_updateRemoveMap("followedUsers", followedUsers);
	}

	public void removeFromFollowedUsers(List<String> followedUsers) {
		_followedUsers.removeAll(followedUsers);

		_updateRemoveMap("followedUsers", followedUsers);
	}

	public List<Map<String, Object>> getSomeList() {
		return _someList;
	}

	public void setSomeList(List<Map<String, Object>> someList) {
		_someList = someList;

		setMap.put("someList", someList);
	}

	public void appendToSomeList(Map<String, Object> someList) {
		_someList.add(someList);

		_updateAppendMap("someList", someList);
	}

	public void appendToSomeList(List<Map<String, Object>> someList) {
		_someList.addAll(someList);

		_updateAppendMap("someList", someList);
	}

	public void addToSomeList(Map<String, Object> someList) {
		if (!_someList.contains(someList)) {
			_someList.add(someList);

			_updateAddMap("someList", someList);
		}
	}

	public void addToSomeList(List<Map<String, Object>> someList) {
		for (Map<String, Object> object : someList) {
			if (!_someList.contains(object)) {
				_someList.add(object);

				_updateAddMap("someList", object);
			}
		}
	}

	public void removeFromSomeList(Map<String, Object> someList) {
		while (_someList.remove(someList))
			;

		_updateRemoveMap("someList", someList);
	}

	public void removeFromSomeList(List<Map<String, Object>> someList) {
		_someList.removeAll(someList);

		_updateRemoveMap("someList", someList);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userId", getUserId());
		map.put("firstName", getFirstName());
		map.put("lastName", getLastName());
		map.put("password", getPassword());
		map.put("active", getActive());
		map.put("image", getImage());
		map.put("info", _info.toMap());
		map.put("likedCars", getLikedCars());
		map.put("followedUsers", getFollowedUsers());
		map.put("someList", getSomeList());

		return map;
	}

	public String toString() {
		return toMap().toString();
	}

	public Map<String, Object> setMap = new HashMap<String, Object>();
	public Map<String, List<Object>> appendMap = new HashMap<String, List<Object>>();
	public Map<String, List<Object>> addMap = new HashMap<String, List<Object>>();
	public Map<String, List<Object>> removeMap = new HashMap<String, List<Object>>();

	private void _updateAppendMap(String fieldName, Object fieldValue) {
		List<Object> appendList = (List<Object>)appendMap.get(fieldName);

		if (appendList == null) {
			appendList = new ArrayList<Object>();
		}

		if (fieldValue instanceof List) {
			appendList.addAll((List)fieldValue);
		}
		else {
			appendList.add(fieldValue);
		}

		appendMap.put(fieldName, appendList);
	}

	private void _updateAddMap(String fieldName, Object fieldValue) {
		List<Object> addList = (List<Object>)addMap.get(fieldName);

		if (addList == null) {
			addList = new ArrayList<Object>();
		}

		addList.add(fieldValue);

		addMap.put(fieldName, addList);
	}

	private void _updateRemoveMap(String fieldName, Object fieldValue) {
		List<Object> removeList = (List<Object>)removeMap.get(fieldName);

		if (removeList == null) {
			removeList = new ArrayList<Object>();
		}

		if (fieldValue instanceof List) {
			removeList.addAll((List)fieldValue);
		}
		else {
			removeList.add(fieldValue);
		}

		removeMap.put(fieldName, removeList);
	}

	private String _id;
	private String _userId;
	private String _firstName;
	private String _lastName;
	private String _password;
	private boolean _active;
	private byte[] _image;
	private Info _info = new InfoImpl();
	private List<String> _likedCars = new ArrayList<String>();
	private List<String> _followedUsers = new ArrayList<String>();
	private List<Map<String, Object>> _someList = new ArrayList<Map<String, Object>>();
	private static final long serialVersionUID = 1L;
}