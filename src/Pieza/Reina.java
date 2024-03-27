package Pieza;

import ajedrezpro2.PanelAjedrez;
import ajedrezpro2.Tipo;

public class Reina extends Pieza {
    
    public Reina(int color, int col, int fil) {
        super(color, col, fil);
        
        tipo = Tipo.REINA;
        
         if(color == PanelAjedrez.blanco) {
            imagen = getImagen("/piezas/reina");
        }
        else {
            imagen = getImagen("/piezas/reinaotra");
            
        }
    }
    
    public boolean puedeMover (int targetCol, int targetFil){
        if(estaEnElTablero(targetCol, targetFil) && esMismoCuadro(targetCol, targetFil) == false){
            //Vertical y Horizontal
            if(targetCol == preCol || targetFil == preFil) {
                if(espacioVacio(targetCol, targetFil) && piezaEnMismaLinea(targetCol, targetFil) == false){
                    return true;
                }
            }
            //Diagonal
            if(Math.abs(targetCol - preCol) == Math.abs(targetFil - preFil)){
                if(espacioVacio(targetCol, targetFil) && piezaEnMismaDiagonal(targetCol, targetFil) == false){
                    return true;
                }
            }
        }
        return false;
    }
    
}

    
