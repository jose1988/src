/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.WrResultado;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author pc
 */
@ManagedBean(name = "cierreSesionController")
@SessionScoped
public class cierreSesionController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service;
    Usuario usuarioLogueo;
    Sesion sesionLogueo;
    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext externalContext = context.getExternalContext();
    Object session = externalContext.getSession(true);
    HttpSession sesionAbierta = (HttpSession) session;

    public void Cerrar() {
        usuarioLogueo = (Usuario) (sesionAbierta.getAttribute("Usuario"));
        WrResultado envoltorio = logOut(usuarioLogueo);
        if (envoltorio.getEstatus().compareTo("OK") == 0) {
            sesionAbierta.removeAttribute("Usuario");
            sesionAbierta.removeAttribute("Sesion");
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/index.xhtml");
            } catch (Exception error) {
                System.out.println("----------------------------Error---------------------------------" + error);
            }
        }
    }

    private WrResultado logOut(com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service.getGestionDeControlDeUsuariosPort();
        return port.logOut(usuarioActual);
    }
}
