package usac.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.HorarioCancha;
import usac.api.repositories.HorarioCanchaRepository;

import java.util.List;

@Service
public class HorarioCanchaService extends usac.api.services.Service {

    @Autowired
    private HorarioCanchaRepository horarioCanchaRepository;

    public List<HorarioCancha> obtenerTodosLosHorariosIncluyendoEliminados(Cancha cancha) {
        return horarioCanchaRepository.findAllByCanchaIncludingDeleted(cancha);
    }

    public HorarioCancha obtenerHorarioPorDiaYCancha(Dia dia, Cancha cancha) {
        return horarioCanchaRepository.findByDiaAndCancha(dia, cancha);
    }

}
