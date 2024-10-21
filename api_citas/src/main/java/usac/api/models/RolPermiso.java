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
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 *
 * @author Luis Monterroso
 */
@Entity
@Table(name = "rol_permiso")
//@SQLDelete(sql = "UPDATE rol_permiso SET deleted_at = NULL WHERE id = ?")
//@Where(clause = "desactivated_at IS NULL")
public class RolPermiso extends Auditor {

    @ManyToOne//indicador de relacion muchos a uno
    @JoinColumn(name = "rol", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Rol rol;

    @ManyToOne//indicador de relacion muchos a uno
    @JoinColumn(name = "permiso", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Permiso permiso;

    public RolPermiso() {
    }

    public RolPermiso(Rol rol, Permiso permiso) {
        this.rol = rol;
        this.permiso = permiso;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }
}
