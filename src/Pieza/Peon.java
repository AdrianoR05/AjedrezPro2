
package Pieza;

import ajedrezpro2.PanelAjedrez;
import ajedrezpro2.Tipo;


public class Peon extends Pieza {
    
    public Peon(int color, int col, int fil) {
        super(color, col, fil);
        
        tipo = Tipo.PEON;
        
        if(color == PanelAjedrez.blanco) {
            imagen = getImagen("/piezas/peon");
        }
        else {
            imagen = getImagen("/piezas/peonotro");
            
        }
    }
    
    public boolean puedeMover(int targetCol, int targetFil){
        if(estaEnElTablero(targetCol, targetFil) && esMismoCuadro(targetCol, targetFil) == false){
            //Define el movimieno segun el color
            int valorMov;
            if(color == PanelAjedrez.blanco){
                valorMov = -1;
            } else{
                valorMov = 1;
            }
            //Chequea la pieza comida
            comida = siendoComida(targetCol, targetFil);
            //Movimiento de 1 cuadro
            if(targetCol == preCol && targetFil == preFil + valorMov && comida == null){
                return true;
            }
            //Movimiento de 2 cuadros
            if(targetCol == preCol && targetFil == preFil + valorMov*2 && comida == null && movido == false && piezaEnMismaLinea(targetCol, targetFil) == false){
                return true;
            }
            //Movimiento diagonal para comer
            if(Math.abs(targetCol - preCol) == 1 && targetFil == preFil + valorMov && comida != null && comida.color != color){
                return true;
            }
            //Movimiento "En Passant"
            if(Math.abs(targetCol - preCol) == 1 && targetFil == preFil + valorMov){
                for(Pieza pieza: PanelAjedrez.simPiezas){
                    if(pieza.col == targetCol && pieza.fil == preFil && pieza.dosPasos == true){
                        comida = pieza;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
