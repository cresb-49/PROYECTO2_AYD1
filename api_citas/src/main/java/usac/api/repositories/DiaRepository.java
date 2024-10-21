package usac.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import usac.api.models.Dia;

@Repository
public interface DiaRepository extends CrudRepository<Dia, Long> {

    public Optional<Dia> findOneByNombreIgnoreCase(String nombre);
}
