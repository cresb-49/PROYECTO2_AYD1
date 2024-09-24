/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import usac.api.models.Auditor;
import usac.api.models.Usuario;

/**
 *
 * @author Luis Monterroso
 */
public class Service {

    @Autowired
    private Validator validator;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Si se falla la validacion se lanza Exception con los motivos
     *
     * @param object
     * @return
     * @throws Exception
     */
    public boolean validarModelo(Object object) throws Exception {
        // extraemos las validaciones
        Set<ConstraintViolation<Object>> validaciones = validator.validate(object);
        // validamos los valores
        if (!validaciones.isEmpty()) {
            throw new Exception(extraerErrores(validaciones));
        }
        return true;
    }

    /**
     * Selecciona solamente os objetos que tengan desactivatedAt = null
     *
     * @param <T>
     * @param entidades
     * @return
     */
    public <T extends Auditor> List<T> ignorarEliminados(List<T> entidades) {
        // Filtrar entidades donde deletedAt sea null
        return entidades.stream()
                .filter(entidad -> entidad.getDesactivatedAt() == null)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si el objeto tiene desactivatedAt = null
     *
     * @param <T> Tipo que extiende de Auditor
     * @param entidad Objeto que extiende de Auditor
     */
    public <T extends Auditor> void isDesactivated(T entidad) throws Exception {
        if (entidad.getDesactivatedAt() != null) {
            throw new Exception("Informacion no encontrada.");
        }
    }

    /**
     * Valida el estado de un solo atributo de un modelo.
     *
     * @param objeto
     * @param attributeName
     * @return
     * @throws Exception
     */
    public boolean validarAtributo(Object objeto, String attributeName) throws Exception {
        Set<ConstraintViolation<Object>> validaciones = validator.validateProperty(
                objeto, attributeName);
        if (!validaciones.isEmpty()) {
            throw new Exception(extraerErrores(validaciones));
        }
        return true;
    }

    /**
     * Extrae la lista de errores de una validacion hecha
     *
     * @param validaciones
     * @return
     */
    private String extraerErrores(Set<ConstraintViolation<Object>> validaciones) {
        String fallas = "";
        for (ConstraintViolation<Object> item : validaciones) {
            fallas += item.getMessage().concat("\n");
        }
        return fallas;
    }

    public void verificarUsuarioJwt(Usuario usuarioTratar) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioAutenticado = authentication.getName();
        // validar si el usuario tiene permiso de eliminar
        if (!emailUsuarioAutenticado.equals(usuarioTratar.getEmail())
                && !isUserAdmin(emailUsuarioAutenticado)) {
            throw new Exception("No tienes permiso para realizar acciones a este usuario.");
        }
    }

    private boolean isUserAdmin(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
