package usac.api.services;

import usac.api.tools.Encriptador;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import usac.api.models.Rol;
import usac.api.models.RolUsuario;
import usac.api.models.Usuario;
import usac.api.models.dto.LoginDTO;
import usac.api.repositories.UsuarioRepository;
import usac.api.services.authentification.AuthenticationService;
import usac.api.services.authentification.JwtGeneratorService;

@Service
public class UsuarioService extends usac.api.services.Service {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolService rolService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtGeneratorService jwtGenerator;

    /**
     * obtiene todos los usuarios que no han sido eliminados
     *
     * @return
     */
    public List<Usuario> getUsuarios() {
        return this.ignorarEliminados(usuarioRepository.findAll());
    }

    /**
     * obtiene un usuario a partir de un correo electronico
     *
     * @param email
     * @return
     * @throws Exception
     */
    public Usuario getByEmail(String email) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            throw new Exception("Usuario no encontrado.");
        }
        this.isDesactivated(usuario);
        return usuario;
    }

    /**
     * obtiene un usuario a partir de un id
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Usuario getUsuario(Long id) throws Exception {
        if (id == null || id <= 0) {// si el correo esta en blanco entonces lanzmaos error
            throw new Exception("Id invalido.");
        }
        // mandamos a traer el estado de la cuenta
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            throw new Exception("No hemos encontrado el usuario.");
        }
        this.isDesactivated(usuario);
        return usuario;
    }

    /**
     * Crea un usuario cliente obteniendo el rol correspondiente
     *
     * @param crear
     * @return
     * @throws Exception
     */
    public LoginDTO crearUsuarioCliente(Usuario crear) throws Exception {
        // validamos
        this.validarModelo(crear);
        // traer rol AYUDANTE
        Rol rol = this.rolService.getRolByNombre("CLIENTE");
        // guardamos el usuario
        Usuario userCreado = this.guardarUsuario(crear, rol);
        // Generar el JWT para el usuario creado
        UserDetails userDetails = authenticationService.loadUserByUsername(crear.getEmail());
        String jwt = jwtGenerator.generateToken(userDetails);
        // Retornar la confirmación con el JWT
        if (userCreado.getId() > 0) {
            return new LoginDTO(userCreado, jwt);
        }
        throw new Exception("No pudimos crear tu usuario, inténtalo más tarde.");
    }

    /**
     * Guarda un usuario asignandole un rol
     *
     * @param crear
     * @param rol
     * @return
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    private Usuario guardarUsuario(Usuario crear, Rol rol) throws Exception {
        if (this.usuarioRepository.existsByEmail(crear.getEmail())) {
            throw new Exception("El Email ya existe.");
        }
        // Asignamos un rol al usuario
        RolUsuario rolUsuario = new RolUsuario(rol, crear);
        ArrayList<RolUsuario> rols = new ArrayList<>();
        rols.add(rolUsuario);
        crear.setRoles(rols);
        // Encriptar la contraseña
        crear.setPassword(Encriptador.encriptar(crear.getPassword()));
        // Guardar el usuario
        return this.usuarioRepository.save(crear);
    }

}
