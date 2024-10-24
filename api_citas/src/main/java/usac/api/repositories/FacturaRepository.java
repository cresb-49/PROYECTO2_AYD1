/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package usac.api.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import usac.api.models.Factura;

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
}
