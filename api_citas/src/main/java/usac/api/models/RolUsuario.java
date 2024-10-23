/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author luid
 */
@Entity
@Table(name = "rol_usuario")
//@SQLDelete(sql = "UPDATE rol_usuario SET deleted_at = NULL WHERE id = ?")
//@Where(clause = "deleted_at IS NULL")
public class RolUsuario extends Auditor {

    @ManyToOne//indicador de relacion muchos a uno
    @JoinColumn(name = "usuario", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)//indicador de relacion muchos a uno
    @JoinColumn(name = "rol", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Rol rol;

    public RolUsuario() {
    }

    public RolUsuario(Rol rol) {
        this.rol = rol;
    }

    public RolUsuario(Usuario usuario, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

}
