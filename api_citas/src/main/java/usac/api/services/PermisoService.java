/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import usac.api.models.Permiso;
import usac.api.repositories.PermisoRepository;

/**
 *
 * @author Luis Monterroso
 */
@org.springframework.stereotype.Service
public class PermisoService extends Service {

    @Autowired
    private PermisoRepository permisoRepository;

    /**
     * Obtiene un permiso por medio de su id
     *
     * @param permisoId
     * @return
     * @throws Exception
     */
    public Permiso getPermisoById(Permiso permisoId) throws Exception {
        this.validarId(permisoId);//validar id
        Permiso permiso = this.permisoRepository.findById(permisoId.getId()).orElse(null);
        this.validarNull(permiso);
        return permiso;
    }

}
