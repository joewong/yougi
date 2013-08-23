/* Yougi is a web application conceived to manage user groups or
 * communities focused on a certain domain of knowledge, whose members are
 * constantly sharing information and participating in social and educational
 * events. Copyright (C) 2011 Hildeberto Mendonça.
 *
 * This application is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This application is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * There is a full copy of the GNU Lesser General Public License along with
 * this library. Look for the file license.txt at the root level. If you do not
 * find it, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 * */

package org.cejug.yougi.web.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.cejug.yougi.entity.UserAccount;
import javax.faces.context.FacesContext;
import org.cejug.yougi.util.*;
import java.util.logging.*;
import org.cejug.yougi.business.UserAccountBean;
import org.cejug.yougi.entity.UserAccount;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.cejug.yougi.business.LoginFunctionsMBean;

/**
 * @author Joe Wong - joe-wong.co.uk
 */

@ManagedBean
@RequestScoped
public class LoginMBean {

	@EJB
    private LoginFunctionsMBean loginFunctionsMBean;
	
	@ManagedProperty(value="#{param.j_username}")
    private String username;
	
	@ManagedProperty(value="#{param.j_password}")
    private String password;
	
	static final Logger LOGGER = Logger.getLogger(LoginMBean.class.getName());
	 
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	/**
	 * login method
	 * 
	 * Redirects to the index page
	 */
	public String login()  {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		
		loginFunctionsMBean.login(request, username, password);
		
		return "index";
	}
}
