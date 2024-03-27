package Pieza;

import ajedrezpro2.PanelAjedrez;
import ajedrezpro2.Tipo;

public class Rey extends Pieza {
    
    public Rey(int color, int col, int fil) {
        super(color, col, fil);
        
        tipo = Tipo.REY;
        
         if(color == PanelAjedrez.blanco) {
            imagen = getImagen("/piezas/rey");
        }
        else {
            imagen = getImagen("/piezas/reyotro");
            
        }
    }
    public boolean puedeMover(int targetCol, int targetFil) {
        
        if(estaEnElTablero(targetCol, targetFil)) {
            if(Math.abs(targetCol - preCol) + Math.abs(targetFil - preFil) == 1 || 
                    Math.abs(targetCol - preCol) * Math.abs(targetFil - preFil) == 1) {
                
                if(espacioVacio(targetCol, targetFil)) {
                    return true;
                }
            }
            if(movido == false){
                //enroque derecha
                if(targetCol == preCol+2 && targetFil == preFil && piezaEnMismaLinea(targetCol, targetFil) == false){
                    for(Pieza pieza : PanelAjedrez.simPiezas){
                        if(pieza.col == preCol+3 && pieza.fil == preFil && pieza.movido == false){
                            PanelAjedrez.enroqueP = pieza;
                            return true;
                        }
                    }
                }
                //enroque izquierda
                if(targetCol == preCol-2 && targetFil == preFil && piezaEnMismaLinea(targetCol, targetFil) == false){
                    Pieza p[] = new Pieza[2];
                    for(Pieza pieza : PanelAjedrez.simPiezas){
                        if(pieza.col == preCol-3 && pieza.fil == targetFil){
                            p[0] = pieza;
                        }
                        if(pieza.col == preCol-4 && pieza.fil == targetFil){
                            p[1] = pieza;
                        }
                        if(p[0] == null && p[1] != null && p[1].movido == false){
                            PanelAjedrez.enroqueP = p[1];
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
}
    

