package com.rock.drawboard.Painter;
import java.awt.*;
import java.lang.Math;
import java.io.Serializable;
/*
������ߣ� ��������ʱ�ظ�
�����Ȩ���������У������˿��Զ���������޸ģ�����ʹ��������룬������ʹ���뱣��������Ϣ��
*/

public class Shape implements Serializable{
	Color  m_ColorPen;   //��ɫ
	Color m_ColorBrush; //ˢ��ɫ
	int m_LineWide;   //�߿�
	
	
	public  boolean IsPoint(int x,int y,float j1){return false;}
//	 ��ͼ��ÿ���Զ���ͼ�ζ�����ʵ�ָýӿڡ�
	public void draw(Graphics g,int m_DrawMode,Color bgColor){}    //m_DrawMode = 0 Ĭ�ϻ�ͼ��m_DrawMode �� 1 ѡ�л�ͼ
//	�����������sqrt
	public float DisPoint(int x1,int y1,int x2,int y2)
	{
		return (float)Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); 
	}
//	�㵽ֱ�߾���
	public float PointLine(int xx,int yy,int x1,int y1,int x2,int y2)  
	{
		float a,b,c,ang1,ang2,ang;
		//���������ߵľ���
		a = DisPoint(x1,y1,xx,yy);
		if(a == 0.0) return (float) 0.0;
		b = DisPoint(x2,y2,xx,yy);
		if(b == 0.0) return (float) 0.0;
		c = DisPoint(x1,y1,x2,y2);
		if(c == 0.0) return a;
		if(a < b)
		{
			if(y1 == y2)
			{
				if(x1 < x2)
					ang1 = 0;
				else 
					ang1 = (float)Math.PI;
			}
			else 
			{
				ang1 = (float)Math.acos((x2 - x1) / c);
				if(y1 > y2)
					ang1 = (float)Math.PI * 2 - ang1;
			}
			ang2 = (float)Math.acos((xx - x1) / a);
			if(y1 > yy) ang2 = (float)Math.PI * 2 - ang2;
			ang = ang2 - ang1;
			if(ang < 0) ang = -ang;
			if(ang > Math.PI)ang = (float)Math.PI *2 - ang;
			if(ang > Math.PI/2) return a;
			else
				return (a * (float)Math.sin(ang));
		}
		else
		{
			if(y1 == y2)
			{
				if(x1 < x2)
					ang1 = (float)Math.PI;
				else
					ang1 =0;
			}
			else
			{
				ang1 = (float)Math.acos((x1 -x2)/c);
				if(y2 > y1) ang1 = (float)Math.PI * 2 - ang1;
			}
			ang2 = (float)Math.acos((xx - x2)/b);
			if(y2 > yy) ang2= (float)Math.PI * 2 - ang2;
			ang = ang2 - ang1;
			if(ang < 0) ang = -ang;
			if(ang > Math.PI)ang = (float)Math.PI *2 - ang;
			if(ang > Math.PI/2) return b;
			else
				return (b * (float)Math.sin(ang));
		}
		
	}



	public Shape(){
		
	}
	
	public Shape(Color colorPen, Color colorBrush, int lineWide) {
		m_ColorPen = colorPen;
		m_ColorBrush = colorBrush;
		m_LineWide = lineWide;
	
	}
	
	
}