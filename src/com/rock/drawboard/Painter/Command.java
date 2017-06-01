package com.rock.drawboard.Painter;

/**
 * �������LINE��ʾ���ߣ�CIRCLE��ʾ��Բ��RECTANGLE��ʾ������
 */
public interface Command {
	public static final int LINE = 1; 
	public static final int CIRCLE = 4; 
	public static final int RECTANGLE = 3; 
	public static final int PENCIL = 2; 
	
	public static final int ERASER=5;
	public static final int ZOOM = 9; 
	public static final int SELECT = 10; 
	public static final int UNSELECTED = 11; 
	public static final int DELETE = 12; 
	public static final int TEXTINSERT = 13;
	
	
}
