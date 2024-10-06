package usac.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import javax.persistence.Column;
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
 * @author Carlos
 */
@Entity
@Table(name = "horario_cancha", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "dia", "cancha" })
})
@SQLDelete(sql = "UPDATE horario_cancha SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class HorarioCancha extends Auditor {
    
    @ManyToOne
    @JoinColumn(name = "cancha",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true)
    private Cancha cancha;
    
    @ManyToOne
    @JoinColumn(name = "dia", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dia dia;
    
    @NotNull(message = "La hora de apertura no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Formato para hora
    private LocalTime apertura;

    @NotNull(message = "La hora de cierre no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // Formato para hora
    private LocalTime cierre;

    public HorarioCancha() {
    }

    public HorarioCancha(Cancha cancha, Dia dia, LocalTime apertura, LocalTime cierre) {
        this.cancha = cancha;
        this.dia = dia;
        this.apertura = apertura;
        this.cierre = cierre;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
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
}
