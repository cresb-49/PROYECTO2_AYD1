/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import usac.api.models.Permiso;
import usac.api.models.Rol;

/**
 *
 * @author luid
 */
@Component
public class RolCreateRequest {

    @NotNull(message = "El rol no puede ser nulo.")
    private Rol rol;
    @NotNull(message = "Los permisos no pueden ser nulos.")
    @NotEmpty(message = "Los permisos no pueden estar vacios.")
    private List<Permiso> permisos;

    public RolCreateRequest() {
    }

    public RolCreateRequest(Rol rol, List<Permiso> permisos) {
        this.rol = rol;
        this.permisos = permisos;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

}
