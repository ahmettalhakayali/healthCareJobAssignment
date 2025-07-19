package nl.gerimedica.assignment;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between Appointment entities and DTOs.
 */
@Component
public class AppointmentMapper {

    /**
     * Converts an Appointment entity to AppointmentDto.
     * 
     * @param appointment the entity to convert
     * @return the corresponding DTO, or null if input is null
     */
    public AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        
        return new AppointmentDto(
            appointment.getId(),
            appointment.getReason(),
            appointment.getDate(),
            appointment.getPatient() != null ? appointment.getPatient().getId() : null
        );
    }

    /**
     * Converts a list of Appointment entities to a list of AppointmentDto objects.
     * 
     * @param appointments the list of entities to convert
     * @return the list of corresponding DTOs
     */
    public List<AppointmentDto> toDtoList(List<Appointment> appointments) {
        if (appointments == null) {
            return List.of();
        }
        
        return appointments.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
} 