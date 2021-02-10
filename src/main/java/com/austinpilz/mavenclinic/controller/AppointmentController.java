package com.austinpilz.mavenclinic.controller;

import com.austinpilz.mavenclinic.core.ScheduleAppointmentRequest;
import com.austinpilz.mavenclinic.core.ScheduleAppointmentResponse;
import com.austinpilz.mavenclinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * This is the appointment controller which is used to access and interact with the appointments in the system.
 *
 * @author Austin Pilz
 */
@RestController
@RequestMapping("/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Schedule new appointment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment not accepted.",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ScheduleAppointmentResponse.class))
                    }),
            @ApiResponse(responseCode = "201", description = "Appointment successfully scheduled.",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ScheduleAppointmentResponse.class))
            })
    })
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleAppointmentResponse> scheduleAppointment(@Valid @RequestBody ScheduleAppointmentRequest request) {

        // Process the request for the new appointment.
        ScheduleAppointmentResponse response = appointmentService.scheduleAppointment(request);

        if (response.isAppointmentAccepted()) {
            // The appointment was accepted. Return 201 Created.
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        // The appointment was not accepted, just return 200 OK.
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
