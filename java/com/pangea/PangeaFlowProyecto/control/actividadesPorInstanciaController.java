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
import java.util.ArrayList;
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
@ManagedBean(name = "actividadesPorInstanciaController")
@SessionScoped
public class actividadesPorInstanciaController {

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
    private Actividad act;
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
        Instancia Instancia = new Instancia();
        Instancia.setId((long) 8);
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = context.getExternalContext();
//        Object session = externalContext.getSession(true);
//        HttpSession SesionAbierta = (HttpSession) session;
//        Instancia = (Instancia) (SesionAbierta.getAttribute("IdInstancia"));
        int j = 0;
        WrActividad Envoltorio, datosActividad;
        Envoltorio = consultarActividadesPorInstancia(Instancia);
        actividades = new ArrayList<Actividad>();
        if (Envoltorio.getActividads().isEmpty()) {
            actividades = null;
        }
        while (Envoltorio.getActividads().size() > j) {
            datosActividad = consultarActividad(Envoltorio.getActividads().get(j));
            if (datosActividad.getEstatus().compareTo("OK") == 0) {
                act = datosActividad.getActividads().get(0);
                actividades.add(act);
            }
            j++;
        }
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
     * Metodo que permite colocar el estilo de una fila mediante un color
     *
     * @param codigo parametro que indica la condicion para determinar si se
     * pinta la fila en rosa o blanco
     * @return
     */
    public String estilo(Actividad actividadx) {
      if(actividadx!=null){
        if(actividadx.getEstado().compareTo("abierta")==0)
          return " background-color: red;";
//        if (fecha.equals("2013-09-05")) {
//            return "background-color: mistyrose;";
//        }
      }
        return " background-color: white;";
    }

    private boolean logSesion(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private WrResultado logOut(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private com.pangea.capadeservicios.servicios.WrActividad consultarActividadesPorInstancia(com.pangea.capadeservicios.servicios.Instancia instanciaActual) {
        com.pangea.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.consultarActividadesPorInstancia(instanciaActual);
    }

    private WrActividad consultarActividad(com.pangea.capadeservicios.servicios.Actividad actividadActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_2.getGestionDeActividadesPort();
        return port.consultarActividad(actividadActual);
    }
}