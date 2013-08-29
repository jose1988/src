/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;


import com.pangea.capadeservicios.servicios.Actividad;
import com.pangea.capadeservicios.servicios.Condicion;
import com.pangea.capadeservicios.servicios.GestionDeActividades_Service;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
/**
 * @author Pangea
 */
@ManagedBean(name = "finalizarActController")
@SessionScoped
public class finalizarActividadController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service;

   
    private List<Condicion> cond;
    private Condicion condactual;
    private Actividad actividad_actual;
    
    


    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext externalContext = context.getExternalContext();
    Object session = externalContext.getSession(true);
    HttpSession actividadact = (HttpSession) session;
    
    @PostConstruct
    public void init() {
        

        actividad_actual = (Actividad) (actividadact.getAttribute("actividadactual"));
        cond = condicionesTransiciones(actividad_actual);
    
    }

    public List<Condicion> getCond() {
        return cond;
    }

    public void setCond(List<Condicion> cond) {
        this.cond = cond;
    }

    public Actividad getActividad_actual() {
        return actividad_actual;
    }

    public void setActividad_actual(Actividad actividad_actual) {
        this.actividad_actual = actividad_actual;
    }

    public Condicion getCondactual() {
        return condactual;
    }

    public void setCondactual(Condicion condactual) {
        this.condactual = condactual;
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Condicion> condicionesTransiciones(com.pangea.capadeservicios.servicios.Actividad actividad) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service.getGestionDeActividadesPort();
        return port.condicionesTransiciones(actividad);
    }

   
 

}