package usac.api.services;

import java.util.Arrays;
import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Validator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Permiso;
import usac.api.models.Rol;
import usac.api.models.request.RolCreateRequest;
import usac.api.models.request.RolPermisoUpdateRequest;
import usac.api.repositories.RolRepository;

import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase RolService.
 */
public class RolServiceTest {

    @InjectMocks
    private RolService rolService;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PermisoService permisoService;

    @Mock
    private Validator validator; // Se agrega el mock del validador

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializar los mocks
    }

    /**
     * Prueba para actualizar un rol exitosamente.
     */
    @Test
    void testActualizarRol_Success() throws Exception {
        // Crear un rol de prueba
        Rol rolMock = new Rol();
        rolMock.setId(1L);
        rolMock.setNombre("ROLE_USER");

        // Simular que el rol ya existe
        when(rolRepository.findById(rolMock.getId())).thenReturn(Optional.of(rolMock));

        // Simular que la validación es exitosa
        when(validator.validate(any())).thenReturn(new HashSet<>());

        // Simular que el nombre del rol no está duplicado
        when(rolRepository.existsByNombreAndIdNot(rolMock.getNombre(), rolMock.getId())).thenReturn(false);

        // Simular guardado exitoso
        when(rolRepository.save(any(Rol.class))).thenReturn(rolMock);

        // Ejecutar la actualización
        Rol rolActualizado = rolService.actualizarRol(rolMock);

        // Verificaciones
        assertNotNull(rolActualizado);
        assertEquals("ROLE_USER", rolActualizado.getNombre());
        verify(rolRepository, times(1)).save(rolMock);
    }

    /**
     * Prueba para lanzar excepción cuando el nombre del rol ya existe.
     */
    @Test
    void testActualizarRol_RolNombreYaExiste() throws Exception {
        // Crear un rol de prueba
        Rol rolMock = new Rol();
        rolMock.setId(1L);
        rolMock.setNombre("ROLE_ADMIN");

        // Simular que el rol ya existe
        when(rolRepository.findById(rolMock.getId())).thenReturn(Optional.of(rolMock));

        // Simular que la validación es exitosa
        when(validator.validate(any())).thenReturn(new HashSet<>());

        // Simular que ya existe un rol con el mismo nombre pero distinto ID
        when(rolRepository.existsByNombreAndIdNot(rolMock.getNombre(), rolMock.getId())).thenReturn(true);

        // Ejecutar la actualización y verificar que lanza la excepción
        Exception exception = assertThrows(Exception.class, () -> rolService.actualizarRol(rolMock));
        assertEquals("Ya existe un rol con el nombre especificado.", exception.getMessage());

        // Verificar que no se haya intentado guardar el rol
        verify(rolRepository, times(0)).save(any(Rol.class));
    }

    /**
     * Prueba para lanzar excepción cuando el rol no es encontrado.
     */
    @Test
    void testActualizarRol_RolNoEncontrado() throws Exception {
        // Crear un rol de prueba
        Rol rolMock = new Rol();
        rolMock.setId(1L);

        // Simular que el rol no existe
        when(rolRepository.findById(rolMock.getId())).thenReturn(Optional.empty());

        // Ejecutar la actualización y verificar que lanza la excepción
        Exception exception = assertThrows(Exception.class, () -> rolService.actualizarRol(rolMock));
        assertEquals("Informacion no encontrada.", exception.getMessage());

        // Verificar que no se haya intentado guardar el rol
        verify(rolRepository, times(0)).save(any(Rol.class));
    }

    /**
     * Prueba para lanzar excepción cuando el rol no pasa la validación del
     * modelo.
     */
    @Test
    void testActualizarRol_ValidacionFallida() throws Exception {
        // Crear un rol de prueba
        Rol rolMock = new Rol();
        rolMock.setId(1L);

        // Simular que hay violaciones en la validación
        when(validator.validate(any())).thenReturn(Set.of(mock(javax.validation.ConstraintViolation.class)));

        // Ejecutar la actualización y verificar que lanza la excepción
        Exception exception = assertThrows(Exception.class, () -> rolService.actualizarRol(rolMock));

        assertNotNull(exception);

        // Verificar que no se haya intentado guardar el rol
        verify(rolRepository, times(0)).save(any(Rol.class));
    }

    /**
     * Prueba para crear un nuevo rol con permisos.
     */
    @Test
    void testCrearRol() throws Exception {
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("NEW_ROLE");

        Permiso permiso1 = new Permiso();
        permiso1.setId(1L);
        Permiso permiso2 = new Permiso();
        permiso2.setId(2L);

        List<Permiso> permisos = List.of(permiso1, permiso2);

        RolCreateRequest request = new RolCreateRequest();
        request.setRol(rol);
        request.setPermisos(permisos);

        // Simular que la validación es exitosa
        when(validator.validate(any())).thenReturn(new HashSet<>());

        when(rolRepository.save(any(Rol.class))).thenReturn(rol);
        when(permisoService.getPermisoById(any(Permiso.class))).thenReturn(permiso1, permiso2);

        Rol result = rolService.crearRol(request);

        // Verificar que el rol fue guardado correctamente
        assertNotNull(result);
        assertEquals("NEW_ROLE", result.getNombre());
        verify(rolRepository, times(2)).save(rol); // Dos veces: al crear y al agregar permisos
    }

    /**
     * Prueba para actualizar los permisos de un rol.
     */
    @Test
    void testActualizarPermisosRol() throws Exception {
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("EXISTING_ROLE");

        Permiso permiso1 = new Permiso();
        permiso1.setId(1L);
        Permiso permiso2 = new Permiso();
        permiso2.setId(2L);

        List<Permiso> permisos = List.of(permiso1, permiso2);
        RolPermisoUpdateRequest updateRequest = new RolPermisoUpdateRequest();
        updateRequest.setIdRol(1L);
        updateRequest.setPermisos(permisos);

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(permisoService.getPermisoById(any(Permiso.class))).thenReturn(permiso1, permiso2);

        // Simular que la validación es exitosa
        when(validator.validate(any())).thenReturn(new HashSet<>());
        when(rolRepository.save(rol)).thenReturn(rol);

        Rol result = rolService.actualizarPermisosRol(updateRequest);

        // Verificar que los permisos fueron actualizados
        assertNotNull(result);
        assertEquals(2, result.getPermisosRol().size());
        verify(rolRepository, times(1)).save(rol);
    }

    /**
     * Prueba para verificar que se lanza excepción cuando el rol no es
     * encontrado.
     */
    @Test
    void testActualizarPermisosRol_RolNoEncontrado() throws Exception {
        RolPermisoUpdateRequest updateRequest = new RolPermisoUpdateRequest();
        updateRequest.setIdRol(1L);

        when(rolRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> rolService.actualizarPermisosRol(updateRequest));

        assertEquals("Informacion no encontrada.", exception.getMessage());
    }

    /**
     * Prueba para verificar que se obtienen los roles correctamente, excluyendo
     * los roles de admin, empleado, y cliente.
     */
    @Test
    void testGetRoles_Success() throws Exception {
        // Crear una lista de roles de prueba
        Rol rol1 = new Rol();
        rol1.setId(1L);
        rol1.setNombre("ROLE_USER");

        Rol rol2 = new Rol();
        rol2.setId(2L);
        rol2.setNombre("ROLE_MANAGER");

        List<Rol> rolesMock = Arrays.asList(rol1, rol2);

        // Simular que el repositorio devuelve los roles excluyendo los específicos
        when(rolRepository.findAllExcludingAdminEmpleadoCliente()).thenReturn(rolesMock);

        // Ejecutar el método
        List<Rol> result = rolService.getRoles();

        // Verificar que los roles retornados no son vacíos y son los esperados
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(rol -> rol.getNombre().equals("ADMIN")));
        assertTrue(result.stream().noneMatch(rol -> rol.getNombre().equals("CLIENTE")));
        assertTrue(result.stream().noneMatch(rol -> rol.getNombre().equals("EMPLEADO")));

        // Verificar que se llamó al método findAllExcludingAdminEmpleadoCliente en el repositorio
        verify(rolRepository, times(1)).findAllExcludingAdminEmpleadoCliente();
    }

    /**
     * Prueba para verificar que se lanza una excepción cuando ocurre un error
     * al obtener los roles.
     */
    @Test
    void testGetRoles_Exception() throws Exception {
        // Simular que el repositorio lanza una RuntimeException
        when(rolRepository.findAllExcludingAdminEmpleadoCliente()).thenThrow(new RuntimeException("Error al obtener roles"));

        // Ejecutar el método y verificar que lanza una excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> rolService.getRoles());

        // Verificar el mensaje de la excepción
        assertEquals("Error al obtener roles", exception.getMessage());

        // Verificar que se llamó al método findAllExcludingAdminEmpleadoCliente en el repositorio
        verify(rolRepository, times(1)).findAllExcludingAdminEmpleadoCliente();
    }
}
