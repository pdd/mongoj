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

package org.mongoj.db;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class DBFactoryImpl implements DBFactory {
	
	public static final String MONGOJ_URI = "mongoj.uri";
	
	public static final String MONGOJ_INDEX = "mongoj.index";

	public static final String MONGOJ_INDEX_BACKGROUND = 
		"mongoj.index.background";
	
	public static final String MONGOJ_PROPERTIES_FILE = "mongoj.properties"; 
	
	public static final String MONGOJ_INDEX_PROPERTIES = 
		"mongoj.index.properties";
	
	public DB getDB() {
		return _db;
	}

	public void setPropertiesFile(String propertiesFileName) {
		try {
			InputStream inStream = this.getClass()
				.getClassLoader()
				.getResourceAsStream(MONGOJ_PROPERTIES_FILE);

			_properties.load(inStream);
			
			if (!StringUtils.isBlank(propertiesFileName)) {
				try {
					//override the base/default properties
					inStream = this.getClass().getResourceAsStream(
						propertiesFileName);
					
					if (inStream == null) {
						inStream = Thread.currentThread()
							.getContextClassLoader()
							.getResourceAsStream(propertiesFileName);
					}
					
					if (inStream != null) {
						_properties.load(inStream);
					}
					else {
						_log.error(
							"Unable to load {}", propertiesFileName);
					}
				}
				catch (IOException e) {
					_log.error(
						"Unable to override default mogoj properties", e);
				}
			}
						
			if (StringUtils.isBlank(_properties.getProperty(MONGOJ_URI))) {
				_log.error("Invalid {}", MONGOJ_URI);
				
				return;
			}
			
			MongoURI mongoURI = 
				new MongoURI(_properties.getProperty(MONGOJ_URI));
						
			_db = mongoURI.connectDB();
			
			_log.info("Successfully connected to {}", mongoURI.getDatabase());
			
			if (Boolean.valueOf(_properties.getProperty(MONGOJ_INDEX))) {					
				inStream = this.getClass()
					.getClassLoader()
					.getResourceAsStream(
						_properties.getProperty(MONGOJ_INDEX_PROPERTIES));
	
				if (inStream == null) {
					_log.error(
						"Indexes not found in {} file...skipping indexing.",
							_properties.getProperty(MONGOJ_INDEX_PROPERTIES));
					
					return;
				}
				
				Properties indexes = new Properties();

				try {

					indexes.load(inStream);
					
					new Indexer(indexes).run();
				}
				catch (IOException e) {
					_log.error("Error loading indexes...skipping indexing.", e);
				}
			}
		}
		catch (MongoException e) {
			_log.error("Error connecting to DB", e);
		}
		catch (UnknownHostException e) {
			_log.error("Error connecting to DB", e);
		}
		catch (IOException e) {
			_log.error("Error loading properties", e);
		}		
	}
	
	public class Indexer implements Runnable {
		
		public Indexer(Properties indexes) {
			_indexes = indexes;
		}
		
		@Override
		public void run() {
			boolean background = Boolean.valueOf(
				_properties.getProperty(MONGOJ_INDEX_BACKGROUND));
			
			if (background) {
				_log.info("Indexing will be run in background");
			}
			else {
				_log.info("Indexing will be run in foreground blocking the DB");
			}

			for (String name : _indexes.stringPropertyNames()) {
				//format collection.0..n or unique.collection.0..n
				String[] tokens = name.split("\\.");

				String valueString = _indexes.getProperty(name);
				
				if (StringUtils.isBlank(valueString)) {
					_log.info(
						"Skipping index generation for {}, keys undefined.",
							name);
					continue;
				}
				
				String[] values = valueString.split(",");
				
				BasicDBObjectBuilder keys = 
					BasicDBObjectBuilder.start();
				
				for (String key : values) {
					keys.add(key, 1);
				}
				
				BasicDBObjectBuilder options = 
					BasicDBObjectBuilder.start();
				
				options.add("background", background);
				
				if (name.startsWith("unique.")) {
					if (tokens.length != 3) {
						_log.info(
							"Skipping index generation for {}, invalid format.",
								name);
						continue;
					}
						
					DBCollection collection = _db.getCollection(tokens[1]);

					options.add("unique", true);

					_log.debug("Indexing collection {}", tokens[1]);
					
					collection.ensureIndex(keys.get(), options.get());
				}
				else {
					if (tokens.length != 2) {
						_log.info(
							"Skipping index generation for {}, invalid format.",
								name);

						continue;
					}
					
					DBCollection collection = _db.getCollection(tokens[0]);
					
					options.add("unique", false);

					_log.debug("Indexing collection {}", tokens[0]);
					
					collection.ensureIndex(keys.get(), options.get());
				}
			}
			_log.info("Index processing done.");			
		}
		
		private Properties _indexes = null;
	}
	
	private Properties _properties = new Properties();

	private DB _db = null;

	private Logger _log = LoggerFactory.getLogger(DBFactoryImpl.class);

}