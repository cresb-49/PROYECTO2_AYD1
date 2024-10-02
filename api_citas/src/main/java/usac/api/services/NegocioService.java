package usac.api.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Negocio CrearNegocio(Negocio negocio) throws Exception {
        // Validar el modelo del negocio
        this.validarModelo(negocio);
        // Crear el negocio
        Negocio negocioCreado = negocioRepository.save(negocio);
        if(negocioCreado != null && negocioCreado.getId() != null) {
            return negocioCreado;
        }
        throw new Exception("No logramos crear el negocio, inténtalo más tarde.");
    }
}
