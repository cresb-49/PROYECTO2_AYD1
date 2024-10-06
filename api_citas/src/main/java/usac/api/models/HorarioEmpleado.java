/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(name = "horario_empleado", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "dia", "empleado" })
})
@SQLDelete(sql = "UPDATE horario_empleado SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class HorarioEmpleado extends Auditor{
    @ManyToOne // indicador de relacion muchos a uno
    @JoinColumn(name = "dia", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dia dia;

    @ManyToOne // indicador de relacion muchos a uno
    @JoinColumn(name = "empleado", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    private Empleado empleado;

    @NotNull(message = "La hora de apertura no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Formato para hora
    private LocalTime entrada;

    @NotNull(message = "La hora de cierre no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Formato para hora
    private LocalTime salida;

    public HorarioEmpleado() {
    }

    public HorarioEmpleado(Dia dia, Empleado empleado, LocalTime entrada, LocalTime salida) {
        this.dia = dia;
        this.empleado = empleado;
        this.entrada = entrada;
        this.salida = salida;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public LocalTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalTime entrada) {
        this.entrada = entrada;
    }

    public LocalTime getSalida() {
        return salida;
    }

    public void setSalida(LocalTime salida) {
        this.salida = salida;
    }
    
    
}
