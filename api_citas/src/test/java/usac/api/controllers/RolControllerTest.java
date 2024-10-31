package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Rol;
import usac.api.models.request.RolCreateRequest;
import usac.api.models.request.RolPermisoUpdateRequest;
import usac.api.services.RolService;
import usac.api.services.permisos.ValidadorPermiso;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RolControllerTest {

    @InjectMocks
    private RolController rolController;

    @Mock
    private RolService rolService;

    @Mock
    private ValidadorPermiso validadorPermiso;

    @Mock
    private Rol mockRol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearRol() {
        try {
            // Setup
            RolCreateRequest crear = new RolCreateRequest();
            when(rolService.crearRol(any(RolCreateRequest.class))).thenReturn(mockRol);

            // Execute
            var response = rolController.crearRol(crear);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(rolService, times(1)).crearRol(crear);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testCrearRolError() {
        try {
            // Setup
            RolCreateRequest crear = new RolCreateRequest();
            when(rolService.crearRol(any(RolCreateRequest.class))).thenThrow(new RuntimeException("Error al crear rol"));

            // Execute
            var response = rolController.crearRol(crear);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testActualizarRol() {
        try {
            // Setup
            when(rolService.actualizarRol(any(Rol.class))).thenReturn(mockRol);

            // Execute
            var response = rolController.actualizarRol(mockRol);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(rolService, times(1)).actualizarRol(mockRol);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testActualizarRolError() {
        try {
            // Setup
            when(rolService.actualizarRol(any(Rol.class))).thenThrow(new RuntimeException("Error al actualizar rol"));

            // Execute
            var response = rolController.actualizarRol(mockRol);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testActualizarPermisosRol() {
        try {
            RolPermisoUpdateRequest update = new RolPermisoUpdateRequest();
            when(rolService.actualizarPermisosRol(update)).thenReturn(mockRol);

            // Execute
            var response = rolController.actualizarPermisosRol(update);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(rolService, times(1)).actualizarPermisosRol(update);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testActualizarPermisosRolError() {
        try {
            RolPermisoUpdateRequest update = new RolPermisoUpdateRequest();
            when(rolService.actualizarPermisosRol(update)).thenThrow(new RuntimeException("Error al actualizar permisos"));

            // Execute
            var response = rolController.actualizarPermisosRol(update);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetRolById() {
        try {
            Long id = 1L;
            when(rolService.getRolById(id)).thenReturn(mockRol);

            // Execute
            var response = rolController.getRolById(id);

            // Verify
            assertNotNull(response);
            verify(rolService, times(1)).getRolById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetRolByIdError() {
        try {
            Long id = 1L;
            when(rolService.getRolById(id)).thenThrow(new RuntimeException("Rol no encontrado"));

            // Execute
            var response = rolController.getRolById(id);

            // Verify
            assertNotNull(response);
            verify(rolService, times(1)).getRolById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetRolesGenericos() {
        try {
            List<Rol> roles = new ArrayList<>();
            roles.add(mockRol);
            when(rolService.getRoles()).thenReturn(roles);

            // Execute
            var response = rolController.getRolesGenericos();

            // Verify
            assertNotNull(response);
            verify(rolService, times(1)).getRoles();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetRolesGenericosError() {
        try {
            when(rolService.getRoles()).thenThrow(new RuntimeException("Error al obtener roles"));

            // Execute
            var response = rolController.getRolesGenericos();

            // Verify
            assertNotNull(response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetRolByNombre() {
        try {
            String nombre = "Admin";
            when(rolService.getRolByNombre(nombre)).thenReturn(mockRol);

            // Execute
            var response = rolController.getRolByNombre(nombre);

            // Verify
            assertNotNull(response);
            verify(rolService, times(1)).getRolByNombre(nombre);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetRolByNombreError() {
        try {
            String nombre = "Admin";
            when(rolService.getRolByNombre(nombre)).thenThrow(new RuntimeException("Rol no encontrado"));

            // Execute
            var response = rolController.getRolByNombre(nombre);

            // Verify
            assertNotNull(response);
            verify(rolService, times(1)).getRolByNombre(nombre);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
