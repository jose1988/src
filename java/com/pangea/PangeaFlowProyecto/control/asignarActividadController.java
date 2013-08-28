/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.GestionDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.ws.WebServiceRef;

/**
 * @author Pangea
 */
@ManagedBean(name = "asignarActividadController")
@SessionScoped
public class asignarActividadController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeUsuarios.wsdl")
    private GestionDeUsuarios_Service service;

    private List<Usuario> usuarios,lista;
    private Usuario usu;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
      @PostConstruct
    public void init() {
        int j=0;  
        lista=listarUsuarios();
        usuarios=new ArrayList<Usuario>();
        if(lista.isEmpty())
            usuarios=null;
        while (lista.size()>j){
            usu= lista.get(j);
            usuarios.add(usu);
            j++;
        } 
       
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Usuario> listarUsuarios() {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        return port.listarUsuarios();
    }
}