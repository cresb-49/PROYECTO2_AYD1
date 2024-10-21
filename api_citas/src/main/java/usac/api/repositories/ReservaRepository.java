/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import usac.api.models.Reserva;

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

}
