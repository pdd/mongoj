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

import org.mongoj.samples.model.Car;
import org.mongoj.samples.model.CarModel;

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
public class CarModelImpl extends BaseModelImpl<Car> implements CarModel {
	public static final String COLLECTION_NAME = "cars";
	public static final Object[][] FIELDS = {
			{ "make", "String" },
			{ "model", "String" },
			{ "style", "String" },
			{ "year", "int" },
			{ "mileage", "int" },
			{ "color", "String" },
			{ "photos", "List<byte[]>" },
			{ "thumbnail", "byte[]" },
			{ "automatic", "boolean" },
			{ "VIN", "String" },
			{ "price", "double" },
			{ "history", "List<RegInfo>" },
			{ "likes", "int" }
		};

	public CarModelImpl() {
	}

	public CarModelImpl(Map<String, Object> map) {
		_id = map.get("_id").toString();

		_make = (String)map.get("make");
		_model = (String)map.get("model");
		_style = (String)map.get("style");
		_year = (Integer)map.get("year");
		_mileage = (Integer)map.get("mileage");
		_color = (String)map.get("color");
		_photos = (List<byte[]>)map.get("photos");
		_thumbnail = (byte[])map.get("thumbnail");
		_automatic = (Boolean)map.get("automatic");
		_VIN = (String)map.get("VIN");
		_price = (Double)map.get("price");
		_history = (List<RegInfo>)map.get("history");
		_likes = (Integer)map.get("likes");
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public String getMake() {
		return _make;
	}

	public void setMake(String make) {
		_make = make;

		setMap.put("make", make);
	}

	public String getModel() {
		return _model;
	}

	public void setModel(String model) {
		_model = model;

		setMap.put("model", model);
	}

	public String getStyle() {
		return _style;
	}

	public void setStyle(String style) {
		_style = style;

		setMap.put("style", style);
	}

	public int getYear() {
		return _year;
	}

	public void setYear(int year) {
		_year = year;

		setMap.put("year", year);
	}

	public int getMileage() {
		return _mileage;
	}

	public void setMileage(int mileage) {
		_mileage = mileage;

		setMap.put("mileage", mileage);
	}

	public String getColor() {
		return _color;
	}

	public void setColor(String color) {
		_color = color;

		setMap.put("color", color);
	}

	public List<byte[]> getPhotos() {
		return _photos;
	}

	public void setPhotos(List<byte[]> photos) {
		_photos = photos;

		setMap.put("photos", photos);
	}

	public void appendToPhotos(byte[] photos) {
		_photos.add(photos);

		_updateAppendMap("photos", photos);
	}

	public void appendToPhotos(List<byte[]> photos) {
		_photos.addAll(photos);

		_updateAppendMap("photos", photos);
	}

	public void addToPhotos(byte[] photos) {
		if (!_photos.contains(photos)) {
			_photos.add(photos);

			_updateAddMap("photos", photos);
		}
	}

	public void addToPhotos(List<byte[]> photos) {
		for (byte[] object : photos) {
			if (!_photos.contains(object)) {
				_photos.add(object);

				_updateAddMap("photos", object);
			}
		}
	}

	public void removeFromPhotos(byte[] photos) {
		while (_photos.remove(photos))
			;

		_updateRemoveMap("photos", photos);
	}

	public void removeFromPhotos(List<byte[]> photos) {
		_photos.removeAll(photos);

		_updateRemoveMap("photos", photos);
	}

	public byte[] getThumbnail() {
		return _thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		_thumbnail = thumbnail;

		setMap.put("thumbnail", thumbnail);
	}

	public boolean getAutomatic() {
		return _automatic;
	}

	public void setAutomatic(boolean automatic) {
		_automatic = automatic;

		setMap.put("automatic", automatic);
	}

	public String getVIN() {
		return _VIN;
	}

	public void setVIN(String VIN) {
		_VIN = VIN;

		setMap.put("VIN", VIN);
	}

	public double getPrice() {
		return _price;
	}

	public void setPrice(double price) {
		_price = price;

		setMap.put("price", price);
	}

	public List<RegInfo> getHistory() {
		return _history;
	}

	public void setHistory(List<RegInfo> history) {
		_history = history;

		setMap.put("history", history);
	}

	public static class RegInfoImpl implements RegInfo {
		public final Object[][] FIELDS = {
				{ "regDate", "Date" },
				{ "mileage", "int" },
				{ "state", "String" },
				{ "foo", "List<Bar>" }
			};

		public RegInfoImpl() {
		}

		public RegInfoImpl(Map<String, Object> map) {
			_regDate = (Date)map.get("regDate");
			_mileage = (Integer)map.get("mileage");
			_state = (String)map.get("state");
			_foo = (List<Bar>)map.get("foo");
		}

		public Date getRegDate() {
			return _regDate;
		}

		public void setRegDate(Date regDate) {
			_regDate = regDate;
		}

		public int getMileage() {
			return _mileage;
		}

		public void setMileage(int mileage) {
			_mileage = mileage;
		}

		public String getState() {
			return _state;
		}

		public void setState(String state) {
			_state = state;
		}

		public List<Bar> getFoo() {
			return _foo;
		}

		public void setFoo(List<Bar> foo) {
			_foo = foo;
		}

		public static class BarImpl implements Bar {
			public final Object[][] FIELDS = {
					{ "x", "int" },
					{ "y", "int" }
				};

			public BarImpl() {
			}

			public BarImpl(Map<String, Object> map) {
				_x = (Integer)map.get("x");
				_y = (Integer)map.get("y");
			}

			public int getX() {
				return _x;
			}

			public void setX(int x) {
				_x = x;
			}

			public int getY() {
				return _y;
			}

			public void setY(int y) {
				_y = y;
			}

			public Map<String, Object> toMap() {
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("x", getX());
				map.put("y", getY());

				return map;
			}

			private int _x;
			private int _y;
		}

		public Map<String, Object> toMap() {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("regDate", getRegDate());
			map.put("mileage", getMileage());
			map.put("state", getState());
			map.put("foo", getFoo());

			return map;
		}

		private Date _regDate;
		private int _mileage;
		private String _state;
		private List<Bar> _foo = new ArrayList<Bar>();
	}

	public void appendToHistory(RegInfo history) {
		_history.add(history);

		_updateAppendMap("history", history);
	}

	public void appendToHistory(List<RegInfo> history) {
		_history.addAll(history);

		_updateAppendMap("history", history);
	}

	public void addToHistory(RegInfo history) {
		if (!_history.contains(history)) {
			_history.add(history);

			_updateAddMap("history", history);
		}
	}

	public void addToHistory(List<RegInfo> history) {
		for (RegInfo object : history) {
			if (!_history.contains(object)) {
				_history.add(object);

				_updateAddMap("history", object);
			}
		}
	}

	public void removeFromHistory(RegInfo history) {
		while (_history.remove(history))
			;

		_updateRemoveMap("history", history);
	}

	public void removeFromHistory(List<RegInfo> history) {
		_history.removeAll(history);

		_updateRemoveMap("history", history);
	}

	public int getLikes() {
		return _likes;
	}

	public void setLikes(int likes) {
		_likes = likes;

		setMap.put("likes", likes);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("make", getMake());
		map.put("model", getModel());
		map.put("style", getStyle());
		map.put("year", getYear());
		map.put("mileage", getMileage());
		map.put("color", getColor());
		map.put("photos", getPhotos());
		map.put("thumbnail", getThumbnail());
		map.put("automatic", getAutomatic());
		map.put("VIN", getVIN());
		map.put("price", getPrice());
		map.put("history", getHistory());
		map.put("likes", getLikes());

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
	private String _make;
	private String _model;
	private String _style;
	private int _year;
	private int _mileage;
	private String _color;
	private List<byte[]> _photos = new ArrayList<byte[]>();
	private byte[] _thumbnail;
	private boolean _automatic;
	private String _VIN;
	private double _price;
	private List<RegInfo> _history = new ArrayList<RegInfo>();
	private int _likes;
	private static final long serialVersionUID = 1L;
}