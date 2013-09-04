/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.GestionDeInstancias_Service;
import com.pangea.capadeservicios.servicios.Instancia;
import com.pangea.capadeservicios.servicios.Post;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.WrInstancia;
import com.pangea.capadeservicios.servicios.WrResultado;
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
 *
 * @author PANGEA
 */
@ManagedBean(name = "instanciaUsuarioController")
@SessionScoped
public class instanciaUsuarioController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeInstancias.wsdl")
    private GestionDeInstancias_Service service;
    private TreeNode mailboxes;
    private List<Post> mails;
    private Post mail;
    private TreeNode mailbox;
    private Usuario idusu;
    private TreeNode estact;

    private List<String> estados;
    private TreeNode estadoSeleccionado;
    private WrInstancia instancia, instac;
    private List<Instancia> instancias, instacs;
    private Instancia inst, insta, instActividad, ins;

    private WrResultado resul;
    private Sesion ses;
    
    private Long idInsta;
    
    public Instancia getIns() {
        return ins;
    }

    public void setIns(Instancia ins) {
        this.ins = ins;
    }
    
    public List<Instancia> getInstacs() {
        return instacs;
    }

    public void setInstacs(List<Instancia> instacs) {
        this.instacs = instacs;
    }
    
    public WrInstancia getInstac() {
        return instac;
    }

    public void setInstac(WrInstancia instac) {
        this.instac = instac;
    }

    public Instancia getInstActividad() {
        return instActividad;
    }

    public void setInstActividad(Instancia instActividad) {
        this.instActividad = instActividad;
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
        instac = consultarInstancia(insta);
        instancias = new ArrayList<Instancia>();
        instacs = new ArrayList<Instancia>();
        if (instancia.getInstancias().isEmpty()) {
            instancias = null;
            instacs = null;
        }
        while (instancia.getInstancias().size() > j) {
            inst = instancia.getInstancias().get(j);
            ins = instac.getInstancias().get(j);
            instancias.add(inst);
            instacs.add(ins);
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
        
        idInsta=inst.getId();
        System.out.println("Ide de la Actividad es:     "+idInsta);
        
        instActividad= new Instancia();
        instActividad.setId(idInsta);
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession httpSession = (HttpSession) session;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.setAttribute("IdInstancia", instActividad);
        
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/actividadesPorInstancia.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
        
        
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

    private WrInstancia consultarInstancias(com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Instancia instanciaActual) {
        com.pangea.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.consultarInstancias(usuarioActual, instanciaActual);
    }

    private java.util.List<java.lang.String> buscarestados() {
        com.pangea.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.buscarestados();
    }

    private WrInstancia consultarInstancia(com.pangea.capadeservicios.servicios.Instancia instanciaActual) {
        com.pangea.capadeservicios.servicios.GestionDeInstancias port = service.getGestionDeInstanciasPort();
        return port.consultarInstancia(instanciaActual);
    }
}
