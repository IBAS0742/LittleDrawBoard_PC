package com.rock.drawboard.board;



import com.rock.drawboard.Painter.*;
import com.rock.drawboard.Painter.Command;
import com.rock.drawboard.Painter.Rectangle;
import com.rock.drawboard.Painter.Shape;
import com.rock.drawboard.module.*;
import com.rock.drawboard.socket.ServerAction;


import javax.swing.*;
import java.awt.*;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MyCanvas extends JPanel implements MouseMotionListener, MouseListener {

	public float blc=1;  //��ͼʱ���������Ǳ�����
	Dimension size = getSize();
	int m_wScreen=828,m_hScreen=426;  // ��ǰ��ͼ�ĸ߶�and���
    Point ZoomPoint = new Point();
    PaintBoard pd = null;
	// ��¼���������ʼλ�ã�x��y����
	int beginX = 0;
	int beginY = 0;
	// ��¼������ĵ�ǰλ�ã�x��y����
	int currentX = 0;
	int currentY = 0;
	int PastX  = 0;
	int PastY = 0;
	//��¼��Ƥ�������
	int eraser = 0;
	int clear = 0;
    //�Ƿ����
	boolean fill = false;
	// ��ʾ��ǰ����Ƿ񱻰���
	boolean bMousePressing = false;
	Pencil pencil = null;
	JTextArea statusTextArea = null;

	private ArrayList Shapes = new ArrayList(), DelShapes = new ArrayList();
	BufferedImage offScreenImg = null;
	Graphics2D offScreenG = null;
	
	// ��¼��ǰ������ǻ�Բ�����ߡ����ǻ�����
	private int command = Command.PENCIL;
	// ���ʵ���ɫ
	private Color fgColor = Color.BLACK;
	private Color bgColor = Color.WHITE;
	private Color brushColor = Color.BLACK;

    public static final Stroke[] STROKES = new Stroke[] {
        new BasicStroke(5.0f),
        new BasicStroke(8.0f),
        new BasicStroke(10.0f),
        new BasicStroke(15f),
        new BasicStroke(20f)
    };

    public static final int[] ERASER_STROKES = new int[] {
       15,20,30,50,100
    };

    private int strokeIndex = 0, eraserIndex = 0;
    private int n_GraphSelect = 0;     //ѡ��ͼ��Ԫ�ص�����

    //���ѡ��ͼ��λ��
   private  int[] GraphSelect = new int[200];


	//���û��嵱ǰ�Ļ�ͼ����
		public String getCommandString(int command){
			switch(command){
			case Command.LINE:
				return "Line";
				
			case Command.CIRCLE:
				return "Circle";
				
			case Command.PENCIL:
				return "Pencil";
				
			case Command.RECTANGLE:
				return "Rectangle";
				
			case Command.ERASER:
				return "ERASER";
			case Command.SELECT:
				return "SELECT";
			case Command.DELETE:
				return "DELETE";
			case Command.UNSELECTED:
				return "UNSELECTED";
			case Command.TEXTINSERT:
				return "TEXTINSERT";
				
			}	
			return "none";
		}


//  ʵ�ֶ�����ͼ��Ԫ���ж��Ƿ�ѡ��
   public int  PointSelect(int x,int y,float j1)
    {

    	int i = 0;
    	Shape shape = null;
    	for(; i < Shapes.size();i++)
    	{
    		shape = (Shape)Shapes.get(i);
    		if(shape.IsPoint(x,y,j1))
    		{
    			return i;
    		}

    	}
    	return -1;
    }
// �洢ѡ��ͼ��Ԫ��
   public boolean AddSelectList(int Index)
   {
   	for(int i = 0; i < n_GraphSelect; i++)
   	{
   		if(Index == GraphSelect[i])
   			return false;
   	}
   	
   	GraphSelect[n_GraphSelect++] = Index;
   	return true;
   }
// ����ͼ��Ԫ��������ʾ
   public void DrawGraph(Graphics g,int Index)
   {


	   if(Index < Shapes.size()){
		   	Shape shape =(Shape) Shapes.get(Index);
		   	shape.draw(g,1,bgColor);
	   }
   }
    //���û�ˢ��ɫ
   public void setBrushColor(Color c){
	   brushColor = c;
   }
   
   //ɾ��ָ����ŵ��ѻ�ͼ��
   public void Delete(int Index)
   {

	   if(Index < Shapes.size()){
		   Shape shape =(Shape) Shapes.get(Index);
		   Shapes.remove(Index);
		   DelShapes.add(shape);
	   }
   }
   
   
   //����ͼ��ΪJPG��ʽ
   public void SaveFile(String fileName) throws Exception {
	   ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
	   out.writeObject(Shapes);    //д�洢ͼ�ζ��������
	   out.writeObject(this.bgColor);  //д������ɫ
	   out.close();
   }
   
 //  ��jwd��ʽ�ļ�

 public void OpenFile(String fileName) throws Exception{
	DelShapes.clear();  //��մ洢ɾ��ͼ�ζ��������
 	ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
 	Shapes =(ArrayList) (in.readObject()); //���洢ͼ�ζ��������
 	this.bgColor = (Color)(in.readObject());//��������ɫ
 	in.close();
 	repaint();
   }
	/**
	 * ���췽��
	 */
	public MyCanvas(JTextArea statusTextArea, PaintBoard pd) {
		this.pd = pd;
        strokeIndex = 0;
        eraserIndex = 0;
        this.statusTextArea = statusTextArea;
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        setForeground(Color.black);
        setBackground(Color.white);
       setPreferredSize(new Dimension(900,400));
		// ����¼�������
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

	}

	   public void setStrokeIndex(int i) {
	        if (i < 0 || i > 4)
	            throw new IllegalArgumentException("Invaild Weight Specified!");
	        strokeIndex = i;
	    }
	    
	    public void setEraserIndex(int i) {
	        if (i < 0 || i > 4)
	            throw new IllegalArgumentException("Invaild Size Specified!");
	        eraserIndex = i;
	    }
	    
	    public void clearBoard() {
		isClear = true;
	    	Shapes.clear();
	    	DelShapes.clear();
	    	n_GraphSelect = 0;
	    	repaint();
	    }
	
	/**
	 * ����������ȡ���ոջ���ͼ�Ρ�
	 */
	private boolean isClear;
	public void undo() {
		isClear = true;
		if (Shapes.size() > 0) {

			// ��vShapes�е���󻭵�һ��ͼ��ȡ��
			Object shape = Shapes.remove(Shapes.size() - 1);
			// ���뵽vDelShapes��
			DelShapes.add(shape);
			// �ػ�����

			repaint();
		}
	}
	/**
	 * �ָ�������ȡ���ոճ�����ͼ��
	 */
	public void redo() {
		isClear = true;
		if (DelShapes.size() > 0) {
			// ��vDelShapes�����һ����ɾ����ͼ��ȡ��
			Object shape = DelShapes.remove(DelShapes.size() - 1);
			// �ŵ�vShapes��
			Shapes.add(shape);
			// �ػ�����
			repaint();
		}
	}
	/**
	 * ����������ߡ���Բ��������
	 * @param command
	 */
	public void setCommand(int command) {
		this.command = command;

	}
	public void setCommandToAndroid(int command) {
		this.command = command;
	}
	/**
	 * ���û�����ɫ
	 * @param color
	 */
	public void setForegroundColor(Color color) {
		this.fgColor = color;
	}
	
	/**
	 * ���ñ�����ɫ
	 * @param color
	 */
	public void setBackgroundColor(Color color) {
		this.bgColor = color;
	}
	public   Color getBackgroundColor() {
		return bgColor;
	}
	
	public void setn_GraphSelect(int n) {
		n_GraphSelect = n;
		Graphics g=getGraphics();
		paint(g);
	}
	
	public void setSelectDel() {
		for(int i = 0; i < n_GraphSelect; i++)
			Delete(GraphSelect[i]);
//		System.out.println("n_GraphSelect:" +canvases[canvas_control].n_GraphSelect);
		n_GraphSelect = 0;
		Graphics g=getGraphics();
		paint(g);
	}
	/**
	 * ��java��repaint()��ͨ������������ʵ��ˢ�¹��ܵģ�����������public void update()��ˢ����Ļ��
	 * ����ٵ���paint(Graphcis g)���ػ���Ļ��������������˸���ر���һЩ��Ҫ�ػ������ĳ���
	 * �����һ��ͼ�������ȫ������һ��ͼ��Ļ����������дupdate����������������˸�� 
	 * public void update(Graphics g){ paint(g) }ͬ������repaint()�ػ���Ļ��
	 * ����ֱ����д������repaint������Graphics g=getGraphics(); paint(g)����ʵ���ػ���Ļ��
	 */
	/**
	 * ��дupdate������������˸��
	 */

	/**
	 * ������
	 */
	public void paint(Graphics g1) {
		super.paint(g1);
		Graphics2D g = (Graphics2D) g1;
		/** �������ϵ�ͼ�� **/
		int i = 0;
		Shape  shape = null;
		for(i = 0; i < Shapes.size(); i++)
		{
			shape = (Shape) Shapes.get(i);
			shape.draw(g, 0,bgColor);
		}
		for(i = 0; i < n_GraphSelect; i++)
	   	{
	   		DrawGraph(g,GraphSelect[i]);
	   	}

		// �����ǰ��껹û���ɿ�
		if (bMousePressing) {
			// ���û�����ɫ
			g.setColor(fgColor);
			switch (command) {
			case Command.LINE:
				// ���ߣ��������ʼλ�ú͵�ǰλ��֮�仭һ��ֱ��
				g.drawLine(beginX, beginY, currentX, currentY);
				break;
			case Command.PENCIL:
				// ����
				pencil.draw(g, 0,bgColor);
				break;
			case Command.RECTANGLE:

					if (currentX < beginX) {
					// �����굱ǰλ������ʼλ�õ����Ϸ���������굱ǰλ��Ϊ���ε����Ͻ�λ�á�
						if (currentY < beginY) {
						g.drawRect(currentX, currentY, beginX - currentX, beginY
								- currentY);
						} else {
						// �����굱ǰλ������ʼλ�õ����·���
						// ���Ե�ǰλ�õĺ��������ʼλ�õ�������Ϊ���ε����Ͻ�λ��
						g.drawRect(currentX, beginY, beginX - currentX, currentY
								- beginY);
						}
					} else {
					// �����굱ǰλ������ʼλ�õ����Ϸ���
					// ���������ʼλ�õĺ�����͵�ǰ��������Ϊ���ε����Ͻ�λ��
						if (currentY < beginY) {
							g.drawRect(beginX, currentY, currentX - beginX, beginY
								- currentY);
						} else {
						// �����굱ǰλ������ʼλ�õ����·���������ʼλ��Ϊ���ε����Ͻ�λ��
							g.drawRect(beginX, beginY, currentX - beginX, currentY - beginY);
						}
					}

				/*int width=Math.abs(beginX-currentX);//���������
				int height=Math.abs(beginY-currentY);//���������
				int startX=trueStart(beginX, currentX, width);
				int startY=trueStart(beginY, currentX, height);
				Rectangle2D.Float rectangle = new Rectangle2D.Float(startX,startY,width,height);
				g.setPaint(Color.black);
				g.draw(rectangle);*/
				break;
			case Command.CIRCLE:
				// ��Բ����ȡ�뾶���뾶����a*a + b*b��ƽ����
					int radius = (int) Math.sqrt((beginX - currentX)
							* (beginX - currentX) + (beginY - currentY)
							* (beginY - currentY));
				if(fill == false)
				{
				// ��Բ��
					g.drawArc(beginX - radius / 2, beginY - radius / 2, radius, radius, 0, 360);
				}
				else{
					g.fillArc(beginX - radius / 2, beginY - radius / 2, radius, radius, 0, 360);
				}


				break;
			case Command.ERASER:
				int erasewidth = ERASER_STROKES[eraserIndex];
				g.setColor(bgColor);
				g.fillRect(currentX,currentY,erasewidth,erasewidth);
				Eraser erase = new Eraser(fgColor,brushColor,eraserIndex,currentX,currentY);
				Shapes.add(erase);

			}//End switch(command)
		}
isClear = false;
	}
	private int trueStart(int start,int end,int distant){
		if(end-start<0){
			start-=distant;
		}
		return start;
	}
	// MouseListener�ӿڶ���ķ�����������굥���¼���
	public void mouseClicked(MouseEvent e) {
		actionClicked(e.getX(),e.getY());
	}
	public void actionClicked(int x,int y) {
		switch(command){
			case Command.SELECT:
				currentX =  x;
				currentY =  y;
				float j1 = 8;
				int pb = -1;
				pb = PointSelect(currentX,currentY,j1);
				if(pb != -1)
				{
					AddSelectList(pb);
				}
				break;
			case Command.TEXTINSERT:
				Point aPoint = new Point(x, y);
				Font f = new Font("", 0, 10);
				TextInput ti = new TextInput(pd, true, null);
				if (ti.showDialog(pd, null, f) == 0) {
					CusString aCusShape = new CusString(ti.str, (int) aPoint.getX(),
							(int) aPoint.getY());
					aCusShape.setFont(ti.aFont);
					aCusShape.setColor(this.fgColor);
					Shapes.add(aCusShape);
					Graphics g=getGraphics();
					paint(g);
				}
				break;
		}
	}
	// MouseListener�ӿڶ���ķ�����������갴���¼���
	public void mousePressed(MouseEvent e) {
		ServerAction.sendData(new DataPackage(DataPackage.DataType.POINT,new com.rock.drawboard.module.Point(e.getX(),e.getY(),PaintBoard.ACTION_DOWN)));
		actionDown(e.getX(),e.getY());
	}
	public void actionDown(int x,int y) {
		// ���������ʼλ��
		beginX = x;
		beginY = y;
		statusTextArea.setText("The position of cursor:  x=" + currentX + ", y=" + currentY + "\n The current draw tool: " + getCommandString(command));
		if(command == Command.PENCIL){
			pencil = new Pencil(this.fgColor, this.bgColor,this.strokeIndex,  x, y);
		}
		// ����bMousePressingΪ�棬��ʾ��갴���ˡ�
		bMousePressing = true;
	}
	//MouseListener�ӿڶ���ķ�������������ɿ��¼���
	public void mouseReleased(MouseEvent e) {
		ServerAction.sendData(new DataPackage(DataPackage.DataType.POINT,new com.rock.drawboard.module.Point(e.getX(),e.getY(),PaintBoard.ACTION_UP)));
		actionUp(e.getX(),e.getY());
	}
	public void actionUp(int x,int y) {
		// ��ȡ��굱ǰλ��
		currentX = x;
		currentY = y;
		statusTextArea.setText("The position of cursor:  x=" + currentX + ", y=" + currentY + "\n The current draw tool: " + getCommandString(command));
		// ��������Ѿ��ɿ��ˡ�
		bMousePressing = false;

		// �ɿ����ʱ�����ոջ���ͼ�α��浽vShapes��
		switch (command) {
			case Command.LINE:
				// �½�ͼ��

				Line line = new Line(fgColor,brushColor,strokeIndex,beginX,beginY,currentX,currentY);
				Shapes.add(line);
				break;
			case Command.PENCIL:

				pencil.setPoints(x, y);
				Shapes.add(pencil);
				pencil = null;
				break;
			case Command.RECTANGLE:
				Rectangle rectangle = null;
				if (currentX < beginX) {
					if (currentY < beginY) {

						rectangle = new Rectangle(fgColor,brushColor,strokeIndex,beginX,beginY,beginX - currentX,beginY - currentY, fill);
					} else {

						rectangle = new Rectangle(fgColor,brushColor,strokeIndex,currentX,beginY,beginX - currentX,currentY - beginY, fill);
					}
				} else {
					if (currentY < beginY) {

						rectangle = new Rectangle(fgColor,brushColor,strokeIndex,beginX,currentY,currentX - beginX,beginY - currentY, fill);
					} else {

						rectangle = new Rectangle(fgColor,brushColor,strokeIndex,beginX,beginY,currentX - beginX,currentY - beginY, fill);
					}
				}
				Shapes.add(rectangle);
				break;
			case Command.CIRCLE:
				int radius = (int) Math.sqrt((beginX - currentX)
						* (beginX - currentX) + (beginY - currentY) * (beginY - currentY));

				Circle circle = new Circle(fgColor,brushColor,strokeIndex,beginX - radius / 2,beginY - radius / 2, radius, fill);

				Shapes.add(circle);
				break;
		} //End switch(command)

		repaint();
	}
	//MouseListener�ӿڶ���ķ�����������������¼���
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}
	
	//MouseListener�ӿڶ���ķ�������������Ƴ��¼���
	public void mouseExited(MouseEvent e) {
		// do nothing
	}
	
	//MouseListener�ӿڶ���ķ�����������갴ס�����϶��¼���
	public void mouseDragged(MouseEvent e) {
		ServerAction.sendData(new DataPackage(DataPackage.DataType.POINT,new com.rock.drawboard.module.Point(e.getX(),e.getY(),PaintBoard.ACTION_MOVE)));
		actionMove(e.getX(),e.getY());
	}
	public void actionMove(int x,int y) {
		// ��ס����϶�ʱ�����ϵĻ�ȡ��굱ǰλ�ã����ػ�����
		PastX = currentX;
		PastY = currentY;
		currentX = x;
		currentY = y;
		statusTextArea.setText("The position of cursor:  x=" + currentX + ", y=" + currentY + "\n The current draw tool: " + getCommandString(command));
		if(command == Command.PENCIL){
			pencil.setPoints(x, y);
		}
		Graphics g=getGraphics();
		//paint(g);
		repaint();
	}
	  public void SaveJpg() {
		  	size = getSize();
		  	m_wScreen = size.width;
			m_hScreen = size.height;
		  	offScreenImg = new BufferedImage(m_wScreen, m_hScreen, BufferedImage.TYPE_INT_RGB);  
	        offScreenG = offScreenImg.createGraphics();
	        offScreenG.setColor(this.bgColor);
		    offScreenG.fillRect(0, 0, this.getWidth(), this.getHeight());
		    int i = 0;
		    Shape shape = null;
		    for (;i < Shapes.size();i++)
		    {
		    	shape = (Shape)Shapes.get(i);
		    	shape.draw(offScreenG,0, bgColor);
		    }
		  
		  }
	  
	  
	  
	
	//MouseListener�ӿڶ���ķ�������������ƶ��¼���
	public void mouseMoved(MouseEvent e) {
		// do nothing
	}




}
