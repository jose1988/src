/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.GestionDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import java.util.ArrayList;
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
@ManagedBean(name = "asignarActividadController")
@SessionScoped
public class asignarActividadController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeUsuarios.wsdl")
    private GestionDeUsuarios_Service service;
    private List<Usuario> usuarios, lista;
    private Usuario usu;
    Usuario usuarioLogueo;
    Sesion sesionLogueo;
    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext externalContext = context.getExternalContext();
    Object session = externalContext.getSession(true);
    HttpSession SesionAbierta = (HttpSession) session;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @PostConstruct
    public void init() {
        //codigo para guardar la lista de usuarios
        int j = 0;
        lista = listarUsuarios(false);
        usuarios = new ArrayList<Usuario>();
        if (lista.isEmpty()) {
            usuarios = null;
        }
        while (lista.size() > j) {
            usu = lista.get(j);
            usuarios.add(usu);
            j++;
        }
    }

    /**
     * Método para verificar si el usuario esta logueado
     */
    public boolean verificarLogueo() {
        boolean bandera = false;
        try {
            //codigo para guardar sesion y usuario logueado, sino existe redireccionamos a index.xhtml
            usuarioLogueo = (Usuario) (SesionAbierta.getAttribute("Usuario"));
            sesionLogueo = (Sesion) (SesionAbierta.getAttribute("Sesion"));
            if (usuarioLogueo == null || sesionLogueo == null) {
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

    private java.util.List<com.pangea.capadeservicios.servicios.Usuario> listarUsuarios(boolean borrado) {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        return port.listarUsuarios(borrado);
    }
}