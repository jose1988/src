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

    /*
     * Objeto de la clase usuario donde se guardara el objeto de la variable de sesión
     */
    Usuario usuarioId;

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
        Actividad estadoActividad = new Actividad();
        estadoActividad.setEstado("abierta");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession SesionAbierta = (HttpSession) session;
        usuarioId = (Usuario) (SesionAbierta.getAttribute("IdUsuario"));
         System.out.println("VALOR ID ES_________"+usuarioId.getId());
        envoltorioAbiertas = consultarActividades(usuarioId, estadoActividad);
        estadoActividad.setEstado("pendiente");
        envoltorioPendientes = consultarActividades(usuarioId, estadoActividad);
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

    /**
     *
     */
    public void liberarActividadUsuario() {
        actividadLibrar = new Actividad();
        actividadLibrar.setId(act.getId());
        
        WrResultado envoltorio = liberarActividad(actividadLibrar, usuarioId);
        System.out.println("LIBROOOOOOOOOOOOOOOOOOO_______" + act.getId());
    }

    /**
     *
     */
    public void liberarActividadesUsuario() {

        WrResultado envoltorio = liberarActividades(usuarioId);
        System.out.println("LIBeROOOOOOOOOOOOOOOOOOO_______");

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
            usuarioLogueo = (Usuario) (SesionAbierta.getAttribute("IdUsuario"));
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
     *
     * @return
     */
    public boolean verificarUsuarioId() {
        boolean bandera = false;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            usuarioId = (Usuario) (SesionAbierta.getAttribute("IdUsuario"));
          
            if (usuarioId == null) {
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
    public void Redirecionar() {
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
    public void redireccionarUsuarioGrupo() {
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/usuarioGrupo.xhtml");
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
        Redirecionar();
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