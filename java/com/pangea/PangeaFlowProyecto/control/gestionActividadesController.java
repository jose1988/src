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
import com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios_Service;
import com.pangea.capadeservicios.servicios.GestionDeGrupo_Service;
import com.pangea.capadeservicios.servicios.Grupo;
import com.pangea.capadeservicios.servicios.Mensajeria_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.WrActividad;
import com.pangea.capadeservicios.servicios.WrBandeja;
import com.pangea.capadeservicios.servicios.WrPost;
import com.pangea.capadeservicios.servicios.WrResultado;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * @author Pangea
 */
@ManagedBean(name = "gestionActividades")
@SessionScoped
public class gestionActividadesController implements Serializable {

    private static final long serialVersionUID = 1L;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeControlDeUsuarios.wsdl")
    private GestionDeControlDeUsuarios_Service service_3;
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
    private boolean boton = false;
    private Usuario usuarioLogueo;
    private Sesion sesionLogueo;
    private long UT;
    private String equivalencia;
    private String GrupoPanel;

    /**
     * enlista los estados, muestra por defecto las actividades el primer estado
     * que conigue las actividades y del primer grupo de consigue.
     */
    @PostConstruct
    public void init() {


        if (verificarLogueo()) {
            Redireccionar();
        } else {
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
                    icono = "i";
                } else if ("cerrada".equals(estados.get(i))) {
                    icono = "t";
                } else {
                    icono = "j";
                }
                TreeNode inbox = new DefaultTreeNode(icono, estados.get(i), estact);
                i++;
            }

            TreeNode inbox = new DefaultTreeNode("j", "Cola de Actividades", estact);
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
            GrupoPanel = grupoSeleccionado.getNombre();
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
    }

    public Grupo getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    public void botonActivado() {

        activi.setEstado(estadoSeleccionado.getData().toString());
        actividad = consultarActividades(idusu, activi);
        actividades = new ArrayList<Actividad>();
        if (actividad.getActividads().isEmpty()) {
            actividades = null;
        }
        Grupo g = new Grupo();

        for (int i = 0; i < grupos.size(); i++) {
            if (grupos.get(i).getNombre().compareTo(GrupoPanel) == 0) {
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

    /**
     *
     * Cambio de pestaña de los grupo, enlista las actividades por el grupo
     * seleccionado y refresca el datatable
     *
     * @param event
     */
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

    /**
     * enlista las actividades por el estado seleccionado y el grupo actual
     * refresca el datatable
     *
     * @param event
     */
    public void onNodeSelect(NodeSelectEvent event) {

        int j = 0;
        if ("Cola de Actividades".equals(event.getTreeNode().toString())) {

            ActividadesCola = consultarActividadesCola(idusu);
            actividades = new ArrayList<Actividad>();


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
                if (grupos.get(i).getNombre().compareTo(GrupoPanel) == 0) {
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

    /**
     * cambia el estado de la actividad pendiente a abierta, inicia la actividad
     * y refresca el datatable
     */
    public void cambiarEstado(CloseEvent evento) {

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

    /**
     * cambia el estado de la actividad de abierta a pendiente y refresca el
     * datatable
     */
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

    /**
     * envia la actividad para finalizarla
     */
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

    /**
     * se toma la condicion, la sesion y la actividad y se cierra la actividad a
     * dedo
     */
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

    /**
     *
     * @param actividadx
     * @return
     * @throws DatatypeConfigurationException
     */
    public String sombreado(Actividad actividadxx) throws DatatypeConfigurationException {
        if (actividadxx != null) {
            WrActividad actividadx = consultarActividad(actividadxx);
            if (actividadx.getActividads().get(0).getFechaApertura() != null) {
                XMLGregorianCalendar FC1;
                XMLGregorianCalendar FA1;
                XMLGregorianCalendar FAA;
                FC1 = actividadx.getActividads().get(0).getFechaApertura();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
                FA1 = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                long duracion = actividadx.getActividads().get(0).getDuracion().longValue();
                equivalencia = actividadx.getActividads().get(0).getIdEquivalenciasTiempo().getNombre();
                UT = actividadx.getActividads().get(0).getIdEquivalenciasTiempo().getMinutos();
                long f = FC1.toGregorianCalendar().getTimeInMillis();
                long f2 = FA1.toGregorianCalendar().getTimeInMillis();
                long resta = f2 - f;

                if ("Minuto".equals(equivalencia)) {
                    long minutos = resta / (60 * 1000);
                    if (minutos > duracion) {
                        return "background-color:  #FF8888";
                    } else if (actividadx.getActividads().get(0).getFechaAlerta() != null) {
                        FAA = actividadx.getActividads().get(0).getFechaAlerta();
                        long f3 = FAA.toGregorianCalendar().getTimeInMillis();
                        long resta2 = f2 - f3;
                        if (resta2 > resta) {
                            return "background-color: orange;";
                        }
                        return "background-color: white";
                    }
                }


                if ("Hora".equals(equivalencia)) {
                    long horas = resta / (60 * 60 * 1000);
                    if (horas > duracion) {
                        return "background-color:  #FF8888";
                    } else if (actividadx.getActividads().get(0).getFechaAlerta() != null) {
                        FAA = actividadx.getActividads().get(0).getFechaAlerta();
                        long f3 = FAA.toGregorianCalendar().getTimeInMillis();
                        long resta2 = f2 - f3;
                        if (resta2 > resta) {
                            return "background-color: orange;";
                        }
                        return "background-color: white";
                    }
                }
                if ("Dia".equals(equivalencia)) {
                    long dias = resta / (24 * 60 * 60 * 1000);
                    if (dias > duracion) {
                        return "background-color:  #FF8888";
                    } else if (actividadx.getActividads().get(0).getFechaAlerta() != null) {
                        FAA = actividadx.getActividads().get(0).getFechaAlerta();
                        long f3 = FAA.toGregorianCalendar().getTimeInMillis();
                        long resta2 = f2 - f3;
                        if (resta2 > resta) {
                            return "background-color: orange;";
                        }
                        return "background-color: white";
                    }
                }
                if ("Semana".equals(equivalencia)) {
                    long semanas = resta / (7 * 24 * 60 * 60 * 1000);
                    if (semanas > duracion) {
                        return "background-color:  #FF8888";
                    } else if (actividadx.getActividads().get(0).getFechaAlerta() != null) {
                        FAA = actividadx.getActividads().get(0).getFechaAlerta();
                        long f3 = FAA.toGregorianCalendar().getTimeInMillis();
                        long resta2 = f2 - f3;
                        if (resta2 > resta) {
                            return "background-color: orange;";
                        }
                        return "background-color: white";
                    }
                }




            }
            return " background-color: white;";
        }
        return " background-color: white;";
    }

    /**
     *
     * @param actividadx
     * @return
     * @throws DatatypeConfigurationException
     */
    public String sombreadoc(Actividad actividadxx) throws DatatypeConfigurationException {
        if (actividadxx != null) {
            WrActividad actividadx = consultarActividad(actividadxx);
            if (actividadx.getActividads().get(0).getFechaCierre() != null && actividadx.getActividads().get(0).getFechaApertura() != null) {
                XMLGregorianCalendar FC1;
                XMLGregorianCalendar FA1;
                FC1 = actividadx.getActividads().get(0).getFechaCierre();
                FA1 = actividadx.getActividads().get(0).getFechaApertura();
                long duracion = actividadx.getActividads().get(0).getDuracion().longValue();
                equivalencia = actividadx.getActividads().get(0).getIdEquivalenciasTiempo().getNombre();
                UT = actividadx.getActividads().get(0).getIdEquivalenciasTiempo().getMinutos();
                long f = FC1.toGregorianCalendar().getTimeInMillis();
                long f2 = FA1.toGregorianCalendar().getTimeInMillis();
                long resta = f - f2;


                if ("Minuto".equals(equivalencia)) {
                    long minutos = resta / (60 * 1000);
                    if (minutos > duracion) {
                        return "background-color:  #FF8888;";
                    }
                }
                if ("Hora".equals(equivalencia)) {
                    long horas = resta / (60 * 60 * 1000);
                    if (horas > duracion) {
                        return "background-color:  #FF8888;";
                    }
                }
                if ("Dia".equals(equivalencia)) {
                    long dias = resta / (24 * 60 * 60 * 1000);
                    if (dias > duracion) {
                        return "background-color:  #FF8888;";
                    }
                }
                if ("Semana".equals(equivalencia)) {
                    long semanas = resta / (7 * 24 * 60 * 60 * 1000);
                    if (semanas > duracion) {
                        return "background-color:  #FF8888;";
                    }
                }


            }
            return " background-color: white";
        }
        return " background-color: white;";
    }

    /**
     * asiga al usuario actual la actividad seleccionada en la cola de
     * actividades
     */
    public void asignar(CloseEvent evento) {

        resul = consumirCola(act, idusu);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(resul.getEstatus()));
        ActividadesCola = consultarActividadesCola(idusu);
        int j = 0;
        actividades = new ArrayList<Actividad>();


        while (ActividadesCola.getActividads().size() > j) {
            act = ActividadesCola.getActividads().get(j);
            actividades.add(act);
            j++;
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
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesionAbierta = (HttpSession) ec.getSession(true);
            usuarioLogueo = (Usuario) (sesionAbierta.getAttribute("Usuario"));
            sesionLogueo = (Sesion) (sesionAbierta.getAttribute("Sesion"));
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
         FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession SesionAbierta = (HttpSession) ec.getSession(true);
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
    public Actividad getAux() {
        return aux;
    }

    /**
     *
     * @param aux
     */
    public void setAux(Actividad aux) {
        this.aux = aux;
    }

    public boolean isBoton() {
        return boton;
    }

    public void setBoton(boolean boton) {
        this.boton = boton;
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

    public String getGrupoPanel() {
        return GrupoPanel;
    }

    public void setGrupoPanel(String GrupoPanel) {
        this.GrupoPanel = GrupoPanel;
    }

    /**
     *
     */
    public void send() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mail Sent!"));
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

    private boolean logSesion(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_3.getGestionDeControlDeUsuariosPort();
        return port.logSesion(sesionActual);
    }

    private WrResultado logOut(com.pangea.capadeservicios.servicios.Sesion sesionActual) {
        com.pangea.capadeservicios.servicios.GestionDeControlDeUsuarios port = service_3.getGestionDeControlDeUsuariosPort();
        return port.logOut(sesionActual);
    }

    private WrActividad consultarActividad(com.pangea.capadeservicios.servicios.Actividad actividadActual) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.consultarActividad(actividadActual);
    }
}
