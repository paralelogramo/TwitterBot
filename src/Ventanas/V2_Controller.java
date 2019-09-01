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

import Clases.ControlVentana;
import Clases.Mensaje;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import javax.imageio.ImageIO;
/**
 * 
 * FXML Controller class
 * Clase xxx: - Clase que representa blablablabla.
 * @author Fernando Pino,  Carmen Ortega,
 *          Vicente Rojas, Kevin Douglas.
 * @version 1.0
 */
public class V2_Controller extends ControlVentana implements Initializable {
    
    @FXML AnchorPane ap;
    @FXML TextArea msj;
    @FXML Text usuario;
    @FXML Text usuario2;
    @FXML Text correo;
    @FXML Text notificacionImagen;
    @FXML ProgressIndicator pgA;
    @FXML ImageView imagenPerfil;
    @FXML ImageView imagenPerfil2;
    @FXML WebView vista;
    @FXML ListView listaTiempo;
    @FXML ImageView preImage;
    private Image profilePhotoImage;
    private File imgFile;
    private Stage stage = this.stage;
    private Twitter twitter;
    private List<Status> lineaDeTiempo;
    
    ArrayList<Long> mensajeId = new ArrayList();
    ArrayList<String> textoMsj = new ArrayList();
    
    public void progresoTexto(KeyEvent event){      
        if(msj.getText().length()>20)
            msj.setEditable(true);
        pgA.setProgress(msj.getText().length()/280.0);
    }
    
    // METODO LISTO
    public int enviarTwitter(MouseEvent event) throws TwitterException{       
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje(msj.getText());              
        StatusUpdate status = new StatusUpdate(mensaje.getMensaje());
        if (imgFile != null) {
            status.setMedia(imgFile);
            twitter.updateStatus(status);
            notificacionImagen.setVisible(false);
            return 0;
        }
        if(mensaje.verificar() && imgFile == null){
            twitter.updateStatus(status);
            msj.clear();
            notificacionImagen.setVisible(false);
            vista.getEngine().reload();
        }
        return 0;
    }
    
    // METODO LISTO
    public void subirImagen_Twitter(MouseEvent event) throws TwitterException, FileNotFoundException{
        String extension;
        FileChooser filech = new FileChooser();
        filech.setTitle("Buscar Imagen");
        filech.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4")
        );
        imgFile = filech.showOpenDialog(stage);
        if (imgFile != null) {
            extension = obtenerExtension(imgFile.getAbsolutePath());
            notificacionImagen.setVisible(true);
            notificacionImagen.setText(extension.toUpperCase()+" Subido Con Exito!!");
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png")) {
                if (imgFile.length()>1000000) {
                    System.out.println("tamaño no permitido");
                    imgFile = null;
                    notificacionImagen.setText("Tamaño Archivo No Soportado!!");
                    this.preImage.setImage(null);
                    // INGRESAR IMAGEN PREDETERMINADA
//                    this.preImage.setImage(new Image("src\\Imagenes\\Overflow.png"));
                }
                else{
                    Image imagen123 = new Image(new FileInputStream(imgFile));
                    this.preImage.setImage(imagen123);
                    notificacionImagen.setVisible(true);
                }
            }
            else
                if(extension.equalsIgnoreCase("mp4") && imgFile.length()>512000000){
                    System.out.println("tamaño no permitido");
                    imgFile = null;
                    notificacionImagen.setText("Tamaño Archivo No Soportado!!");
                    this.preImage.setImage(null);
                }
        }
    }
    
    // METODO LISTO
    public void subirGif_Twitter(MouseEvent event) throws TwitterException, FileNotFoundException{
        FileChooser filech = new FileChooser();
        filech.setTitle("Buscar Gif");
        filech.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );
        imgFile = filech.showOpenDialog(stage);
        if (imgFile != null) {
            notificacionImagen.setText("GIF Subido Con Exito!!");
            if (imgFile.length()>15000000) {
                System.out.println("tamaño no permitido");
                imgFile = null;
                notificacionImagen.setText("Tamaño Archivo No Soportado!!");
                this.preImage.setImage(null);
            }
            Image imagen123 = new Image(new FileInputStream(imgFile));
            this.preImage.setImage(imagen123);
            notificacionImagen.setVisible(true);
        }
    }
    
    // METODO LISTO
    private static String obtenerExtension(String filename){
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        }
        else{
            return filename.substring(index+1);
        }
    }
    
    
    // METODO LISTO
    public void eliminarTwitter(MouseEvent event) throws TwitterException{
        
        System.out.println("Entro");
        ResponseList<Status> Line = twitter.getHomeTimeline();
        Line.forEach((status) -> {
            mensajeId.add(status.getId());
            textoMsj.add(status.getText());
            //System.out.println("ID: " + status.getId() + " - " + status.getText());
        });
        System.out.println("talla ids: "+mensajeId.size());
        System.out.println("talla textos " + textoMsj.size());
         for (int i = 0; i<mensajeId.size(); i++) {
            System.out.println(i+"."+textoMsj.get(i));
        }
         
        System.out.print("numero del twitt a eliminar: ");
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        
        twitter.destroyStatus(mensajeId.get(opcion));
        mensajeId.remove(opcion);
        textoMsj.remove(opcion);
        System.out.println("mesaje eliminado");
        vista.getEngine().reload();
        /* long id = sc.nextLong();
        
        for (Status status : Line) {
            if (id==status.getId()) {
                twitter.destroyStatus(id);
                System.out.println("Twitt "+id+" eliminado");
                vista.getEngine().reload();
            }
        }*/
        
  
        
       
        
    }
    
    
    public void seguirUsuario(MouseEvent event) throws TwitterException{
        
    }
    
    
    public void noSeguirUsuario(MouseEvent event) throws TwitterException{
        
    }
    
    //vista protegida?
    public void costumeImageView(){
        
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("FI7zwXVxcqKBAozpTrqxJNfIA")
        .setOAuthConsumerSecret("bNjRO185DPOXnFqMOXJ2arJOQF1EG3TLFKXQPGCoCrM7649Lpe")
        .setOAuthAccessToken("1160640022971867138-WMcI6McTZAjpQ81949F6pr8NhfyO5a")
        .setOAuthAccessTokenSecret("2YtfMfSlpMUO3QlS3adHSw9AJmTqJMXiZC9D4BEcyhP7Q")
        .setIncludeEmailEnabled(true);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        
        // ****** ESTO GENERA EL WEBVIEW ******
        WebEngine engine = vista.getEngine();
        engine.setUserAgent("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");       
        engine.load("https://twitter.com/power_java");
        String webViewContents = (String) engine
            .executeScript("document.documentElement.outerHTML");
        StringBuilder scrollHtml = scrollWebView(0, 50);
        String appendContent = "<div id='append'>Appended html content</div> Appended text content";
        //engine.loadContent(scrollHtml + webViewContents);
        vista.setContextMenuEnabled(false);      
        vista.setZoom(0.80);        
        // ************ HASTA ACA *********
        msj.setWrapText(true);
        try {
            lineaDeTiempo = twitter.getUserTimeline();            
            User newUser = twitter.showUser(twitter.getScreenName());            
            usuario.setText("@"+newUser.getScreenName());
            usuario2.setText(newUser.getName());
            imagenPerfil.setImage(new Image(newUser.get400x400ProfileImageURL()));
            imagenPerfil2.setImage(new Image(newUser.get400x400ProfileImageURL()));
            correo.setText(newUser.getEmail());
            
        } catch (TwitterException | IllegalStateException ex) {
            Logger.getLogger(V2_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    @FXML
    public void cerrarSesion(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        URL location = V1_Control.class.getResource("V1.fxml");
        loader.setLocation(location);
        Stage v1 = new Stage();
        v1.setTitle(" TwitterBot_ | Inicio");
        v1.getIcons().add(new Image(getClass().getResourceAsStream("/Imagenes/icon.png"))); 
        v1.setOpacity(1);         
        AnchorPane inicioSesion = loader.load();        
        Scene scene = new Scene(inicioSesion);     
        //stage.setOpacity(0.95);
        v1.setScene(scene);
        v1.initOwner(this.ap.getScene().getWindow());            
        v1.setResizable(false);
        ((Stage)this.ap.getScene().getWindow()).close();     
        v1.initStyle(StageStyle.TRANSPARENT);
        v1.centerOnScreen();
        v1.show();  
    }
    
    public static StringBuilder scrollWebView(int xPos, int yPos) {
        StringBuilder script = new StringBuilder().append("<html>");
        script.append("<head>");
        script.append("   <script language=\"javascript\" type=\"text/javascript\">");
        script.append("       function toBottom(){");
        script.append("           window.scrollTo(" + xPos + ", " + yPos + ");");
        script.append("       }");
        script.append("   </script>");
        script.append("</head>");
        script.append("<body onload='toBottom()'>");
        return script;
    }
}