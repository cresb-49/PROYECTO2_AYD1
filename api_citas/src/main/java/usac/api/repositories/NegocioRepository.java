package usac.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import usac.api.models.Negocio;

@Repository
public interface NegocioRepository extends CrudRepository<Negocio, Long> {

    // Método personalizado para obtener el único registro
    public Optional<Negocio> findFirstByOrderByIdAsc();
}
