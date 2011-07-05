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
public interface CarModel extends BaseModel<Car> {
	public String getId();

	public void setId(String id);

	public String getMake();

	public void setMake(String make);

	public String getModel();

	public void setModel(String model);

	public String getStyle();

	public void setStyle(String style);

	public int getYear();

	public void setYear(int year);

	public int getMileage();

	public void setMileage(int mileage);

	public String getColor();

	public void setColor(String color);

	public List<byte[]> getPhotos();

	public void setPhotos(List<byte[]> photos);

	/**
	* Appends to the end of List, irrespective of value already existing.
	* See addToPhotos to add without duplicating.
	*/
	public void appendToPhotos(byte[] photos);

	/**
	* Appends to the end of List, irrespective of value already existing.
	* See addToPhotos to add without duplicating.
	*/
	public void appendToPhotos(List<byte[]> photos);

	/**
	* Adds to the List by treating List as a Set.
	* Thus adds only if not already exists in the List and avoids duplication.
	* See appendToPhotos to append to List.
	*/
	public void addToPhotos(byte[] photos);

	/**
	* Adds to the List by treating List as a Set.
	* Thus adds only if not already exists in the List and avoids duplication.
	* See appendToPhotos to append to List.
	*/
	public void addToPhotos(List<byte[]> photos);

	/**
	* Removes all occurences of value from the List
	*/
	public void removeFromPhotos(byte[] photos);

	/**
	* Removes all occurences of all values from the List
	*/
	public void removeFromPhotos(List<byte[]> photos);

	public byte[] getThumbnail();

	public void setThumbnail(byte[] thumbnail);

	public boolean getAutomatic();

	public void setAutomatic(boolean automatic);

	public String getVIN();

	public void setVIN(String VIN);

	public double getPrice();

	public void setPrice(double price);

	public List<RegInfo> getHistory();

	public void setHistory(List<RegInfo> history);

	public static interface RegInfo extends Serializable {
		public Date getRegDate();

		public void setRegDate(Date regDate);

		public int getMileage();

		public void setMileage(int mileage);

		public String getState();

		public void setState(String state);

		public List<ServiceDetails> getServiceRecord();

		public void setServiceRecord(List<ServiceDetails> serviceRecord);

		public static interface ServiceDetails extends Serializable {
			public Date getServiceDate();

			public void setServiceDate(Date serviceDate);

			public String getNotes();

			public void setNotes(String notes);

			public Map<String, Object> toMap();
		}

		public Map<String, Object> toMap();
	}

	/**
	* Appends to the end of List, irrespective of value already existing.
	* See addToHistory to add without duplicating.
	*/
	public void appendToHistory(RegInfo history);

	/**
	* Appends to the end of List, irrespective of value already existing.
	* See addToHistory to add without duplicating.
	*/
	public void appendToHistory(List<RegInfo> history);

	/**
	* Adds to the List by treating List as a Set.
	* Thus adds only if not already exists in the List and avoids duplication.
	* See appendToHistory to append to List.
	*/
	public void addToHistory(RegInfo history);

	/**
	* Adds to the List by treating List as a Set.
	* Thus adds only if not already exists in the List and avoids duplication.
	* See appendToHistory to append to List.
	*/
	public void addToHistory(List<RegInfo> history);

	/**
	* Removes all occurences of value from the List
	*/
	public void removeFromHistory(RegInfo history);

	/**
	* Removes all occurences of all values from the List
	*/
	public void removeFromHistory(List<RegInfo> history);

	public int getLikes();

	public void setLikes(int likes);
}