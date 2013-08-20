package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Organizacion;
import com.pangea.PangeaFlowProyecto.control.util.PaginationHelper;
import com.pangea.capadeservicios.servicios.GestionDeOrganizacion_Service;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.model.DataModel;
import javax.xml.ws.WebServiceRef;

@ManagedBean(name = "organizacionController")
@SessionScoped
public class organizacionController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeOrganizacion.wsdl")
    private GestionDeOrganizacion_Service service;

    private Organizacion current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public organizacionController() {
    }

    public Organizacion getSelected() {
        if (current == null) {
            current = new Organizacion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private static void editarOrganizacion(com.pangea.capadeservicios.servicios.Organizacion registroOrganizacion) {
        com.pangea.capadeservicios.servicios.GestionDeOrganizacion_Service service = new com.pangea.capadeservicios.servicios.GestionDeOrganizacion_Service();
        com.pangea.capadeservicios.servicios.GestionDeOrganizacion port = service.getGestionDeOrganizacionPort();
        port.editarOrganizacion(registroOrganizacion);
    }

    private static void eliminarOrganizacion(java.lang.Long idOrganizacion) {
        com.pangea.capadeservicios.servicios.GestionDeOrganizacion_Service service = new com.pangea.capadeservicios.servicios.GestionDeOrganizacion_Service();
        com.pangea.capadeservicios.servicios.GestionDeOrganizacion port = service.getGestionDeOrganizacionPort();
        port.eliminarOrganizacion(idOrganizacion);
    }

    private static void insertarOrganizacion(com.pangea.capadeservicios.servicios.Organizacion registroOrganizacion) {
        com.pangea.capadeservicios.servicios.GestionDeOrganizacion_Service service = new com.pangea.capadeservicios.servicios.GestionDeOrganizacion_Service();
        com.pangea.capadeservicios.servicios.GestionDeOrganizacion port = service.getGestionDeOrganizacionPort();
        port.insertarOrganizacion(registroOrganizacion);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Organizacion> listarOrganizacion(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarOrganizacion.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionDeOrganizacion port = service.getGestionDeOrganizacionPort();
        return port.listarOrganizacion(sortF, sortB, range, fil, cad);
    }

    private int contarOrganizacion() {
        com.pangea.capadeservicios.servicios.GestionDeOrganizacion port = service.getGestionDeOrganizacionPort();
        return port.contarOrganizacion();
    }

    
    
}
