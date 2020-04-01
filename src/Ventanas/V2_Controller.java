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
import Clases.Tweet;
import Clases.Trie;
import Clases.UserDM;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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
import twitter4j.IDs;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.ListCell;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.SepiaTone;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import twitter4j.UploadedMedia;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.concurrent.TimeUnit;

/**
 * 
 * FXML Controller class
 * Clase V2_Controller: - Clase Correspondiente al panel de control del bot, aqui
 * es en donde se tiene acceso a todas las tareas que se pueden realizar con el bot.
 * @author Fernando Pino,  Carmen Ortega,
 *          Vicente Rojas.
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
    @FXML ListView lista;
    @FXML ListView lista_de_usuarios;
    @FXML TextField busqueda;
    @FXML Button check;
    @FXML ImageView preImage;
    @FXML Text avisolimite;
    @FXML ImageView equis;
    @FXML ImageView continuar;
    @FXML ImageView pausa;

    public ArrayList<String> getUsuariosSeguidos() {
        return usuariosSeguidos;
    }
    
    @FXML ImageView parar;
    @FXML ListView<String> listView = new ListView<String>();
    @FXML MediaView mv;
    Image imagen123;
    private MediaPlayer mp;
    private Media med;
    private Image profilePhotoImage;
    private File imgFile;
    private Stage stage = this.stage;
    public static Twitter twitter;
    private Twitter sender;
    private List<Status> lineaDeTiempo;   
    private final char arroa = 64;
    private ArrayList<UserDM> userDM = new ArrayList<>();
    private final ArrayList<String> usuariosSeguidos = new ArrayList<>();
    private List<Status> listaTweets;
    private List<Status> ownTweets;
    private ArrayList<Tweet> listaTimeline = new ArrayList<>();
    private ArrayList<Integer> likeados = new ArrayList<>();
    private long seleccionTweet = 0;
    private boolean esVideo = false;
    private Trie trieNSFW = new Trie();
    private Trie trieSW = new Trie();
    private boolean hayComandos = false;
    private String usuarioSeleccionado;
    private String usuarioSeleccionado2;
    private ArrayList<long[]> retweetsID = new ArrayList<>();
    private ArrayList<long[]> retweetsID_Actual = new ArrayList<>();
    private ArrayList<long[]> likesID = new ArrayList<>();
    private ArrayList<long[]> likesID_Actual = new ArrayList<>();
    
    Scanner sc = new Scanner(System.in);
    

    public void seleccionTweet(MouseEvent event){
        try{
            Tweet auxiliar =  (Tweet) lista.getSelectionModel().getSelectedItem();
            String idString = String.valueOf(auxiliar.getId());
            StringSelection stringSelect = new StringSelection(idString);
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(stringSelect, null);
            seleccionTweet = auxiliar.getId();           
            usuarioSeleccionado = auxiliar.getUsuario();
        }catch(NullPointerException e){
        }
    }
    
    /**
     * ProgresoTexto: - Método que perminte saber la cantidad de carácteres ingresados
     * en el cuadro de texto antes de realizar el twitt.
     * @param event 
     */
    public void progresoTexto(Event event){   
        System.out.println(msj.getAnchor());
        pgA.setProgress(msj.getText().length()/280.0);  
        pgA.progressProperty().addListener((ov, oldValue, newValue) -> {
            Text text = (Text) pgA.lookup(" .percentage");
            if(text!=null){
               text.setText(Integer.toString(280-msj.getText().length()));               
            }
        });                      
        if(msj.getText().length()>=250){
            pgA.setStyle("-fx-progress-color: darkorange;");
            System.out.println("aaa");
            
        }else{
            pgA.setStyle("-fx-progress-color: darkblue;");
        }
        if(msj.getText().length()==280){
            pgA.setProgress(0.999999);
            avisolimite.setVisible(false);
        }
        if(msj.getText().length()>=270){                
            pgA.setStyle("-fx-progress-color: crimson;");
            if(msj.getText().length()>280){
                avisolimite.setVisible(true);
                pgA.setProgress(0.999999);
            }                    
            
        }
    }
    
    public void minimizarVentana(MouseEvent event){    
        ((Stage) ap.getScene().getWindow()).setIconified(true);        
    }    
    
    // METODO LISTO
    /**
     * EnviarTwitter: - Método que permite publicar un twitt, ya sea texto plano o
     * un arvhivo.
     * @param event
     * @return 
     */
    public int enviarTwitter(MouseEvent event){       
        String[] msg = null;
        StatusUpdate status = null;
        Mensaje mensaje = new Mensaje();
        Mensaje original = new Mensaje();
        mensaje.setMensaje(msj.getText());
        original.setMensaje(msj.getText());
        String mensajeLowerCase = mensaje.getMensaje().toLowerCase();
        long[] mediaIds = new long[1];
        String mensaje_limpio = "";
        try {
            msg = comandos(msj.getText());
        } catch (TwitterException e) {
        }
        for (int i = 0; i < msg.length; i++) {
            if (msg[i]!="") {
                mensaje_limpio = mensaje_limpio.concat(msg[i]);
                mensaje_limpio = mensaje_limpio.concat(" ");
            }
        }
        
        double porcentaje = noSW(mensaje_limpio);
        double porcentaje2 = noNSFW(mensaje_limpio);
        if (porcentaje>=0.70) {
            this.popUp(1, "¡ES SPAM! ¡TWEETER NO ENVIADO!", "ERROR");
            msj.clear();
            notificacionImagen.setVisible(false);
            this.pgA.setProgress(0);
            this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));
            this.equis.setImage(null);
            return 0;
        }
        if (porcentaje2>=0.70) {
            this.popUp(1, "¡ORDINARIO! ¡TWEETER NO ENVIADO!", "ERROR");
            msj.clear();
            notificacionImagen.setVisible(false);
            this.pgA.setProgress(0);
            this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));
            this.equis.setImage(null);
            return 0;
        }
        else{
            mensaje.setMensaje(mensaje_limpio);
            status = new StatusUpdate(mensaje.getMensaje());
        }        
        
        if (imgFile != null) {
            try {
                if (!esVideo) {
                    this.mp = null;
                    this.mv.setMediaPlayer(null);
                    status.setMedia(imgFile);
                }
                else{
                    this.imagen123 = null;
                    InputStream target = new FileInputStream(this.imgFile);
                    UploadedMedia um = twitter.uploadMediaChunked("Video", target);
                    mediaIds[0] = um.getMediaId();
                    status.setMediaIds(mediaIds);
                }
                if (this.hayComandos && mensaje_limpio.length()==0) {
                    this.popUp(1, "Hashtags Exitosos", "Hashtags");
                    this.hayComandos = false;
                }
                else{
                    if (this.hayComandos && mensaje_limpio.length()!=0) {
                        this.popUp(1, "Hashtags Exitosos", "Hashtags");
                        twitter.updateStatus(status);
                        this.hayComandos = false;
                    }
                    else{
                        twitter.updateStatus(status);
                        this.hayComandos = false;
                    }
                }
                msj.clear();
                this.hayComandos=false;
                notificacionImagen.setVisible(false);
                this.pgA.setProgress(0);
                this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));
                this.equis.setImage(null);
                this.esVideo = false;
                this.imgFile = null;
                this.mv.setMediaPlayer(null);
                this.popUp(1, "Twitt publicado con exito", "Twittear");
                this.actualizarLista();
                return 0;
            } catch (TwitterException ex) {
                System.out.println("Error, No se puede enviar el twit");
            } catch (FileNotFoundException ex) {
                this.popUp(0, "se cayo porque no hay archivo :v", "Error");
                System.out.println("se cayo porque no hay archivo :v");
            }
            msj.clear();
            this.hayComandos=false;
        }
        if(mensaje.verificar() && imgFile == null){
            try {
                if (this.hayComandos) {
                    this.popUp(1, "Hashtags Exitosos", "Hashtags");
                }
                twitter.updateStatus(status);
                this.hayComandos = false;
                msj.clear();
                notificacionImagen.setVisible(false);
                this.pgA.setProgress(0);
                this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));
                this.equis.setImage(null);
                this.popUp(1, "Twitt publicado con exito", "Twittear");
                this.actualizarLista();
                //this.ejecutarComandos(hashtagsActivos);
                return 0;
            } catch (TwitterException ex) {
               this.popUp(0, "Error Tweet vacio o repetido", "Error");
               System.out.println("entra al try catch linea 290");
                
                
            }
        }else{
            if (this.hayComandos) {
                this.popUp(1, "Hashtags Exitosos", "Hashtags");
            }
            else{
                
                 this.popUp(0, "Debe incluir un mensaje o un archivo", "Error");
                 System.out.println("error linea 318");
            }
        }
        this.msj.clear();
        this.actualizarLista();
        this.hayComandos=false;
        
        return 0;        
    }
    
    /**
     * actualizarLista: - Método que permite actuliazar el webView del bot
     */
    public void actualizarLista(){
        try { 
            this.listaTimeline.clear();
            listaTweets = twitter.getHomeTimeline();
           
            for (int i = 0; i < listaTweets.size(); i++) {    
                this.listaTimeline.add(new Tweet(listaTweets.get(i),new Image (listaTweets.get(i).getUser().getMiniProfileImageURL())));
            }            
            ObservableList<Tweet> oLista = FXCollections.observableArrayList(listaTimeline);            
            lista.setItems(oLista);
            lista.refresh();
            
        } catch (TwitterException ex) {
            Logger.getLogger(V2_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    // METODO LISTO
    
    /**
     * subirImagen_Twitter: - Método que permite twittear imagenes, verificando su
     * formato y tamaño antes de ser subida.
     * @param event 
     */
    public void subirImagen_Twitter(MouseEvent event){
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
            this.equis.setImage(new Image("/Imagenes/close.png"));
            extension = obtenerExtension(imgFile.getAbsolutePath());
            notificacionImagen.setVisible(true);
            notificacionImagen.setText(extension.toUpperCase()+" Cargado Con Exito!!");
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png")) {
                if (imgFile.length()>1000000) {
                    System.out.println("Tamaño no permitido");
                    this.imgFile = null;                    
                    equis = null;
                    notificacionImagen.setText("Tamaño Archivo No Soportado!!");
                    this.preImage.setImage(null);                    
                }
                else{
                    try {
                        this.parar.setVisible(false);
                        this.pausa.setVisible(false);
                        this.continuar.setVisible(false);
                        this.mv.setMediaPlayer(null);                        
                        imagen123 = new Image(new FileInputStream(imgFile));
                        this.preImage.setImage(imagen123);                        
                        this.preImage.toFront();
                        this.equis.toFront();
                        this.mv.toBack();                        
                        notificacionImagen.setVisible(true);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Error, Imagen no encontrada");
                    }
                }
            }
            else
                if(extension.equalsIgnoreCase("mp4") && imgFile.length()>512000000){
                    System.out.println("tamaño no permitido");
                    imgFile = null;
                    notificacionImagen.setText("Tamaño Archivo No Soportado!!");
                    this.preImage.setImage(null);
                }
                else{
                    if (extension.equalsIgnoreCase("mp4")) {                        
                        String dir = this.imgFile.getAbsolutePath();
                        med = new Media (new File (dir).toURI().toString());
                        mp = new MediaPlayer(med);
                        mv.setMediaPlayer(mp);
                        this.parar.setVisible(true);
                        this.pausa.setVisible(true);
                        this.continuar.setVisible(true);
                        this.preImage.setImage(null);
                        this.imagen123 = null;
                        this.esVideo = true;
                        this.preImage.toBack();
                        this.mv.toFront();
                        this.equis.toFront();
                    }
                }
            
        }
        if(this.preImage.getImage() == null)
            this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));   
    }
    
    /**
     * pausar_video: - Permite la pausa un video en la vista previa.
     * @param event 
     */
    public void pausar_Video (MouseEvent event){
        this.mp.pause();
    }
    
    /**
     * continuar_video: - Permite la continuacion/reproduccion un video en la 
     * vista previa.
     * @param event 
     */
    public void continuar_Video (MouseEvent event){
        this.mp.play();
    }
    
    /**
     * detener_video: - Permite en la vista previa.
     * @param event 
     */
    public void detener_Video (MouseEvent event){
        this.mp.stop();
    }
    
    // METODO LISTO
    public void subirGif_Twitter(MouseEvent event){
        FileChooser filech = new FileChooser();
        filech.setTitle("Buscar Gif");
        filech.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );
        imgFile = filech.showOpenDialog(stage);
        if (imgFile != null) {
            try {
                notificacionImagen.setText("GIF Cargado Con Exito!!");
                if (imgFile.length()>15000000) {
                    System.out.println("Tamaño no permitido");
                    imgFile = null;
                    notificacionImagen.setText("Tamaño Archivo No Soportado!!");
                    this.preImage.setImage(null);
                }
                
                this.equis.setImage(new Image("/Imagenes/close.png"));
                Image imagen123 = new Image(new FileInputStream(imgFile));
                this.preImage.setImage(imagen123);
                notificacionImagen.setVisible(true);
            } catch (FileNotFoundException ex) {
                System.out.println("Error, Gif no encontrado");
            }
        }
    }
    
    @FXML
    public void eliminarImagenSubida(){
        this.imgFile = null;
        this.equis.setImage(null);
        this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));
        this.mv.setMediaPlayer(null);
        this.mv.toBack();
        this.notificacionImagen.setText(("Archivo eliminado con exito!"));
    }
    
    // METODO LISTO
    
    /**
     * obtenerExtension: - Método que permite obtner la extension del archivo 
     * que se desea subir.
     * @param filename
     * @return 
     */
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
    
    /**
     *eliminarTwitter: Método que permite eliminar un twitt publicado.
     * @param event 
     */
    public void eliminarTwitter(MouseEvent event){
       
        ArrayList<Long> mensajeId = new ArrayList();
        ArrayList<String> textoMsj = new ArrayList();        
        
        int opcion=-1;
        String opcion2;
        int largoArreglo;
    
        try {
            ResponseList<Status> Line = twitter.getUserTimeline();
            
            Line.forEach((status) -> {
                mensajeId.add(status.getId());
                textoMsj.add(status.getText());
            });
            
            if(mensajeId.contains(this.seleccionTweet)){
                twitter.destroyStatus(this.seleccionTweet);
                mensajeId.remove(this.seleccionTweet);
                this.seleccionTweet = 0;
                this.popUp(1, "Twitter eliminado con exito", "Eliminar Twitt");
            } else {
                this.seleccionTweet = 0;
                this.popUp(0, "No se puede eliminar tweet de tereros o tweets ya eliminados ", "Error");                
            }
            
        } catch (TwitterException ex) {
            this.popUp(1, "No se puede eliminar el tweet", "Info");
            System.out.println("Error, No se puede eliminar el tweet");
        }
        this.actualizarLista();
    }
    
    /**
     * retweetear: Método que permite retweetear un twitt especifico, ya sea de
     * otro usuario o del mismo bot
     * @param event 
     */
    public void retweetear(MouseEvent event){
        ArrayList<Long> mensajeId = new ArrayList();
        ArrayList<String> textoMsj = new ArrayList();
        Mensaje mensaje = new Mensaje();
        int opcion=-1;
        String opcion2;
        int largoArreglo;
    
        try {
            ResponseList<Status> Line = twitter.getHomeTimeline();
             
            Line.forEach((status) -> {
                mensajeId.add(status.getId());
                textoMsj.add(status.getText());
            });
            
            if(this.seleccionTweet==0){               
                this.popUp(0, "No se ha seleccionado tweet a retweetear", "Error");                          
            } else {
                twitter.retweetStatus(this.seleccionTweet);       
                this.seleccionTweet = 0;
                this.popUp(1, "Mensaje Retwiteado con Exito", "Retwittear");
            }
            
        } catch (TwitterException ex) {
            System.out.println("Error, no se puede retwittear el mensaje");
            this.popUp(0, "No se puede retweetear tweet inexistente o retweeteado nteriormente", "Error");
        }
        this.actualizarLista();
    }
    
    /**
     * seguirUsuario: Método que permite seguir a un usuario especifico en 
     * twitter
     * @param event 
     */
    public int seguirUsuario(MouseEvent event){
        if(this.usuarioSeleccionado2!=null){
            try {
                twitter.friendsFollowers().createFriendship(usuarioSeleccionado2);
                this.popUp(1, "Usuario "+usuarioSeleccionado2+" Seguido con exito!", "Twitter");
            } catch (TwitterException ex) {
                this.popUp(0, "Usuario ingresado no existe o ya lo sigues", "Error");
            }
        }
        else{
            this.popUp(0, "Usuario no seleccionado", "Error");
        }
        
        this.usuarioSeleccionado2 = null;
        
        this.actualizarLista();
        return 0;
    }   
    
    /**
     * noSeguriUsuario: Método que permite dejar de seguir un usuario 
     * especifico en twitter
     * @param event 
     */
    
    public void noSeguirUsuario(MouseEvent event){
        User u1 = null ;
        long indicador = -1;
        try{
            IDs ids;  
            do {    
                ids = twitter.getFriendsIDs(twitter.getScreenName(), indicador);
                for (long id : ids.getIDs()) {
                    User user = twitter.showUser(id);
                    usuariosSeguidos.add(arroa+user.getScreenName());              
                }
            } while ((indicador = ids.getNextCursor()) != 0);
        } catch (TwitterException | IllegalStateException ex) {                
            this.popUp(0, "Esta cuenta no sigue a ningun usuario", "Error");
        }
        if(usuariosSeguidos.isEmpty())
            this.popUp(0, "Esta cuenta no sigue a ningun usuario", "Error");
        else{            
            String [] seguidos = usuariosSeguidos.toArray(new String[usuariosSeguidos.size()]); 
            Icon icono = new ImageIcon("/Imagenes/icon.png");       
            try { 
                String seleccionado = (String) JOptionPane.showInputDialog(null, "Seleccionar usuario para dejar de seguir", "TwitterBot_ | Dejar de Seguir", JOptionPane.DEFAULT_OPTION, icono, seguidos, seguidos[0]);                          
                twitter.destroyFriendship(seleccionado);
                this.popUp(1, "Ha dejado de seguir al usuario "+seleccionado+" con exito", "Dejar de Seguir");
            } catch (NullPointerException | TwitterException ex) {
               //this.popUp(0, "Elemento seleccionado no es valido para la función", "Error");
            }            
        }
        this.usuariosSeguidos.clear();
    }
    
    @FXML
    public void mensajeDirecto(MouseEvent event){
        this.ventanaMensajes();             
    }
    
    public long ventanaLike(MouseEvent event){
        long id=0;  
        return id;
    }
    
    
    /**
     * darLike: Método que permite dar like a cualquier twitt seleccionado, ya sea del mismo
     * bot o de otro usuario.
     */
    public void darLike(){
        
        try{
            
            if(this.seleccionTweet==0){               
                this.popUp(0, "No se ha seleccionado tweet a dar like", "Error");                          
            } else {
                if(this.likeados.contains(this.seleccionTweet))
                    try{
                        twitter.destroyFavorite(this.seleccionTweet);                          
                    }catch(TwitterException e){
                        twitter.createFavorite(this.seleccionTweet);  
                    }
                else
                    try{
                        twitter.createFavorite(this.seleccionTweet);    
                    }catch(TwitterException e){
                        twitter.destroyFavorite(this.seleccionTweet);
                    }
                this.seleccionTweet = 0;
            }
            
        
        } catch (TwitterException e) {
            this.popUp(0, "Tweet ingresado no valido(no se puede dar like a un tweet eliminado)", "Error");           
        } 
       
        this.actualizarLista();
    }
    
    
    @FXML
    public void cerrarSesion(MouseEvent event){
        FXMLLoader loader = new FXMLLoader();
        URL location = V1_Control.class.getResource("V1.fxml");
        loader.setLocation(location);
        Stage v1 = new Stage();
        v1.setTitle("TwitterBot_ | Inicio");
        v1.getIcons().add(new Image(getClass().getResourceAsStream("/Imagenes/icon.png"))); 
        v1.setOpacity(1);         
        AnchorPane inicioSesion;        
        try {
            inicioSesion = loader.load();
            Scene scene = new Scene(inicioSesion);  
            v1.setScene(scene);
        } catch (IOException ex) {
            this.popUp(0, "El proceso no puede cargar la ventana (archivo: V1.fxml)", "Error");            
        }           
        
        v1.initOwner(this.ap.getScene().getWindow());            
        v1.setResizable(false);
        ((Stage)this.ap.getScene().getWindow()).close();     
        v1.initStyle(StageStyle.TRANSPARENT);
        v1.centerOnScreen();
        v1.show();  
    }
    
    public void ventanaMensajes(){        
        try {
            FXMLLoader loader = new FXMLLoader(V2_Controller.class.getResource("V3.fxml"));
            AnchorPane ventanaV3 = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Venta Dos");
            ventana.initOwner(this.ap.getScene().getWindow());
            Scene scene = new Scene(ventanaV3);
            scene.setFill(Color.TRANSPARENT);
            ventana.setScene(scene);
            V3_Control controller = loader.getController();
            ventana.initModality(Modality.WINDOW_MODAL);
            ventana.initStyle(StageStyle.TRANSPARENT);               
            ventana.show();
        } catch (IOException ex) {
            this.popUp(0, "El proceso no puede cargar la ventana (archivo: V3.fxml)", "Error");
        }
    }
    
    public char paraSaber(boolean sino){
        if (sino == false)
            return 9932;
        return 9989;
    }
    
    /**
     * Comandos: Método que permite el uso de comandos en el bot, los que 
     * corresponden a #seguir, #difundir, #gustar.
     * @param mensaje: corresponde al texto ingresado en el cuadro de texto
     * @return
     * @throws TwitterException 
     */
    public String[] comandos(String mensaje) throws TwitterException {
        Status ultimo = null;
        
        Tweet auxiliar =  (Tweet) lista.getSelectionModel().getSelectedItem();
        User rUser; //usuario al que se envia autoTag
        long userId; // id usuario
        
        
        try {
            ResponseList status = twitter.getUserTimeline();
            ultimo = (Status) status.get(0);
        } catch (TwitterException e) {
        }
        
        String mensaje_minuscula = mensaje.toLowerCase();
        String[] mensaje_desmenuzado = mensaje_minuscula.split(" ");

        for (int i = 0; i < mensaje_desmenuzado.length; i++) {
            switch (mensaje_desmenuzado[i]) {
                case "#seguir":
                    this.hayComandos = true;
                    try { 
                        String user ="";
                        try {
                            user = mensaje_desmenuzado[i + 1];
                        } catch (Exception e) {
                        }
                        if (user.charAt(0) == '@') {
                            try {
                                twitter.friendsFollowers().createFriendship(user);
                                //tags seguir usuario.
                                
                                rUser = twitter.showUser(user);
                                userId = rUser.getId();
                                twitter.directMessages().sendDirectMessage(userId, "Hola, somos los Power Ranger-1!!! Hemos comenzado a seguirte!!!");
                                
                                
                            } catch (TwitterException e) {
                                this.popUp(0, "El usuario no Existe/Ya seguido", "Error");
                            }
                            mensaje_desmenuzado[i] = "";
                            mensaje_desmenuzado[i+1] = "";
                            i+=1;
                        }
                        else{
                            this.hayComandos = false;
                        }
                    } catch (Exception e) {
                    }
                    break;

                case "#difundir":
                    this.hayComandos = true;
                    try {
                        mensaje_desmenuzado[i] = "";
                        String ID = "";
                        try {
                            ID = mensaje_desmenuzado[i + 1];
                        } catch (Exception e) {
                        }
                        System.out.println("ID: "+ID);
                        try {
                            if (esNumero(ID)) {
                                try {
                                    twitter.retweetStatus(Long.parseLong(ID));
                                } catch (NumberFormatException | TwitterException e) {
                                    this.popUp(0, "Tweet ya Retweeteado", "Error");
                                }
                                try {
                                    twitter.directMessages().sendDirectMessage(usuarioSeleccionado,"Le hemos dado retweet al tweet ID "+ID); 
                                } catch (Exception e) {
                                    this.popUp(0, "Mensaje no enviado, la otra cuenta no te sigue", "Error");
                                }
                                mensaje_desmenuzado[i+1] = "";
                                i+=1;
                            } else {
                                twitter.retweetStatus(ultimo.getId());
                                
                            }
                        } catch (TwitterException e) {
                        }
                    } catch (NumberFormatException e) {
                        twitter.retweetStatus(ultimo.getId());
                    }
                    break;
                case "#gustar":
                    this.hayComandos = true;
                    try {
                        mensaje_desmenuzado[i] = "";
                        String ID = "";
                        try {
                            ID = mensaje_desmenuzado[i + 1];
                        } catch (Exception e) {
                        }
                        try {
                            if (esNumero(ID)) {
                                try {
                                    twitter.createFavorite(Long.parseLong(ID));
                                } catch (NumberFormatException | TwitterException e) {
                                    this.popUp(0, "Tweet ya Likeado", "Error");
                                }
                                try {
                                    twitter.directMessages().sendDirectMessage(usuarioSeleccionado,"Le hemos dado ME GUSTA al tweet ID "+ID);
                                } catch (TwitterException e) {
                                    this.popUp(0, "Mensaje no enviado, la otra cuenta no te sigue", "Error");
                                }
                                mensaje_desmenuzado[i+1] = "";
                                i+=1;
                            } else {
                                twitter.createFavorite(ultimo.getId());
                            }
                        } catch (TwitterException e) {
                        }   
                    } catch (NumberFormatException e) {
                        twitter.createFavorite(ultimo.getId());
                    }
                    break;
            }
        }        
        return mensaje_desmenuzado;
    }
    
    
    /**
     * Verifica si en la cadena de texto existe un ID ingresado, retornado true 
     * en caso de corresponder a un ID o false en el caso contrario.
     * @param cadena
     * @return 
     */
    
    public boolean esNumero(String cadena) {
        boolean resultado;
        try {
            Long.parseLong(cadena);
            resultado = true;
        } catch (NumberFormatException e) {
            resultado = false;
        }
        return resultado;
    }
    
    /**
     * Método que verifica si el texto ingresado es spam o no, retornado 0 en 
     * caso de que sea spam y un 1 en el caso contario
     * @param mensaje
     * @return 0 o 1
     */
    public double noSW(String mensaje){
        String[] palabras = mensaje.split(" ");
        double total = 0;
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length()>2) {
                total+=1;
            }
        }
        double conteo = 0;
        for (int j = 0; j < palabras.length; j++) {
            if (trieSW.search(palabras[j])) {
                conteo+=1;
            }
        }
        return conteo/total;
    }
    
    public double noNSFW(String mensaje){
        String[] palabras = mensaje.split(" ");
        double total = 0;
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length()>2) {
                total+=1;
            }
        }
        double conteo = 0;
        for (int j = 0; j < palabras.length; j++) {
            if (trieNSFW.search(palabras[j])) {
                conteo+=1;
            }
        }
        return conteo/total;
    }
    
    public void verificarTexto2(){
        if (this.busqueda.getText().length()==0){
            this.check.setDisable(true);
        }
        else{
            this.check.setDisable(false);
        }
    }
    
    public void verListadoDeUsuarios(){
        try {
            this.userDM.clear();
            List<User> aux = twitter.searchUsers(this.busqueda.getText(), 1);
            for (int i = 0; i < aux.size(); i++) {
                this.userDM.add(new UserDM(new Image (aux.get(i).getMiniProfileImageURL()), aux.get(i).getScreenName()));
            }
            ObservableList<UserDM> oLista = FXCollections.observableArrayList(userDM);
            lista_de_usuarios.setItems(oLista);
            lista_de_usuarios.refresh();
        } catch (TwitterException e) {
        }
    }
    
    public void seleccionarUser(){
        UserDM auxiliar = (UserDM) lista_de_usuarios.getSelectionModel().getSelectedItem();
        usuarioSeleccionado2 = auxiliar.getUsuario();
        
        System.out.println(usuarioSeleccionado2);
    }
    
    public void refrescar() throws TwitterException{  
        this.actualizarLista();
        ResponseList<Status> statuspropios = twitter.getUserTimeline();                
        for (int i = 0; i < statuspropios.size(); i++) {         
            long[] aux = twitter.getRetweeterIds(statuspropios.get(i).getId(), 3, -1).getIDs(); 
            long[] aux2 = new long[aux.length+1];
            for (int j = 0; j < aux.length; j++) {
                aux2[j] = aux[j];
            }            
            aux2[aux.length] = statuspropios.get(i).getId();            
            this.retweetsID_Actual.add(aux2);            
        }
        if (this.retweetsID.isEmpty()) {
            for (int i = 0; i < this.retweetsID_Actual.size(); i++) {
                for (int j = 0; j < this.retweetsID_Actual.get(i).length-1; j++) {
                    twitter.directMessages().sendDirectMessage(this.retweetsID_Actual.get(i)[j], "Gracias por tu retweet en tweet https://twitter.com/" + twitter.getScreenName() + "/status/" + this.retweetsID_Actual.get(i)[this.retweetsID_Actual.get(i).length-1]);
                }                
            }    
            this.retweetsID = this.retweetsID_Actual;
        }/*else{
            for (int i = 0; i < this.retweetsID.size(); i++) {
                for (int j = 0; j < this.retweetsID.get(i).length; j++) {
                    for (int l = 0; l < retweetsID_Actual.size(); l++) {           
                        for (int k = 0; k < retweetsID_Actual.get(l).length; k++) {
                            if (this.retweetsID.get(i)[this.retweetsID.get(i).length-1]!=this.retweetsID_Actual.get(l)[this.retweetsID_Actual.get(l).length-1]) {
                                k = retweetsID_Actual.get(l).length;
                            }else{
                                if (this.retweetsID.get(i)[j]==this.retweetsID_Actual.get(l)[k]) {
                                    
                                }
                            }                                
                        }    
                    }
                }        
            }
        }
        long[] aux = twitter.getFollowersIDs(twitter.getId()).getIDs(); 
        for (int i = 0; i < aux.length; i++) {
            twitter.directMessages().sendDirectMessage(aux[i], "Gracias por ser seguidor de mi cuenta");
        }   
        */       
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
        .setOAuthConsumerKey("re7Dqm5XXdntquMGMhfPGmyvb")
        .setOAuthConsumerSecret("PClBZjCdfMYcjdFt9vMeMoh3KJc8dkNeT9urrIvov3XAqfmnnD")
        .setOAuthAccessToken("1160640022971867138-pKnQdWR1CPjRZoyAd7BwOBWIgWt56u")
        .setOAuthAccessTokenSecret("nW7iSxt9oHdyzukWKwQGEfLHIcZQFr8V0rZvqZl28DdFG")                
        .setIncludeEmailEnabled(true);        
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<String> keys = new ArrayList<>();
        
        Scanner input = null;
        try {
            input = new Scanner(new File("nsfw.txt"));
        } catch (FileNotFoundException e) {
        }
        while (input.hasNextLine()){
            String linea = input.nextLine();
            trieNSFW.insert(linea);
        }
        
        try {
            input = new Scanner(new File("stopwords.txt"));
        } catch (FileNotFoundException e) {
        }
        while (input.hasNextLine()) {
            String linea = input.nextLine();
            trieSW.insert(linea);
        }
        
        /*
        // ****** ESTO GENERA EL WEBVIEW ******
        WebEngine engine = vista.getEngine();
        //engine.setUserAgent("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");       
        engine.load("https://twitter.com/power_java");        
        vista.setContextMenuEnabled(false);      
        vista.setZoom(0.50);        
        // ************ HASTA ACA *********/
        msj.setWrapText(true);         
        try {
               
            listaTweets = twitter.getHomeTimeline(); 
            for (int i = 0; i < listaTweets.size(); i++) {
                
                this.listaTimeline.add(new Tweet(listaTweets.get(i),new Image (listaTweets.get(i).getUser().getMiniProfileImageURL())));                
            }
            
            ObservableList<Tweet> oLista = FXCollections.observableArrayList(listaTimeline);
            lista.setItems(oLista);
            
            User newUser = twitter.showUser(twitter.getScreenName());
            
            lista.setCellFactory(param -> new ListCell<Tweet>() {
            private ImageView imageView = new ImageView(new Image (newUser.getMiniProfileImageURL()));
            @Override
            public void updateItem(Tweet name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {                      
                    imageView.setImage(name.getImagenPerfil());    
                    setGraphic(imageView);                      
                    String indicadores = "       LoRetweetie: "+paraSaber(name.getTweet().isRetweetedByMe())+"     DiLike: "+paraSaber(name.getTweet().isFavorited());
                    setText(" @"+name.getUsuario()+" :  "+name.getTexto() + "\n"+indicadores);                                
                }
            }
            });
            
            usuario.setText("@"+newUser.getScreenName());
            usuario2.setText(newUser.getName());
            imagenPerfil.setImage(new Image(newUser.get400x400ProfileImageURL()));
            imagenPerfil2.setImage(new Image(newUser.get400x400ProfileImageURL()));                 
            System.out.println(newUser.getEmail());
            correo.setText(newUser.getEmail());            
        } catch (TwitterException | IllegalStateException ex) {
            this.popUp(0, "El proceso no puede establecer conexión con twitter", "Error");
        }
        try {
                
                User newUser = twitter.showUser(twitter.getScreenName());

                lista_de_usuarios.setCellFactory(param -> new ListCell<UserDM>(){
                private ImageView iv = new ImageView(new Image (newUser.getMiniProfileImageURL()));
                @Override
                public void updateItem(UserDM name, boolean empty){
                    super.updateItem(name, empty);
                    if (empty) {                    
                        setText(null);
                        setGraphic(null);
                    } else {                    
                        iv.setImage(name.getImagendeperfil());    
                        setGraphic(iv);
                        setText(" "+name.getUsuario());
                    }
                }
                });
            } catch (IllegalStateException | TwitterException e) {
            }
        
        this.arrastrarVentana(this.ap);           
        this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));        
    }  
}