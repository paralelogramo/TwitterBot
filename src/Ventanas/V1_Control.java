/*
Copyright(C) 2019 TwitterProyectoCorp.
Este programa es de sofware libre: puede redistribuirlo y/o modificarlo bajo los
términos de la Licencia Públic General de GNU publicada por Free Sofware Foundation,
ya sea la versión 3 de la licencia o (a su elección) cualquier version anterior.
**
Este programa se distribuye con la esperanza de que sea útil, pero SIN NINGUNA
GARANTÍA; sin siquiera la garantía implícita de COMERCIABILIDAD O APTITUD PARA
UN PROPÓSITO EN PARTICULAR. Ver la Licencia Pública genenral GNU para mas
detaller.
**
Debería haber resivido una copia de la Licencia Pública General de GNU junto con
este programa. Si no, vea <http://www.gnu.org/licenses/>.
*/
package Ventanas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import Clases.ControlVentana;
import Clases.Usuario;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * FXML Controller class
 * Clase xxx: - Clase que representa blablablabla.
 * @author Fernando Pino,  Carmen Ortega,
 *          Vicente Rojas, Kevin Douglas.
 * @version 1.0
 */
public class V1_Control extends ControlVentana implements Initializable {
    
    @FXML private AnchorPane ap;
    @FXML private AnchorPane ingresar;
    @FXML private AnchorPane recuperar;
    @FXML private ProgressIndicator cargando;
    @FXML private TextField usuario;
    @FXML private PasswordField clave; 
    private final String servidorDB = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10304415";
    private final String usuarioDB = "sql10304415";
    private final String claveDB = "55M5SGgfTH";
    private Connection coneccionDB;
    private final String dirWeb = "www.twitter.com";
    private final int puerto = 80;
    private String usuarioRed;
    private String claveRed;            
    private final Pattern patronUsuario = Pattern.compile("^[a-zA-Z0-9]{4,10}$", Pattern.MULTILINE);
    private final Pattern patronCorreo = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", Pattern.MULTILINE);
    
    private final ArrayList<Usuario> usuarios = new ArrayList<>();    
    //Shape forma = new RoundRectangle2D.Double(0, 0, ap.getBoundsInLocal().getWidth(), ap.getBoundsInLocal().getHeight(), 30, 30); 
    
    public void minimizarVentana(MouseEvent event){    
        ((Stage) ap.getScene().getWindow()).setIconified(true);        
    }    
    
    @FXML
    public void inicioSesion(MouseEvent evento){         
        usuario.setStyle("-fx-text-fill: rgba(255,255,255,1); -fx-background-color: rgba(0,0,0,0.6);");
        clave.setStyle("-fx-text-fill: rgba(255,255,255,1); -fx-background-color: rgba(0,0,0,0.6);");        
        String consulta = "SELECT * FROM Usuario WHERE Usuarios='"+usuario.getText()+"' && clave='"+clave.getText()+"'";         
        //ResultSet resultado = coneccionDB.createStatement().executeQuery(consulta);
        //String aux = resultado.getString(consulta);
        //System.out.println(aux);
        if(this.esCompatible(usuario.getText(), clave.getText()))           
            if(this.ingresar(usuario.getText(), clave.getText(), new Usuario (usuario.getText(), clave.getText(),"")))
                iniciarSesion();  
            else{                
                Toolkit.getDefaultToolkit().beep();            
                JOptionPane auxiliar = new JOptionPane();
                auxiliar.setMessage("Nombre usuario y/o clave incorrecta");
                auxiliar.setMessageType(0);
                JDialog dialog = auxiliar.createDialog("TwitterBot_ | Error");
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(true);    
                clave.clear();                
            }  
        else{            
            Toolkit.getDefaultToolkit().beep();            
            JOptionPane auxiliar = new JOptionPane();
            auxiliar.setMessage("Nombre usuario y/o clave no valida");
            auxiliar.setMessageType(0);
            JDialog dialog = auxiliar.createDialog("TwitterBot_ | Error");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);    
            clave.clear();            
        }       
    }      
    
    @FXML 
    public void clickEnter(KeyEvent event){
        if(event.getKeyCode()==KeyEvent.VK_ENTER){
            //cargando.setVisible(true);                   
        }
    }
    
    @FXML
    public void efectoUsuario(Event event){
        usuario.setStyle("-fx-text-fill: black; -fx-background-color: rgba(0,0,0,0.1);");
        clave.setStyle("-fx-text-fill: rgba(255,255,255,1); -fx-background-color: rgba(0,0,0,0.6);");
    }
    
    @FXML
    public void efectoClave(Event event){        
        clave.setStyle("-fx-text-fill: black; -fx-background-color: rgba(0,0,0,0.1);");
        usuario.setStyle("-fx-text-fill: rgba(255,255,255,1); -fx-background-color: rgba(0,0,0,0.6);");
    }
    
    
    private boolean esCompatible(String usuario,String clave){       
        return patronUsuario.matcher(usuario).matches() && patronUsuario.matcher(clave).matches();
    }
    
    private boolean ingresar(String usuario,String clave,Usuario usuarioRemoto){              
        return usuarios.contains(usuarioRemoto) && usuarios.get(usuarios.indexOf(usuarioRemoto)).contraseñaValida(clave);
    }
    
    @FXML
    private void iniciarSesion(){
        FXMLLoader loader = new FXMLLoader();
        URL location = V1_Control.class.getResource("V2.fxml");
        loader.setLocation(location);
        Stage stage = new Stage();
        stage.setTitle(" TwitterBot_ | Panel de Control");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Imagenes/icon.png"))); 
        stage.setOpacity(1);         
        AnchorPane panelControl;        
        try {
            panelControl = loader.load();
            Scene scene = new Scene(panelControl); 
            stage.setScene(scene);
        } catch (IOException ex) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane auxiliar = new JOptionPane();
            auxiliar.setMessage("El proceso no puede cargar la ventana (archivo: V2.fxml)");
            auxiliar.setMessageType(0);
            JDialog dialog = auxiliar.createDialog("TwitterBot_ | Error ");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);       
        }            
        //stage.setOpacity(0.95);        
        stage.initOwner(this.ap.getScene().getWindow());            
        stage.setResizable(false);
        ((Stage)this.ap.getScene().getWindow()).close();     
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        stage.show();       
    }
        
    @FXML
    private void recuperarUsuario(MouseEvent event){
        ingresar.setVisible(false);
        recuperar.setVisible(true);
        
    }
    
    @FXML
    private void volver(MouseEvent event){
        ingresar.setVisible(true);
        recuperar.setVisible(false);        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {          
        try {                      
            this.coneccionDB = DriverManager.getConnection(servidorDB, usuarioDB, claveDB);
            Toolkit.getDefaultToolkit().beep();            
            JOptionPane auxiliar = new JOptionPane();
            auxiliar.setMessage("Conectado a servidor con exito");
            auxiliar.setMessageType(1);
            JDialog dialog = auxiliar.createDialog("TwitterBot_ | Conexión ");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);            
        } catch (SQLException ex) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane auxiliar = new JOptionPane();
            auxiliar.setMessage("Error en conexión a servidor");
            auxiliar.setMessageType(0);
            JDialog dialog = auxiliar.createDialog("TwitterBot_ | Conexión ");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);       
        }
        try{
            Socket s = new Socket(dirWeb, puerto);
            Toolkit.getDefaultToolkit().beep();            
            JOptionPane auxiliar = new JOptionPane();
            auxiliar.setMessage("Conectado a Twitter.com con exito");
            auxiliar.setMessageType(1);
            JDialog dialog = auxiliar.createDialog("TwitterBot_ | Conexión ");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);            
        } catch (HeadlessException | IOException | SecurityException ex) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane auxiliar = new JOptionPane();
            auxiliar.setMessage("Error en conexión a Twitter.com");
            auxiliar.setMessageType(0);
            JDialog dialog = auxiliar.createDialog("TwitterBot_ | Conexión ");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);       
        }
        usuarios.add(new Usuario("user","admin",""));
        System.out.println(new Usuario("user","admin","").getClave());
        this.arrastrarVentana(this.ap);            
    }    
}