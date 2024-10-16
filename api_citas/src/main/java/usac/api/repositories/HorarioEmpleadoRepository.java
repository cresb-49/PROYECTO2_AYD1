package usac.api.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import usac.api.models.Dia;
import usac.api.models.Empleado;
import usac.api.models.HorarioEmpleado;

import java.util.List;

@Repository
public interface HorarioEmpleadoRepository extends CrudRepository<HorarioEmpleado, Long> {
    @Query(value = "SELECT h FROM HorarioEmpleado h WHERE h.empleado = :empleado")
    public List<HorarioEmpleado> findAllByEmpleadoIncludingDeleted(Empleado empleado);

    @Query(value = "SELECT h FROM HorarioEmpleado h WHERE h.dia = :dia AND h.empleado = :empleado")
    public HorarioEmpleado findByDiaAndEmpleado(Dia dia, Empleado empleado);
}
