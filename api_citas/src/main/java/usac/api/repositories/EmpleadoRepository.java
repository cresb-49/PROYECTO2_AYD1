package usac.api.repositories;

import org.springframework.data.repository.CrudRepository;
import usac.api.models.Empleado;

public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {
}
