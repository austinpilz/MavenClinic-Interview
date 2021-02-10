package com.austinpilz.mavenclinic.manager;

import com.austinpilz.mavenclinic.core.MavenAppointment;
import com.austinpilz.mavenclinic.core.ScheduleAppointmentRequest;
import com.austinpilz.mavenclinic.core.ScheduleAppointmentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The User Appointment Manager controls all of the appointments for a specific user. It allows for scheduling new appointments
 * and retrieving information about existing appointments.
 *
 * Normally this logic would be in the Appointment Service as it is business logic, but since this application is not
 * using a database back end, this style of manager allows us to simulate it a bit more cleanly.
 *
 * @author Austin Pilz
 */
@AllArgsConstructor
@Builder
public class UserAppointmentManager {

    private final String userId;
    private final Map<LocalDate, MavenAppointment> appointments = new HashMap<>();

    /**
     * Processes the appointment schedule request for the user. This will determine if the appointment being requested
     * can be honored. If it is, it will schedule the appointment for the user.
     *
     * Reviewer Note: The requirement of this project dictates that the appointment time be on the hour or half past, which
     * this does by validating it. We could also change the appointment time to the hour or half past (whichever is closer
     * to what is sent in), but that could be a disconnect between what the user thought the time was and what we schedule
     * it to be, so I opted not to do that.
     *
     * @param request Schedule Appointment Response.
     * @return Schedule Appointment Response.
     * @author Austin Pilz
     */
    public ScheduleAppointmentResponse scheduleAppointment(ScheduleAppointmentRequest request) {

        ScheduleAppointmentResponse response = new ScheduleAppointmentResponse();

        // Validate constraint of only one appointment per day.
        if (isAlreadyAppointmentScheduled(request.getAppointmentTime().toLocalDate())) {
            // The user already has an appointment scheduled on this date which is the only one they can have.
            response.setStatusMessage("User already has existing appointment on " + request.getAppointmentTime().toLocalDate());

            return response;
        }

        // Validate constraint of time on the hour or half past.
        if (!isAppointmentTimeValid(request.getAppointmentTime())) {
            // The appointment time is not valid.
            response.setStatusMessage("Appointment time must be on the hour or half past.");

            return response;
        }

        // Create the new appointment.
        MavenAppointment appointment = MavenAppointment.builder()
                .userId(request.getUserId())
                .start(request.getAppointmentTime())
                .end(request.getAppointmentTime().plusMinutes(30)) // Every appointment is 30 min, exactly.
                .build();

        // "Schedule" the appointment by persisting it.
        addAppointment(appointment);

        // Update our response since we accepted the request.
        response.setAppointmentAccepted(true);

        return response;
    }

    /**
     * Adds the appointment to the data store which commits the schedule.
     *
     * @param appointment Appointment.
     * @author Austin Pilz
     */
    void addAppointment(MavenAppointment appointment) {

        appointments.put(appointment.getStart().toLocalDate(), appointment);
    }

    /**
     * Returns if there is already a scheduled appointment on the provided day.
     *
     * @param date Date.
     * @return If there is already an appointment scheduled for the user on the provided date.
     * @author Austin Pilz
     */
    boolean isAlreadyAppointmentScheduled(LocalDate date) {

        return appointments.containsKey(date);
    }

    /**
     * Determines if the desired appointment time adheres to our requirements of being on the hour or half past.
     *
     * Reviewer Note: This could be moved out into a static util class since it's a validation that can be run statically.
     * For ease of use for this small scale example, I kept it here.
     *
     * @param time Desired appointment time.
     * @return If the appointment time is on the hour or half past.
     * @author Austin Pilz
     */
    boolean isAppointmentTimeValid(LocalDateTime time) {

        LocalDate appointmentDate = time.toLocalDate();

        LocalDateTime onTheHour = appointmentDate.atTime(time.getHour(), 00);
        LocalDateTime halfPastHour = appointmentDate.atTime(time.getHour(), 30);

        return time.isEqual(onTheHour) || time.isEqual(halfPastHour);
    }

    /**
     * Returns all of the users currently scheduled appointments.
     *
     * Reviewer Note: You could also make this return an unmodifiable collection since it really shouldn't be modified
     * elsewhere. In this case it's not of big concern since the list is created locally and isn't a reference to a list
     * in this class.
     *
     * @return All user scheduled appointments.
     * @author Austin Pilz
     */
    public List<MavenAppointment> getAllAppointments() {

        return new ArrayList<>(appointments.values());
    }
}
