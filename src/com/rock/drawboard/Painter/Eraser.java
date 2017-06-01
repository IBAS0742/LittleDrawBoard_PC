
/*
 ������ߣ� ��������ʱ�ظ�
 �����Ȩ���������У������˿��Զ���������޸ģ�����ʹ��������룬������ʹ���뱣��������Ϣ��
*/
package com.rock.drawboard.Painter;

import com.rock.drawboard.board.MyCanvas;

import java.awt.*;



public class Eraser extends Shape {
	private int x;
	private int y;
	public Eraser(Color colorPen, Color colorBrush, int lineWide, int x,int y) {
		super(colorPen, colorBrush, lineWide);
		this.x = x;
		this.y = y;
		}

	
	public boolean IsPoint(int x,int y,float j1)
	{
		return false;
	}
	public void draw(Graphics g,int m_DrawMode,Color bgColor) {
		g.setColor(bgColor);
		int wide = MyCanvas.ERASER_STROKES[super.m_LineWide];
		g.fillRect(this.x,this.y,wide,wide);
	
	}
}