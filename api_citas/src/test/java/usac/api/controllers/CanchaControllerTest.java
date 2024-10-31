package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Cancha;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.request.CanchaRequest;
import usac.api.models.request.ReservacionCanchaRequest;
import usac.api.services.CanchaService;
import usac.api.services.ReservaService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CanchaControllerTest {

    @InjectMocks
    private CanchaController canchaController;

    @Mock
    private CanchaService canchaService;

    @Mock
    private ReservaService reservaService;

    @Mock
    private Cancha mockCancha;

    @Mock
    private ArchivoDTO mockArchivoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCanchas() {
        // Setup
        List<Cancha> canchas = new ArrayList<>();
        canchas.add(mockCancha);
        when(canchaService.getCanchas()).thenReturn(canchas);

        // Execute
        var response = canchaController.getCanchas();

        // Verify
        assertNotNull(response);
        verify(canchaService, times(1)).getCanchas();
    }

    @Test
    void testGetCanchaById() {
        try {
            // Setup
            Long id = 1L;
            when(canchaService.getCanchaById(id)).thenReturn(mockCancha);

            // Execute
            var response = canchaController.getCanchaById(id);

            // Verify
            assertNotNull(response);
            verify(canchaService, times(1)).getCanchaById(id);
        } catch (Exception ex) {
            Logger.getLogger(CanchaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCreateCancha() {
        try {
            // Setup
            CanchaRequest canchaRequest = new CanchaRequest();
            canchaRequest.setCancha(mockCancha);
            canchaRequest.setHorarios(new ArrayList<>());

            when(canchaService.crearCancha(any(Cancha.class), anyList())).thenReturn(mockCancha);

            // Execute
            var response = canchaController.createCancha(canchaRequest);

            // Verify
            assertNotNull(response);
            verify(canchaService, times(1)).crearCancha(any(Cancha.class), anyList());
        } catch (Exception ex) {
            Logger.getLogger(CanchaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testDeleteCanchaById() {
        try {
            // Setup
            Long id = 1L;

            // Execute
            var response = canchaController.deleteCanchaById(id);

            // Verify
            assertNotNull(response);
            verify(canchaService, times(1)).deleteCanchaById(id);
        } catch (Exception ex) {
            Logger.getLogger(CanchaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testUpdateCancha() {
        try {
            // Setup
            CanchaRequest canchaRequest = new CanchaRequest();
            canchaRequest.setCancha(mockCancha);
            canchaRequest.setHorarios(new ArrayList<>());

            when(canchaService.actualizarCancha(any(Cancha.class), anyList())).thenReturn(mockCancha);

            // Execute
            var response = canchaController.updateCancha(canchaRequest);

            // Verify
            assertNotNull(response);
            verify(canchaService, times(1)).actualizarCancha(any(Cancha.class), anyList());
        } catch (Exception ex) {
            Logger.getLogger(CanchaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testReservarCancha() {
        try {
            // Setup
            ReservacionCanchaRequest reservacionCanchaRequest = new ReservacionCanchaRequest();
            when(reservaService.reservaCancha(reservacionCanchaRequest)).thenReturn(mockArchivoDTO);

            // Execute
            var response = canchaController.reservarCancha(reservacionCanchaRequest);

            // Verify
            assertNotNull(response);
            verify(reservaService, times(1)).reservaCancha(reservacionCanchaRequest);
        } catch (Exception ex) {
            Logger.getLogger(CanchaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetCanchaByIdNotFound() {
        try {
            // Setup
            Long id = 2L; // Assuming this ID does not exist
            when(canchaService.getCanchaById(id)).thenThrow(new RuntimeException("Cancha no encontrada"));

            // Execute
            var response = canchaController.getCanchaById(id);

            // Verify
            assertNotNull(response);
            verify(canchaService, times(1)).getCanchaById(id);
        } catch (Exception ex) {
            Logger.getLogger(CanchaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCreateCanchaError() {
        try {
            // Setup
            CanchaRequest canchaRequest = new CanchaRequest();
            canchaRequest.setCancha(mockCancha);
            canchaRequest.setHorarios(new ArrayList<>());

            when(canchaService.crearCancha(any(Cancha.class), anyList())).thenThrow(new RuntimeException("Error al crear cancha"));

            // Execute
            var response = canchaController.createCancha(canchaRequest);

            // Verify
            assertNotNull(response);
            verify(canchaService, times(1)).crearCancha(any(Cancha.class), anyList());
        } catch (Exception ex) {
            Logger.getLogger(CanchaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
