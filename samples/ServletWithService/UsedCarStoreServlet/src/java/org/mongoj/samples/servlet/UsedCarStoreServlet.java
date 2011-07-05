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

package org.mongoj.samples.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.mongoj.exception.SystemException;
import org.mongoj.exception.UpdateException;
import org.mongoj.samples.model.User;
import org.mongoj.samples.service.UserLocalServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UsedCarStoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UsedCarStoreServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		String path = request.getPathInfo();
		
		RequestDispatcher requestDispatcher = null; 
		
		if (path.equals("/login")) {
			HttpSession session = request.getSession();
			
			if (isUserAuthenticated(request)) {
				response.sendRedirect(
					request.getContextPath() + "/store/view");
				
				return;
			}
			
			String userName = request.getParameter("user");
			String password = request.getParameter("password");
			
			if (!StringUtils.isBlank(userName) && 
				!StringUtils.isBlank(password)) {
				
				boolean success = _authenticate(
					request, userName.trim(), password.trim());

				if (success == true) {
					response.sendRedirect(
						request.getContextPath() + "/store/view");
				
					return;
				}
				else {
					request.setAttribute("message", "Authentication failure");
					
					requestDispatcher =
						request.getRequestDispatcher("/login.jsp");
					
					requestDispatcher.forward(request, response);
					
					return;
				}
			}
			else {
				requestDispatcher =
					request.getRequestDispatcher("/login.jsp");
				
				requestDispatcher.forward(request, response);
				
				return;
			}
		}
		else if (path.equals("/view")) {
			HttpSession session = request.getSession();
			
			if (isUserAuthenticated(request)) {
				requestDispatcher =
					request.getRequestDispatcher("/WEB-INF/jsp/view.jsp");
				
				requestDispatcher.forward(request, response);
				
				return;
			}
			else {
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				
				return;
			}
		}
		else if (path.equals("/logout")) {
			request.getSession().removeAttribute("user_name");
			
		}
	
		response.sendRedirect(request.getContextPath() + "/login.jsp");	
	}
	
	protected boolean isUserAuthenticated(HttpServletRequest request) {
		if (request.getSession().getAttribute("user_name") != null) {
			return true;
		}
		
		return false;
	}

	private boolean _authenticate(HttpServletRequest request,
		String userName, String password) {
		boolean result = false;
		
		try {
			User user = UserLocalServiceUtil.findByUserId(userName);
			
			if (user == null) {
				user = UserLocalServiceUtil.createUser();
				
				user.setUserId(userName);
				user.setPassword(password);
				user.setFirstName(userName);
				
				try {
					UserLocalServiceUtil.addUser(user);
				}
				catch (UpdateException e) {
					_log.error(e.getMessage(), e);
					
					return false;
				}
			}
			
			if (password.equals(user.getPassword())) {
				result = true;
		
				request.getSession().setAttribute("user_name", userName);
			}
		}
		catch (SystemException e) {
			_log.error(e.getMessage(), e);
		}
		
		return result;
	}
	
	private static Logger _log = 
		LoggerFactory.getLogger(UsedCarStoreServlet.class);
}
