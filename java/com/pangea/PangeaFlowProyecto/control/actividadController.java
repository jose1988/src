/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;
import com.pangea.capadeservicios.servicios.Post;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.Actividad;
import com.pangea.capadeservicios.servicios.ClasificacionUsuario;
import com.pangea.capadeservicios.servicios.GestionDeActividades_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * @author Pangea
 */

@ManagedBean(name = "actividadController")

@SessionScoped

public class actividadController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_1;
   
    private TreeNode mailboxes;
    private List<Post> mails;
    private Post mail;
    private TreeNode mailbox;    
    private TreeNode estact;
    private TreeNode estadoSeleccionado;
    
    private List<Actividad> actividades, actividad;
    private Actividad activi, act, id;
    
    private Usuario idusu;
    private List<String> estados;
    private ClasificacionUsuario idclasi, idcla;
    
    Actividad idSesionActividad;
    
    Usuario usuarioLogueo;
    Sesion sesionLogueo;
    
    
    
    public ClasificacionUsuario getIdcla() {
        return idcla;
    }

    public void setIdcla(ClasificacionUsuario idcla) {
        this.idcla = idcla;
    }

    public ClasificacionUsuario getIdclasi() {
        return idclasi;
    }

    public void setIdclasi(ClasificacionUsuario idclasi) {
        this.idclasi = idclasi;
    }
    
    public Actividad getId() {
        return id;
    }

    public void setId(Actividad id) {
        this.id = id;
    }
  
    public TreeNode getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(TreeNode estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public void setMailboxes(TreeNode mailboxes) {
        this.mailboxes = mailboxes;
    }

    public void setMails(List<Post> mails) {
        this.mails = mails;
    }

    public TreeNode getSelectedNode() {  
        return estact;  
    }  
  
    public void setSelectedNode(TreeNode selectedNode) {  
        this.estact = selectedNode;  
    }  
   
    public Usuario getIdusu() {
        return idusu;
    }

    public void setIdusu(Usuario idusu) {
        this.idusu = idusu;
    }

    public Actividad getActivi() {
        return activi;
    }

    public void setActivi(Actividad activi) {
        this.activi = activi;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public Actividad getAct() {
        return act;
    }

    public void setAct(Actividad act) {
        this.act = act;
    }
    
    public TreeNode getMailboxes() {
        return mailboxes;
    }

    public List<Post> getMails() {
        return mails;
    }

    public Post getMail() {
        return mail;
    }
    
    public TreeNode getEstact() {
        return estact;
    }

    public void setEstact(TreeNode estact) {
        this.estact = estact;
    }

    public void setMail(Post mail) {
        this.mail = mail;
    }

    public TreeNode getMailbox() {
        return mailbox;
    }

    public void setMailbox(TreeNode mailbox) {
        this.mailbox = mailbox;
    }
    
    public List<String> getEstados() {
        return estados;
    }

    public void setEstados(List<String> estados) {
        this.estados = estados;
    }
    
    @PostConstruct
    public void init() {
        
        estact = new DefaultTreeNode("root", null);
        estados=buscarestados();
        int i=0;
        while (estados.size()>i){
            if("pendiente".equals(estados.get(i))){
                TreeNode inbox = new DefaultTreeNode(estados.get(i), estact);
            }
            i++;
        }
        
        /**
         * Lista de Actividades con estado pendientey que no han sido borradas
         */
        estadoSeleccionado = estact.getChildren().get(0);
        int j=0;  
        activi= new Actividad(); 
        activi.setEstado(estados.get(j));
        actividad=listarActividades("pendiente", false);
        actividades=new ArrayList<Actividad>();
        if(actividad.isEmpty())
            actividades=null;
        while (actividad.size()>j){
            act= actividad.get(j);
            actividades.add(act);
            j++;
        } 
    }
  
     public void onNodeSelect(NodeSelectEvent event) {
        /**
         * Lista de Actividades con estado pendientey que no han sido borradas
         */
        int j=0;  
        activi= new Actividad(); 
        activi.setEstado(event.getTreeNode().toString());
        actividad=listarActividades("pendiente", false);
        actividades=new ArrayList<Actividad>();
        if(actividad.isEmpty())
            actividades=null;
        while (actividad.size()>j){
            act= actividad.get(j);
            actividades.add(act);
            j++;
        } 
    }
     
     /**
     * Método para verificar si el usuario esta logueado
     */
    public boolean verificarLogueo() {
        boolean bandera = false;
        try {
            /**
             * Codigo para guardar sesion y usuario logueado, sino existe redireccionamos a index.xhtml
             */
            
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
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
    
    /**
     * Método en el que se obtiene en una variable de sesión el id de 
     * la actividad a la que se desea asignar el usuario y redirecciona 
     * a asignaractividad.xhtml 
     */
    public void actividadAsignar(){
        
        System.out.println("IDEEEEEEEEE    "+act.getId());
        
        idSesionActividad= new Actividad();
        idSesionActividad.setId(act.getId());
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session1 = externalContext.getSession(true);
        HttpSession httpSession = (HttpSession) session1;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.setAttribute("IdActividad", idSesionActividad);
        
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/asignarActividad.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }    
        
        
      
    }
    

    private java.util.List<java.lang.String> buscarestados() {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.buscarestados();
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Actividad> listarActividades(java.lang.String estado, boolean borrado) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.listarActividades(estado, borrado);
    }

}