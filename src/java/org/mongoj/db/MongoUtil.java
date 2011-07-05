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

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class MongoUtil {

		public static final String ID = "_id";
		
		public static final String PUSH = "$push";
		
		public static final String PULL = "$pull";
		
		public static final String ADD_TO_SET = "$addToSet";
		
		public static final String EACH = "$each";
		
		private MongoUtil() {
			try {
				//TODO: read uri from config
				_mongoURI = new MongoURI("mongodb://127.0.0.1:27017/test");
				
				_db = _mongoURI.connectDB();
			}
			catch (MongoException e) {
				_log.error(e.getMessage());
				
				_mongoEnabled = false;
			}
			catch (UnknownHostException e) {
				_log.error(e.getMessage());
				
				_mongoEnabled = false;
			}
		}
		
		public static boolean isMongoEnabled() {
			return _instance._mongoEnabled;
		}
		
		public static DB getMongoDB() {
			return _instance._db;
		}

		private static MongoUtil _instance = new MongoUtil();
			
		private static MongoURI _mongoURI = null; 
			
		private boolean _mongoEnabled = false;
		
		private DB _db = null;
		
		Logger _log = LoggerFactory.getLogger(MongoUtil.class);

}