package com.rock.drawboard.Painter;

/*
������ߣ� ��������ʱ�ظ�
�����Ȩ���������У������˿��Զ���������޸ģ�����ʹ��������룬������ʹ���뱣��������Ϣ��
*/
import com.rock.drawboard.board.MyCanvas;

import java.awt.Color;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.*;


/**
 * ����
 */
public class Line extends Shape {
	// �ߵ����λ�ã�x��y����
	private int x1;
	private int y1;
	
	// �ߵ��յ�λ�ã�x��y����
	private int x2;
	private int y2;

	public Line(){
		super(Color.BLACK,Color.BLACK,1);
	}
	//private ;
	
	// ���췽��
	public Line(Color ColorPen,Color ColorBrush,int LineWide,
			int x1, int y1, int x2, int y2) {
		super(ColorPen,ColorBrush,LineWide);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
//	�õ�ֱ�ߵľ��α߽�
	public void GetRect(Point point1,Point point2)
	{
		point1.x = Math.min(x1,x2);		
		point2.x = Math.max(x1,x2);		
		point1.y = Math.min(y1,y2);		
		point2.y = Math.max(y1,y2);
		
	}
//	�ж��Ƿ񱻵�ѡ�еĺ���
	public boolean IsPoint(int x,int y,float j1)
	{
		float xx,x1=0,x2=0,y1=0,y2=0;
		Point point1 = new Point();
		Point point2 = new Point();
		GetRect(point1,point2);
		x1 = point1.x;
		y1 = point1.y;
		x2 = point2.x;
		y2 = point2.y;
		if(!(x >= x1-j1 && x <= x2+j1 && y >= y1-j1 && y <= y2+j1))
			return false;
		xx = super.PointLine(x,y,this.x1,this.y1,this.x2,this.y2);
		if(xx < j1)
			return true;
		return false;
		
	}
	
	
	
	
	// ��ֱ��
	public void draw(Graphics g,int m_DrawMode,Color bgColor) {
		Graphics2D g2d = (Graphics2D) g;
		// ���û�����ɫ
		g2d.setColor(super.m_ColorPen);
		g2d.setStroke(MyCanvas.STROKES[super.m_LineWide]);
		// drawLine��������
	
		if(m_DrawMode == 0)   //������ͼ
		{
			g2d.drawLine(this.x1,this.y1,this.x2,this.y2);
	
		
		}
		else{   //ѡ��ʱ������ʾ
			Stroke thindashed = new BasicStroke(2.0f,
					BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL,1.0f,
					new float[]{8.0f,3.0f,2.0f,3.0f},
					0.0f);
			g2d.setColor(bgColor);
			g2d.drawLine(this.x1,this.y1,this.x2,this.y2);
			g2d.setStroke(thindashed);
			g2d.setColor(super.m_ColorPen);
			g2d.drawLine(this.x1,this.y1,this.x2,this.y2);
		}
	}
}