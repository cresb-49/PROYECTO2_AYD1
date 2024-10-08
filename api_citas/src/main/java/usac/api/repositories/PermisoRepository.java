/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import usac.api.models.Permiso;

/**
 *
 * @author Luis Monterroso
 */
@Repository
public interface PermisoRepository extends CrudRepository<Permiso, Long> {

}
