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

package org.mongoj.util;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class ConversionUtil {

	public static final String STRING_BLANK = "";
	
	public static String getString(Object object) {
		if (object == null) {
			return STRING_BLANK;
		}
		
		return object.toString();		
	}
	
	public static boolean isBlank(String string) {
		return StringUtils.isBlank(string);
	}
	
	public static boolean getboolean(Object object) {
		if (object == null) {
			return false;
		}
		
		if (object instanceof Boolean) {
			return (Boolean)object;
		}
		else {
			return false;
		}
	}
	
	public static boolean getboolean(String boolValue) {
		if (boolValue == null) {
			return false;
		}
		
		return Boolean.parseBoolean(boolValue);
	}
	
	public static Boolean getBoolean(String boolValue) {
		if (boolValue == null) {
			return Boolean.FALSE;
		}
		
		return Boolean.valueOf(boolValue);
	}
	
	public static int getint(Object object) {
		if (object == null) {
			return 0;
		}
		
		if (object instanceof Integer) {
			return (Integer)object;
		}
		else {
			return 0;
		}
	}
	
	public static int getint(String intValue) {
		if (intValue == null) {
			return 0;
		}
		
		return Integer.parseInt(intValue);
	}
	
	public static int getInt(String intValue) {
		if (intValue == null) {
			return 0;
		}
		
		return Integer.valueOf(intValue);
	}
		
	public static long getlong(Object object) {
		if (object == null) {
			return 0L;
		}
		
		if (object instanceof Long) {
			return (Long)object;
		}
		else {
			return 0L;
		}
	}
	
	public static long getlong(String longValue) {
		if (longValue == null) {
			return 0L;
		}
		
		return Long.parseLong(longValue);
	}
	
	public static Long getLong(String longValue) {
		if (longValue == null) {
			return 0L;
		}
		
		return Long.valueOf(longValue);
	}

	public static double getdouble(Object object) {
		if (object == null) {
			return 0.0;
		}
		
		if (object instanceof Double) {
			return (Double)object;
		}
		else {
			return 0.0;
		}
	}
	
	public static double getdouble(String doubleValue) {
		if (doubleValue == null) {
			return 0.0;
		}
		
		return Double.parseDouble(doubleValue);
	}
	
	public static Double getDouble(String doubleValue) {
		if (doubleValue == null) {
			return 0.0;
		}
		
		return Double.valueOf(doubleValue);
	}
	
}