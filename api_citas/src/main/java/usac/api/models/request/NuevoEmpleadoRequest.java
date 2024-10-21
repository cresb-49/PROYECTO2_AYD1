/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import usac.api.models.HorarioEmpleado;
import usac.api.models.Rol;
import usac.api.models.Usuario;

/**
 *
 * @author luid
 */
@Component
public class NuevoEmpleadoRequest {

    @NotNull(message = "El usuario no puede ser nulo")
    private Usuario usuario;

    @NotNull(message = "El rol no puede ser nulo")
    private Rol rol;

    @NotNull(message = "Los horarios del empleado no puede ser nulos.")
    private List<HorarioEmpleado> horarios;

    public NuevoEmpleadoRequest() {
    }

    public NuevoEmpleadoRequest(Usuario usuario, List<HorarioEmpleado> horarios, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
        this.horarios = horarios;
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

    public List<HorarioEmpleado> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioEmpleado> horarios) {
        this.horarios = horarios;
    }

}