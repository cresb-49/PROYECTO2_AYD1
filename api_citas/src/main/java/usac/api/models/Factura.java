/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "factura")
@SQLDelete(sql = "UPDATE factura SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Factura extends Auditor {

    @NotBlank(message = "El nombre del cliente no puede estar vacío.")
    @Size(min = 1, max = 250, message = "El nombre del cliente debe tener entre 1 y 250 caracteres.")
    @Column(length = 250)
    private String nombre;

    @NotBlank(message = "El NIT del cliente no puede estar vacío.")
    @NotNull(message = "El NIT del cliente no puede ser nulo.")
    @Size(min = 6, max = 12, message = "El NIT del cliente debe tener entre 6 y 12 caracteres.")
    @Column(length = 13)
    private String nit;

    @NotBlank(message = "El detalle del servicio no puede estar vacío.")
    @NotNull(message = "El detalle del servicio no puede ser nulo.")
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String concepto;

    @NotBlank(message = "El detalle del servicio no puede estar vacío.")
    @NotNull(message = "El detalle del servicio no puede ser nulo.")
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String detalle;

    @Column(name = "precio", nullable = false)
    @Min(value = 1, message = "El total debe ser de al menos 1.")
    private Double total;

    @OneToOne(mappedBy = "factura")
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "reserva", nullable = false, unique = true)
    private Reserva reserva;

    public Factura(String nombre, String nit, String concepto, String detalle, Double total) {
        this.nombre = nombre;
        this.nit = nit;
        this.concepto = concepto;
        this.detalle = detalle;
        this.total = total;
    }

    public Factura() {
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

}
