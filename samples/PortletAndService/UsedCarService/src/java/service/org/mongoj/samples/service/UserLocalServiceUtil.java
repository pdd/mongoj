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

package org.mongoj.samples.service;

/**
 * @author Prashant Dighe
 */
public class UserLocalServiceUtil {
	public static org.mongoj.samples.model.User addUser(
		org.mongoj.samples.model.User user)
		throws org.mongoj.exception.SystemException,
			org.mongoj.exception.UpdateException {
		return getService().addUser(user);
	}

	public static org.mongoj.samples.model.User createUser() {
		return getService().createUser();
	}

	public static void deleteUser(java.lang.String id)
		throws org.mongoj.exception.SystemException,
			org.mongoj.exception.UpdateException {
		getService().deleteUser(id);
	}

	public static void deleteUser(org.mongoj.samples.model.User user)
		throws org.mongoj.exception.SystemException,
			org.mongoj.exception.UpdateException {
		getService().deleteUser(user);
	}

	public static org.mongoj.samples.model.User getUser(java.lang.String id)
		throws org.mongoj.exception.SystemException {
		return getService().getUser(id);
	}

	public static long getUserCount()
		throws org.mongoj.exception.SystemException {
		return getService().getUserCount();
	}

	public static org.mongoj.samples.model.User updateUser(
		org.mongoj.samples.model.User user)
		throws org.mongoj.exception.SystemException,
			org.mongoj.exception.UpdateException {
		return getService().updateUser(user);
	}

	public static org.mongoj.samples.model.User findByUserId(
		java.lang.String userId) throws org.mongoj.exception.SystemException {
		return getService().findByUserId(userId);
	}

	public static void clearService() {
		_service = null;
	}

	public static UserLocalService getService() {
		if (_service == null) {
			throw new NullPointerException("Service is null");
		}

		return _service;
	}

	public void setService(UserLocalService service) {
		_service = service;
	}

	private static UserLocalService _service;
}