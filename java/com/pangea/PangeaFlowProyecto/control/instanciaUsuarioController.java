/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Actividad;
import com.pangea.capadeservicios.servicios.GestionDeActividades_Service;
import com.pangea.capadeservicios.servicios.GestionDeInstancias_Service;
import com.pangea.capadeservicios.servicios.Instancia;
import com.pangea.capadeservicios.servicios.Post;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.WrActividad;
import com.pangea.capadeservicios.servicios.WrInstancia;
import com.pangea.capadeservicios.servicios.WrResultado;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author PANGEA
 */
@ManagedBean(name = "instanciaUsuarioController")
@SessionScoped
public class instanciaUsuarioController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeInstancias.wsdl")
    private GestionDeInstancias_Service service;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_1;
    private TreeNode mailboxes;
    private List<Post> mails;
    private Post mail;
    private TreeNode mailbox;
    private Usuario idusu;
    private TreeNode estact;

    private List<String> estados;
    private TreeNode estadoSeleccionado;
    private WrInstancia instancia;
    private List<Instancia> instancias;
    private Instancia inst, insta, instActividad;

    private WrResultado resul;
    private Sesion ses;
    
    private Long idInsta;
    
    private Actividad actividad, activi;
    
    private WrActividad listaActivi;
    
    private List<Actividad> actividades;
    
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

    public WrActividad getListaActivi() {
        return listaActivi;
    }

    public void setListaActivi(WrActividad listaActivi) {
        this.listaActivi = listaActivi;
    }

    public Instancia getInstActividad() {
        return instActividad;
    }

    public void setInstActividad(Instancia instActividad) {
        this.instActividad = instActividad;
    }
    
    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Long getIdInsta() {
        return idInsta;
    }

    public void setIdInsta(Long idInsta) {
        this.idInsta = idInsta;
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

    @PostConstruct
    public void init() {
        estact = new DefaultTreeNode("root", null);
        idusu = new Usuario();
        idusu.setId("thunder");
        String icono;
        estados = buscarestados();
        int i = 0;
        while (estados.size() > i) {
            if ("abierta".equals(estados.get(i))) {
                icono = "s";
                TreeNode inbox = new DefaultTreeNode(icono, estados.get(i), estact);
            } else if ("cerrada".equals(estados.get(i))) {
                icono = "i";
                TreeNode inbox = new DefaultTreeNode(icono, estados.get(i), estact);
            }
            i++;
        }
        estadoSeleccionado = estact.getChildren().get(0);
        estact.getChildren().get(0).setSelected(true);
        int j = 0;
        insta = new Instancia();
        insta.setEstado(estados.get(j));
        instancia = consultarInstancias(idusu, insta);
        instancias = new ArrayList<Instancia>();
        if (instancia.getInstancias().isEmpty()) {
            instancias = null;
        }
        while (instancia.getInstancias().size() > j) {
            inst = instancia.getInstancias().get(j);
            instancias.add(inst);
            j++;
        }
          
    }

    public void onNodeSelect(NodeSelectEvent event) {
        int j = 0;
        insta = new Instancia();
        insta.setEstado(event.getTreeNode().toString());
        instancia = consultarInstancias(idusu, insta);
        instancias = new ArrayList<Instancia>();
        if (instancia.getInstancias().isEmpty()) {
            instancias = null;
        }
        while (instancia.getInstancias().size() > j) {
            inst = instancia.getInstancias().get(j);
            instancias.add(inst);
            j++;
        }
          
    }
    
    public void listarActividades(){
        int k=0;
        idInsta=inst.getId();
        System.out.println("Ide de la Actividad es:     "+idInsta);
        
        instActividad= new Instancia();
        instActividad.setId(idInsta);
        
        listaActivi=consultarActividadesPorInstancia(instActividad);
        actividades=new ArrayList<Actividad>();
        
        if(listaActivi.getActividads().isEmpty()){
            actividades=null;
        }
        
        while(listaActivi.getActividads().size() > k){
            activi=listaActivi.getActividads().get(k);
            actividades.add(activi);
            k++;
        }
        
        System.out.println("Las Actividades son: "+actividades);
        
    }

    public Instancia getInsta() {
        return insta;
    }

    public void setInsta(Instancia insta) {
        this.insta = insta;
    }

    public WrInstancia getInstancia() {
        return instancia;
    }

    public void setInstancia(WrInstancia instancia) {
        this.instancia = instancia;
    }

    public List<Instancia> getInstancias() {
        return instancias;
    }

    public void setInstancias(List<Instancia> instancias) {
        this.instancias = instancias;
    }

    public Instancia getInst() {
        return inst;
    }

    public void setInst(Instancia inst) {
        this.inst = inst;
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

    private java.util.List<java.lang.String> buscarestados() {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.buscarestados();
    }

    private WrInstancia consultarInstancias(com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Instancia instanciaActual) {
        com.pangea.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.consultarInstancias(usuarioActual, instanciaActual);
    }

    private WrActividad consultarActividadesPorInstancia(com.pangea.capadeservicios.servicios.Instancia instanciaActual) {
        com.pangea.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.consultarActividadesPorInstancia(instanciaActual);
    }

}