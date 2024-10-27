package usac.api.models.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import usac.api.models.Factura;
import usac.api.models.ReservaCancha;
import usac.api.models.ReservaServicio;
import usac.api.models.Usuario;

public class ReservaDTO {
    private Long id;
    private Usuario reservador;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalDate fechaReservacion;
    private Long idFactura;
    private Boolean realizada;
    private LocalDateTime canceledAt;
    private Double adelanto;
    private Double totalACobrar;
    private ReservaCancha reservaCancha;
    private ReservaServicio reservaServicio;

    /**
     * @param id
     * @param reservador
     * @param horaInicio
     * @param horaFin
     * @param fechaReservacion
     * @param idFactura
     * @param realizada
     * @param canceledAt
     * @param adelanto
     * @param totalACobrar
     * @param reservaCancha
     * @param reservaServicio
     */
    public ReservaDTO(Long id, Usuario reservador, LocalTime horaInicio, LocalTime horaFin, LocalDate fechaReservacion,
            Long idFactura, Boolean realizada, LocalDateTime canceledAt, Double adelanto, Double totalACobrar,
            ReservaCancha reservaCancha, ReservaServicio reservaServicio) {
        this.id = id;
        this.reservador = reservador;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaReservacion = fechaReservacion;
        this.idFactura = idFactura;
        this.realizada = realizada;
        this.canceledAt = canceledAt;
        this.adelanto = adelanto;
        this.totalACobrar = totalACobrar;
        this.reservaCancha = reservaCancha;
        this.reservaServicio = reservaServicio;
    }

    public ReservaDTO() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the reservador
     */
    public Usuario getReservador() {
        return reservador;
    }

    /**
     * @param reservador the reservador to set
     */
    public void setReservador(Usuario reservador) {
        this.reservador = reservador;
    }

    /**
     * @return the horaInicio
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * @param horaInicio the horaInicio to set
     */
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * @return the horaFin
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * @param horaFin the horaFin to set
     */
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * @return the fechaReservacion
     */
    public LocalDate getFechaReservacion() {
        return fechaReservacion;
    }

    /**
     * @param fechaReservacion the fechaReservacion to set
     */
    public void setFechaReservacion(LocalDate fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    /**
     * @return the idFactura
     */
    public Long getIdFactura() {
        return idFactura;
    }

    /**
     * @param idFactura the idFactura to set
     */
    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    /**
     * @return the realizada
     */
    public Boolean getRealizada() {
        return realizada;
    }

    /**
     * @param realizada the realizada to set
     */
    public void setRealizada(Boolean realizada) {
        this.realizada = realizada;
    }

    /**
     * @return the canceledAt
     */
    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    /**
     * @param canceledAt the canceledAt to set
     */
    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    /**
     * @return the adelanto
     */
    public Double getAdelanto() {
        return adelanto;
    }

    /**
     * @param adelanto the adelanto to set
     */
    public void setAdelanto(Double adelanto) {
        this.adelanto = adelanto;
    }

    /**
     * @return the totalACobrar
     */
    public Double getTotalACobrar() {
        return totalACobrar;
    }

    /**
     * @param totalACobrar the totalACobrar to set
     */
    public void setTotalACobrar(Double totalACobrar) {
        this.totalACobrar = totalACobrar;
    }

    /**
     * @return the reservaCancha
     */
    public ReservaCancha getReservaCancha() {
        return reservaCancha;
    }

    /**
     * @param reservaCancha the reservaCancha to set
     */
    public void setReservaCancha(ReservaCancha reservaCancha) {
        this.reservaCancha = reservaCancha;
    }

    /**
     * @return the reservaServicio
     */
    public ReservaServicio getReservaServicio() {
        return reservaServicio;
    }

    /**
     * @param reservaServicio the reservaServicio to set
     */
    public void setReservaServicio(ReservaServicio reservaServicio) {
        this.reservaServicio = reservaServicio;
    }
}
