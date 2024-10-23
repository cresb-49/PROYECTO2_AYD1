/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Luis Monterroso
 */
public class CreacionClienteRequest {

    @NotBlank(message = "El token de verificacion no puede estar vacio.")
    private String tokenAuth;

    @NotBlank(message = "El nit del cliente no puede estar vacío.")
    @NotNull(message = "El nit del cliente no puede ser nulo")
    @Size(min = 6, max = 12, message = "El nit del cliente debe tener entre 6 y 12 caracteres.")
    @Column(length = 13, unique = true)
    private String nit;

    @NotBlank(message = "El cui del cliente no puede estar vacío.")
    @NotNull(message = "El cui del cliente no puede ser nulo")
    @Size(min = 13, max = 13, message = "El cui del cliente debe tener 13 caracteres.")
    @Column(length = 13, unique = true)
    private String cui;

    @NotBlank(message = "El teléfono del cliente no puede estar vacío.")
    @NotNull(message = "El teléfono del cliente no puede ser nulo")
    @Size(min = 8, max = 8, message = "El teléfono del cliente debe tener 8 digitos")
    @Column(length = 10, unique = true)
    private String phone;

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

    public CreacionClienteRequest(String tokenAuth, String nit, String cui, String phone, String nombres, String apellidos, String password) {
        this.tokenAuth = tokenAuth;
        this.nit = nit;
        this.cui = cui;
        this.phone = phone;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.password = password;
    }

    public String getTokenAuth() {
        return tokenAuth;
    }

    public void setTokenAuth(String tokenAuth) {
        this.tokenAuth = tokenAuth;
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

}
