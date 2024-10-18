/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;

/**
 *
 * @author luid
 */
@Controller
public class ReservacionCanchaRequest extends ReservacionRequest {

    @NotNull(message = "La hora de apertura no puede ser nula")
    private Long canchaId;

    public ReservacionCanchaRequest(Long canchaId, LocalTime horaInicio, LocalTime horaFin, LocalDate fechaReservacion) {
        super(horaInicio, horaFin, fechaReservacion);
        this.canchaId = canchaId;
    }

    public ReservacionCanchaRequest() {
    }

    public Long getCanchaId() {
        return canchaId;
    }

    public void setCanchaId(Long canchaId) {
        this.canchaId = canchaId;
    }
}
