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
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import usac.api.enums.PermisoEnum;
import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.HorarioCancha;
import usac.api.models.HorarioEmpleado;
import usac.api.models.Negocio;
import usac.api.models.Permiso;
import usac.api.models.Rol;
import usac.api.models.Servicio;
import usac.api.models.Usuario;
import usac.api.models.request.NuevoEmpleadoRequest;
import usac.api.models.request.RolPermisoUpdateRequest;
import usac.api.repositories.DiaRepository;
import usac.api.repositories.PermisoRepository;
import usac.api.repositories.RolRepository;
import usac.api.repositories.UsuarioRepository;
import usac.api.services.B64Service;
import usac.api.services.CanchaService;
import usac.api.services.DiaService;
import usac.api.services.EmpleadoService;
import usac.api.services.NegocioService;
import usac.api.services.RolService;
import usac.api.services.ServicioService;
import usac.api.services.UsuarioService;

/**
 * @author Luis Monterroso
 */
@Component
public class SeedersConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private RolService rolService;
    @Autowired
    private PermisoRepository permisoRepository;
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
    @Autowired
    private ServicioService servicioService;
    @Autowired
    private B64Service b64Service;
    @Autowired
    private UsuarioRepository usuarioRepository;

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
            Optional<Dia> opDia = this.diaRepository.findOneByNombreIgnoreCase(dia.getNombre());
            if (opDia.isPresent()) {
                return opDia.get();
            }
            return this.diaRepository.save(dia);
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    public Permiso insertarPermisoSiNoExiste(Permiso pa) {
        Permiso permiso
                = this.permisoRepository.findOneByNombre(pa.getNombre()).orElse(null);
        if (permiso == null) {
            return this.permisoRepository.save(
                    pa);
        }
        return permiso;
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
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (usuarioRepository.count() != 0) {
            return;
        }
        try {
            // Creacion de los dias de la semana
            Dia lunes = this.insertarDia(new Dia("Lunes"));
            Dia martes = this.insertarDia(new Dia("Martes"));
            Dia miercoles = this.insertarDia(new Dia("Miércoles"));
            Dia jueves = this.insertarDia(new Dia("Jueves"));
            Dia viernes = this.insertarDia(new Dia("Viernes"));
            Dia sabado = this.insertarDia(new Dia("Sábado"));
            Dia domingo = this.insertarDia(new Dia("Domingo"));

            // siders roles
            Rol clienteRol = this.insertarRol(new Rol("CLIENTE"));
            Rol adminRol = this.insertarRol(new Rol("ADMIN"));
            Rol empleadoRol = this.insertarRol(new Rol("EMPLEADO"));
            Rol customRol = this.insertarRol(new Rol("Custom"));
            Rol customRol2 = this.insertarRol(new Rol("Custom2"));
            Rol customRol3 = this.insertarRol(new Rol("Custom3"));
            Rol customRol4 = this.insertarRol(new Rol("Custom4"));
            Rol customRol5 = this.insertarRol(new Rol("Custom5"));

            // Seeder de usuarios del sistema
            Usuario admin = new Usuario("456123789", "3322114455669",
                    "89456123",
                    "admin@admin.com",
                    "admin", "admin",
                    "12345");

            Usuario admin2 = new Usuario(
                    "777666555",
                    "1234567891233",
                    "12345678",
                    "admin2@admin.com",
                    "Carlos",
                    "Pac",
                    "12345");

            // 5 Usuarios de prueba en modo cliente
            Usuario cliente1 = new Usuario("123456789", "1234567891234",
                    "12345679",
                    "usuarioprueba1@email.com",
                    "Usuario", "Prueba1",
                    "12345");

            Usuario cliente2 = new Usuario("987654321", "9876543214321",
                    "98765432",
                    "usuarioprueba2@email.com",
                    "Usuario", "Prueba2",
                    "12345");

            Usuario cliente3 = new Usuario("456789123", "4567891238765",
                    "45678912",
                    "usuarioprueba3@email.com",
                    "Usuario", "Prueba3",
                    "12345");

            Usuario cliente4 = new Usuario("321654987", "3216549870987",
                    "32165498",
                    "usuarioprueba4@email.com",
                    "Usuario", "Prueba4",
                    "12345");

            Usuario cliente5 = new Usuario("654987321", "6549873217654",
                    "65498732",
                    "usuarioprueba5@email.com",
                    "Usuario", "Prueba5",
                    "12345");

            Usuario empleado1 = new Usuario("987653321", "9876543219876",
                    "98763432",
                    "empleadoprueba1@email.com",
                    "Empleado", "Prueba1",
                    "12345");

            Usuario empleado2 = new Usuario("123456780", "1234567801234",
                    "12385678",
                    "empleadoprueba2@email.com",
                    "Empleado", "Prueba2",
                    "12345");

            Usuario empleado3 = new Usuario("876543219", "8765432198765",
                    "87654321",
                    "empleadoprueba3@email.com",
                    "Empleado", "Prueba3",
                    "12345");

            Usuario empleado4 = new Usuario("135792468", "1357924681357",
                    "13579246",
                    "empleadoprueba4@email.com",
                    "Empleado", "Prueba4",
                    "12345");

            Usuario empleado5 = new Usuario("246801357", "2468013572468",
                    "24680135",
                    "empleadoprueba5@email.com",
                    "Empleado", "Prueba5",
                    "12345");

            Usuario empleado6 = new Usuario("2462113", "2468014272468",
                    "24680136",
                    "empleadoprueba6@email.com",
                    "Empleado", "Prueba6",
                    "12345");

            Usuario empleado7 = new Usuario("2469224", "2456781567246",
                    "40930136",
                    "empleadoprueba7@email.com",
                    "Empleado", "Prueba7",
                    "12345");

            Usuario empleado8 = new Usuario("2468902", "2468013472434",
                    "24032313",
                    "empleadoprueba8@email.com",
                    "Empleado", "Prueba8",
                    "12345");

            Usuario empleado9 = new Usuario("2468013", "2468013572488",
                    "24623476",
                    "empleadoprueba9@email.com",
                    "Empleado", "Prueba9",
                    "12345");

            Usuario empleado10 = new Usuario("2468033", "2468013234468",
                    "24422013",
                    "empleadoprueba10@email.com",
                    "Empleado", "Prueba10",
                    "12345");

            ArrayList<HorarioEmpleado> horarios = new ArrayList<>();
            horarios.add(new HorarioEmpleado(lunes, null, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            horarios.add(new HorarioEmpleado(martes, null, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            horarios.add(new HorarioEmpleado(miercoles, null, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            horarios.add(new HorarioEmpleado(jueves, null, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            horarios.add(new HorarioEmpleado(viernes, null, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            try {
                this.usuarioService.crearAdministrador(admin);
                this.usuarioService.crearAdministrador(admin2);

                this.usuarioService.crearUsuarioCliente(cliente1);
                this.usuarioService.crearUsuarioCliente(cliente2);
                this.usuarioService.crearUsuarioCliente(cliente3);
                this.usuarioService.crearUsuarioCliente(cliente4);
                this.usuarioService.crearUsuarioCliente(cliente5);

                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado1, horarios, customRol));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado2, horarios, customRol2));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado3, horarios, customRol3));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado4, horarios, customRol4));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado5, horarios, customRol5));

                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado6, horarios, customRol));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado7, horarios, customRol));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado8, horarios, customRol));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado9, horarios, customRol));
                this.usuarioService.crearEmpleado(new NuevoEmpleadoRequest(empleado10, horarios, customRol));

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
            // Se crea el modelo del negocio de la APP
            // Cargamos la imagen que esta en resources/images/logo.svg y la convertimos a
            // base64
            String logo = cargarImagenComoBase64("logo.svg");
            Negocio negocio = new Negocio(logo, "TiendaAyD", false,
                    "2da calle XXX-XXX-XX Quetgo", 5.0);
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
            ArrayList<HorarioCancha> horariosEmpleados = new ArrayList<>();
            horariosEmpleados.add(new HorarioCancha(null, lunes, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            horariosEmpleados.add(new HorarioCancha(null, martes, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            horariosEmpleados.add(new HorarioCancha(null, miercoles, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            horariosEmpleados.add(new HorarioCancha(null, jueves, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            horariosEmpleados.add(new HorarioCancha(null, viernes, LocalTime.of(8, 0), LocalTime.of(18, 0)));
            //Se crean las canchas
            try {
                //Verificamos si ya existen 5 canchas
                if (this.canchaService.countCanchas() < 5) {
                    this.canchaService.crearCancha(cancha1, horariosEmpleados);
                    this.canchaService.crearCancha(cancha2, horariosEmpleados);
                    this.canchaService.crearCancha(cancha3, horariosEmpleados);
                    this.canchaService.crearCancha(cancha4, horariosEmpleados);
                    this.canchaService.crearCancha(cancha5, horariosEmpleados);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            List<Permiso> permisos = new ArrayList<>();

            // Creacion de todos los permisos que tiene el sistema
            for (PermisoEnum permiso : PermisoEnum.values()) {

                Permiso insercion = this.insertarPermisoSiNoExiste(
                        new Permiso(
                                permiso.getNombrePermiso(),
                                permiso.getRuta()));
                permisos.add(insercion);
            }

            try {
                // Asignacion de permisos a los usuarios
                this.rolService.actualizarPermisosRol(new RolPermisoUpdateRequest(customRol.getId(), permisos));
                this.rolService.actualizarPermisosRol(new RolPermisoUpdateRequest(customRol2.getId(), permisos));
                this.rolService.actualizarPermisosRol(new RolPermisoUpdateRequest(customRol3.getId(), permisos));
                this.rolService.actualizarPermisosRol(new RolPermisoUpdateRequest(customRol4.getId(), permisos));
                this.rolService.actualizarPermisosRol(new RolPermisoUpdateRequest(customRol5.getId(), permisos));
            } catch (Exception e) {
            }

            String imagenEjemplo = cargarImagenComoBase64("imagen_ejemplo.jpg");
            // Creacion de los servicios de la tienda
            try {
                this.servicioService.crearServicio(new Servicio("Servicio 1", 1.0, imagenEjemplo, 100.0, "Descripcion del servicio 1", customRol, 2));
                this.servicioService.crearServicio(new Servicio("Servicio 2", 2.0, imagenEjemplo, 200.0, "Descripcion del servicio 2", customRol2, 6));
                this.servicioService.crearServicio(new Servicio("Servicio 3", 3.0, imagenEjemplo, 300.0, "Descripcion del servicio 3", customRol3, 3));
                this.servicioService.crearServicio(new Servicio("Servicio 4", 4.0, imagenEjemplo, 400.0, "Descripcion del servicio 4", customRol4, 5));
                this.servicioService.crearServicio(new Servicio("Servicio 5", 5.0, imagenEjemplo, 500.0, "Descripcion del servicio 5", customRol5, 5));
            } catch (Exception e) {
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
             *
             * }
             */
        } catch (Exception ex) {
            Logger.getLogger(SeedersConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String cargarImagenComoBase64(String imagen) {
        // Cargar el archivo desde resources
        try (InputStream inputStream = getClass().getResourceAsStream("/images/" + imagen)) {
            if (inputStream == null) {
                throw new IOException("No se pudo encontrar el archivo logo.svg en el directorio /resources/images");
            }
            // Leer todos los bytes del archivo
            byte[] bytes = inputStream.readAllBytes();
            // Convertir a Base64
            String base64 = Base64.getEncoder().encodeToString(bytes);
            if (this.b64Service.hasExtension(base64)) {
                return base64;
            } else {
                return this.b64Service.addExtension(base64);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar y convertir la imagen a Base64", e);
        }
    }
}
