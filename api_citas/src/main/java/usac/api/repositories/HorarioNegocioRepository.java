package usac.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import usac.api.models.Dia;
import usac.api.models.HorarioNegocio;
import usac.api.models.Negocio;

@Repository
public interface HorarioNegocioRepository extends CrudRepository<HorarioNegocio, Long> {

    // Método para obtener todos los horarios, incluidos los eliminados
    @Query("SELECT h FROM HorarioNegocio h WHERE h.negocio = :negocio")
    List<HorarioNegocio> findAllByNegocioIncludingDeleted(Negocio negocio);

    // Método para buscar un horario específico por día y negocio
    @Query("SELECT h FROM HorarioNegocio h WHERE h.dia = :dia AND h.negocio = :negocio")
    HorarioNegocio findByDiaAndNegocio(Dia dia, Negocio negocio);
}
