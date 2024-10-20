/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReservacionServicioRequest extends ReservacionRequest {

    @NotNull(message = "El id del empleado no puede ser nulo.")
    private Long empleadoId;

    @NotNull(message = "El id del servicio no puede ser nulo.")
    private Long servicioId;

    public ReservacionServicioRequest(Long empleadoId, Long servicioId, LocalTime horaInicio, LocalTime horaFin, LocalDate fechaReservacion) {
        super(horaInicio, horaFin, fechaReservacion);
        this.empleadoId = empleadoId;
        this.servicioId = servicioId;
    }

    public ReservacionServicioRequest() {
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

}
