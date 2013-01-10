import javax.microedition.lcdui.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diseno10
 */
public class Opciones extends Form{
    // Atributos
    private ChoiceGroup token,viewscore,viewtray;
    static final Command volver = new Command ("Volver", Command.BACK, 1);
    static final Command aceptar = new Command ("Jugar", Command.OK, 2);
    
    // Metodos
    public Opciones (){
        // llamada al a'pa
        super("Opciones");
        // instancia del choicegroup
        String tokens[]= {"3", "4", "5","6"};
        String answer[]= {"Si","No"};
        token= new ChoiceGroup ("Tokens en linea", Choice.EXCLUSIVE, tokens, null);
        viewscore= new ChoiceGroup ("Mostrar marcador", Choice.EXCLUSIVE, answer, null);
        viewtray= new ChoiceGroup ("Mostrar jugada", Choice.EXCLUSIVE, answer, null);
        // agregar botones y el choice
        addCommand(volver);
        addCommand(aceptar);
        append(token);
        append (viewscore);
        append (viewtray);
    }
    
    // Metodos
    public Command Volver(){
      return volver;
    }
    
    public Command Aceptar(){
      return aceptar;
    }
    
    public int Token(){
      String tok= token.getString(token.getSelectedIndex());
      if (tok.equals("3")){
          return 3;
      }
      else if (tok.equals("4")){
          return 4;
      }
      else if (tok.equals("5")){
          return 5;
      }
      else if (tok.equals("6")){
          return 6;
      }
      else return 3;
  }
    public boolean Score(){
        String sco= viewscore.getString(viewscore.getSelectedIndex());
        if (sco.equals("Si"))
            return true;
        else
            return false;
    }
    
     public boolean Tray(){
        String tray= viewtray.getString(viewtray.getSelectedIndex());
        if (tray.equals("Si"))
            return true;
        else
            return false;
    }
}
