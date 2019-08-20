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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import Clases.ControlVentana;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    //Shape forma = new RoundRectangle2D.Double(0, 0, ap.getBoundsInLocal().getWidth(), ap.getBoundsInLocal().getHeight(), 30, 30); 
    
    public void minimizarVentana(MouseEvent event){       
        //ap.setVisible(false);
        // url('/Imagenes/background1.png')
    }    
    
    @FXML
    public void inicioSesion(Event evento) throws IOException{        
        iniciarSesion();        
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
        AnchorPane juegoAp = loader.load();        
        Scene scene = new Scene(juegoAp);     
        stage.setOpacity(0.95);
        stage.setScene(scene);
        stage.initOwner(this.ap.getScene().getWindow());            
        stage.setResizable(false);
        ((Stage)this.ap.getScene().getWindow()).close();     
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        stage.show();
        
    }
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         this.arrastrarVentana(this.ap);
    }    
}