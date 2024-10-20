package usac.api.repositories;

import org.springframework.stereotype.Repository;

import usac.api.models.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import usac.api.models.Rol;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    //Busqueda de usuario por email
    public Optional<Usuario> findByEmail(String email);

    public Optional<Usuario> findByTokenRecuperacion(String codigo);

    public Long deleteUsuarioById(Long id);

    public boolean existsByEmail(String email);

    public boolean existsByNit(String nit);

    public boolean existsByPhone(String phone);

    public boolean existsByCui(String cui);

    public boolean existsUsuarioByEmailAndIdNot(String email, Long id);

    public boolean existsUsuarioByNitAndIdNot(String nit, Long id);

    public boolean existsUsuarioByCuiAndIdNot(String nit, Long id);

    public boolean existsUsuarioByPhoneAndIdNot(String nit, Long id);

    @Query("SELECT u FROM Usuario u JOIN u.roles ru WHERE ru.rol = :rolAsociadoAlServicio")
    public List<Usuario> findUsuariosByRolUsuario_Rol(@Param("rolAsociadoAlServicio") Rol rolAsociadoAlServicio);

    @Override
    public List<Usuario> findAll();
    /*
    @Query("SELECT new com.ayd1.APIecommerce.models.dto.reports.ClienteFrecuenteDto("
            + "df.usuario.id, "
            + "df.usuario.nombres, "
            + "df.usuario.apellidos, COUNT(v.id), SUM(v.valorTotal),"
            + " SUM(v.valorTotal) / COUNT(v.id)) "
            + "FROM DatosFacturacion df "
            + "JOIN df.venta v "
            + "WHERE (:fechaInicio IS NULL OR v.createdAt >= :fechaInicio) "
            + "AND (:fechaFin IS NULL OR v.createdAt <= :fechaFin) "
            + "GROUP BY df.usuario.id "
            + "ORDER BY SUM(v.valorTotal) DESC")
    List<ClienteFrecuenteDto> obtenerReporteClientesFrecuentes(
            @Param("fechaInicio") Instant fechaInicio,
            @Param("fechaFin") Instant fechaFin);*/

}
