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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "servicio", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre"})
})
@SQLDelete(sql = "UPDATE servicio SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Servicio extends Auditor {
    
    @Column(length = 250, nullable = false)
    @NotBlank(message = "El nombre del servicio no puede estar vacío.")
    @NotNull(message = "El nombre del servicio no puede ser nulo")
    @Size(min = 1, max = 250, message = "El nombre del servicio debe tener entre 1 y 250 caracteres.")
    private String nombre;

    @Column(name = "duracion", nullable = false)
    @DecimalMin(value = "0.1", message = "La duracion del servicio debe ser mayor o igual a 0.1 horas")
    private Double duracion;
    
    @Column(nullable = false)
    @Lob
    @NotBlank(message = "La imagen del servicio no puede estar vacío.")
    @NotNull(message = "La imagen del servicio no puede ser nulo")
    private String imagen;

    @Column(name = "precio", nullable = false)
    @Min(value = 1, message = "El precio debe tener como valor mínimo 1.")
    private Double costo;

    @Column(nullable = false)
    @Lob
    @NotBlank(message = "Los detalles del servicio no pueden estar vacío.")
    @NotNull(message = "Los detalles del servicio no pueden ser nulo")
    private String detalles;
    
    @OneToOne
    @JoinColumn(name = "tipo_empleado", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TipoEmpleado tipoEmpleado;

    public Servicio() {
    }

    public Servicio(String nombre, Double duracion, String imagen, Double costo, String detalles, TipoEmpleado tipoEmpleado) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.imagen = imagen;
        this.costo = costo;
        this.detalles = detalles;
        this.tipoEmpleado = tipoEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getDuracion() {
        return duracion;
    }

    public void setDuracion(Double duracion) {
        this.duracion = duracion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }
    
    
}
