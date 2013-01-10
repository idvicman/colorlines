import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.lang.*;
import java.util.*;
/*
 * Clase GameCanvas para juego ColorLines
 * ver 1.0
 * 8/11/2008
 */

/**
 * @author ID VicMan
 */
public class ClaseJuego extends GameCanvas implements Runnable{
    // Atributos 
     private Command salir, newplay;
     private boolean play, vienedemov, generarballs, first3balls, firstime;
     private int oldx, oldy, x,y,newx, newy, h,w,col,fil,inc, bandera, bandxy, tokens, puntos, score;
     private int colorballs[]= new int[6], GO[]={0,1};
     private Image seleccion, background, ballsgroup, logo, numbers, nextballs, gameover;
     private Graphics g;
     private LayerManager lm;
     private static Vector tracebuff = new Vector();    
     private static int tracelast = 0;
     TiledLayer tablero, balls, newballs, printscore, printballs, printgameover;
     Sprite cursor;
     Random rnd = new Random(System.currentTimeMillis());
    
     
    // Constructor 
    public ClaseJuego(){
        
        
        // invocar a la clase padre GameCanvas
        super(true);
        // metodo que inicializa los valores de los atributos
        iniciavar();
        // metodo que inicializa el tablero
        iniciatablero();
        // metodo que inicializa el tablero imaginario de balls
        iniciaballs();
        // metodo que inicializa el cursor
        iniciacursor();
        // metodo que inicializa el tablerito para el marcador
        iniciascore();
        // metodo que inicializa el tablerito para las nuevas balls
        inicianextballs();
        // metodo que inicializa el letrerito de gameover
        GameOver();
        // metodos para generar balls
        //nextballs();
        //fillballs();
        // Instancia de botones
        salir= new Command("Salir", Command.EXIT,1);
        newplay= new Command("Nuevo Juego", Command.SCREEN,2);
        // Instancia de Layer Manager
        lm= new LayerManager();
        // Agregar layers al Layer Manager
        //lm.append(cursor);
        lm.append(printgameover);
        lm.append(balls);
        lm.append(tablero);
        lm.append(printscore);
        lm.append(printballs);
        
        
        //showWelcome(g);
    }   
    
    // Metodos
    
    //metodo para inicializar los valores de los atributos, llamado desde el constructor
    public void iniciavar(){
        h= getHeight(); w=getWidth();
        x=w/2-90; y=h/2-90;
        oldx=x; oldy=y;
        col=9; fil=9;
        inc=20;
        g=getGraphics();
        play= false;
        bandera=0;
        bandxy=1;
        vienedemov=false;
        score=0; puntos=2;
        generarballs= true; first3balls= true;
        firstime=true;
    }
    
    public void opc(int tok){
        tokens= tok;
    }
    
    public void showscore(boolean answer){
        printscore.setVisible(answer);
    }
    
    public void showtray(boolean answer){
        printballs.setVisible(answer);
    }
    
    //metodo para mensaje de bienvenida
    private void showWelcome(Graphics g){
        String labels[] = {"ColorLines", "", "Un juego para pensar", "ver. 1.0", "ID VicMan"};
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
            g.drawString(labels[i], w/ 2, f1.getHeight() + ((i - 1) * (f2.getHeight()))+20, Graphics.TOP|Graphics.HCENTER);
        try{
            logo= Image.createImage("/ColorLines.png");
        } catch (Exception e){}
        g.drawImage (logo, w/2, h/2, Graphics.HCENTER|Graphics.VCENTER);
    }
    
    //metodo para mensaje de juego terminado
    private void showGameOver(Graphics g){
        System.out.println("letrero de perdiste....");
        Font f1 = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font f2 = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        g.setFont(f1);
        g.setColor(0,45,60);
        g.fillRoundRect(0, h/2, w, f1.getHeight() + f2.getHeight(), 5, 5);
        g.setColor(4,115,145);
        g.drawString("Haz perdido!!", w/2, h/2, Graphics.TOP|Graphics.HCENTER);
        g.setFont(f2);
    }
    
    // metodo para inicializar los valores del tablero imaginario de balls, llamado desde el constructor
    public void iniciaballs(){
        try{
            // Imagen del tablero 180x180px 9x9, tiles de 20x20px c/u
            ballsgroup= Image.createImage("/balls.png");
            balls= new TiledLayer(col,fil,ballsgroup,20,20);
        } catch (Exception e){}
        // crear arreglo para el tablero
        int mapa2[][]={
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
        };
        //almacenar los bloques del tablero en la posicion de pantalla
        for(int j=0; j<fil; j++)
            for (int i=0; i<col; i++)
                balls.setCell(i, j, mapa2[j][i]);  
        
    }
    
    //metodo para inicializar el tablerito para mostrar el score
    public void iniciascore(){
        try{
            // imagen de los numbers 132x20px 11x1 tiles de 12x20 c/u
            numbers= Image.createImage("/numbers.png");
            printscore= new TiledLayer(15,1,numbers,12,20);
        }catch (Exception e){}
        // crear arreglo para el tablero
        int mapa3 [][]= { {11,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};
        // almacenar
        for (int j=0; j<1; j++)
            for (int i=0; i<15; i++)
                printscore.setCell(i, j, mapa3[j][i]);
    }
    
    //metodo para inicializar el tablerito para mostrar gameover
    public void GameOver(){
        try{
            // imagen
            gameover= Image.createImage("/gameover.png");
            printgameover= new TiledLayer(1,1,gameover,240,60);
        }catch (Exception e){}
        // crear arreglo
        int mapa5[][]= {{1}};
        //almacenar
        for (int j=0; j<1; j++)
            for (int i=0; i<1; i++)
                printgameover.setCell(i, j, mapa5[j][i]);
        printgameover.setVisible(false);   
    }
    
    //metodo para inicializar el tablerito para mostrar las nextballs
    public void inicianextballs(){
        try{
            // imagen
            nextballs= Image.createImage("/nextballs.png");
            printballs= new TiledLayer(9,1,nextballs,20,20);
        }catch (Exception e){}
        // crear arreglo
        int mapa4[][]= { {8,8,8,8,8,8,8,8, 8} };
        //almacenar
        for (int j=0; j<1; j++)
            for (int i=0; i<9; i++)
                printballs.setCell(i, j, mapa4[j][i]);
    }

    // metodo para inicializar los valores del tablero, llamado desde el constructor
    public void iniciatablero(){
        try{
            // Imagen del tablero 180x180px 9x9, tiles de 20x20px c/u
            background= Image.createImage("/tablero.png");
            tablero= new TiledLayer(col,fil,background,20,20);
        } catch (Exception e){}
        // crear arreglo para el tablero
        int mapa[][]={
            {7,4,4,4,4,4,4,4,9},
            {3,1,1,1,1,1,1,1,2},
            {3,1,1,1,1,1,1,1,2},
            {3,1,1,1,1,1,1,1,2},
            {3,1,1,1,1,1,1,1,2},
            {3,1,1,1,1,1,1,1,2},
            {3,1,1,1,1,1,1,1,2},
            {3,1,1,1,1,1,1,1,2},
            {6,5,5,5,5,5,5,5,8},
        };
        int mapax= mapa.length;
        int mapay= mapa[0].length;
        
        //almacenar los bloques del tablero en la posicion de pantalla
        for(int j=0; j<fil; j++)
            for (int i=0; i<col; i++)
                tablero.setCell(i, j, mapa[j][i]); 
    }
    
    // metodo para inicalizar el sprite, llamado desde el constructor
    public void iniciacursor(){
         try{
            // Imagen del tablero 180x180px 9x9, tiles de 20x20px c/u
            seleccion= Image.createImage("/cursor2.png");
            // instancia de la clase sprite
            cursor= new Sprite(seleccion, 20, 20);
        } catch (Exception e){}
        // Dar de alta la secuencia de imagenes en el sprite
      cursor.setFrameSequence(GO);
    }
    
    //metodo para generar tres nuevos colores para los balls
    private void nextballs () {    
        // La primera vez de cada juego
        if (generarballs==true && firstime==true){
            for (int c = 0; c < 6; c++) {
                colorballs[c] = Math.abs(rnd.nextInt() % 7) + 1;
                    if (c>2)
                        printballs.setCell(8-(c-3), 0, colorballs[c]);
            }
        }
        // cuando sea necesario pintar 0,1 y 2
        else if (generarballs==true && firstime==false){
            for (int c = 0; c < 6; c++) {
                colorballs[c] = Math.abs(rnd.nextInt() % 7) + 1;
                    if (c<3)
                        printballs.setCell(8-c, 0, colorballs[c]);
            }
        }
        // cuando sea necesario pintar 3,4 y 5
        else if (generarballs==false){
            for (int c = 3; c < 6; c++)
                printballs.setCell(8-(c-3), 0, colorballs[c]);
        }
    }
    
    //metodo para asignar la ubicacion de los nuevos balls
    private void fillballs(){
        int cont=0;
        int posballs[]= new int[3];
        if (first3balls==true){        
            // contar los espacios vacios
            for(int r=0; r<9; r++)
                for (int c=0; c<9; c++)
                    if (balls.getCell(r, c)==0)
                        cont++;
            // asignar una ubicacion diferente para cada ball
            while (posballs[0]==posballs[1] || posballs[1]==posballs[2] || posballs[0]==posballs[2])
                for (int d = 0; d < 3; d++) 
                    posballs[d] = Math.abs(rnd.nextInt() % cont);
            cont=0;
            // asignar las balls al tablero
            for(int r=0; r<9; r++)
                for (int c=0; c<9; c++)
                    if (balls.getCell(r, c)==0){
                    for (int d = 0; d < 3; d++)
                           if (posballs[d]==cont)
                               balls.setCell(r, c, colorballs[d]);
                    cont++;
                    }             
            generarballs=false;
            first3balls=false;
        }
        else{
            cont=0;
            // contar los espacios vacios
            for(int r=0; r<9; r++)
                for (int c=0; c<9; c++)
                    if (balls.getCell(r, c)==0)
                        cont++;
            // asignar una ubicacion diferente para cada ball
            while (posballs[0]==posballs[1] || posballs[1]==posballs[2] || posballs[0]==posballs[2])
                for (int d = 0; d < 3; d++) 
                    posballs[d] = Math.abs(rnd.nextInt() % cont);
            cont=0;
            // asignar las balls al tablero
            for(int r=0; r<9; r++)
                for (int c=0; c<9; c++)
                    if (balls.getCell(r, c)==0){
                    for (int d = 0; d < 3; d++)
                           if (posballs[d]==cont)
                               balls.setCell(r, c, colorballs[d+3]);
                    cont++;
                    }             
            generarballs=true;
            first3balls=true;
        }
    }
    
    // metodo abstracto del implements Runnable
    public void run(){
        // valores iniciales de la posicion x e y
        // ciclo infinito mientras se pulse alguna tecla
        
        while (play){
            if (evalua()){
                System.out.println("Juego terminado");
                showGameOver(g);
                stop();
            }
            
            // evalua la tecla oprimida
           //evaluacursor();
            if (bandxy==1)
                evaluafire();
            //keypress();
            // dibuja
            paint();
            // evalua si hay un cambio de sentido o si ha llegado al final de la pantalla
            //actualiza();
            // pausa
            try{
                Thread.sleep(30);
            } catch(Exception e){}
        }
    }
    
    // NEW!!!  metodo abstracto para usar el lapicito
    protected void pointerPressed (int x, int y){
         if ((x)>(w/2-90) && (x)<(w/2+90) && (y)>(h/2-90) && (y)<(h/2+90) ){
           oldx= this.x;
           oldy=this.y;
           this.x= x;
           this.y= y;
           bandxy=1;
        }
        else
        {
            bandxy=0;
            this.x= oldx;
                    
        }
    }  
    
    // metodo para evaluar la tecla que oprime el usuario
    private void keypress(){
        // variable que toma el valor de la tecla oprimida
        int key= getKeyStates();
        if ((x)>(w/2-90))
            if ((key & LEFT_PRESSED)!=0){
                x-=inc;
            }      
        if ((x+20)<(w/2+90))
            if ((key & RIGHT_PRESSED)!=0){ 
                x+=inc;
            }  
        if ((y)>(h/2-90))
        if ((key & UP_PRESSED)!=0){
            y-=inc;
        }
        if ((y+20)<(h/2+90))
        if ((key & DOWN_PRESSED)!=0){
            y+=inc;
        }
        if ((key & FIRE_PRESSED)!=0){
            evaluafire();
        }
    }
    
    // metodo para evaluar los casos si se da click en FIRE
    public void evaluafire(){
        int XX= (x-(w/2-90))/inc;
        int YY= (y-(h/2-90))/inc;
        if (balls.getCell(XX, YY)==0 && bandera==1){
            System.out.println("Enviar al metodo move");
            move();
            vienedemov=false;
            cursor.prevFrame();
            //bandera=0;
        }
        else if (balls.getCell(XX, YY)!=0 && bandera==1){
            newx=x; newy=y;
           // System.out.println("cambiar nueva x e y");
            bandera=1;
        }
        else if (balls.getCell(XX, YY)!=0 && bandera==0){
            //if (!vienedemov){
                newx= x; newy=y;
                bandera=1;
                cursor.nextFrame();
                System.out.println("asignar nueva x e y");
            //}
            //else{
                
                //bandera=1;
            //}
        }
    }
    
    //metodo para intentar realizar un movimiento
    public void move(){
        Vector path;
        int Sx= (newx-(w/2-90))/inc;
        int Sy= (newy-(h/2-90))/inc;
        int Gx= (x-(w/2-90))/inc;
        int Gy= (y-(h/2-90))/inc;
        int xfinal, yfinal;
        path= trace(balls, Sx, Sy, Gx, Gy);
        System.out.println("Sx: "+Sx+" Sy: "+Sy+" Gx: "+Gx+" Gy: "+Gy);
        if (path!=null){
            System.out.println("encontro una ruta viable");
            for (Enumeration e = path.elements() ; e.hasMoreElements() ;) {
                //System.out.println(e.nextElement());
                int coord[]= (int[])e.nextElement();
                System.out.println(coord[0]+","+coord[1]);
                xfinal=coord[0];
                yfinal=coord[1];
            }
            int color= balls.getCell(Sx, Sy);
            balls.setCell(Sx, Sy, 0);
            System.out.println(color);
            balls.setCell(Gx, Gy, color);
            removeballs();
            cursor.prevFrame();
            bandera=0;
            vienedemov=true;
            System.out.println("bandera: "+bandera);
            //evalua();
            fillballs();
            nextballs();
            removeballs();
        }
    }
    
    // metodo para borrar balls del mismo color
    private void removeballs(){
        Vector clr= getcleared(balls, tokens);
        // si hay lineas por borrar
        if (clr!=null){
            Enumeration coord = clr.elements();
            while (coord.hasMoreElements()){
                int del[]= (int [])coord.nextElement();
                balls.setCell(del[0], del[1], 0);
                score+=puntos;
                score(score);
            }
        System.out.println("********************************* SCORE: "+score);
        //score(score);
        }
    }
    
    // metodo para actualizar el score
    private void score(int puntos){
        StringBuffer marcador= new StringBuffer(Integer.toString(puntos));
        
        System.out.println("largo del marcador"+marcador.length());
        
        for (int lar=0; lar<marcador.length(); lar++){
            char caracter= marcador.charAt(lar);
            System.out.print(" - "+caracter);
            switch(caracter){
                case '0': printscore.setCell(lar, 0, 11); break;
                case '1': printscore.setCell(lar, 0, 2); break;
                case '2': printscore.setCell(lar, 0, 3); break;
                case '3': printscore.setCell(lar, 0, 4); break;
                case '4': printscore.setCell(lar, 0, 5); break;
                case '5': printscore.setCell(lar, 0, 6); break;
                case '6': printscore.setCell(lar, 0, 7); break;
                case '7': printscore.setCell(lar, 0, 8); break;
                case '8': printscore.setCell(lar, 0, 9); break;
                case '9': printscore.setCell(lar, 0, 10); break;
                 default: printscore.setCell(lar,0, 1); break;
            }
        }
    }
    
    // metodo para limpiar el score
    private void cleanscore(){
        for (int j=0; j<1; j++)
            for (int i=0; i<15; i++)
                printscore.setCell(i, j, 1);
    }
    
    //metodo para saber si el jugador ha perdido
    public boolean evalua(){
        int contvacias=0;
        for (int xx = 0; xx < balls.getRows(); xx++)
            for (int yy = 0; yy < balls.getColumns(); yy++)
                if (balls.getCell(xx, yy)==0)
                    contvacias++;
        if (contvacias<3){
            //showGameOver(g);
            //play=false;
            //printgameover.setVisible(true);
            return true;
            //System.out.println("Juego terminado");
        }
        return false;
    }
    
    //metodo para mostrargameover en caso de perder
    public void sipierde(){
        int contvacias=0;
        for (int xx = 0; xx < balls.getRows(); xx++)
            for (int yy = 0; yy < balls.getColumns(); yy++)
                if (balls.getCell(xx, yy)==0)
                    contvacias++;
        if (contvacias<3){
            printballs.setVisible(false);
            //balls.setVisible(false);
            //tablero.setVisible(false);
            printgameover.setVisible(true);
        }
    }
    
    // metodo para un nuevo juego
    public void Nuevoplay(){
        play=true;
        cleanscore();
        score=0;
        score(score);
        firstime=true;
        //balls.setVisible(true);
        //tablero.setVisible(true);
        //printballs.setVisible(true);
        //printscore.setVisible(true);
        printgameover.setVisible(false);
        for(int j=0; j<fil; j++)
            for (int i=0; i<col; i++)
                balls.setCell(i, j, 0);
        flushGraphics();
        nextballs();
        fillballs();
        firstime=false;
    }
    
            
    
    private Vector getcleared(TiledLayer tableroballs, int len ){
        int brdx = tableroballs.getColumns();
        int brdy = tableroballs.getRows();
        int [][]tr = new int[brdx][brdy];
        int last = 0;
        int []l = new int[2];   
        Vector buff = new Vector();
        // recorrer el tablero horizontal y vertical
        for (int xx = 0; xx < brdx; xx++){
            traceline(0,0,0,0,-1);
            for (int yy = 0; yy < brdy; yy++){
                Vector v =  traceline(xx, yy, tableroballs.getCell(xx, yy), len, 0);
                if (v != null){
                    Enumeration el = v.elements();
                    while(el.hasMoreElements()){
                        int []ll = (int[])el.nextElement();
                        tr[ll[0]][ll[1]] = 1;
                    }
                }
            }
        }
   
        for (int yy = 0; yy < brdy; yy++) {
            traceline(0,0,0,0,-1);
            for (int xx = 0; xx < brdx; xx++) {
                Vector v =  traceline(xx, yy, tableroballs.getCell(xx, yy), len, 0);
                if (v != null){
                    Enumeration el = v.elements();
                    while(el.hasMoreElements()){
                        int []ll = (int[])el.nextElement();
                        tr[ll[0]][ll[1]] = 1;
                    }
                }
            }
        }

        // recorrer el tablero diagonal
        for (int xx = 0; xx < brdx; xx++){
            // right1 
            traceline(0,0,0,0,-1); 
            for (int yy = 0; yy < brdy; yy++) {
                if (xx + yy < brdx){
                    int px = xx + yy;
                    int py = yy;
                    Vector v =  traceline(px, py, tableroballs.getCell(px, py), len, 0);
                    if (v != null) {
                        Enumeration el = v.elements();
                        while(el.hasMoreElements()){
                            int []ll = (int[])el.nextElement();
                            tr[ll[0]][ll[1]] = 1;
                        }
                    }
                }
            }
 
            // right2 
            traceline(0,0,0,0,-1); 
            for (int yy = 0; yy < brdy; yy++){
                if (xx + yy < brdy) {
                    int px = yy;
                    int py = xx + yy;
                    Vector v =  traceline(px, py, tableroballs.getCell(px, py), len,0);
                    if (v != null) {
                        Enumeration el = v.elements();
                        while(el.hasMoreElements()){
                            int []ll = (int[])el.nextElement();
                            tr[ll[0]][ll[1]] = 1;
                        }
                    }
                }
            }        

            // left1
            traceline(0,0,0,0,-1); 
            for (int yy = 0; yy < brdy; yy++) {
                if (xx + yy < brdx) {
                    int px = (brdx - 1) - (xx + yy);
                    int py = yy;
                    Vector v =  traceline(px, py,tableroballs.getCell(px, py), len,0);
                    if (v != null) {
                        Enumeration el = v.elements();
                        while(el.hasMoreElements()) {
                            int []ll = (int[])el.nextElement();
                            tr[ll[0]][ll[1]] = 1;
                        }
                    }
                }
            }
    
            // left2
            traceline(0,0,0,0,-1); 
            for (int yy = 0; yy < brdy; yy++){
                if (xx + yy < brdx) {
                    int py = xx + yy;
                    int px = (brdx - 1) - yy;
                    Vector v =  traceline(px, py, tableroballs.getCell(px, py), len,0);
                    if (v != null) {
                        Enumeration el = v.elements();
                        while(el.hasMoreElements()) {
                            int []ll = (int[])el.nextElement();
                            tr[ll[0]][ll[1]] = 1;
                        }
                    }
                }
            }
        }// fin del for para diagonales

        for (int xx = 0; xx < brdx; xx++)
            for (int yy = 0; yy < brdy; yy++)
                if (tr[xx][yy] == 1) {
                    int []ll = new int[2];
                    ll[0] = xx;
                    ll[1] = yy;
                    buff.addElement(ll);
                }

    return buff;       
    }
    
    // metodo auxiliar para getcleared
    private Vector traceline(int x, int y, int v, int l, int s)
  {
     if (s == -1){
     tracebuff.removeAllElements();
     tracelast = 0;
     return null;
     }
      
    if (v == 0)
    {
        tracebuff.removeAllElements();
        tracelast = 0;
        return null;
    }
    
    int []ll = new int[3];
    ll[0] = x;
    ll[1] = y;
    ll[2] = v;
    
    tracebuff.addElement(ll);
    
    if (tracebuff.size() < l)
        return null;
    
    if (tracebuff.size() > l)
        tracebuff.removeElementAt(0);
    
    // do we have a buffer valid for clearing ?
    boolean clear = true;
    Enumeration n = tracebuff.elements();
    int last = 0;
    while(n.hasMoreElements())
        {
            int []el = (int[])n.nextElement();
            
            if (last == 0)
                last = el[2];
            
            /*if (last == s)
                last = el[2];*/
            
            if ((last != el[2]) /*&& (el[2)!=s*/)
                clear = false;
        }
    
    if (clear)
        return tracebuff;
    return null;
  }
    
    //metodo para generar el vector o ruta del movimiento
    public Vector trace(TiledLayer tableroballs, int sx, int sy, int gx, int gy){
        Vector retvc = new Vector();
        int brdx = tableroballs.getColumns();
        int brdy = tableroballs.getRows();
        int [][]tr = new int[brdx][brdy];
        int val=0, iter = 0;
        boolean tracemore = true;
        // llenar la matriz tr de 0's
        for (int r = 0; r < tr.length; r++)
            for (int c = 0; c < tr[r].length; c++)
                tr[r][c] = val;
        // asignar en la ubicacion del movimiento un valor 1
        
        tr[sx][sy] = 1;
        // inicia ciclo
        while(tracemore)
        {
            int trccnt = 0;
            iter++;
            tracemore = true;
            // recorrer todo el tablero
            for (int xx = 0; (xx < brdx) && tracemore; xx++)
                for (int yy = 0; (yy < brdy) && tracemore; yy++)
	        {
                    int trl = brdx * brdy;
                    // esta vacio???
                    if (tableroballs.getCell(xx, yy) == 0)
                    { // eval all eight corners
                        int b;
                        if (xx > 0){
                            b = tr[xx-1][yy];
                            if (b > 0)
                                trl = Math.min(trl, b + 1);
                        }
		
                        if (xx < (brdx - 1)){
                            b = tr[xx+1][yy];
                            if (b > 0)
                                trl = Math.min(trl, b + 1);
                        }
                        if (yy > 0){
                            b = tr[xx][yy - 1];
                            if (b > 0)
                                trl = Math.min(trl, b + 1);
                        }
                        if (yy < (brdy - 1)){
                            b = tr[xx][yy + 1];
                            if (b > 0)
                                trl = Math.min(trl, b + 1);
                        }
                        if ((trl < brdx * brdy) && (tr[xx][yy] == 0) || (trl < tr[xx][yy]))
                        {
                            tr[xx][yy] = trl;
                            trccnt++;
                            if ((xx == gx) && (yy == gy))
                                tracemore = false;
                        }
                    }
                } // fin del for
            
            if (trccnt == 0) // no necesita trazar mas
                tracemore = false;
        }// fin del while

        // no encontro un ruta
        if (tr[gx][gy] == 0) {
            return null;
        }
        // encontro una ruta y devuelve un vector valido  
        int []l = new int[2];
        l[0] = gx;
        l[1] = gy;
        // insertElementAt(object obj, int index)
        // inserta el objeto especifico como un componente en el vector en el indice especifico
        retvc.insertElementAt(l, 0);
        tracemore = true;
        int btrx = gx;
        int btry = gy;
        while(tracemore)
        {
            int min = tr[btrx][btry];
            int nbtrx = 0;
            int nbtry = 0;
  
            if (btrx > 0)
            {
                int btrv = tr[btrx - 1][btry];
                if (btrv != 0)
                    if (btrv < min){
                        min = btrv;
                        nbtrx = btrx - 1;
                        nbtry = btry;
                    }
            }
       
            if (btrx < (brdx - 1)){
                int btrv = tr[btrx + 1][btry];
                if (btrv != 0)
                    if (btrv < min){
                        min = btrv;
                        nbtrx = btrx + 1;
                        nbtry = btry;
                    }
            }
       
            if (btry > 0){
                int btrv = tr[btrx][btry - 1];
                if (btrv != 0)
                    if (btrv < min){
                        min = btrv;
                        nbtrx = btrx;
                        nbtry = btry - 1;
                    }
            }
       
            if (btry < (brdy - 1)){
                int btrv = tr[btrx][btry + 1];
                if (btrv != 0)
                    if (btrv < min){
                        min = btrv;
                        nbtrx = btrx;
                        nbtry = btry + 1;
                    }
                }
       
            l = new int[2];
            l[0] = nbtrx;
            l[1] = nbtry;
      
            retvc.insertElementAt(l, 0);
            btrx = nbtrx;
            btry = nbtry;
       
            if ((btrx == sx) && (btry == sy))
                tracemore = false;
        }// fin del while
        return retvc;
    }
    
    // metodo para empezar a correr el hilo, llamado desde el Midlet
    public void start(){
        play= true;
        new Thread(this).start();
    }
  
    //metodo para detener la aplicacion
    public void stop(){
        play= false;
    }
  
    // metodo para saber si esta jugando
    public boolean play(){
      return play;
    }
    
    // metodo abstracto de la clase Graphics
    private void paint(){
        g.setColor(0,0,0);
        g.fillRect(0, 0, w, h);
        g.setColor(204, 255, 0);
        g.fillRoundRect(w/2-92, h/2-92, 184, 184, 2, 2);
        tablero.setPosition(w/2-90, h/2-90);
        balls.setPosition(w/2-90, h/2-90);        
        printscore.setPosition(w/2-90, h/2+92);
        printballs.setPosition(w/2-90,(h/2-92)-20);
        printgameover.setPosition(w/2-120, h/2-30);
        sipierde();
        cursor.setPosition(x, y);
        lm.paint(g, 0, 0);
        flushGraphics();
    }
}
