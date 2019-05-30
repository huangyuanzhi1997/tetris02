
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
 
	        
	
	
    // 20行，10列
    private final int ROW = 16;
    private final int COL = 10;

    // 每个方块边长，单位为像素
    private final int LEN = 35;

    // 游戏区域的左、上边距
    private final int LEFT_MARGIN = LEN*2;
    private final int UP_MARGIN = LEN;    
    
    // 面布大小，单位为像素
    private final int AREA_WIDTH = LEN*18;
    private final int AREA_HEIGHT = LEN*18;

    // 是否需要网格
    private boolean showGrid = true;

    // 是否彩色，将来可以作为贴图控制
    private boolean isColor = true;

    // 得分
    private int score = 0;

    // 画布
    private MyCanvas drawArea = new MyCanvas();
    // 窗口
    private JFrame f = new JFrame("姓名：黄远志          班级：软测1班           学号：1701132118");
    
    // 画图用的image
    private BufferedImage image = new BufferedImage(AREA_WIDTH, AREA_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private Graphics g = image.createGraphics();

    // 数组，用于保存背景
    private int[][] map = new int[COL][ROW];
    public  boolean game_over=false,stop;
    
    // 数组，用于保存颜色
    private Color[] color = new Color[]{Color.green, Color.red, Color.orange, Color.blue, Color.cyan, Color.yellow, Color.magenta, Color.gray};
    //默认灰色
    private final int DEFAULT = 7;
    private Color[][] mapColor = new Color[COL][ROW];

    //组件的横坐标
    int wordX = LEN*14;// 组件的横坐标
    int wordY = LEN*9; // 字的初始纵坐标

    //shape的四个参数
    private int type, state, x, y, nextType, nextState;

    //如果刚开始游戏，由于无nextType，先给type等随机一个，下为首次开始游戏的标志
    private boolean newBegin = true;
	private Object jl_bg;

   
    private void initMusic() {
		// TODO 自动生成的方法存根
		
	}


	private Object getIcon(String string) {
			// TODO 自动生成的方法存根
			return null;
		}
	

	{
    	
	//添加音乐
	InputStream   is =null;   
	AudioStream   as = null ; 
	  is  =   getClass().getResourceAsStream("真的爱你.wav");  
	try {
	as =   new   AudioStream(is);
	} 
	catch (IOException e) {}  
	AudioPlayer.player.start(as);
    }
    
    		
	
	 // 用数组来代表不同形状的下坠物，四维分别是类型Type、旋转状态State、横坐标X、纵坐标Y。画示意图即可得出坐标
    // 方块共有7种，分别以S、Z、L、J、I、O、T这7个字母的形状来命名

    private int[][][][] shape = new int[][][][]{
        // S的四种翻转状态:
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
     * 初始化界面
     */
    public void init(){
        drawArea.setPreferredSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        f.add(drawArea);
        JCheckBox gridCB = new JCheckBox("显示网格",true);
        JCheckBox colorCB = new JCheckBox("彩色方块", false);
        gridCB.setBounds(wordX, wordY-LEN,LEN,LEN);
        colorCB.setBounds(wordX, wordY-2*LEN,LEN,LEN);
        
        
        
        
        

        // paintArea();
        // 加键盘监听器
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
        // 窗口显示在屏幕正中
        // Toolkit是抽象类，只能用getDefaultToolkit()方法来获取实例。
        // getScreenSize()方法返回的是一个Dimension对象，还须用getWidth()获取宽度
        f.pack();
        int screenSizeX = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenSizeY = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int fSizeX = (int)f.getSize().getWidth();
        int fSizeY = (int)f.getSize().getHeight();
        f.setResizable(false);// 禁止改变Frame大小
        f.setBounds((screenSizeX-fSizeX)/2, (screenSizeY-fSizeY)/2, fSizeX,fSizeY );    
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setVisible(true);
    }

    private Object newInteger(int minValue) {
		// TODO 自动生成的方法存根
		return null;
	}

	private Object getResources() {
		// TODO 自动生成的方法存根
		return null;
	}

	//private JPanel getContentPane() {
		// TODO 自动生成的方法存根
		//return null;
	//}

	//private Container getLayeredPane() {
		// TODO 自动生成的方法存根
		//return null;
	//}

	/**
     * 绘图
     */
    private void paintArea(){
        //  默认黑色，填充白色
        g.setColor(Color.pink);
        g.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);
        //  方格线
        //  先画最外围
        g.setColor(Color.red);
        for (int offset = 0; offset <= 2; offset++){
            g.drawRect(LEFT_MARGIN-offset, UP_MARGIN-offset, COL*LEN+offset*2, ROW*LEN+offset*2);
        }
        
        //  如果showGrid为true则显示网格
        if(showGrid){
            g.setColor(Color.MAGENTA);
            // 9条竖线
            for (int i = 1 ; i <= 9; i++){
                g.drawLine(LEFT_MARGIN+LEN*i, UP_MARGIN, LEFT_MARGIN+LEN*i, UP_MARGIN+ROW*LEN);
            }
            // 19条横线
            for(int i = 1; i <= 19; i++){
                g.drawLine(LEFT_MARGIN, UP_MARGIN+LEN*i, LEFT_MARGIN+COL*LEN, UP_MARGIN+LEN*i);
            }
        }
        // 右上角显示下一个shape        
        int offset2 = 3;// 边框粗细
        int col = 4;// 右上角方框的列数
        int row = 4;// 行数
        g.setColor(Color.red);
        g.setFont(new Font("Microsoft YaHei Mono", Font.BOLD, 20));
        g.drawString("下一个：", wordX, LEN*1);
        int nextX = wordX;
        int nextY = LEN*1;
        //画方框
        for (int offset = 0; offset <= 2; offset++){
            g.drawRect(nextX-offset+6, nextY+10-offset, col*LEN+offset*2, row*LEN+offset*2);
         }
        //画下一次出现的下坠方块
        g.setColor(isColor?color[nextType]:color[DEFAULT]);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (shape[nextType][nextState][i][j]==1) {
                    g.fill3DRect(nextX+10+i*LEN, nextY+10+j*LEN, LEN, LEN,true);               
                }
            }
        }
        
        g.setColor(Color.MAGENTA);//设置右边字体颜色
        g.setFont(new Font("aa", Font.ITALIC, 20));//设置右边字体(如BOLD)和大小
        g.drawString("score=" + score, 480, 200);//设置分数和下面八句话的间隔
     
        Image bgImage = null;
    	
        g.drawString("离离原上草，", 480, 250);
        g.drawString("一岁一枯荣。", 480, 300);
        g.drawString("野火烧不尽，", 480, 350);
        g.drawString("春风吹又生。", 480, 400);
        g.drawString("远芳侵古道，", 480, 450);
        g.drawString("晴翠接荒城。", 480, 500);
        g.drawString("又送王孙去，", 480, 550);
        g.drawString("萋萋满别情。", 480, 600);
        
 
        /*
        g.setColor(Color.gray);
        g.setFont(new Font("Times", Font.BOLD, 15));      
        g.drawString("玩法：", wordX, wordY+LEN*2);
        g.drawString("上箭头：翻转", wordX, wordY+LEN*3);
        g.drawString("左箭头：左移", wordX, wordY+LEN*4);
        g.drawString("右箭头：右移", wordX, wordY+LEN*5);
        g.drawString("下箭头：下落", wordX, wordY+LEN*6);
        g.setFont(new Font("Times", Font.BOLD, 25));
        g.drawString("得分：" + score, wordX, wordY+LEN*8);
        
        
        
        */
        //画下坠物shape
        g.setColor(isColor?color[type]:color[DEFAULT]);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (shape[type][state][i][j]==1) {
                    g.fill3DRect(LEFT_MARGIN+(x+i)*LEN, UP_MARGIN+(y+j)*LEN, LEN, LEN,true);               
                }
            }
        }
        //画背景map
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
     * 自定义画布，重写paint()方法
     */
    private class MyCanvas extends JPanel{
        public void paint(Graphics g){
            g.drawImage(image, 0, 0, null);
        }
    }

    /**
     * 判断位置是否合法
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
     * 判断游戏是否结束
     */
    private boolean isGameOver(int type, int state, int x, int y){
        return !check(type, state, x, y);
    }

    /**
     * 新建方块
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
        // 如果游戏已结束，则重新开始
        if(isGameOver(type, state, x, y)){            
            JOptionPane.showMessageDialog(f, "GAME OVER!");
            newGame();
        }
        paintArea();
        
    }

    /**
     * 新建游戏
     */
    private void newGame(){
        newMap();
        score = 0;
        newBegin = true;
    }

    /**
     * 清空背景图
     */
    private void newMap(){
        for(int i = 0; i < COL; i++){
            Arrays.fill(map[i],0);
        }        
    }

    /**
     * 消行
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
     * 计时器所用的事件监听器
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
     * 把shape存到map的add方法
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
     * 下面为四个方向键对应的方法
     */
    private void turn(){
        int tmpState = state;
        state = (state + 1)%4;
        if (!check(type,state, x, y )) {
            state = tmpState; //不能转就什么都不做           
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
        //如果下不去则固定之
        else{
            add(type, state, x, y);
            delLine();
            newShape();
        }
        paintArea();
    }


    /**
     * 主函数
     */
    
    public static void main(String[] args){
    	
        new Tetris().init();
        
        
        
    }
    
    
    
    
}





