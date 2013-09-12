/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.Actividad;
import com.pangea.capadeservicios.servicios.ClasificacionUsuario;
import com.pangea.capadeservicios.servicios.GestionDeActividades_Service;
import com.pangea.capadeservicios.servicios.Sesion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;

/**
 * @author Pangea
 */

@ManagedBean(name = "actividadController")

@SessionScoped

public class actividadController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeActividades.wsdl")
    private GestionDeActividades_Service service_1;
    
    private List<Actividad> actividades, actividad;
    private Actividad activi, act, id;
    private Usuario idusu;
    private List<String> estados;
    private ClasificacionUsuario idclasi, idcla;
    private Actividad idSesionActividad;  
    
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
        
        /**
         * Lista de Actividades con estado pendiente y que no han sido borradas
         */
        int j=0;  
        activi= new Actividad(); 
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
     * Método en el que se obtiene en una variable de sesión el id de 
     * la actividad a la que se desea asignar el usuario y redirecciona 
     * a asignaractividad.xhtml 
     */
    public void actividadAsignar(){
        
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
    
    /**
     * Método que cambia el formato de la fecha
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
    
    private java.util.List<com.pangea.capadeservicios.servicios.Actividad> listarActividades(java.lang.String estado, boolean borrado) {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.listarActividades(estado, borrado);
    }

    private java.util.List<java.lang.String> buscarEstados() {
        com.pangea.capadeservicios.servicios.GestionDeActividades port = service_1.getGestionDeActividadesPort();
        return port.buscarEstados();
    }
   
}