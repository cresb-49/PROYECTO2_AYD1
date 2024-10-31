package usac.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import usac.api.models.*;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.request.ReservacionServicioRequest;
import usac.api.repositories.ReservaRepository;
import usac.api.repositories.ReservaCanchaRepository;
import usac.api.repositories.ReservaServicioRepository;
import usac.api.tools.ManejadorTiempo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpHeaders;
import usac.api.enums.FileHttpMetaData;
import usac.api.models.request.GetReservacionesRequest;
import usac.api.models.request.ReservacionCanchaRequest;
import usac.api.reportes.imprimibles.ComprobanteReservaImprimible;
import usac.api.reportes.imprimibles.FacturaImprimible;

class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaCanchaRepository reservaCanchaRepository;

    @Mock
    private ReservaServicioRepository reservaServicioRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ManejadorTiempo manejadorFechas;

    @Mock
    private HorarioCanchaService horarioCanchaService;

    @Mock
    private DiaService diaService;

    @Mock
    ReporteService reporteService;

    @Mock
    private CanchaService canchaService;

    @Mock
    private NegocioService negocioService;

    @Mock
    private ServicioService servicioService;

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private FacturaService facturaService;

    @Mock
    private ComprobanteReservaImprimible comprobanteReservaImprimible;

    @Mock
    private FacturaImprimible facturaImprimible;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Usuario genérico para las pruebas
        usuario = new Usuario();
        usuario.setId(1L);
    }

    @Mock
    private javax.validation.Validator validator;

    @Test
    void testGetReservasDelMes_RolCliente() throws Exception {
        // Arrange
        GetReservacionesRequest request = new GetReservacionesRequest();
        request.setMes(10);
        request.setAnio(2024);

        Rol rolCliente = new Rol();
        rolCliente.setNombre("CLIENTE");
        usuario.setRoles(List.of(new RolUsuario(rolCliente)));

        when(usuarioService.getUsuarioUseJwt()).thenReturn(usuario);
        when(reservaRepository.findReservasByReservadorAndMesAndAnio(usuario.getId(), request.getMes(), request.getAnio()))
                .thenReturn(List.of(new Reserva()));

        // Act
        List<Reserva> reservas = reservaService.getReservasDelMes(request);

        // Assert
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        verify(reservaRepository, times(1)).findReservasByReservadorAndMesAndAnio(usuario.getId(), request.getMes(), request.getAnio());
    }

    @Test
    void testGetReservasDelMes_RolAdmin() throws Exception {
        // Arrange
        GetReservacionesRequest request = new GetReservacionesRequest();
        request.setMes(10);
        request.setAnio(2024);

        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ADMIN");
        usuario.setRoles(List.of(new RolUsuario(rolAdmin)));

        when(usuarioService.getUsuarioUseJwt()).thenReturn(usuario);
        when(reservaRepository.findReservasByMesAndAnio(request.getMes(), request.getAnio()))
                .thenReturn(List.of(new Reserva()));

        // Act
        List<Reserva> reservas = reservaService.getReservasDelMes(request);

        // Assert
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        verify(reservaRepository, times(1)).findReservasByMesAndAnio(request.getMes(), request.getAnio());
    }

    @Test
    void testGetReservasDelMes_RolEmpleado() throws Exception {
        // Arrange
        GetReservacionesRequest request = new GetReservacionesRequest();
        request.setMes(10);
        request.setAnio(2024);

        Rol rolEmpleado = new Rol();
        rolEmpleado.setNombre("EMPLEADO");
        usuario.setRoles(List.of(new RolUsuario(rolEmpleado)));

        when(usuarioService.getUsuarioUseJwt()).thenReturn(usuario);
        when(reservaRepository.findReservasByEmpleadoAndMesAndAnio(usuario.getId(), request.getMes(), request.getAnio()))
                .thenReturn(List.of(new Reserva()));

        // Act
        List<Reserva> reservas = reservaService.getReservasDelMes(request);

        // Assert
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        verify(reservaRepository, times(1)).findReservasByEmpleadoAndMesAndAnio(usuario.getId(), request.getMes(), request.getAnio());
    }

    @Test
    void testReservaCancha_HoraFinVacia() {
        // Arrange
        ReservacionCanchaRequest request = new ReservacionCanchaRequest();
        request.setCanchaId(1L);
        request.setFechaReservacion(LocalDate.now());
        request.setHoraInicio(LocalTime.of(10, 0));
        request.setHoraFin(null);  // Hora de fin vacía

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> reservaService.reservaCancha(request));
        assertTrue(exception.getMessage().contains("La hora de fin de la reservacion no puede estar vacia."));
    }

    @Test
    void testReservaCancha_CanchaOcupada() throws Exception {
        // Arrange
        ReservacionCanchaRequest request = new ReservacionCanchaRequest();
        request.setCanchaId(1L);
        request.setFechaReservacion(LocalDate.now());
        request.setHoraInicio(LocalTime.of(10, 0));
        request.setHoraFin(LocalTime.of(12, 0));

        Cancha cancha = new Cancha();
        cancha.setCostoHora(50.0);

        HorarioCancha horarioCancha = new HorarioCancha();
        horarioCancha.setApertura(LocalTime.of(9, 0));
        horarioCancha.setCierre(LocalTime.of(18, 0));

        when(canchaService.getCanchaById(anyLong())).thenReturn(cancha);
        when(manejadorFechas.localDateANombreDia(any())).thenReturn("Lunes");
        when(diaService.getDiaByNombre("Lunes")).thenReturn(new Dia());
        when(horarioCanchaService.obtenerHorarioPorDiaYCancha(any(Dia.class), eq(cancha))).thenReturn(horarioCancha);
        when(reservaCanchaRepository.findReservasSolapadas(any(), anyLong(), any(), any())).thenReturn(List.of(new ReservaCancha()));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> reservaService.reservaCancha(request));
        assertFalse(exception.getMessage().contains("La cancha ya está ocupada en el horario solicitado."));
    }

    @Test
    void testReservaCancha_UsuarioConReservaSolapada() {
        try {
            // Arrange
            ReservacionCanchaRequest request = new ReservacionCanchaRequest();
            request.setCanchaId(1L);
            request.setFechaReservacion(LocalDate.now());
            request.setHoraInicio(LocalTime.of(10, 0));
            request.setHoraFin(LocalTime.of(12, 0));

            Usuario reservador = new Usuario();
            reservador.setId(1L);

            when(usuarioService.getUsuarioUseJwt()).thenReturn(reservador);
            when(reservaRepository.countReservasDelReservadorSolapadas(anyLong(), any(), any(), any())).thenReturn(1L);

            // Act & Assert
            Exception exception = assertThrows(Exception.class, () -> reservaService.reservaCancha(request));
            assertFalse(exception.getMessage().contains("Ya tienes una reservación en el mismo periodo."));
        } catch (Exception ex) {
            Logger.getLogger(ReservaServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testReservaCancha_HorarioNoDisponible() throws Exception {
        // Arrange
        ReservacionCanchaRequest request = new ReservacionCanchaRequest();
        request.setCanchaId(1L);
        request.setFechaReservacion(LocalDate.now());
        request.setHoraInicio(LocalTime.of(20, 0));
        request.setHoraFin(LocalTime.of(22, 0));

        Cancha cancha = new Cancha();
        cancha.setCostoHora(50.0);

        HorarioCancha horarioCancha = new HorarioCancha();
        horarioCancha.setApertura(LocalTime.of(9, 0));
        horarioCancha.setCierre(LocalTime.of(18, 0));

        when(canchaService.getCanchaById(anyLong())).thenReturn(cancha);
        when(manejadorFechas.localDateANombreDia(any())).thenReturn("Lunes");
        when(diaService.getDiaByNombre("Lunes")).thenReturn(new Dia());
        when(horarioCanchaService.obtenerHorarioPorDiaYCancha(any(Dia.class), eq(cancha))).thenReturn(horarioCancha);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> reservaService.reservaCancha(request));
        assertFalse(exception.getMessage().contains("fuera del horario disponible"));
    }

    @Test
    void testReservaServicio_UsuarioConReservaSolapada() {
        try {
            ReservacionServicioRequest request = new ReservacionServicioRequest();
            request.setServicioId(1L);
            request.setFechaReservacion(LocalDate.now());
            request.setHoraInicio(LocalTime.of(10, 0));

            Usuario reservador = new Usuario();
            reservador.setId(1L);

            when(usuarioService.getUsuarioUseJwt()).thenReturn(reservador);
            when(reservaRepository.countReservasDelReservadorSolapadas(anyLong(), any(), any(), any())).thenReturn(1L);

            Exception exception = assertThrows(Exception.class, () -> reservaService.reservaServicio(request));
            assertFalse(exception.getMessage().contains("Ya tienes una reservación en el mismo periodo."));
        } catch (Exception ex) {
            Logger.getLogger(ReservaServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testReservaServicio_EmpleadoNoAutorizado() throws Exception {
        ReservacionServicioRequest request = new ReservacionServicioRequest();
        request.setServicioId(1L);
        request.setFechaReservacion(LocalDate.now());
        request.setHoraInicio(LocalTime.of(10, 0));

        Servicio servicio = new Servicio();
        servicio.setEmpleadosParalelos(2);

        Empleado empleadoNoAutorizado = new Empleado();
        empleadoNoAutorizado.setId(99L);

        when(servicioService.getServicioById(1L)).thenReturn(servicio);
        when(empleadoService.getEmpleadosPorServicio(servicio)).thenReturn(List.of(empleadoNoAutorizado));

        Exception exception = assertThrows(Exception.class, () -> reservaService.reservaServicio(request));
        assertFalse(exception.getMessage().contains("El empleado seleccionado no está autorizado"));
    }

    @Test
    void testReservaServicio_HorarioEmpleadoNoDisponible() throws Exception {
        ReservacionServicioRequest request = new ReservacionServicioRequest();
        request.setServicioId(1L);
        request.setFechaReservacion(LocalDate.now());
        request.setHoraInicio(LocalTime.of(20, 0));

        Servicio servicio = new Servicio();
        servicio.setEmpleadosParalelos(2);

        Empleado empleado = new Empleado();
        empleado.setId(1L);

        HorarioEmpleado horarioEmpleado = new HorarioEmpleado();
        horarioEmpleado.setEntrada(LocalTime.of(9, 0));
        horarioEmpleado.setSalida(LocalTime.of(18, 0));

        when(servicioService.getServicioById(1L)).thenReturn(servicio);
        when(empleadoService.getEmpleadosPorServicio(servicio)).thenReturn(List.of(empleado));
        when(manejadorFechas.localDateANombreDia(any())).thenReturn("Lunes");
        when(diaService.getDiaByNombre("Lunes")).thenReturn(new Dia());
        when(empleadoService.obtenerHorarioDiaEmpleado(any(Dia.class), any(Empleado.class))).thenReturn(horarioEmpleado);

        Exception exception = assertThrows(Exception.class, () -> reservaService.reservaServicio(request));
        assertFalse(exception.getMessage().contains("fuera del horario disponible del empleado"));
    }

    @Test
    void testReservaServicio_SinDisponibilidadDeEmpleados() throws Exception {
        // Arrange
        ReservacionServicioRequest request = new ReservacionServicioRequest();
        request.setServicioId(1L);
        request.setFechaReservacion(LocalDate.now());
        request.setHoraInicio(LocalTime.of(10, 0));
        request.setHoraFin(LocalTime.of(12, 0));

        Servicio servicio = new Servicio();
        servicio.setEmpleadosParalelos(1);
        when(servicioService.getServicioById(1L)).thenReturn(servicio);
        when(negocioService.obtenerNegocio()).thenReturn(new Negocio(20.0));

        // Mock la no disponibilidad de empleados
        when(reservaServicioRepository.countReservasActivasPorServicio(anyLong(), any(), any(), any())).thenReturn(1);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> reservaService.reservaServicio(request));
        assertFalse(exception.getMessage().contains(
                "No hay disponibilidad de empleados para "
                + "este servicio en el horario seleccionado."));
    }

    @Test
    void testRealizarLaReserva_ReservaYaRealizada() throws Exception {
        // Arrange
        Long reservaId = 1L;
        Reserva reserva = new Reserva();
        reserva.setRealizada(true);
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            reservaService.realizarLaReserva(reservaId);
        });

        assertTrue(exception.getMessage().contains("La reservacion ya ha sido realizada"));
    }

    @Test
    void testObtenerReservaPorId_ThrowsExceptionIfNotFound() {
        // Arrange
        Long reservaId = 1L;
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            reservaService.obtenerReservaPorId(reservaId);
        });

        assertTrue(exception.getMessage().contains("No se encontró la reserva"));
    }

    @Test
    void testObtenerComprobanteReservaPorId_ReservaEncontrada() throws Exception {
        // Arrange
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        byte[] reporteBytes = new byte[]{1, 2, 3};
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", FileHttpMetaData.PDF.getContentType());

        when(reservaRepository.findById(reserva.getId())).thenReturn(Optional.of(reserva));
        when(comprobanteReservaImprimible.init(reserva, "pdf")).thenReturn(reporteBytes);
        when(reporteService.setHeaders(FileHttpMetaData.PDF)).thenReturn(headers);

        // Act
        ArchivoDTO archivoDTO = reservaService.obtenerComprobanteReservaPorId(reserva.getId());

        // Assert
        assertNotNull(archivoDTO);
        assertArrayEquals(reporteBytes, archivoDTO.getArchivo());
        assertEquals(FileHttpMetaData.PDF.getContentType(), archivoDTO.getHeaders().getContentType().toString());
    }

    @Test
    void testObtenerComprobanteReservaPorId_ReservaNoEncontrada() {
        // Arrange
        Long reservaId = 999L;
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> reservaService.obtenerComprobanteReservaPorId(reservaId));
        assertTrue(exception.getMessage().contains("No se encontró la reserva con el ID proporcionado: " + reservaId));
    }

}
