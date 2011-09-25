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

package org.mongoj.samples.model.impl;

import org.mongoj.model.impl.BaseModelImpl;

import org.mongoj.samples.model.Car;
import org.mongoj.samples.model.CarModel;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
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

		List<RegInfo> objectList = new ArrayList<RegInfo>();

		List<Map<String, Object>> mapList = (List<Map<String, Object>>)map.get(
				"history");

		if (mapList != null) {
			for (Map<String, Object> object : mapList) {
				objectList.add(new RegInfoImpl(object));
			}
		}

		_history = objectList;
		_likes = (Integer)map.get("likes");
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

		updateAppendMap("photos", photos);
	}

	public void appendToPhotos(List<byte[]> photos) {
		_photos.addAll(photos);

		updateAppendMap("photos", photos);
	}

	public void addToPhotos(byte[] photos) {
		if (!_photos.contains(photos)) {
			_photos.add(photos);

			updateAddMap("photos", photos);
		}
	}

	public void addToPhotos(List<byte[]> photos) {
		for (byte[] object : photos) {
			if (!_photos.contains(object)) {
				_photos.add(object);

				updateAddMap("photos", object);
			}
		}
	}

	public void removeFromPhotos(byte[] photos) {
		while (_photos.remove(photos))
			;

		updateRemoveMap("photos", photos);
	}

	public void removeFromPhotos(List<byte[]> photos) {
		_photos.removeAll(photos);

		updateRemoveMap("photos", photos);
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

		setMap.put("history", objectListToMapList(history));
	}

	public static class RegInfoImpl implements RegInfo {
		public final Object[][] FIELDS = {
				{ "regDate", "Date" },
				{ "mileage", "int" },
				{ "state", "String" },
				{ "serviceRecord", "List<ServiceDetails>" }
			};

		public RegInfoImpl() {
		}

		public RegInfoImpl(Map<String, Object> map) {
			_regDate = (Date)map.get("regDate");
			_mileage = (Integer)map.get("mileage");
			_state = (String)map.get("state");

			List<ServiceDetails> objectList = new ArrayList<ServiceDetails>();

			List<Map<String, Object>> mapList = (List<Map<String, Object>>)map.get(
					"serviceRecord");

			if (mapList != null) {
				for (Map<String, Object> object : mapList) {
					objectList.add(new ServiceDetailsImpl(object));
				}
			}

			_serviceRecord = objectList;
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

		public List<ServiceDetails> getServiceRecord() {
			return _serviceRecord;
		}

		public void setServiceRecord(List<ServiceDetails> serviceRecord) {
			_serviceRecord = serviceRecord;
		}

		public static class ServiceDetailsImpl implements ServiceDetails {
			public final Object[][] FIELDS = {
					{ "serviceDate", "Date" },
					{ "notes", "String" }
				};

			public ServiceDetailsImpl() {
			}

			public ServiceDetailsImpl(Map<String, Object> map) {
				_serviceDate = (Date)map.get("serviceDate");
				_notes = (String)map.get("notes");
			}

			public Date getServiceDate() {
				return _serviceDate;
			}

			public void setServiceDate(Date serviceDate) {
				_serviceDate = serviceDate;
			}

			public String getNotes() {
				return _notes;
			}

			public void setNotes(String notes) {
				_notes = notes;
			}

			public Map<String, Object> toMap() {
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("serviceDate", getServiceDate());
				map.put("notes", getNotes());

				return map;
			}

			private Date _serviceDate;
			private String _notes;
		}

		public Map<String, Object> toMap() {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("regDate", getRegDate());
			map.put("mileage", getMileage());
			map.put("state", getState());
			map.put("serviceRecord", objectListToMapList(getServiceRecord()));

			return map;
		}

		private Date _regDate;
		private int _mileage;
		private String _state;
		private List<ServiceDetails> _serviceRecord = new ArrayList<ServiceDetails>();
	}

	public void appendToHistory(RegInfo history) {
		_history.add(history);

		updateAppendMap("history", history.toMap());
	}

	public void appendToHistory(List<RegInfo> history) {
		_history.addAll(history);

		updateAppendMap("history", objectListToMapList(history));
	}

	public void addToHistory(RegInfo history) {
		if (!_history.contains(history)) {
			_history.add(history);

			updateAddMap("history", history.toMap());
		}
	}

	public void addToHistory(List<RegInfo> history) {
		for (RegInfo object : history) {
			if (!_history.contains(object)) {
				_history.add(object);

				updateAddMap("history", object.toMap());
			}
		}
	}

	public void removeFromHistory(RegInfo history) {
		while (_history.remove(history))
			;

		updateRemoveMap("history", history.toMap());
	}

	public void removeFromHistory(List<RegInfo> history) {
		_history.removeAll(history);

		updateRemoveMap("history", objectListToMapList(history));
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
		map.put("history", objectListToMapList(getHistory()));
		map.put("likes", getLikes());

		return map;
	}

	public String toString() {
		return toMap().toString();
	}

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