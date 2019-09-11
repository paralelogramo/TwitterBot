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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import twitter4j.Friendship;
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
    @FXML ComboBox buscar;
    @FXML TextArea mensajeDirecto;
    private final char arroa = 64;
    private ArrayList<Long> usuarios = new ArrayList<>();
    private ObservableList<String> items = FXCollections.observableArrayList();
    
    public void enviarMensaje(MouseEvent event) throws TwitterException{        
        String aUsuario = buscar.getValue().toString();
        User user;        
        
        try {
            user = V2_Controller.twitter.showUser(aUsuario);
            long userId = user.getId();
            System.out.println(userId);
            V2_Controller.twitter.directMessages().sendDirectMessage(userId, mensajeDirecto.getText());    
        } catch (TwitterException ex) {
            this.popUp(0, aUsuario+" no existe", "Error");
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
            buscar.setItems(items);
            IDs ids = V2_Controller.twitter.getFriendsIDs(V2_Controller.twitter.getScreenName(), -1);
            for (long id : ids.getIDs()) {
                User u = V2_Controller.twitter.showUser(id);
                items.add(u.getScreenName());
            }
        } catch (TwitterException ex) {
            Logger.getLogger(V3_Control.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(V3_Control.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
