/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @author carlos
 */
@Entity
@Table(name = "tipo_empleado", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nombre"})
})
@SQLDelete(sql = "UPDATE tipo_empleado SET deleted_at = NULL WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class TipoEmpleado extends Auditor {

    @Column(length = 250, nullable = false)
    @NotBlank(message = "El nombre del tipo de empleado no puede estar vacío.")
    @NotNull(message = "El nombre del tipo de empleado no puede ser nulo")
    @Size(min = 1, max = 250, message = "El nombre del tipo de empleado debe tener entre 1 y 250 caracteres.")
    private String nombre;

    public TipoEmpleado() {
    }

    public TipoEmpleado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
