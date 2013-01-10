/*
 * Juego para móvil ColorLines
 * ver 1.0
 * 8/11/2008
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author ID VicMan
 */
public class ColorLines extends MIDlet implements CommandListener{
    // Atributos
    private Command salir, jugar, opc, salirplay, newplay, about;
    private int tok;
    private Display pantalla;
    private Welcome inicio;
    private ClaseJuego mijuego;
    private Opciones menu_opc;
    private About acercade;
    
    // Constructor
    public ColorLines(){
       // pantalla
       pantalla= Display.getDisplay(this);
       // botones
       salir = new Command ("Salir", Command.SCREEN,1);
       about= new Command ("Acerca de", Command.SCREEN, 2);
       opc= new Command ("Opciones", Command.SCREEN,3);
       jugar = new Command ("Jugar", Command.SCREEN,4);
       newplay= new Command ("Nuevo Juego", Command.SCREEN,3);
       salirplay = new Command ("Volver", Command.SCREEN,1);
       // la clase canvas
       inicio= new Welcome();
       mijuego= new ClaseJuego();
       menu_opc= new Opciones();
       acercade= new About();
       // agregar los botones
       inicio.addCommand(jugar);
       inicio.addCommand(opc);
       inicio.addCommand(salir);
       inicio.addCommand(about);
       mijuego.addCommand(salirplay);
       mijuego.addCommand(newplay);
       // agregar el lapiz
       inicio.setCommandListener(this);
       mijuego.setCommandListener(this);
       menu_opc.setCommandListener(this);
       acercade.setCommandListener(this);
    }
    
    // Metodos
    public void startApp() throws MIDletStateChangeException{ 
        pantalla.setCurrent(inicio); 
        inicio.repaint();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    
    }
    
    public void commandAction (Command c, Displayable s){
        if (c== salir){
            if (mijuego.play()){
                mijuego.stop();
            }
            else {
                destroyApp(false);
                notifyDestroyed();
            }
        }
        if (c== about){
            pantalla.setCurrent(acercade);
        }
            if (c== acercade.Volver()){
                pantalla.setCurrent(inicio);
                inicio.repaint();
            }
        if (c == jugar && !mijuego.play()){
            mijuego.start();
            mijuego.Nuevoplay();
            mijuego.opc(4);
            mijuego.showscore(true);
            mijuego.showtray(true);
            pantalla.setCurrent(mijuego);
        }
        if (c == opc){
            pantalla.setCurrent(menu_opc);
        }
            if (c == menu_opc.Volver()){
                pantalla.setCurrent(inicio);
                inicio.repaint();
            }
            if (c == menu_opc.Aceptar()){
                mijuego.Nuevoplay();
                pantalla.setCurrent(mijuego);
                tok= menu_opc.Token();
                mijuego.opc(tok);
                mijuego.showscore(menu_opc.Score());
                mijuego.showtray(menu_opc.Tray());
                mijuego.start();
            }
        if (c== salirplay){
            mijuego.stop();
            pantalla.setCurrent(inicio);
            inicio.repaint();
        }
        if (c==newplay){
            mijuego.Nuevoplay();
        }
    }
}
