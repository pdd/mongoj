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

package org.mongoj.samples.portlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.jni.File;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.POJONode;
import org.mongoj.exception.SystemException;
import org.mongoj.exception.UpdateException;
import org.mongoj.model.BaseModel;
import org.mongoj.samples.model.Car;
import org.mongoj.samples.model.User;
import org.mongoj.samples.model.impl.CarImpl;
import org.mongoj.samples.service.CarLocalServiceUtil;
import org.mongoj.samples.service.UserLocalServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class UsedCarStorePortlet extends GenericPortlet {

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
		throws PortletException, IOException {
		response.setContentType("text/html");

		PortletRequestDispatcher dispatcher = getPortletContext()
			.getRequestDispatcher("/WEB-INF/jsp/view.jsp");

		dispatcher.include(request, response);
	}

	@Override
	public void serveResource(ResourceRequest request, 
		ResourceResponse response) throws PortletException, IOException {
		try {
			String action = ParamUtil.getString(request, "action");
			
			if (action.equals("getCar")) {
				JSONObject data = new JSONObject();
				data.put("car", getCar(request, response));

				JSONObject jsonResponse = new JSONObject();
				
				jsonResponse.put("data", data);
				jsonResponse.put("status", "success");

				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else if (action.equals("saveCar")) {	
				JSONObject jsonResponse = new JSONObject();

				try {
					saveCar(request, response);
					
					jsonResponse.put("status", "success");
				}
				catch (UpdateException e) {
					_log.error(e.getMessage(), e);
					
					jsonResponse.put("status", "error");
				}
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else if (action.equals("deleteCar")) {
				String vin = ParamUtil.getString(request, "vin");
				
				Car car = CarLocalServiceUtil.findByVIN(vin);
				
				if (car == null) {
					_log.debug("Can not find car with vin = {}", vin);
					
					return;
				}
				
				CarLocalServiceUtil.deleteCar(car);
				
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("status", "success");
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else if (action.equals("upload-thumbnail")) { 
				boolean success = uploadThumbnail(request, response);

				// This response object is different because the file upload
				// plugin needs the response like this otherwise it fails
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("success", success);
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else if (action.equals("upload-photo")) { 
				boolean success = uploadPhoto(request, response);
				
				// This response object is different because the file upload
				// plugin needs the response like this otherwise it fails
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("success", success);
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else if (action.equals("store")) { 
				JSONObject jsonResponse = new JSONObject();
				JSONObject data = new JSONObject();

				data.put("cars", getCars(request, response));
				
				jsonResponse.put("data", data);
				jsonResponse.put("status", "success");
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else if (action.equals("getProfile")) {
				JSONObject jsonResponse = new JSONObject();
				JSONObject data = new JSONObject();

				com.liferay.portal.model.User portalUser = 
					PortalUtil.getUser(request);
				
				if (portalUser == null) {
					_log.error("portalUser is null");
					
					jsonResponse.put("status", "error");
					
					response.getWriter().write(jsonResponse.toString());
					
					return;
				}
							
				User user = UserLocalServiceUtil.findByUserId(
					portalUser.getUserId());
				
				JSONObject jsonUser = new JSONObject(user.toMap());
				
				data.put("user", jsonUser);
				data.put("likedCarImages", getLikedCarImages(user));
				
				jsonResponse.put("data", data);
				jsonResponse.put("status", "success");
				
				response.getWriter().write(jsonResponse.toString());
				
				return;				
			}
			else if (action.equals("saveUser")) {
				String firstName = ParamUtil.getString(request, "firstName");
				String lastName = ParamUtil.getString(request, "lastName");
				
				JSONObject jsonResponse = new JSONObject();
				
				com.liferay.portal.model.User portalUser =
					PortalUtil.getUser(request);
				
				if (portalUser == null) {
					_log.error("portalUser is null");
					
					jsonResponse.put("status", "error");
					
					response.getWriter().write(jsonResponse.toString());
					
					return;
				}
				
				User user = 
					UserLocalServiceUtil.findByUserId(portalUser.getUserId());
				
				if (user == null) {
					user = UserLocalServiceUtil.createUser();
					user.setUserId(portalUser.getUserId());
				}
				
				user.setFirstName(firstName);
				user.setLastName(lastName);
				
				UserLocalServiceUtil.updateUser(user);

				jsonResponse.put("status", "success");
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else if (action.equals("likeCar")) {
				JSONObject jsonResponse = new JSONObject();
				
				String vin = ParamUtil.getString(request, "vin");
				
				com.liferay.portal.model.User portalUser =
					PortalUtil.getUser(request);
				
				if (portalUser == null) {
					_log.error("portalUser is null");
					
					jsonResponse.put("status", "error");
					
					response.getWriter().write(jsonResponse.toString());
					
					return;
				}
				
				User user = 
					UserLocalServiceUtil.findByUserId(portalUser.getUserId());
				
				if (user == null) {
					user = UserLocalServiceUtil.createUser();
					user.setUserId(portalUser.getUserId());
				}
				
				user.addToLikedCars(vin);
				
				UserLocalServiceUtil.updateUser(user);

				jsonResponse.put("status", "success");
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else if (action.equals("unlikeCar")) {
				JSONObject jsonResponse = new JSONObject();
				
				String vin = ParamUtil.getString(request, "vin");
				
				com.liferay.portal.model.User portalUser =
					PortalUtil.getUser(request);
				
				if (portalUser == null) {
					_log.error("portalUser is null");
					
					jsonResponse.put("status", "error");
					
					response.getWriter().write(jsonResponse.toString());
					
					return;
				}
				
				User user = 
					UserLocalServiceUtil.findByUserId(portalUser.getUserId());
				
				user.removeFromLikedCars(vin);
				
				UserLocalServiceUtil.updateUser(user);

				jsonResponse.put("status", "success");
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
			else {
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("status", "error");
				
				response.getWriter().write(jsonResponse.toString());
				
				return;
			}
		}
		catch (Exception e) {
			// this catch all block is not such a good idea but exists to 
			// keep the example easy

			_log.error(e.getMessage(), e);
			
			throw new PortletException(e);
		}
	}

	protected JSONObject getLikedCarImages(User user) 
		throws Exception {
		JSONObject likedCarImages = new JSONObject();
		
		List<String> likedCars = user.getLikedCars();
		
		if (likedCars == null || likedCars.isEmpty()) {
			return likedCarImages;
		}
		
		for (String vin : likedCars) {
			// encode Base64 to be able to send image binaries as json

			Car car = CarLocalServiceUtil.findByVIN(vin);
			
			byte[] thumbnail = car.getThumbnail();
			
			if (thumbnail != null && thumbnail.length != 0) {
				byte[] img = Base64.encodeBase64(thumbnail);
				
				likedCarImages.put(vin, img);
			}
		}
		
		return likedCarImages;
	}
	
	protected JSONObject getCar(ResourceRequest request, 
		ResourceResponse response) throws Exception {
		String  vin = ParamUtil.getString(request, "vin");
		
		Car car = CarLocalServiceUtil.findByVIN(vin);
	
		JSONObject jsonResponse = new JSONObject();
		
		JSONObject data = new JSONObject();
		
		car = (car == null) ? CarLocalServiceUtil.createCar() : car;
	
		JSONObject jsonCar = new JSONObject(car.toMap());
			
		return jsonCar;
	}
	
	protected JSONArray getCars(ResourceRequest request, 
		ResourceResponse response) throws Exception {
		List<Car> cars = CarLocalServiceUtil.findAll();
		
		JSONArray carsArray = new JSONArray();
		
		for (Car car : cars) {
			// encode Base64 to be able to send image binaries as json
			
			byte[] thumbnail = car.getThumbnail();
			
			if (thumbnail != null && thumbnail.length != 0) {
				byte[] img = Base64.encodeBase64(thumbnail);
				
				car.setThumbnail(img);
			}
			
			List<byte[]> photos = new ArrayList<byte[]>();
			
			for (byte[] photo : car.getPhotos()) {
				if (photo != null && photo.length != 0) {
					photo = Base64.encodeBase64(photo);
					
					photos.add(photo);
				}
			}
			
			car.setPhotos(photos);
			
			carsArray.put(car.toMap());
		}
		
		return carsArray;
	}
	
	protected void saveCar(ResourceRequest request, 
		ResourceResponse response) throws Exception {
		String  vin = ParamUtil.getString(request, "vin");
		String  make = ParamUtil.getString(request, "make");
		String  model = ParamUtil.getString(request, "model");
		String  style = ParamUtil.getString(request, "style");
		int  year = ParamUtil.getInteger(request, "year");
		int  mileage = ParamUtil.getInteger(request, "mileage");
		String  color = ParamUtil.getString(request, "color");
		Boolean automatic  = ParamUtil.getBoolean(request, "automatic");
		Double price  = ParamUtil.getDouble(request, "price");
		String history = ParamUtil.getString(request, "history");
		
		Car car = null;
		try {
			car = CarLocalServiceUtil.findByVIN(vin);
			
			_log.debug("Found car with vin = {}", vin);
		}
		catch (SystemException e) {
			_log.error(e.getMessage(), e);
			
			throw new PortletException(e);
		}
		
		if (car == null) {
			_log.debug(
				"Can not find car with vin = {}, creating new.", vin);
			
			car = CarLocalServiceUtil.createCar();
		}
		
		car.setVIN(vin);
		car.setMake(make);
		car.setModel(model);
		car.setStyle(style);
		car.setYear(year);
		car.setMileage(mileage);
		car.setColor(color);
		car.setAutomatic(automatic);
		car.setPrice(price);
					
		car.setHistory(getRegistrartionInfo(history));
		
		CarLocalServiceUtil.updateCar(car);
	}
	
	protected boolean uploadThumbnail(ResourceRequest request, 
		ResourceResponse response) throws Exception {
		String vin = ParamUtil.getString(request, "vin");
		
		int length = request.getContentLength();
	
		BufferedInputStream bis = 
			new BufferedInputStream(request.getPortletInputStream());
		
		int i = 0;
		int offset = 0;
		int n = 100;
		
		byte[] bytes = new byte[length + n];

		while((i = bis.read(bytes, offset, n)) != -1) {
			offset += i;
			
			if (offset + n > bytes.length) {
				return false;
			}
		}
	
		Car car = CarLocalServiceUtil.findByVIN(vin);
		
		if (car == null) {
			_log.debug(
				"Can not find car with vin = {}, creating new.", vin);
			
			car = CarLocalServiceUtil.createCar();
		}
		
		car.setVIN(vin);
		car.setThumbnail(bytes);
		
		CarLocalServiceUtil.updateCar(car);
		
		return true;
	}
	
	public boolean uploadPhoto(ResourceRequest request, 
		ResourceResponse response) throws Exception {
		String vin = ParamUtil.getString(request, "vin");
		
		int length = request.getContentLength();

		BufferedInputStream bis = 
			new BufferedInputStream(request.getPortletInputStream());
		
		int i = 0;
		int offset = 0;
		int n = 100;
		
		byte[] bytes = new byte[length + n];
				
		while((i = bis.read(bytes, offset, n)) != -1) {
			offset += i;
			
			if (offset + n > bytes.length) {
				return false;
			}
		}

		Car car = CarLocalServiceUtil.findByVIN(vin);
		
		if (car == null) {
			_log.debug(
				"Can not find car with vin = {}, creating new.", vin);
			
			car = CarLocalServiceUtil.createCar();
		}
		
		car.setVIN(vin);
		car.appendToPhotos(bytes);
		
		CarLocalServiceUtil.updateCar(car);
		
		return true;
	}
	
	/*
	 * This method can go into CarImpl as a custom helper which reads
	 * registration history json string and builds the List<RegInfo> object.
	 * 
	 * There are better and easier ways this could have been done, like have 
	 * another form etc. In reality, this would need to be dynamic form with
	 * user being able to add as many past registrations as possible. But
	 * anyhow, this is good enough to demonstrate arbitrary nesting of 
	 * JSON objects and arrays.  
	 */
	protected List<Car.RegInfo> getRegistrartionInfo(String regHistory) 
		throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		List<Car.RegInfo> regInfoList = new ArrayList<Car.RegInfo>();

		ArrayNode jsonArray = mapper.readValue(regHistory, ArrayNode.class);
		
		Iterator<JsonNode> iterator = jsonArray.iterator();
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		while (iterator.hasNext()) {
			JsonNode jsonObject = iterator.next();
			
			Car.RegInfo regInfo = new CarImpl.RegInfoImpl();
			
			String d = jsonObject.get("regDate").getTextValue();
			
			Date regDate = null;
			
			try {
				regDate = dateFormat.parse(d);
			}
			catch (ParseException e) {
				_log.error(
					"Registration date could not be parsed...using today");
				
				regDate = new Date();
			}
			
			int regMileage = jsonObject.get("mileage").getIntValue();
			
			String state = jsonObject.get("state").getTextValue();
				
			List<Car.RegInfo.ServiceDetails> serviceDetailsList =
				new ArrayList<Car.RegInfo.ServiceDetails>();
			
			ArrayNode svcRecords = 
				(ArrayNode)jsonObject.get("serviceRecord");
			
			Iterator<JsonNode> sIterator = svcRecords.iterator();
			
			while (sIterator.hasNext()) {
				Car.RegInfo.ServiceDetails serviceDetails = 
					new CarImpl.RegInfoImpl.ServiceDetailsImpl();
				
				JsonNode svcRec = sIterator.next();
				
				d = svcRec.get("serviceDate").getTextValue();
				
				Date serviceDate = null;
				try {
					serviceDate = dateFormat.parse(d);
				}
				catch (ParseException e) {
					_log.error("Service date could not be parsed..." +
						"using today");
					
					serviceDate = new Date();
				}
				
				String notes = svcRec.get("notes").getTextValue();
				
				serviceDetails.setServiceDate(serviceDate);
				serviceDetails.setNotes(notes);
				
				serviceDetailsList.add(serviceDetails);
			}
			
			regInfo.setRegDate(regDate);
			regInfo.setMileage(regMileage);
			regInfo.setState(state);
			regInfo.setServiceRecord(serviceDetailsList);
			
			regInfoList.add(regInfo);
		}
		
		return regInfoList;
	}
	
	private Logger _log = LoggerFactory.getLogger(UsedCarStorePortlet.class);
}
