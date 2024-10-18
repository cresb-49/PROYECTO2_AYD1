/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author luid
 */
@Entity
@Table(name = "reserva_cancha")
public class ReservaCancha extends Auditor {

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "cancha", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "La cancha reservada no puede ser nula.")
    private Cancha cancha;

    public ReservaCancha(Reserva reserva, Cancha cancha) {
        this.reserva = reserva;
        this.cancha = cancha;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

}
