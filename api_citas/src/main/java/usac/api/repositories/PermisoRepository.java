/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import usac.api.models.Permiso;

/**
 *
 * @author Luis Monterroso
 */
@Repository
public interface PermisoRepository extends CrudRepository<Permiso, Long> {

    public Optional<Permiso> findOneByNombre(String nombre);

    @Override
    public List<Permiso> findAll();
}
