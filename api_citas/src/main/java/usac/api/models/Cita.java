/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "cita")
@SQLDelete(sql = "UPDATE cita SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Cita extends Auditor {

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empleado", nullable = false)
    private Empleado empleado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "servicio", nullable = false)
    private Servicio servicio;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean realizada = false;

    @NotNull(message = "La fecha no puede ser nula")
    @Column(nullable = false)
    private Date fecha;

    @NotNull(message = "La hora de inicio de la cita no puede ser nula")
    @Column(nullable = false)
    private LocalTime inicioCita;

    @NotNull(message = "La hora de fin de la cita no puede ser nula")
    @Column(nullable = false)
    private LocalTime finCita;

    @OneToOne(optional = true)
    @JoinColumn(name = "factura", nullable = true)
    private Factura factura;

    public Cita() {
    }

    public Cita(Usuario usuario, Empleado empleado, Servicio servicio, boolean realizada, Date fecha, LocalTime inicioCita, LocalTime finCita, Factura factura) {
        this.usuario = usuario;
        this.empleado = empleado;
        this.servicio = servicio;
        this.realizada = realizada;
        this.fecha = fecha;
        this.inicioCita = inicioCita;
        this.finCita = finCita;
        this.factura = factura;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public boolean isRealizada() {
        return realizada;
    }

    public void setRealizada(boolean realizada) {
        this.realizada = realizada;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public LocalTime getInicioCita() {
        return inicioCita;
    }

    public void setInicioCita(LocalTime inicioCita) {
        this.inicioCita = inicioCita;
    }

    public LocalTime getFinCita() {
        return finCita;
    }

    public void setFinCita(LocalTime finCita) {
        this.finCita = finCita;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}
