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

package org.cejug.yougi.business;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
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
import org.cejug.yougi.entity.Authentication;

/**
 * @author Joe Wong - joe-wong.co.uk
 */

@Stateless
@LocalBean
public class LoginFunctionsMBean {

    @PersistenceContext
    private EntityManager em, em2;
	
	@EJB
    private UserAccountBean userAccountBean;

	static final Logger LOGGER = Logger.getLogger(LoginFunctionsMBean.class.getName());
	 
	/**
	 * login
	 * 
	 * Encrypts the username twice before authenticating. 
	 */
	public void login(HttpServletRequest request, String username, String password)  {
		
		boolean encrypt_username = true;
		
		// Find and encrypt account if it hasn't been done already.
		try
		{
			UserAccount userAccount = userAccountBean.findUserAccountByEmail(username);
			Authentication auth 	= userAccountBean.findAuthenticationUser(userAccount.getId());
			
			// username is not encrypted
	    	if(userAccount != null && auth != null && userAccount.getEmailIsEncrypted() == false)
	    	{
	    		auth.setUsername(AESencrp.encrypt(username));
	    		userAccount.setEmail(AESencrp.encrypt(username));
	    		userAccount.setEmailIsEncrypted(true);
	    		
	    		em.merge(userAccount);
	    		em.flush();
	    		
	    		em2.merge(auth);
	    		em.flush();
	    		
	    		encrypt_username = false;
			}
		}
		catch(Exception e)
		{
			LOGGER.log(Level.INFO, "COULD NOT ENCRYPT USERNAME (" + username + ") DUE TO AN ERROR.");
		}
		
		// login to the application
	    try
	    {
	    	String encrypted_username = username;
	    	
	    	if(encrypt_username == true)
	    	{
	    		encrypted_username = AESencrp.encrypt(username);
	    	}
	    	
	    	request.login(encrypted_username, password);
	    }
	    catch(Exception e)
	    {
	    	LOGGER.log(Level.INFO, "Unable to login: " + e);
	    }
	}
}
