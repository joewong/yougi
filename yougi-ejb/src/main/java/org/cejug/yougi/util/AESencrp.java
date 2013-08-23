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
package org.cejug.yougi.util;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.*;
import sun.misc.*;

/**
 * This class provides both encryption and decryption methods.
 *
 * @author Joseph Wong - http://www.joe-wong.co.uk
 * 
 * This class is based on the Encryption and Decryption
 * code 2 learn article: http://www.code2learn.com/2011/06/encryption-and-decryption-of-data-using.html?m=1
 */
public class AESencrp {
    
    private static final String ALGO = "AES";
    private static final byte[] keyValue = 
    new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y'};
    
    /**
     * encrypt
     * 
     * Only encrypt a field if it is not encrypted.
     */
    public static String encrypt(String Data) throws Exception  {
    	String encryptedValue = ""; 
    	
    	if(Data.length() > 0)
    	{
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encVal = c.doFinal(Data.getBytes());
	        encryptedValue = new BASE64Encoder().encode(encVal);
    	}
    	
        return encryptedValue;
    }
    
    public static String decrypt(String encryptedData) throws Exception {
    	String decryptedValue = "";
    	
    	if(encryptedData.length() > 0){
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.DECRYPT_MODE, key);
	        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
	        byte[] decValue = c.doFinal(decordedValue);
	        decryptedValue = new String(decValue);
    	}else{
    		decryptedValue = "";
    	}
    	
        return decryptedValue;
    }
    
    private static Key generateKey() {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

}