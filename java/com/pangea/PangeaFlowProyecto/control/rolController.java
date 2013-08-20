package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Rol;
import com.pangea.PangeaFlowProyecto.control.util.JsfUtil;
import com.pangea.PangeaFlowProyecto.control.util.PaginationHelper;
import com.pangea.capadeservicios.servicios.GestionDeRol_Service;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.xml.ws.WebServiceRef;

@ManagedBean(name = "rolController")
@SessionScoped
public class rolController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeRol.wsdl")
    private GestionDeRol_Service service;

    private Rol current;
    private DataModel items = null;
    @EJB
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public rolController() {
    }

    public Rol getSelected() {
        if (current == null) {
            current = new Rol();
            selectedItemIndex = -1;
        }
        return current;
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Rol> listarRol(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarRol.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionDeRol port = service.getGestionDeRolPort();
        return port.listarRol(sortF, sortB, range, fil, cad);
    }

    private void insertarRol(com.pangea.capadeservicios.servicios.Rol registroRol) {
        com.pangea.capadeservicios.servicios.GestionDeRol port = service.getGestionDeRolPort();
        port.insertarRol(registroRol);
    }

    private void eliminarRol(long idrol) {
        com.pangea.capadeservicios.servicios.GestionDeRol port = service.getGestionDeRolPort();
        port.eliminarRol(idrol);
    }

    private void editarRol(com.pangea.capadeservicios.servicios.Rol edtiRol) {
        com.pangea.capadeservicios.servicios.GestionDeRol port = service.getGestionDeRolPort();
        port.editarRol(edtiRol);
    }

    private int contarRol() {
        com.pangea.capadeservicios.servicios.GestionDeRol port = service.getGestionDeRolPort();
        return port.contarRol();
    }

    
}
