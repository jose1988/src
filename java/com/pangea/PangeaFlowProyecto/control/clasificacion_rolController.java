package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.ClasificacionRol;
import com.pangea.PangeaFlowProyecto.control.util.JsfUtil;
import com.pangea.PangeaFlowProyecto.control.util.PaginationHelper;
import com.pangea.capadeservicios.servicios.GestionDeClasificacionRol_Service;
import com.pangea.capadeservicios.servicios.ListarGrupo.Fil;
import com.pangea.capadeservicios.servicios.ListarGrupo.Fil.Entry;
//import com.pangea.capadeservicios.servicios.ContarClasificacion;
//import com.pangea.capadeservicios.servicios.Listacargo.Fil.Entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "clasificacion_rolController")
@SessionScoped
public class clasificacion_rolController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeClasificacion_rol.wsdl")
    private GestionDeClasificacionRol_Service service;
    
    private ClasificacionRol current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    /**
     * LAZYYY *
     */
    private LazyDataModel<com.pangea.capadeservicios.servicios.ClasificacionRol> lazyModel;
    private int pagIndex = 0;
    private Map<String, String> fields = new HashMap<String, String>();
    private String sortF = null;
    private boolean sortB = false;
    private int paginacion = 10;

    /**
     * FIN LAZY *
     */
    public clasificacion_rolController() {
    }
    
    public ClasificacionRol getSelected() {
        if (current == null) {
            current = new ClasificacionRol();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     * INICIO *
     */
    public void inicializarLazy() {
        lazyModel = new LazyDataModel<com.pangea.capadeservicios.servicios.ClasificacionRol>() {
            public List<com.pangea.capadeservicios.servicios.ClasificacionRol> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<Integer> lista = new ArrayList<Integer>();
                paginacion = pageSize;
                //  int cant=getFacade().count();
                int cantidad = contarClasificacion();
                // current.se
                if (cantidad > 0) {
                    pagIndex = first;
                    fields = filters;
                    sortF = sortField;
                    sortB = true;
                    String cadena = "";
                    lazyModel.setWrappedData(null);
                    cantidad = contarClasificacion();
                    lazyModel.setRowCount(cantidad);
                    lista.add(first);
                    lista.add(first + pageSize);
                   Fil algo = new Fil();
                    for (Map.Entry e : filters.entrySet()) {
                        Entry otro = new Entry();
                        otro.setKey(e.getKey().toString());
                        otro.setValue(e.getValue().toString());
                        algo.getEntry().add(otro);
                    }
                  // return listarclasificacion_rol(sortF, sortB, lista, algo, cadena);
                    //     return getFacade().findRange(sortF, sortB, new int[]{first, first + pageSize}, filters, cadena);
                }
                return null;
                // return lazymv;
            }
        };
        if (lazyModel.getPageSize() == 0) {
            lazyModel.setPageSize(1);
        }
        if (lazyModel.getRowCount() == 0) {
            lazyModel.setRowCount(10);
        }
        
    }
    
    public LazyDataModel<com.pangea.capadeservicios.servicios.ClasificacionRol> getLazyModel() {
        if (lazyModel == null) {
            inicializarLazy();
        }
        return lazyModel;
    }
    
    public void setLazyModel(LazyDataModel<com.pangea.capadeservicios.servicios.ClasificacionRol> lazyModel) {
        this.lazyModel = lazyModel;
    }
//fin lazy

    public void onEdit(RowEditEvent event) {
        ClasificacionRol c = (ClasificacionRol) event.getObject();
        editarClaRol(c);
        System.out.println("Edito");
    }
    
    public void onCancel(RowEditEvent event) {
        System.out.println("Cancelo");
        
    }

    private void insertarClasificacionRol(com.pangea.capadeservicios.servicios.ClasificacionRol registroClaRol) {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionRol port = service.getGestionDeClasificacionRolPort();
        port.insertarClasificacionRol(registroClaRol);
    }

    private void editarClaRol(com.pangea.capadeservicios.servicios.ClasificacionRol editaClaRol) {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionRol port = service.getGestionDeClasificacionRolPort();
        port.editarClaRol(editaClaRol);
    }

    private int contarClasificacion() {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionRol port = service.getGestionDeClasificacionRolPort();
        return port.contarClasificacion();
    }

    private void eliminarClaRol(long idClaRol) {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionRol port = service.getGestionDeClasificacionRolPort();
        port.eliminarClaRol(idClaRol);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.ClasificacionRol> listarclasificacionRol(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarclasificacionRol.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionRol port = service.getGestionDeClasificacionRolPort();
        return port.listarclasificacionRol(sortF, sortB, range, fil, cad);
    }

    
}
