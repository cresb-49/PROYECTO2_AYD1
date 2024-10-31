package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.dto.ReservaDTO;
import usac.api.models.request.GetReservacionesRequest;
import usac.api.services.ReservaService;
import usac.api.services.permisos.ValidadorPermiso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReservaControllerTest {

    @InjectMocks
    private ReservaController reservaController;

    @Mock
    private ReservaService reservaService;

    @Mock
    private ValidadorPermiso validadorPermiso;

    @Mock
    private ArchivoDTO mockArchivoDTO;

    @Mock
    private ReservaDTO mockReservaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReservarServicio() {
        try {
            Long idReserva = 1L;
            when(reservaService.cancelarReserva(idReserva)).thenReturn(mockArchivoDTO);

            var response = reservaController.reservarServicio(idReserva);

            assertNotNull(response);
            verify(reservaService, times(1)).cancelarReserva(idReserva);
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testReservarServicioError() {
        try {
            Long idReserva = 1L;
            when(reservaService.cancelarReserva(idReserva)).thenThrow(new RuntimeException("Error al cancelar"));

            var response = reservaController.reservarServicio(idReserva);

            assertNotNull(response);
            verify(reservaService, times(1)).cancelarReserva(idReserva);
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testRealizarReserva() {
        try {
            Long reservaId = 1L;
            when(reservaService.realizarLaReserva(reservaId)).thenReturn(mockArchivoDTO);

            var response = reservaController.realizarReserva(reservaId);

            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(reservaService, times(1)).realizarLaReserva(reservaId);
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testRealizarReservaError() {
        try {
            Long reservaId = 1L;
            when(reservaService.realizarLaReserva(reservaId)).thenThrow(new RuntimeException("Error al realizar"));

            var response = reservaController.realizarReserva(reservaId);

            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetReservaByID() {
        try {
            Long reservaId = 1L;
            when(reservaService.obtenerReservaDTO(reservaId)).thenReturn(mockReservaDTO);

            var response = reservaController.getReservaByID(reservaId);

            assertNotNull(response);
            verify(reservaService, times(1)).obtenerReservaDTO(reservaId);
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetReservaByIDError() {
        try {
            Long reservaId = 1L;
            when(reservaService.obtenerReservaDTO(reservaId)).thenThrow(new RuntimeException("Reserva no encontrada"));

            var response = reservaController.getReservaByID(reservaId);

            assertNotNull(response);
            verify(reservaService, times(1)).obtenerReservaDTO(reservaId);
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetCitasDelMes() {
        try {
            Integer anio = 2024;
            Integer mes = 1;
            GetReservacionesRequest req = new GetReservacionesRequest(mes, anio);
            List<ReservaDTO> reservas = new ArrayList<>();
            reservas.add(mockReservaDTO);

            when(reservaService.getReservasDelMesResponse(any(GetReservacionesRequest.class))).thenReturn(reservas);

            var response = reservaController.getCitasDelMes(anio, mes);

            assertNotNull(response);
            ArgumentCaptor<GetReservacionesRequest> captor = ArgumentCaptor.forClass(GetReservacionesRequest.class);
            verify(reservaService, times(1)).getReservasDelMesResponse(captor.capture());

            // Verifica que el mes y el año sean correctos
            assertNotNull(captor.getValue());
            assertEquals(mes, captor.getValue().getMes());
            assertEquals(anio, captor.getValue().getAnio());
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetCitasDelMesError() {
        try {
            Integer anio = 2024;
            Integer mes = 1;
            GetReservacionesRequest req = new GetReservacionesRequest(mes, anio);
            when(reservaService.getReservasDelMesResponse(any(GetReservacionesRequest.class))).thenThrow(new RuntimeException("Error al obtener reservas"));

            var response = reservaController.getCitasDelMes(anio, mes);

            assertNotNull(response);
            ArgumentCaptor<GetReservacionesRequest> captor = ArgumentCaptor.forClass(GetReservacionesRequest.class);
            verify(reservaService, times(1)).getReservasDelMesResponse(captor.capture());
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testComprobanteReservaPorId() {
        try {
            Long id = 1L;
            when(reservaService.obtenerComprobanteReservaPorId(id)).thenReturn(mockArchivoDTO);

            var response = reservaController.comprobanteReservaPorId(id);

            assertNotNull(response);
            verify(reservaService, times(1)).obtenerComprobanteReservaPorId(id);
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testComprobanteReservaPorIdError() {
        try {
            Long id = 1L;
            when(reservaService.obtenerComprobanteReservaPorId(id)).thenThrow(new RuntimeException("Comprobante no encontrado"));

            var response = reservaController.comprobanteReservaPorId(id);

            assertNotNull(response);
            verify(reservaService, times(1)).obtenerComprobanteReservaPorId(id);
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testComprobanteReservaPorIdAdmin() {
        try {
            Long id = 1L;
            when(reservaService.obtenerComprobanteReservaPorId(id)).thenReturn(mockArchivoDTO);
            when(validadorPermiso.verificarPermiso()).thenReturn(null);

            var response = reservaController.comprobanteReservaPorIdAdmin(id);

            assertNotNull(response);
            verify(reservaService, times(1)).obtenerComprobanteReservaPorId(id);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testComprobanteReservaPorIdAdminError() {
        try {
            Long id = 1L;
            when(reservaService.obtenerComprobanteReservaPorId(id)).thenThrow(new RuntimeException("Comprobante no encontrado"));
            when(validadorPermiso.verificarPermiso()).thenReturn(null);

            var response = reservaController.comprobanteReservaPorIdAdmin(id);

            assertNotNull(response);
            verify(reservaService, times(1)).obtenerComprobanteReservaPorId(id);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            Logger.getLogger(ReservaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
