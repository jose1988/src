package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.GestionarSkin_Service;
import com.pangea.capadeservicios.servicios.ListarSkin.Fil;
import com.pangea.capadeservicios.servicios.ListarSkin.Fil.Entry;
import com.pangea.capadeservicios.servicios.Skin;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.xml.ws.WebServiceRef;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


@ManagedBean(name = "skinController")
@SessionScoped
public class skinController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionarSkin.wsdl")
    private GestionarSkin_Service service;

    private Skin current;
    private DataModel items = null;
    @EJB

    private int selectedItemIndex;
    /** LAZYYY **/
    private LazyDataModel<com.pangea.capadeservicios.servicios.Skin> lazyModel;
    private int pagIndex = 0;
    private Map<String, String> fields = new HashMap<String, String>();
    private String sortF = null;
    private boolean sortB = false;
    private int paginacion = 10;
    /** FIN LAZY **/

    public skinController() {
    }

    public Skin getSelected() {
        if (current == null) {
            current = new Skin();
            selectedItemIndex = -1;
        }
        return current;
    }  

    
       /** INICIO **/
    public void inicializarLazy() {
        lazyModel = new LazyDataModel<com.pangea.capadeservicios.servicios.Skin>() {
    public List<com.pangea.capadeservicios.servicios.Skin> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
                paginacion = pageSize;
                int cantidad = contarSkin();
                if (cantidad > 0) {
                    pagIndex = first;
                    fields = filters;
                    sortF = sortField;
                    sortB = true;
                    String cadena = "";
                    lazyModel.setWrappedData(null);
                    cantidad = contarSkin();
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
                  
                    return listarSkin(sortF, sortB, lis, algo, cadena);

                }
                return null;
                // return lazymv;
            }
        };

        lazyModel.setRowCount((int) contarSkin());
        //lazyModel.setRowCount(10);

        if (lazyModel.getPageSize() == 0) {
            lazyModel.setPageSize(1);
        }
        if (lazyModel.getRowCount() == 0) {
            lazyModel.setRowCount(10);
        }

    }

    public LazyDataModel<com.pangea.capadeservicios.servicios.Skin> getLazyModel() {
        if (lazyModel == null) {
            inicializarLazy();
        }
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<com.pangea.capadeservicios.servicios.Skin> lazyModel) {
        this.lazyModel = lazyModel;
    }
//fin lazy

    private static void insertarSkin(com.pangea.capadeservicios.servicios.Skin registroskin) {
        com.pangea.capadeservicios.servicios.GestionarSkin_Service service = new com.pangea.capadeservicios.servicios.GestionarSkin_Service();
        com.pangea.capadeservicios.servicios.GestionarSkin port = service.getGestionarSkinPort();
        port.insertarSkin(registroskin);
    }

    private static void eliminarSkin(long idSkin) {
        com.pangea.capadeservicios.servicios.GestionarSkin_Service service = new com.pangea.capadeservicios.servicios.GestionarSkin_Service();
        com.pangea.capadeservicios.servicios.GestionarSkin port = service.getGestionarSkinPort();
        port.eliminarSkin(idSkin);
    }

    private static void editarSkin(com.pangea.capadeservicios.servicios.Skin registroskin) {
        com.pangea.capadeservicios.servicios.GestionarSkin_Service service = new com.pangea.capadeservicios.servicios.GestionarSkin_Service();
        com.pangea.capadeservicios.servicios.GestionarSkin port = service.getGestionarSkinPort();
        port.editarSkin(registroskin);
    }

    private int contarSkin() {
        com.pangea.capadeservicios.servicios.GestionarSkin port = service.getGestionarSkinPort();
        return port.contarSkin();
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Skin> listarSkin(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarSkin.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionarSkin port = service.getGestionarSkinPort();
        return port.listarSkin(sortF, sortB, range, fil, cad);
    }
    
}