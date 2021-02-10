package com.austinpilz.mavenclinic.manager;

import com.austinpilz.mavenclinic.core.MavenAppointment;
import com.austinpilz.mavenclinic.core.ScheduleAppointmentRequest;
import com.austinpilz.mavenclinic.core.ScheduleAppointmentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserAppointmentManagerTest {

    private UserAppointmentManager manager;

    @BeforeEach
    void prepare() {
        manager = new UserAppointmentManager("hireMePlz");
    }

    @Test
    @DisplayName("Schedule Appointment - Accepted")
    void shouldTestScheduleAppointment() {

        ScheduleAppointmentRequest request = ScheduleAppointmentRequest.builder()
                .appointmentTime(LocalDate.now().atStartOfDay()) // Valid time.
                .userId("hireMePlz")
                .build();

        // Schedule the appointment.
        ScheduleAppointmentResponse response = manager.scheduleAppointment(request);

        assertTrue(response.isAppointmentAccepted());
        assertNull(response.getStatusMessage());
    }

    @Test
    @DisplayName("Schedule Appointment - Duplicate Date")
    void shouldTestScheduleAppointmentDuplicateDate() {

        // Add example appointment for today.
        manager.addAppointment(MavenAppointment.builder()
                .userId("hireMePlz")
                .start(LocalDate.now().atStartOfDay())
                .end(LocalDate.now().atStartOfDay().plusMinutes(30))
                .build());

        ScheduleAppointmentRequest request = ScheduleAppointmentRequest.builder()
                .appointmentTime(LocalDate.now().atStartOfDay()) // Valid time.
                .userId("hireMePlz")
                .build();

        // Schedule the appointment.
        ScheduleAppointmentResponse response = manager.scheduleAppointment(request);

        assertFalse(response.isAppointmentAccepted());
        assertNotNull(response.getStatusMessage());
    }

    @Test
    @DisplayName("Schedule Appointment - Invalid Time")
    void shouldTestScheduleAppointmentInvalidTime() {

        ScheduleAppointmentRequest request = ScheduleAppointmentRequest.builder()
                .appointmentTime(LocalDate.now().atStartOfDay().plusMinutes(7)) // Invalid time.
                .userId("hireMePlz")
                .build();

        // Schedule the appointment.
        ScheduleAppointmentResponse response = manager.scheduleAppointment(request);

        assertFalse(response.isAppointmentAccepted());
        assertNotNull(response.getStatusMessage());
    }

    @Test
    @DisplayName("Appointment Already Scheduled")
    void shouldTestIsAppointmentAlreadyScheduled() {
        // Add example appointment for today.
        manager.addAppointment(MavenAppointment.builder()
                .userId("hireMePlz")
                .start(LocalDate.now().atStartOfDay())
                .end(LocalDate.now().atStartOfDay().plusMinutes(30))
                .build());

        assertTrue(manager.isAlreadyAppointmentScheduled(LocalDate.now()));
        assertFalse(manager.isAlreadyAppointmentScheduled(LocalDate.now().plusDays(1)));
    }

    @Test
    @DisplayName("Validate Appointment Time")
    void shouldTestIsAppointmentTimeValid() {

        // Validate on the hour and half past.
        assertTrue(manager.isAppointmentTimeValid(LocalDate.now().atStartOfDay()));
        assertTrue(manager.isAppointmentTimeValid(LocalDate.now().atStartOfDay().plusMinutes(30)));

        // Validate that anything not on the hour or half past fails.
        assertFalse(manager.isAppointmentTimeValid(LocalDate.now().atStartOfDay().plusMinutes(01)));
        assertFalse(manager.isAppointmentTimeValid(LocalDate.now().atStartOfDay().plusMinutes(31)));
        assertFalse(manager.isAppointmentTimeValid(LocalDate.now().atStartOfDay().plusMinutes(59)));
    }
}