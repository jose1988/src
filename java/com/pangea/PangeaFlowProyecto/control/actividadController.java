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
    
    
    
    /**
     *
     * @return
     */
    public ClasificacionUsuario getIdcla() {
        return idcla;
    }

    /**
     *
     * @param idcla
     */
    public void setIdcla(ClasificacionUsuario idcla) {
        this.idcla = idcla;
    }

    /**
     *
     * @return
     */
    public ClasificacionUsuario getIdclasi() {
        return idclasi;
    }

    /**
     *
     * @param idclasi
     */
    public void setIdclasi(ClasificacionUsuario idclasi) {
        this.idclasi = idclasi;
    }
    
    /**
     *
     * @return
     */
    public Actividad getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Actividad id) {
        this.id = id;
    }
  
    /**
     *
     * @return
     */
    public TreeNode getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    /**
     *
     * @param estadoSeleccionado
     */
    public void setEstadoSeleccionado(TreeNode estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    /**
     *
     * @param mailboxes
     */
    public void setMailboxes(TreeNode mailboxes) {
        this.mailboxes = mailboxes;
    }

    /**
     *
     * @param mails
     */
    public void setMails(List<Post> mails) {
        this.mails = mails;
    }

    /**
     *
     * @return
     */
    public TreeNode getSelectedNode() {  
        return estact;  
    }  
  
    /**
     *
     * @param selectedNode
     */
    public void setSelectedNode(TreeNode selectedNode) {  
        this.estact = selectedNode;  
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
    public Actividad getActivi() {
        return activi;
    }

    /**
     *
     * @param activi
     */
    public void setActivi(Actividad activi) {
        this.activi = activi;
    }

    /**
     *
     * @return
     */
    public List<Actividad> getActividades() {
        return actividades;
    }

    /**
     *
     * @param actividades
     */
    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    /**
     *
     * @return
     */
    public Actividad getAct() {
        return act;
    }

    /**
     *
     * @param act
     */
    public void setAct(Actividad act) {
        this.act = act;
    }
    
    /**
     *
     * @return
     */
    public TreeNode getMailboxes() {
        return mailboxes;
    }

    /**
     *
     * @return
     */
    public List<Post> getMails() {
        return mails;
    }

    /**
     *
     * @return
     */
    public Post getMail() {
        return mail;
    }
    
    /**
     *
     * @return
     */
    public TreeNode getEstact() {
        return estact;
    }

    /**
     *
     * @param estact
     */
    public void setEstact(TreeNode estact) {
        this.estact = estact;
    }

    /**
     *
     * @param mail
     */
    public void setMail(Post mail) {
        this.mail = mail;
    }

    /**
     *
     * @return
     */
    public TreeNode getMailbox() {
        return mailbox;
    }

    /**
     *
     * @param mailbox
     */
    public void setMailbox(TreeNode mailbox) {
        this.mailbox = mailbox;
    }
    
    /**
     *
     * @return
     */
    public List<String> getEstados() {
        return estados;
    }

    /**
     *
     * @param estados
     */
    public void setEstados(List<String> estados) {
        this.estados = estados;
    }
    
    /**
     *
     */
    @PostConstruct
    public void init() {
        
        estact = new DefaultTreeNode("root", null);
        estados=buscarEstados();
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
        actividad=listarActividades("pendiente",false);
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
     *
     * @param event
     */
    public void onNodeSelect(NodeSelectEvent event) {
        /**
         * Lista de Actividades con estado pendientey que no han sido borradas
         */
        int j=0;  
        activi= new Actividad(); 
        activi.setEstado(event.getTreeNode().toString());
        actividad=listarActividades("pendiente",false);
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
     * @return 
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
    

   

    private java.util.List<com.pangea.capadeservicios.servicios.Actividad> listarActividades(java.lang.String estado, boolean borrado) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.listarActividades(estado, borrado);
    }

    private java.util.List<java.lang.String> buscarEstados() {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.buscarEstados();
    }
   
}