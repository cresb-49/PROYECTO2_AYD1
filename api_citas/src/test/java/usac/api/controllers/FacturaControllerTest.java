package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Factura;
import usac.api.models.dto.ArchivoDTO;
import usac.api.services.FacturaService;
import usac.api.services.permisos.ValidadorPermiso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class FacturaControllerTest {

    @InjectMocks
    private FacturaController facturaController;

    @Mock
    private FacturaService facturaService;

    @Mock
    private ValidadorPermiso validadorPermiso;

    @Mock
    private Factura mockFactura;

    @Mock
    private ArchivoDTO mockArchivoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testComprobanteReservaPorId() {
        try {
            // Setup
            Long id = 1L;
            when(facturaService.obtenerFacturaPorIdCliente(id)).thenReturn(mockArchivoDTO);
            when(mockArchivoDTO.getArchivo()).thenReturn(new byte[0]); // Simular que hay un archivo

            // Execute
            var response = facturaController.comprobanteReservaPorId(id);

            // Verify
            assertNotNull(response);
            verify(facturaService, times(1)).obtenerFacturaPorIdCliente(id);
        } catch (Exception ex) {
            Logger.getLogger(FacturaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testComprobanteReservaPorIdError() {
        try {
            // Setup
            Long id = 1L;
            when(facturaService.obtenerFacturaPorIdCliente(id)).thenThrow(new RuntimeException("Error en el servicio"));

            // Execute
            var response = facturaController.comprobanteReservaPorId(id);

            // Verify
            assertNotNull(response);
            verify(facturaService, times(1)).obtenerFacturaPorIdCliente(id);
        } catch (Exception ex) {
            Logger.getLogger(FacturaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testComprobanteReservaPorIdCliente() {
        try {
            // Setup
            when(facturaService.getFacturasCliente()).thenReturn(new ArrayList<Factura>() {
                {
                    add(mockFactura);
                }
            });

            // Execute
            var response = facturaController.comprobanteReservaPorId();

            // Verify
            assertNotNull(response);
            verify(facturaService, times(1)).getFacturasCliente();
        } catch (Exception ex) {
            Logger.getLogger(FacturaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testComprobanteReservaPorIdClienteError() {
        try {
            // Setup
            when(facturaService.getFacturasCliente()).thenThrow(new RuntimeException("Error en el servicio"));

            // Execute
            var response = facturaController.comprobanteReservaPorId();

            // Verify
            assertNotNull(response);
            verify(facturaService, times(1)).getFacturasCliente();
        } catch (Exception ex) {
            Logger.getLogger(FacturaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetFacturas() {
        try {
            // Setup
            when(validadorPermiso.verificarPermiso()).thenReturn(null);
            List<Factura> facturas = new ArrayList<>();
            facturas.add(mockFactura);
            when(facturaService.getFacturasAdmin()).thenReturn(facturas);

            // Execute
            var response = facturaController.getFacturas();

            // Verify
            assertNotNull(response);
            verify(facturaService, times(1)).getFacturasAdmin();
        } catch (Exception ex) {
            Logger.getLogger(FacturaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetFacturasError() {
        try {
            // Setup
            when(validadorPermiso.verificarPermiso()).thenThrow(new RuntimeException("Permiso denegado"));

            // Execute
            var response = facturaController.getFacturas();

            // Verify
            assertNotNull(response);
            verify(facturaService, times(0)).getFacturasAdmin(); // Verifica que no se llama al método si hay un error en permisos
        } catch (Exception ex) {
            Logger.getLogger(FacturaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetFacturaPorIdAdmin() {
        try {
            // Setup
            Long id = 1L;
            when(validadorPermiso.verificarPermiso()).thenReturn(null);
            when(facturaService.obtenerFacturaPorIdAdmin(id)).thenReturn(mockArchivoDTO);
            when(mockArchivoDTO.getArchivo()).thenReturn(new byte[0]); // Simular que hay un archivo

            // Execute
            var response = facturaController.getFacturaPorIdAdmin(id);

            // Verify
            assertNotNull(response);
            verify(facturaService, times(1)).obtenerFacturaPorIdAdmin(id);
        } catch (Exception ex) {
            Logger.getLogger(FacturaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testGetFacturaPorIdAdminError() {
        try {
            // Setup
            Long id = 1L;
            when(validadorPermiso.verificarPermiso()).thenThrow(new RuntimeException("Permiso denegado"));

            // Execute
            var response = facturaController.getFacturaPorIdAdmin(id);

            // Verify
            assertNotNull(response);
            verify(facturaService, times(0)).obtenerFacturaPorIdAdmin(id); // Verifica que no se llama al método si hay un error en permisos
        } catch (Exception ex) {
            Logger.getLogger(FacturaControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
