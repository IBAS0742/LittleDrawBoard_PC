package com.rock.drawboard.board;

/*
������ߣ� ��������ʱ�ظ�
�����Ȩ���������У������˿��Զ���������޸ģ�����ʹ��������룬������ʹ���뱣��������Ϣ��
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TextInput extends JDialog{
   private JTextField sample=new JTextField();
   private JLabel jlblInput=new JLabel();
   private JButton btnOK=new JButton();
   private JButton btnCancel=new JButton();
   private JButton btnFont=new JButton();
   Font aFont1=new Font("",0,10);
   boolean flag=false;
   public Font aFont;
   public String str;
  public TextInput(Frame parent,boolean modal,String str)
  {
    super(parent,modal);
    initAll();
  }
  public int showDialog(Frame parent,String str,Font f)
  {
    aFont=f;
    sample.setText(str);
    setVisible(true);
    if(flag)
    {
        this.str = this.sample.getText();
        this.aFont=aFont1;
        return 0;
    }
    else return 1;
  }
  private void initAll() {
    getContentPane().setLayout(null);
    setBounds(50, 50, 330, 150);
    sample.setBounds(10, 30, 300, 20);
    sample.setForeground(Color.black);
    getContentPane().add(sample);
    jlblInput.setText("����Text:");
    jlblInput.setBounds(10,10,100,15);
    getContentPane().add(jlblInput);
    btnOK.setText("ȷ��");
    btnOK.setBounds(185,85,65,25);
    getContentPane().add(btnOK);
    btnCancel.setText("ȡ��");
    btnCancel.setBounds(255,85,65,25);
    getContentPane().add(btnCancel);
    btnFont.setText("ѡ������");
    btnFont.setBounds(10,60,80,25);
    getContentPane().add(btnFont);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        setVisible(false);
      }
    });
    btnOK.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        aFont=aFont1;
        flag=true;
        setVisible(false);
      }
    });
    btnCancel.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        flag=false;
        setVisible(false);
      }
    }
    );
    btnFont.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        aFont1=NwFontChooserS.showDialog(null,null,aFont1);
        sample.setFont(aFont1);
      }
    }
    );
  }
}
