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
import Clases.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.TwitterException;

/**
 *
 * Clase xxx: - Clase que representa blablablabla.
 * @author Fernando Pino,  Carmen Ortega,
 *          Vicente Rojas, Kevin Douglas.
 * @version 1.0
 */
public class TwitterBot extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {       
        Parent root = FXMLLoader.load(getClass().getResource("/Ventanas/V1.fxml"));       
        Scene scene = new Scene(root);        
        scene.setFill(Color.TRANSPARENT);        
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Imagenes/icon.png")));         
        stage.initStyle(StageStyle.TRANSPARENT);
        //stage.setOpacity(0.83);
        stage.setResizable(false);
        stage.centerOnScreen();  
        stage.show();             
    }

    /**
     * @param args the command line arguments
     * @throws twitter4j.TwitterException
     */
    public static void main(String[] args) throws TwitterException {           
    /*  ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("FI7zwXVxcqKBAozpTrqxJNfIA")
        .setOAuthConsumerSecret("bNjRO185DPOXnFqMOXJ2arJOQF1EG3TLFKXQPGCoCrM7649Lpe")
        .setOAuthAccessToken("1160640022971867138-WMcI6McTZAjpQ81949F6pr8NhfyO5a")
        .setOAuthAccessTokenSecret("2YtfMfSlpMUO3QlS3adHSw9AJmTqJMXiZC9D4BEcyhP7Q");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();   */        
        launch(args);
    }
    
}
