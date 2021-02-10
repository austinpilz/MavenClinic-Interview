package com.austinpilz.mavenclinic.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ScheduleAppointmentResponse {

    private boolean appointmentAccepted;
    private String statusMessage;
}
