package Pieza;

import ajedrezpro2.PanelAjedrez;
import ajedrezpro2.Tipo;


public class Torre extends Pieza {
    
    public Torre(int color, int col, int fil) {
        super(color, col, fil);
        
        tipo = Tipo.TORRE;
        
        if(color == PanelAjedrez.blanco) {
            imagen = getImagen("/piezas/torre");
        }
        else {
            imagen = getImagen("/piezas/torreotra");
            
            
        }
    }
    
    public boolean puedeMover(int targetCol, int targetFil) {
        if(estaEnElTablero(targetCol, targetFil) && esMismoCuadro(targetCol, targetFil) == false){
            //Se mueve horizontal y verticalmente lo que desee
            if(targetCol == preCol || targetFil == preFil) {
                if(espacioVacio(targetCol, targetFil) && piezaEnMismaLinea(targetCol, targetFil) == false){
                    return true;
                }
            }
        }
        return false;
    }
}
    

