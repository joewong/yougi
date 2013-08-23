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
package org.cejug.yougi.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import org.cejug.yougi.util.*;

/**
 * Represents the user account.
 *
 * @author Hildeberto Mendonca - http://www.hildeberto.com
 */
@Entity
@Table(name="user_account")
public class UserAccount implements Serializable, Identified {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name="last_name", nullable=false)
    private String lastName;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable=false)
    private Gender gender;

    private String email;

    @Transient
    private String emailConfirmation;

    @Column(name="unverified_email")
    private String unverifiedEmail;

    @Column(name="confirmation_code")
    private String confirmationCode;

    @Column(name="email_is_encrypted")
    private Boolean emailIsEncrypted;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="registration_date")
    private Date registrationDate;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="last_update")
    private Date lastUpdate;

    private Boolean deactivated = false;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="deactivation_date")
    private Date deactivationDate;

    @Column(name="deactivation_reason")
    private String deactivationReason;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="deactivation_type")
    private DeactivationType deactivationType;

    private String website;

    private String twitter;

    @ManyToOne
    @JoinColumn(name="country")
    private Country country;

    @ManyToOne
    @JoinColumn(name="province")
    private Province province;

    @ManyToOne
    @JoinColumn(name="city")
    private City city;

    @Column(name="timezone")
    private String timeZone;

    @Column(name = "public_profile")
    private Boolean publicProfile;

    @Column(name = "mailing_list")
    private Boolean mailingList;

    private Boolean news;

    @Column(name="general_offer")
    private Boolean generalOffer;

    @Column(name = "job_offer")
    private Boolean jobOffer;

    private Boolean event;

    private Boolean sponsor;

    private Boolean speaker;

    private Boolean verified = false;

    public UserAccount() {}

    public UserAccount(String id) {
        this.id = id;
    }

    public UserAccount(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        try{
        	this.email = AESencrp.encrypt(email);
        }catch(Exception e){
        	this.email = email;
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setEmailIsEncrypted(Boolean encrypted) {
        this.emailIsEncrypted = encrypted;
    }
    
    public void setEmail(String email){
    	this.email = email;
    }
    
    public Boolean getEmailIsEncrypted(){
    	return this.emailIsEncrypted;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = TextUtils.INSTANCE.capitalizeFirstCharWords(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = TextUtils.INSTANCE.capitalizeFirstCharWords(lastName);
    }

    public String getFullName() {
        StringBuilder str = new StringBuilder();
        str.append(firstName);
        str.append(" ");
        str.append(lastName);
        return str.toString();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the email address of the user. Despite its validity, do not use
     * the returned value to send email messages to the user. Use getPostingEmail() instead.
     * @see #getPostingEmail()
     */
    public String getEmail() {
    	String email_str = email;
    	try{
    		 email_str = AESencrp.decrypt(email);
    	}catch(Exception e){}
    	return email_str;
    }

    /**
     * @return the unverifiedEmail is not null when the user's email is not
     * confirmed yet. Once the email is confirmed this method returns null.
     */
    public String getUnverifiedEmail() {
        return unverifiedEmail;
    }

    public void setUnverifiedEmail(String unverifiedEmail) {
        if(unverifiedEmail != null) {
        	try{
        	this.unverifiedEmail = AESencrp.encrypt(unverifiedEmail.toLowerCase());
        	}catch(Exception e){}
        }
        else {
            this.unverifiedEmail = null;
        }
    }

    public void setEmailAsVerified() {
        this.email = this.unverifiedEmail;
        this.unverifiedEmail = null;
    }

    /**
     * @return Independent of the verification of the email, this method returns
     * the available email address for posting email messages.
     */
    public String getPostingEmail() {
    	String email_local = "";
    	try{
	        // In case there is an unverified email, it has the priority to be in
	        // the message recipient.
	        if(this.unverifiedEmail != null && !this.unverifiedEmail.isEmpty()) {
	        		return AESencrp.decrypt(this.unverifiedEmail);
	        }
	        // If unverified email is null it means that the email is valid and it
	        // can be used in the message recipient.
	        else {
	            return AESencrp.decrypt(this.email);
	        }
    	}catch(Exception e){}
    	return email_local;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getDeactivated() {
        if(deactivated != null) {
            return deactivated;
        }
        else {
            return false;
        }
    }

    public void setDeactivated(Boolean deactivated) {
        this.deactivated = deactivated;
    }

    public Date getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Date deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public String getDeactivationReason() {
        return deactivationReason;
    }

    public void setDeactivationReason(String deactivationReason) {
        this.deactivationReason = deactivationReason;
    }

    public DeactivationType getDeactivationType() {
        return deactivationType;
    }

    public void setDeactivationType(DeactivationType deactivationType) {
        this.deactivationType = deactivationType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        if(website == null || website.trim().isEmpty()) {
            this.website = null;
        }
        else if(website.contains("http://")) {
            this.website = website.replace("http://", "");
        }
        else if(website.contains("https://")) {
            this.website = website.replace("https://", "");
        }
        else {
            this.website = website;
        }
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        if(twitter == null || twitter.trim().isEmpty()) {
            this.twitter = null;
        }
        else if(twitter.contains("@")) {
            this.twitter = twitter.replace("@", "");
        }
        else {
            this.twitter = twitter;
        }
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    /**
     * @return the timezone from where the user is located. It is automatically
     * set based on the city where the user is located.
     */
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Boolean getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Boolean speaker) {
        this.speaker = speaker;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getPublicProfile() {
        return publicProfile;
    }

    public void setPublicProfile(Boolean publicProfile) {
        this.publicProfile = publicProfile;
    }

    public Boolean getMailingList() {
        return mailingList;
    }

    public void setMailingList(Boolean mailingList) {
        this.mailingList = mailingList;
    }

    public Boolean getNews() {
        return news;
    }

    public void setNews(Boolean news) {
        this.news = news;
    }

    public Boolean getGeneralOffer() {
        return generalOffer;
    }

    public void setGeneralOffer(Boolean generalOffer) {
        this.generalOffer = generalOffer;
    }

    public Boolean getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(Boolean jobOffer) {
        this.jobOffer = jobOffer;
    }

    public Boolean getEvent() {
        return event;
    }

    public void setEvent(Boolean event) {
        this.event = event;
    }

    public Boolean getSponsor() {
        return sponsor;
    }

    public void setSponsor(Boolean sponsor) {
        this.sponsor = sponsor;
    }

    public String getEmailConfirmation() {
        return emailConfirmation;
    }

    public void setEmailConfirmation(String emailConfirmation) {
        this.emailConfirmation = emailConfirmation.toLowerCase();
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    /**
     * Defines a confirmation code to the user. It usually happens when the user
     * fill in the registration form and the email address needs to be confirmed
     * or when (s)he needs to change the password.
     */
    public void defineNewConfirmationCode() {
        UUID uuid = UUID.randomUUID();
        this.confirmationCode = uuid.toString().replaceAll("-", "");
    }

    /**
     * Set the confirmation code as null. Should be called when the confirmation
     * code is confirmed and can be discarded.
     */
    public void resetConfirmationCode() {
        this.confirmationCode = null;
    }

    public boolean getConfirmed() {
        return confirmationCode == null;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserAccount other = (UserAccount) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}