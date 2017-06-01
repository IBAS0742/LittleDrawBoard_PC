package com.rock.drawboard.Painter;
/*
������ߣ� ��������ʱ�ظ�
�����Ȩ���������У������˿��Զ���������޸ģ�����ʹ��������룬������ʹ���뱣��������Ϣ��
*/

import com.rock.drawboard.board.MyCanvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;


// ������
public class Rectangle extends Shape {
	// �������Ͻ�λ�õ����꣬x��yֵ
	private int x;
	private int y;
	
	// ���εĳ��Ϳ�
	private int width;
	private int height;
	private boolean fill = false;   //�Ƿ�ʵ��
	

	// ���췽��
	public Rectangle(Color ColorPen,Color ColorBrush,int LineWide,
			int x, int y, int width, int height ,boolean Fill) {
		super(ColorPen,ColorBrush,LineWide);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		fill = Fill;
	}
//	�õ����εľ��α߽�
	public void GetRect(Point point1,Point point2)
	{
		point1.x = this.x;
		point1.y = this.y;
		point2.x = this.x + this.width;
		point2.y = this.y + this.height;
	}
//	�ж��Ƿ񱻵�ѡ�еĺ���
	public boolean IsPoint(int x,int y,float j1)
	{
		float xx,x1=0,y1=0,x2=0,y2=0;
	
		Point point1 = new Point();
		Point point2 = new Point();
		GetRect(point1,point2);
		x1 = point1.x;
		y1 = point1.y;
		x2 = point2.x;
		y2 = point2.y;
		if(!fill)
		{
			xx = super.PointLine(x,y,this.x,this.y+this.height,this.x,this.y );
			if(xx < j1)
				return true;
			xx = super.PointLine(x,y,this.x,this.y + this.height,this.x+this.width,this.y);
			if(xx < j1)
				return true;
			xx = super.PointLine(x,y,this.x,this.y + this.height,this.x+this.width,this.y + this.height);
			if(xx < j1)
				return true;
			xx = super.PointLine(x,y,this.x+this.width,this.y,this.x+this.width,this.y + this.height);
			if(xx < j1)
				return true;

		}
		else
		{
			if((x >= x1-j1 && x <= x2+j1 && y >= y1-j1 && y <= y2+j1))
				return true;
		}
		return false;
		
	}
	 //�ж��Ǿ��λ�������
	public boolean IsRectangle()
	{
		return !fill;
	}
	
	
	
	// ������
	public void draw(Graphics g,int m_DrawMode,Color bgColor) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.white);
		g2d.setColor(super.m_ColorPen);
		g2d.setStroke(MyCanvas.STROKES[super.m_LineWide]);
	
		if(m_DrawMode == 0)   //������ͼ)
		{
			if(fill == false){
				g2d.drawRect(this.x, this.y,this.width,this.height);
			}
			else
			{
				g2d.drawRect(this.x, this.y,this.width,this.height);
				g2d.setColor(super.m_ColorBrush);
				g2d.fillRect(this.x, this.y,this.width,this.height);
			}
		}
		else  //ѡ��ʱ������ʾ
		{
			Stroke thindashed = new BasicStroke(2.0f,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,1.0f,
					new float[]{8.0f,3.0f,2.0f,3.0f},
					0.0f);
			g2d.setStroke(thindashed);
			g.setColor(Color.RED);
			if(fill == false){
				g2d.drawRect(this.x, this.y,this.width,this.height);
			}
			else
			{
				g2d.drawRect(this.x, this.y,this.width,this.height);
				g2d.setColor(Color.RED);
				g2d.fillRect(this.x, this.y,this.width,this.height);
			}
		}
	}
}

