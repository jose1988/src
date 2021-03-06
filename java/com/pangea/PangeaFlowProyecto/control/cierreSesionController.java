
package com.pangea.PangeaFlowProyecto.control;
import com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.WrResultado;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Pangeatech
 */
@ManagedBean(name = "cierreSesionController")
@SessionScoped
public class cierreSesionController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service;
    Usuario usuarioLogueo;
    Sesion sesionLogueo;

    /**
     *Método creado para el cierre de sesión del usuario en cuanto a las variables de sesión 
     * y en cuanto a la base de datos
     */
    public void Cerrar() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession sesionAbierta = (HttpSession) session;
        sesionLogueo = (Sesion) (sesionAbierta.getAttribute("Sesion"));
        WrResultado envoltorio = logOut(sesionLogueo);
        if (envoltorio.getEstatus().compareTo("OK") == 0) {
//            sesionAbierta.removeAttribute("Usuario");
//            sesionAbierta.removeAttribute("Sesion");
            sesionAbierta.invalidate();
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/index.xhtml");
            } catch (Exception error) {
                System.out.println("----------------------------Error---------------------------------" + error);
            }
        }
    }

    private WrResultado logOut(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    
}
