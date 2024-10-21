/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package usac.api.services.permisos;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import usac.api.models.Permiso;
import usac.api.models.RolPermiso;
import usac.api.models.RolUsuario;
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
        if (usuarioService.isUserAdmin(usuarioEncontrado.getEmail())) {
            return true;
        }
        //verificar si el usuario tiene el permiso a la ruta que esta intentando acceder
        if (!this.tienePermiso(usuarioEncontrado.getRoles())) {
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

    /**
     * Verifica si el usuario tiene un permiso con el nombre dado.
     *
     * @param permisos
     * @return true si el permiso está asignado al usuario, false en caso
     * contrario.
     */
    private boolean tienePermiso(List<RolUsuario> roles) {
        //obtener el nombre de la ruta que se esta usando
        String requestURI = request.getRequestURI();
        return this.compararPermisos(roles, requestURI);
    }
    
    private boolean compararPermisos(List<RolUsuario> roles, String url) {
        ArrayList<RolPermiso> permisos = new ArrayList<>();

        //por cada uno de los roles del usuario debemos adjuntar sus permisos
        for (RolUsuario rolUsuarioItem : roles) {
            permisos.addAll(rolUsuarioItem.getRol().getPermisosRol());
        }
        
        Boolean tienePermiso = permisos.stream()
                .map(RolPermiso::getPermiso)
                .map(Permiso::getRuta)
                .anyMatch(nombre -> url.contains(nombre));
        return tienePermiso;
    }
}
