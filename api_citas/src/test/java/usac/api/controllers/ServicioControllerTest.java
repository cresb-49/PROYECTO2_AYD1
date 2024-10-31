package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Empleado;
import usac.api.models.Servicio;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.request.ReservacionServicioRequest;
import usac.api.services.ReservaService;
import usac.api.services.ServicioService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ServicioControllerTest {

    @InjectMocks
    private ServicioController servicioController;

    @Mock
    private ServicioService servicioService;

    @Mock
    private ReservaService reservaService;

    @Mock
    private ArchivoDTO mockArchivoDTO;

    @Mock
    private Servicio mockServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllServicios() {
        try {
            List<Servicio> servicios = new ArrayList<>();
            servicios.add(mockServicio);
            when(servicioService.getServicios()).thenReturn(servicios);

            var response = servicioController.getAllServicios();

            assertNotNull(response);
            verify(servicioService, times(1)).getServicios();
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetAllServiciosError() {
        try {
            when(servicioService.getServicios()).thenThrow(new RuntimeException("Error al obtener servicios"));

            var response = servicioController.getAllServicios();

            assertNotNull(response);
            verify(servicioService, times(1)).getServicios();
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetServicioById() {
        try {
            Long id = 1L;
            when(servicioService.getServicioById(id)).thenReturn(mockServicio);

            var response = servicioController.getServicioById(id);

            assertNotNull(response);
            verify(servicioService, times(1)).getServicioById(id);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetServicioByIdError() {
        try {
            Long id = 1L;
            when(servicioService.getServicioById(id)).thenThrow(new RuntimeException("Servicio no encontrado"));

            var response = servicioController.getServicioById(id);

            assertNotNull(response);
            verify(servicioService, times(1)).getServicioById(id);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetServiciosLikeNombre() {
        try {
            String nombre = "Servicio1";
            List<Servicio> servicios = new ArrayList<>();
            servicios.add(mockServicio);
            when(servicioService.getServiciosLikeNombre(nombre)).thenReturn(servicios);

            var response = servicioController.getServiciosLikeNombre(nombre);

            assertNotNull(response);
            verify(servicioService, times(1)).getServiciosLikeNombre(nombre);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetServiciosLikeNombreError() {
        try {
            String nombre = "Servicio1";
            when(servicioService.getServiciosLikeNombre(nombre)).thenThrow(new RuntimeException("Error al buscar servicios"));

            var response = servicioController.getServiciosLikeNombre(nombre);

            assertNotNull(response);
            verify(servicioService, times(1)).getServiciosLikeNombre(nombre);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testDeleteServicio() {
        try {
            Long id = 1L;

            var response = servicioController.deleteServicio(id);

            assertNotNull(response);
            verify(servicioService, times(1)).eliminarServicio(id);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testDeleteServicioError() {
        try {
            Long id = 1L;
            doThrow(new RuntimeException("Error al eliminar servicio")).when(servicioService).eliminarServicio(id);

            var response = servicioController.deleteServicio(id);

            assertNotNull(response);
            verify(servicioService, times(1)).eliminarServicio(id);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testUpdateServicio() {
        try {
            when(servicioService.actualizarServicio(mockServicio)).thenReturn(mockServicio);

            var response = servicioController.updateServicio(mockServicio);

            assertNotNull(response);
            verify(servicioService, times(1)).actualizarServicio(mockServicio);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testUpdateServicioError() {
        try {
            when(servicioService.actualizarServicio(mockServicio)).thenThrow(new RuntimeException("Error al actualizar servicio"));

            var response = servicioController.updateServicio(mockServicio);

            assertNotNull(response);
            verify(servicioService, times(1)).actualizarServicio(mockServicio);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCreateServicio() {
        try {
            when(servicioService.crearServicio(mockServicio)).thenReturn(mockServicio);

            var response = servicioController.createServicio(mockServicio);

            assertNotNull(response);
            verify(servicioService, times(1)).crearServicio(mockServicio);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCreateServicioError() {
        try {
            when(servicioService.crearServicio(mockServicio)).thenThrow(new RuntimeException("Error al crear servicio"));

            var response = servicioController.createServicio(mockServicio);

            assertNotNull(response);
            verify(servicioService, times(1)).crearServicio(mockServicio);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testReservarServicio() {
        try {
            ReservacionServicioRequest request = new ReservacionServicioRequest();
            when(reservaService.reservaServicio(request)).thenReturn(mockArchivoDTO);

            var response = servicioController.reservarServicio(request);

            assertNotNull(response);
            verify(reservaService, times(1)).reservaServicio(request);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testReservarServicioError() {
        try {
            ReservacionServicioRequest request = new ReservacionServicioRequest();
            when(reservaService.reservaServicio(request)).thenThrow(new RuntimeException("Error al reservar servicio"));

            var response = servicioController.reservarServicio(request);

            assertNotNull(response);
            verify(reservaService, times(1)).reservaServicio(request);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testEmpleadosAsociadosAlServicio() {
        try {
            Long id = 1L;
            List<Empleado> empleados = new ArrayList<>();
            empleados.add(new Empleado());
            when(servicioService.empleadosAsociadosAlServicio(id)).thenReturn(empleados);

            var response = servicioController.empleadosAsociadosAlServicio(id);

            assertNotNull(response);
            verify(servicioService, times(1)).empleadosAsociadosAlServicio(id);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testEmpleadosAsociadosAlServicioError() {
        try {
            Long id = 1L;
            when(servicioService.empleadosAsociadosAlServicio(id)).thenThrow(new RuntimeException("Error al obtener empleados"));

            var response = servicioController.empleadosAsociadosAlServicio(id);

            assertNotNull(response);
            verify(servicioService, times(1)).empleadosAsociadosAlServicio(id);
        } catch (Exception ex) {
            Logger.getLogger(ServicioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
