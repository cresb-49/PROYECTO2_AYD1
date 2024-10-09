package usac.api.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usac.api.models.Empleado;
import usac.api.models.TipoEmpleado;
import usac.api.repositories.EmpleadoRepository;
import usac.api.repositories.TipoEmpleadoRepository;

@Service
public class EmpleadoService extends usac.api.services.Service {
    @Autowired
    private TipoEmpleadoRepository tipoEmpleadoRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Transactional(rollbackOn = Exception.class)
    public TipoEmpleado createTipoEmpleado(TipoEmpleado tipoEmpleado) throws Exception {
        //Validamos el modelo
        this.validarModelo(tipoEmpleado);
        //Creamos el tipo de empleado
        TipoEmpleado tipoCreado = tipoEmpleadoRepository.save(tipoEmpleado);
        if (tipoCreado != null && tipoCreado.getId() != null) {
            return tipoCreado;
        }
        throw new Exception("No se pudo crear el tipo de empleado");
    }

    public TipoEmpleado getTipoEmpleadoByNombre(String nombre) throws Exception {
        TipoEmpleado tipoEmpleado = tipoEmpleadoRepository.findOneByNombre(nombre).orElse(null);
        if (tipoEmpleado == null) {
            throw new Exception("Tipo de empleado no encontrado");
        }
        return tipoEmpleado;
    }

    @Deprecated
    @Transactional(rollbackOn = Exception.class)
    public Empleado createEmpleado(Empleado empleado) throws Exception {
        //Validamos el modelo
        this.validarModelo(empleado);
        //Creamos el empleado
        Empleado empleadoCreado = empleadoRepository.save(empleado);
        if (empleadoCreado != null && empleadoCreado.getId() != null) {
            return empleadoCreado;
        }
        throw new Exception("No se pudo crear el empleado");
    }

    public List<Empleado> getEmpleados() {
        List<Empleado> empleados = this.ignorarEliminados(empleadoRepository.findAll());
        return empleados;
    }
}
