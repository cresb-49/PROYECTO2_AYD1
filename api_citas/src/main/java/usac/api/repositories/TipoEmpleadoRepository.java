package usac.api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import usac.api.models.TipoEmpleado;

import java.util.Optional;

@Repository
public interface TipoEmpleadoRepository extends CrudRepository<TipoEmpleado, Long> {
    public Optional<TipoEmpleado> findOneByNombre(String nombre);
}
