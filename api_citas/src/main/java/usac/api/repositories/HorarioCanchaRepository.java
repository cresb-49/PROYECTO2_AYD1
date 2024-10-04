package usac.api.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.HorarioCancha;

import java.util.List;

@Repository
public interface HorarioCanchaRepository extends CrudRepository<HorarioCancha, Long> {
    // Método para buscar todos los horarios de una cancha
    @Query(value = "SELECT h FROM HorarioCancha h WHERE h.cancha = :cancha")
    public List<HorarioCancha> findAllByCanchaIncludingDeleted(Cancha cancha);

    // Método para buscar un horario específico por día y cancha
    @Query(value = "SELECT h FROM HorarioCancha h WHERE h.dia = :dia AND h.cancha = :cancha")
    public HorarioCancha findByDiaAndCancha(Dia dia, Cancha cancha);
}
