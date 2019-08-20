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
package Clases;

/**
 *
 * Clase xxx: - Clase que representa blablablabla.
 * @author Fernando Pino,  Carmen Ortega,
 *          Vicente Rojas, Kevin Douglas.
 * @version 1.0
 */
public class Mensaje {
   
    private String texto; //texto a twitear        
    
    public Mensaje(){  
       
    }   
    
    public void setMensaje(String mensaje){        
        texto = mensaje;
    }
    
    public String getMensaje(){
        return texto;
    }
    
    public void twittear(){
       
    }
    
    public boolean verificar(){
        int largo = texto.length();//largo del texto        
        if (largo>280){
            System.out.println("Demasiado Largo !!!!!");            
        }else if (largo==0){
            System.out.println("Ingresa texto!!!!!!!");            
        }               
        return largo<280&&largo>0;
    }
    
}

