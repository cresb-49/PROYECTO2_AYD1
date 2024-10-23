/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import usac.api.models.TokenAuth;
import usac.api.repositories.TokenAuthRepository;

/**
 *
 * @author Luis Monterroso
 */
@org.springframework.stereotype.Service
public class TokenAuthService extends Service {

    @Autowired
    private TokenAuthRepository tokenAuthRepository;

    /**
     * Genera un token temporal para el email proporcionado.
     *
     * @param email El correo electrónico para el cual se genera el token.
     * @return El token generado.
     */
    @Transactional(rollbackOn = Exception.class)
    public TokenAuth generateToken(String email) throws Exception {
        //si el email ya existe entonces lanzar error
        if (tokenAuthRepository.existsByEmail(email)) {
            throw new Exception("El email ya tiene un token registrado.");
        }

        // Generar un token único (puedes usar UUID o algún generador más sofisticado)
        String token = UUID.randomUUID().toString();
        // fecha expiracion
        LocalDateTime fechaExpiracion = LocalDateTime.now().plusMinutes(5); // Expiración en 1 día
        // Asociar el token al email (podrías agregar una expiración usando alguna biblioteca como JWT)
        TokenAuth tokenObj = new TokenAuth(email, token, fechaExpiracion);
        return this.tokenAuthRepository.save(tokenObj);
    }

    /**
     * Valida si el token es válido y no ha vencido. Si el token ha vencido, lo
     * elimina y lanza una excepción.
     *
     * @param token El token que se desea validar.
     * @return El token válido que se encontró.
     * @throws Exception Si el token no se encuentra o ha expirado.
     */
    @Transactional(rollbackOn = Exception.class)
    public TokenAuth validateToken(String token) throws Exception {

        // Buscar el token en la base de datos
        TokenAuth tokenAuth = this.tokenAuthRepository.findOneByToken(token)
                .orElseThrow(() -> new Exception("Token no encontrado."));

        // Verificar si el token ha expirado
        if (tokenAuth.getFechaHoraExpiracion().isBefore(LocalDateTime.now())) {
            // Eliminar el token si ha expirado
            tokenAuthRepository.delete(tokenAuth);
            // Lanzar una excepción indicando que el token ha expirado
            throw new Exception("El token ha expirado.");
        }
        // Si el token es válido y no ha expirado, devolverlo
        return tokenAuth;
    }

    @Transactional(rollbackOn = Exception.class)
    public boolean eliminarToken(TokenAuth token) {
        Long eliminacion = this.tokenAuthRepository.deleteByToken(token.getToken());
        return eliminacion > 0;
    }

    /**
     * Método que se ejecuta cada 5 minutos para eliminar tokens expirados.
     */
    @Scheduled(fixedRate = 300000) // Cada 5 minutos (300,000 ms)
    public void eliminarTokensExpirados() {
        // Obtener la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();

        // Buscar todos los tokens que han expirado
        List<TokenAuth> tokensExpirados = tokenAuthRepository.findAllByFechaHoraExpiracionBefore(
                ahora);

        // Eliminar los tokens expirados
        if (!tokensExpirados.isEmpty()) {
            tokenAuthRepository.deleteAll(tokensExpirados);
            System.out.println("Tokens expirados eliminados: " + tokensExpirados.size());
        }
    }
}
