/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Column;
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
@Table(name = "reserva")
public class Reserva extends Auditor {

    @NotNull(message = "El usuario reservador no puede ser nulo.")
    @ManyToOne//indicador de relacion muchos a uno
    @JoinColumn(name = "reservador", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Usuario reservador;

    @NotNull(message = "La hora de inicio de la reservacion.")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin de la reservacion no puede ser nula")
    private LocalTime horaFin;

    @NotNull(message = "La fecha de la reservacion no puede ser nula")
    private LocalDate fechaReservacion;

    // Relación opcional uno a uno con Factura
    @OneToOne(optional = false)
    @JoinColumn(name = "factura", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    private Factura factura;

    @NotNull(message = "La fecha de la reservacion no puede ser nula")
    private Boolean realizada;

    @Column(nullable = true)
    private LocalDateTime canceledAt;

    @NotNull(message = "El Adelanto de la reservacion no puede ser nula")
    private Double adelanto;

    @NotNull(message = "El Adelanto de la reservacion no puede ser nula")
    private Double totalACobrar;

    // Relación opcional uno a uno con Reserva
    @OneToOne(optional = true, mappedBy = "reserva")// La relación con Reserva es opcional
    @JoinColumn(name = "reserva_cancha")
    private ReservaCancha reservaCancha;

    // Relación opcional uno a uno con Cita
    @OneToOne(optional = true, mappedBy = "reserva") // La relación con Cita es opcional
    @JoinColumn(name = "reserva_servicio")
    private ReservaServicio reservaServicio;

    public Reserva(Usuario reservador, LocalTime horaInicio, LocalTime horaFin, LocalDate fechaReservacion, Factura factura, Boolean realizada,
            Double adelanto, Double totalACobrar) {
        this.reservador = reservador;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaReservacion = fechaReservacion;
        this.factura = factura;
        this.realizada = realizada;
        this.adelanto = adelanto;
        this.totalACobrar = totalACobrar;
    }

    public Reserva() {
    }

    public Usuario getReservador() {
        return reservador;
    }

    public void setReservador(Usuario reservador) {
        this.reservador = reservador;
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

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Boolean getRealizada() {
        return realizada;
    }

    public void setRealizada(Boolean realizada) {
        this.realizada = realizada;
    }

    public ReservaCancha getReservaCancha() {
        return reservaCancha;
    }

    public void setReservaCancha(ReservaCancha reservaCancha) {
        this.reservaCancha = reservaCancha;
    }

    public ReservaServicio getReservaServicio() {
        return reservaServicio;
    }

    public void setReservaServicio(ReservaServicio reservaServicio) {
        this.reservaServicio = reservaServicio;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public Double getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(Double adelanto) {
        this.adelanto = adelanto;
    }

    public Double getTotalACobrar() {
        return totalACobrar;
    }

    public void setTotalACobrar(Double totalACobrar) {
        this.totalACobrar = totalACobrar;
    }

}
