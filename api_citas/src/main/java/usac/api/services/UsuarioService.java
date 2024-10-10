package usac.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import usac.api.models.Empleado;
import usac.api.models.HorarioEmpleado;
import usac.api.models.Rol;
import usac.api.models.RolUsuario;
import usac.api.models.Usuario;
import usac.api.models.dto.LoginDTO;
import usac.api.models.request.NuevoEmpleadoRequest;
import usac.api.models.request.PasswordChangeRequest;
import usac.api.models.request.UserChangePasswordRequest;
import usac.api.repositories.UsuarioRepository;
import usac.api.services.authentification.AuthenticationService;
import usac.api.services.authentification.JwtGeneratorService;
import usac.api.tools.Encriptador;

@Service
public class UsuarioService extends usac.api.services.Service {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolService rolService;
    @Autowired
    private MailService mailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private Encriptador encriptador;
    @Autowired
    private JwtGeneratorService jwtGenerator;
    @Autowired
    private EmpleadoService empleadoService;

    /**
     * Envía un correo electrónico de recuperación de contraseña a un usuario
     * con un código de recuperación único.
     *
     * @param correo El correo electrónico del usuario que ha solicitado la
     * recuperación de su contraseña.
     * @return Un mensaje indicando si el correo de recuperación fue enviado con
     * éxito.
     * @throws Exception Si el correo está en blanco, el correo electrónico del
     * usuario no es encontrado, el usuario está desactivado, o si ocurre un
     * error durante el envío del correo de recuperación.
     */
    @Transactional(rollbackOn = Exception.class)
    public String enviarMailDeRecuperacion(String correo) throws Exception {
        // Verificamos que el correo no esté vacío
        if (correo.isBlank()) {
            throw new Exception("Correo vacío.");
        }

        // Buscamos el usuario por correo electrónico en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(correo).orElse(null);

        // Si no se encuentra el usuario, lanzamos una excepción
        if (usuario == null) {
            throw new Exception("No hemos encontrado tu correo electrónico.");
        }

        // Verificamos si el usuario está desactivado
        this.isDesactivated(usuario);

        // Generamos un código único de recuperación
        String codigoRecuperacion = UUID.randomUUID().toString();

        // Asignamos el código de recuperación al usuario
        usuario.setTokenRecuperacion(codigoRecuperacion);

        // Guardamos la actualización en la base de datos
        Usuario actualizacion = usuarioRepository.save(usuario);

        // Si la actualización es exitosa, enviamos el correo de recuperación
        if (actualizacion != null && actualizacion.getId() > 0) {
            // Usamos el servicio de correo para enviar el correo en segundo plano
            mailService.enviarCorreoDeRecuperacion(
                    actualizacion.getEmail(),
                    actualizacion.getTokenRecuperacion()
            );

            // Retornamos un mensaje de confirmación
            return "Te hemos enviado un correo electrónico con las instrucciones para recuperar tu cuenta. Por favor revisa tu bandeja de entrada.";
        }

        // Si algo falla en el proceso, lanzamos una excepción
        throw new Exception("No hemos podido enviar el correo electrónico. Inténtalo más tarde.");
    }

    /**
     * Inicia sesión para un usuario dado, validando credenciales, verificando
     * su estado y generando un token JWT si la autenticación es exitosa.
     *
     * @param log El objeto Usuario que contiene las credenciales de inicio de
     * sesión.
     * @return Un objeto LoginDTO que contiene el usuario autenticado y el token
     * JWT generado.
     * @throws Exception Si el correo electrónico o la contraseña son
     * incorrectos, el usuario está desactivado, o si ocurre un error de
     * autenticación.
     */
    public LoginDTO iniciarSesion(Usuario log) throws Exception {
        try {
            // Validamos el email y la contraseña
            this.validarAtributo(log, "email");
            this.validarAtributo(log, "password");

            // Buscamos el usuario por su correo electrónico
            Usuario usuario = usuarioRepository.findByEmail(log.getEmail()).orElse(null);

            // Si no se encuentra el usuario, lanzamos una excepción
            if (usuario == null) {
                throw new Exception("Correo electrónico incorrecto.");
            }

            // Verificamos que el usuario no esté desactivado
            this.isDesactivated(usuario);

            // Autenticamos al usuario utilizando su contraseña
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(log.getEmail(), log.getPassword()));

            // Cargamos los detalles del usuario y generamos el token JWT
            UserDetails userDetails = authenticationService.loadUserByUsername(log.getEmail());
            String jwt = jwtGenerator.generateToken(userDetails);

            return new LoginDTO(usuario, jwt);
        } catch (AuthenticationException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Cambia la contraseña de un usuario, validando el ID del usuario,
     * verificando que el usuario no esté desactivado, y encriptando la nueva
     * contraseña antes de guardarla.
     *
     * @param usuPassChange El objeto Usuario que contiene el ID y la nueva
     * contraseña a establecer.
     * @return Un mensaje indicando si el cambio de contraseña fue exitoso.
     * @throws Exception Si el ID del usuario es inválido, si el usuario no es
     * encontrado, si el usuario está desactivado, o si ocurre un error durante
     * el proceso de actualización de la contraseña.
     */
    @Transactional(rollbackOn = Exception.class)
    public String cambiarPassword(UserChangePasswordRequest usuPassChange) throws Exception {
        // validar el modelo
        boolean validarModelo = this.validarModelo(usuPassChange);
        // Buscamos al usuario por su ID
        Usuario usuario = usuarioRepository.findById(usuPassChange.getId()).orElse(null);

        // Si no se encuentra el usuario, lanzamos una excepción
        if (usuario == null) {
            throw new Exception("No hemos encontrado el usuario.");
        }

        // Verificamos que el usuario no esté desactivado y que tenga los permisos necesarios
        this.isDesactivated(usuario);
        this.verificarUsuarioJwt(usuario);

        // Verificamos que la contraseña actual sea correcta
        if (!this.encriptador.compararPassword(usuPassChange.getPassword(), usuario.getPassword())) {
            throw new Exception("Contraseña actual incorrecta.");
        }

        // Encriptamos la nueva contraseña y actualizamos el campo en el modelo de usuario
        usuario.setPassword(this.encriptador.encriptar(usuPassChange.getNewPassword()));

        // Guardamos el usuario con la nueva contraseña
        Usuario update = this.usuarioRepository.save(usuario);

        // Verificamos si el cambio fue exitoso comparando el ID del usuario original y el actualizado
        if (update != null && update.getId().longValue() == usuario.getId().longValue()) {
            return "Se cambió tu contraseña con éxito.";
        }

        // Si la actualización no fue exitosa, lanzamos una excepción
        throw new Exception("No pudimos actualizar tu contraseña, inténtalo más tarde.");
    }

    /**
     * Permite recuperar la contraseña de un usuario a partir de un código de
     * recuperación válido.
     *
     * @param cambioPassword El objeto PasswordChangeRequest que contiene el
     * código de recuperación y la nueva contraseña.
     * @return Un mensaje indicando si el cambio de contraseña fue exitoso.
     * @throws Exception Si el código de recuperación es inválido, el usuario
     * está desactivado, o si ocurre algún error durante el proceso de
     * actualización de la contraseña.
     */
    @Transactional(rollbackOn = Exception.class)
    public String recuperarPassword(PasswordChangeRequest cambioPassword) throws Exception {
        // Validamos el modelo PasswordChangeRequest
        this.validarModelo(cambioPassword);

        // Buscamos el usuario utilizando el código de recuperación
        Usuario usuario = this.usuarioRepository.findByTokenRecuperacion(cambioPassword.getCodigo()).orElse(null);

        // Si no se encuentra el usuario, lanzamos una excepción
        if (usuario == null) {
            throw new Exception("Código de autorización inválido.");
        }

        // Verificamos que el usuario no esté desactivado
        this.isDesactivated(usuario);

        // Limpiamos el token de recuperación y actualizamos la contraseña
        usuario.setTokenRecuperacion(null);
        usuario.setPassword(this.encriptador.encriptar(cambioPassword.getNuevaPassword()));

        // Guardamos los cambios en el repositorio
        Usuario update = this.usuarioRepository.save(usuario);

        // Verificamos si la actualización fue exitosa
        if (update != null && update.getId().longValue() == usuario.getId().longValue()) {
            return "Se cambió tu contraseña con éxito.";
        }
        throw new Exception("No pudimos actualizar tu contraseña, inténtalo más tarde.");
    }

    /**
     * Actualiza los datos de un usuario en la base de datos, validando la
     * existencia del usuario, que no haya otro usuario con el mismo correo
     * electrónico, y conservando la contraseña original.
     *
     * @param usuario El objeto Usuario con los nuevos datos a actualizar.
     * @return El objeto Usuario actualizado.
     * @throws Exception Si el ID es inválido, el usuario no es encontrado, ya
     * existe otro usuario con el mismo correo electrónico, o si ocurre un error
     * durante la actualización.
     */
    @Transactional(rollbackOn = Exception.class)
    public Usuario updateUsuario(Usuario usuario) throws Exception {
        // Validamos el ID del usuario
        if (usuario.getId() == null || usuario.getId() <= 0) {
            throw new Exception("Id inválido.");
        }

        // Buscamos el usuario por su ID
        Usuario usuarioEncontrado = usuarioRepository.findById(usuario.getId()).orElse(null);

        // Si no se encuentra el usuario, lanzamos una excepción
        if (usuarioEncontrado == null) {
            throw new Exception("No hemos encontrado el usuario.");
        }

        // Verificamos que el usuario tenga permisos y no esté desactivado
        this.verificarUsuarioJwt(usuarioEncontrado);
        this.isDesactivated(usuarioEncontrado);

        // Verificamos si existe otro usuario con el mismo correo electrónico
        if (this.usuarioRepository.existsUsuarioByEmailAndIdNot(usuario.getEmail(),
                usuario.getId())) {
            throw new Exception(String.format("No se editó el usuario %s, "
                    + "debido a que ya existe otro usuario con el mismo email.",
                    usuario.getEmail()));
        }

        // Verificamos si existe otro usuario con el mismo phone
        if (this.usuarioRepository.existsUsuarioByPhoneAndIdNot(usuario.getPhone(),
                usuario.getId())) {
            throw new Exception(String.format("No se editó el usuario"
                    + " %s, debido a que ya existe otro usuario con el mismo teléfono.",
                    usuario.getEmail()));
        }

        // Verificamos si existe otro usuario con el mismo correo cui
        if (this.usuarioRepository.existsUsuarioByCuiAndIdNot(usuario.getCui(),
                usuario.getId())) {
            throw new Exception(String.format("No se editó el usuario"
                    + " %s, debido a que ya existe otro usuario con el mismo cui.",
                    usuario.getEmail()));
        }

        if (usuario.getNit() != null) {
            // Verificamos si existe otro usuario con el mismo nit
            if (this.usuarioRepository.existsUsuarioByNitAndIdNot(usuario.getNit(),
                    usuario.getId())) {
                throw new Exception(String.format("No se editó el usuario"
                        + " %s, debido a que ya existe otro usuario con el mismo nit.",
                        usuario.getEmail()));
            }
        }

        // Mantenemos la contraseña original y roles del usuario
        usuario.setPassword(usuarioEncontrado.getPassword());
        usuario.setRoles(usuarioEncontrado.getRoles());

        // Validamos el modelo del usuario
        this.validarModelo(usuario);

        // Guardamos los cambios en el repositorio
        Usuario usuarioUpdate = this.usuarioRepository.save(usuario);

        // Verificamos si la actualización fue exitosa
        if (usuarioUpdate != null && usuarioUpdate.getId() > 0) {
            return usuarioUpdate;
        }
        throw new Exception("No pudimos actualizar el usuario, inténtalo más tarde.");
    }

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
     * obtiene un usuario a partir de un id, verifica que la peticion la haga el
     * mismo usuario logeado.
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Usuario getPerfil(Long id) throws Exception {
        if (id == null || id <= 0) {// si el correo esta en blanco entonces lanzmaos error
            throw new Exception("Id invalido.");
        }
        // mandamos a traer el estado de la cuenta
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            throw new Exception("No hemos encontrado el usuario.");
        }
        this.verificarUsuarioJwt(usuario);
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

        //preparamos el rol
        List<RolUsuario> rolesUsuario = new ArrayList<>();
        rolesUsuario.add(new RolUsuario(crear, rol));

        // guardamos el usuario con el rol
        Usuario userCreado = this.guardarUsuario(crear, rolesUsuario);
        // Generar el JWT para el usuario creado
        UserDetails userDetails = authenticationService.loadUserByUsername(crear.getEmail());
        String jwt = jwtGenerator.generateToken(userDetails);
        // Retornar la confirmación con el JWT
        if (userCreado != null && userCreado.getId() > 0) {
            return new LoginDTO(userCreado, jwt);
        }
        throw new Exception("No pudimos crear tu usuario, inténtalo más tarde.");
    }

    /**
     * Crea un nuevo usuario con el rol de Administrador, validando el modelo y
     * asignando el rol correspondiente.
     *
     * @param crear El objeto Usuario que contiene los datos del nuevo
     * administrador a crear.
     * @return El objeto Usuario creado con el rol de Administrador.
     * @throws Exception Si los datos del usuario no son válidos o si ocurre un
     * error durante la creación.
     */
    public Usuario crearAdministrador(Usuario crear) throws Exception {
        // Validamos el modelo de usuario
        this.validarModelo(crear);
        // Guardamos el usuario con el rol de Administrador
//        Usuario usuarioGuardado = this.guardarUsuario(crear, null);
        // Obtenemos el rol 'ADMIN' para asignarlo al nuevo usuario
        Rol rol = this.rolService.getRolByNombre("ADMIN");

        List<RolUsuario> rolesUsuario = new ArrayList<>();
        rolesUsuario.add(new RolUsuario(crear, rol));

        // Guardamos el usuario con el rol de Administrador
        return this.guardarUsuario(crear, rolesUsuario);
    }

    /**
     * Crea un nuevo usuario con el rol de empleado, validando el modelo y
     * asignando el rol correspondiente.
     *
     * @param nuevoEmpleadoRequest
     * @return El objeto Usuario creado con el rol de Empleado.
     * @throws Exception Si los datos del usuario no son válidos o si ocurre un
     * error durante la creación.
     */
    public Usuario crearEmpleado(NuevoEmpleadoRequest nuevoEmpleadoRequest) throws Exception {
        // Validamos el modelo de usuario
        this.validarModelo(nuevoEmpleadoRequest);
        this.validarModelo(nuevoEmpleadoRequest.getUsuario());
        this.validarId(nuevoEmpleadoRequest.getRol());

        // Obtenemos el rol para asignarlo al nuevo usuario
        Rol rolEmpleado = this.rolService.getRolByNombre("EMPLEADO");
        Rol rolPrincipal = this.rolService.getRolById(nuevoEmpleadoRequest.getRol().getId());

        List<RolUsuario> rolesUsuario = new ArrayList<>();
        rolesUsuario.add(new RolUsuario(nuevoEmpleadoRequest.getUsuario(), rolEmpleado));
        rolesUsuario.add(new RolUsuario(nuevoEmpleadoRequest.getUsuario(), rolPrincipal));

        Usuario usuarioGuardadoFinal = this.guardarUsuario(nuevoEmpleadoRequest.getUsuario(), rolesUsuario);

        // Creamos el registro de tipo de empleado
        Empleado empleado = new Empleado(usuarioGuardadoFinal);
        // Guardamos el empleado
        this.empleadoService.createEmpleado(empleado);
        //Horarios del empleado
        ArrayList<HorarioEmpleado> horariosEmpleadoCreado = new ArrayList<>();
        for (HorarioEmpleado horario : nuevoEmpleadoRequest.getHorarios()) {
            horariosEmpleadoCreado.add(new HorarioEmpleado(horario.getDia(), empleado, horario.getEntrada(), horario.getSalida()));
        }
        empleado.setHorarios(horariosEmpleadoCreado);
        // Guardamos el empleado
        this.empleadoService.createEmpleado(empleado);
        // Retornamos el usuario guardado
        return usuarioGuardadoFinal;
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
    private Usuario guardarUsuario(Usuario crear, List<RolUsuario> roles) throws Exception {
        if (this.usuarioRepository.existsByEmail(crear.getEmail())) {
            throw new Exception(String.format("El Email %s ya existe.", crear.getEmail()));
        }

        if (this.usuarioRepository.existsByPhone(crear.getPhone())) {
            throw new Exception("El numero de telefono ya existe.");
        }

        if (this.usuarioRepository.existsByCui(crear.getCui())) {
            throw new Exception("El CUI ya existe.");
        }

        if (crear.getNit() != null) {
            if (this.usuarioRepository.existsByNit(crear.getNit())) {
                throw new Exception("El nit ya existe.");
            }
        }

        // Asignamos un rol al usuario
        crear.setRoles(roles);
        // Encriptar la contraseña
        crear.setPassword(this.encriptador.encriptar(crear.getPassword()));
        // Guardar el usuario
        return this.usuarioRepository.save(crear);
    }

    @Transactional(rollbackOn = Exception.class)
    public void eliminarUsuarioById(Long id) throws Exception {
        Usuario usuario = this.usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new Exception("No se encontro el usuario");
        }
        this.usuarioRepository.deleteUsuarioById(id);
    }
}
