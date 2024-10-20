package usac.api.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.Empleado;
import usac.api.models.Rol;
import usac.api.models.Servicio;
import usac.api.repositories.RolRepository;
import usac.api.repositories.ServicioRepository;

@Service
public class ServicioService extends usac.api.services.Service {
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private EmpleadoService empleadoService;

    /**
     * Obtiene un servicio por su id
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public Servicio getServicioById(Long id) throws Exception {
        Servicio servicio = this.servicioRepository.findById(id).orElse(null);
        this.validarNull(servicio);
        return servicio;
    }

    /**
     * Obtiene todos los servicios
     * 
     * @return
     */
    public List<Servicio> getServicios() {
        List<Servicio> servicios = this.servicioRepository.findAll();
        servicios = this.ignorarEliminados(servicios);
        return servicios;
    }

    /**
     * Obtiene los servicios que contienen una cadena en su nombre
     * 
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
     * 
     * @param id
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    public void eliminarServicio(Long id) throws Exception {
        Servicio servicio = this.servicioRepository.findById(id).orElse(null);
        if (servicio == null) {
            throw new Exception("No se encontro el servicio");
        }
        this.servicioRepository.deleteServicioById(servicio.getId());
    }

    /**
     * Actualiza un servicio
     * 
     * @param servicio
     * @return
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    public Servicio actualizarServicio(Servicio servicio) throws Exception {
        if (servicio.getId() == null || servicio == null) {
            throw new Exception("El servicio no tiene un id valido");
        }
        // Obtenemos el servicio actual
        Servicio servicioActual = this.servicioRepository.findById(servicio.getId()).orElse(null);
        if (servicioActual == null) {
            throw new Exception("No se encontro el servicio");
        }
        // Verificamos si hay un rol asociado al servicio
        if (servicio.getRol() == null) {
            throw new Exception("El servicio debe tener un rol asociado");
        }
        // Verificamos si el rol asociado al servicio existe
        if (servicio.getRol().getId() == null) {
            throw new Exception("El rol asociado al servicio no tiene un id valido");
        }
        // Obtenemos el rol actual
        Rol rolActual = this.rolRepository.findById(servicio.getRol().getId()).orElse(null);
        if (rolActual == null) {
            throw new Exception("No se encontro el rol asociado al servicio");
        }
        // Colocamos los nuevos valores
        servicioActual.setNombre(servicio.getNombre());
        servicioActual.setDuracion(servicio.getDuracion());
        servicioActual.setImagen(servicio.getImagen());
        servicioActual.setCosto(servicio.getCosto());
        servicioActual.setDetalles(servicio.getDetalles());
        servicioActual.setRol(rolActual);
        // Verificamos el modelo
        this.validarModelo(servicioActual);
        Servicio servicioActualizado = this.servicioRepository.save(servicioActual);
        return servicioActualizado;
    }

    /**
     * Crea un servicio
     * 
     * @param servicio
     * @return
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    public Servicio crearServicio(Servicio servicio) throws Exception {
        if (servicio.getId() != null) {
            throw new Exception("El servicio ya tiene un id");
        }
        this.validarModelo(servicio);
        Servicio servicioCreado = this.servicioRepository.save(servicio);
        return servicioCreado;
    }

    public List<Empleado> empleadosAsociadosAlServicio(Long id) throws Exception {
        Servicio servicio = this.servicioRepository.findById(id).orElse(null);
        if (servicio == null) {
            throw new Exception("No se encontro el servicio");
        }
        List<Empleado> empleados = empleadoService.getEmpleadosByRolId(servicio.getRol().getId());
        return empleados;
    }
}
