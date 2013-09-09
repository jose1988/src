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
import com.pangea.capadeservicios.servicios.GestionDeGrupo_Service;
import com.pangea.capadeservicios.servicios.Grupo;
import com.pangea.capadeservicios.servicios.Mensajeria_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.WrActividad;
import com.pangea.capadeservicios.servicios.WrBandeja;
import com.pangea.capadeservicios.servicios.WrPost;
import com.pangea.capadeservicios.servicios.WrResultado;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * @author Pangea
 */
@ManagedBean(name = "loginController")
@SessionScoped
public class loginController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeGrupo.wsdl")
    private GestionDeGrupo_Service service_2;
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
    private Actividad aux;
    private int indice;
    private WrResultado resul;
    private Sesion ses;
    private Condicion cond;
    private Bandeja idban;
    private List<Grupo> grupos;
    private Grupo grupoSeleccionado;
    WrActividad ActividadesCola;
    private Sesion sesion_actual;
    private int rango;

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
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
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion_actual = (Sesion) (sesion.getAttribute("Sesion"));
        idusu = (Usuario) (sesion.getAttribute("Usuario"));
        String icono;
        // bande=consultarBandejas(idusu);
        estados = buscarEstados();
        int i = 0;
        while (estados.size() > i) {
            if ("abierta".equals(estados.get(i))) {
                icono = "s";
            } else if ("pendiente".equals(estados.get(i))) {
                icono = "t";
            } else if ("cerrada".equals(estados.get(i))) {
                icono = "i";
            } else {
                icono = "j";
            }
            TreeNode inbox = new DefaultTreeNode(icono, estados.get(i), estact);
            i++;
        }

        TreeNode inbox = new DefaultTreeNode("s", "Cola de Actividades", estact);
        grupos = this.gruposUsuario(idusu);
        estadoSeleccionado = estact.getChildren().get(0);
        indice = 0;
        estact.getChildren().get(0).setSelected(true);
        int j = 0;
        activi = new Actividad();
        activi.setEstado(estados.get(j));
        actividad = consultarActividades(idusu, activi);
        actividades = new ArrayList<Actividad>();
        grupoSeleccionado = grupos.get(0);
        if (actividad.getActividads().isEmpty()) {
            actividades = null;
        }
        while (actividad.getActividads().size() > j) {
            act = actividad.getActividads().get(j);
            if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId().compareTo(grupoSeleccionado.getId()) != 0) {
            } else {
                actividades.add(act);
            }

            j++;
        }


    }

    public void onTabChange(TabChangeEvent event) {
        activi = new Actividad();
        Grupo gSeleccionado = (Grupo) event.getData();
        grupoSeleccionado = gSeleccionado;
        activi.setEstado(estadoSeleccionado.getData().toString());
        actividad = consultarActividades(idusu, activi);
        actividades = new ArrayList<Actividad>();
        if (actividad.getActividads().isEmpty()) {
            actividades = null;
        }
        Grupo g = new Grupo();
        for (int i = 0; i < grupos.size(); i++) {
            if (grupos.get(i).getNombre().compareTo(gSeleccionado.getNombre()) == 0) {
                g = grupos.get(i);
                indice = i;
            }

        }

        int j = 0;

        while (actividad.getActividads().size() > j) {
            act = actividad.getActividads().get(j);
            if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId().compareTo(g.getId()) == 0) {
                actividades.add(act);
            }

            j++;
        }


    }

    public void onNodeSelect(NodeSelectEvent event) {

        int j = 0;
        if ("Cola de Actividades".equals(event.getTreeNode().toString())) {

            ActividadesCola = consultarActividadesCola(idusu);
            actividades = new ArrayList<Actividad>();
            if (actividad.getActividads().isEmpty()) {
                actividades = null;
            }

            while (ActividadesCola.getActividads().size() > j) {
                act = ActividadesCola.getActividads().get(j);
                actividades.add(act);
                j++;
            }


        } else {
            activi = new Actividad();
            activi.setEstado(event.getTreeNode().toString());
            actividad = consultarActividades(idusu, activi);
            actividades = new ArrayList<Actividad>();
            if (actividad.getActividads().isEmpty()) {
                actividades = null;
            }
            Grupo g = new Grupo();
            for (int i = 0; i < grupos.size(); i++) {
                if (grupos.get(i).getNombre().compareTo(grupoSeleccionado.getNombre()) == 0) {
                    g = grupos.get(i);
                    indice = i;
                }

            }
            while (actividad.getActividads().size() > j) {
                act = actividad.getActividads().get(j);
                if (act.getIdInstancia().getIdPeriodoGrupoProceso().getIdGrupo().getId().compareTo(g.getId()) != 0) {
                } else {
                    actividades.add(act);
                }

                j++;
            }
        }
    }

    public void cambiarEstado() {



        resul = iniciarActividad(act, sesion_actual);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(resul.getEstatus()));
        int j = 0;
        activi = new Actividad();
        activi.setEstado(estadoSeleccionado.toString());
        actividad = consultarActividades(idusu, activi);
        actividades = new ArrayList<Actividad>();
        if (actividad.getActividads().isEmpty()) {
            actividades = null;
        }
        while (actividad.getActividads().size() > j) {
            act = actividad.getActividads().get(j);

            actividades.add(act);
            j++;
        }

    }

    public void cambiarEstadoPendiente() {


        resul = pendienteActividad(act, sesion_actual);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(resul.getEstatus()));
        int j = 0;
        activi = new Actividad();
        activi.setEstado(estadoSeleccionado.toString());
        actividad = consultarActividades(idusu, activi);
        actividades = new ArrayList<Actividad>();
        if (actividad.getActividads().isEmpty()) {
            actividades = null;
        }
        while (actividad.getActividads().size() > j) {
            act = actividad.getActividads().get(j);

            actividades.add(act);
            j++;
        }

    }

    public void finalizaract() {

        System.out.println(act.getId());

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion.setAttribute("actividadactual", act);

        try {
            ec.redirect("/PangeaFlowProyecto/faces/finalizarActividad.xhtml");
        } catch (Exception e) {
            System.out.println("----------------------------Error---------------------------------" + e);
        }
    }

    public void cerraractividad() {

        ses = new Sesion();
        ses.setIdUsuario(idusu);
        cond = new Condicion();
        cond.setEstado("activa");
        resul = finalizarActividad(act, ses, cond);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(resul.getEstatus()));
        int j = 0;
        activi = new Actividad();
        activi.setEstado(estadoSeleccionado.toString());
        actividad = consultarActividades(idusu, activi);
        actividades = new ArrayList<Actividad>();
        if (actividad.getActividads().isEmpty()) {
            actividades = null;
        }
        while (actividad.getActividads().size() > j) {
            act = actividad.getActividads().get(j);
            actividades.add(act);
            j++;
        }

    }

    public String sombreado(Actividad actividadx) throws DatatypeConfigurationException {
        if (actividadx != null) {
            if (actividadx.getFechaCierre() != null) {
                XMLGregorianCalendar FC1;
                XMLGregorianCalendar FA1;
                FC1 = actividadx.getFechaCierre();
                Date fc = FC1.toGregorianCalendar().getTime();
                Date fecha = new Date();

                if (fc.before(fecha)) {
                    return "background-color:  #FF8888";
                } else if (actividadx.getFechaAlerta() != null) {
                    FA1 = actividadx.getFechaAlerta();
                    Date fa = FA1.toGregorianCalendar().getTime();
                    if (fa.before(fecha)) {
                        return "background-color: orange;";
                    }
                    return "background-color: white";
                }
            }
            return " background-color: white;";
        }
        return " background-color: white;";
    }

    public String sombreadoc(Actividad actividadx) throws DatatypeConfigurationException {
        if (actividadx != null) {
            if (actividadx.getFechaCierre() != null && actividadx.getFechaApertura() != null) {
                XMLGregorianCalendar FC1;
                XMLGregorianCalendar FA1;
                FC1 = actividadx.getFechaCierre();
                FA1 = actividadx.getFechaApertura();
                long duracion = actividadx.getDuracion().longValue();

                long f = FC1.toGregorianCalendar().getTimeInMillis();
                long f2 = FA1.toGregorianCalendar().getTimeInMillis();
                long resta = f - f2;
                long dias = resta / (24 * 60 * 60 * 1000);

                if (dias > duracion) {
                    return "background-color:  #FF8888;";
                }
            }
            return " background-color: white";
        }
        return " background-color: white;";
    }

    public void asignar() {

        resul = consumirCola(act, idusu);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(resul.getEstatus()));
        ActividadesCola = consultarActividadesCola(idusu);
        int j = 0;
        actividades = new ArrayList<Actividad>();
        if (actividad.getActividads().isEmpty()) {
            actividades = null;
        }

        while (ActividadesCola.getActividads().size() > j) {
            act = ActividadesCola.getActividads().get(j);
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

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
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

    public Actividad getAux() {
        return aux;
    }

    public void setAux(Actividad aux) {
        this.aux = aux;
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

    private static WrBandeja consultarBandejas(com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.consultarBandejas(usuarioActual);
    }

    private WrActividad consultarActividades(com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Actividad actividadActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.consultarActividades(usuarioActual, actividadActual);
    }

    private WrResultado finalizarActividad(com.pangea.capadeservicios.servicios.Actividad actividadActual, com.pangea.capadeservicios.servicios.Sesion sesionActual, com.pangea.capadeservicios.servicios.Condicion condicionActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.finalizarActividad(actividadActual, sesionActual, condicionActual);
    }

    private WrResultado iniciarActividad(com.pangea.capadeservicios.servicios.Actividad actividadActual, com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.iniciarActividad(actividadActual, sesionActual);
    }

    private WrActividad consultarActividadesCola(com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.consultarActividadesCola(usuarioActual);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Grupo> gruposUsuario(com.pangea.capadeservicios.servicios.Usuario user) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_2.getGestionDeGrupoPort();
        return port.gruposUsuario(user);
    }

    private java.util.List<java.lang.String> buscarEstados() {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.buscarEstados();
    }

    private WrResultado pendienteActividad(com.pangea.capadeservicios.servicios.Actividad actividadActual, com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.pendienteActividad(actividadActual, sesionActual);
    }

    private WrResultado consumirCola(com.pangea.capadeservicios.servicios.Actividad actividadActual, com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.consumirCola(actividadActual, usuarioActual);
    }
}