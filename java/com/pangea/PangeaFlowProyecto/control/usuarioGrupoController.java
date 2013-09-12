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
    
    /*
     * Objetos de la clase Usuario en donde se guardan los datos del usuario
     */
    private Usuario idusu, usuarioGrupoSeleccionado, datosusuarios;    
    
    /*
     * Objeto de la clase Grupo en donde se guarda el grupo seleccionado en el menu
     */
    private Grupo grupoSeleccionado;
    
    /*
     * Objetos de la clase List<Grupo> en donde se guarda la lista de grupos consultados
     * en el servicio
     */
    private List<Grupo> grupos;
    
    /*
     * Objetos de la clase UsuarioGrupoRol donde se guardan los datos de un
     * usuario_grupo_rol en especifico
     */
    private UsuarioGrupoRol grup;
    
    /*
     * Objetos de la clase List<UsuarioGrupoRol> donde se guardan la lista de
     * usuario_grupo_rol consultadas
     */
    private List<UsuarioGrupoRol> grupo, grupoUsuarios;
    
    /*
     * Variable de tipo int en donde se guarda el valor del indice de grupo
     * seleccionado
     */
    private int indice;
    
    /*
     * Variable de tipo String en donde se guarda el id del usuario
     */
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
        
        //Creación de las listas con la información de los grupos
        grupoSeleccionado = new Grupo();
        grupos=listarGrupos();
        grupoSeleccionado = grupos.get(0);
        grupo = new ArrayList<UsuarioGrupoRol>();
        grupo=listarUsuariosGrupo(grupoSeleccionado,false);
        
        //Carga la información de la tabla usuario_grupo_rol dependiendo del grupo
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
        
        //Carga la información de la tabla usuario_grupo_rol dependiendo del grupo
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
        
        //Creación de la variable de Sesión para el id de usuario y del id de grupo
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