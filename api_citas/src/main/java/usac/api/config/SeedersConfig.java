/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.HorarioCancha;
import usac.api.models.Negocio;
import usac.api.models.Rol;
import usac.api.models.TipoEmpleado;
import usac.api.models.Usuario;
import usac.api.models.request.NuevoEmpleadoRequest;
import usac.api.repositories.DiaRepository;
import usac.api.repositories.RolRepository;
import usac.api.services.CanchaService;
import usac.api.services.DiaService;
import usac.api.services.EmpleadoService;
import usac.api.services.NegocioService;
import usac.api.services.UsuarioService;

/**
 * @author Luis Monterroso
 */
@Component
public class SeedersConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private NegocioService negocioService;
    @Autowired
    private DiaService diaService;
    @Autowired
    private DiaRepository diaRepository;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private CanchaService canchaService;

    public Rol insertarRol(Rol rol) throws Exception {
        try {
            Optional<Rol> opRol = this.rolRepository.findOneByNombre(rol.getNombre());
            if (opRol.isPresent()) {
                return opRol.get();
            }
            return this.rolRepository.save(rol);
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    public Dia insertarDia(Dia dia) throws Exception {
        try {
            Optional<Dia> opDia = this.diaRepository.findOneByNombre(dia.getNombre());
            if (opDia.isPresent()) {
                return opDia.get();
            }
            return this.diaRepository.save(dia);
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    /*
     *
     * public EstadoEnvio insertarEstadoEnvio(EstadoEnvio estadoEnvio) throws
     * Exception {
     * try {
     * Optional<EstadoEnvio> opRol =
     * this.estadoEnvioRepository.findOneByNombre(estadoEnvio.getNombre());
     * if (opRol.isPresent()) {
     * return opRol.get();
     * }
     * return this.estadoEnvioRepository.save(estadoEnvio);
     * } catch (Exception e) {
     * throw new Exception("Error");
     * }
     * }
     *
     * public Categoria insertarCategoria(Categoria cat) throws Exception {
     * try {
     * Optional<Categoria> opCat =
     * this.categoriaRepository.findOneByNombre(cat.getNombre());
     * if (opCat.isPresent()) {
     * return opCat.get();
     * }
     * return this.categoriaRepository.save(cat);
     * } catch (Exception e) {
     * throw new Exception("Error");
     * }
     * }
     *
     * public TiendaConfig insertarTiendaConfig(TiendaConfig config) throws
     * Exception {
     * try {
     * TiendaConfig conf =
     * this.tiendaConfigReporitory.findFirstByOrderByIdAsc().orElse(null);
     * if (conf == null) {
     * return this.tiendaConfigReporitory.save(config);
     * }
     * return conf;
     *
     * } catch (Exception e) {
     * throw new Exception("Error");
     * }
     * }
     *
     * public Permiso insertarPermisoSiNoExiste(Permiso pa) {
     * Permiso permiso =
     * this.permisoRepository.findOneByNombre(pa.getNombre()).orElse(null);
     * if (permiso == null) {
     * return this.permisoRepository.save(
     * pa);
     * }
     * return permiso;
     * }
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            // Creacion de los dias de la semana
            Dia lunes = this.insertarDia(new Dia("Lunes"));
            Dia martes = this.insertarDia(new Dia("Martes"));
            Dia miercoles = this.insertarDia(new Dia("Miercoles"));
            Dia jueves = this.insertarDia(new Dia("Jueves"));
            Dia viernes = this.insertarDia(new Dia("Viernes"));
            Dia sabado = this.insertarDia(new Dia("Sabado"));
            Dia domingo = this.insertarDia(new Dia("Domingo"));

            // siders roles
            Rol clienteRol = this.insertarRol(new Rol("CLIENTE"));
            Rol adminRol = this.insertarRol(new Rol("ADMIN"));
            Rol empleadoRol = this.insertarRol(new Rol("EMPLEADO"));

            // Seeder de usuarios del sistema
            Usuario admin = new Usuario("456123789", "3322114455669",
                    "89456123",
                    "elrincondelgamer77@gmail.com",
                    "admin", "admin",
                    "12345", adminRol);

            Usuario admin2 = new Usuario(
                    "777666555",
                    "1234567891233",
                    "12345678",
                    "carlosbpac@gmail.com",
                    "Carlos",
                    "Pac",
                    "12345", adminRol);

            // 5 Usuarios de prueba en modo cliente
            Usuario cliente1 = new Usuario("123456789", "1234567891234",
                    "12345679",
                    "usuarioprueba1@email.com",
                    "Usuario", "Prueba1",
                    "12345", clienteRol);

            Usuario cliente2 = new Usuario("987654321", "9876543214321",
                    "98765432",
                    "usuarioprueba2@email.com",
                    "Usuario", "Prueba2",
                    "12345", clienteRol);

            Usuario cliente3 = new Usuario("456789123", "4567891238765",
                    "45678912",
                    "usuarioprueba3@email.com",
                    "Usuario", "Prueba3",
                    "12345", clienteRol);

            Usuario cliente4 = new Usuario("321654987", "3216549870987",
                    "32165498",
                    "usuarioprueba4@email.com",
                    "Usuario", "Prueba4",
                    "12345", clienteRol);

            Usuario cliente5 = new Usuario("654987321", "6549873217654",
                    "65498732",
                    "usuarioprueba5@email.com",
                    "Usuario", "Prueba5",
                    "12345", clienteRol);

            Usuario empleado1 = new Usuario("987653321", "9876543219876",
                    "98763432",
                    "empleadoprueba1@email.com",
                    "Empleado", "Prueba1",
                    "12345", empleadoRol);

            Usuario empleado2 = new Usuario("123456780", "1234567801234",
                    "12385678",
                    "empleadoprueba2@email.com",
                    "Empleado", "Prueba2",
                    "12345", empleadoRol);

            Usuario empleado3 = new Usuario("876543219", "8765432198765",
                    "87654321",
                    "empleadoprueba3@email.com",
                    "Empleado", "Prueba3",
                    "12345", empleadoRol);

            Usuario empleado4 = new Usuario("135792468", "1357924681357",
                    "13579246",
                    "empleadoprueba4@email.com",
                    "Empleado", "Prueba4",
                    "12345", empleadoRol);

            Usuario empleado5 = new Usuario("246801357", "2468013572468",
                    "24680135",
                    "empleadoprueba5@email.com",
                    "Empleado", "Prueba5",
                    "12345", empleadoRol);

            TipoEmpleado tipoEmpleado1 = new TipoEmpleado("Organizador");
            TipoEmpleado tipoEmpleado2 = new TipoEmpleado("Limpieza");
            TipoEmpleado tipoEmpleado3 = new TipoEmpleado("Seguridad");
            TipoEmpleado tipoEmpleado4 = new TipoEmpleado("Cocina");
            TipoEmpleado tipoEmpleado5 = new TipoEmpleado("Recepcion");

            try {
                this.empleadoService.createTipoEmpleado(tipoEmpleado1);
                this.empleadoService.createTipoEmpleado(tipoEmpleado2);
                this.empleadoService.createTipoEmpleado(tipoEmpleado3);
                this.empleadoService.createTipoEmpleado(tipoEmpleado4);
                this.empleadoService.createTipoEmpleado(tipoEmpleado5);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            try {
                this.usuarioService.crearAdministrador(admin);
                this.usuarioService.crearAdministrador(admin2);
                this.usuarioService.crearUsuarioCliente(cliente1);
                this.usuarioService.crearUsuarioCliente(cliente2);
                this.usuarioService.crearUsuarioCliente(cliente3);
                this.usuarioService.crearUsuarioCliente(cliente4);
                this.usuarioService.crearUsuarioCliente(cliente5);

                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado1, tipoEmpleado1));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado2, tipoEmpleado2));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado3, tipoEmpleado3));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado4, tipoEmpleado4));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado5, tipoEmpleado5));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            // Se crea el modelo del negocio de la APP
            // Cargamos la imagen que esta en resources/images/logo.svg y la convertimos a
            // base64
            String logo = cargarImagenComoBase64();
            Negocio negocio = new Negocio(logo, "TiendaAyD", false, "2da calle XXX-XXX-XX Quetgo");
            try {
                ArrayList<Dia> horario = new ArrayList<>();
                horario.add(lunes);
                horario.add(martes);
                horario.add(miercoles);
                horario.add(jueves);
                horario.add(viernes);
                this.negocioService.CrearNegocio(negocio, horario);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            //Inicio de datos de las canchas
            Cancha cancha1 = new Cancha(100.0, "Cancha de futbol 5");
            Cancha cancha2 = new Cancha(150.0, "Cancha de futbol 7");
            Cancha cancha3 = new Cancha(200.0, "Cancha de futbol 11 con cesped natural");
            Cancha cancha4 = new Cancha(250.0, "Cancha de futbol 11 con cesped sintetico");
            Cancha cancha5 = new Cancha(300.0, "Cancha de futbol 11 con cesped artificial");
            //Horario generico para las canchas
            ArrayList<HorarioCancha> horarios = new ArrayList<>();
            horarios.add(new HorarioCancha(null, lunes, LocalTime.of(8, 0), LocalTime.of(23, 0)));
            horarios.add(new HorarioCancha(null, martes, LocalTime.of(8, 0), LocalTime.of(23, 0)));
            horarios.add(new HorarioCancha(null, miercoles, LocalTime.of(8, 0), LocalTime.of(23, 0)));
            horarios.add(new HorarioCancha(null, jueves, LocalTime.of(8, 0), LocalTime.of(23, 0)));
            horarios.add(new HorarioCancha(null, viernes, LocalTime.of(8, 0), LocalTime.of(23, 0)));
            horarios.add(new HorarioCancha(null, sabado, LocalTime.of(8, 0), LocalTime.of(17, 0)));
            horarios.add(new HorarioCancha(null, domingo, LocalTime.of(8, 0), LocalTime.of(17, 0)));
            //Se crean las canchas
            try {
                //Verificamos si ya existen 5 canchas
                if (this.canchaService.countCanchas() < 5) {
                    this.canchaService.crearCancha(cancha1, horarios);
                    this.canchaService.crearCancha(cancha2, horarios);
                    this.canchaService.crearCancha(cancha3, horarios);
                    this.canchaService.crearCancha(cancha4, horarios);
                    this.canchaService.crearCancha(cancha5, horarios);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            /*
             * Categoria categoria = new Categoria("Hogar");
             * this.insertarCategoria(categoria);
             *
             * //estado de envio
             * EstadoEnvio estadoEnvio = new EstadoEnvio("PENDIENTE");
             * this.insertarEstadoEnvio(estadoEnvio);
             * EstadoEnvio estadoEnvio2 = new EstadoEnvio("ENTREGADO");
             * this.insertarEstadoEnvio(estadoEnvio2);
             *
             * // IMAGEN DEFAULT DE LA TIENDA
             * byte[] img = getClass().getResourceAsStream("/img/logo.png").readAllBytes();
             *
             * TiendaConfig tiendaConfig = new TiendaConfig("TiendaAyD1", img, 12.00,
             * "2da calle XXX-XXX-XX Quetgo",
             * "image/png", 25.0);
             * this.insertarTiendaConfig(tiendaConfig);
             *
             * Usuario user1 = new Usuario("Carlos",
             * "Pac", "carlosbpac@gmail.com",
             * null, "12345",
             * null, null, false);
             *
             * Usuario ayudante = new Usuario("Luis",
             * "Monterroso", "luismonteg1@hotmail.com",
             * null, "12345",
             * null, null, false);
             *
             *
             * // Crear los usuarios, un catch por usuario para que ignore las * excepciones
             * que puedan haber
             *
             * try {
             * this.usuarioService.crearAdministrador(admin);
             * } catch (Exception e) {
             * }
             * try {
             * this.usuarioService.crearUsuarioNormal(user1);
             *
             * } catch (Exception e) {
             * }
             * try {
             * this.usuarioService.crearAyudante(
             * new UsuarioAyudanteRequest(ayudante, new ArrayList<>()));
             * } catch (Exception e) {
             * }
             *
             * // Creacion de todos los permisos que tiene el sistema
             * for (PermisoEnum permiso : PermisoEnum.values()) {
             *
             * Permiso insercion = this.insertarPermisoSiNoExiste(
             * new Permiso(
             * permiso.getNombrePermiso(),
             * permiso.getRuta()));
             * try {
             * // Asignacion de permisos a los usuarios
             * Usuario ayudante2 = this.usuarioService.getByEmail(
             * "luismonteg1@hotmail.com");
             * this.usuarioService.agregarPermisoUsuario(ayudante2,
             * insercion);
             * } catch (Exception e) {
             * }
             * }
             */
        } catch (Exception ex) {
            Logger.getLogger(SeedersConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String cargarImagenComoBase64() {
        // Cargar el archivo desde resources
        try (InputStream inputStream = getClass().getResourceAsStream("/images/logo.svg")) {
            if (inputStream == null) {
                throw new IOException("No se pudo encontrar el archivo logo.svg en el directorio /resources/images");
            }

            // Leer todos los bytes del archivo
            byte[] bytes = inputStream.readAllBytes();

            // Convertir a Base64
            return Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar y convertir la imagen a Base64", e);
        }
    }
}
