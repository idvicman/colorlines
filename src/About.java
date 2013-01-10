import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

/**
 *
 * @author ID VicMan
 */

public class About extends GameCanvas{
    // Atributos
    private int w,h;
    private Image logo;
    static final Command volver = new Command ("Volver", Command.BACK, 1);
    
    // Constructor
    public About(){
        super(true);
        addCommand(volver);
    }
    
    // Metodos
    public Command Volver(){
      return volver;
    }
     
    // metodo abstracto de la clase Graphics
    public void paint(Graphics g){
        h= getHeight(); w=getWidth();
        g.setColor(0,0,0);
        g.fillRect(0,0,w,h);
        try{
            logo= Image.createImage("/about.png");
        } catch (Exception e){}
        g.drawImage (logo, w/2, h/2, Graphics.HCENTER|Graphics.VCENTER);
    }

}
