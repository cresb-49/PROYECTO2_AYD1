/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package usac.api.services.permisos;

import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import usac.api.models.Permiso;
import usac.api.models.RolPermiso;
import usac.api.models.Usuario;
import usac.api.services.UsuarioService;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ValidadorPermiso {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UsuarioService usuarioService;

    public Boolean verificarPermiso() throws Exception {
        Usuario usuarioEncontrado = this.getUsuarioPorJwt();
        if (!this.isAyudante()) {
            return true;
        }
        //verificar si el usuario tiene el permiso a la ruta que esta intentando acceder
        if (!this.tienePermiso(usuarioEncontrado.getRol().getPermisosRol())) {
            throw new Exception("Sin permisos");
        }
        return true;
    }

    public Boolean verificarPermiso(String terminacionUrl) throws Exception {
        Usuario usuarioEncontrado = this.getUsuarioPorJwt();
        if (!this.isAyudante()) {
            return true;
        }
        //verificar si el usuario tiene el permiso a la ruta que esta intentando acceder
        if (!this.tienePermiso(usuarioEncontrado.getRol().getPermisosRol(), terminacionUrl)) {
            throw new Exception("Sin permisos");
        }
        return true;
    }

    public Usuario getUsuarioPorJwt() throws Exception {
        //extraer la autenticacion (JWT)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Extraer el email del usuario
        String email = authentication.getName();
        //si se trata de un ayudante entonces mandar a buscar al usuario por su correo
        return this.usuarioService.getByEmail(email);
    }

    private boolean isAyudante() {
        //extraer la autenticacion (JWT)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Extraer roles del usuario y verificar si se trata de un ayudante
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //buscar el rol AYUDANTE
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_AYUDANTE"));
    }

    /**
     * Verifica si el usuario tiene un permiso con el nombre dado.
     *
     * @param permisos
     * @return true si el permiso está asignado al usuario, false en caso
     * contrario.
     */
    private boolean tienePermiso(List<RolPermiso> permisos) {
        //obtener el nombre de la ruta que se esta usando
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        Boolean tienePermiso = permisos.stream()
                .map(RolPermiso::getPermiso)
                .map(Permiso::getRuta)
                .anyMatch(nombre -> requestURI.contains(nombre));
        return tienePermiso;
    }

    /**
     * Verifica si el usuario tiene un permiso con el nombre dado.
     *
     * @param permisos
     * @return true si el permiso está asignado al usuario, false en caso
     * contrario.
     */
    private boolean tienePermiso(List<RolPermiso> permisos,
            String terminacionUrl) {
        //obtener el nombre de la ruta que se esta usando
        String requestURI = request.getRequestURI() + "/" + terminacionUrl;
        System.out.println(requestURI);
        Boolean tienePermiso = permisos.stream()
                .map(RolPermiso::getPermiso)
                .map(Permiso::getRuta)
                .anyMatch(nombre -> requestURI.contains(nombre));
        return tienePermiso;
    }
}
