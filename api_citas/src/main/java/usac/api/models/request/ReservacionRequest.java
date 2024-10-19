/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;

/**
 *
 * @author luid
 */
@Controller
public class ReservacionRequest {

    @NotNull(message = "La hora de inicio de la reservacion.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Formato para hora
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin de la reservacion no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Formato para hora
    private LocalTime horaFin;

    @NotNull(message = "La fecha de la reservacion no puede ser nula")
    private LocalDate fechaReservacion;

    public ReservacionRequest(LocalTime horaInicio, LocalTime horaFin, LocalDate fechaReservacion) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaReservacion = fechaReservacion;
    }

    public ReservacionRequest() {
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public LocalDate getFechaReservacion() {
        return fechaReservacion;
    }

    public void setFechaReservacion(LocalDate fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

}
