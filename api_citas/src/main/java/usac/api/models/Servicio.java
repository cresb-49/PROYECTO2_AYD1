/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

    @Column(nullable = false)
    @NotNull(message = "La cantidad de empleados que pueden trabajar al"
            + " no puede ser nula.")
    @Min(value = 1, message = "la cantidad de empleados que pueden trabajar al"
            + " mismo tiempo debe tener como valor mínimo 1.")
    private Integer empleadosParalelos;

    @OneToOne
    @NotNull(message = "El rol con el que se relaciona el servicio no puede ser nulo.")
    @JoinColumn(name = "rol", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@Schema(hidden = true)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Rol rol;

    @OneToMany(mappedBy = "servicio", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<ReservaServicio> reservas;

    public Servicio() {
    }

    public Servicio(String nombre, Double duracion, String imagen, Double costo,
            String detalles, Rol rol, Integer empleadosParalelos) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.imagen = imagen;
        this.costo = costo;
        this.detalles = detalles;
        this.rol = rol;
        this.empleadosParalelos = empleadosParalelos;
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<ReservaServicio> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservaServicio> reservas) {
        this.reservas = reservas;
    }

    public Integer getEmpleadosParalelos() {
        return empleadosParalelos;
    }

    public void setEmpleadosParalelos(Integer empleadosParalelos) {
        this.empleadosParalelos = empleadosParalelos;
    }

    @Override
    public String toString() {
        return "Servicio [nombre=" + nombre + ", duracion=" + duracion + ", costo=" + costo
                + ", detalles=" + detalles + ", rol=" + rol + "]";
    }
}
