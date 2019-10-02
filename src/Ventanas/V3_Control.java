/*
 * Copyright (C) 2019 F. Pino
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import twitter4j.DirectMessageList;
import twitter4j.IDs;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * FXML Controller class
 *
 * @author F. Pino
 */
public class V3_Control extends ControlVentana implements Initializable {
    
    @FXML AnchorPane ap;
    @FXML TextArea mensajeDirecto;
    @FXML TextField usuario;
    @FXML TextFlow chat;
    @FXML ImageView perfil;
    private final char arroa = 64;
    private ArrayList<Long> usuarios = new ArrayList<>();
    private ObservableList<String> items = FXCollections.observableArrayList();
    private List mensajes;
    
    public void enviarMensaje(MouseEvent event) throws TwitterException{        
        String aUsuario = usuario.getText().toString();
        User user;
        
        
        try {
            user = V2_Controller.twitter.showUser(aUsuario);
            long userId = user.getId();
            V2_Controller.twitter.directMessages().sendDirectMessage(userId, mensajeDirecto.getText());
            this.usuario.setText("");
            this.mensajeDirecto.setText("");
            this.perfil.setImage(null);
            this.popUp(1,"Mensaje Enviado", "Logrado");
            
        } catch (TwitterException ex) {
            if (this.mensajeDirecto.getText()==null) {
                this.popUp(0, "Texto Vacio", "Error");
            }
            else{
                this.popUp(0, "Deben ser BFF y hacer F4F uwu", "Error");
            }
        }
    }
    
    public void cerrarMensaje(){
        ((Stage) ap.getScene().getWindow()).close();
    }
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            IDs ids = V2_Controller.twitter.getFriendsIDs(V2_Controller.twitter.getScreenName(), -1);
            for (long id : ids.getIDs()) {
                User u = V2_Controller.twitter.showUser(id);
                items.add(u.getScreenName());
            }
            TextFields.bindAutoCompletion(usuario, items);
        } catch (TwitterException ex) {
            Logger.getLogger(V3_Control.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(V3_Control.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void verUsuario() throws TwitterException{
        try {
            User u = V2_Controller.twitter.showUser(this.usuario.getText());
            Image foto = new Image(u.get400x400ProfileImageURL());
            this.perfil.setImage(foto);
            
            /*this.chat.getChildren().clear();
            DirectMessageList mensajes = V2_Controller.twitter.getDirectMessages(50);
            for (int i = 0; i < 10; i++) {
                this.chat.getChildren().add(new Text(mensajes.get(i).getText()+"\n"));
            }*/
        } catch (Exception e) {
            this.perfil.setImage(null);
        }
    }
}
