/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Usuario;
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
    
    /*
     * Objeto de la clase usuario donde se guardara el objeto de la variable de sesión
     */
    Usuario usuarioLogueo;
    /*
     * Objeto de la clase sesión donde se guardara el objeto de la variable de sesión
     */
    Sesion sesionLogueo;
    
    private Usuario idusu, usuarioGrupoSeleccionado, datosusuarios;    
    private Grupo grupoSeleccionado;
    private Sesion sesion_actual;
    private List<Grupo> grupos;
    private UsuarioGrupoRol grup;
    private List<UsuarioGrupoRol> grupo, grupoUsuarios;
    private int indice;
    private String idUsuario;

    /**
     *
     * @return
     */
    public Usuario getDatosusuarios() {
        return datosusuarios;
    }

    /**
     *
     * @param datosusuarios
     */
    public void setDatosusuarios(Usuario datosusuarios) {
        this.datosusuarios = datosusuarios;
    }
    
    /**
     *
     * @return
     */
    public Usuario getUsuarioGrupoSeleccionado() {
        return usuarioGrupoSeleccionado;
    }

    /**
     *
     * @param usuarioGrupoSeleccionado
     */
    public void setUsuarioGrupoSeleccionado(Usuario usuarioGrupoSeleccionado) {
        this.usuarioGrupoSeleccionado = usuarioGrupoSeleccionado;
    }

    /**
     *
     * @return
     */
    public Grupo getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    /**
     *
     * @param grupoSeleccionado
     */
    public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    /**
     *
     * @return
     */
    public Sesion getSesion_actual() {
        return sesion_actual;
    }

    /**
     *
     * @param sesion_actual
     */
    public void setSesion_actual(Sesion sesion_actual) {
        this.sesion_actual = sesion_actual;
    }

    /**
     *
     * @return
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     *
     * @param idUsuario
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     *
     * @return
     */
    public int getIndice() {
        return indice;
    }

    /**
     *
     * @param indice
     */
    public void setIndice(int indice) {
        this.indice = indice;
    }

    /**
     *
     * @return
     */
    public List<UsuarioGrupoRol> getGrupoUsuarios() {
        return grupoUsuarios;
    }

    /**
     *
     * @param grupoUsuarios
     */
    public void setGrupoUsuarios(List<UsuarioGrupoRol> grupoUsuarios) {
        this.grupoUsuarios = grupoUsuarios;
    }

    /**
     *
     * @return
     */
    public UsuarioGrupoRol getGrup() {
        return grup;
    }

    /**
     *
     * @param grup
     */
    public void setGrup(UsuarioGrupoRol grup) {
        this.grup = grup;
    }

    /**
     *
     * @return
     */
    public List<UsuarioGrupoRol> getGrupo() {
        return grupo;
    }

    /**
     *
     * @param grupo
     */
    public void setGrupo(List<UsuarioGrupoRol> grupo) {
        this.grupo = grupo;
    }
    
     /**
     *
     * @return
     */
    public Usuario getIdusu() {
        return idusu;
    }

    /**
     *
     * @param idusu
     */
    public void setIdusu(Usuario idusu) {
        this.idusu = idusu;
    }

    /**
     *
     * @return
     */
    public List<Grupo> getGrupos() {
        return grupos;
    }

    /**
     *
     * @param grupos
     */
    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    /**
     * Método constructor que se incia al hacer la llamada a la pagina
     * usuarioGrupo.xhml
     */
    @PostConstruct
    public void init() {
        idusu = new Usuario();
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion_actual = (Sesion) (sesion.getAttribute("Sesion"));
        idusu= (Usuario) (sesion.getAttribute("Usuario"));
        
        grupoSeleccionado = new Grupo();
        grupos=listarGrupos();
        grupoSeleccionado = grupos.get(0);
        grupo = new ArrayList<UsuarioGrupoRol>();
        grupo=listarUsuariosGrupo(grupoSeleccionado,false);
        
        int j=0;
        grupoUsuarios=new ArrayList<UsuarioGrupoRol>();
        
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

    /**
     * Método que es llamado al momento de cambiar de grupo y lista 
     * los usuarios que posee dicho grupo
     * @param event
     */
    public void onTabChange(TabChangeEvent event) {
       
        Grupo gSeleccionado = (Grupo) event.getData();
        grupoSeleccionado = gSeleccionado;
        
        grupo = new ArrayList<UsuarioGrupoRol>();
        grupo=listarUsuariosGrupo(grupoSeleccionado,false);
        grupoUsuarios=new ArrayList<UsuarioGrupoRol>();
        
        if(grupo.isEmpty()){
            grupoUsuarios=null;
        }
        
        Grupo g = new Grupo();
        for (int i = 0; i < grupos.size(); i++) {
            if (grupos.get(i).getNombre().compareTo(gSeleccionado.getNombre()) == 0) {
                g = grupos.get(i);
                indice = i;
            }
        }
        
        int j=0;
        while(grupo.size() > j){
            grup=grupo.get(j);
            if(grup.getIdGrupo().getId().compareTo(grupoSeleccionado.getId())==0){
                grupoUsuarios.add(grup);
            }
            j++;
            
        }
    }
    
    /**
     * Método que es llamado para ver las actividades que posee un usuario 
     * en especifico guarda el id del usuario en una variable de sesión y 
     * redirecciona a la página actividadesPorUsuario.xhtml
     */
    public void verActividadesUsuario(){
        
        idUsuario=grup.getIdUsuario().getId();
        usuarioGrupoSeleccionado = new Usuario();
        usuarioGrupoSeleccionado.setId(idUsuario);
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object sessionInstancia = externalContext.getSession(true);
        HttpSession httpSession = (HttpSession) sessionInstancia;
        httpSession.invalidate();  
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.setAttribute("IdUsuario", usuarioGrupoSeleccionado);
        httpSession.setAttribute("IdGrupo", grupoSeleccionado);
        
        System.out.println("Usuariooooooo: "+usuarioGrupoSeleccionado.getId());
        
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/actividadesPorUsuario.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
    }
    
    /**
     * Método que es llamado para mostrar la información del usuario seleccionado
     */
    public void verDatosUsuario(){
        
        idUsuario=grup.getIdUsuario().getId();
        usuarioGrupoSeleccionado = new Usuario();
        usuarioGrupoSeleccionado.setId(idUsuario);
        datosusuarios=buscarUsuario(usuarioGrupoSeleccionado);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Grupo> listarGrupos() {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarGrupos();
    }

    private Usuario buscarUsuario(com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        return port.buscarUsuario(usuarioActual);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.UsuarioGrupoRol> listarUsuariosGrupo(com.pangea.capadeservicios.servicios.Grupo grupousuarios, boolean borrado) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarUsuariosGrupo(grupousuarios, borrado);
    }

}