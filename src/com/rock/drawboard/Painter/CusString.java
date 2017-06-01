package com.rock.drawboard.Painter;

/*
 ������ߣ� ��������ʱ�ظ�
 �����Ȩ���������У������˿��Զ���������޸ģ�����ʹ��������룬������ʹ���뱣��������Ϣ��
*/

import java.io.Serializable;
import java.awt.*;
import java.awt.geom.*;
import java.awt.datatransfer.*;


/**
 * CusString�࣬ʵ��Serializable,CusShape�ӿ�
 */
public class CusString extends Shape{
  public String aString;
  public int x,y;
  private Font aFont=new Font("",Font.BOLD,10);
  private Color col=Color.black;
  /**
   * ���캯��
   * @param aString String
   * @param x int
   * @param y int
   */
  public CusString(String aString, int x, int y)
  {
	super();
    this.aString=aString;
    this.x=x;
    this.y=y;
  }
  /**
   * ���캯��
   * @param aString String
   * @param x int
   * @param y int
   * @param aFont Font
   * @param col Color
   */
  public CusString(String aString, int x, int y, Font aFont, Color col)
  {
    this.aString=aString;
    this.x=x;
    this.y=y;
    this.aFont=aFont;
    this.col=col;
  }
  /**
   * ��������
   * @param f Font
   */
  public void setFont(Font f)
  {
    this.aFont=f;
  }
  /**
   * �õ�����
   * @return Font
   */
  public Font getFont()
  {
    return aFont;
  }
  /**
   * �ж��Ƿ���x,y,w,h���ɵľ����ཻ
   * @param x double
   * @param y double
   * @param w double
   * @param h double
   * @return boolean
   */
  public boolean intersects(double x,double y,double w,double h)
  {
    Rectangle2D rec2D=new Rectangle2D.Float(this.x,this.y,aFont.getSize()*aString.length()/2,aFont.getSize());
    return rec2D.intersects(x,y,w,h);
  }
  /**
   * �õ��߽�
   * @return Rectangle
   */
  public java.awt.Rectangle getBounds()
  {
    return new java.awt.Rectangle(this.x,this.y,aFont.getSize()*aString.length()/2,aFont.getSize());
  }
  /**
   * ����λ��
   * @param x int
   * @param y int
   */
  public void setLocation(int x,int y)
  {
    this.x=x;
    this.y=y;
  }
  /**
   * ������ɫ
   * @param col Color
   */
  public void setColor(Color col)
  {
    this.col=col;
  }
  
  
  public boolean IsPoint(int x,int y,float j1)
	{
	  java.awt.Rectangle rect = this.getBounds();
	  int x1 = 0,y1 = 0,x2 = 0,y2 = 0;
	  x1 = this.x;
	  y1 = this.y;
	  x2 = x1 + rect.width;
	  y2 = y1 + rect.height;
	  if((x >= x1-j1 && x <= x2+j1 && y >= y1-j1 && y <= y2+j1))
			return true;
	  return false;
	}
  /**
   * �õ���ɫ
   * @return Color
   */
  public Color getColor()
  {
    return col;
  }
  /**
   * ���Ʒ���
   * @param g Graphics
   */
  public void draw(Graphics g,int m_DrawMode,Color bgColor)
  {
	  Graphics2D g2d=(Graphics2D)g;
//		  if(super.b_Delete==true)
	//		  return;
		  g2d.setColor(col);
		  g2d.setFont(aFont);
		  g2d.drawString(aString,x,y+aFont.getSize());
		
	 if(m_DrawMode != 0)
	  {
		  Stroke thindashed = new BasicStroke(2.0f,
				  BasicStroke.CAP_BUTT,
				  BasicStroke.JOIN_BEVEL,1.0f,
				  new float[]{8.0f,3.0f,2.0f,3.0f},
				  0.0f);
		  g2d.setStroke(thindashed);
		  java.awt.Rectangle rect = this.getBounds();
		  g2d.drawRect(this.x,this.y,rect.width,rect.height);
		  
	  }
  }
  /**
   * �����Ƿ���䣬������
   * @param isFill boolean
   */
  public void setIsFill(boolean isFill)
  {
  }
  /**
   * �õ��Ƿ���䣬������
   * @return boolean
   */
  public boolean getIsFill()
  {
    return false;
  }
  /**
   * ��������
   * @param aStroke BasicStroke
   */
  public void setStroke(BasicStroke aStroke)
  {

  }
  
}
