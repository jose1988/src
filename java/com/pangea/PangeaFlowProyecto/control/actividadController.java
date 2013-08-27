/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;
import com.pangea.capadeservicios.servicios.Post;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.Bandeja;
import com.pangea.capadeservicios.servicios.Actividad;
import com.pangea.capadeservicios.servicios.Condicion;
import com.pangea.capadeservicios.servicios.GestionDeActividades_Service;
import com.pangea.capadeservicios.servicios.Mensajeria_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.WrActividad;
import com.pangea.capadeservicios.servicios.WrBandeja;
import com.pangea.capadeservicios.servicios.WrPost;
import com.pangea.capadeservicios.servicios.WrResultado;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author PANGEA
 */

@ManagedBean(name = "actividadController")
@SessionScoped

public class actividadController {
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/Mensajeria.wsdl")
    private Mensajeria_Service service;
   
    private TreeNode mailboxes;

    private List<Post> mails;
    
    private List<WrBandeja> ban;

    private Post mail;

    private TreeNode mailbox;
    
    private Usuario idusu;
    
    private WrBandeja bande;
    private WrPost bandej;
    
    private TreeNode estact;
    private Actividad activi;
    private List<String> estados;
    private TreeNode estadoSeleccionado;
    private WrActividad actividad;
    private List<Actividad> actividades;
    private Actividad act;
    private WrResultado resul;
    
    private Sesion ses;
    private Condicion cond;
    
    private Bandeja idban;
  
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

    @PostConstruct
    public void init() {
        estact = new DefaultTreeNode("root", null);
        idusu=new Usuario();
        idusu.setId("thunder");
        String icono;
      //bande=consultarBandejas(idusu);
        estados=buscarestados();
        int i=0;
        while (estados.size()>i){
            if("pendiente".equals(estados.get(i))){
                icono="s";
                TreeNode inbox = new DefaultTreeNode(icono,estados.get(i), estact);
                i++;
            }
        }
        int j=0;  
        activi= new Actividad(); 
        activi.setEstado(estados.get(j));
        actividad=consultarActividades(idusu, activi);
        actividades=new ArrayList<Actividad>();
        
        if(actividad.getActividads().isEmpty())
            actividades=null;
        while (actividad.getActividads().size()>j){
            act= actividad.getActividads().get(j);
            actividades.add(act);
            j++;
        }
    }
    
  
    public void onNodeSelect(NodeSelectEvent event) {  
        int j=0;
        activi= new Actividad(); 
        activi.setEstado(event.getTreeNode().toString());
        actividad=consultarActividades(idusu, activi);
        actividades=new ArrayList<Actividad>();
        if(actividad.getActividads().isEmpty())
        actividades=null;
        while (actividad.getActividads().size()>j){
            act= actividad.getActividads().get(j);
            actividades.add(act);
            j++;
         }
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
    
    public void send() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mail Sent!"));
    }

    private WrActividad consultarActividades(com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Actividad actividadActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.consultarActividades(usuarioActual, actividadActual);
    }

    private java.util.List<java.lang.String> buscarestados() {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.buscarestados();
    }

}
