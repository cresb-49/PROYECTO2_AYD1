package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Empleado;
import usac.api.models.Usuario;
import usac.api.models.dto.LoginDTO;
import usac.api.models.request.NuevoEmpleadoRequest;
import usac.api.models.request.PasswordChangeRequest;
import usac.api.models.request.UpdateEmpleadoRequest;
import usac.api.models.request.UserChangePasswordRequest;
import usac.api.models.request.UsuarioRolAsignacionRequest;
import usac.api.services.UsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Usuario mockUsuario;

    @Mock
    private LoginDTO mockLoginDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsuario() {
        try {
            Long id = 1L;
            when(usuarioService.getUsuario(id)).thenReturn(mockUsuario);

            var response = usuarioController.getUsuario(id);

            assertNotNull(response);
            verify(usuarioService, times(1)).getUsuario(id);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetUsuarioError() {
        try {
            Long id = 1L;
            when(usuarioService.getUsuario(id)).thenThrow(new RuntimeException("Usuario no encontrado"));

            var response = usuarioController.getUsuario(id);

            assertNotNull(response);
            verify(usuarioService, times(1)).getUsuario(id);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetPerfil() {
        try {
            Long id = 1L;
            when(usuarioService.getPerfil(id)).thenReturn(mockUsuario);

            var response = usuarioController.getPerfil(id);

            assertNotNull(response);
            verify(usuarioService, times(1)).getPerfil(id);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetPerfilError() {
        try {
            Long id = 1L;
            when(usuarioService.getPerfil(id)).thenThrow(new RuntimeException("Perfil no encontrado"));

            var response = usuarioController.getPerfil(id);

            assertNotNull(response);
            verify(usuarioService, times(1)).getPerfil(id);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testMailDeRecuperacion() {
        try {
            HashMap<String, Object> requestBody = new HashMap<>();
            requestBody.put("correoElectronico", "test@example.com");
            String mensaje = "Correo enviado exitosamente";
            when(usuarioService.enviarMailDeRecuperacion("test@example.com")).thenReturn(mensaje);

            var response = usuarioController.mailDeRecupeacion(requestBody);

            assertNotNull(response);
            verify(usuarioService, times(1)).enviarMailDeRecuperacion("test@example.com");
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testMailDeRecuperacionError() {
        try {
            HashMap<String, Object> requestBody = new HashMap<>();
            requestBody.put("correoElectronico", "test@example.com");
            when(usuarioService.enviarMailDeRecuperacion("test@example.com")).thenThrow(new RuntimeException("Error al enviar correo"));

            var response = usuarioController.mailDeRecupeacion(requestBody);

            assertNotNull(response);
            verify(usuarioService, times(1)).enviarMailDeRecuperacion("test@example.com");
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testRecuperarPassword() {
        try {
            PasswordChangeRequest request = new PasswordChangeRequest();
            String respuesta = "Contraseña recuperada exitosamente";
            when(usuarioService.recuperarPassword(request)).thenReturn(respuesta);

            var response = usuarioController.recuperarPassword(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).recuperarPassword(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testRecuperarPasswordError() {
        try {
            PasswordChangeRequest request = new PasswordChangeRequest();
            when(usuarioService.recuperarPassword(request)).thenThrow(new RuntimeException("Error al recuperar contraseña"));

            var response = usuarioController.recuperarPassword(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).recuperarPassword(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCambiarPassword() {
        try {
            UserChangePasswordRequest request = new UserChangePasswordRequest();
            String respuesta = "Contraseña cambiada exitosamente";
            when(usuarioService.cambiarPassword(request)).thenReturn(respuesta);

            var response = usuarioController.cambiarPassword(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).cambiarPassword(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCambiarPasswordError() {
        try {
            UserChangePasswordRequest request = new UserChangePasswordRequest();
            when(usuarioService.cambiarPassword(request)).thenThrow(new RuntimeException("Error al cambiar contraseña"));

            var response = usuarioController.cambiarPassword(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).cambiarPassword(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testActualizarUsuarioSinPassword() {
        try {
            Usuario updates = new Usuario();
            when(usuarioService.updateUsuario(updates)).thenReturn(mockUsuario);

            var response = usuarioController.actualizarUsuarioSinPassword(updates);

            assertNotNull(response);
            verify(usuarioService, times(1)).updateUsuario(updates);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testActualizarUsuarioSinPasswordError() {
        try {
            Usuario updates = new Usuario();
            when(usuarioService.updateUsuario(updates)).thenThrow(new RuntimeException("Error al actualizar usuario"));

            var response = usuarioController.actualizarUsuarioSinPassword(updates);

            assertNotNull(response);
            verify(usuarioService, times(1)).updateUsuario(updates);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetUsuarios() {
        try {
            List<Usuario> usuarios = List.of(mockUsuario);
            when(usuarioService.getUsuarios()).thenReturn(usuarios);

            var response = usuarioController.getUsuarios();

            assertNotNull(response);
            verify(usuarioService, times(1)).getUsuarios();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetUsuariosError() {
        try {
            when(usuarioService.getUsuarios()).thenThrow(new RuntimeException("Error al obtener usuarios"));

            var response = usuarioController.getUsuarios();

            assertNotNull(response);
            verify(usuarioService, times(1)).getUsuarios();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCrearAdministrador() {
        try {
            when(usuarioService.crearAdministrador(mockUsuario)).thenReturn(mockUsuario);

            var response = usuarioController.crearAdministrador(mockUsuario);

            assertNotNull(response);
            verify(usuarioService, times(1)).crearAdministrador(mockUsuario);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCrearAdministradorError() {
        try {
            when(usuarioService.crearAdministrador(mockUsuario)).thenThrow(new RuntimeException("Error al crear administrador"));

            var response = usuarioController.crearAdministrador(mockUsuario);

            assertNotNull(response);
            verify(usuarioService, times(1)).crearAdministrador(mockUsuario);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCrearEmpleado() {
        try {
            NuevoEmpleadoRequest request = new NuevoEmpleadoRequest();
            when(usuarioService.crearEmpleado(request)).thenReturn(mockUsuario);

            var response = usuarioController.crearEmpleado(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).crearEmpleado(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCrearEmpleadoError() {
        try {
            NuevoEmpleadoRequest request = new NuevoEmpleadoRequest();
            when(usuarioService.crearEmpleado(request)).thenThrow(new RuntimeException("Error al crear empleado"));

            var response = usuarioController.crearEmpleado(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).crearEmpleado(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testActualizarEmpleado() {
        try {
            UpdateEmpleadoRequest request = new UpdateEmpleadoRequest();
            when(usuarioService.actualizarEmpleado(request)).thenReturn(new Empleado());

            var response = usuarioController.actualizarEmpleado(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).actualizarEmpleado(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testActualizarEmpleadoError() {
        try {
            UpdateEmpleadoRequest request = new UpdateEmpleadoRequest();
            when(usuarioService.actualizarEmpleado(request)).thenThrow(new RuntimeException("Error al actualizar empleado"));

            var response = usuarioController.actualizarEmpleado(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).actualizarEmpleado(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testLogin() {
        try {
            when(usuarioService.iniciarSesion(mockUsuario)).thenReturn(mockLoginDTO);

            var response = usuarioController.login(mockUsuario);

            assertNotNull(response);
            verify(usuarioService, times(1)).iniciarSesion(mockUsuario);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testLoginError() {
        try {
            when(usuarioService.iniciarSesion(mockUsuario)).thenThrow(new RuntimeException("Error al iniciar sesión"));

            var response = usuarioController.login(mockUsuario);

            assertNotNull(response);
            verify(usuarioService, times(1)).iniciarSesion(mockUsuario);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testAsignarRolAUsuario() {
        try {
            UsuarioRolAsignacionRequest request = new UsuarioRolAsignacionRequest();
            when(usuarioService.updateRoles(request)).thenReturn(mockUsuario);

            var response = usuarioController.asignarRolAUsuario(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).updateRoles(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testAsignarRolAUsuarioError() {
        try {
            UsuarioRolAsignacionRequest request = new UsuarioRolAsignacionRequest();
            when(usuarioService.updateRoles(request)).thenThrow(new RuntimeException("Error al asignar rol"));

            var response = usuarioController.asignarRolAUsuario(request);

            assertNotNull(response);
            verify(usuarioService, times(1)).updateRoles(request);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testEliminarUsuario() {
        try {
            Long id = 1L;
            usuarioController.eliminarUsuario(id);

            verify(usuarioService, times(1)).eliminarUsuarioById(id);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testEliminarUsuarioError() {
        try {
            Long id = 1L;
            doThrow(new RuntimeException("Error al eliminar usuario")).when(usuarioService).eliminarUsuarioById(id);

            var response = usuarioController.eliminarUsuario(id);

            assertNotNull(response);
            verify(usuarioService, times(1)).eliminarUsuarioById(id);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
