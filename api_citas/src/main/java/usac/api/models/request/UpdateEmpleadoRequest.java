package usac.api.models.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import usac.api.models.HorarioEmpleado;
import usac.api.models.Rol;
import usac.api.models.Usuario;

public class UpdateEmpleadoRequest {

    @NotNull(message= "El id del empleado no puede ser nulo")
    private Long id;

    @NotNull(message = "El usuario no puede ser nulo")
    private Usuario usuario;

    @NotNull(message = "El rol no puede ser nulo")
    private Rol rol;

    @NotNull(message = "Los horarios del empleado no puede ser nulos.")
    private List<HorarioEmpleado> horarios;

    public UpdateEmpleadoRequest() {
    }

    public UpdateEmpleadoRequest(List<HorarioEmpleado> horarios, Long id, Rol rol, Usuario usuario) {
        this.horarios = horarios;
        this.id = id;
        this.rol = rol;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
