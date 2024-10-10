package usac.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import usac.api.models.Servicio;

public interface ServicioRepository extends CrudRepository<Servicio, Long> {
    @Override
    public List<Servicio> findAll();

    public Long deleteServicioById(Long id);

    //Una busqueda like por nombre
    public List<Servicio> findByNombreContaining(String nombre);

}
