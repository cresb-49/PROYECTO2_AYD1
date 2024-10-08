package usac.api.models;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "horario_negocio", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "dia", "negocio" })
})
@SQLDelete(sql = "UPDATE horario_negocio SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class HorarioNegocio extends Auditor {

    @ManyToOne // indicador de relacion muchos a uno
    @JoinColumn(name = "dia", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dia dia;

    @ManyToOne // indicador de relacion muchos a uno
    @JoinColumn(name = "negocio", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    private Negocio negocio;

    @NotNull(message = "La hora de apertura no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Formato para hora
    private LocalTime apertura;

    @NotNull(message = "La hora de cierre no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Formato para hora
    private LocalTime cierre;

    public HorarioNegocio() {
    }

    public HorarioNegocio(Dia dia, Negocio negocio, LocalTime apertura, LocalTime cierre) {
        this.dia = dia;
        this.negocio = negocio;
        this.apertura = apertura;
        this.cierre = cierre;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public Negocio getNegocio() {
        return negocio;
    }

    public void setNegocio(Negocio negocio) {
        this.negocio = negocio;
    }

    public LocalTime getApertura() {
        return apertura;
    }

    public void setApertura(LocalTime apertura) {
        this.apertura = apertura;
    }

    public LocalTime getCierre() {
        return cierre;
    }

    public void setCierre(LocalTime cierre) {
        this.cierre = cierre;
    }

    @Override
    public String toString() {
        return "HorarioNegocio [apertura=" + apertura + ", cierre=" + cierre + ", dia=" + dia.getNombre() + ", negocio="
                + negocio.getId()
                + ", deletedAt=" + getDeletedAt() + "]";
    }
}
