/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services.authentification;

import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import usac.api.models.Rol;
import usac.api.models.RolUsuario;
import usac.api.models.Usuario;
import usac.api.repositories.UsuarioRepository;

/**
 *
 * @author Luis Monterroso
 */
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba para verificar que un usuario es cargado exitosamente por su
     * nombre de usuario.
     */
    @Test
    void testLoadUserByUsername_Success() {
        // Simulación de un usuario con roles
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");

        // Simulación de roles
        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ADMIN");

        RolUsuario rolUsuario = new RolUsuario();
        rolUsuario.setRol(rolAdmin);

        ArrayList<RolUsuario> roles = new ArrayList<>();
        roles.add(rolUsuario);
        usuario.setRoles(roles);

        // Simulamos la búsqueda del usuario en el repositorio
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));

        // Ejecutamos el método a probar
        UserDetails userDetails = authenticationService.loadUserByUsername("test@example.com");

        // Verificamos los resultados
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    /**
     * Prueba que verifica que se lanza UsernameNotFoundException cuando el
     * usuario no es encontrado.
     */
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Simulamos que no se encuentra el usuario
        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Ejecutamos el método y verificamos que lanza la excepción UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.loadUserByUsername("nonexistent@example.com");
        });

        // Verificamos que el repositorio fue llamado
        verify(usuarioRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
