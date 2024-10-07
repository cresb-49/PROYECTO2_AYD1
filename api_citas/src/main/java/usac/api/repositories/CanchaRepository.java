package usac.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import usac.api.models.Cancha;
import usac.api.models.Usuario;

@Repository
public interface CanchaRepository extends CrudRepository<Cancha,Long> {
    @Override
    public List<Cancha> findAll();
}
