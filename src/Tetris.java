
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.Timer;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.JOptionPane;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;
 
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
 
public class Tetris extends JFrame{
 
	        
	
	
    // 20�У�10��
    private final int ROW = 16;
    private final int COL = 10;

    // ÿ������߳�����λΪ����
    private final int LEN = 35;

    // ��Ϸ��������ϱ߾�
    private final int LEFT_MARGIN = LEN*2;
    private final int UP_MARGIN = LEN;    
    
    // �沼��С����λΪ����
    private final int AREA_WIDTH = LEN*18;
    private final int AREA_HEIGHT = LEN*18;

    // �Ƿ���Ҫ����
    private boolean showGrid = true;

    // �Ƿ��ɫ������������Ϊ��ͼ����
    private boolean isColor = true;

    // �÷�
    private int score = 0;

    // ����
    private MyCanvas drawArea = new MyCanvas();
    // ����
    private JFrame f = new JFrame("��������Զ־          �༶�����1��           ѧ�ţ�1701132118");
    
    // ��ͼ�õ�image
    private BufferedImage image = new BufferedImage(AREA_WIDTH, AREA_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private Graphics g = image.createGraphics();

    // ���飬���ڱ��汳��
    private int[][] map = new int[COL][ROW];
    public  boolean game_over=false,stop;
    
    // ���飬���ڱ�����ɫ
    private Color[] color = new Color[]{Color.green, Color.red, Color.orange, Color.blue, Color.cyan, Color.yellow, Color.magenta, Color.gray};
    //Ĭ�ϻ�ɫ
    private final int DEFAULT = 7;
    private Color[][] mapColor = new Color[COL][ROW];

    //����ĺ�����
    int wordX = LEN*14;// ����ĺ�����
    int wordY = LEN*9; // �ֵĳ�ʼ������

    //shape���ĸ�����
    private int type, state, x, y, nextType, nextState;

    //����տ�ʼ��Ϸ��������nextType���ȸ�type�����һ������Ϊ�״ο�ʼ��Ϸ�ı�־
    private boolean newBegin = true;
	private Object jl_bg;

   
    private void initMusic() {
		// TODO �Զ����ɵķ������
		
	}


	private Object getIcon(String string) {
			// TODO �Զ����ɵķ������
			return null;
		}
	

	{
    	
	//�������
	InputStream   is =null;   
	AudioStream   as = null ; 
	  is  =   getClass().getResourceAsStream("��İ���.wav");  
	try {
	as =   new   AudioStream(is);
	} 
	catch (IOException e) {}  
	AudioPlayer.player.start(as);
    }
    
    		
	
	 // ������������ͬ��״����׹���ά�ֱ�������Type����ת״̬State��������X��������Y����ʾ��ͼ���ɵó�����
    // ���鹲��7�֣��ֱ���S��Z��L��J��I��O��T��7����ĸ����״������

    private int[][][][] shape = new int[][][][]{
        // S�����ַ�ת״̬:
        { { {0,1,0,0}, {1,1,0,0}, {1,0,0,0}, {0,0,0,0} }, 
        { {0,0,0,0}, {1,1,0,0}, {0,1,1,0}, {0,0,0,0} }, 
        { {0,1,0,0}, {1,1,0,0}, {1,0,0,0}, {0,0,0,0} }, 
        { {0,0,0,0}, {1,1,0,0}, {0,1,1,0}, {0,0,0,0} } },
        // Z:
        { { {1,0,0,0}, {1,1,0,0}, {0,1,0,0}, {0,0,0,0} }, 
        { {0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0} }, 
        { {1,0,0,0}, {1,1,0,0}, {0,1,0,0}, {0,0,0,0} }, 
        { {0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0} } },
        // L:
        { { {0,0,0,0}, {1,1,1,0}, {0,0,1,0}, {0,0,0,0} }, 
        { {0,0,0,0}, {0,1,1,0}, {0,1,0,0}, {0,1,0,0} }, 
        { {0,0,0,0}, {0,1,0,0}, {0,1,1,1}, {0,0,0,0} }, 
        { {0,0,1,0}, {0,0,1,0}, {0,1,1,0}, {0,0,0,0} } },
        // J:
        { { {0,0,0,0}, {0,0,1,0}, {1,1,1,0}, {0,0,0,0} }, 
        { {0,0,0,0}, {0,1,1,0}, {0,0,1,0}, {0,0,1,0} }, 
        { {0,0,0,0}, {0,1,1,1}, {0,1,0,0}, {0,0,0,0} }, 
        { {0,1,0,0}, {0,1,0,0}, {0,1,1,0}, {0,0,0,0} } },
        // I:
        { { {0,1,0,0}, {0,1,0,0}, {0,1,0,0}, {0,1,0,0} }, 
        { {0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0} }, 
        { {0,1,0,0}, {0,1,0,0}, {0,1,0,0}, {0,1,0,0} }, 
        { {0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0} } },
        // O:
        { { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0} }, 
        { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  }, 
        { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  }, 
        { {0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}  } },
        // T:
        { { {0,1,0,0}, {1,1,0,0}, {0,1,0,0}, {0,0,0,0} }, 
        { {0,0,0,0}, {1,1,1,0}, {0,1,0,0}, {0,0,0,0} }, 
        { {0,1,0,0}, {0,1,1,0}, {0,1,0,0}, {0,0,0,0} }, 
        { {0,1,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0} } },

    };

    /**
     * ��ʼ������
     */
    public void init(){
        drawArea.setPreferredSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        f.add(drawArea);
        JCheckBox gridCB = new JCheckBox("��ʾ����",true);
        JCheckBox colorCB = new JCheckBox("��ɫ����", false);
        gridCB.setBounds(wordX, wordY-LEN,LEN,LEN);
        colorCB.setBounds(wordX, wordY-2*LEN,LEN,LEN);
        
        
        
        
        

        // paintArea();
        // �Ӽ��̼�����
        f.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        turn();
                        break;
                    case KeyEvent.VK_LEFT:
                        left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        right();
                        break;
                    case KeyEvent.VK_DOWN:
                        down();
                        break;
                }                
            }
        });   
        Timer timer = new Timer(1000, new timerListener());
        newShape();
        timer.start();
        // ������ʾ����Ļ����
        // Toolkit�ǳ����ֻ࣬����getDefaultToolkit()��������ȡʵ����
        // getScreenSize()�������ص���һ��Dimension���󣬻�����getWidth()��ȡ���
        f.pack();
        int screenSizeX = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenSizeY = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int fSizeX = (int)f.getSize().getWidth();
        int fSizeY = (int)f.getSize().getHeight();
        f.setResizable(false);// ��ֹ�ı�Frame��С
        f.setBounds((screenSizeX-fSizeX)/2, (screenSizeY-fSizeY)/2, fSizeX,fSizeY );    
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setVisible(true);
    }

    private Object newInteger(int minValue) {
		// TODO �Զ����ɵķ������
		return null;
	}

	private Object getResources() {
		// TODO �Զ����ɵķ������
		return null;
	}

	//private JPanel getContentPane() {
		// TODO �Զ����ɵķ������
		//return null;
	//}

	//private Container getLayeredPane() {
		// TODO �Զ����ɵķ������
		//return null;
	//}

	/**
     * ��ͼ
     */
    private void paintArea(){
        //  Ĭ�Ϻ�ɫ������ɫ
        g.setColor(Color.pink);
        g.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);
        //  ������
        //  �Ȼ�����Χ
        g.setColor(Color.red);
        for (int offset = 0; offset <= 2; offset++){
            g.drawRect(LEFT_MARGIN-offset, UP_MARGIN-offset, COL*LEN+offset*2, ROW*LEN+offset*2);
        }
        
        //  ���showGridΪtrue����ʾ����
        if(showGrid){
            g.setColor(Color.MAGENTA);
            // 9������
            for (int i = 1 ; i <= 9; i++){
                g.drawLine(LEFT_MARGIN+LEN*i, UP_MARGIN, LEFT_MARGIN+LEN*i, UP_MARGIN+ROW*LEN);
            }
            // 19������
            for(int i = 1; i <= 19; i++){
                g.drawLine(LEFT_MARGIN, UP_MARGIN+LEN*i, LEFT_MARGIN+COL*LEN, UP_MARGIN+LEN*i);
            }
        }
        // ���Ͻ���ʾ��һ��shape        
        int offset2 = 3;// �߿��ϸ
        int col = 4;// ���ϽǷ��������
        int row = 4;// ����
        g.setColor(Color.red);
        g.setFont(new Font("Microsoft YaHei Mono", Font.BOLD, 20));
        g.drawString("��һ����", wordX, LEN*1);
        int nextX = wordX;
        int nextY = LEN*1;
        //������
        for (int offset = 0; offset <= 2; offset++){
            g.drawRect(nextX-offset+6, nextY+10-offset, col*LEN+offset*2, row*LEN+offset*2);
         }
        //����һ�γ��ֵ���׹����
        g.setColor(isColor?color[nextType]:color[DEFAULT]);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (shape[nextType][nextState][i][j]==1) {
                    g.fill3DRect(nextX+10+i*LEN, nextY+10+j*LEN, LEN, LEN,true);               
                }
            }
        }
        
        g.setColor(Color.MAGENTA);//�����ұ�������ɫ
        g.setFont(new Font("aa", Font.ITALIC, 20));//�����ұ�����(��BOLD)�ʹ�С
        g.drawString("score=" + score, 480, 200);//���÷���������˾仰�ļ��
     
        Image bgImage = null;
    	
        g.drawString("����ԭ�ϲݣ�", 480, 250);
        g.drawString("һ��һ���١�", 480, 300);
        g.drawString("Ұ���ղ�����", 480, 350);
        g.drawString("���紵������", 480, 400);
        g.drawString("Զ���ֹŵ���", 480, 450);
        g.drawString("���ӻĳǡ�", 480, 500);
        g.drawString("��������ȥ��", 480, 550);
        g.drawString("���������顣", 480, 600);
        
 
        /*
        g.setColor(Color.gray);
        g.setFont(new Font("Times", Font.BOLD, 15));      
        g.drawString("�淨��", wordX, wordY+LEN*2);
        g.drawString("�ϼ�ͷ����ת", wordX, wordY+LEN*3);
        g.drawString("���ͷ������", wordX, wordY+LEN*4);
        g.drawString("�Ҽ�ͷ������", wordX, wordY+LEN*5);
        g.drawString("�¼�ͷ������", wordX, wordY+LEN*6);
        g.setFont(new Font("Times", Font.BOLD, 25));
        g.drawString("�÷֣�" + score, wordX, wordY+LEN*8);
        
        
        
        */
        //����׹��shape
        g.setColor(isColor?color[type]:color[DEFAULT]);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (shape[type][state][i][j]==1) {
                    g.fill3DRect(LEFT_MARGIN+(x+i)*LEN, UP_MARGIN+(y+j)*LEN, LEN, LEN,true);               
                }
            }
        }
        //������map
        for(int i = 0; i < COL; i++){
            for(int j = 0; j < ROW; j++){
                if (map[i][j] == 1) {
                    g.setColor(mapColor[i][j]);
                    g.fill3DRect(LEFT_MARGIN+i*LEN, UP_MARGIN+j*LEN, LEN, LEN,true);
                }
            }
        }

        drawArea.repaint();
    }

    /**
     * �Զ��廭������дpaint()����
     */
    private class MyCanvas extends JPanel{
        public void paint(Graphics g){
            g.drawImage(image, 0, 0, null);
        }
    }

    /**
     * �ж�λ���Ƿ�Ϸ�
     */
    private boolean check(int type, int state, int x, int y){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if ( (shape[type][state][i][j] == 1) && ( (x+i>=COL) || (x+i<0 ) || (y+j>=ROW) || (map[x+i][y+j]==1) ) ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * �ж���Ϸ�Ƿ����
     */
    private boolean isGameOver(int type, int state, int x, int y){
        return !check(type, state, x, y);
    }

    /**
     * �½�����
     */
    private void newShape(){
        Random rand = new Random();
        if(newBegin){
            type = rand.nextInt(7); 
            state = rand.nextInt(4); 
            newBegin = false;
        }
        else{
            type = nextType;        
            state = nextState;
        }        
        nextType = rand.nextInt(7); 
        nextState = rand.nextInt(4);        
        x = 3;
        y = 0;
        // �����Ϸ�ѽ����������¿�ʼ
        if(isGameOver(type, state, x, y)){            
            JOptionPane.showMessageDialog(f, "GAME OVER!");
            newGame();
        }
        paintArea();
        
    }

    /**
     * �½���Ϸ
     */
    private void newGame(){
        newMap();
        score = 0;
        newBegin = true;
    }

    /**
     * ��ձ���ͼ
     */
    private void newMap(){
        for(int i = 0; i < COL; i++){
            Arrays.fill(map[i],0);
        }        
    }

    /**
     * ����
     */
    private void delLine(){
        boolean flag = true;
        int addScore = 0;
        for(int j = 0; j < ROW; j++){
            flag = true;
            for( int i = 0; i < COL; i++){
                if (map[i][j]==0){
                    flag = false;
                    break;
                }
            }
            if(flag){
                addScore += 10;
                for(int t = j; t > 0; t--){
                    for(int i = 0; i <COL; i++){
                        map[i][t] = map[i][t-1];                        
                    }
                }
            }        
        }
        score += addScore*addScore/COL;
    }

    /**
     * ��ʱ�����õ��¼�������
     */
    private class timerListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(check(type, state , x, y+1) ){
                y = y +1;
            }
            else{
               add(type, state, x, y);
               delLine();
               newShape();
            }
            paintArea();
        }
    }

    /**
     * ��shape�浽map��add����
     */
    private void add(int type, int state, int x, int y){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4 ; j++){
                if((y+j<ROW)&&(x+i<COL)&&(x+i>=0)&&(map[x+i][y+j]==0)){
                    map[x+i][y+j]=shape[type][state][i][j];
                    mapColor[x+i][y+j]=color[isColor?type:DEFAULT];
                }
            }
        }
    }

    /**
     * ����Ϊ�ĸ��������Ӧ�ķ���
     */
    private void turn(){
        int tmpState = state;
        state = (state + 1)%4;
        if (!check(type,state, x, y )) {
            state = tmpState; //����ת��ʲô������           
        }
        paintArea();
    }

    private void left(){
        if(check(type,state, x-1, y)){
            --x;
        }
        paintArea();
    }

    private void right(){
        if (check(type,state, x+1, y)) {
            ++x;
        }
        paintArea();
    }

    private void down(){
        if (check(type,state, x, y+1)) {
            ++y;
        }
        //����²�ȥ��̶�֮
        else{
            add(type, state, x, y);
            delLine();
            newShape();
        }
        paintArea();
    }


    /**
     * ������
     */
    
    public static void main(String[] args){
    	
        new Tetris().init();
        
        
        
    }
    
    
    
    
}





