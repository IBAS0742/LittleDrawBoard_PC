package com.rock.drawboard.Painter;

/*
������ߣ� ��������ʱ�ظ�
�����Ȩ���������У������˿��Զ���������޸ģ�����ʹ��������룬������ʹ���뱣��������Ϣ��
*/
import java.awt.image.*;
import java.io.*;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * ��Image����ת����jpegͼƬ
 */
public class ImageToJpeg {
  public static boolean toJpeg(File aFile,BufferedImage bimg)
  {
    try
    {
      FileOutputStream fos=new FileOutputStream(aFile);
      JPEGImageEncoder encoder=JPEGCodec.createJPEGEncoder(fos);
      encoder.encode(bimg);
    }
    catch(Exception ex)
    {
      return false;
    }
    return true;
  }
}
