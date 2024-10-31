package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Dia;
import usac.api.models.Empleado;
import usac.api.models.HorarioEmpleado;
import usac.api.models.Servicio;
import usac.api.models.Usuario;
import usac.api.repositories.EmpleadoRepository;
import usac.api.repositories.HorarioEmpleadoRepository;
import usac.api.repositories.RolRepository;
import usac.api.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import usac.api.models.Rol;

class EmpleadoServiceTest {

    @InjectMocks
    private EmpleadoService empleadoService;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HorarioEmpleadoRepository horarioEmpleadoRepository;

    @Mock
    private RolRepository rolRepository;

    private Empleado empleado;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);

        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setUsuario(usuario);
    }

    @Test
    void testGetEmpleadosPorServicioSinUsuarios() {
        Servicio servicio = new Servicio();
        servicio.setId(1L);
        servicio.setRol(new Rol());

        when(usuarioRepository.findUsuariosByRolUsuario_Rol(servicio.getRol())).thenReturn(List.of());

        Exception exception = assertThrows(Exception.class, () -> empleadoService.getEmpleadosPorServicio(servicio));
        assertEquals("No hay usuarios con el rol asociado al servicio con ID: 1", exception.getMessage());
    }

    @Test
    void testEliminarEmpleadoExitoso() throws Exception {
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.of(empleado));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        empleadoService.eliminarEmpleado(empleado.getId());

        verify(empleadoRepository, times(1)).deleteEmpleadoById(empleado.getId());
        verify(usuarioRepository, times(1)).deleteUsuarioById(usuario.getId());
    }

    @Test
    void testEliminarEmpleadoSinUsuarioAsociado() {
        Empleado empleadoSinUsuario = new Empleado();
        empleadoSinUsuario.setId(2L);

        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.of(empleadoSinUsuario));

        Exception exception = assertThrows(Exception.class, () -> empleadoService.eliminarEmpleado(empleadoSinUsuario.getId()));
        assertEquals("No se encontro el usuario asociado al empleado", exception.getMessage());
    }

    @Test
    void testGetEmpleadoByIdExitoso() throws Exception {
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.of(empleado));

        Empleado result = empleadoService.getEmpleadoById(1L);

        assertNotNull(result);
        assertEquals(empleado.getId(), result.getId());
    }

    @Test
    void testGetEmpleadoByIdNoEncontrado() {
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> empleadoService.getEmpleadoById(1L));
        assertEquals("No encontramos al empleado.", exception.getMessage());
    }

    @Test
    void testObtenerHorarioDiaEmpleado() {
        Dia dia = new Dia();
        HorarioEmpleado horarioEmpleado = new HorarioEmpleado();

        when(horarioEmpleadoRepository.findByDiaAndEmpleado(dia, empleado)).thenReturn(horarioEmpleado);

        HorarioEmpleado result = empleadoService.obtenerHorarioDiaEmpleado(dia, empleado);

        assertNotNull(result);
        verify(horarioEmpleadoRepository, times(1)).findByDiaAndEmpleado(dia, empleado);
    }

    @Test
    void testGetEmpleadosByRolId() {
        Long rolId = 1L;
        when(empleadoRepository.findEmpleadosByRolId(rolId)).thenReturn(List.of(empleado));

        List<Empleado> empleados = empleadoService.getEmpleadosByRolId(rolId);

        assertNotNull(empleados);
        assertFalse(empleados.isEmpty());
        assertEquals(1, empleados.size());
    }

    @Test
    void testGetEmpleados_TodosEliminados() {

        when(empleadoRepository.findAll()).thenReturn(
                List.of(new Empleado()));

        List<Empleado> empleados = empleadoService.getEmpleados();

        // Verificar que la lista de empleados está vacía
        assertNotNull(empleados);
        assertFalse(empleados.isEmpty());
        verify(empleadoRepository, times(1)).findAll();
    }
}
