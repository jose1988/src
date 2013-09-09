/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Actividad;
import com.pangea.capadeservicios.servicios.GestionDeActividades_Service;
import com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.GestionDeInstancias_Service;
import com.pangea.capadeservicios.servicios.GestionDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.Instancia;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.WrActividad;
import com.pangea.capadeservicios.servicios.WrResultado;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;

/**
 * @author Pangea
 */
@ManagedBean(name = "actividadesUsuarioController")
@SessionScoped
public class actividadesUsuarioController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_2;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeInstancias.wsdl")
    private GestionDeInstancias_Service service;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_1;
    /*
     * Objeto de la clase actividad para mostrar la información de las actividades por instancia en una lista
     */
    private List<Actividad> actividades;
    /*
     * Objeto de la clase actividad para mostrar la información de las actividades por instancia
     */
    private Actividad act, actividadLibrar;
    long idAct;
    /*
     * Objeto de la clase usuario donde se guardara el objeto de la variable de sesión
     */
    Usuario usuarioLogueo;
    /*
     * Objeto de la clase sesión donde se guardara el objeto de la variable de sesión
     */
    Sesion sesionLogueo;

    /**
     *
     * @return
     */
    public Actividad getAct() {
        return act;
    }

    /**
     *
     * @param act
     */
    public void setAct(Actividad act) {
        this.act = act;
    }

    /**
     *
     * @return
     */
    public List<Actividad> getActividades() {
        return actividades;
    }

    /**
     *
     * @param Actividades
     */
    public void setActividades(List<Actividad> Actividades) {
        this.actividades = Actividades;
    }

    /**
     * Metodo constructor que se incia al hacer la llamada a la pagina
     * actividadesPorInstancia.xhml donde se mustra las actividades de una
     * determinada instancia
     */
    @PostConstruct
    public void init() {
        //codigo para guardar la lista de actividades por Instancia
        int j = 0;
        WrActividad envoltorioAbiertas, envoltorioPendientes, datosActividad;
        Actividad actividadesPendientes = new Actividad();
        actividadesPendientes.setEstado("pendiente");
        Actividad actividadesAbiertas = new Actividad();
        actividadesAbiertas.setEstado("abierta");
        usuarioLogueo = new Usuario();
        usuarioLogueo.setId("thunder");
        envoltorioAbiertas = consultarActividades(usuarioLogueo, actividadesAbiertas);
        envoltorioPendientes = consultarActividades(usuarioLogueo, actividadesPendientes);
        actividades = new ArrayList<Actividad>();
        if ((envoltorioAbiertas.getActividads().isEmpty() || envoltorioAbiertas.getActividads() == null) && (envoltorioPendientes.getActividads().isEmpty() || envoltorioPendientes.getActividads() == null)) {
            actividades = null;
        }
        while (envoltorioAbiertas.getActividads().size() > j) {
            datosActividad = consultarActividad(envoltorioAbiertas.getActividads().get(j));
            if (datosActividad.getEstatus().compareTo("OK") == 0) {
                act = datosActividad.getActividads().get(0);
                actividades.add(act);
            }
            j++;
        }
        j = 0;
        while (envoltorioPendientes.getActividads().size() > j) {
            datosActividad = consultarActividad(envoltorioPendientes.getActividads().get(j));
            if (datosActividad.getEstatus().compareTo("OK") == 0) {
                act = datosActividad.getActividads().get(0);
                actividades.add(act);
            }
            j++;
        }
    }

    public void liberarActividadUsuario() {
        actividadLibrar = new Actividad();
        actividadLibrar.setId(act.getId());
        WrResultado envoltorio = liberarActividad(actividadLibrar, usuarioLogueo);
        System.out.println("LIBROOOOOOOOOOOOOOOOOOO_______" + act.getId());
    }

    /**
     * Método para verificar si el usuario esta logueado
     *
     * @return un booleano si es true es por que si estaba logueado
     */
    public boolean verificarLogueo() {
        boolean bandera = false, sesionBd = false;
        try {
            //codigo para guardar sesion y usuario logueado, sino existe redireccionamos a index.xhtml
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            usuarioLogueo = (Usuario) (SesionAbierta.getAttribute("Usuario"));
            sesionLogueo = (Sesion) (SesionAbierta.getAttribute("Sesion"));
            sesionBd = logSesion(sesionLogueo);
            if (usuarioLogueo == null || sesionLogueo == null || !sesionBd) {
                bandera = true;
            }
        } catch (Exception e) {
            bandera = true;
        }

        return bandera;
    }

    /**
     * Método para redireccionar a index.xhtml si el usuario no esta logueado
     */
    public void Redireccionar() {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/index.xhtml");
        } catch (Exception error) {
            System.out.println("----------------------------Error---------------------------------" + error);
        }
    }

    /**
     *
     */
    public void Desactivado() {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Su sesión se cerrara", "Ud ha estado inactivo mas de 3 minutos"));
    }

    /**
     * Método encargado de cerrar la sesión del usuario en la base de datos y a
     * nivel de variables de sesión por tener un tiempo de inactividad de
     * 3minutos
     */
    public void cerrarPorInactividad() {
        WrResultado result;
        result = logOut(sesionLogueo);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession SesionAbierta = (HttpSession) session;
        SesionAbierta.invalidate();
        Redireccionar();
    }

    /**
     *
     * @param fecha
     * @return
     */
    public String formatoFecha(XMLGregorianCalendar fecha) {
        if (fecha != null) {
            Date fechaDate = fecha.toGregorianCalendar().getTime();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String fechaCadena = formateador.format(fechaDate);
            return fechaCadena;
        }
        return "";

    }

    /**
     * Metodo que permite colocar el estilo de una fila mediante un color
     *
     * @param actividadx
     * @return
     */
    public String estilo(Actividad actividadx) {
        if (actividadx != null) {
            Date fecha = actividadx.getFechaCierre().toGregorianCalendar().getTime();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String fechaSistema = formateador.format(new Date());
            String fechaCierre = formateador.format(fecha);
            boolean resultado = compararFechasConDate(fechaCierre, fechaSistema);
            if (resultado) {
                return " background-color: #FF8888;";
            }
        }
        return " background-color: white;";
    }

    /**
     *
     * @param fecha1
     * @param fechaActual
     * @return
     */
    public boolean compararFechasConDate(String fecha1, String fechaActual) {
        boolean resultado = false;
        try {
            /**
             * Obtenemos las fechas enviadas en el formato a comparar
             */
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fechaActual);
            if (fechaDate2.before(fechaDate1)) {
                resultado = true;
            }
        } catch (Exception e) {
            System.out.println("Se Produjo un Error!!!  " + e.getMessage());
        }
        return resultado;
    }

    private boolean logSesion(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private WrResultado logOut(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private WrActividad consultarActividad(com.pangea.capadeservicios.servicios.Actividad actividadActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.consultarActividad(actividadActual);
    }

    private WrActividad consultarActividades(com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Actividad actividadActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.consultarActividades(usuarioActual, actividadActual);
    }

    private WrResultado liberarActividades(com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.liberarActividades(usuarioActual);
    }

    private WrResultado liberarActividad(com.pangea.capadeservicios.servicios.Actividad actividadActual, com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.liberarActividad(actividadActual, usuarioActual);
    }
}