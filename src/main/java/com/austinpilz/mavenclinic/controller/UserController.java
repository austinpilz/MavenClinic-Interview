package com.austinpilz.mavenclinic.controller;

import com.austinpilz.mavenclinic.core.MavenAppointment;
import com.austinpilz.mavenclinic.exception.NoAppointmentsFoundException;
import com.austinpilz.mavenclinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This is the user controller which provides access primarily to a uniquer user's appointments with Maven Clinic.
 *
 * Reviewers Note: In the getUserAppointments() method, you'll find that we throw an exception if there are no appointments
 * found. This exception is mapped to the 204 NO CONTENT return status, so when its thrown Spring will return 204. Since
 * there is nothing to return, there is less network overhead to return 204 than it is 200 with a semi empty body. That's
 * why I opted for 204 NO CONTENT.
 *
 * When calling an endpoint like this for potentially a large list, I would normally include filtering options like count
 * and offset, to allow us to perform pagination. Especially with working mothers and families having to plan things out,
 * a customer could have scheduled quite a few appointments into the future. Ideally this endpoint would allow us to
 * specify how many we want back, perhaps just on what day (since there can only be one), etc. so that our calling application,
 * say a UI, doesn't receive a ton of unwanted information and has to wait while the back end (database) looks it up. For
 * this example you'll see I didn't include those, but I would if this were a to scale application.
 *
 * @author Austin Pilz
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Retrieve user appointments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User appointment(s) found.",
            content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MavenAppointment.class))
            }),
            @ApiResponse(responseCode = "204", description = "No user appointments found.",
                    content = {@Content})
    })
    @RequestMapping(value = "{userId}/appointments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MavenAppointment> getUserAppointments(@PathVariable(value = "userId") String userId) {

        // Obtain the users appointments.
        List<MavenAppointment> appointments = appointmentService.getUserAppointments(userId);

        if (CollectionUtils.isEmpty(appointments)) {
            // There are no appointments for the user.
            throw new NoAppointmentsFoundException();
        }

        return appointments;
    }
}


