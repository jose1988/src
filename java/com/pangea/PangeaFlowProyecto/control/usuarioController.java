package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.PangeaFlowProyecto.control.util.JsfUtil;
import com.pangea.PangeaFlowProyecto.control.util.PaginationHelper;
import com.pangea.capadeservicios.servicios.GestionDeUsuarios_Service;

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

@ManagedBean(name = "usuarioController")
@SessionScoped
public class usuarioController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeUsuarios.wsdl")
    private GestionDeUsuarios_Service service;

    private Usuario current;
    private DataModel items = null;
    @EJB
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public usuarioController() {
    }

    public Usuario getSelected() {
        if (current == null) {
            current = new Usuario();
            selectedItemIndex = -1;
        }
        return current;
    }

    private int contarUsuario() {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        return port.contarUsuario();
    }

    private void editarUsuario(com.pangea.capadeservicios.servicios.Usuario editoUsuario) {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        port.editarUsuario(editoUsuario);
    }

    private void eliminarUsuario(java.lang.String eliminoUsuario) {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        port.eliminarUsuario(eliminoUsuario);
    }

    private void insertarUsuario(com.pangea.capadeservicios.servicios.Usuario registroUsuario) {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        port.insertarUsuario(registroUsuario);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Usuario> listarUsuario(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarUsuario.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionDeUsuarios port = service.getGestionDeUsuariosPort();
        return port.listarUsuario(sortF, sortB, range, fil, cad);
    }

}
