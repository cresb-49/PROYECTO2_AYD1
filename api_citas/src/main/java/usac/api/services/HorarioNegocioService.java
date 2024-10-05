package usac.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.HorarioNegocio;
import usac.api.models.Negocio;
import usac.api.repositories.HorarioNegocioRepository;

@Service
public class HorarioNegocioService {

    @Autowired
    private HorarioNegocioRepository horarioNegocioRepository;

    public List<HorarioNegocio> obtenerTodosLosHorariosIncluyendoEliminados(Negocio negocio) {
        return horarioNegocioRepository.findAllByNegocioIncludingDeleted(negocio);
    }

    public HorarioNegocio obtenerHorarioPorDiaYNegocio(usac.api.models.Dia dia, Negocio negocio) {
        return horarioNegocioRepository.findByDiaAndNegocio(dia, negocio);
    }
}
