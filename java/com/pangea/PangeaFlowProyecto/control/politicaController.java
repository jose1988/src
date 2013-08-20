package com.pangea.PangeaFlowProyecto.control;

import com.pangea.PangeaFlowProyecto.control.util.JsfUtil;
import com.pangea.capadeservicios.servicios.Politica;
import com.pangea.PangeaFlowProyecto.control.util.PaginationHelper;
import com.pangea.capadeservicios.servicios.GestionDepolitica_Service;
import com.pangea.capadeservicios.servicios.ListarPolitica.Fil;
import com.pangea.capadeservicios.servicios.ListarPolitica.Fil.Entry;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;



@ManagedBean(name = "politicaController")
@SessionScoped
public class politicaController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDepolitica.wsdl")
    private GestionDepolitica_Service service;

    private Politica current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
     /** LAZYYY **/
    private LazyDataModel<com.pangea.capadeservicios.servicios.Politica> lazyModel;
    private int pagIndex = 0;
    private Map<String, String> fields = new HashMap<String, String>();
    private String sortF = null;
    private boolean sortB = false;
    private int paginacion = 10;
    /** FIN LAZY **/


    public politicaController() {
    }
 
   
     /** INICIO **/
   /* public void inicializarLazy() {
        lazyModel = new LazyDataModel<com.pangea.capadeservicios.servicios.Politica>() {
    public List<com.pangea.capadeservicios.servicios.Politica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
                paginacion = pageSize;
                int cantidad = contarPolitica();
                if (cantidad > 0) {
                    pagIndex = first;
                    fields = filters;
                    sortF = sortField;
                    sortB = true;
                    String cadena = "";
                    lazyModel.setWrappedData(null);
                    cantidad = contarPolitica();
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
                  
                    return listarPolitica(sortF, sortB, lis, algo, cadena);

                }
                return null;
                // return lazymv;
            }
        };

        lazyModel.setRowCount((int) contarPolitica());
        //lazyModel.setRowCount(10);

        if (lazyModel.getPageSize() == 0) {
            lazyModel.setPageSize(1);
        }
        if (lazyModel.getRowCount() == 0) {
            lazyModel.setRowCount(10);
        }

    }

    public LazyDataModel<com.pangea.capadeservicios.servicios.Politica> getLazyModel() {
        if (lazyModel == null) {
            inicializarLazy();
        }
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<com.pangea.capadeservicios.servicios.Politica> lazyModel) {
        this.lazyModel = lazyModel;
    }
//fin lazy

    private int contarPolitica() {
        com.pangea.capadeservicios.servicios.GestionDepolitica port = service.getGestionDepoliticaPort();
        return port.contarPolitica();
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Politica> listarPolitica(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.capadeservicios.servicios.ListarPolitica.Fil fil, java.lang.String cad) {
        com.pangea.capadeservicios.servicios.GestionDepolitica port = service.getGestionDepoliticaPort();
        return port.listarPolitica(sortF, sortB, range, fil, cad);
    }
    
    
 @FacesConverter(forClass = Politica.class)
public static class politicaControllerConverter implements Converter {

public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
if (value == null || value.length() == 0) {
return null;
}
politicaController controller = (politicaController) facesContext.getApplication().getELResolver().
getValue(facesContext.getELContext(), null, "politicaController");
return controller.ejbFacade.find(getKey(value));
}

java.lang.Long getKey(String value) {
java.lang.Long key;
key = Long.valueOf(value);
return key;
}

String getStringKey(java.lang.Long value) {
StringBuffer sb = new StringBuffer();
sb.append(value);
return sb.toString();
}

public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
if (object == null) {
return null;
}
if (object instanceof Politica) {
Politica o = (Politica) object;
return getStringKey(o.getId());
} else {
throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Politica.class.getName());
}
} 
 }

  */
    
}
