/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package usac.api.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import usac.api.models.Rol;

/**
 *
 * @author Luis Monterroso
 */
@Repository
public interface RolRepository extends CrudRepository<Rol, Long> {

    Optional<Rol> findOneByNombre(String nombre);

    @Query("SELECT r FROM Rol r WHERE r.nombre <> 'admin' AND r.nombre <> 'empleado' AND r.nombre <> 'cliente'")
    public List<Rol> findAllExcludingAdminEmpleadoCliente();

    public boolean existsByNombre(String nombre);

    public boolean existsByNombreAndIdNot(String nombre, Long id);
}
