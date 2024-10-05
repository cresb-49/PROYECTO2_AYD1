/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import org.springframework.stereotype.Component;
import usac.api.models.TipoEmpleado;
import usac.api.models.Usuario;

/**
 *
 * @author luid
 */
@Component
public class NuevoEmpleadoRequest {

    private Usuario usuario;
    private TipoEmpleado tipoEmpleado;

    public NuevoEmpleadoRequest() {
    }

    public NuevoEmpleadoRequest(Usuario usuario, TipoEmpleado tipoEmpleado) {
        this.usuario = usuario;
        this.tipoEmpleado = tipoEmpleado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

}
