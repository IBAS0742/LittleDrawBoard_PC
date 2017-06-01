
package com.rock.drawboard.Painter;
/*
������ߣ� ��������ʱ�ظ�
�����Ȩ���������У������˿��Զ���������޸ģ�����ʹ��������룬������ʹ���뱣��������Ϣ��
*/
import com.rock.drawboard.board.MyCanvas;

import java.awt.*;


public class Pencil extends Shape {
	
	private PointsSet pointsSet;
	// ���췽��
	public Pencil(Color ColorPen, Color ColorBrush, int LineWide,
				  int x, int y) {
		super(ColorPen,ColorBrush,LineWide);
		pointsSet = new PointsSet(50);
		pointsSet.addPoint(x, y);
	}
	
	public Pencil(Color ColorPen,Color ColorBrush,int LineWide
			) {
	//	super(ColorPen,ColorBrush,LineWide,Delete);
		 pointsSet = new PointsSet(50);
	}
	
	public void setPoints(int x, int y){
		pointsSet.addPoint(x, y);
	}
    
	public boolean IsPoint(int x,int y,float j1)
	{
		return false;
	}

    
    public void draw(Graphics g, int m_DrawMode,Color bgColor) {
    	Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(super.m_ColorPen);
        g2d.setStroke(MyCanvas.STROKES[super.m_LineWide]);
        int[][] points = pointsSet.getPoints();
        if (points == null)
            return;
        int s = points[0].length;
        if (s == 1) {
            int x = points[0][0];
            int y = points[1][0];
            g2d.drawLine(x, y, x, y);
        } else {
        	g2d.drawPolyline(points[0], points[1], s);
        }
    }
    
}
