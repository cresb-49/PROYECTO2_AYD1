package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Negocio;
import usac.api.models.dto.NegocioPublicDTO;
import usac.api.models.dto.NegocioUpdateDTO;
import usac.api.models.HorarioNegocio;
import usac.api.services.B64Service;
import usac.api.services.NegocioService;
import usac.api.tools.transformers.ApiBaseTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NegocioControllerTest {

    @InjectMocks
    private NegocioController negocioController;

    @Mock
    private NegocioService negocioService;

    @Mock
    private B64Service b64Service;

    @Mock
    private Negocio mockNegocio;

    @Mock
    private NegocioUpdateDTO mockNegocioUpdateDTO;

    @Mock
    private List<HorarioNegocio> mockHorarios;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInfoNegocio() {
        try {
            // Setup
            when(negocioService.obtenerNegocio()).thenReturn(mockNegocio);
            when(b64Service.hasExtension(mockNegocio.getLogo())).thenReturn(false);
            when(b64Service.addExtension(mockNegocio.getLogo())).thenReturn("logoWithExtension");
            
            // Execute
            var response = negocioController.infoNegocio();
            
            // Verify
            assertNotNull(response);
            verify(negocioService, times(1)).obtenerNegocio();
            verify(b64Service, times(1)).hasExtension(mockNegocio.getLogo());
        } catch (Exception ex) {
            Logger.getLogger(NegocioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testInfoNegocioError() {
        try {
            // Setup
            when(negocioService.obtenerNegocio()).thenThrow(new RuntimeException("Error al obtener negocio"));
            
            // Execute
            var response = negocioController.infoNegocio();
            
            // Verify
            assertNotNull(response);
            verify(negocioService, times(1)).obtenerNegocio();
        } catch (Exception ex) {
            Logger.getLogger(NegocioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetNegocio() {
        try {
            // Setup
            when(negocioService.obtenerNegocio()).thenReturn(mockNegocio);
            when(b64Service.hasExtension(mockNegocio.getLogo())).thenReturn(false);
            when(b64Service.addExtension(mockNegocio.getLogo())).thenReturn("logoWithExtension");
            
            // Execute
            var response = negocioController.getNegocio();
            
            // Verify
            assertNotNull(response);
            verify(negocioService, times(1)).obtenerNegocio();
            verify(b64Service, times(1)).hasExtension(mockNegocio.getLogo());
        } catch (Exception ex) {
            Logger.getLogger(NegocioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetNegocioError() {
        try {
            // Setup
            when(negocioService.obtenerNegocio()).thenThrow(new RuntimeException("Error al obtener negocio"));
            
            // Execute
            var response = negocioController.getNegocio();
            
            // Verify
            assertNotNull(response);
            verify(negocioService, times(1)).obtenerNegocio();
        } catch (Exception ex) {
            Logger.getLogger(NegocioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testActualizarNegocio() {
        try {
            // Setup
            when(mockNegocioUpdateDTO.getNegocio()).thenReturn(mockNegocio);
            when(mockNegocioUpdateDTO.getHorarios()).thenReturn(mockHorarios);
            when(negocioService.actualizarNegocio(mockNegocio, mockHorarios)).thenReturn(mockNegocio);
            
            // Execute
            var response = negocioController.actualizarNegocio(mockNegocioUpdateDTO);
            
            // Verify
            assertNotNull(response);
            verify(negocioService, times(1)).actualizarNegocio(mockNegocio, mockHorarios);
        } catch (Exception ex) {
            Logger.getLogger(NegocioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testActualizarNegocioError() {
        try {
            // Setup
            when(mockNegocioUpdateDTO.getNegocio()).thenReturn(mockNegocio);
            when(mockNegocioUpdateDTO.getHorarios()).thenReturn(mockHorarios);
            when(negocioService.actualizarNegocio(mockNegocio, mockHorarios)).thenThrow(new RuntimeException("Error al actualizar negocio"));
            
            // Execute
            var response = negocioController.actualizarNegocio(mockNegocioUpdateDTO);
            
            // Verify
            assertNotNull(response);
            verify(negocioService, times(1)).actualizarNegocio(mockNegocio, mockHorarios);
        } catch (Exception ex) {
            Logger.getLogger(NegocioControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
