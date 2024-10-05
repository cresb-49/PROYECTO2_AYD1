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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Luis Monterroso
 */
@Entity
@Table(name = "permiso")
@DynamicUpdate
public class Permiso extends Auditor {

    @Column(name = "permiso", length = 250, unique = true)
    @NotBlank(message = "El nombre del permiso no puede estar vacío.")
    @NotNull(message = "El nombre del permiso no puede ser nulo")
    @Size(min = 1, max = 250, message = "El nombre del permiso debe tener entre 1 y 250 caracteres.")
    private String nombre;

    @Column(name = "ruta", length = 250, unique = true)
    @NotBlank(message = "El nombre del permiso no puede estar vacío.")
    @NotNull(message = "El nombre del permiso no puede ser nulo")
    @Size(min = 1, max = 250, message = "El nombre del permiso debe tener entre 1 y 250 caracteres.")
    private String ruta;

    @OneToMany(mappedBy = "permiso", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    private List<RolPermiso> asignaciones;

    public Permiso(Long id) {
        super(id);
    }

    public Permiso(String nombre, String ruta) {
        this.nombre = nombre;
        this.ruta = ruta;
    }

    public Permiso() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List<RolPermiso> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<RolPermiso> asignaciones) {
        this.asignaciones = asignaciones;
    }

}
