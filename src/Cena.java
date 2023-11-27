import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import textura.Textura;

import java.awt.*;

/**
 *
 * @author ExceedEdits & Izabelle
 */
public class Cena implements GLEventListener{    
    GLU glu;
    TextRenderer textRenderer;
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    double angle = 0;
    int score = 0, hp = 3;
    float xFactor = 0, bY = 0, bX = 0, xSpeed = 2, ySpeed = 1;
    public boolean turn = true, loss = false;
    public int mode;
    public float ang;
    // Atributos
    public float limite;

    //Referencia para classe Textura
    Textura textura = null;
    //Quantidade de Texturas a ser carregada
    private int totalTextura = 1;

    //Constantes para identificar as imagens

    public static final String FACE1 = "assets/Menu.jpg";

    public int filtro = GL2.GL_LINEAR;
    public int wrap = GL2.GL_REPEAT;
    public String texto;
    
    @Override
    public void init(GLAutoDrawable drawable) {
        // Dados iniciais da cena
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();
        // Estabelece as coordenadas do SRU
        xMin = yMin = zMin = -100;
        xMax = yMax = zMax = 100;

        ang = 0;
        limite = 1;

        //Cria uma instancia da Classe Textura indicando a quantidade de texturas
        textura = new Textura(totalTextura);

        // Estabelece o renderizador de textos
        textRenderer = new TextRenderer(new Font("Comic Sans MS Negrito",
                Font.PLAIN, 30));
        
        // Habilita o zbuffer
        gl.glEnable(GL2.GL_DEPTH_TEST);

        reset();
    }

    public void reset(){
        ang = 0;

        //preenchimento
        mode = GL2.GL_FILL;
    }

    public void writeText(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase){
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        // Retorna a largura e altura da janela
        textRenderer.beginRendering(Renderer.screenWidth, Renderer.screenHeight);
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();
    }

    public void ambientLights(GL2 gl) {
        float luzAmbiente[] = {0, 0, 0, 0.5f}; // Cor
        float posicaoLuz[] = {-50.0f, 50.0f, 100.0f, 1.0f}; // Pontual

        // Define parametros de luz de numero 0
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }

    public void lightsOn(GL2 gl) {
        // Habilita a definicao da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        // Habilita o uso da iluminacao na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // Habilita a luz de numero 0
        gl.glEnable(GL2.GL_LIGHT0);
        // Especifica o tipo de tonalizacao a ser utilizado: GL_FLAT -> Reto | GL_SMOOTH -> Suave
        gl.glShadeModel(gl.GL_SMOOTH);
    }

    @Override
    public void display(GLAutoDrawable drawable) {  
        // Obtem o contexto OpenGL
        GL2 gl = drawable.getGL().getGL2();                
        // Objeto para desenho 3D
        GLUT glut = new GLUT();
        
        // Define a cor da janela (R, G, G, alpha)
        gl.glClearColor(1, 1, 1, 1);
        // Limpa a janela com a cor especificada
        //limpa o buffer de profundidade
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);       
        gl.glLoadIdentity(); // Le a matriz identidade
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); //modo dos objetos 3d
        
        // DESENHO DA CENA

        gl.glMatrixMode(GL2.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glRotatef(180, 1, 0, 0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        //não é geração de textura automática
        textura.setAutomatica(false);

        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(GL2.GL_DECAL);
        textura.setWrap(wrap);

        //cria a textura indicando o local da imagem e o índice
        textura.gerarTextura(gl, FACE1, 0);

        gl.glBegin (GL2.GL_QUADS );
        //coordenadas da Textura            //coordenadas do quads
        gl.glTexCoord2f(0.0f, limite);     gl.glVertex3f(-100.0f, -100.0f,  100.0f);
        gl.glTexCoord2f(limite, limite);     gl.glVertex3f( 100.0f, -100.0f,  100.0f);
        gl.glTexCoord2f(limite, 0.0f);     gl.glVertex3f( 100.0f,  100.0f,  100.0f);
        gl.glTexCoord2f(0.0f, 0.0f);     gl.glVertex3f(-100.0f,  100.0f,  100.0f);
        gl.glEnd();

        writeText(gl, 50, 350, Color.RED, "DEAD CLUB CITY PONG");
        writeText(gl, 50, 300, Color.white, "- Controles: para controlar a barra (carrinho) utilize");
        writeText(gl, 50, 280, Color.white, "as setas da direita e esquerda do teclado");
        writeText(gl, 50, 260, Color.white, "- Para sair do jogo pressione a tecla ESC");
        //writeText(gl, 50, 400, Color.BLACK ,"Para pausar o jogo pressione a tecla backspace, ou a barra de espaço \n");
        writeText(gl, 50, 240, Color.white, "- Para começar o jogo pressione a tecla R");
        writeText(gl, 50, 200, Color.white, "Bom jogo!");



        if (turn) {
            ambientLights(gl);
            lightsOn(gl);
        }

        // Escreve os textos
        writeText(gl, 0, 570, Color.BLUE, "Score: " + score);
        writeText(gl, 250, 570, Color.GREEN, "HP: " + hp);
        // Texto de Derrota
        if(loss){
            writeText(gl, 220, 570/2, Color.RED, "You lose!");
            writeText(gl, 170, (570/2)-40, Color.RED, "Press ESC to exit.");
            writeText(gl, 170, (570/2)-80, Color.RED, "Press R to restart.");
        }

        // Desenha a bola
        gl.glColor3f(1,0,0); // Vermelho
        gl.glPushMatrix();
        gl.glTranslated(bX, bY, 0);
        gl.glRotated(angle, 0, 0, 1);
        glut.glutSolidSphere(5, 20, 20);
        gl.glPopMatrix();

        // Desenha a barra
        gl.glColor3f(1,0,1); // Roxo
        gl.glPushMatrix();
        gl.glTranslated(xFactor,-90,0);
        gl.glRotated(30, 1, 0, 0);
        gl.glScaled(4, 1, 1);
        glut.glutSolidCube(10);
        gl.glPopMatrix();


        gl.glFlush();

        // MECANICA DE JOGO

        // Animacao da bola
        bX+=xSpeed;
        bY+=ySpeed;
        angle+=1;

        // Fisica da bola
        if(bX+5>100 || bX-5<-100 || bY-5<=-85 && bX==xFactor-20
                || bY-5<=-85 && bX==xFactor+20){
            xSpeed = -xSpeed;
        }
        if(bY+5>100 || bY-5<=-85 && bX>xFactor-20 && bX<xFactor+20){
            ySpeed = -ySpeed;
        } else if (bY-5<-100){
            if(hp > 1){
                hp--;
                bX=0;
                bY=0;
                ySpeed = -ySpeed;
            } else{  // Derrota
                hp = 0;
                xSpeed = 0;
                ySpeed = 0;
                loss = true;
            }
        }

        // Contagem de pontos
        if(bY-5==-85 && bX >xFactor-20 && bX<xFactor+20){
            score+=10;
        }

        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {    
        // Obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();
        
        // Evita a divisao por zero
        if(height == 0) 
			height = 1;
                
        // Ativa a matriz de projecao
        gl.glMatrixMode(GL2.GL_PROJECTION);      
        gl.glLoadIdentity(); // Le a matriz identidade

        // Projecao ortogonal sem a correcao do aspecto
        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);
        
        // Ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); // Le a matriz identidade
        System.out.println("Reshape: " + width + ", " + height);
    }    
       
    @Override
    public void dispose(GLAutoDrawable drawable) {}

}
