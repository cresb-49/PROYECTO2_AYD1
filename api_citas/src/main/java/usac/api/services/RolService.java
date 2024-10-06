/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import usac.api.models.Permiso;
import usac.api.models.Rol;
import usac.api.models.RolPermiso;
import usac.api.models.request.RolPermisoRequest;
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
     * @param nombre
     * @return
     * @throws Exception
     */
    public Rol getRolById(Rol idRol) throws Exception {
        //validamos el id
        this.validarId(idRol);
        // Buscamos el usuario en la base de datos
        Rol rol = rolRepository.findById(idRol.getId()).orElse(null);
        // si el rol no existe lanzamos error
        this.validarNull(rol);
        return rol;
    }

    /**
     * Agrega permisos a un rol
     *
     * @param rolPermisoRequest
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    public Rol actualizarPermisosRol(RolPermisoRequest rolPermisoRequest) throws Exception {
        // Buscamos el rol en la base de datos
        Rol rol = this.getRolById(new Rol(rolPermisoRequest.getIdRol()));

        List<RolPermiso> permisosNuevos = new ArrayList<>();
        // por cada permiso que se haya especificado creamos un nuevo permiso
        for (Permiso item : rolPermisoRequest.getPermisos()) {
            //mandamos a traer el permiso, se verifica si existe
            Permiso permiso = this.permisoService.getPermisoById(
                    new Permiso(item.getId()));
            permisosNuevos.add(new RolPermiso(
                    rol, permiso));
        }

        if (rol.getPermisosRol() == null) {
            rol.setPermisosRol(permisosNuevos);
        } else {
            // asignamos los nuevos permisos al usuario
            rol.getPermisosRol().clear();
            rol.getPermisosRol().addAll(permisosNuevos);
        }
        // Guardamos el usuario
        this.saveRol(rol);
        return rol;
    }

    @Transactional(rollbackOn = Exception.class)
    private String saveRol(Rol rol) throws Exception {
        Rol rolSave = this.rolRepository.save(rol);

        this.validarNull(rolSave);

        if (rolSave.getId() <= 0) {
            throw new Exception("No pudimos guardar la informacion del rol.");
        }

        return "Se guardo la informacion del rol con exito.";
    }
}
