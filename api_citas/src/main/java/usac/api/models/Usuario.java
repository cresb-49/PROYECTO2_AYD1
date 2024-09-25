/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author Luis Monterroso
 */
@Entity
@Table(name = "usuario")
@DynamicUpdate
public class Usuario extends Auditor {

    @NotBlank(message = "El cui del cliente no puede estar vacío.")
    @NotNull(message = "El cui del cliente no puede ser nulo")
    @Size(min = 11, max = 12, message = "El nit del cliente debe tener entre 11 y 12 caracteres.")
    @Column(length = 13, unique = true)
    private String nit;

    @NotBlank(message = "El cui del cliente no puede estar vacío.")
    @NotNull(message = "El cui del cliente no puede ser nulo")
    @Size(min = 11, max = 12, message = "El cui del cliente debe tener entre 11 y 12 caracteres.")
    @Column(length = 13, unique = true)
    private String cui;

    @NotBlank(message = "El teléfono del cliente no puede estar vacío.")
    @NotNull(message = "El teléfono del cliente no puede ser nulo")
    @Size(min = 8, max = 9, message = "El teléfono del cliente debe tener entre 8 y 9 caracteres.")
    @Column(length = 10, unique = true)
    private String phone;

    @NotBlank(message = "El email del cliente no puede estar vacío.")
    @NotNull(message = "El email del cliente no puede ser nulo")
    @Size(min = 1, max = 250, message = "El email del cliente debe tener entre 1 y 250 caracteres.")
    @Column(length = 250)
    private String email;

    @NotBlank(message = "Los nombres del cliente no puede estar vacío.")
    @Size(min = 1, max = 250, message = "Los nombres del cliente debe tener entre 1 y 250 caracteres.")
    @Column(length = 250)
    private String nombres;

    @NotBlank(message = "Los apellidos del cliente no puede estar vacío.")
    @Size(min = 1, max = 250, message = "Los apellidos del cliente debe tener entre 1 y 250 caracteres.")
    @Column(length = 250)
    private String apellidos;

    @NotBlank(message = "La password del cliente no puede estar vacía.")
    @NotNull(message = "La password del cliente no puede ser nula.")
    @Size(min = 1, max = 250, message = "El email del cliente debe tener entre 1 y 250 caracteres.")
    @Column(length = 250)
    private String password;

    @Column(length = 250)
    private String tokenRecuperacion;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<RolUsuario> roles;

    public Usuario(String nit, String cui, String phone, String email, String nombres, String apellidos, String password, String tokenRecuperacion) {
        this.nit = nit;
        this.cui = cui;
        this.phone = phone;
        this.email = email;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.password = password;
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public Usuario() {
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public List<RolUsuario> getRoles() {
        return roles;
    }

    public void setRoles(List<RolUsuario> roles) {
        this.roles = roles;
    }

}
