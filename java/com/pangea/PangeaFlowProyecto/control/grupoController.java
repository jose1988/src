package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Grupo;
import com.pangea.PangeaFlowProyecto.control.util.PaginationHelper;
import com.pangea.capadeservicios.servicios.GestionDeGrupo_Service;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.model.DataModel;
import javax.xml.ws.WebServiceRef;

@ManagedBean(name = "grupoController")
@SessionScoped
public class grupoController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeGrupo.wsdl")
    private GestionDeGrupo_Service service;

    private Grupo current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public grupoController() {
    }

    public Grupo getSelected() {
        if (current == null) {
            current = new Grupo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private static void editarGrupo(com.pangea.capadeservicios.servicios.Grupo registroGrupo) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo_Service service = new com.pangea.capadeservicios.servicios.GestionDeGrupo_Service();
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service.getGestionDeGrupoPort();
        port.editarGrupo(registroGrupo);
    }

   

    private static void insertarGrupo(com.pangea.capadeservicios.servicios.Grupo registroGrupo) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo_Service service = new com.pangea.capadeservicios.servicios.GestionDeGrupo_Service();
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service.getGestionDeGrupoPort();
        port.insertarGrupo(registroGrupo);
    }


    private static void eliminarGrupo(java.lang.Long idGrupo) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo_Service service = new com.pangea.capadeservicios.servicios.GestionDeGrupo_Service();
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service.getGestionDeGrupoPort();
        port.eliminarGrupo(idGrupo);
    }

    private int contarGrupo() {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service.getGestionDeGrupoPort();
        return port.contarGrupo();
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Grupo> listarGrupo(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarGrupo.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service.getGestionDeGrupoPort();
        return port.listarGrupo(sortF, sortB, range, fil, cad);
    }

}
