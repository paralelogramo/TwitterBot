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
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
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
    private final String servidorDB = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10303413";
    private final String usuarioDB = "sql10303413";
    private final String claveDB = "HAW2rTnKZ4";
    private Connection coneccionDB;
    private final Pattern patronUsuario = Pattern.compile("^[a-zA-Z0-9]{4,10}$", Pattern.MULTILINE);
    private final Pattern patronCorreo = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", Pattern.MULTILINE);
    
    private final ArrayList<Usuario> usuarios = new ArrayList<>();    
    //Shape forma = new RoundRectangle2D.Double(0, 0, ap.getBoundsInLocal().getWidth(), ap.getBoundsInLocal().getHeight(), 30, 30); 
    
    public void minimizarVentana(MouseEvent event){       
        //ap.setVisible(false);
        // url('/Imagenes/background1.png')
    }    
    
    @FXML
    public void inicioSesion(MouseEvent evento){         
        usuario.setStyle("-fx-text-fill: rgba(255,255,255,1); -fx-background-color: rgba(0,0,0,0.6);");
        clave.setStyle("-fx-text-fill: rgba(255,255,255,1); -fx-background-color: rgba(0,0,0,0.6);");
        
        String consulta = "SELECT * FROM Usuario WHERE usuario='"+usuario.getText()+"' && clave='"+clave.getText()+"'";
        try{
        ResultSet resultado = coneccionDB.createStatement().executeQuery(consulta);
        String aux = resultado.getString(consulta);
        }catch (SQLException e){
            
        }
        if(this.esCompatible(usuario.getText(), clave.getText()))           
            if(this.ingresar(usuario.getText(), clave.getText(), new Usuario (usuario.getText(), clave.getText(),"")))
                try {  
                    iniciarSesion();
                } catch (IOException ex) {
                    Logger.getLogger(V1_Control.class.getName()).log(Level.SEVERE, null, ex);
                }  
            else{
                Toolkit.getDefaultToolkit().beep();
                this.popUp("Error", "Nombre usuario y/o clave incorrecta");
                clave.clear();
                cargando.setVisible(false);
            }  
        else{
            Toolkit.getDefaultToolkit().beep();
            this.popUp("Error", "Nombre usuario y/o clave no valida");
            clave.clear();
            cargando.setVisible(false);
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
    private void iniciarSesion() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        URL location = V1_Control.class.getResource("V2.fxml");
        loader.setLocation(location);
        Stage stage = new Stage();
        stage.setTitle(" TwitterBot_ | Panel de Control");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Imagenes/icon.png"))); 
        stage.setOpacity(1);         
        AnchorPane panelControl = loader.load();        
        Scene scene = new Scene(panelControl);     
        //stage.setOpacity(0.95);
        stage.setScene(scene);
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
            JOptionPane.showMessageDialog(null, "Conectado a servidor con exito", "Conección", 1);              
        } catch ( SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en coneccion con servidor", "Conección", 0);       
        }
        usuarios.add(new Usuario("user","admin",""));
        this.arrastrarVentana(this.ap);            
    }    
}