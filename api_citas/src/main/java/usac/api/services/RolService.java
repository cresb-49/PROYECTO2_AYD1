/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import usac.api.models.Rol;
import usac.api.repositories.RolRepository;

/**
 *
 * @author Luis Monterroso
 */
@org.springframework.stereotype.Service
public class RolService extends Service {

    @Autowired
    private RolRepository rolRepository;

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
        if (rol == null) {
            throw new Exception("Rol no encontrado.");
        }

        return rol;
    }
}
