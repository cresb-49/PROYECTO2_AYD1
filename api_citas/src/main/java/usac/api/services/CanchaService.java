package usac.api.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.Cancha;
import usac.api.models.HorarioCancha;
import usac.api.repositories.CanchaRepository;

@Service
public class CanchaService extends usac.api.services.Service {
    @Autowired
    private CanchaRepository canchaRepository;
    @Autowired
    private HorarioCanchaService horarioCanchaService;
    @Autowired
    private DiaService diaService;

    /**
     * Método para crear una cancha
     *
     * @param cancha
     * @param horarios
     * @return
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    public Cancha crearCancha(Cancha cancha, List<HorarioCancha> horarios) throws Exception {
        //Validamos el modelo
        this.validarModelo(cancha);
        // Creamos los horarios de la cancha
        ArrayList<HorarioCancha> horarioCanchaCreados = new ArrayList<>();
        for (HorarioCancha horario : horarios) {
            //Obtenemos el dia
            horarioCanchaCreados.add(new HorarioCancha(cancha, horario.getDia(), horario.getApertura(), horario.getCierre()));
        }
        // Asignamos los horarios a la cancha
        cancha.setHorarios(horarioCanchaCreados);
        // Creamos la cancha
        Cancha canchaCreada = canchaRepository.save(cancha);
        if (canchaCreada != null && canchaCreada.getId() != null) {
            return canchaCreada;
        }
        throw new Exception("No se pudo crear la cancha");
    }

    /**
     * Método para actualizar una cancha
     * @param cancha
     * @param horarios
     * @return
     * @throws Exception
     */
    public Cancha actualizarCancha(Cancha cancha, List<HorarioCancha> horarios) throws Exception {
        // Validar que la cancha tenga un id
        if (cancha.getId() == null || cancha == null) {
            throw new Exception("Id invalido");
        }
        // Buscar la cancha en base al id y validar que exista
        Cancha canchaEncontrada = canchaRepository.findById(cancha.getId()).orElse(null);
        if (canchaEncontrada == null) {
            throw new Exception("No se encontró la cancha");
        }
        // Validar el modelo de la cancha
        this.validarModelo(cancha);
        // Actualizar la cancha
        // Actualizar los datos de la cancha
        canchaEncontrada.setDescripcion(cancha.getDescripcion());
        canchaEncontrada.setCostoHora(cancha.getCostoHora());
        // Eliminar los horarios anteriores
        canchaEncontrada.getHorarios().clear();
        // Actualizar o agregar los nuevos horarios
        for (HorarioCancha horario : horarios) {
            HorarioCancha horarioEncontrado = horarioCanchaService
                    .obtenerHorarioPorDiaYCancha(diaService.getDiaByNombre(horario.getDia().getNombre()), canchaEncontrada);
            // Si el horario ya existe, se actualiza
            if (horarioEncontrado != null) {
                horarioEncontrado.setApertura(horario.getApertura());
                horarioEncontrado.setCierre(horario.getCierre());
                canchaEncontrada.getHorarios().add(horarioEncontrado);
                //Si el horario encontrado esta eliminado, se activa
                if (horarioEncontrado.getDeletedAt() != null) {
                    horarioEncontrado.setDeletedAt(null);
                }
            } else {
                // Si el horario no existe, se crea
                canchaEncontrada.getHorarios().add(new HorarioCancha(canchaEncontrada, diaService.getDiaByNombre(horario.getDia().getNombre()), horario.getApertura(), horario.getCierre()));
            }
        }
        // Guardar los cambios
        Cancha canchaActualizada = canchaRepository.save(canchaEncontrada);
        if (canchaActualizada != null && canchaActualizada.getId() != null) {
            return canchaActualizada;
        }
        throw new Exception("No se pudo actualizar la cancha");
    }

    /**
     * Método para obtener una cancha por su id
     * @param id
     * @return
     * @throws Exception
     */
    public Cancha getCanchaById(Long id) throws Exception {
        Cancha cancha = canchaRepository.findById(id).orElse(null);
        if (cancha == null) {
            throw new Exception("No se encontró la cancha");
        }
        return cancha;
    }

    /**
     * Método para eliminar una cancha por su id
     * @param id
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteCanchaById(Long id) throws Exception {
        Cancha cancha = canchaRepository.findById(id).orElse(null);
        if (cancha == null) {
            throw new Exception("No se encontró la cancha");
        }
        canchaRepository.deleteCanchaById(id);
    }

    public int countCanchas() {
        return (int) canchaRepository.count();
    }

    public List<Cancha> getCanchas() {
        return this.ignorarEliminados(canchaRepository.findAll());
    }
}
