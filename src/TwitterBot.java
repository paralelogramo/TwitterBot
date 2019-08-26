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
