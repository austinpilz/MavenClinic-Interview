package com.austinpilz.mavenclinic.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScheduleAppointmentRequest {

    @ApiModelProperty(required = true, value = "Desired appointment date & time.")
    @NotNull
    private LocalDateTime appointmentTime;

    @ApiModelProperty(required = true, value = "UserID requesting the appointment.")
    @NotBlank
    private String userId;
}
