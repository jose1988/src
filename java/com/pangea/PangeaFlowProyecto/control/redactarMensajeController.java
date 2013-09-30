package com.pangea.PangeaFlowProyecto.control;

import com.pangea.capadeservicios.servicios.GestionDeGrupo_Service;
import com.pangea.capadeservicios.servicios.Grupo;
import com.pangea.capadeservicios.servicios.Mensajeria_Service;
import com.pangea.capadeservicios.servicios.Post;
import com.pangea.capadeservicios.servicios.Rol;
import com.pangea.capadeservicios.servicios.Sesion;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.UsuarioGrupoRol;
import com.pangea.capadeservicios.servicios.WrResultado;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pangeatech
 */
@ManagedBean(name = "redactarMensajeController")
@SessionScoped
public class redactarMensajeController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/GestionDeGrupo.wsdl")
    private GestionDeGrupo_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/Mensajeria.wsdl")
    private Mensajeria_Service service;
    private Usuario usuarioLogueo;
    private String Para;
    private String Asunto;
    private String Cuerpo;
    private TreeNode root;
    private TreeNode selectedNode;

    public String getPara() {
        return Para;
    }

    public void setPara(String Para) {
        this.Para = Para;
    }

    public String getAsunto() {
        return Asunto;
    }

    public void setAsunto(String Asunto) {
        this.Asunto = Asunto;
    }

    public String getCuerpo() {
        return Cuerpo;
    }

    public void setCuerpo(String Cuerpo) {
        this.Cuerpo = Cuerpo;
    }

    public void Envio() {
        usuarioLogueo = new Usuario();
        usuarioLogueo.setId("thunder");
        Post mensaje = new Post();
        mensaje.setDe(usuarioLogueo);
        mensaje.setPara(Para);
        mensaje.setAsunto(Asunto);
        mensaje.setTexto(Cuerpo);
        WrResultado envoltorio = enviarPost(mensaje);
        if (envoltorio.getEstatus().compareTo("OK") == 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje enviado", "El mensaje fue enviado"));
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", envoltorio.getObservacion()));
        }
    }

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        List<Grupo> Grupos = listarGrupos();
        TreeNode nodosGrupos[] = new TreeNode[Grupos.size()];
        for (int i = 0; i < Grupos.size(); i++) {
            nodosGrupos[i] = new DefaultTreeNode(Grupos.get(i).getNombre() + "@grupo", root);
            List< Rol> Roles = listarRolesPorGrupo(Grupos.get(i), false);
            TreeNode nodosRoles[] = new TreeNode[Roles.size()];
            for (int j = 0; j < Roles.size(); j++) {
                nodosRoles[j] = new DefaultTreeNode(Roles.get(j).getNombre() + "@rol", nodosGrupos[i]);
                List< UsuarioGrupoRol> Usuarios = listarUsuariosPorGrupoYRol(Grupos.get(i), Roles.get(j));
                TreeNode nodosUsuarios[] = new TreeNode[Usuarios.size()];
                for (int k = 0; k < Usuarios.size(); k++) {
                    nodosUsuarios[k] = new DefaultTreeNode(Usuarios.get(k).getIdUsuario().getId() + "@usuario", nodosRoles[j]);
                }
            }
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        if (Para.compareTo("") == 0) {
            Para = event.getTreeNode().toString() + ";";
        } else {
            Para = Para + event.getTreeNode().toString() + ";";
        }
    }

    private WrResultado enviarPost(com.pangea.capadeservicios.servicios.Post mensajeActual) {
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.enviarPost(mensajeActual);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Grupo> listarGrupos() {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarGrupos();
    }

    private java.util.List<com.pangea.capadeservicios.servicios.Rol> listarRolesPorGrupo(com.pangea.capadeservicios.servicios.Grupo grupousuarios, boolean borrado) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarRolesPorGrupo(grupousuarios, borrado);
    }

    private java.util.List<com.pangea.capadeservicios.servicios.UsuarioGrupoRol> listarUsuariosPorGrupoYRol(com.pangea.capadeservicios.servicios.Grupo grupousuarios, com.pangea.capadeservicios.servicios.Rol roles) {
        com.pangea.capadeservicios.servicios.GestionDeGrupo port = service_1.getGestionDeGrupoPort();
        return port.listarUsuariosPorGrupoYRol(grupousuarios, roles);
    }
}
