/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Post;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.Bandeja;
import com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.GestionDeGrupo_Service;
import com.pangea.capadeservicios.servicios.Grupo;
import com.pangea.capadeservicios.servicios.Mensajeria_Service;
import com.pangea.capadeservicios.servicios.Rol;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.UsuarioGrupoRol;
import com.pangea.capadeservicios.servicios.WrBandeja;
import com.pangea.capadeservicios.servicios.WrPost;
import com.pangea.capadeservicios.servicios.WrResultado;
import com.pangea.capadeservicios.servicios.WrRol;
import com.pangea.capadeservicios.servicios.WrUsuarioGrupoRol;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * @author Pangea
 */
@ManagedBean(name = "mensajeriaController")
@SessionScoped
public class mensajeriaController implements Serializable {

    private static final long serialVersionUID = 1L;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeGrupo.wsdl")
    private GestionDeGrupo_Service service_2;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/Mensajeria.wsdl")
    private Mensajeria_Service service;
    private Usuario usuarioLogueo;
    private TreeNode mailboxes;
    private List<Post> mails;
    private List<WrBandeja> ban;
    private Post mail;
    private TreeNode mailbox;
    private Usuario idusu;
    private WrBandeja bande;
    private WrPost bandej;
    private Bandeja idban, crear, modificar, eliminar;
    //String para guardar usuario
    private String buscar_usuario;
    //String para guardar password
    private String buscar_password;
    private Sesion sesionLogueo;
    private TreeNode estadoSeleccionado;
    private WrPost bandejas;
    private Sesion sesion_actual;
    private String resultado;
    private WrResultado reliminar, creando, modificando, eliminando;
    /**
     * Cadena que guarda el nombre de la bandeja que se va a crear o que se va a
     * modificar
     */
    private String nombre;
    /**
     * Cadena que guarda los contactos a los cuales se les enviara el mensaje
     */
    private String Para;
    /**
     * Cadena que guarda el asunto del mensaje
     */
    private String Asunto;
    /**
     * Cadena que guarda el cuerpo del mensaje
     *
     */
    private String Cuerpo;
    /**
     * Árbol en el cual se guardara los contactos existentes en cuanto a grupos,
     * roles y usuarios
     *
     */
    private TreeNode root;
    /**
     * Arbol en el cual se guardara el evento del contacto(nodo) seleccionado
     */
    private TreeNode selectedNode;

    /**
     * Método constructor que se incia al hacer la llamada a la página
     * mensajeria.xhml
     */
    @PostConstruct
    public void init() {
        if (verificarLogueo()) {
            Redireccionar();
        } else {

            mailboxes = new DefaultTreeNode("root", null);
            idusu = new Usuario();
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            HttpSession sesion = (HttpSession) ec.getSession(true);
            sesion_actual = (Sesion) (sesion.getAttribute("Sesion"));
            idusu = (Usuario) (sesion.getAttribute("Usuario"));
            bande = consultarBandejas(idusu);
            int i = 0;
            String icono;

            TreeNode inbox = new DefaultTreeNode("k", "Redactar Mensaje", mailboxes);
            while (bande.getBandejas().size() > i) {
                if ("Enviados".equals(bande.getBandejas().get(i).getNombre())) {
                    icono = "s";
                } else if ("Papelera".equals(bande.getBandejas().get(i).getNombre())) {
                    icono = "t";
                } else if ("Recibidos".equals(bande.getBandejas().get(i).getNombre())) {
                    icono = "i";
                } else {
                    icono = "j";
                }
                inbox = new DefaultTreeNode(icono, bande.getBandejas().get(i).getNombre(), mailboxes);
                i++;
            }
            inbox = new DefaultTreeNode("k", "Crear Bandeja", mailboxes);

            int j = 0;
            idban = new Bandeja();
            mails = new ArrayList<Post>();
            estadoSeleccionado = mailboxes.getChildren().get(1);
            mailboxes.getChildren().get(1).setSelected(true);
            idban = new Bandeja();
            idban.setId(bande.getBandejas().get(0).getId());
            bandej = consultarMensajes(idusu, idban);

            while (bandej.getPosts().size() > j) {
                mail = bandej.getPosts().get(j);
                mails.add(mail);
                j++;
            }

            //Cargando árbol de contactos
            root = new DefaultTreeNode("Root", null);
            List<Grupo> Grupos = listarGrupos();
            TreeNode nodosGrupos[] = new TreeNode[Grupos.size()];
            for (int ii = 0; ii < Grupos.size(); ii++) {
                WrRol resultadoRol = listarRolesPorGrupo(Grupos.get(ii), false);
                if (resultadoRol.getEstatus().compareTo("OK") == 0) {
                    List< Rol> Roles = resultadoRol.getRols();
                    TreeNode nodosRoles[] = new TreeNode[Roles.size()];
                    if (Roles.size() > 0) {
                        nodosGrupos[ii] = new DefaultTreeNode(Grupos.get(ii).getNombre() + "@grupo", root);
                        for (int jj = 0; jj < Roles.size(); jj++) {
                            WrUsuarioGrupoRol resultadoLista = listarUsuariosPorGrupoYRol(Grupos.get(ii), Roles.get(jj));
                            if (resultadoLista.getEstatus().compareTo("OK") == 0) {
                                List< UsuarioGrupoRol> Usuarios = resultadoLista.getUsuarioGrupoRols();
                                if (Usuarios.size() > 0) {
                                    nodosRoles[jj] = new DefaultTreeNode(Roles.get(jj).getNombre() + "@rol", nodosGrupos[ii]);
                                    TreeNode nodosUsuarios[] = new TreeNode[Usuarios.size()];
                                    for (int k = 0; k < Usuarios.size(); k++) {
                                        nodosUsuarios[k] = new DefaultTreeNode(Usuarios.get(k).getIdUsuario().getId() + "@usuario", nodosRoles[jj]);
                                    }
                                }
                            } else {
                                System.out.println("No se pudo mostrar el rol debido a que " + resultadoLista.getObservacion());
                            }

                        }
                    }
                } else {
                    System.out.println("No se pudo mostrar el usuario debido a que " + resultadoRol.getObservacion());
                }

            }
        }
    }

    /**
     * Método que recarga la información del arbol dependiendo del estado que se
     * seleccione
     *
     * @param event un onNodeSelect que indica que opción se ha seleccionado
     */
    public void onNodeSelect(NodeSelectEvent event) {

        int j = 0;
        if ("Cola de Actividades".equals(event.getTreeNode().toString())) {
        } else {
            idban = new Bandeja();
            crear = new Bandeja();
            modificar = new Bandeja();
            eliminar = new Bandeja();
            int y = 0;
            while (bande.getBandejas().size() > y) {
                if (bande.getBandejas().get(y).getNombre().equals(event.getTreeNode().toString())) {
                    idban.setId(bande.getBandejas().get(y).getId());
                    crear = idban;
                    modificar = idban;
                    eliminar = idban;
                }
                y++;
            }
            bandejas = consultarMensajes(idusu, idban);
            mails = null;
            mails = new ArrayList<Post>();

            if (bandejas.getPosts().isEmpty()) {
                mails = null;
            } else {
                while (bandejas.getPosts().size() > j) {

                    mail = bandejas.getPosts().get(j);
                    mails.add(mail);
                    j++;
                }
            }
        }
    }

    /**
     * Método que elimina un mensaje
     */
    public void mensajeelimimar() {

        reliminar = eliminarMensaje(mail, idusu);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(reliminar.getEstatus()));
        int j = 0;
        bandejas = consultarMensajes(idusu, idban);
        mails = null;
        mails = new ArrayList<Post>();

        if (bandejas.getPosts().isEmpty()) {
            mails = null;
        } else {
            while (bandejas.getPosts().size() > j) {

                mail = bandejas.getPosts().get(j);
                mails.add(mail);
                j++;
            }

        }
    }

    /**
     *
     * @param correo
     * @return
     */
    public String sombreado(Post correo) {

        if (correo != null) {
            resultado = consultarLeido(correo, idusu);
            if (resultado != null) {
                if ("No Leido".equals(resultado)) {
                    return "background-color:  #81F7F3;";
                }
            }
            return "background-color:  #FFFFFF;";
        }
        return "background-color:  #FFFFFF;";
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
     * Método encargado de cerrar la sesión del usuario en la base de datos y a
     * nivel de variables de sesión por tener un tiempo de inactividad de
     * 4minutos
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
     * Método encargado de mostrar la fecha en el formato dd/mm/yyyy
     *
     * @param fecha
     * @return
     */
    public String formatoFecha(XMLGregorianCalendar fecha) {
        if (fecha != null) {
            Date fechaDate = fecha.toGregorianCalendar().getTime();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String fechaCadena = formateador.format(fechaDate);
            return fechaCadena;
        }
        return "";
    }

    /**
     * Método que muestra un mensaje cuando se ha enviado el mensaje
     */
    public void send() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mail Sent!"));
    }

    /**
     * Método en el cual se realiza el envio del mensaje
     */
    public void Envio() {
        Post mensaje = new Post();
        mensaje.setDe(idusu);
        mensaje.setPara(Para);
        mensaje.setAsunto(Asunto);
        mensaje.setTexto(Cuerpo);
        WrResultado envoltorio = enviarPost(mensaje);
        if (envoltorio.getEstatus().compareTo("OK") == 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje enviado", "El mensaje fue enviado"));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", envoltorio.getObservacion()));
        }
    }

    /**
     * Método llamado al seleccionar un contacto para agregarlo en el campo de
     * texto "Para"
     *
     * @param event
     */
    public void NodoSeleccionado(NodeSelectEvent event) {
        if (Para.compareTo("") == 0) {
            Para = event.getTreeNode().toString() + ";";
        } else {
            Para = Para + event.getTreeNode().toString() + ";";
        }
    }

    /**
     * Método que crea una nueva bandeja
     */
    public void crearBand() {

        crear.setIdUsuario(idusu);
        crear.setNombre(nombre);

        System.out.println("Nombre de la bandeja creada: " + crear.getNombre());
        System.out.println("Nombre de la bandeja creada: " + crear.getIdUsuario().getId());

        creando = crearBandeja(crear);

        //Cargo de nuevo el árbol de bandejas
        mailboxes = new DefaultTreeNode("root", null);
        bande = consultarBandejas(idusu);
        TreeNode inbox = new DefaultTreeNode("k", "Redactar Mensaje", mailboxes);
        int i = 0;
        String icono;
        while (bande.getBandejas().size() > i) {
            if ("Enviados".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "s";
            } else if ("Papelera".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "t";
            } else if ("Recibidos".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "i";
            } else {
                icono = "j";
            }
            inbox = new DefaultTreeNode(icono, bande.getBandejas().get(i).getNombre(), mailboxes);
            i++;
        }
        inbox = new DefaultTreeNode("k", "Crear Bandeja", mailboxes);
        int j = 0;
        idban = new Bandeja();
        mails = new ArrayList<Post>();
        estadoSeleccionado = mailboxes.getChildren().get(1);
        mailboxes.getChildren().get(1).setSelected(true);
        idban = new Bandeja();
        idban.setId(bande.getBandejas().get(0).getId());
        bandej = consultarMensajes(idusu, idban);

        while (bandej.getPosts().size() > j) {
            mail = bandej.getPosts().get(j);
            mails.add(mail);
            j++;
        }

        Redireccionando();
    }

    /**
     * Método que modifica el nombre de la bandeja seleccionada
     */
    public void modificarBand() {

        modificar.setNombre(nombre);

        System.out.println("Id de bandeja: " + modificar.getId());
        System.out.println("Nombre nuevo de bandeja: " + modificar.getNombre());

        modificando = modificarBandeja(modificar, idusu);

        //Cargo de nuevo el árbol de bandejas
        mailboxes = new DefaultTreeNode("root", null);
        bande = consultarBandejas(idusu);
        TreeNode inbox = new DefaultTreeNode("k", "Redactar Mensaje", mailboxes);
        int i = 0;
        String icono;
        while (bande.getBandejas().size() > i) {
            if ("Enviados".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "s";
            } else if ("Papelera".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "t";
            } else if ("Recibidos".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "i";
            } else {
                icono = "j";
            }
            inbox = new DefaultTreeNode(icono, bande.getBandejas().get(i).getNombre(), mailboxes);
            i++;
        }
        inbox = new DefaultTreeNode("k", "Crear Bandeja", mailboxes);
        int j = 0;
        idban = new Bandeja();
        mails = new ArrayList<Post>();
        estadoSeleccionado = mailboxes.getChildren().get(1);
        mailboxes.getChildren().get(1).setSelected(true);
        idban = new Bandeja();
        idban.setId(bande.getBandejas().get(0).getId());
        bandej = consultarMensajes(idusu, idban);

        while (bandej.getPosts().size() > j) {
            mail = bandej.getPosts().get(j);
            mails.add(mail);
            j++;
        }

        Redireccionando();
    }

    /**
     * Método que elimina la bandeja seleccionada
     */
    public void eliminarBand() {

        System.out.println("Id de bandeja: " + eliminar.getId());

        eliminando = eliminarBandeja(eliminar);

        //Cargo de nuevo el árbol de bandejas
        mailboxes = new DefaultTreeNode("root", null);
        bande = consultarBandejas(idusu);
        TreeNode inbox = new DefaultTreeNode("k", "Redactar Mensaje", mailboxes);
        int i = 0;
        String icono;
        while (bande.getBandejas().size() > i) {
            if ("Enviados".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "s";
            } else if ("Papelera".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "t";
            } else if ("Recibidos".equals(bande.getBandejas().get(i).getNombre())) {
                icono = "i";
            } else {
                icono = "j";
            }
            inbox = new DefaultTreeNode(icono, bande.getBandejas().get(i).getNombre(), mailboxes);
            i++;
        }
        inbox = new DefaultTreeNode("k", "Crear Bandeja", mailboxes);
        int j = 0;
        idban = new Bandeja();
        mails = new ArrayList<Post>();
        estadoSeleccionado = mailboxes.getChildren().get(1);
        mailboxes.getChildren().get(1).setSelected(true);
        idban = new Bandeja();
        idban.setId(bande.getBandejas().get(0).getId());
        bandej = consultarMensajes(idusu, idban);

        while (bandej.getPosts().size() > j) {
            mail = bandej.getPosts().get(j);
            mails.add(mail);
            j++;
        }

        Redireccionando();

    }

    /**
     * Método que redirecciona a la página mensajeria.xhtml
     */
    public void Redireccionando() {
        this.nombre = "";
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect("/PangeaFlowProyecto/faces/mensajeria.xhtml");
        } catch (Exception error) {
            System.out.println("----------------------------Error---------------------------------" + error);
        }
    }

    /**
     *
     * @return
     */
    public String getPara() {
        return Para;
    }

    /**
     *
     * @param Para
     */
    public void setPara(String Para) {
        this.Para = Para;
    }

    /**
     *
     * @return
     */
    public String getAsunto() {
        return Asunto;
    }

    /**
     *
     * @param Asunto
     */
    public void setAsunto(String Asunto) {
        this.Asunto = Asunto;
    }

    /**
     *
     * @return
     */
    public String getCuerpo() {
        return Cuerpo;
    }

    /**
     *
     * @param Cuerpo
     */
    public void setCuerpo(String Cuerpo) {
        this.Cuerpo = Cuerpo;
    }

    /**
     *
     * @return
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     *
     * @return
     */
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     *
     * @param selectedNode
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    /**
     *
     * @return
     */
    public String getBuscar_usuario() {
        return buscar_usuario;
    }

    /**
     *
     * @param buscar_usuario
     */
    public void setBuscar_usuario(String buscar_usuario) {
        this.buscar_usuario = buscar_usuario;
    }

    /**
     *
     * @return
     */
    public String getBuscar_password() {
        return buscar_password;
    }

    /**
     *
     * @param buscar_password
     */
    public void setBuscar_password(String buscar_password) {
        this.buscar_password = buscar_password;
    }
    Usuario usuario_logeo = new Usuario();

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
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

//Servicios usados de la capa de servicios
    private WrResultado enviarPost(com.pangea.capadeservicios.servicios.Post mensajeActual) {
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.enviarPost(mensajeActual);
    }

    private static WrPost consultarMensajes(com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Bandeja bandejaActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.consultarMensajes(usuarioActual, bandejaActual);
    }

    private static WrPost consultarMensaje(com.pangea.capadeservicios.servicios.Post mensajeActual, com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.consultarMensaje(mensajeActual, usuarioActual);
    }

    private static WrResultado moverMensaje(com.pangea.capadeservicios.servicios.PostEnBandeja postEnBandejaActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.moverMensaje(postEnBandejaActual);
    }

    private static WrResultado enviarMensaje(com.pangea.capadeservicios.servicios.WrDestinatario destinatarios, com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Mensaje mensajeActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.enviarMensaje(destinatarios, usuarioActual, mensajeActual);
    }

    private WrBandeja consultarBandejas(com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.consultarBandejas(usuarioActual);
    }

    private boolean logSesion(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private WrResultado logOut(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_1.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private String consultarLeido(com.pangea.capadeservicios.servicios.Post mensajeActual, com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.consultarLeido(mensajeActual, usuarioActual);
    }

    private WrResultado eliminarMensaje(com.pangea.capadeservicios.servicios.Post mensajeActual, com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.eliminarMensaje(mensajeActual, usuarioActual);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Grupo> listarGrupos() {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarGrupos();
    }

    private static WrResultado crearBandeja(com.pangea.capadeservicios.servicios.Bandeja bandejaActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.crearBandeja(bandejaActual);
    }

    private static WrResultado modificarBandeja(com.pangea.capadeservicios.servicios.Bandeja carpetaActual, com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.modificarBandeja(carpetaActual, usuarioActual);
    }

    private static WrResultado eliminarBandeja(com.pangea.capadeservicios.servicios.Bandeja bandejaActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.eliminarBandeja(bandejaActual);
    }

    private WrRol listarRolesPorGrupo(com.pangea.capadeservicios.servicios.Grupo grupousuarios, boolean borrado) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarRolesPorGrupo(grupousuarios, borrado);
    }

    private WrUsuarioGrupoRol listarUsuariosPorGrupoYRol(com.pangea.capadeservicios.servicios.Grupo grupousuarios, com.pangea.capadeservicios.servicios.Rol roles) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.listarUsuariosPorGrupoYRol(grupousuarios, roles);
    }
}