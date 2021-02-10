package com.austinpilz.mavenclinic.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The Maven Appointment represents a customer appointment placed on the platform.
 *
 * @author Austin Pilz
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MavenAppointment {

    private LocalDateTime start;
    private LocalDateTime end;
    private String userId;
}
