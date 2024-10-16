package usac.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import usac.api.models.Empleado;

public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {
    @Override
    public List<Empleado> findAll();

    @Query(nativeQuery = true, value = "SELECT e.* FROM empleado e join usuario u ON e.usuario = u.id JOIN rol_usuario ru on ru.usuario = u.id  WHERE ru.rol = :rolId GROUP BY e.id")
    List<Empleado> findEmpleadosByRolId(@Param("rolId") Long rolId);

    public Long deleteEmpleadoById(Long id);
}
