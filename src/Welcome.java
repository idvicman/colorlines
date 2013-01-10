import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

/**
 *
 * @author ID VicMan
 */

public class Welcome extends GameCanvas{
    // Atributos
    private int w,h;
    private Image logo;
    
    // Constructor
    public Welcome(){
        super(true);
    }
     
    // metodo abstracto de la clase Graphics
    public void paint(Graphics g){
        h= getHeight(); w=getWidth();
        g.setColor(0,0,0);
        g.fillRect(0,0,w,h);
        /*String labels[] = {"ColorLines", "", "Un juego para pensar", "ver. 1.0", "ID VicMan"};
        Font f1 = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font f2 = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        g.setColor(4,115,145);
	g.fillRect(0,0,w, h);
        g.setColor(255,255,255);
        g.setFont(f1);
        g.drawString(labels[0], w/ 2, 20, Graphics.TOP|Graphics.HCENTER);
        g.setColor(0,45,60);
        g.setFont(f2);
        for (int i = 1; i < labels.length; i++)
            g.drawString(labels[i], w/ 2, f1.getHeight() + ((i - 1) * (f2.getHeight()))+20, Graphics.TOP|Graphics.HCENTER);*/
        try{
            logo= Image.createImage("/welcome.png");
        } catch (Exception e){}
        g.drawImage (logo, w/2, h/2, Graphics.HCENTER|Graphics.VCENTER);
    }

}
