
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usac.api.models.dto.reportes.ReporteServiciosDTO;
import usac.api.models.dto.reportes.ServicioMasDemandadoDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReporteServiciosDTOTest {

    private ReporteServiciosDTO reporte;
    private List<ServicioMasDemandadoDto> servicios;
    private List<ServicioMasDemandadoDto> canchas;

    @BeforeEach
    void setUp() {
        ServicioMasDemandadoDto servicio1 = new ServicioMasDemandadoDto(1L, "Consulta General", 150L);
        ServicioMasDemandadoDto servicio2 = new ServicioMasDemandadoDto(2L, "Limpieza Dental", 100L);
        servicios = Arrays.asList(servicio1, servicio2);

        ServicioMasDemandadoDto cancha1 = new ServicioMasDemandadoDto(3L, "Cancha de Fútbol", 80L);
        ServicioMasDemandadoDto cancha2 = new ServicioMasDemandadoDto(4L, "Cancha de Tenis", 50L);
        canchas = Arrays.asList(cancha1, cancha2);

        reporte = new ReporteServiciosDTO(servicios, canchas, "2024-01-01", "2024-12-31", 230);
    }

    @Test
    void testConstructorInicializaCamposCorrectamente() {
        assertEquals(servicios, reporte.getServicios());
        assertEquals(canchas, reporte.getCanchas());
        assertEquals("2024-01-01", reporte.getFecha1());
        assertEquals("2024-12-31", reporte.getFecha2());
        assertEquals(230, reporte.getNumeroReservaciones());
    }

    @Test
    void testSetServicios() {
        ServicioMasDemandadoDto nuevoServicio = new ServicioMasDemandadoDto(5L, "Ortodoncia", 60L);
        List<ServicioMasDemandadoDto> nuevosServicios = Arrays.asList(nuevoServicio);
        reporte.setServicios(nuevosServicios);
        assertEquals(nuevosServicios, reporte.getServicios());
    }

    @Test
    void testSetCanchas() {
        ServicioMasDemandadoDto nuevaCancha = new ServicioMasDemandadoDto(6L, "Cancha de Baloncesto", 30L);
        List<ServicioMasDemandadoDto> nuevasCanchas = Arrays.asList(nuevaCancha);
        reporte.setCanchas(nuevasCanchas);
        assertEquals(nuevasCanchas, reporte.getCanchas());
    }

    @Test
    void testSetFecha1() {
        reporte.setFecha1("2025-01-01");
        assertEquals("2025-01-01", reporte.getFecha1());
    }

    @Test
    void testSetFecha2() {
        reporte.setFecha2("2025-12-31");
        assertEquals("2025-12-31", reporte.getFecha2());
    }

    @Test
    void testSetNumeroReservaciones() {
        reporte.setNumeroReservaciones(300);
        assertEquals(300, reporte.getNumeroReservaciones());
    }
}
