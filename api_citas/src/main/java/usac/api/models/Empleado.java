/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 *
 * @author carlo
 */
@Entity
@Table(name = "empleado")
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

}
