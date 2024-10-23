/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package usac.api.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import usac.api.models.TokenAuth;

/**
 *
 * @author Luis Monterroso
 */
@Repository
public interface TokenAuthRepository extends CrudRepository<TokenAuth, Long> {

    public Optional<TokenAuth> findOneByToken(String token);

    public Optional<TokenAuth> findOneByEmail(String token);

    public Boolean existsByEmail(String email);

    public Long deleteByToken(String token);

    /**
     * Encuentra todos los tokens cuya fecha de expiración haya pasado.
     *
     * @param fechaExpiracion La fecha actual para comparar.
     * @return Lista de tokens expirados.
     */
    public List<TokenAuth> findAllByFechaHoraExpiracionBefore(LocalDateTime fechaExpiracion);
}
