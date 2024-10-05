/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services.authentification;

import java.util.ArrayList;
import java.util.Optional;
import usac.api.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import usac.api.repositories.UsuarioRepository;

/**
 *
 * @author luism
 */
@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioBusqueda = usuarioRepository.findByEmail(username);
        if (!usuarioBusqueda.isEmpty()) {//si no esta vacia la busqueda
            Usuario usuario = usuarioBusqueda.get();
            User.UserBuilder userBuilder = User.withUsername(username);
            userBuilder.password(usuario.getPassword()).roles(
                    usuario.getRol().getNombre()
            );
            return userBuilder.build();

        }
        throw new UsernameNotFoundException(username);
    }

}
