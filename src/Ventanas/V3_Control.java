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
import Clases.Trie;
import Clases.Tweet;
import Clases.UserDM;
import static Ventanas.V2_Controller.twitter;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import twitter4j.DirectMessageList;
import twitter4j.IDs;
import twitter4j.TwitterException;
import twitter4j.User;


/**
 * FXML Controller class
 *
 * @author Fernando Pino, Carmen Ortega
 *         Vicente Rojas.
 */
public class V3_Control extends ControlVentana implements Initializable {
    
    @FXML AnchorPane ap;
    @FXML TextArea mensajeDirecto;
    @FXML TextField usuario;
    @FXML TextField posibleUser;
    @FXML ListView chat;
    @FXML ListView todosLosUsers;
    @FXML ImageView perfilMini;
    @FXML ImageView perfilBot;
    @FXML Text nombreFerfil;
    @FXML Text nombreUsuario;
    @FXML ListView seguidos;
    @FXML Button check;
    @FXML Button check2;
    @FXML Button enviar;
    @FXML Button refresh;
    private long idMensaje;
    private long idPropio;
    private final char arroa = 64;
    private ArrayList<Long> usuarios = new ArrayList<>();
    private ObservableList<String> items = FXCollections.observableArrayList();
    private ObservableList<Text> mensajes = FXCollections.observableArrayList();
    private ArrayList<String> textoMensajes = new ArrayList<>();
    private ArrayList<UserDM> userDM = new ArrayList<>();
    private Trie trieNSFW = new Trie();
    private Trie trieSW = new Trie();
    
    public int enviarMensaje() throws TwitterException{        
        String aUsuario = usuario.getText().toString();
        User user;
        Text t = new Text(this.mensajeDirecto.getText());
        double porcentaje = noSW(t.getText());
        double porcentaje2 = noNSFW(t.getText());
        if (porcentaje>=0.70) {
            this.popUp(1, "¡ES SPAM! ¡MENSAJE NO ENVIADO!", "ERROR");
            this.mensajeDirecto.clear();
            return 0;
        }
        if (porcentaje2>=0.70) {
            this.popUp(1, "¡ORDINARIO! ¡MENSAJE NO ENVIADO!", "ERROR");
            this.mensajeDirecto.clear();
            return 0;
        }
        
        try {
            user = V2_Controller.twitter.showUser(aUsuario);
            long userId = user.getId();
            V2_Controller.twitter.directMessages().sendDirectMessage(userId, mensajeDirecto.getText());
            this.popUp(1,"Mensaje Enviado", "Logrado");
            
            t.setFont(Font.font("Helvetica", 15));
            t.setTextAlignment(TextAlignment.RIGHT);
            t.setWrappingWidth(337.0);
            this.chat.getItems().add(t);
            this.mensajeDirecto.setText("");
            
        } catch (TwitterException ex) {
            this.popUp(0, "Usuario no seleccinado", "Error");
        }
        return 1;
    }
    
    public void cerrarMensaje(){        
        this.ap.setEffect(null);
        this.ap.setDisable(false);
        ((Stage) ap.getScene().getWindow()).close();               
    }
    
    public void verUsuario() throws TwitterException{
        if(this.mensajeDirecto.getText().length()==0){
            this.enviar.setDisable(true);
        }
        try {
            User u = V2_Controller.twitter.showUser(this.usuario.getText());
            Image foto = new Image(u.get400x400ProfileImageURL());
            this.perfilMini.setImage(new Image(u.get400x400ProfileImageURL()));
            this.nombreFerfil.setText("@"+u.getScreenName());
            this.nombreUsuario.setText(u.getName());
            Text t;
            //><
            this.chat.getItems().clear();
            DirectMessageList m = V2_Controller.twitter.getDirectMessages(50);
            
            this.textoMensajes.clear();
            for (int i = 0; i < m.size(); i++) {
                textoMensajes.add(m.get(i).getText());
            }
            
            for (int i = m.size()-1; i >= 0; i--) {
                if (m.get(i).getRecipientId()==u.getId() && m.get(i).getSenderId()==this.idPropio) {                    
                    t = new Text(m.get(i).getText());
                    t.setFont(Font.font("Helvetica", 15));
                    t.setTextAlignment(TextAlignment.RIGHT);
                    t.setWrappingWidth(337.0);
                    this.chat.getItems().add(t);
                }
                else{
                    if (m.get(i).getSenderId()==u.getId() && m.get(i).getRecipientId()==this.idPropio) {
                        t = new Text(m.get(i).getText());
                        t.setFont(Font.font("Helvetica", 15));
                        t.setTextAlignment(TextAlignment.LEFT);
                        t.setWrappingWidth(337.0);
                        this.chat.getItems().add(t);
                    }
                }
            }
            this.check.setDisable(true);
            this.enviar.setDisable(false);
        } catch (TwitterException e) {
        }
    }

    public void volverCheck(){
        if (this.usuario.getText().length()==0){
            this.check.setDisable(true);
        }
        else{
            this.check.setDisable(false);
        }
    }
    
    public void verificarTexto(){
        if (this.mensajeDirecto.getText().length()==0){
            this.enviar.setDisable(true);
        }
        else{
            this.enviar.setDisable(false);
        }
    }
    
    public void verificarTexto2(){
        if (this.posibleUser.getText().length()==0){
            this.check2.setDisable(true);
        }
        else{
            this.check2.setDisable(false);
        }
    }
    
    public void ponerUsuario(){
        String auxiliar = seguidos.getSelectionModel().getSelectedItem().toString();
        this.usuario.setText(auxiliar);
        this.check.setDisable(false);
    }
    
    public void ponerUsuario2(){
        UserDM udm = (UserDM) todosLosUsers.getSelectionModel().getSelectedItem();
        this.usuario.setText(udm.getUsuario());
        this.check.setDisable(false);
    }
    
    public void verListadoDeUsuarios(){
        try {
            this.userDM.clear();
            List<User> aux = twitter.searchUsers(this.posibleUser.getText(), 1);
            for (int i = 0; i < aux.size(); i++) {
                this.userDM.add(new UserDM(new Image (aux.get(i).getMiniProfileImageURL()), aux.get(i).getScreenName()));
            }
            ObservableList<UserDM> oLista = FXCollections.observableArrayList(userDM);
            todosLosUsers.setItems(oLista);
            todosLosUsers.refresh();
        } catch (TwitterException e) {
        }
    }
    
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
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.enviar.setDisable(true);
        this.check.setDisable(true);
        this.usuario.setEditable(false);
        this.check2.setDisable(true);
        try {
            idPropio = V2_Controller.twitter.getId();
            User auxUser = V2_Controller.twitter.showUser(idPropio);
            this.perfilBot = new ImageView(new Image (auxUser.get400x400ProfileImageURL()));
            IDs ids = V2_Controller.twitter.getFriendsIDs(V2_Controller.twitter.getScreenName(), -1);
            for (long id : ids.getIDs()) {
                User u = V2_Controller.twitter.showUser(id);
                items.add(u.getScreenName());
            }
            TextFields.bindAutoCompletion(usuario, items);
            
            this.seguidos.setItems(items);
            this.seguidos.setCellFactory(param -> new ListCell<String>() {
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {       
                    setGraphic(null);
                    setText(" @"+name+"\n"+" ");                                
                }
            }
            });    
            
        } catch (TwitterException | IllegalStateException ex) {
            Logger.getLogger(V3_Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            User newUser = twitter.showUser(twitter.getScreenName());
            
            todosLosUsers.setCellFactory(param -> new ListCell<UserDM>(){
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
        } catch (Exception e) {
        }
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
    }
}