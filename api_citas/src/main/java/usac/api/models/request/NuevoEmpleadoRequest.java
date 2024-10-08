/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.util.List;

import org.springframework.stereotype.Component;

import usac.api.models.HorarioEmpleado;
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
    private List<HorarioEmpleado> horarios;

    public NuevoEmpleadoRequest() {
    }

    public NuevoEmpleadoRequest(Usuario usuario, TipoEmpleado tipoEmpleado,List<HorarioEmpleado> horarios) {
        this.usuario = usuario;
        this.tipoEmpleado = tipoEmpleado;
        this.horarios = horarios;
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

    public List<HorarioEmpleado> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioEmpleado> horarios) {
        this.horarios = horarios;
    }

}
