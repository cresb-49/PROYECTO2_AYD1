/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.util.List;
import org.springframework.stereotype.Component;
import usac.api.models.Permiso;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class RolPermisoUpdateRequest {

    private Long idRol;
    private List<Permiso> permisos;

    public RolPermisoUpdateRequest(Long idRol, List<Permiso> permisos) {
        this.idRol = idRol;
        this.permisos = permisos;
    }

    public RolPermisoUpdateRequest() {
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

}
