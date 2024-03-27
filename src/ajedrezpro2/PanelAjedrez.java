package ajedrezpro2;


import Pieza.Alfil;
import Pieza.Caballo;
import Pieza.Peon;
import Pieza.Pieza;
import Pieza.Reina;
import Pieza.Rey;
import Pieza.Torre;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

public final class PanelAjedrez extends JPanel implements Runnable{
    
    public static int ancho = 1100;
    public static int alto = 800;
    final int FPS = 60;
    Thread EjecutarThread;
    Tablero tablero = new Tablero();
    Mouse mouse = new Mouse();
    
    public static ArrayList<Pieza> piezas = new ArrayList<>();
    public static ArrayList<Pieza> simPiezas = new ArrayList<>();
    ArrayList<Pieza> cambiarPieza = new ArrayList<>();
    Pieza activarP, chequeandoP;
    public static Pieza enroqueP;
    
    
    //color
    public static final int blanco = 0;
    public static final int negro = 1;
    int colorActual = blanco;
    
    boolean puedeMover;
    boolean validarCuadro;
    boolean cambiarP;
    boolean finJuego;
    boolean tablas;
    
    public PanelAjedrez() {
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        
        
        setPiezas();
        //probarCambioP();
        //probarIlegal();
        copiarPiezas(piezas, simPiezas);
       
    }
    public void correrJuego() {
        EjecutarThread = new Thread(this);
        EjecutarThread.start();
    }
    public void setPiezas() {
        
        //los blancos
        piezas.add(new Peon(blanco,0,6));
        piezas.add(new Peon(blanco,1,6));
        piezas.add(new Peon(blanco,2,6));
        piezas.add(new Peon(blanco,3,6));
        piezas.add(new Peon(blanco,4,6));
        piezas.add(new Peon(blanco,5,6));
        piezas.add(new Peon(blanco,6,6));
        piezas.add(new Peon(blanco,7,6));
        piezas.add(new Torre(blanco,0,7));
        piezas.add(new Caballo(blanco,1,7));
        piezas.add(new Alfil(blanco,2,7));
        piezas.add(new Reina(blanco,3,7));
        piezas.add(new Rey(blanco,4,7));
        piezas.add(new Alfil(blanco,5,7));
        piezas.add(new Caballo(blanco,6,7));
        piezas.add(new Torre(blanco,7,7));
        
        //los negros
        piezas.add(new Peon(negro,0,1));
        piezas.add(new Peon(negro,1,1));
        piezas.add(new Peon(negro,2,1));
        piezas.add(new Peon(negro,3,1));
        piezas.add(new Peon(negro,4,1));
        piezas.add(new Peon(negro,5,1));
        piezas.add(new Peon(negro,6,1));
        piezas.add(new Peon(negro,7,1));
        piezas.add(new Torre(negro,0,0));
        piezas.add(new Caballo(negro,1,0));
        piezas.add(new Alfil(negro,2,0));
        piezas.add(new Reina(negro,3,0));
        piezas.add(new Rey(negro,4,0));
        piezas.add(new Alfil(negro,5,0));
        piezas.add(new Caballo(negro,6,0));
        piezas.add(new Torre(negro,7,0));
        
    }
    
    public void probarCambioP(){
        piezas.add(new Peon(blanco, 0, 3));
        piezas.add(new Peon(negro, 5, 4));
    }
    
    public void probarIlegal(){
        piezas.add(new Peon(blanco, 2, 7));
        piezas.add(new Peon(blanco, 3, 7));
        piezas.add(new Peon(blanco, 4, 7));
        piezas.add(new Peon(blanco, 7, 5));
        piezas.add(new Rey(blanco, 3, 6));
        piezas.add(new Rey(negro, 0, 2));
        piezas.add(new Alfil(negro, 1, 3));
        piezas.add(new Reina(negro, 4, 4));
    }
    
    private void copiarPiezas(ArrayList<Pieza> source, ArrayList<Pieza> Target) {
        
        Target.clear();
        for(int i = 0; i < source.size(); i++) {
            Target.add(source.get(i));
            
            
        }
        
    }
     @Override
    public void run() {
       //loop del juego
       
       double drawInterval = 1000000000/FPS;
       double delta = 0;
       long lastTime = System.nanoTime();
       long currentTime;
       
       while(EjecutarThread != null) {
           
           currentTime = System.nanoTime();
           
           delta += (currentTime - lastTime)/drawInterval;
           lastTime = currentTime;
           
           if(delta >= 1) {
               update();
               repaint();
               delta--;
               
            }
        }
    }
    private void update() {
        if(cambiarP){
            cambiandoP();
        }else if(finJuego == false && tablas == false){
            if(mouse.pressed) {
                if(activarP == null) {
                     // si activeP es null, se chequea si puede tomar la pieza
                    for(Pieza pieza : simPiezas) {//si la pieza es aliada, la toma como activeP
                        if(pieza.color == colorActual && pieza.col == mouse.x/Tablero.tamano_cuadro && pieza.fil == mouse.y/Tablero.tamano_cuadro) {
                        
                            activarP = pieza;
                        }
                    }
                }
                else{//si el jugador esta sosteniendo la pieza va a simular el movimiento
                simulador();
                
                }
            
            }
            if(mouse.pressed == false){
                if(activarP != null) {
                    if(validarCuadro) {
                        //movimiento confirmado
                        //se actualiza la lista de piezas en caso de que alguna se haya capturado y se remueve de la simulacion
                        copiarPiezas(simPiezas, piezas);
                        activarP.actualizarPos();
                        if(enroqueP != null){
                            enroqueP.actualizarPos();
                        }
                        if(estaReyEnJaque() && esJaqueMate()){
                            // fin del juego
                            finJuego = true;
                            
                        } 
                        else if(estaEstancado() && estaReyEnJaque() == false){
                            tablas = true;
                        }
                        else{ //El juego aun sigue
                            if(puedeCambiar()){
                                cambiarP = true;
                            } else {
                                cambiarJugador();
                            }
                            
                        }
                    }
                    else {
                        //si el movimiento no es valido se reinicia todo
                        copiarPiezas(piezas, simPiezas);
                        activarP.reiniciarPos();
                        activarP = null;
                    }
                }
            }
        }
        

    }
    private void simulador () {
        
        puedeMover = false;
        validarCuadro = false;
        
        //reinicia la lista en cada loop
        //basicamente para restaurar la pieza que fue removida en la simulacion
        copiarPiezas(piezas, simPiezas);
        
        //Resetea la posicion de la pieza del enroque
        if(enroqueP != null) {
            enroqueP.col = enroqueP.preCol;
            enroqueP.x = enroqueP.getX(enroqueP.col);
            enroqueP = null;
        }
        
        //si la pieza esta siendo sostenida, actualiza la posicion\
        activarP.x = mouse.x - Tablero.mitad_cuadro;
        activarP.y = mouse.y - Tablero.mitad_cuadro;
        activarP.col = activarP.getCol(activarP.x);
        activarP.fil = activarP.getFil(activarP.y);
        
        if(activarP.puedeMover(activarP.col, activarP.fil)) {
            
            puedeMover = true;
            
            if(activarP.comida != null) {
                simPiezas.remove(activarP.comida.getIndex());
            }
            chequeaEnroque();
            
            if(movIlegal(activarP) == false && rivalPuedeComerRey() == false){
                validarCuadro = true;
            }
        }
        
    }
    
    //2 Metodos para mov ilegales del rey en jaque
    //Movimiento ilegal del rey
    private boolean movIlegal(Pieza rey){
        if(rey.tipo == Tipo.REY){
            for(Pieza pieza : simPiezas){
                if(pieza != rey && pieza.color != rey.color && pieza.puedeMover(rey.col, rey.fil)){
                    return true;
                }
            }
        }
        return false;
    }
    //Movimiento ilegal: el rival puede comer al rey en el siguiente turno
    private boolean rivalPuedeComerRey(){
        Pieza rey = getRey(false);
        for(Pieza pieza : simPiezas){
            if(pieza.color != rey.color && pieza.puedeMover(rey.col, rey.fil)){
                return true;
            }
        }
        return false;
    }
    
    private boolean estaReyEnJaque(){
        Pieza rey = getRey(true);
        if(activarP.puedeMover(rey.col, rey.fil)){
            chequeandoP = activarP;
            return true;
        } else{
            chequeandoP = null;
        }
        return false;
    }
    
    private Pieza getRey(boolean rival){
        Pieza rey = null;
        for(Pieza pieza : simPiezas){
            if(rival){
                if(pieza.tipo == Tipo.REY && pieza.color != colorActual){
                    rey = pieza;
                }
            } else{
                if(pieza.tipo == Tipo.REY && pieza.color == colorActual){
                    rey = pieza;
                }
            }
        }
        return rey;
    }
    //Rey puede moverse para evitar el jaquemate
    private boolean esJaqueMate(){
        Pieza rey = getRey(true);
        if(reyPuedeMover(rey)){
            return false;
        } else{
            //Chequea si se puede bloquear el ataque con otra pieza
            
            //Verifica la posicion de la pieza amenazando y del rey en jaque
            int colDif = Math.abs(chequeandoP.col - rey.col);
            int filDif = Math.abs(chequeandoP.fil - rey.fil);
            if(colDif == 0){
                //La pieza amenazando esta en vertical
                if(chequeandoP.fil < rey.fil){
                    //La pieza esta por encima del rey
                    for(int fil = chequeandoP.fil; fil < rey.fil; fil++){
                        for(Pieza pieza : simPiezas){
                            if(pieza != rey && pieza.color != colorActual && pieza.puedeMover(chequeandoP.col, fil)){
                                return false;
                            }
                        }
                    }
                }
                if(chequeandoP.fil > rey.fil){
                    //La pieza esta por debajo del rey
                    for(int fil = chequeandoP.fil; fil > rey.fil; fil--){
                        for(Pieza pieza : simPiezas){
                            if(pieza != rey && pieza.color != colorActual && pieza.puedeMover(chequeandoP.col, fil)){
                                return false;
                            }
                        }
                    }
                }
            } else if(filDif == 0){
                //La pieza amenazando esta en horizontal
                if(chequeandoP.col < rey.col){
                    //La pieza esta a la izquierda
                    for(int col = chequeandoP.col; col < rey.col; col++){
                        for(Pieza pieza : simPiezas){
                            if(pieza != rey && pieza.color != colorActual && pieza.puedeMover(col, chequeandoP.fil)){
                                return false;
                            }
                        }
                    }
                }
                if(chequeandoP.col > rey.col){
                    //La pieza esta a la derecha
                    for(int col = chequeandoP.col; col > rey.col; col--){
                        for(Pieza pieza : simPiezas){
                            if(pieza != rey && pieza.color != colorActual && pieza.puedeMover(col, chequeandoP.fil)){
                                return false;
                            }
                        }
                    }
                }
            } else if(colDif == filDif){
                //La pieza amenazando esta en diagonal
                if(chequeandoP.fil < rey.fil){
                    //La pieza esta por encima del rey
                    if(chequeandoP.col < rey.col){
                        //La pieza esta arriba-izquierda
                        for(int col = chequeandoP.col, fil = chequeandoP.fil; col < rey.col; col++, fil++){
                            for(Pieza pieza : simPiezas){
                                if(pieza != rey && pieza.color != colorActual && pieza.puedeMover(col, fil)){
                                    return false;
                                }
                            }
                        }
                    }
                    if(chequeandoP.col > rey.col){
                        //La pieza esta arriba-derecha
                        for(int col = chequeandoP.col, fil = chequeandoP.fil; col > rey.col; col--, fil++){
                            for(Pieza pieza : simPiezas){
                                if(pieza != rey && pieza.color != colorActual && pieza.puedeMover(col, fil)){
                                    return false;
                                }
                            }
                        }
                    }
                }
                if(chequeandoP.fil < rey.fil){
                    //La pieza esta por debajo del rey
                    if(chequeandoP.col < rey.col){
                        //La pieza esta abajo-izquierda
                        for(int col = chequeandoP.col, fil = chequeandoP.fil; col < rey.col; col++, fil--){
                            for(Pieza pieza : simPiezas){
                                if(pieza != rey && pieza.color != colorActual && pieza.puedeMover(col, fil)){
                                    return false;
                                }
                            }
                        }
                    }
                    if(chequeandoP.col > rey.col){
                        //La pieza esta abajo-derecha
                        for(int col = chequeandoP.col, fil = chequeandoP.fil; col < rey.col; col--, fil--){
                            for(Pieza pieza : simPiezas){
                                if(pieza != rey && pieza.color != colorActual && pieza.puedeMover(col, fil)){
                                    return false;
                                }
                            }
                        }
                    }
                }
            } else{
                //La pieza amenazando es un caballo
            }
        }
        
        return true;
    }
    
    private boolean reyPuedeMover(Pieza rey){
        //Simula si hay algun cuadro al que se pueda mover el rey
        if(esMovValido(rey, -1, -1)){
            return true;
        }
        if(esMovValido(rey, 0, 1)){
            return true;
        }
        if(esMovValido(rey, 1, -1)){
            return true;
        }
        if(esMovValido(rey, 1, 0)){
            return true;
        }
        if(esMovValido(rey, -1, 0)){
            return true;
        }
        if(esMovValido(rey, -1, 1)){
            return true;
        }
        if(esMovValido(rey, 1, 1)){
            return true;
        }
        if(esMovValido(rey, 0, -1)){
            return true;
        }
        return false;
    }
    
    private boolean esMovValido(Pieza rey, int colMas, int filMas){
        boolean esMovValido = false;
        //Actualiza la posicion del rey por un segundo
        rey.col += colMas;
        rey.fil += filMas;
        if(rey.puedeMover(rey.col, rey.fil)){
            if(rey.comida != null){
                simPiezas.remove(rey.comida.getIndex());
            }
            if(movIlegal(rey) == false){
                esMovValido = true;
            }
        }
        //Resetea la posicion del rey y restablece las piezas removidas
        rey.reiniciarPos();
        copiarPiezas(piezas, simPiezas);
        
        return esMovValido;
    }
    
    private boolean estaEstancado(){
        int cont = 0;
        //Cuenta el numero de piezas
        for(Pieza pieza : simPiezas){
            if(pieza.color != colorActual){
                cont++;
            }
        }
        //Si solo queda el rey
        if(cont == 1){
            if(reyPuedeMover(getRey(true)) == false){
                return true;
            }
        }
        return false;
    }
    
    private void chequeaEnroque(){
        if(enroqueP != null){
            if(enroqueP.col == 0){
                enroqueP.col += 3;
            } else if(enroqueP.col == 7){
                enroqueP.col -= 2;
            }
            enroqueP.x = enroqueP.getX(enroqueP.col);
        }
    }
    
    private void cambiarJugador(){
        if(colorActual == blanco){
            colorActual = negro;
            //Resetea el estatus de dosPasos del negro
            for(Pieza pieza : piezas){
                if(pieza.color == negro){
                    pieza.dosPasos = false;
                }
            }
        }else {
            colorActual = blanco;
            //Resetea el estatus de dosPasos del blanco
            for(Pieza pieza : piezas){
                if(pieza.color == blanco){
                    pieza.dosPasos = false;
                }
            }
        }
        activarP = null;
    }
    
    private boolean puedeCambiar(){
        if(activarP.tipo == Tipo.PEON){
            //Blanca: Verifica si es llego al final || Negra: Verifica si llego al final
            if(colorActual == blanco && activarP.fil == 0 || colorActual == negro && activarP.fil == 7){
                cambiarPieza.clear();
                cambiarPieza.add(new Torre(colorActual, 9, 2));
                cambiarPieza.add(new Caballo(colorActual, 9, 3));
                cambiarPieza.add(new Alfil(colorActual, 9, 4));
                cambiarPieza.add(new Reina(colorActual, 9, 5));
                return true;
            }
        }
        return false;
    }
    
    private void cambiandoP(){
        if(mouse.pressed){
            for(Pieza pieza : cambiarPieza){
                if(pieza.col == mouse.x/Tablero.tamano_cuadro && pieza.fil == mouse.y/Tablero.tamano_cuadro){
                    switch(pieza.tipo){
                        case TORRE: simPiezas.add(new Torre(colorActual, activarP.col, activarP.fil));
                            break;
                        case CABALLO: simPiezas.add(new Caballo(colorActual, activarP.col, activarP.fil));
                            break;
                        case ALFIL: simPiezas.add(new Alfil(colorActual, activarP.col, activarP.fil));
                            break;
                        case REINA: simPiezas.add(new Reina(colorActual, activarP.col, activarP.fil));
                            break;
                        default: break;
                    }
                    simPiezas.remove(activarP.getIndex());
                    copiarPiezas(simPiezas, piezas);
                    activarP = null;
                    cambiarP = false;
                    cambiarJugador();
                }
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics a) {
        super.paintComponent(a);
        
        Graphics2D a2 = (Graphics2D)a;
        
        tablero.draw(a2);
        
        for(Pieza P : simPiezas) {
            P.draw(a2);
        }
        if(activarP != null) {
            if(puedeMover) {
                if(movIlegal(activarP) || rivalPuedeComerRey()){
                    a2.setColor(Color.RED);
                    a2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    a2.fillRect(activarP.col*Tablero.tamano_cuadro, activarP.fil*Tablero.tamano_cuadro, Tablero.tamano_cuadro, Tablero.tamano_cuadro);
                    a2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } else{
                    a2.setColor(Color.green);
                    a2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    a2.fillRect(activarP.col*Tablero.tamano_cuadro, activarP.fil*Tablero.tamano_cuadro, Tablero.tamano_cuadro, Tablero.tamano_cuadro);
                    a2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }

            }
            
            activarP.draw(a2);
        }
        //Status de la partida
        a2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        a2.setFont(new Font("Arial Narrow", Font.PLAIN, 40));
        a2.setColor(Color.white);
        
        if(cambiarP){
            a2.drawString("Cambiar por: ", 840, 150);
            for(Pieza pieza : cambiarPieza){
                a2.drawImage(pieza.imagen, pieza.getX(pieza.col), pieza.getY(pieza.fil), Tablero.tamano_cuadro, Tablero.tamano_cuadro, null); 
            }
        } else{
                if(colorActual == blanco){
                    a2.drawString("Turno del blanco", 840, 550);
                    if(chequeandoP != null && chequeandoP.color == negro){
                        a2.setColor(Color.YELLOW);
                        a2.drawString("El Rey esta", 840, 650);
                        a2.drawString("en Jaque", 840, 700);
                    }
                }else{
                    a2.drawString("Turno del negro", 840, 250);
                    if(chequeandoP != null && chequeandoP.color == blanco){
                        a2.setColor(Color.YELLOW);
                        a2.drawString("El Rey esta", 840, 100);
                        a2.drawString("en Jaque", 840, 150);
                    }
                }
        }
        if(finJuego){
            String u = "";
            if(colorActual == blanco){
                u = "GGaanador Blanco";
            } else{
                u = "Ganador Negro";
            }
            a2.setFont(new Font("Arial Narrow", Font.PLAIN, 90));
            a2.setColor(Color.MAGENTA);
            a2.drawString(u, 200, 420);
        }
        if(tablas){
            a2.setFont(new Font("Arial Narrow", Font.PLAIN, 90));
            a2.setColor(Color.orange);
            a2.drawString("Tablas", 200, 420);
        }
    }
}


