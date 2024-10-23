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
@Table(name = "reserva_servicio")
public class ReservaServicio extends Auditor {

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "La reserva no puede ser nula.")
    private Reserva reserva;

    @ManyToOne//indicador de relacion muchos a uno
    @JoinColumn(name = "empleado", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "El empleado no puede ser nulo.")
    @Schema(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Empleado empleado;

    @ManyToOne//indicador de relacion muchos a uno
    @JoinColumn(name = "servicio", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "El servicio no puede ser nulo.")
    private Servicio servicio;

    public ReservaServicio(Reserva reserva, Empleado empleado, Servicio servicio) {
        this.reserva = reserva;
        this.empleado = empleado;
        this.servicio = servicio;
    }

    public ReservaServicio() {
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

}
