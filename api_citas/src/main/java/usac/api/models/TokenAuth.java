/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Luis Monterroso
 */
@Entity
@Table(name = "token_auth")
@DynamicUpdate
public class TokenAuth extends Auditor {

    @NotBlank(message = "El email del cliente no puede estar vacío.")
    @NotNull(message = "El email del cliente no puede ser nulo")
    @Size(min = 1, max = 250, message = "El email del cliente debe tener entre 1 y 250 caracteres.")
    @Column(length = 250)
    private String email;

    @NotBlank(message = "El email del cliente no puede estar vacío.")
    @NotNull(message = "El email del cliente no puede ser nulo")
    private String token;

    private LocalDateTime fechaHoraExpiracion;

    public TokenAuth(String email, String token, LocalDateTime fechaHoraExpiracion) {
        this.email = email;
        this.token = token;
        this.fechaHoraExpiracion = fechaHoraExpiracion;
    }

    public TokenAuth() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getFechaHoraExpiracion() {
        return fechaHoraExpiracion;
    }

    public void setFechaHoraExpiracion(LocalDateTime fechaHoraExpiracion) {
        this.fechaHoraExpiracion = fechaHoraExpiracion;
    }

}
