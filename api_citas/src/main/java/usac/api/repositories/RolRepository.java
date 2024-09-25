/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package usac.api.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import usac.api.models.Rol;

/**
 *
 * @author Luis Monterroso
 */
public interface RolRepository extends CrudRepository<Rol, Long> {

    Optional<Rol> findOneByNombre(String nombre);
}
