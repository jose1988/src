/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.GestionDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.WrResultado;
import com.pangea.capadeservicios.servicios.WrSesion;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
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
@ManagedBean(name = "logIntOutController")
@SessionScoped
public class logIntOutController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeUsuarios.wsdl")
    private GestionDeUsuarios_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service;
    /**
     *
     */
    /**
     * objeto con el cual se haran las validaciones en cuanto al inicio y el
     * cerrar sesión de usuario
     */
    private Usuario usuarioLogeo, usuarioSesion;
    /**
     * cadena donde se guardara el nombre de usuario
     */
    private String User;
    /**
     * cadena donde se guardara el nombre de usuario
     */
    private String Contrasena;
    /**
     * objeto de la clase de sesión
     */
    Sesion sesionUsuario;
    Sesion sesionLogueo;

    /**
     *
     * @return
     */
    public String getUser() {
        return User;
    }

    /**
     *
     * @param User
     */
    public void setUser(String User) {
        this.User = User;
    }

    /**
     *
     * @return
     */
    public String getContrasena() {
        return Contrasena;
    }

    /**
     *
     * @param Contrasena
     */
    public void setContrasena(String Contrasena) {
        this.Contrasena = Contrasena;
    }

    /**
     * Método para hacer el Inicio de Sesión Nota: De acuerdo a la dirección MAC
     * del equipo, si por alguna circunstancia no se puede obtener esa
     * dirección, se busca el nombre junto con la ip del equipo , y si tampoco
     * se puede obtener se guardara 127.0.0.1
     */
    public void logeoInt() {
        sesionUsuario = new Sesion();
        usuarioLogeo = new Usuario();
        usuarioLogeo.setId(User);
        usuarioLogeo.setClave(Contrasena);

        NetworkInterface Address;
        StringBuilder direccionMac = new StringBuilder();
        try {
            Address = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = Address.getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                direccionMac.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
        } catch (Exception e) {
            System.out.println(e);
            try {
                direccionMac.setLength(0);
                direccionMac.append(InetAddress.getLocalHost().toString());
            } catch (Exception ee) {
                direccionMac.setLength(0);
                direccionMac.append("127.0.0.1");
            }
        }
        sesionUsuario.setIdUsuario(usuarioLogeo);
        sesionUsuario.setIp(direccionMac.toString());
        sesionUsuario.setBorrado(false);
        WrSesion envoltorio;
        envoltorio = logIn(sesionUsuario);
        if (envoltorio.getEstatus().compareTo("OK") == 0) {
            usuarioSesion = buscarUsuario(usuarioLogeo);
            //Se Crea la sesión llamada usuarioSesion junto con el objeto usuario
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession httpSession = (HttpSession) session;
            httpSession.invalidate();
            httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("Usuario", usuarioSesion);
            httpSession.setAttribute("Sesion", envoltorio.getSesions().get(0));
            if (usuarioSesion.getIdClasificacionUsuario().getId() == 1) {
                try {
                    FacesContext contex = FacesContext.getCurrentInstance();
                    contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/actividad.xhtml");
                } catch (Exception e) {
                    System.out.println("----------------------------Error---------------------------------" + e);
                }
            } else {
                try {
                    FacesContext contex = FacesContext.getCurrentInstance();
                    contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/actividadusuario.xhtml");
                } catch (Exception e) {
                    System.out.println("----------------------------Error---------------------------------" + e);
                }
            }

        } else {
            mostrarMensaje(1, "Advertencia", envoltorio.getObservacion());
        }
    }

    /**
     * metodo para hacer el Cerrar Sesión
     */
//    public void logeoOut() {
//        /**
//         * objeto envoltorio que obtiene si el usuario puede acceser o
//         * no.........
//         */
//        WrResultado envoltorioResult;
//        usuarioLogeo = new Usuario();
//        usuarioLogeo.setId(User);
//        envoltorioResult = logOut(usuarioLogeo);
//        System.out.println("nombre de usuario " + User);
//        System.out.println("ESTADO     " + envoltorioResult.getEstatus());
//        System.out.println("Observación     " + envoltorioResult.getObservacion());
//        if (envoltorioResult.getEstatus().compareTo("OK") == 0) {
//            try {
//                FacesContext contex = FacesContext.getCurrentInstance();
//                contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/index.xhtml");
//            } catch (Exception e) {
//                System.out.println("----------------------------Error---------------------------------" + e);
//            }
//        } else {
//            System.out.println("FALLO-------------------------------");
//        }
//    }


    /**
     * Servicio consumido de la capa de servicios para inicio de sesión
     */
    private WrSesion logIn(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service.getGestionDeControlDeUsuariosPort();
        return port.logIn(sesionActual);
    }

    /**
     *
     * @param opcMensaje
     * @param cabeceraMensaje
     * @param cuerpoMensaje
     */
    public void mostrarMensaje(int opcMensaje, String cabeceraMensaje, String cuerpoMensaje) {
        //0 informacón 1 advertenciA 2 error 3 fatal
        FacesContext context = FacesContext.getCurrentInstance();
        switch (opcMensaje) {
            case 0: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, cabeceraMensaje, cuerpoMensaje));
                break;
            }
            case 1: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, cabeceraMensaje, cuerpoMensaje));
                break;
            }
            case 2: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, cabeceraMensaje, cuerpoMensaje));
                break;
            }
            case 3: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, cabeceraMensaje, cuerpoMensaje));
                break;
            }
        }
    }

    private Usuario buscarUsuario(com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service_1.getGestionDeUsuariosPort();
        return port.buscarUsuario(usuarioActual);
    }

    private WrResultado logOut(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }
}