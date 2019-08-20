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
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    
    @FXML TextArea msj;
    @FXML Text user;
    @FXML Text email;
    @FXML Text notificationImage;
    @FXML ProgressIndicator pgA;
    @FXML ImageView profilePhoto;
    private File imgFile;
    private Stage stage = this.stage;
    private Twitter twitter;
    
    public void progresoTexto(KeyEvent event){      
        if(msj.getText().length()>20)
            msj.setEditable(true);
        pgA.setProgress(msj.getText().length()/280.0);
    }
    
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
    
    public void upImage_Twitter(MouseEvent event) throws TwitterException{
        FileChooser filech = new FileChooser();
        filech.setTitle("Buscar Imagen");
        filech.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        imgFile = filech.showOpenDialog(stage);
        if (imgFile != null) {
            notificationImage.setVisible(true);
        }
    }
    
    public void deleteTwitter(MouseEvent event) throws TwitterException{
        System.out.println("Entro");
        ResponseList<Status> Line = twitter.getHomeTimeline();
        for (Status status : Line) {
            System.out.println("ID: " + status.getId() + " - " + status.getText());
        }
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
        
        try {
            User newUser = twitter.showUser(twitter.getScreenName());            
            user.setText("@"+newUser.getName());
            /*
            email.setText(newUser.getEmail());
            String urlIm = newUser.getProfileImageURL();
            Image img = new Image(urlIm);
            profilePhoto.setImage(img);

            PENDIENTES POR PROBLEMAS CON PRIVACY POLICY
            */
        } catch (TwitterException | IllegalStateException ex) {
            Logger.getLogger(V2_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }        
}