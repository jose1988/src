/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
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
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeInstancias.wsdl")
    private GestionDeInstancias_Service service;
    
    /*
     * Objeto de la clase usuario donde se guardara el objeto de la variable de sesión
     */
    Usuario usuarioLogueo;
    /*
     * Objeto de la clase sesión donde se guardara el objeto de la variable de sesión
     */
    Sesion sesionLogueo;
    
    private TreeNode mailboxes;
    private List<Post> mails;
    private Post mail;
    private TreeNode mailbox;
    private Usuario idusu, idusuario;

    public Usuario getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Usuario idusuario) {
        this.idusuario = idusuario;
    }
    private TreeNode estact;

    private List<String> estados;
    private TreeNode estadoSeleccionado;
    private WrInstancia instancia, instac;
    private List<Instancia> instancias;
    private Instancia inst, insta, instActividad;

    private WrResultado resul;
    private Sesion ses;
    
    private Long idInsta;
    private String usuario;


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    /**
     *
     * @return
     */
    public WrInstancia getInstac() {
        return instac;
    }

    /**
     *
     * @param instac
     */
    public void setInstac(WrInstancia instac) {
        this.instac = instac;
    }

    /**
     *
     * @return
     */
    public Instancia getInstActividad() {
        return instActividad;
    }

    /**
     *
     * @param instActividad
     */
    public void setInstActividad(Instancia instActividad) {
        this.instActividad = instActividad;
    }

    /**
     *
     * @return
     */
    public Long getIdInsta() {
        return idInsta;
    }

    /**
     *
     * @param idInsta
     */
    public void setIdInsta(Long idInsta) {
        this.idInsta = idInsta;
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
    public Instancia getInsta() {
        return insta;
    }

    /**
     *
     * @param insta
     */
    public void setInsta(Instancia insta) {
        this.insta = insta;
    }

    /**
     *
     * @return
     */
    public WrInstancia getInstancia() {
        return instancia;
    }

    /**
     *
     * @param instancia
     */
    public void setInstancia(WrInstancia instancia) {
        this.instancia = instancia;
    }

    /**
     *
     * @return
     */
    public List<Instancia> getInstancias() {
        return instancias;
    }

    /**
     *
     * @param instancias
     */
    public void setInstancias(List<Instancia> instancias) {
        this.instancias = instancias;
    }

    /**
     *
     * @return
     */
    public Instancia getInst() {
        return inst;
    }

    /**
     *
     * @param inst
     */
    public void setInst(Instancia inst) {
        this.inst = inst;
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
     * Metodo constructor que se incia al hacer la llamada a la pagina
     * instanciaUsuario.xhml
     */
    @PostConstruct
    public void init() {
        
        verificarLogueo();
        
        System.out.println("Usuarioooo:     "+usuario);
        
        estact = new DefaultTreeNode("root", null);
        idusu = new Usuario();
        
        idusu.setId(usuario);
        
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
            instac = consultarInstancia(instancia.getInstancias().get(j));
            inst = instac.getInstancias().get(0);
            instancias.add(inst);
            j++;
        }
          
    }

    /**
     * Método que recarga la información del arbol dependiendo del estado que se seleccione
     * @param event
     */
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
            instac = consultarInstancia(instancia.getInstancias().get(j));
            inst = instac.getInstancias().get(0);
            instancias.add(inst);
            j++;
        }
          
    }
    
    /**
     * Método que recibe el id de la instancia a la que se desea 
     * consultar las actividades asociadas a ella y crea una
     * variable de sesión con dicho id y redirecciona a actividadesPorInstancia.xhtml
     */
    public void listarActividades(){
        
        idInsta=inst.getId();
        System.out.println("Ide de la Actividad es:     "+idInsta);
        
        instActividad= new Instancia();
        instActividad.setId(idInsta);
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object sessionInstancia = externalContext.getSession(true);
        HttpSession httpSession = (HttpSession) sessionInstancia;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.setAttribute("IdInstancia", instActividad);
        
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/actividadesPorInstancia.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
        
        
    }

    /**
     * Método para verificar si el usuario esta logueado
     *
     * @return un booleano si es true es por que si estaba logueado
     */
    public boolean verificarLogueo() {
        boolean bandera = false, sesionBd = false;
        try {
            //codigo para guardar sesion y usuario logueado, sino existe redireccionamos a index.xhtml
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(true);
            HttpSession SesionAbierta = (HttpSession) session;
            usuarioLogueo = (Usuario) (SesionAbierta.getAttribute("Usuario"));
            sesionLogueo = (Sesion) (SesionAbierta.getAttribute("Sesion"));
            
            usuario=usuarioLogueo.getId();
            
            sesionBd = logSesion(sesionLogueo);
            if (usuarioLogueo == null || sesionLogueo == null || !sesionBd) {
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
     * Método encargado de cerrar la sesión del usuario en la base de datos y a
     * nivel de variables de sesión por tener un tiempo de inactividad de
     * 3minutos
     */
    public void cerrarPorInactividad() {
        WrResultado result;
        result = logOut(sesionLogueo);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(true);
        HttpSession SesionAbierta = (HttpSession) session;
        SesionAbierta.invalidate();
        Redireccionar();
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

    private WrResultado logOut(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private boolean logSesion(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }
    
}
