package usac.api.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;

@Entity
@Table(name = "negocio")
@SQLDelete(sql = "UPDATE negocio SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "desactivated_at IS NULL")
public class Negocio extends Auditor {

    @Column(nullable = false)
    @Lob
    @NotBlank(message = "El logo del negocio no puede estar vacío.")
    @NotNull(message = "El logo del negocio no puede ser nulo")
    private String logo;

    @Column(length = 250, nullable = false)
    @NotBlank(message = "El nombre del negocio no puede estar vacío.")
    @NotNull(message = "El nombre del negocio no puede ser nulo")
    @Size(min = 1, max = 250, message = "El nombre del negocio debe tener entre 1 y 250 caracteres.")
    private String nombre;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    @NotNull(message = "La asignación manual no puede ser nula")
    private boolean asignacionManual;

    @Column(length = 250, nullable = false)
    @NotBlank(message = "La dirección del negocio no puede estar vacía.")
    @NotNull(message = "La dirección del negocio no puede ser nula")
    @Size(min = 1, max = 250, message = "La dirección del negocio debe tener entre 1 y 250 caracteres.")
    private String direccion;

    @Column(nullable = false)
    @NotNull(message = "El porcentaje de adelanto no puede ser nula")
    @Min(value = 1, message = "El valor minimo del porcentaje de adelanto es 1.")
    private Double porcentajeAnticipo;

    @OneToMany(mappedBy = "negocio", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Where(clause = "deleted_at IS NULL")
    private List<HorarioNegocio> horarios;

    public Negocio() {
    }

    public Negocio(String logo, String nombre, boolean asignacionManual, String direccion,
            Double porcentajeAnticipo) {
        this.logo = logo;
        this.nombre = nombre;
        this.asignacionManual = asignacionManual;
        this.direccion = direccion;
        this.porcentajeAnticipo = porcentajeAnticipo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isAsignacionManual() {
        return asignacionManual;
    }

    public void setAsignacionManual(boolean asignacionManual) {
        this.asignacionManual = asignacionManual;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<HorarioNegocio> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioNegocio> horarios) {
        this.horarios = horarios;
    }

    public Double getPorcentajeAnticipo() {
        return porcentajeAnticipo;
    }

    public void setPorcentajeAnticipo(Double porcentajeAnticipo) {
        this.porcentajeAnticipo = porcentajeAnticipo;
    }

}
