package usac.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @author Carlos
 */
@Entity
@Table(name = "cancha")
@SQLDelete(sql = "UPDATE cancha SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Cancha extends Auditor {

    @Column(nullable = false, precision = 2)
    @NotNull(message = "El costo por hora no puede ser nulo.")
    @Min(value = 0, message = "El costo por hora no puede ser negativo.")
    private Double costoHora;

    @Column(length = 250, nullable = false)
    @NotBlank(message = "La descripción de la cancha no puede estar vacía.")
    @NotNull(message = "La descripción de la cancha no puede ser nula")
    private String descripcion;

    @OneToMany(mappedBy = "cancha", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Where(clause = "deleted_at IS NULL")
    private List<HorarioCancha> horarios;

    @OneToMany(mappedBy = "cancha", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    private List<ReservaCancha> reservas;

    public Cancha() {
    }

    public Cancha(Double costoHora, String descripcion) {
        this.costoHora = costoHora;
        this.descripcion = descripcion;
    }

    public Double getCostoHora() {
        return costoHora;
    }

    public void setCostoHora(Double costoHora) {
        this.costoHora = costoHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<HorarioCancha> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioCancha> horarios) {
        this.horarios = horarios;
    }

    public List<ReservaCancha> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservaCancha> reservas) {
        this.reservas = reservas;
    }

}
