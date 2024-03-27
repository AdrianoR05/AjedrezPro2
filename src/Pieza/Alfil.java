package Pieza;

import ajedrezpro2.PanelAjedrez;
import ajedrezpro2.Tipo;

public class Alfil extends Pieza {
    
    public Alfil(int color, int col, int fil) {
        super(color, col, fil);
        
        tipo = Tipo.ALFIL;
        
         if(color == PanelAjedrez.blanco) {
            imagen = getImagen("/piezas/alfil");
        }
        else {
            imagen = getImagen("/piezas/alfilotro");
            
        }
    }
    
    public boolean puedeMover (int targetCol, int targetFil){
        if(estaEnElTablero(targetCol, targetFil) && esMismoCuadro(targetCol, targetFil) == false){
            //Movimiento diagonal
            if(Math.abs(targetCol - preCol) == Math.abs(targetFil - preFil)){
                if(espacioVacio(targetCol, targetFil) && piezaEnMismaDiagonal(targetCol, targetFil) == false) {
                    return true;
                }
            }
        }
        return false;
    }
}
    

