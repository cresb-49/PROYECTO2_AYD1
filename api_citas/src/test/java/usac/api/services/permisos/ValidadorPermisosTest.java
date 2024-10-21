package usac.api.services.permisos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import usac.api.models.Permiso;
import usac.api.models.RolPermiso;
import usac.api.models.Usuario;
import usac.api.services.UsuarioService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import usac.api.models.Rol;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import usac.api.models.RolUsuario;

/**
 * Clase de pruebas unitarias para {@link ValidadorPermiso}. Se verifican los
 * permisos de acceso a rutas protegidas por rol de usuario.
 */
public class ValidadorPermisosTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ValidadorPermiso validadorPermiso;

    private List<RolPermiso> permisosRol;

    /**
     * Configuración inicial antes de cada prueba. Se inicializan los mocks y
     * los datos de prueba.
     */
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Inicializamos el SecurityContext y lo asignamos al contexto actual
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Inicializamos los permisos
        permisosRol = new ArrayList<>();
    }

    /**
     * Verifica que un usuario con el rol "AYUDANTE" tenga el permiso para
     * acceder a una ruta específica.
     */
    @Test
    void testVerificarPermisoAyudanteConPermiso() throws Exception {
        // Crear un nuevo usuario y asignarle rol y permisos
        Usuario usuario = new Usuario();
        Rol rol = new Rol();

        Permiso permiso = new Permiso();
        permiso.setRuta("/api/test");
        RolPermiso rolPermiso = new RolPermiso(null, permiso);
        permisosRol.add(rolPermiso);
        rol.setPermisosRol(permisosRol);
        List<RolUsuario> roles = new ArrayList<>();
        roles.add(new RolUsuario(usuario, rol));
        usuario.setRoles(roles);

        // Simular que el usuario es un ayudante
        when(authentication.getAuthorities()).thenReturn(mockAuthorities("ROLE_AYUDANTE"));
        when(request.getRequestURI()).thenReturn("/api/test/permiso");
        when(validadorPermiso.getUsuarioPorJwt()).thenReturn(usuario);

        // Ejecutar el método
        assertTrue(validadorPermiso.verificarPermiso());
    }

    /**
     * Verifica que se lance una excepción cuando un usuario con el rol
     * "AYUDANTE" no tiene el permiso para acceder a una ruta.
     */
    @Test
    void testVerificarPermisoAyudanteSinPermiso() throws Exception {
        // Crear un nuevo usuario y asignarle rol sin permisos
        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        rol.setPermisosRol(permisosRol);
        List<RolUsuario> roles = new ArrayList<>();
        roles.add(new RolUsuario(usuario, rol));

        usuario.setRoles(roles);

        // Simular que el usuario es un ayudante
        when(authentication.getAuthorities()).thenReturn(mockAuthorities("ROLE_AYUDANTE"));
        when(request.getRequestURI()).thenReturn("/api/test/permiso");
        when(validadorPermiso.getUsuarioPorJwt()).thenReturn(usuario);

        // Ejecutar el método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> validadorPermiso.verificarPermiso());
        assertEquals("Sin permisos", exception.getMessage());
    }

    /**
     * Verifica que un usuario con un rol distinto a "AYUDANTE" pueda acceder
     * sin restricciones a la ruta.
     */
    @Test
    void testVerificarPermisoNoAyudante() throws Exception {
        // Crear un nuevo usuario y asignarle rol sin permisos
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@usuario");
        Rol rol = new Rol();
        List<RolUsuario> roles = new ArrayList<>();
        roles.add(new RolUsuario(usuario, rol));
        usuario.setRoles(roles);

        // Simular que el usuario no es un ayudante
        when(usuarioService.isUserAdmin(usuario.getEmail())).thenReturn(true);
        when(validadorPermiso.getUsuarioPorJwt()).thenReturn(usuario);
        // Ejecutar el método
        assertTrue(validadorPermiso.verificarPermiso());
    }

    /**
     * Verifica que el usuario pueda ser obtenido correctamente desde el JWT
     * utilizando el método {@link ValidadorPermiso#getUsuarioPorJwt()}.
     */
    @Test
    void testGetUsuarioPorJwt() throws Exception {
        // Crear un nuevo usuario
        Usuario usuario = new Usuario();

        when(authentication.getName()).thenReturn("test@example.com");
        when(usuarioService.getByEmail(anyString())).thenReturn(usuario);

        // Ejecutar el método
        Usuario usuarioEncontrado = validadorPermiso.getUsuarioPorJwt();
        assertNotNull(usuarioEncontrado);
        verify(usuarioService, times(1)).getByEmail("test@example.com");
    }

    /**
     * Método auxiliar para simular los roles del usuario autenticado.
     *
     * @param role El rol que se desea simular.
     * @return Una colección de autoridades simuladas con el rol proporcionado.
     */
    private Collection mockAuthorities(String role) {
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        return java.util.Collections.singletonList(authority); // Utilizamos Collections.singletonList para simular una lista con una autoridad
    }
}
