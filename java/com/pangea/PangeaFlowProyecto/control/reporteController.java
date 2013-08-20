package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.Reporte;
import com.pangea.PangeaFlowProyecto.control.util.PaginationHelper;
import com.pangea.capadeservicios.servicios.GestionarReporte_Service;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.model.DataModel;
import javax.xml.ws.WebServiceRef;

@ManagedBean(name = "reporteController")
@SessionScoped
public class reporteController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionarReporte.wsdl")
    private GestionarReporte_Service service;

    private Reporte current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public reporteController() {
    }

    public Reporte getSelected() {
        if (current == null) {
            current = new Reporte();
            selectedItemIndex = -1;
        }
        return current;
    }

    private static void editarReporte(com.pangea.capadeservicios.servicios.Reporte registroReporte) {
        com.pangea.capadeservicios.servicios.GestionarReporte_Service service = new com.pangea.capadeservicios.servicios.GestionarReporte_Service();
        com.pangea.capadeservicios.servicios.GestionarReporte port = service.getGestionarReportePort();
        port.editarReporte(registroReporte);
    }

    private static void eliminarReporte(java.lang.Long idReporte) {
        com.pangea.capadeservicios.servicios.GestionarReporte_Service service = new com.pangea.capadeservicios.servicios.GestionarReporte_Service();
        com.pangea.capadeservicios.servicios.GestionarReporte port = service.getGestionarReportePort();
        port.eliminarReporte(idReporte);
    }

    private static void insertarReporte(com.pangea.capadeservicios.servicios.Reporte registroReporte) {
        com.pangea.capadeservicios.servicios.GestionarReporte_Service service = new com.pangea.capadeservicios.servicios.GestionarReporte_Service();
        com.pangea.capadeservicios.servicios.GestionarReporte port = service.getGestionarReportePort();
        port.insertarReporte(registroReporte);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Reporte> listarReporte(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarReporte.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionarReporte port = service.getGestionarReportePort();
        return port.listarReporte(sortF, sortB, range, fil, cad);
    }

    private int contarReporte() {
        com.pangea.capadeservicios.servicios.GestionarReporte port = service.getGestionarReportePort();
        return port.contarReporte();
    }

    

}
