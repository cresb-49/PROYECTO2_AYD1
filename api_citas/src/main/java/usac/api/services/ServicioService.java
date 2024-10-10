package usac.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.Servicio;
import usac.api.repositories.ServicioRepository;

@Service
public class ServicioService extends usac.api.services.Service {
    @Autowired
    private ServicioRepository servicioRepository;

    /**
     * Obtiene un servicio por su id
     * @param id
     * @return
     * @throws Exception
     */
    public Servicio getServicioById(Long id) throws Exception {
        Servicio servicio =  this.servicioRepository.findById(id).orElse(null);
        this.validarNull(servicio);
        return servicio;
    }

    /**
     * Obtiene todos los servicios
     * @return
     */
    public List<Servicio> getServicios() {
        List<Servicio> servicios = this.servicioRepository.findAll();
        servicios = this.ignorarEliminados(servicios);
        return servicios;
    }

    /**
     * Obtiene los servicios que contienen una cadena en su nombre
     * @param nombre
     * @return
     */
    public List<Servicio> getServiciosLikeNombre(String nombre) {
        List<Servicio> servicios = this.servicioRepository.findByNombreContaining(nombre);
        servicios = this.ignorarEliminados(servicios);
        return servicios;
    }

    /**
     * Elimina un servicio por su id
     * @param id
     * @throws Exception
     */
    public void eliminarServicio(Long id) throws Exception {
        Servicio servicio = this.servicioRepository.findById(id).orElse(null);
        if(servicio == null) {
            throw new Exception("No se encontro el servicio");
        }
        this.servicioRepository.deleteServicioById(servicio.getId());
    }

    /**
     * Actualiza un servicio
     * @param servicio
     * @return
     * @throws Exception
     */
    public Servicio actualizarServicio(Servicio servicio) throws Exception {
        throw new Exception("No implementado");
        // if(servicio.getId() == null || servicio == null) {
        //     throw new Exception("El servicio no tiene un id valido");
        // }
        // this.validarModelo(servicio);
        // Servicio servicioActualizado = this.servicioRepository.save(servicio);
        // return servicioActualizado;
    }

    /**
     * Crea un servicio
     * @param servicio
     * @return
     * @throws Exception
     */
    public Servicio crearServicio(Servicio servicio) throws Exception {
        if(servicio.getId() != null) {
            throw new Exception("El servicio ya tiene un id");
        }
        this.validarModelo(servicio);
        Servicio servicioCreado = this.servicioRepository.save(servicio);
        return servicioCreado;
    }
}
