/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.dto;

import org.springframework.stereotype.Component;
import usac.api.models.Usuario;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class LoginDTO {

    private Usuario usuario;
    private String jwt;
    private boolean hasTwoFactorCode;

    public LoginDTO(Usuario usuario, String jwt) {
        this.usuario = usuario;
        this.jwt = jwt;
    }

    public LoginDTO(Usuario usuario, String jwt, boolean hasTwoFactorCode) {
        this.usuario = usuario;
        this.jwt = jwt;
        this.hasTwoFactorCode = hasTwoFactorCode;
    }

    public LoginDTO() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public boolean isHasTwoFactorCode() {
        return hasTwoFactorCode;
    }

    public void setHasTwoFactorCode(boolean hasTwoFactorCode) {
        this.hasTwoFactorCode = hasTwoFactorCode;
    }

}
