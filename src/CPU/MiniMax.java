
package CPU;

import ajedrezpro2.Tablero;


public class MiniMax implements EstrategiaMov {
    private final EvaluarTablero evaluartablero;
    
    public MiniMax(){
        this.evaluartablero = null;
    }
    
    @Override
    public String toString(){
        return "MiniMax";
    }
    
    public int min(final Tablero tablero, final int prof){
        if(prof == 0 /*  || finJuego  */){
            return this.evaluartablero.evaluar(tablero, prof);
        }
        int valorMasPequeno = Integer.MAX_VALUE;
        //for( Mov mov : )
        return valorMasPequeno;
    }
    public int max(final Tablero tablero, final int prof){
        if(prof == 0 /*  || finJuego  */){
            return this.evaluartablero.evaluar(tablero, prof);
        }
        int valorMasGrande = Integer.MIN_VALUE;
        return valorMasGrande;
    }
}
