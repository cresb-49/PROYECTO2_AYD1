/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package usac.api.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usac.api.models.ReservaCancha;
import usac.api.models.ReservaServicio;

/**
 *
 * @author Luis Monterroso
 */
@Repository
public interface ReservaServicioRepository extends CrudRepository<ReservaServicio, Long> {

    @Query("SELECT r FROM ReservaServicio r WHERE r.reserva.fechaReservacion = :fecha AND"
            + " r.empleado.id = :empleadoId AND"
            + " (r.reserva.horaInicio < :horaFin AND r.reserva.horaFin > :horaInicio) AND"
            + " r.reserva.canceledAt IS NULL AND"
            + " r.reserva.desactivatedAt IS NULL AND"
            + " r.reserva.deletedAt IS NULL")
    public List<ReservaServicio> findReservasSolapadasEmpleado(@Param("fecha") LocalDate fecha,
            @Param("empleadoId") Long empleadoId,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin);

    @Query("SELECT COUNT(rs) FROM ReservaServicio rs WHERE rs.empleado.id"
            + " = :empleadoId AND rs.reserva.fechaReservacion = :fecha AND"
            + " rs.reserva.canceledAt IS NULL AND"
            + " rs.reserva.desactivatedAt IS NULL AND"
            + " rs.reserva.deletedAt IS NULL")
    public Long countReservasByEmpleadoAndFecha(@Param("empleadoId") Long empleadoId,
            @Param("fecha") LocalDate fecha);

    @Query("SELECT COUNT(r) FROM ReservaServicio r WHERE r.servicio.id = :servicioId"
            + " AND r.reserva.fechaReservacion = :fecha AND "
            + "(r.reserva.horaInicio < :horaFin AND r.reserva.horaFin > :horaInicio) AND"
            + " r.reserva.canceledAt IS NULL AND"
            + " r.reserva.desactivatedAt IS NULL AND"
            + " r.reserva.deletedAt IS NULL")
    public Integer countReservasActivasPorServicio(@Param("servicioId") Long servicioId, @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin);

}
