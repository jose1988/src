/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.PangeaFlowProyecto.control;
import com.pangea.capadeservicios.servicios.Post;
import com.pangea.capadeservicios.servicios.Usuario;
import com.pangea.capadeservicios.servicios.Bandeja;
import com.pangea.capadeservicios.servicios.Mensajeria_Service;
import com.pangea.capadeservicios.servicios.WrBandeja;
import com.pangea.capadeservicios.servicios.WrPost;
import com.pangea.capadeservicios.servicios.WrResultado;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * @author Pangea
 */

@ManagedBean(name = "loginController")

@SessionScoped

public class loginController {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_15362/CapaDeServicios/Mensajeria.wsdl")
    private Mensajeria_Service service;
   
    private TreeNode mailboxes;

    private List<Post> mails;
    
    private List<WrBandeja> ban;

    private Post mail;

    private TreeNode mailbox;
    
    private Usuario idusu;
    
    private WrBandeja bande;
    private WrPost bandej;
    
    private Bandeja idban;
  

    public void setMailboxes(TreeNode mailboxes) {
        this.mailboxes = mailboxes;
    }

    public void setMails(List<Post> mails) {
        this.mails = mails;
    }

    @PostConstruct
    public void init() {
        mailboxes = new DefaultTreeNode("root", null);
        idusu=new Usuario();
        idusu.setId("admin");
         String icono;
        bande=consultarBandejas(idusu);
        int i=0;
        while (bande.getBandejas().size()>i){
            if("Enviados".equals(bande.getBandejas().get(i).getNombre())){
             icono="s"; 
            }else if("Papelera".equals(bande.getBandejas().get(i).getNombre())){
             icono="t"; 
            }else if("Recibidos".equals(bande.getBandejas().get(i).getNombre())){
             icono="i"; 
            }else{
            icono="j";
            }
        TreeNode inbox = new DefaultTreeNode(icono, bande.getBandejas().get(i).getNombre(), mailboxes);
       i++;
        }
        int j=0;  
        idban=new Bandeja();
        idban.setId(bande.getBandejas().get(0).getId());
        bandej=consultarMensajes(idusu, idban);
         mails = new ArrayList<Post>();
         while (bandej.getPosts().size()>j){
         mail=bandej.getPosts().get(j);
         mails.add(mail);
         j++;
        }
    }

    public TreeNode getMailboxes() {
        return mailboxes;
    }

    public List<Post> getMails() {
        return mails;
    }

    public Post getMail() {
        return mail;
    }

    public void setMail(Post mail) {
        this.mail = mail;
    }

    public TreeNode getMailbox() {
        return mailbox;
    }

    public void setMailbox(TreeNode mailbox) {
        this.mailbox = mailbox;
    }

    public void send() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mail Sent!"));
    }

    private static WrPost consultarMensajes(com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Bandeja bandejaActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.consultarMensajes(usuarioActual, bandejaActual);
    }

    private static WrPost consultarMensaje(com.pangea.capadeservicios.servicios.Post mensajeActual, com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.consultarMensaje(mensajeActual, usuarioActual);
    }

    private static WrResultado moverMensaje(com.pangea.capadeservicios.servicios.PostEnBandeja postEnBandejaActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.moverMensaje(postEnBandejaActual);
    }

    private static WrResultado enviarMensaje(com.pangea.capadeservicios.servicios.WrDestinatario destinatarios, com.pangea.capadeservicios.servicios.Usuario usuarioActual, com.pangea.capadeservicios.servicios.Mensaje mensajeActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.enviarMensaje(destinatarios, usuarioActual, mensajeActual);
    }

    private static WrBandeja consultarBandejas(com.pangea.capadeservicios.servicios.Usuario usuarioActual) {
        com.pangea.capadeservicios.servicios.Mensajeria_Service service = new com.pangea.capadeservicios.servicios.Mensajeria_Service();
        com.pangea.capadeservicios.servicios.Mensajeria port = service.getMensajeriaPort();
        return port.consultarBandejas(usuarioActual);
    }


   
}