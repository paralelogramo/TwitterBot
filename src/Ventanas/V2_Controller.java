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
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import twitter4j.Paging;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.ListCell;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import twitter4j.UploadedMedia;

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
    @FXML ListView listaTiempo;
    @FXML ImageView preImage;
    @FXML Text avisolimite;
    @FXML ImageView equis;
    @FXML ListView<String> listView = new ListView<String>();
    @FXML MediaView mv;
    private MediaPlayer mp;
    private Media med;
    private Image profilePhotoImage;
    private File imgFile;
    private Stage stage = this.stage;
    protected static Twitter twitter;
    private Twitter sender;
    private List<Status> lineaDeTiempo;   
    private final char arroa = 64;
    private final ArrayList<String> usuariosSeguidos = new ArrayList<>();    
    private List<Status> listaTweets;    
    private ArrayList<String> listaTimeline = new ArrayList<>();
    private long seleccionTweet;
    private boolean esVideo = false;
    
    Scanner sc = new Scanner(System.in);    
    
    
    public void seleccionTweet(MouseEvent event){
        Status auxiliar = (Status) listaTiempo.getSelectionModel().getSelectedItem();
        seleccionTweet = auxiliar.getId();
        System.out.println(seleccionTweet);
    }
    
    
    
    
    public void progresoTexto(Event event){   
        System.out.println(msj.getAnchor());
        pgA.setProgress(msj.getText().length()/280.0);  
        pgA.progressProperty().addListener((ov, oldValue, newValue) -> {
            Text text = (Text) pgA.lookup(" .percentage");
            if(text!=null){
               text.setText(Integer.toString(280-msj.getText().length()));               
               // pgA.setPrefWidth(text.getLayoutBounds().getWidth());
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
            //pgA.setProgress((msj.getText().length()/280.0)-1.0);            
            pgA.setStyle("-fx-progress-color: crimson;");
            if(msj.getText().length()>280){
                avisolimite.setVisible(true);
                pgA.setProgress(0.999999);
            }
            //if(pgA.getProgress()>0.99)            
            
        }
    }
    
    public void minimizarVentana(MouseEvent event){    
        ((Stage) ap.getScene().getWindow()).setIconified(true);        
    }    
    
    // METODO LISTO
    public int enviarTwitter(MouseEvent event){       
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje(msj.getText());
        long[] mediaIds = new long[1];
        StatusUpdate status = new StatusUpdate(mensaje.getMensaje());
        if (imgFile != null) {
            try {
                if (!esVideo) {
                    status.setMedia(imgFile);
                }
                else{
                    InputStream target = new FileInputStream(imgFile);
                    UploadedMedia um = twitter.uploadMediaChunked("Video", target);
                    mediaIds[0] = um.getMediaId();
                    status.setMediaIds(mediaIds);
                }
                twitter.updateStatus(status);
                msj.clear();
                notificacionImagen.setVisible(false);
                this.pgA.setProgress(0);
                this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));
                this.equis.setImage(null);
                this.esVideo = false;
                return 0;
            } catch (TwitterException ex) {
                System.out.println("Error, No se puede enviar el twit");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(V2_Controller.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("se cayo porque no hay archivo :v");
            }
        }
        if(mensaje.verificar() && imgFile == null){
            try {
                twitter.updateStatus(status);
                msj.clear();
                notificacionImagen.setVisible(false);
                this.pgA.setProgress(0);
                this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));
                this.equis.setImage(null);
                
                return 0;
            } catch (TwitterException ex) {
                System.out.println("No se puede subir la imagen");
            }
        }else{            
            Toolkit.getDefaultToolkit().beep();            
            JOptionPane auxiliar = new JOptionPane();
            auxiliar.setMessage("Texto de Tweet demasiado largo");
            auxiliar.setMessageType(0);
            JDialog dialog = auxiliar.createDialog("TwitterBot_ | Error");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);   
        }
        return 0;        
    }
    
    // METODO LISTO
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
                    imgFile = null;
                    equis = null;
                    notificacionImagen.setText("Tamaño Archivo No Soportado!!");
                    this.preImage.setImage(null);                    
                }
                else{
                    try {
                        Image imagen123 = new Image(new FileInputStream(imgFile));
                        this.preImage.setImage(imagen123);
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
                        String dir = imgFile.getAbsolutePath();
                        med = new Media (new File (dir).toURI().toString());
                        mp = new MediaPlayer(med);
                        mv.setMediaPlayer(mp);
                        this.esVideo = true;                        
                    }
                }
        }
        if(this.preImage.getImage() == null)
            this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));   
    }
    
    public void pausar_Video (MouseEvent event){
        this.mp.pause();
    }
    
    public void continuar_Video (MouseEvent event){
        this.mp.play();
    }
    
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
        this.notificacionImagen.setText(("Archivo eliminado con exito!"));
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
    public void eliminarTwitter(MouseEvent event){
       
        ArrayList<Long> mensajeId = new ArrayList();
        ArrayList<String> textoMsj = new ArrayList();        
        
        int opcion=-1;
        String opcion2;
        int largoArreglo;
    
        try {
            ResponseList<Status> Line = twitter.getHomeTimeline();
                
            Line.forEach((status) -> {
                mensajeId.add(status.getId());
                textoMsj.add(status.getText());
            });
            
            largoArreglo = mensajeId.size();
            
            if(largoArreglo>0){
                for (int i = 0; i < mensajeId.size(); i++) {
                    System.out.println(i+". "+textoMsj.get(i));
                }
                
                do{        
                    System.out.print("Numero del twitt a eliminar: "); 
                    try{
                        opcion2 = sc.nextLine();
                        if(opcion2.equalsIgnoreCase("")){
                            opcion =-1;
                        }
                        else{
                            opcion = Integer.parseInt(opcion2);
                        }
                        if(opcion>=0 && opcion<mensajeId.size()){
                            twitter.destroyStatus(mensajeId.get(opcion));
                            mensajeId.remove(opcion);
                            textoMsj.remove(opcion);
                            System.out.println("Mensaje eliminado!!!");
                            
                        }
                    }catch(NumberFormatException | TwitterException e){
                        System.out.println("Opcion Invalida!!!!!!!!");
                    }
                    
                }while(opcion<0 || opcion>mensajeId.size());
                
            }
            else{
                System.out.println("No existen tweets publicados!!!");
            }
        } catch (TwitterException ex) {
            System.out.println("Error, No se puede eliminar el twitt");
        }
    }
    
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
            
            largoArreglo = mensajeId.size();
            
            //muestra lista de tweets existentes en la cuenta
            if(largoArreglo>0){
                for (int i = 0; i < mensajeId.size(); i++) {
                    System.out.println(i+". "+textoMsj.get(i));
                }
                
                do{
                    System.out.print("Numero del twitt a retweetear: ");
                    try{
                        opcion2 = sc.nextLine();
                        if(opcion2.equalsIgnoreCase("")){
                            opcion =-1;
                        }
                        else{
                            opcion = Integer.parseInt(opcion2);
                        }
                        if(opcion>=0 && opcion<mensajeId.size()){
                            twitter.retweetStatus(mensajeId.get(opcion));
                            System.out.println("Mensaje Retwitteado");
                            
                        }
                    }catch(NumberFormatException | TwitterException e){
                        System.out.println("Opcion Invalida!!!");
                    }
                    
                }while(opcion<0 || opcion>mensajeId.size());
            }
            else{
                System.out.println("No existen tweets publicados!!!");
            }
        } catch (TwitterException ex) {
            System.out.println("Error, no se puede retwittear el mensaje");
        }
    }
    
    /* Obsoleto eliminarTwitter
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
         long id = sc.nextLong();
        
        for (Status status : Line) {
            if (id==status.getId()) {
                twitter.destroyStatus(id);
                System.out.println("Twitt "+id+" eliminado");
                vista.getEngine().reload();
            }
        }       
    }*/ 
    
    
    public void seguirUsuario(MouseEvent event){         
        try{
            String seguirUsuario =  JOptionPane.showInputDialog("Ingrese un usuario a seguir","@Usuario");     
            try {
                if (seguirUsuario!=null&&!seguirUsuario.equals("") && !seguirUsuario.equals("@Usuario"))
                    twitter.friendsFollowers().createFriendship(seguirUsuario.substring(1));                      
                System.out.println("Seguir usuarios: "+seguirUsuario);
            } catch (TwitterException ex) {
                this.popUp(0, "Usuario ingresado no existe", "Error");           
            } 
        } catch (NullPointerException e) {
                      
        } 
    }   
    
    public void noSeguirUsuario(MouseEvent event){
        User u1 = null ;
        long indicador = -1;
        try{
            IDs ids;  
            do {    
                ids = twitter.getFriendsIDs(twitter.getScreenName(), indicador);
                for (long id : ids.getIDs()) {
                    System.out.println(indicador);
                    User user = twitter.showUser(id);
                    System.out.println(user.getScreenName());
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
    
    public void darLike(){
        /*PASOS:
        1.- PEDIR EL @USUARIO
        2.- BUSCAR EL USUARIO
        3.- MOSTRAR UNA LISTA CON LOS POST DEL USUARIO
        4.- QUE EL USUARIO ELIJA CUAL DAR LIKE
        */
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingresar @usuario");
        
        try{
            String buscarUsuario =  JOptionPane.showInputDialog("Ingrese un usuario a buscar","@Usuario");     
            try {                
                List tweets = twitter.getUserTimeline(buscarUsuario);
                for (int i = 0; i < tweets.size(); i++) {
                    Status status = (Status) tweets.get(i);
                    System.out.println("ID: "+status.getId());
                    System.out.println("Texto: "+status.getText());
                    System.out.println("-----------------");
                }
                System.out.print("ID a dar like: ");
                long id = sc.nextLong();
                try {
                    for (int j = 0; j < tweets.size(); j++) {
                        Status status = (Status) tweets.get(j);
                        if (id == status.getId()) {
                            twitter.createFavorite(id);
                        }
                    }
                } catch (TwitterException e) {
                }
            } catch (TwitterException e) {
                this.popUp(0, "Usuario ingresado no existe", "Error");           
            } 
        } catch (NullPointerException e) {
                      
        } 
        
    }
    
    //vista protegida?
    public void costumeImageView(){
        
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
        //stage.setOpacity(0.95);        
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
            ventana.initStyle(StageStyle.TRANSPARENT);               
            ventana.show();
        } catch (IOException ex) {
            this.popUp(0, "El proceso no puede cargar la ventana (archivo: V3.fxml)", "Error");
        }
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
                String aux = arroa+listaTweets.get(i).getUser().getScreenName() + ": " +listaTweets.get(i).getText(); 
                System.out.println(aux);
                this.listaTimeline.add(aux);
            }
            
            ObservableList<String> oLista = FXCollections.observableArrayList(listaTimeline);
            listaTiempo.setItems(oLista);
            
            User newUser = twitter.showUser(twitter.getScreenName());
            
            listaTiempo.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView(new Image(newUser.getMiniProfileImageURL()));
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {                   
                    String [] aux1 = name.split(":");
                    String usuario = aux1[0];
                    User aux2;
                    try {
                        aux2 = twitter.showUser(usuario);
                        imageView.setImage(new Image(aux2.getMiniProfileImageURL()));
                    } catch (TwitterException ex) {
                        Logger.getLogger(V2_Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                    setText(name);
                    setGraphic(imageView);
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
            Logger.getLogger(V2_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.arrastrarVentana(this.ap);           
        this.preImage.setImage(new Image(getClass().getResourceAsStream("/Imagenes/default.png")));
    }  
}