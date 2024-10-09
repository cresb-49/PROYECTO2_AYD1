/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import usac.api.models.Rol;

/**
 *
 * @author luid
 */
@Component
public class UsuarioRolAsignacionRequest {

    @NotNull(message = "El id del usuario no puede ser nulo.")
    private Long usuarioId;
    @NotNull(message = "Los roles no puede nser nulos")
    private List<Rol> roles;

    public UsuarioRolAsignacionRequest() {
    }

    public UsuarioRolAsignacionRequest(Long usuarioId, List<Rol> roles) {
        this.usuarioId = usuarioId;
        this.roles = roles;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

}
