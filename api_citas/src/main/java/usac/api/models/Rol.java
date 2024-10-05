/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Luis Monterroso
 */
@Entity
@Table(name = "rol")
@SQLDelete(sql = "UPDATE rol SET deleted_at = NULL WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Rol extends Auditor {

    @Column(length = 250, nullable = false)
    @NotBlank(message = "El nombre del rol no puede estar vacío.")
    @NotNull(message = "El nombre del rol no puede ser nulo")
    @Size(min = 1, max = 250, message = "El nombre del rol debe tener entre 1 y 250 caracteres.")
    @Pattern(regexp = "^(CLIENTE|ADMIN|AYUDANTE)$",
            message = "El nombre de rol solo puede ser CLIENTE, ADMIN, AYUDANTE")
    private String nombre;

    @OneToMany(mappedBy = "rol", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    private List<Usuario> ususarios;

    @OneToMany(mappedBy = "rol", orphanRemoval = true)//indicamos que la relacion debera ser por medio del atributo "Paciente" del objeto Tratamiento
    @Cascade(CascadeType.ALL)
    private List<RolPermiso> permisosRol;

    public Rol() {
    }

    public Rol(Long id) {
        super(id);
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<RolPermiso> getPermisosRol() {
        return permisosRol;
    }

    public void setPermisosRol(List<RolPermiso> permisosRol) {
        this.permisosRol = permisosRol;
    }

    public List<Usuario> getUsusarios() {
        return ususarios;
    }

    public void setUsusarios(List<Usuario> ususarios) {
        this.ususarios = ususarios;
    }

}
