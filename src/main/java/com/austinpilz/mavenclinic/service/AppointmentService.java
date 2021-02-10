package com.austinpilz.mavenclinic.service;

import com.austinpilz.mavenclinic.core.MavenAppointment;
import com.austinpilz.mavenclinic.core.ScheduleAppointmentRequest;
import com.austinpilz.mavenclinic.core.ScheduleAppointmentResponse;
import com.austinpilz.mavenclinic.manager.UserAppointmentManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Account Service acts as the layer of business logic that controls appointment behavior prior to going to the data
 * layer. For this example, the UserAppointmentManager is the "business logic" behind scheduling since we don't have an
 * actual database back end. If we did, the business logic would go in this service.
 *
 * In a normal application, the back end would be a database which this service would interact with (or another layer
 * would) in order to store and retrieve appointments from the database. Since this example just calls for storing it in
 * memory and the requirements only want to be able to see appointments for a specific user, I chose to store appointments
 * according to user for easy in-memory lookup. It pains me to do it and if I had more time I'd implement a H2 database,
 * but here we are. This allows for the fastest lookup (a la hashing) for user appointments.
 *
 * @author Austin Pilz
 */
@Service
@AllArgsConstructor
public class AppointmentService {

    private Map<String, UserAppointmentManager> appointments = new HashMap<>();

    /**
     * Processes new appointment scheduling request. This will attempt to schedule the new appointment for the user and
     * will return the outcome in the response.
     *
     * Reviewer Note: The validity of the request is handled by Spring using the @Valid param in the rest controller.
     * It will validate the request field constraints so that when we get here, we can assume safely that the request
     * is valid.
     *
     * @param request Schedule Appointment Request.
     * @return Schedule Appointment Response.
     * @author Austin Pilz
     */
    public ScheduleAppointmentResponse scheduleAppointment(ScheduleAppointmentRequest request) {

        return getOrCreateUserAppointmentManager(request.getUserId()).scheduleAppointment(request);
    }

    /**
     * This will obtain the {@link UserAppointmentManager} manager for the provided user. If a manager does not already exist
     * for the user, this will create one and store it in the data store.
     *
     * @param userId User ID.
     * @return User Appointment Manager.
     * @author Austin Pilz n0286596
     */
    private UserAppointmentManager getOrCreateUserAppointmentManager(String userId) {

        UserAppointmentManager userAppointmentManager = appointments.get(userId);

        if (userAppointmentManager == null) {
            // Create new entry for this user.
            userAppointmentManager = new UserAppointmentManager(userId);
            appointments.put(userId, userAppointmentManager);
        }

        return userAppointmentManager;
    }

    /**
     * Obtains all of the users {@link MavenAppointment}s.
     *
     * @param userId User ID.
     * @return User appointments.
     * @author Austin Pilz n2086596
     */
    public List<MavenAppointment> getUserAppointments(String userId) {

        return getOrCreateUserAppointmentManager(userId).getAllAppointments();
    }
}
