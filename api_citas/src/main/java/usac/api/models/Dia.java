package usac.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "dia")
@SQLDelete(sql = "UPDATE dia SET deleted_at = NULL WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Dia extends Auditor{

    @Column(length = 20, nullable = false)
    @NotBlank(message = "El nombre del día no puede estar vacío.")
    @NotNull(message = "El nombre del día no puede ser nulo")
    @Size(min = 1, max = 20, message = "El nombre del día debe tener entre 1 y 250 caracteres.")
    private String nombre;

    public Dia() {
    }

    public Dia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
