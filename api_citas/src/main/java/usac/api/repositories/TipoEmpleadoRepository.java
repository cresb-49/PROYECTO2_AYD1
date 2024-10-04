package usac.api.repositories;

import org.springframework.data.repository.CrudRepository;
import usac.api.models.TipoEmpleado;

import java.util.Optional;

public interface TipoEmpleadoRepository extends CrudRepository<TipoEmpleado, Long> {
    public Optional<TipoEmpleado> findOneByNombre(String nombre);
}
