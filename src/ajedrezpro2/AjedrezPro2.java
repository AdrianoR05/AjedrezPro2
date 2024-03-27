package ajedrezpro2;

import javax.swing.JFrame;

public class AjedrezPro2 {

    public static void main(String[] args) {
        
        JFrame ventana = new JFrame("Ajedrez");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        
        //agrega gamepanel a la ventana
        PanelAjedrez panel = new PanelAjedrez();
        ventana.add(panel);
        ventana.pack();
        
        
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        
        panel.correrJuego();
        
  
    }
    
}
