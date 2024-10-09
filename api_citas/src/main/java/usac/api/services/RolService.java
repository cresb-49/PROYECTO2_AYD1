/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import usac.api.models.Permiso;
import usac.api.models.Rol;
import usac.api.models.RolPermiso;
import usac.api.models.request.RolCreateRequest;
import usac.api.models.request.RolPermisoUpdateRequest;
import usac.api.repositories.RolRepository;

/**
 *
 * @author Luis Monterroso
 */
@org.springframework.stereotype.Service
public class RolService extends Service {
    
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PermisoService permisoService;

    /**
     * Busca un rol por su nombre
     *
     * @param nombre
     * @return
     * @throws Exception
     */
    public Rol getRolByNombre(String nombre) throws Exception {
        Rol rol = this.rolRepository.findOneByNombre(nombre).orElse(null);
        // si el rol no existe lanzamos error
        this.validarNull(rol);
        return rol;
    }

    /**
     * Busca un rol por su nombre
     *
     * @param idRol
     * @return
     * @throws Exception
     */
    public Rol getRolById(Long idRol) throws Exception {
        //validamos el id
        this.validarId(new Rol(idRol));
        // Buscamos el usuario en la base de datos
        Rol rol = rolRepository.findById(idRol).orElse(null);
        // si el rol no existe lanzamos error
        this.validarNull(rol);
        return rol;
    }

    /**
     * Busca todos los roles excepto el admin, cliente, y empleado
     *
     * @return
     * @throws Exception
     */
    public List<Rol> getRoles() throws Exception {
        return rolRepository.findAllExcludingAdminEmpleadoCliente();
    }

    /**
     * Agrega permisos a un rol
     *
     * @param rolPermisoRequest
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    public Rol actualizarPermisosRol(RolPermisoUpdateRequest rolPermisoRequest) throws Exception {
        // Buscamos el rol en la base de datos
        Rol rol = this.getRolById(rolPermisoRequest.getIdRol());
        
        List<RolPermiso> permisosNuevos = new ArrayList<>();
        // Usamos un Set para evitar permisos duplicados por su id
        Set<Long> permisosIdsUnicos = new HashSet<>();
        // Por cada permiso que se haya especificado, creamos un nuevo permiso si su id no está duplicado
        for (Permiso item : rolPermisoRequest.getPermisos()) {
            // Traemos el permiso, se verifica si existe
            Permiso permiso = this.permisoService.getPermisoById(new Permiso(item.getId()));

            // Verificamos si el id del permiso ya ha sido agregado
            if (!permisosIdsUnicos.contains(permiso.getId())) {
                permisosNuevos.add(new RolPermiso(rol, permiso));
                permisosIdsUnicos.add(permiso.getId());  // Lo añadimos al Set para que no se repita
            }
        }
        
        if (rol.getPermisosRol() == null) {
            rol.setPermisosRol(permisosNuevos);
        } else {
            // asignamos los nuevos permisos al usuario
            rol.getPermisosRol().clear();
            rol.getPermisosRol().addAll(permisosNuevos);
        }
        // Guardamos el rol
        Rol saveRol = this.saveRol(rol);
        return saveRol;
    }
    
    @Transactional(rollbackOn = Exception.class)
    public Rol crearRol(RolCreateRequest createRequest) throws Exception {
        //validar el modelo de creacion
        this.validarModelo(createRequest);
        //validar el rol dentro del modelo de creacion
        this.validarModelo(createRequest.getRol());
        // guardamos el rol
        Rol saveRol = this.saveRol(createRequest.getRol());
        
        List<RolPermiso> permisos = new ArrayList<>();
        
        for (Permiso item : createRequest.getPermisos()) {
            this.validarId(item);
            Permiso permiso = this.permisoService.getPermisoById(item);
            permisos.add(new RolPermiso(saveRol, permiso));
        }
        
        createRequest.getRol().setPermisosRol(permisos);
        saveRol.setPermisosRol(permisos);
        
        Rol saveRolFinal = this.saveRol(saveRol);
        return saveRolFinal;
    }
    
    @Transactional(rollbackOn = Exception.class)
    public Rol actualizarRol(Rol rol) throws Exception {
        //validar el modelo de creacion
        this.validarModelo(rol);
        //validar id
        this.validarId(rol);
        
        Rol rolEnconrtrado = this.getRolById(rol.getId());
        
        if (this.rolRepository.existsByNombreAndIdNot(rol.getNombre(), rol.getId())) // guardamos el rol
        {
            throw new Exception("Ya existe un rol con el nombre especificado.");
        }
        
        rol.setPermisosRol(rolEnconrtrado.getPermisosRol());
        rol.setUsusarios(rolEnconrtrado.getUsusarios());
        
        Rol saveRolFinal = this.saveRol(rol);
        return saveRolFinal;
    }
    
    @Transactional(rollbackOn = Exception.class)
    private Rol saveRol(Rol rol) throws Exception {
        Rol rolSave = this.rolRepository.save(rol);
        
        this.validarNull(rolSave);
        if (rolSave.getId() <= 0) {
            throw new Exception("No pudimos guardar la informacion del rol.");
        }
        return rolSave;
    }
}
