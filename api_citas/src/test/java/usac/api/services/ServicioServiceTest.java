package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Empleado;
import usac.api.models.Rol;
import usac.api.models.Servicio;
import usac.api.repositories.RolRepository;
import usac.api.repositories.ServicioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import usac.api.services.EmpleadoService;
import usac.api.services.ServicioService;

class ServicioServiceTest {

    @InjectMocks
    private ServicioService servicioService;

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private EmpleadoService empleadoService;

    private Servicio servicio;
    private Rol rol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rol = new Rol();
        rol.setId(1L);

        servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Servicio de prueba");
        servicio.setRol(rol);
    }

    @Test
    void testGetServicioByIdExitoso() throws Exception {
        when(servicioRepository.findById(anyLong())).thenReturn(Optional.of(servicio));

        Servicio servicioObtenido = servicioService.getServicioById(1L);

        assertNotNull(servicioObtenido);
        assertEquals(servicio.getId(), servicioObtenido.getId());
        verify(servicioRepository, times(1)).findById(1L);
    }

    @Test
    void testGetServicioByIdNoEncontrado() {
        when(servicioRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> servicioService.getServicioById(1L));
        assertEquals("Informacion no encontrada.", exception.getMessage());
    }

    @Test
    void testGetServicios() {
        List<Servicio> servicios = List.of(servicio);
        when(servicioRepository.findAll()).thenReturn(servicios);

        List<Servicio> resultado = servicioService.getServicios();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(servicios.size(), resultado.size());
        verify(servicioRepository, times(1)).findAll();
    }

    @Test
    void testGetServiciosLikeNombre() {
        List<Servicio> servicios = List.of(servicio);
        when(servicioRepository.findByNombreContaining(anyString())).thenReturn(servicios);

        List<Servicio> resultado = servicioService.getServiciosLikeNombre("prueba");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(servicios.size(), resultado.size());
        verify(servicioRepository, times(1)).findByNombreContaining("prueba");
    }

    @Test
    void testEliminarServicioNoEncontrado() {
        when(servicioRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> servicioService.eliminarServicio(1L));
        assertEquals("No se encontro el servicio", exception.getMessage());
    }

    @Test
    void testActualizarServicioSinId() {
        Servicio servicioSinId = new Servicio();

        Exception exception = assertThrows(Exception.class, () -> servicioService.actualizarServicio(servicioSinId));
        assertEquals("El servicio no tiene un id valido", exception.getMessage());
    }

    @Test
    void testCrearServicioConIdExistente() {
        Exception exception = assertThrows(Exception.class, () -> servicioService.crearServicio(servicio));
        assertEquals("El servicio ya tiene un id", exception.getMessage());
    }

    @Test
    void testEmpleadosAsociadosAlServicioExitoso() throws Exception {
        List<Empleado> empleados = new ArrayList<>();
        when(servicioRepository.findById(anyLong())).thenReturn(Optional.of(servicio));
        when(empleadoService.getEmpleadosByRolId(anyLong())).thenReturn(empleados);

        List<Empleado> resultado = servicioService.empleadosAsociadosAlServicio(1L);

        assertNotNull(resultado);
        verify(empleadoService, times(1)).getEmpleadosByRolId(anyLong());
    }

    @Test
    void testEmpleadosAsociadosAlServicioNoEncontrado() {
        when(servicioRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> servicioService.empleadosAsociadosAlServicio(1L));
        assertEquals("No se encontro el servicio", exception.getMessage());
    }
}
