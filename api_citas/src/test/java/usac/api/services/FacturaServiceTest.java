package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Factura;
import usac.api.models.Reserva;
import usac.api.models.Usuario;
import usac.api.models.dto.ArchivoDTO;
import usac.api.reportes.imprimibles.FacturaImprimible;
import usac.api.repositories.FacturaRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FacturaServiceTest {

    @InjectMocks
    private FacturaService facturaService;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private ReservaService reservaService;

    @Mock
    private FacturaImprimible facturaImprimible;

    @Mock
    private UsuarioService usuarioService;

    private Factura factura;
    private Reserva reserva;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNit("12345678");

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setReservador(usuario);

        factura = new Factura();
        factura.setId(1L);
        factura.setReserva(reserva);
    }

    @Test
    void testCrearFacturaExitoso() throws Exception {
        when(reservaService.obtenerReservaPorId(anyLong())).thenReturn(reserva);
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura);

        Factura facturaCreada = facturaService.crearFactura(factura, reserva.getId());

        assertNotNull(facturaCreada);
        assertEquals(factura.getId(), facturaCreada.getId());
        verify(facturaRepository, times(1)).save(factura);
    }

    @Test
    void testObtenerFacturaImprimible() throws Exception {
        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(facturaImprimible.init(any(Factura.class), eq("pdf"))).thenReturn(expectedBytes);

        ArchivoDTO archivoDTO = facturaService.obtenerFacturaImprimible(factura);

        assertNotNull(archivoDTO);
        assertArrayEquals(expectedBytes, archivoDTO.getArchivo());
        assertEquals("application/pdf", archivoDTO.getHeaders().getContentType().toString());
    }

    @Test
    void testObtenerFacturaPorIdClienteNoEncontrada() {
        when(facturaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> facturaService.obtenerFacturaPorIdCliente(1L));
        assertEquals("Factura no econtrada.", exception.getMessage());
    }

    @Test
    void testObtenerFacturaPorIdAdminExitoso() throws Exception {
        when(facturaRepository.findById(anyLong())).thenReturn(Optional.of(factura));
        when(facturaImprimible.init(any(Factura.class), eq("pdf"))).thenReturn(new byte[]{1, 2, 3});

        ArchivoDTO archivoDTO = facturaService.obtenerFacturaPorIdAdmin(factura.getId());

        assertNotNull(archivoDTO);
        verify(facturaRepository, times(1)).findById(factura.getId());
    }

    @Test
    void testGetFacturasCliente() throws Exception {
        when(usuarioService.getUsuarioUseJwt()).thenReturn(usuario);
        when(facturaRepository.findAllByReserva_Reservador_Id(anyLong())).thenReturn(List.of(factura));

        List<Factura> facturas = facturaService.getFacturasCliente();

        assertNotNull(facturas);
        assertFalse(facturas.isEmpty());
        assertEquals(1, facturas.size());
        verify(facturaRepository, times(1)).findAllByReserva_Reservador_Id(usuario.getId());
    }

    @Test
    void testGetFacturasAdmin() {
        try {
            when(facturaRepository.findAll()).thenReturn(List.of(factura));

            List<Factura> facturas = facturaService.getFacturasAdmin();

            assertNotNull(facturas);
            assertFalse(facturas.isEmpty());
            assertEquals(1, facturas.size());
            verify(facturaRepository, times(1)).findAll();
        } catch (Exception ex) {
            Logger.getLogger(FacturaServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testCrearFacturaReservaYaFacturada() {
        try {
            reserva.setFactura(factura);
            when(reservaService.obtenerReservaPorId(anyLong())).thenReturn(reserva);

            Exception exception = assertThrows(Exception.class, () -> facturaService.crearFactura(factura, reserva.getId()));
            assertEquals("La reserva ya tiene una factura asociada.", exception.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(FacturaServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testObtenerFacturaPorIdCliente_FacturaNoEncontrada() {
        // Configuración del mock para simular que no se encuentra la factura
        when(facturaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Ejecutar y verificar que se lance una excepción con el mensaje esperado
        Exception exception = assertThrows(Exception.class, () -> facturaService.obtenerFacturaPorIdCliente(1L));
        assertEquals("Factura no econtrada.", exception.getMessage());
        verify(facturaRepository, times(1)).findById(1L);
    }

}
