package com.pangea.PangeaFlowProyecto.control;

import com.pangea.PangeaFlowProyecto.control.util.JsfUtil;
import com.pangea.PangeaFlowProyecto.control.util.PaginationHelper;
import com.pangea.capadeservicios.servicios.ClasificacionUsuario;
import com.pangea.capadeservicios.servicios.ListarclasificacionUsuario.Fil;
import com.pangea.capadeservicios.servicios.ListarclasificacionUsuario.Fil.Entry;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.ws.WebServiceRef;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "clasificacion_usuarioController")
@SessionScoped
public class clasificacion_usuarioController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeClasificacion_usuario.wsdl")
    private com.pangea.capadeservicios.servicios.GestionDeClasificacionUsuario_Service service;

    private ClasificacionUsuario current;
    private PaginationHelper pagination;
    private int selectedItemIndex;

     /** LAZYYY **/
    private LazyDataModel<com.pangea.capadeservicios.servicios.ClasificacionUsuario> lazyModel;
    private int pagIndex = 0;
    private Map<String, String> fields = new HashMap<String, String>();
    private String sortF = null;
    private boolean sortB = false;
    private int paginacion = 10;
    /** FIN LAZY **/
    
    public clasificacion_usuarioController() {
    }

    public ClasificacionUsuario getSelected() {
        if (current == null) {
            current = new ClasificacionUsuario();
            selectedItemIndex = -1;
        }
        return current;
    }

     /** INICIO **/
    public void inicializarLazy() {
        lazyModel = new LazyDataModel<com.pangea.capadeservicios.servicios.ClasificacionUsuario>() {
    public List<com.pangea.capadeservicios.servicios.ClasificacionUsuario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
                paginacion = pageSize;
                int cantidad = contarClasificacionUsuario();
                if (cantidad > 0) {
                    pagIndex = first;
                    fields = filters;
                    sortF = sortField;
                    sortB = true;
                    String cadena = "";
                    lazyModel.setWrappedData(null);
                    cantidad = contarClasificacionUsuario();
                    lazyModel.setRowCount(cantidad);
                    List<Integer> lis= new ArrayList();
                    lis.add(first);
                    lis.add(first+pageSize);
                    Fil algo = new Fil();
                    for (Map.Entry e : filters.entrySet()) {
                        Entry otro=new Entry();
                        
                        otro.setKey(e.getKey().toString());
                        otro.setValue(e.getValue().toString());
                        algo.getEntry().add(otro);
                    }   
                  
                    return listarclasificacionUsuario(sortF, sortB, lis, algo, cadena);
                }
                return null;
                // return lazymv;
            }
        };

        lazyModel.setRowCount((int) contarClasificacionUsuario());
        //lazyModel.setRowCount(10);

        if (lazyModel.getPageSize() == 0) {
            lazyModel.setPageSize(1);
        }
        if (lazyModel.getRowCount() == 0) {
            lazyModel.setRowCount(10);
        }

    }

    public LazyDataModel<com.pangea.capadeservicios.servicios.ClasificacionUsuario> getLazyModel() {
        if (lazyModel == null) {
            inicializarLazy();
        }
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<com.pangea.capadeservicios.servicios.ClasificacionUsuario> lazyModel) {
        this.lazyModel = lazyModel;
    }
//fin lazy
  

    private void editarClasificacionUsuario(com.pangea.capadeservicios.servicios.ClasificacionUsuario registroClasificacionUsuario) {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionUsuario port = service.getGestionDeClasificacionUsuarioPort();
        port.editarClasificacionUsuario(registroClasificacionUsuario);
    }

    private void insertarClasificacionUsuario(com.pangea.capadeservicios.servicios.ClasificacionUsuario registroClasificacionUsuario) {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionUsuario port = service.getGestionDeClasificacionUsuarioPort();
        port.insertarClasificacionUsuario(registroClasificacionUsuario);
    }

    private int contarClasificacionUsuario() {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionUsuario port = service.getGestionDeClasificacionUsuarioPort();
        return port.contarClasificacionUsuario();
    }

    private java.util.List<com.pangea.capadeservicios.servicios.ClasificacionUsuario> listarclasificacionUsuario(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarclasificacionUsuario.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionUsuario port = service.getGestionDeClasificacionUsuarioPort();
        return port.listarclasificacionUsuario(sortF, sortB, range, fil, cad);
    }

    private void eliminarClasificacionUsuario(java.lang.Long idClasificacionUsuario) {
        com.pangea.capadeservicios.servicios.GestionDeClasificacionUsuario port = service.getGestionDeClasificacionUsuarioPort();
        port.eliminarClasificacionUsuario(idClasificacionUsuario);
    }

}
