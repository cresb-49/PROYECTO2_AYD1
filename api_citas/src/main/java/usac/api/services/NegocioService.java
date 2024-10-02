package usac.api.services;

import java.time.LocalTime;
import java.util.ArrayList;

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

    public Negocio obtenerNegocio() {
        return negocioRepository.findFirstByOrderByIdAsc().orElse(null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Negocio actualizarNegocio(Negocio negocio) throws Exception {
        // Validar que el negocio tenga un id
        if(negocio.getId() == null || negocio == null) {
            throw new Exception("Id invalido");
        }
        // Buscar el negocio en base al id y validar que exista
        Negocio negocioEncontrado = negocioRepository.findById(negocio.getId()).orElse(null);
        if(negocioEncontrado == null) {
            throw new Exception("No se encontró el negocio");
        }
        // Validar el modelo del negocio
        this.validarModelo(negocio);
        // Actualizar el negocio
        Negocio negocioUpdate =  negocioRepository.save(negocio);
        if(negocioUpdate != null && negocioUpdate.getId() != null) {
            return negocioUpdate;
        }        
        throw new Exception("No logramos actualizar el negocio, inténtalo más tarde.");
    }

    @Transactional(rollbackOn = Exception.class)
    public Negocio CrearNegocio(Negocio negocio,ArrayList<Dia>horario) throws Exception {
        //Solo puede existir un negocio
        if(negocioRepository.count() > 0) {
            throw new Exception("Ya existe un negocio registrado");
        }
        // Validar el modelo del negocio
        this.validarModelo(negocio);
        // Asignar el horario al negocio
        ArrayList<HorarioNegocio> horarioCreado = new ArrayList<>();
        for(Dia dia: horario) {
            horarioCreado.add(new HorarioNegocio(dia, negocio,LocalTime.of(8,0),LocalTime.of(18,0)));
        }
        negocio.setHorarios(horarioCreado);
        // Crear el negocio
        Negocio negocioCreado = negocioRepository.save(negocio);
        if(negocioCreado != null && negocioCreado.getId() != null) {
            return negocioCreado;
        }
        throw new Exception("No logramos crear el negocio, inténtalo más tarde.");
    }
}
