package Pieza;

import ajedrezpro2.PanelAjedrez;
import ajedrezpro2.Tablero;
import ajedrezpro2.Tipo;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Pieza  {
    
    public Tipo tipo;
    public BufferedImage imagen;
    public int x, y; 
    public int col, fil, preCol, preFil;
    public int color;
    public Pieza comida;
    public boolean movido, dosPasos;
    
    
    public Pieza(int color, int col, int fil) {
        
        this.color = color; 
        this.col = col;
        this.fil = fil;
        x = getX(col);
        y = getY(fil);
        preCol = col;
        preFil = fil;
}
public BufferedImage getImagen(String pathImagen){

    BufferedImage imagen = null;
    
    try {
        imagen = ImageIO.read(getClass().getResourceAsStream(pathImagen + ".png"));
        
    }catch(IOException e) {
        e.printStackTrace();
    }
    return imagen;

        
    }
    public int getX(int col) {
        return col * Tablero.tamano_cuadro;  
    }
    public int getY(int fil) {
        return fil * Tablero.tamano_cuadro;
    }
    public int getCol(int x) {
        return(x + Tablero.mitad_cuadro)/Tablero.tamano_cuadro;
    }
    public int getFil(int y) {
        return(y + Tablero.mitad_cuadro)/Tablero.tamano_cuadro;
    }
    public int getIndex() {
        for(int index = 0; index < PanelAjedrez.simPiezas.size(); index++) {
            if(PanelAjedrez.simPiezas.get(index) == this) {
                return index;
            }
        } 
        return 0;
    }
    public void actualizarPos(){
        //Para chequear En Passant
        if(tipo == Tipo.PEON){
            if(Math.abs(fil - preFil) == 2){
                dosPasos = true;
            }
        }
        
        x = getX(col);
        y = getY(fil);
        preCol = getCol(x);
        preFil = getFil(y);
        movido = true;
    }
    public void reiniciarPos() {
        col = preCol;
        fil = preFil;
        x = getX(col);
        y = getY(fil);
    }
    public boolean puedeMover(int targetCol, int targetFil) {
        return false;
    }
    public boolean estaEnElTablero(int targetCol, int targetFil) {
        if(targetCol >= 0 && targetCol <= 7 && targetFil >= 0 && targetFil <= 7){
            return true;
        }
        return false;
    }
    
    public boolean esMismoCuadro(int targetCol, int targetFil){
        if(targetCol == preCol && targetFil == preFil){
            return true;
        }
        return false;
    }
    
    public Pieza siendoComida(int targetCol, int targetFil) {
        for(Pieza pieza : PanelAjedrez.simPiezas) {
            if(pieza.col == targetCol && pieza.fil == targetFil && pieza != this) {
                return pieza;
            }
        }
        return null;
    }
    public boolean espacioVacio(int targetCol, int targetFila) {
        
        comida = siendoComida(targetCol, targetFila);
        if(comida == null) {
            return true;
        }
        else {
            if(comida.color != this.color) {
                return true;
            }
            else {
                comida = null;
            }
        }
        return false;
    }
    
    public boolean piezaEnMismaLinea(int targetCol, int targetFil){
        //Cuando va a la izquierda
        for(int c = preCol-1; c > targetCol; c--){
            for(Pieza pieza : PanelAjedrez.simPiezas){
                //Si la pieza esta en la linea verifica para ser comida
                if(pieza.col == c && pieza.fil == targetFil){
                    comida = pieza;
                    return true;
                }
            }
        }
        //Cuando va a la derecha
        for(int c = preCol+1; c < targetCol; c++){
            for(Pieza pieza : PanelAjedrez.simPiezas){
                //Si la pieza esta en la linea verifica para ser comida
                if(pieza.col == c && pieza.fil == targetFil){
                    comida = pieza;
                    return true;
                }
            }
        }
        //Cuando va para arriba
        for(int f = preFil-1; f > targetFil; f--){
            for(Pieza pieza : PanelAjedrez.simPiezas){
                //Si la pieza esta en la linea verifica para ser comida
                if(pieza.col == targetCol && pieza.fil == f){
                    comida = pieza;
                    return true;
                }
            }
        }
        //Cuando va para abajo
        for(int f = preFil+1; f < targetFil; f++){
            for(Pieza pieza : PanelAjedrez.simPiezas){
                //Si la pieza esta en la linea verifica para ser comida
                if(pieza.col == targetCol && pieza.fil == f){
                    comida = pieza;
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean piezaEnMismaDiagonal(int targetCol, int targetFil){
        if(targetFil < preFil){
            //Arriba izquierda
            for(int c = preCol-1; c > targetCol; c--){
                int diferencia = Math.abs( c - preCol);
                for(Pieza pieza : PanelAjedrez.simPiezas) {
                    if(pieza.col == c && pieza.fil == preFil - diferencia) {
                        return true;
                    }
                }
            }
            //Arriba derecha
            for(int c = preCol+1; c < targetCol; c++){
                int diferencia = Math.abs( c - preCol);
                for(Pieza pieza : PanelAjedrez.simPiezas) {
                    if(pieza.col == c && pieza.fil == preFil - diferencia) {
                        return true;
                    }
                }
            }
        }
        if(targetFil > preFil){
            //Abajo izquierda
            for(int c = preCol-1; c > targetCol; c--){
                int diferencia = Math.abs( c - preCol);
                for(Pieza pieza : PanelAjedrez.simPiezas) {
                    if(pieza.col == c && pieza.fil == preFil + diferencia) {
                        return true;
                    }
                }
            }
            //Abajo derecha
            for(int c = preCol+1; c < targetCol; c++){
                int diferencia = Math.abs( c - preCol);
                for(Pieza pieza : PanelAjedrez.simPiezas) {
                    if(pieza.col == c && pieza.fil == preFil + diferencia) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public void draw(Graphics2D a2) {
        a2.drawImage(imagen, x, y, Tablero.tamano_cuadro, Tablero.tamano_cuadro, null);
    }
}
