package usac.api.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.Dia;
import usac.api.models.HorarioNegocio;
import usac.api.models.Negocio;
import usac.api.repositories.NegocioRepository;

@Service
public class NegocioService extends usac.api.services.Service {

    @Autowired
    private NegocioRepository negocioRepository;
    @Autowired
    private DiaService diaService;
    @Autowired
    private HorarioNegocioService horarioNegocioService;

    public Negocio obtenerNegocio() throws Exception {
        return negocioRepository.findFirstByOrderByIdAsc().orElseThrow(
                () -> new Exception("No pudimos encontrar la configuracion del negocio."));
    }

    @Transactional(rollbackOn = Exception.class)
    public Negocio actualizarNegocio(Negocio negocio, List<HorarioNegocio> horarios) throws Exception {
        // Validar que el negocio tenga un id
        if (negocio.getId() == null || negocio == null) {
            throw new Exception("Id invalido");
        }
        // Buscar el negocio en base al id y validar que exista
        Negocio negocioEncontrado = negocioRepository.findById(negocio.getId()).orElse(null);
        if (negocioEncontrado == null) {
            throw new Exception("No se encontró el negocio");
        }
        // Validar el modelo del negocio
        this.validarModelo(negocio);
        // Actualizar el negocio
        // Actualizar los datos del negocio
        negocioEncontrado.setNombre(negocio.getNombre());
        negocioEncontrado.setLogo(negocio.getLogo());
        negocioEncontrado.setPorcentajeAnticipo(negocio.getPorcentajeAnticipo());
        negocioEncontrado.setAsignacionManual(negocio.isAsignacionManual());
        negocioEncontrado.setDireccion(negocio.getDireccion());
        // Eliminar los horarios anteriores
        negocioEncontrado.getHorarios().clear();
        // Actualizar o agregar los nuevos horarios
        for (HorarioNegocio horario : horarios) {
            HorarioNegocio horarioEncontrado = horarioNegocioService
                    .obtenerHorarioPorDiaYNegocio(diaService.getDiaByNombre(horario.getDia().getNombre()), negocioEncontrado);
            // Si el horario ya existe, se actualiza
            if (horarioEncontrado != null) {
                horarioEncontrado.setApertura(horario.getApertura());
                horarioEncontrado.setCierre(horario.getCierre());
                negocioEncontrado.getHorarios().add(horarioEncontrado);
                //Si el horario encontrado esta eliminado, se activa
                if (horarioEncontrado.getDeletedAt() != null) {
                    horarioEncontrado.setDeletedAt(null);
                }
            } else {
                // Si el horario no existe, se crea
                negocioEncontrado.getHorarios().add(new HorarioNegocio(diaService.getDiaByNombre(horario.getDia().getNombre()), negocioEncontrado, horario.getApertura(), horario.getCierre()));
            }
        }
        // Guardar los cambios
        Negocio negocioUpdate = negocioRepository.save(negocioEncontrado);
        if (negocioUpdate != null && negocioUpdate.getId() != null) {
            return negocioUpdate;
        }
        throw new Exception("No logramos actualizar el negocio, inténtalo más tarde.");
    }

    @Transactional(rollbackOn = Exception.class)
    public Negocio CrearNegocio(Negocio negocio, ArrayList<Dia> horario) throws Exception {
        // Solo puede existir un negocio
        if (negocioRepository.count() > 0) {
            throw new Exception("Ya existe un negocio registrado");
        }
        // Validar el modelo del negocio
        this.validarModelo(negocio);
        // Asignar el horario al negocio
        ArrayList<HorarioNegocio> horarioCreado = new ArrayList<>();
        for (Dia dia : horario) {
            horarioCreado.add(new HorarioNegocio(dia, negocio, LocalTime.of(8, 0), LocalTime.of(18, 0)));
        }
        negocio.setHorarios(horarioCreado);
        // Crear el negocio
        Negocio negocioCreado = negocioRepository.save(negocio);
        if (negocioCreado != null && negocioCreado.getId() != null) {
            return negocioCreado;
        }
        throw new Exception("No logramos crear el negocio, inténtalo más tarde.");
    }
}
