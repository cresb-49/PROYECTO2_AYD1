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
import usac.api.models.ReservaCancha;

/**
 *
 * @author luid
 */
public interface ReservaCanchaRepository extends CrudRepository<ReservaCancha, Long> {

    @Query("SELECT r FROM ReservaCancha r WHERE r.reserva.fechaReservacion = :fecha AND"
            + " r.cancha.id = :canchaId AND"
            + " (r.reserva.horaInicio < :horaFin AND r.reserva.horaFin > :horaInicio) AND"
            + " r.reserva.canceledAt IS NULL AND"
            + " r.reserva.desactivatedAt IS NULL AND"
            + " r.reserva.deletedAt IS NULL")
    public List<ReservaCancha> findReservasSolapadas(@Param("fecha") LocalDate fecha,
            @Param("canchaId") Long canchaId,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin);
}
