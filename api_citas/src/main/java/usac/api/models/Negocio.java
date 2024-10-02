package usac.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "negocio")
@SQLDelete(sql = "UPDATE negocio SET deleted_at = NULL WHERE id = ?")
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

    @Column(nullable = false,columnDefinition="TINYINT(1) DEFAULT 0")
    @NotNull(message = "La asignación manual no puede ser nula")
    private boolean asignacionManual;

    @Column(length=250, nullable = false)
    @NotBlank(message = "La dirección del negocio no puede estar vacía.")
    @NotNull(message = "La dirección del negocio no puede ser nula")
    @Size(min = 1, max = 250, message = "La dirección del negocio debe tener entre 1 y 250 caracteres.")
    private String direccion;

    public Negocio() {
    }

    public Negocio(String logo, String nombre, boolean asignacionManual,String direccion) {
        this.logo = logo;
        this.nombre = nombre;
        this.asignacionManual = asignacionManual;
        this.direccion = direccion;
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

    public void setAsignacionManual(boolean asignacionManual) {
        this.asignacionManual = asignacionManual;
    }

    public boolean isAsignacion_manual() {
        return asignacionManual;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }
}
