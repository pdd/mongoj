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

package org.mongoj.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.URL;

/**
 * 
 * @author Prashant Dighe
 *
 */
public class FileUtil {

	public static void mkdirs(String pathName) {
		File file = new File(pathName);

		file.mkdirs();
	}
	
	public static String read(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		BufferedReader bufferedReader = 
			new BufferedReader(new InputStreamReader(is));
		
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line)
				.append("\n");
		}
		
		return sb.toString();
	}
	
	public static String read(File file) throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

		byte[] bytes = new byte[(int)randomAccessFile.length()];

		randomAccessFile.readFully(bytes);

		randomAccessFile.close();
		
		return new String(bytes);
	}
	
	public static void write(File file, String content)
		throws IOException {
	
		byte[] bytes = content.getBytes();
		
		if (file.getParent() != null) {
			mkdirs(file.getParent());
		}
	
		FileOutputStream fos = new FileOutputStream(file);
	
		fos.write(bytes, 0, bytes.length);
	
		fos.close();
	}

}