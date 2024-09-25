package usac.api.tools;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class Encriptador {

    /**
     * Este metodo crea un bucle de 5 iteraciones y por cada iteracion envia y
     * recibe una clave incriptada
     *
     * @param password
     * @return
     */
    public String encriptar(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * Este metodo compara una contraseña sin encriptar con una contraseña
     * encriptada
     *
     * @param passwordSinEncriptar
     * @param passwordEncriptada
     * @return
     */
    public boolean compararPassword(String passwordSinEncriptar, String passwordEncriptada) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(passwordSinEncriptar, passwordEncriptada);
    }

}
