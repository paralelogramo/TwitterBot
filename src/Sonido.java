import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/*
 * Copyright (C) 2019 Vicente
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

/**
 *
 * @author Vicente
 */
public class Sonido {
    public static Clip obtenerSonido(String file){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sounds" + System.getProperty("file.separator") + file).getAbsoluteFile());
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip sound = (Clip)AudioSystem.getLine(info);
            sound.open(audioInputStream);
            return sound;
        }
        catch (Exception e){
            return null;
        }
    }
    
    public static void ejecutarSonido (Clip clip){
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
