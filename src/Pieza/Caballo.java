package Pieza;

import ajedrezpro2.PanelAjedrez;
import ajedrezpro2.Tipo;

public class Caballo extends Pieza {
    
    public Caballo(int color, int col, int fil) {
        super(color, col, fil);
        
        tipo = Tipo.CABALLO;
        
         if(color == PanelAjedrez.blanco) {
            imagen = getImagen("/piezas/caballo");
        }
        else {
            imagen = getImagen("/piezas/caballootro");
            
        }
    }
    
    public boolean puedeMover(int targetCol, int targetFil){
        if(estaEnElTablero(targetCol,targetFil)){
            //movimiento en L
            if(Math.abs(targetCol - preCol) * Math.abs(targetFil - preFil) == 2){
                if(espacioVacio(targetCol, targetFil)){
                    return true;
                }
            }
        }
        return false;
    }
}
    

