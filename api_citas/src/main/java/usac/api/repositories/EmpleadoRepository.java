package usac.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import usac.api.models.Empleado;

public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {
    @Override
    public List<Empleado> findAll();

    public Long deleteEmpleadoById(Long id);
}
