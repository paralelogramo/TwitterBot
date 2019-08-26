/*
 * Copyright (C) 2019 TwitterProyectCorp.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Ventanas;

import Clases.ControlVentana;
import Clases.Mensaje;
import java.io.File;
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
    @FXML Text user;
    @FXML Text user2;
    @FXML Text email;
    @FXML Text notificationImage;
    @FXML ProgressIndicator pgA;
    @FXML ImageView profilePhoto;
    @FXML ImageView profilePhoto2;
    @FXML WebView view;
    @FXML ListView listaTiempo;
    private Image profilePhotoImage;
    private File imgFile;
    private Stage stage = this.stage;
    private Twitter twitter;
    private List<Status> lineaDeTiempo;
    
    
    public void progresoTexto(KeyEvent event){      
        if(msj.getText().length()>20)
            msj.setEditable(true);
        pgA.setProgress(msj.getText().length()/280.0);
    }
    
    // METODO LISTO
    public int sendTwitter(MouseEvent event) throws TwitterException{       
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje(msj.getText());              
        StatusUpdate status = new StatusUpdate(mensaje.getMensaje());
        if (imgFile != null) {
            status.setMedia(imgFile);
            twitter.updateStatus(status);
            notificationImage.setVisible(false);
            return 0;
        }
        if(mensaje.verificar() && imgFile == null){
            twitter.updateStatus(status);
            msj.clear();
            notificationImage.setVisible(false);
        }
        return 0;
    }
    
    // METODO LISTO
    public void upImage_Twitter(MouseEvent event) throws TwitterException{
        FileChooser filech = new FileChooser();
        filech.setTitle("Buscar Imagen");
        filech.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4")
        );
        imgFile = filech.showOpenDialog(stage);
        if (imgFile != null) {
            notificationImage.setVisible(true);
        }
    }
    
    // METODO LISTO
    public void upGif_Twitter(MouseEvent event) throws TwitterException{
        FileChooser filech = new FileChooser();
        filech.setTitle("Buscar Gif");
        filech.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );
        imgFile = filech.showOpenDialog(stage);
        if (imgFile != null) {
            notificationImage.setVisible(true);
        }
    }
    
    // METODO LISTO
    public void deleteTwitter(MouseEvent event) throws TwitterException{
        System.out.println("Entro");
        ResponseList<Status> Line = twitter.getHomeTimeline();
        Line.forEach((status) -> {
            System.out.println("ID: " + status.getId() + " - " + status.getText());
        });
        System.out.print("Id del status a eliminar: ");
        Scanner sc = new Scanner(System.in);
        
        long id = sc.nextLong();
        
        for (Status status : Line) {
            if (id==status.getId()) {
                twitter.destroyStatus(id);
                System.out.println("status eliminado");
            }
        }
    }
    
    
    public void followUser(MouseEvent event) throws TwitterException{
        
    }
    
    
    public void unfollowUser(MouseEvent event) throws TwitterException{
        
    }
    
    
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
        WebEngine engine = view.getEngine();
        engine.setUserAgent("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");       
        engine.load("https://twitter.com/power_java");
        view.setContextMenuEnabled(false);      
        view.setZoom(0.80);
        // ************ HASTA ACA *********
        msj.setWrapText(true);
        try {
            lineaDeTiempo = twitter.getUserTimeline();            
            User newUser = twitter.showUser(twitter.getScreenName());            
            user.setText("@"+newUser.getScreenName());
            user2.setText(newUser.getName());
            profilePhoto.setImage(new Image(newUser.get400x400ProfileImageURL()));
            profilePhoto2.setImage(new Image(newUser.get400x400ProfileImageURL()));
            email.setText(newUser.getEmail());
            
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
}