package usac.api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import usac.api.models.Cancha;

@Repository
public interface CanchaRepository extends CrudRepository<Cancha,Long> {
}
