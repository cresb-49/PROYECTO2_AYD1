/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Luis Monterroso
 */
@Entity
@Table(name = "usuario")
@DynamicUpdate
public class Usuario extends Auditor {

    @Column(length = 250, unique = true)
    private String nit;
    
    @Column(length = 250, unique = true)
    private String cui;
    
    @Column(length = 250)
    private String phone;
    
    @Column(length = 250)
    private String email;
    
    @Column(length = 250)
    private String nombre;
    
    @Column(length = 250)
    private String password;
    
    @Column(length = 250)
    private String tokenRecuperacion;

    public Usuario(String nit, String cui, String phone, String email, String nombre, String password, String tokenRecuperacion) {
        this.nit = nit;
        this.cui = cui;
        this.phone = phone;
        this.email = email;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

}
