/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.FetchType;

/**
 * @author carlo
 */
@Entity
@Table(name = "empleado", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario"})
})
@SQLDelete(sql = "UPDATE empleado SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Empleado extends Auditor {

    @OneToOne
    @JoinColumn(name = "usuario", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @OneToMany(mappedBy = "empleado", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Where(clause = "deleted_at IS NULL")
    private List<HorarioEmpleado> horarios;

    @OneToMany(mappedBy = "empleado", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<ReservaServicio> citas;

    public Empleado() {
    }

    public Empleado(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<HorarioEmpleado> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioEmpleado> horarios) {
        this.horarios = horarios;
    }

    public List<ReservaServicio> getCitas() {
        return citas;
    }

    public void setCitas(List<ReservaServicio> citas) {
        this.citas = citas;
    }

}
