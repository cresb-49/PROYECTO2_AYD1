package usac.api.models.dto.reportes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReporteDisponibilidadDTOTest {

    private ReporteDisponibilidadDTO reporte;
    private List<DisponiblilidadRecursoDTO> disponibilidadEmpleados;
    private List<DisponiblilidadRecursoDTO> disponibilidadCanchas;

    @BeforeEach
    void setUp() {
        DisponiblilidadRecursoDTO recursoEmpleado = new DisponiblilidadRecursoDTO("Juan", LocalTime.of(8, 0), LocalTime.of(12, 0), LocalDate.now(), "Recurso 1");
        DisponiblilidadRecursoDTO recursoCancha = new DisponiblilidadRecursoDTO("Maria", LocalTime.of(9, 0), LocalTime.of(13, 0), LocalDate.now(), "Cancha 1");

        disponibilidadEmpleados = Arrays.asList(recursoEmpleado);
        disponibilidadCanchas = Arrays.asList(recursoCancha);

        reporte = new ReporteDisponibilidadDTO(disponibilidadEmpleados, disponibilidadCanchas, "2024-01-01", "2024-12-31");
    }

    @Test
    void testConstructorInicializaCamposCorrectamente() {
        assertEquals(disponibilidadEmpleados, reporte.getDisponibilidadEmpleados());
        assertEquals(disponibilidadCanchas, reporte.getDisponibilidadCanchas());
        assertEquals("2024-01-01", reporte.getFecha1());
        assertEquals("2024-12-31", reporte.getFecha2());
    }

    @Test
    void testSetDisponibilidadEmpleados() {
        DisponiblilidadRecursoDTO nuevoEmpleado = new DisponiblilidadRecursoDTO("Carlos", LocalTime.of(10, 0), LocalTime.of(14, 0), LocalDate.now(), "Recurso 2");
        List<DisponiblilidadRecursoDTO> nuevaDisponibilidadEmpleados = Arrays.asList(nuevoEmpleado);
        reporte.setDisponibilidadEmpleados(nuevaDisponibilidadEmpleados);
        assertEquals(nuevaDisponibilidadEmpleados, reporte.getDisponibilidadEmpleados());
    }

    @Test
    void testSetDisponibilidadCanchas() {
        DisponiblilidadRecursoDTO nuevaCancha = new DisponiblilidadRecursoDTO("Ana", LocalTime.of(11, 0), LocalTime.of(15, 0), LocalDate.now(), "Cancha 2");
        List<DisponiblilidadRecursoDTO> nuevaDisponibilidadCanchas = Arrays.asList(nuevaCancha);
        reporte.setDisponibilidadCanchas(nuevaDisponibilidadCanchas);
        assertEquals(nuevaDisponibilidadCanchas, reporte.getDisponibilidadCanchas());
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
}
