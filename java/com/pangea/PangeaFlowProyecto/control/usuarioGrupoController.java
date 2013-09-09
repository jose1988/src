/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.GestionDeActividades_Service;
import com.pangea.capadeservicios.servicios.GestionDeGrupo_Service;
import com.pangea.capadeservicios.servicios.GestionDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.Grupo;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.UsuarioGrupoRol;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.TabChangeEvent;

/**
 * @author Pangea
 */
@ManagedBean(name = "usuarioGrupoController")
@SessionScoped
public class usuarioGrupoController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeUsuarios.wsdl")
    private GestionDeUsuarios_Service service;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeGrupo.wsdl")
    private GestionDeGrupo_Service service_2;
    private Usuario idusu;
    private Grupo grupoSeleccionado, gru;
    private Sesion sesion_actual;
    private List<Grupo> grupos;
    private UsuarioGrupoRol grup, usuarioGrupoSeleccionado;
    private List<UsuarioGrupoRol> grupo, grupoUsuarios, prueba;

    public List<UsuarioGrupoRol> getPrueba() {
        return prueba;
    }

    public void setPrueba(List<UsuarioGrupoRol> prueba) {
        this.prueba = prueba;
    }

    public Grupo getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public Grupo getGru() {
        return gru;
    }

    public void setGru(Grupo gru) {
        this.gru = gru;
    }

    public List<UsuarioGrupoRol> getGrupoUsuarios() {
        return grupoUsuarios;
    }

    public void setGrupoUsuarios(List<UsuarioGrupoRol> grupoUsuarios) {
        this.grupoUsuarios = grupoUsuarios;
    }

    public UsuarioGrupoRol getGrup() {
        return grup;
    }

    public void setGrup(UsuarioGrupoRol grup) {
        this.grup = grup;
    }

    public UsuarioGrupoRol getUsuarioGrupoSeleccionado() {
        return usuarioGrupoSeleccionado;
    }

    public void setUsuarioGrupoSeleccionado(UsuarioGrupoRol usuarioGrupoSeleccionado) {
        this.usuarioGrupoSeleccionado = usuarioGrupoSeleccionado;
    }

    public List<UsuarioGrupoRol> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<UsuarioGrupoRol> grupo) {
        this.grupo = grupo;
    }
    
     public Usuario getIdusu() {
        return idusu;
    }

    public void setIdusu(Usuario idusu) {
        this.idusu = idusu;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    @PostConstruct
    public void init() {
        idusu = new Usuario();
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion_actual = (Sesion) (sesion.getAttribute("Sesion"));
        idusu= (Usuario) (sesion.getAttribute("Usuario"));
        
        grupos=listarGrupos();
        grupoSeleccionado = grupos.get(0);
        System.out.println("Grupoooo  "+grupoSeleccionado.getId());
        
        prueba=listarUsuarioGrupoRol();
        //usuarioGrupoSeleccionado=prueba.get(0);
       //System.out.println("Usuarioooooo  "+usuarioGrupoSeleccionado.getIdUsuario().getId());
        
        grupo=this.listarGruposUsuarios(grupoSeleccionado);
        grupoUsuarios = new ArrayList<UsuarioGrupoRol>();
        
        int j=0;
        
        if(grupo.isEmpty()){
            grupoUsuarios=null;
        }
        while(grupo.size() > j){
            grup=grupo.get(j);
            if(grup.getIdGrupo().getId().compareTo(grupoSeleccionado.getId())!=0){
            }
            else{
                grupoUsuarios.add(grup);
            }
            j++;
            
        }
        

    }

    public void onTabChange(TabChangeEvent event) {
       
        Grupo gSeleccionado = (Grupo) event.getData();
        grupoSeleccionado = gSeleccionado;
        grupos=listarGrupos();
        
        grupo=listarGruposUsuarios(grupoSeleccionado);
        grupoUsuarios = new ArrayList<UsuarioGrupoRol>();
        
        
        if(grupo.isEmpty()){
            grupoUsuarios=null;
        }
        
        Grupo g = new Grupo();
        for (int i = 0; i < grupos.size(); i++) {
            if (grupos.get(i).getNombre().compareTo(gSeleccionado.getNombre()) == 0) {
                g = grupos.get(i);
            }

        }
        
        int j=0;
        
        while(grupo.size() > j){
            grup=grupo.get(j);
            if(grup.getIdGrupo().getId().compareTo(grupoSeleccionado.getId())!=0){
            }
            else{
                grupoUsuarios.add(grup);
            }
            System.out.println("Usuariooooossss "+grupoUsuarios.get(j).getIdUsuario());
            j++;
            
        }
        
    }

   

    private java.util.List<com.pangea.capadeservicios.servicios.Grupo> listarGrupos() {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarGrupos();
    }

    private java.util.List<com.pangea.capadeservicios.servicios.UsuarioGrupoRol> listarGruposUsuarios(com.pangea.capadeservicios.servicios.Grupo grupo) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarGruposUsuarios(grupo);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.UsuarioGrupoRol> listarUsuarioGrupoRol() {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarUsuarioGrupoRol();
    }

    
}