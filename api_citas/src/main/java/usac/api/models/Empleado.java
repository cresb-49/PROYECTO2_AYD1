/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.util.List;

/**
 * @author carlo
 */
@Entity
@Table(name = "empleado", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario"})
})
@SQLDelete(sql = "UPDATE empleado SET deleted_at = NULL WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Empleado extends Auditor {

    @OneToOne
    @JoinColumn(name = "usuario", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "tipo_empleado", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TipoEmpleado tipoEmpleado;

    @OneToMany(mappedBy = "empleado", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Where(clause = "deleted_at IS NULL")
    private List<HorarioEmpleado> horarios;

    public Empleado() {
    }

    public Empleado(Usuario usuario, TipoEmpleado tipoEmpleado) {
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

    public List<HorarioEmpleado> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioEmpleado> horarios) {
        this.horarios = horarios;
    }
}
