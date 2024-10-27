/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package usac.api.repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import usac.api.models.Factura;
import usac.api.models.dto.reportes.ClienteFrecuenteDto;

/**
 *
 * @author Luis Monterroso
 */
public interface FacturaRepository extends CrudRepository<Factura, Long> {

    @Query(value = "SELECT * FROM factura f "
            + "WHERE (:fechaInicio IS NULL OR DATE(f.created_at) >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR DATE(f.created_at) <= :fechaFin)",
            nativeQuery = true)
    public List<Factura> findAllByCreatedAtDateBetween(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    public List<Factura> findAllByReserva_Reservador_Id(
            Long idReservador);

    // Consulta que agrupa por cliente y realiza los cálculos directamente en la base de datos
    /*@Query("SELECT new usac.api.models.dto.reportes.ClienteFrecuenteDto( "
            + "r.reservador.id, r.reservador.nombres, COUNT(f), SUM(f.total), SUM(f.total) / COUNT(f)) "
            + "FROM Factura f "
            + "JOIN f.reserva r "
            + "WHERE (:fechaInicio IS NULL OR DATE(f.createdAt) >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR DATE(f.createdAt) <= :fechaFin) "
            + "GROUP BY r.reservador.id, r.reservador.nombres "
            + "ORDER BY COUNT(f) DESC")
    public List<ClienteFrecuenteDto> findClientesFrecuentes(
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);*/
    @Query("SELECT new usac.api.models.dto.reportes.ClienteFrecuenteDto( "
            + "r.reservador.id, r.reservador.nombres, COUNT(f), SUM(f.total), ROUND(SUM(f.total) / COUNT(f), 2)) "
            + "FROM Factura f "
            + "JOIN f.reserva r "
            + "WHERE (:fechaInicio IS NULL OR DATE(f.createdAt) >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR DATE(f.createdAt) <= :fechaFin) "
            + "GROUP BY r.reservador.id, r.reservador.nombres "
            + "ORDER BY COUNT(f) DESC")
    public List<ClienteFrecuenteDto> findClientesFrecuentes(
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);

}
