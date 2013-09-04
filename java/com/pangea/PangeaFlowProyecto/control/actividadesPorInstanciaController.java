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
    private List<Actividad> Actividades, lista;
    private Actividad act;
    Usuario usuarioLogueo;
    Sesion sesionLogueo;

    public List<Actividad> getActividades() {
        return Actividades;
    }

    public void setActividades(List<Actividad> Actividades) {
        this.Actividades = Actividades;
    }

    @PostConstruct
    public void init() {
        //codigo para guardar la lista de actividades por Instancia
        Instancia Instancia = new Instancia();
        Instancia.setId((long) 1);
        int j = 0;
        WrActividad Envoltorio, datosActividad;
        Envoltorio = consultarActividadesPorInstancia(Instancia);
        System.out.println("estado_________" + Envoltorio.getEstatus());
        Actividades = new ArrayList<Actividad>();
        if (Envoltorio.getActividads().isEmpty()) {
            Actividades = null;
        }
        while (Envoltorio.getActividads().size() > j) {
            System.out.println("Actividad" + j + "   __" + Envoltorio.getActividads().get(j).getId());
            datosActividad = consultarActividad(Envoltorio.getActividads().get(j));
            act = datosActividad.getActividads().get(0);
            Actividades.add(act);
            j++;
        }
    }

    /**
     * Método para verificar si el usuario esta logueado
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

    public void Desactivado() {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Su sesión se cerrara", "Ud ha estado inactivo mas de 3 minutos"));
    }

    public void Cerrar() {
        WrResultado result;
        result = logOut(sesionLogueo);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession SesionAbierta = (HttpSession) session;
        SesionAbierta.invalidate();
        //   System.out.println("----------------------------oyeeeeee---------------------------------");

        Redireccionar();
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