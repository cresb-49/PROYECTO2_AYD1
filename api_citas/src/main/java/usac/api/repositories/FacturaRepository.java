/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package usac.api.repositories;

import org.springframework.data.repository.CrudRepository;
import usac.api.models.Factura;

/**
 *
 * @author Luis Monterroso
 */
public interface FacturaRepository extends CrudRepository<Factura, Long> {

}
