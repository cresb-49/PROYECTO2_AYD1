/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import usac.api.models.Reserva;
import usac.api.models.dto.reportes.DisponiblilidadRecursoDTO;
import usac.api.models.dto.reportes.ServicioMasDemandadoDto;

/**
 *
 * @author luid
 */
public interface ReservaRepository extends CrudRepository<Reserva, Long> {

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.reservador.id = :usuarioId"
            + " AND r.fechaReservacion = :fecha"
            + " AND (r.horaInicio < :horaFin AND r.horaFin > :horaInicio)"
            + " AND r.canceledAt IS NULL AND r.desactivatedAt IS NULL AND r.deletedAt IS NULL")
    public Long countReservasDelReservadorSolapadas(@Param("usuarioId") Long usuarioId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin);

    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.reservaCancha rc LEFT JOIN FETCH rc.cancha c WHERE MONTH(r.fechaReservacion) = :mes AND YEAR(r.fechaReservacion) = :anio")
    public List<Reserva> findReservasByMesAndAnio(@Param("mes") int mes, @Param("anio") int anio);

    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.reservaCancha rc LEFT JOIN FETCH rc.cancha c WHERE r.reservador.id = :reservadorId AND MONTH(r.fechaReservacion) = :mes AND YEAR(r.fechaReservacion) = :anio")
    public List<Reserva> findReservasByReservadorAndMesAndAnio(@Param("reservadorId") Long reservadorId,
            @Param("mes") int mes,
            @Param("anio") int anio);

    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.reservaServicio rs LEFT JOIN FETCH r.reservaCancha rc LEFT JOIN FETCH rc.cancha c WHERE rs.empleado.id = :empleadoId AND MONTH(r.fechaReservacion) = :mes AND YEAR(r.fechaReservacion) = :anio")
    public List<Reserva> findReservasByEmpleadoAndMesAndAnio(@Param("empleadoId") Long empleadoId,
            @Param("mes") int mes,
            @Param("anio") int anio);

    //REPORTES 
    @Query("SELECT new usac.api.models.dto.reportes.ServicioMasDemandadoDto("
            + "c.cancha.id, c.cancha.descripcion, COUNT(c)) "
            + "FROM ReservaCancha c "
            + "JOIN c.reserva r "
            + "WHERE (:fechaInicio IS NULL OR DATE(r.fechaReservacion) >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR DATE(r.fechaReservacion) <= :fechaFin) "
            + "GROUP BY c.cancha.id, c.cancha.descripcion "
            + "ORDER BY COUNT(c) DESC")
    public List<ServicioMasDemandadoDto> findCanchasMasDemandadas(
            @Param("fechaInicio") java.sql.Date fechaInicio,
            @Param("fechaFin") java.sql.Date fechaFin);

    @Query("SELECT new usac.api.models.dto.reportes.ServicioMasDemandadoDto("
            + "s.servicio.id, s.servicio.nombre, COUNT(s)) "
            + "FROM ReservaServicio s "
            + "JOIN s.reserva r "
            + "WHERE (:fechaInicio IS NULL OR DATE(r.fechaReservacion) >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR DATE(r.fechaReservacion) <= :fechaFin) "
            + "GROUP BY s.servicio.id, s.servicio.nombre "
            + "ORDER BY COUNT(s) DESC")
    public List<ServicioMasDemandadoDto> findServiciosMasDemandados(
            @Param("fechaInicio") java.sql.Date fechaInicio,
            @Param("fechaFin") java.sql.Date fechaFin);

    /* @Query("SELECT new usac.api.models.dto.reportes.DisponiblilidadRecursoDTO("
            + "r.reservador.nombres, r.horaInicio, r.horaFin, r.fechaReservacion, "
            + "CASE WHEN rc.cancha IS NOT NULL THEN rc.cancha.descripcion "
            + "WHEN rs.servicio IS NOT NULL THEN rs.empleado.usuario.nombres "
            + "END) "
            + "FROM Reserva r "
            + "LEFT JOIN r.reservaCancha rc "
            + "LEFT JOIN r.reservaServicio rs "
            + "WHERE (:fechaInicio IS NULL OR r.fechaReservacion >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR r.fechaReservacion <= :fechaFin) "
            + "ORDER BY CASE WHEN rc.cancha IS NOT NULL THEN rc.cancha.id "
            + "WHEN rs.empleado IS NOT NULL THEN rs.empleado.id "
            + "END ASC")*/
    @Query("SELECT new usac.api.models.dto.reportes.DisponiblilidadRecursoDTO("
            + "u.nombres, "
            + "r.horaInicio, "
            + "r.horaFin, "
            + "r.fechaReservacion, "
            + "CASE "
            + "WHEN rc.cancha IS NOT NULL THEN c.descripcion "
            + "WHEN rs.servicio IS NOT NULL THEN s.nombre "
            + "ELSE 'No asignado' "
            + "END) "
            + "FROM Reserva r "
            + "LEFT JOIN r.reservaCancha rc "
            + "LEFT JOIN rc.cancha c "
            + "LEFT JOIN r.reservaServicio rs "
            + "LEFT JOIN rs.servicio s "
            + "LEFT JOIN r.reservador u "
            + "WHERE (rc.cancha IS NOT NULL OR rs.servicio IS NOT NULL) "
            + "AND (:fechaInicio IS NULL OR r.fechaReservacion >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR r.fechaReservacion <= :fechaFin) "
            + "ORDER BY "
            + "CASE "
            + "WHEN rc.cancha IS NOT NULL THEN c.id "
            + "WHEN rs.servicio IS NOT NULL THEN s.id "
            + "END ASC")
    public List<DisponiblilidadRecursoDTO> findReporteDisponibilidad(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT new usac.api.models.dto.reportes.DisponiblilidadRecursoDTO("
            + "u.nombres, "
            + "r.horaInicio, "
            + "r.horaFin, "
            + "r.fechaReservacion, "
            + "CASE WHEN rs.empleado IS NOT NULL THEN e.usuario.nombres ELSE 'No asignado' END) "
            + "FROM Reserva r "
            + "JOIN r.reservaServicio rs "
            + "JOIN rs.empleado e "
            + "JOIN r.reservador u "
            + "WHERE (:fechaInicio IS NULL OR r.fechaReservacion >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR r.fechaReservacion <= :fechaFin) "
            + "ORDER BY e.id ASC")
    public List<DisponiblilidadRecursoDTO> findReporteDisponibilidadReservasServicio(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT new usac.api.models.dto.reportes.DisponiblilidadRecursoDTO("
            + "u.nombres, "
            + "r.horaInicio, "
            + "r.horaFin, "
            + "r.fechaReservacion, "
            + "CASE WHEN rc.cancha IS NOT NULL THEN c.descripcion ELSE 'No asignado' END) "
            + "FROM Reserva r "
            + "JOIN r.reservaCancha rc "
            + "JOIN rc.cancha c "
            + "JOIN r.reservador u "
            + "WHERE (:fechaInicio IS NULL OR r.fechaReservacion >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR r.fechaReservacion <= :fechaFin) "
            + "ORDER BY c.id ASC")
    public List<DisponiblilidadRecursoDTO> findReporteDisponibilidadReservasCancha(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

}
