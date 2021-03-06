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
package org.cejug.yougi.event.business;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.cejug.yougi.event.entity.Venue;
import org.cejug.yougi.entity.EntitySupport;
import org.cejug.yougi.event.entity.Event;

/**
 * Manages venues.
 *
 * @author Hildeberto Mendonca - http://www.hildeberto.com
 */
@Stateless
@LocalBean
public class VenueBean {

    @PersistenceContext
    private EntityManager em;

    public Venue findVenue(String id) {
        if(id != null) {
            return em.find(Venue.class, id);
        }
        else {
            return null;
        }
    }

    public List<Venue> findVenues() {
    	return em.createQuery("select v from Venue v order by v.name asc")
                 .getResultList();
    }

    public List<Venue> findEventVenues(Event event) {
        if(event == null) {
            return null;
        }

        List<Venue> venues = em.createQuery("select ev.venue from EventVenue ev where ev.event = :event")
                               .setParameter("event", event)
                               .getResultList();

        if((venues == null || venues.isEmpty()) && event.getParent() != null) {
            venues = findEventVenues(event.getParent());
        }

        return venues;
    }

    public void save(Venue venue) {
    	if(EntitySupport.INSTANCE.isIdNotValid(venue)) {
            venue.setId(EntitySupport.INSTANCE.generateEntityId());
            em.persist(venue);
        }
        else {
            em.merge(venue);
        }
    }

    public void remove(String id) {
        Venue venue = findVenue(id);
        if(venue != null) {
            em.remove(venue);
        }
    }
}