package com.rock.drawboard.board;

import com.rock.drawboard.Painter.Command;
import com.rock.drawboard.Painter.ImageToJpeg;
import com.rock.drawboard.module.*;
import com.rock.drawboard.module.Point;
import com.rock.drawboard.socket.ServerAction;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;


public class PaintBoard extends JFrame implements ServerAction.OnEventListener {

	private final ServerAction serviceAction;
	private JFileChooser filechooser1;
    private JFileChooser filechooser2;
	
	/********�˵������˵��Ͳ˵���*********/
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("�ļ�");
	JMenu editMenu = new JMenu("�༭");	
	JMenu imageMenu = new JMenu("ͼ��");
	JMenu viewMenu = new JMenu("�鿴");
	JMenu filterMenu = new JMenu("�˾�");
	JMenu helpMenu = new JMenu("����");
	JMenuItem newMenuItem = new JMenuItem("�½�");
	JMenuItem openMenuItem = new JMenuItem("��");
	JMenuItem saveMenuItem = new JMenuItem("����");
	JMenuItem saveAsMenuItem = new JMenuItem("���ΪJPG");
	JMenuItem closeMenuItem = new JMenuItem("�ر�");
	JMenuItem exitMenuItem = new JMenuItem("�˳�");
	
	JMenuItem undoMenuItem = new JMenuItem("����");
	JMenuItem redoMenuItem = new JMenuItem("�ָ�");
	JMenuItem cutMenuItem = new JMenuItem("����");
	JMenuItem copyMenuItem = new JMenuItem("����");
	JMenuItem pasteMenuItem = new JMenuItem("ճ��");
	JMenuItem clearMenuItem = new JMenuItem("���");
	JMenuItem selectAllMenuItem = new JMenuItem("ȫѡ");
	
	JMenuItem lineMenuItem = new JMenuItem("ֱ��");
	JMenuItem circleMenuItem = new JMenuItem("Բ");
	JMenuItem ovalMenuItem = new JMenuItem("��Բ");
	JMenuItem undodMenuItem = new JMenuItem("Բ��");
	JMenuItem rectangleMenuItem = new JMenuItem("����");
	JMenuItem eraserMenuItem = new JMenuItem("��Ƥ");
	JMenuItem selectedMenuItem = new JMenuItem("ѡ��");
	JMenuItem unSelectedMenuItem = new JMenuItem("����ѡ��");
	JMenuItem deletedMenuItem = new JMenuItem("ɾ��");
	JMenuItem textInsertyMenuItem = new JMenuItem("�ı�����");


	JMenuItem viewToolBarMenuItem = new JMenuItem("������");
	JMenuItem viewColorPaletteMenuItem = new JMenuItem("��ɫ��");
	JMenuItem viewStatusBarMenuItem = new JMenuItem("״̬��");
	JMenuItem stopFlashMenuItem = new JMenuItem("ֹͣFlash");
	JMenuItem startFlashMenuItem = new JMenuItem("��ʼFlash");
		
	JMenuItem reverseColorFilterMenuItem = new JMenuItem("��ɫ");
	JMenuItem rotateFilterMenuItem = new JMenuItem("��ת");
	JMenuItem blurFilterMenuItem = new JMenuItem("ģ��");
		
	JMenuItem helpMenuItem = new JMenuItem("Help");
	
	/********����****/
	//�ö��������Ŷ��ͼ���ļ�������JScrollPane��
	//��JScrollPane����JTabbedPane�У������һ����ҳ��Ĳ���
	
	//���������ÿ��������ʾһ��ͼ��
	MyCanvas[] canvases = new MyCanvas[10];
	//��ʾ���λ�ú͵�ǰ����ͼ�ε�״̬��
	JTextArea statusTextArea = new JTextArea();
	JScrollPane[] canvasScrollPanes = new JScrollPane[10];
	JScrollPane statusScrollPane;
	//����ı�����ڲ�ͬtab��
	JTabbedPane canvasTabbedPane = new JTabbedPane();
	//��������
	int x = -1, y = -1, eraser, clear;	
	Color pencilColor;

	
	
	/**********�������Լ��������ϵİ�ť**********/
	JToolBar editToolBar = new JToolBar();
	GridBagConstraints gridBagConstraints  = new GridBagConstraints();;
	JPanel ctrlPanel = new JPanel();
	JPanel imageToolBar = new JPanel();
	JPanel colorPanel = new JPanel();
	JPanel sizePanel = new JPanel();
	JPanel savePanel = new JPanel();
	JLabel copyleft = new JLabel();
	JPanel mediumPanel1 = new JPanel();
	JPanel brColorPanel = new JPanel(); 
	
    ButtonGroup toolsGroup = new ButtonGroup();
    JButton fgButton = new JButton();
    JButton bgButton = new JButton();
    JButton brButton = new JButton();
    JComboBox weightCombo = new JComboBox();
    JComboBox eraserCombo = new JComboBox();
    JRadioButton filledButton = new JRadioButton("Fill",false);
    JRadioButton emptyButton = new JRadioButton("Empty",true);
    JPanel fillPanel = new JPanel();
    boolean fill = false;
  
  
	JButton openButton = new JButton(new ImageIcon(loadImage("image/open.gif")));
	JButton newButton = new JButton(new ImageIcon(loadImage("image/new.gif")));
	JButton saveButton = new JButton(new ImageIcon(loadImage("image/save.gif")));
	JButton helpButton = new JButton(new ImageIcon(loadImage("image/help.gif")));
	JButton exitButton = new JButton(new ImageIcon(loadImage("image/close.gif")));
	JButton copyButton = new JButton(new ImageIcon(loadImage("image/copy.gif")));
	JButton cutButton = new JButton(new ImageIcon(loadImage("image/cut.gif")));
	JButton pasteButton = new JButton(new ImageIcon(loadImage("image/paste.gif")));

	
	JToggleButton eraserButton = new JToggleButton(new ImageIcon(loadImage("image/EraserTool.png")));



	JToggleButton lineButton = new JToggleButton(new ImageIcon(loadImage("image/LineTool.png")));
    JToggleButton circleButton = new JToggleButton(new ImageIcon(loadImage("image/CircleTool.png")));     
    JToggleButton rectangleButton = new JToggleButton(new ImageIcon(loadImage("image/RectangleTool.gif")));
    JToggleButton pencilButton = new JToggleButton(new ImageIcon(loadImage("image/PencilTool.gif")));
    JToggleButton selectedButton = new JToggleButton(new ImageIcon(loadImage("image/SelectAreaTool.png")));
	JToggleButton unSelectedButton = new JToggleButton(new ImageIcon(loadImage("image/4.GIF")));
    JToggleButton deletedButton = new JToggleButton(new ImageIcon(loadImage("image/3.GIF")));
    

	
	//���ı�����ʾ��ǰ����ڵ�ǰ �����е����꼰��ͼ״̬
    JTextArea showStatus = new JTextArea();
	//�Ի����壬���������жԻ�����ʾ�ڸô�����
    JFrame dialogFrame = new JFrame();
    
	/*******���֮��ķָ���******/
	JSplitPane leftCenterSplitPane;
	JSplitPane toolFlashSplitPane;
	JSplitPane tabbedStatusSplitPane;
	
	/**********�ļ�ѡ�񡢴洢���********/
	//�ļ�������
	Filter fileFilter = new Filter();
	//�ļ�ѡ����
	//FileChooser fileChooser = new FileChooser();
	// �ļ���д���ƣ�0��ʾ�ļ�ѡ�������ļ���1�ļ�ѡ������ʾд�ļ�
	int fileChooser_control = 0;
	FileWriter fileWriter;
	
	// tabbedPane��tabҳ�ĵ�ǰ����
	int tb = 1;
	int find_control = 0;
	//����Ŀ�������ָ��ǰ�����Ļ���
	int canvas_control = 0;
	//��ǰ�����ͼ��
	Image currentImageInCanvas;

	//��־�ļ��Ƿ�Ϊ�½��ģ�������½����ļ���Ϊtrue
	boolean[] newFileFlags = new boolean[10];
	//��Ŵ��ļ����ڵ�Ŀ¼
	String[] directory = new String[10];
	
    
	/********������ʾFlash�Ŀ�����****/
	JLabel flashLabel = new JLabel(new ImageIcon(loadImage("image/Juggler0.gif")));
	Timer timer = new Timer(100, new Act_timer());
	int timerControl = 0;
	
	
	/********�������****/
	Font font = new Font("Courier", Font.TRUETYPE_FONT, 14);
	JTextArea helpTextArea = new JTextArea();
	JFrame helpFrame = new JFrame("Help");
	
	public PaintBoard(){
		super("����");
		SplashWindow splash = new SplashWindow("jtable.gif", this, 1000);
		//Ϊ������Ӽ����¼�������
		//������һ�зǳ���Ҫ����ʾ�����ܹ����ܽ��㡣
		//���û����һ�䣬�����̻���Ч��
		this.setFocusable(true);
		this.addKeyListener(new MyKeyListener());
		
		//Ϊ������Ӵ����¼�������
		this.addWindowListener(new WindowListener());
		
		//��ʼ��
		init();
		//setLocation(200, 200);
		this.setBounds(140, 140, 1200, 700);
		setResizable(false);
		setVisible(true);
		pack();
		//��ʼʱ��������
		timer.start();
		canvases[canvas_control].setCommand(Command.PENCIL);
		serviceAction = new ServerAction();
		serviceAction.start();
		serviceAction.setOnEventListener(this);
	}
	
	private void init(){
		/*******��ʼ�����塢Ŀ¼*********/
		for(int i=0; i<10; i++){
			newFileFlags[i] = true;
			//���û���ı�����ǰ����ɫ
			canvases[i] = new MyCanvas(statusTextArea, this);
			canvases[i].setBackground(Color.WHITE);
			canvases[i].setForeground(Color.BLACK);
			// ���ó�ʼ������Ϊ���ߣ�����ѡ�е����ť��ǰ��ɫ�ú�ɫ��ʾ
			lineButton.setForeground(Color.red);
			canvases[canvas_control].setCommand(Command.PENCIL);
			// Ϊ�ı�����������¼�������
			canvases[i].addKeyListener(new MyKeyListener());
			canvasScrollPanes[i] = new JScrollPane(canvases[i],
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			Graphics g=getGraphics();
			canvases[i].paint(g);
		}
		
		
		//��ʼ����ʾ��ǰ���λ�õ��ı���
		statusScrollPane = new JScrollPane(statusTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		statusTextArea.setEnabled(false);
		statusTextArea.setFont(font);
		statusTextArea.setBackground(new Color(70, 80, 91));
		statusTextArea.setDisabledTextColor(Color.yellow);

		/**************��ʼ���˵�*************/
		//��ʼ���˵���
		newMenuItem.addActionListener(new Act_NewFile());
		openMenuItem.addActionListener(new Act_OpenFile());
		saveMenuItem.addActionListener(new Act_SaveFile());
		saveAsMenuItem.addActionListener(new Act_SaveAs());
		exitMenuItem.addActionListener(new Act_ExitEditor());
		helpMenuItem.addActionListener(new Act_Help());
		stopFlashMenuItem.addActionListener(new Act_StopFlash());
		startFlashMenuItem.addActionListener(new Act_StartFlash());
		startFlashMenuItem.setEnabled(false);
		undoMenuItem.addActionListener(new Act_UndoAs());
		redoMenuItem.addActionListener(new Act_Redo()); 
		clearMenuItem.addActionListener(new Act_Clear());
		lineMenuItem.addActionListener(new Act_Line()); 
		rectangleMenuItem.addActionListener(new Act_Rectangle()); 		
		eraserMenuItem.addActionListener(new Act_Eraser()); 
		circleMenuItem.addActionListener(new Act_Circle());
		selectedMenuItem.addActionListener(new Act_Selected()); 
		unSelectedMenuItem.addActionListener(new Act_UnSelected()); 
		deletedMenuItem.addActionListener(new Act_Deleted());
		textInsertyMenuItem.addActionListener(new Act_TextInsert()); 

		viewColorPaletteMenuItem.addActionListener(new Act_Palette()); 
		stopFlashMenuItem.addActionListener(new Act_StopFlash()); 
		startFlashMenuItem.addActionListener(new Act_StartFlash()); 

		
		//��ʼ���˵�
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);	
		fileMenu.add(exitMenuItem);
		fileMenu.add(exitMenuItem);
		
		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.add(clearMenuItem);
		helpMenu.add(helpMenuItem);

		
		imageMenu.add(lineMenuItem);
		imageMenu.add(circleMenuItem);
		imageMenu.add(rectangleMenuItem);
		imageMenu.add(eraserMenuItem);
		imageMenu.add(selectedMenuItem);
		imageMenu.add(unSelectedMenuItem);
		imageMenu.add(deletedMenuItem);
		imageMenu.add(textInsertyMenuItem);

		viewMenu.add(viewColorPaletteMenuItem);
		viewMenu.add(stopFlashMenuItem);
		viewMenu.add(startFlashMenuItem);
		
		filterMenu.add(reverseColorFilterMenuItem );
		filterMenu.add(rotateFilterMenuItem );
		filterMenu.add(blurFilterMenuItem );
		
		//��ʼ���˵���
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(imageMenu);
		menuBar.add(viewMenu);	
		menuBar.add(filterMenu);
		menuBar.add(helpMenu);
		//���˵�����ӵ�������
		setJMenuBar(menuBar);
		
		/***********��ʼ���������Լ���ť**********/
		//��ʼ����ť
		newButton.addActionListener(new Act_NewFile());
		openButton.addActionListener(new Act_OpenFile());
		saveButton.addActionListener(new Act_SaveFile());
		exitButton.addActionListener(new Act_ExitEditor());		
		helpButton.addActionListener(new Act_Help());
		eraserButton.addActionListener(new Act_Eraser());

		lineButton.addActionListener(new Act_Line());
		circleButton.addActionListener(new Act_Circle());
		rectangleButton.addActionListener(new Act_Rectangle());
		pencilButton.addActionListener(new Act_Pencil());
		selectedButton.addActionListener(new Act_Selected());
		unSelectedButton.addActionListener(new Act_UnSelected());
		deletedButton.addActionListener(new Act_Deleted());
		// Ϊ������������ʾ��Ϣ��������ڹ�������ť��ͣ��һ��ʱ��ʱ������ʾ��ʾ��Ϣ
		newButton.setToolTipText("New");
		openButton.setToolTipText("Open");
		saveButton.setToolTipText("Save");
		exitButton.setToolTipText("Exit");
		helpButton.setToolTipText("Help");	
		copyButton.setToolTipText("Copy");
		cutButton.setToolTipText("Cut");
		pasteButton.setToolTipText("Paste");
		eraserButton.setToolTipText("Eraser");
		lineButton.setToolTipText("Line");
		circleButton.setToolTipText("Circle");
		rectangleButton.setToolTipText("Rectangle");
		pencilButton.setToolTipText("Pencil");
		selectedButton.setToolTipText("Seleted");	
		unSelectedButton.setToolTipText("UnSelected");
		deletedButton.setToolTipText("Deleted");
		toolsGroup.add(eraserButton);
		toolsGroup.add(lineButton);
		toolsGroup.add(circleButton);
		toolsGroup.add(rectangleButton);
		toolsGroup.add(pencilButton);
		toolsGroup.add(selectedButton);
		toolsGroup.add(unSelectedButton);
		toolsGroup.add(deletedButton);
	
		
		newButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		openButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		saveButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		exitButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		helpButton.setBorder(new BevelBorder(BevelBorder.RAISED));		
		cutButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		copyButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		pasteButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		eraserButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		lineButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		circleButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		rectangleButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		pencilButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		selectedButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		unSelectedButton.setBorder(new BevelBorder(BevelBorder.RAISED));
		deletedButton.setBorder(new BevelBorder(BevelBorder.RAISED));
				
		//��ʼ��������
		setTitle("����");
		editToolBar.add(newButton);
		editToolBar.add(openButton);
		editToolBar.add(saveButton);
		editToolBar.add(exitButton);
		editToolBar.add(helpButton);
		/*editToolBar.add(copyButton);
		editToolBar.add(cutButton);
		editToolBar.add(pasteButton);*/	
		
		
		imageToolBar.setLayout(new GridLayout(3, 3, 5, 5));
		imageToolBar.add(lineButton);
		imageToolBar.add(circleButton);	
		imageToolBar.add(rectangleButton);
		imageToolBar.add(pencilButton);
		imageToolBar.add(eraserButton);
		imageToolBar.add(selectedButton);
		imageToolBar.add(unSelectedButton);
		imageToolBar.add(deletedButton);
		//��ʼ��������
		ctrlPanel.setLayout(new GridBagLayout());
        ctrlPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        mediumPanel1.setLayout(new BoxLayout(mediumPanel1, BoxLayout.Y_AXIS));

        imageToolBar.setBorder(new TitledBorder("Drawing Tools"));
        mediumPanel1.add(imageToolBar);

        colorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));

        colorPanel.setBorder(new TitledBorder("Color Settings"));
        fgButton.setBackground(canvases[0].getForeground());
        fgButton.setToolTipText("Change Drawing Color");
        fgButton.setBorder(new LineBorder(new Color(0, 0, 0)));
        fgButton.setPreferredSize(new Dimension(30, 30));
        fgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fgButtonActionPerformed(evt);
            }
        });

        colorPanel.add(fgButton);

        bgButton.setBackground(canvases[0].getBackground());
        bgButton.setToolTipText("Change Board Background Color");
        bgButton.setBorder(new LineBorder(new Color(0, 0, 0)));
        bgButton.setPreferredSize(new Dimension(30, 30));
        bgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bgButtonActionPerformed(evt);
            }
        });

        colorPanel.add(bgButton);
        mediumPanel1.add(colorPanel);

        brButton.setBackground(Color.BLACK);
        brButton.setText("Brush Color");
        brButton.setToolTipText("Change Brush Background Color");
        brButton.setBorder(new LineBorder(new Color(0, 0, 0)));
        brButton.setPreferredSize(new Dimension(100, 25));
        brButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                brButtonActionPerformed(evt);
            }
        });

        brColorPanel.setBorder(new TitledBorder("Brush Color Settings"));
        brColorPanel.add(brButton);
        mediumPanel1.add(brColorPanel);

        sizePanel.setLayout(new BorderLayout(0, 3));

        weightCombo.setFont(new Font("Dialog", 0, 10));
        weightCombo.setModel(new DefaultComboBoxModel(new String[] { "Stroke Weight 5px", "Stroke Weight 8px", "Stroke Weight 10px", "Stroke Weight 15px", "Stroke Weight 20px" }));
        weightCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                weightComboActionPerformed(evt);
            }
        });

        sizePanel.setBorder(new TitledBorder("Size Setttings"));
        sizePanel.add(weightCombo, BorderLayout.NORTH);

        eraserCombo.setFont(new Font("Dialog", 0, 10));
        eraserCombo.setModel(new DefaultComboBoxModel(new String[] { "Eraser Size 15px", "Eraser Size 20px", "Eraser Size 30px", "Eraser Size 50px", "Eraser Size 100px" }));
        eraserCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                eraserComboActionPerformed(evt);
            }
        });

        sizePanel.add(eraserCombo, BorderLayout.SOUTH);
        mediumPanel1.add(sizePanel);


        fillPanel.setLayout(new FlowLayout(BoxLayout.Y_AXIS, 4, 4));
        fillPanel.setBorder(new TitledBorder("Fill Settings"));
        //������ѡť�������ӵ���ť��
        //Ϊÿ����ѡ��ťָ��һ���¼���������
        ButtonGroup radioGroup = new ButtonGroup();
        emptyButton.addChangeListener(new Item_FillChanged());
        filledButton.addChangeListener(new Item_FillChanged());
        radioGroup.add(emptyButton);
        radioGroup.add(filledButton);
        fillPanel.add(emptyButton);
        fillPanel.add(filledButton);
        mediumPanel1.add(fillPanel);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(10, 5, 5, 5);
        ctrlPanel.add(mediumPanel1, gridBagConstraints);


        copyleft.setFont(new Font("Verdana", 0, 10));
        copyleft.setForeground(new Color(255, 153, 0));
        copyleft.setText("CopyLeft 2007 NJUPT");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 15, 5);
        ctrlPanel.add(copyleft, gridBagConstraints);

		/********��ʼ��tabҳ���������ķָ���*********/
		canvasTabbedPane.addTab("Image1", canvasScrollPanes[0]);
		canvasTabbedPane.addChangeListener(new Act_ChangeTab());
		//�ļ�Ŀ¼����Flash֮��ķָ���
		toolFlashSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, flashLabel, ctrlPanel);
		//�ļ��ı��������ָʾ����������ı������֮��ķָ���
		tabbedStatusSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, canvasTabbedPane, statusScrollPane);
		//��ߴ�������ұߴ����֮��ķָ���
		leftCenterSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, toolFlashSplitPane, tabbedStatusSplitPane);
		// ���÷ָ������������ʾ�Ŀ�ȣ�����ָ��������ҷָ�������ʾ�ָ����ĺ�����
		// ����ָ��������·ָ�������ʾ�ָ�����������
		leftCenterSplitPane.setDividerLocation(150);
		tabbedStatusSplitPane.setDividerLocation(460);
		toolFlashSplitPane.setDividerLocation(120);


		//��ʼ������
		initHelp();
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(editToolBar, BorderLayout.NORTH);
		this.getContentPane().add(leftCenterSplitPane);

	}

	/**
	 * ��ʼ��������Ϣ
	 */
	private void initHelp(){

		//������Ϣ��Ҫ��ʾ�˵��Ŀ�ݷ�ʽ

		// field���JTable�ı�ͷ��Ϣ������ı���
		String[] field = { "MenuItem", "ShortCut Key" };
		// data���JTable�����ݡ�
		Object[][] data = { { "     New           ", "    Ctrl+N    " },
				{ "    Open          ", "    F12       " },
				{ "    Save          ", "    Ctrl+S    " },
				{ "    Exit          ", "    Ctrl+X    " },
				{ "    Help          ", "    Ctrl+H    " }, };
		// �ñ�ͷ�����ݹ���һ����
		JTable help_Table = new JTable(data, field);
		help_Table.setFont(font);
		//���ɱ༭������Ϣ��
		help_Table.setEnabled(false);
		// Ϊ����ı������ñ�����ǰ����ɫ
		helpTextArea.setFont(new Font("Courier", Font.TRUETYPE_FONT, 16));
		helpFrame.getContentPane().setLayout(new BorderLayout());
		help_Table.setForeground(Color.pink);
		helpTextArea.setForeground(Color.pink);
		help_Table.setBackground(new Color(70, 80, 91));
		help_Table.setSelectionBackground(new Color(70, 80, 91));
		helpTextArea.setBackground(new Color(70, 80, 91));
		helpTextArea.setText(" If you want to use this software with all functions,\n"
						+ "     You must do the things following:\n"
						+ "     1: install jdk_1.3 or Higher than it ;\n"
						+ "     2: set your classpath and path correctly;\n"
						+ "     3: if you want to use the compile and build functions,\n"
						+ "       you should save your edited File in the save directory\n"
						+ "       with  this software.\n ");
		// ���ı���ͱ�ӵ�������
		helpFrame.getContentPane().add(
				new JScrollPane(help_Table,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		helpFrame.getContentPane().add(
				new JScrollPane(helpTextArea,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.NORTH);

	}



	//�˳�����
	private void exitPaintBoard(){
		if(JOptionPane.showConfirmDialog(this, "��ȷ���˳����壿", "�˳�", JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
			// ���ѡ��YES�����˳���
			//dispose���������ͷ���Դ
			//�ͷ��ɴ� Window�������������ӵ�е������������ʹ�õ����б�����Ļ��Դ��
			//����Щ Component ����Դ�����ƻ�������ʹ�õ������ڴ涼�����ص�����ϵͳ���������Ǳ��Ϊ������ʾ��
			//ͨ������ pack �� show �ĵ������¹��챾����Դ�������ٴ���ʾ Window �����������
			//���´����� Window �����������״̬���Ƴ� Window �ĵ�������Щ�����״̬����һ���ģ���������Щ����֮����������ģ���
			dispose();
			System.exit(0);
		}
	}

	//���û��ʳ���
	private static final int STROKE_PEN = 12;       //����1
	private static final int STROKE_ERASER = 2;    //��Ƥ��2
	private static final int STROKE_RECT = 9;      //���� 4
	private static final int STROKE_CIRCLE = 8;    //Բ 5
	private static final int STROKE_LINE = 6;      //ֱ��7
	public static final int REDO = 201;  //redo
	public static final int UNDO = 202;  //undo
	public static final int CLEAR_BOARD = 203;  //������
	public static final int ACTION_DOWN = 301;
	public static final int ACTION_MOVE = 302;
	public static final int ACTION_UP = 303;
	@Override
	public void onPencil(com.rock.drawboard.module.Point point) {
		switch (point.getState()) {
			case ACTION_DOWN:
				canvases[canvas_control].actionDown(point.getX(),point.getY());
				break;
			case ACTION_MOVE:
				canvases[canvas_control].actionMove(point.getX(),point.getY());
				break;
			case ACTION_UP:
				canvases[canvas_control].actionUp(point.getX(),point.getY());
				break;
		}
	}

	@Override
	public void onCommand(com.rock.drawboard.module.Command command) {
		switch (command.getType()) {
			case STROKE_PEN:
				canvases[canvas_control].setCommandToAndroid(Command.PENCIL);
				break;
			case STROKE_ERASER:
				canvases[canvas_control].setCommandToAndroid(Command.ERASER);
				break;
			case STROKE_CIRCLE:
				canvases[canvas_control].setCommandToAndroid(Command.CIRCLE);
				break;
			case STROKE_LINE:
				canvases[canvas_control].setCommandToAndroid(Command.LINE);
				break;
			case STROKE_RECT:
				canvases[canvas_control].setCommandToAndroid(Command.RECTANGLE);
				break;
			case REDO:
				canvases[canvas_control].redo();
				break;
			case UNDO:
				canvases[canvas_control].undo();
				break;
			case CLEAR_BOARD:
				canvases[canvas_control].clearBoard();
				break;
		}
	}

	@Override
	public void onColor(SelectColor color) {

		canvases[canvas_control].setForegroundColor(new Color(color.getR(),color.getG(),color.getB()));
	}

	@Override
	public void onStroke(StrokeWidth strokeWidth) {
		if(strokeWidth.getType()==STROKE_PEN) {
			canvases[canvas_control].setStrokeIndex(strokeWidth.getStroke());
		}else {
			canvases[canvas_control].setEraserIndex(strokeWidth.getStroke());
		}
	}

	/**
	 * �����¼�������
	 */
	public class MyKeyListener extends KeyAdapter{
		//���Ǹ����keyPressed�����������������ʱ���¼���
		public void keyPressed(KeyEvent e){
			//��F12���ļ�
			if(e.getKeyCode() == KeyEvent.VK_F12){
				(new Act_OpenFile()).actionPerformed(null);
			}
			// ��Ctrl��S�������ļ�
			else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
				(new Act_SaveFile()).actionPerformed(null);
			}
			// ��Ctrl��N�½��ļ�
			else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N){
				(new Act_NewFile()).actionPerformed(null);
			}
			// ��Ctrl��E�˳��༭��
			else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E){
				(new Act_ExitEditor()).actionPerformed(null);
			}
			// ��Ctrl��H��ʾ����
			else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_H){
				(new Act_Help()).actionPerformed(null);
			}
		}
	}

	/**
	 * �����¼�������
	 */
	public class WindowListener extends WindowAdapter{
		//����رմ����¼�
		public void windowClosing(WindowEvent e){
			dispose();
			System.exit(0);
		}
	}

	/**
	 * �л�tabҳ�¼�
	 */
	class Act_ChangeTab implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			//�л�tabҳʱ������canvas_control��ֵ��
			canvas_control = canvasTabbedPane.getSelectedIndex();
		}
	}

	/**
	 * ���ļ��¼�
	 */
	class Act_OpenFile implements ActionListener{
		public void actionPerformed(ActionEvent e_ji1){
			//�������ļ�
			 if (filechooser1 == null) {
	                filechooser1 = new JFileChooser();
	                filechooser1.setFileFilter(fileFilter);
	                filechooser1.setMultiSelectionEnabled(false);
	                filechooser1.setAcceptAllFileFilterUsed(false);
	                filechooser1.setCurrentDirectory(new File("./"));
	            }
	            int retVal = filechooser1.showOpenDialog(canvases[canvas_control]);
	            if (retVal != JFileChooser.APPROVE_OPTION)
	                return;
	            //��ȡ���򿪵��ļ���
				String filename = filechooser1.getSelectedFile().getName();
				//�½�һ��tabҳ������װ�´򿪵��ļ�
				canvasTabbedPane.addTab(filename, canvasScrollPanes[tb]);
				canvasTabbedPane.setSelectedIndex(tb);
				//����ǰ�ı������õ��´򿪵��ļ���
				canvas_control = tb;
				tb++;
				//��ȡ���򿪵��ļ����ڵ�Ŀ¼����Ŀ¼���������飬�����ڱ����ļ���ʱ���ܹ����ļ������浽Ŀ¼��
				directory[canvas_control] = filechooser1.getCurrentDirectory().toString();
				try {
					//���ļ�������ʾ��������
					canvases[canvas_control].OpenFile(directory[canvas_control] + "/" + filename);
				} catch (Exception e_open) {
					JOptionPane.showMessageDialog(dialogFrame.getContentPane(), "��ȡ��������");
				}
		}
	}

	/**
	 * �½��ļ��¼�
	 */
	class Act_NewFile implements ActionListener{
		public void actionPerformed(ActionEvent e_ji10){
			// �л�tabҳʱ������canvas_control��ֵ��
			canvasTabbedPane.addTab("Image" + (tb + 1), canvasScrollPanes[tb]);
			canvasTabbedPane.setSelectedIndex(tb);
			canvas_control = tb;
			tb++;
		}
	}

	/**
	 * �����ļ��¼�
	 */
	class Act_SaveFile implements ActionListener{
		public void actionPerformed(ActionEvent e_ji2){
			 if (filechooser1 == null) {
	                filechooser1 = new JFileChooser();
	                filechooser1.setFileFilter(fileFilter);
	                filechooser1.setMultiSelectionEnabled(false);
	                filechooser1.setAcceptAllFileFilterUsed(false);
	                filechooser1.setCurrentDirectory(new File("./"));
	            }
	            int retVal = filechooser1.showSaveDialog(canvases[canvas_control]);
	            if (retVal != JFileChooser.APPROVE_OPTION)
	                return;
	            File file = filechooser1.getSelectedFile();
	            if (!file.getName().toLowerCase().endsWith(".jpg")) {
	            	//��ȡ���򿪵��ļ���
	    			String filename = filechooser1.getSelectedFile().getName();
	    			canvasTabbedPane.setTitleAt(tb-1, filename);
	    			filename = filename+ ".jpg";
	    			//��ȡ���򿪵��ļ����ڵ�Ŀ¼����Ŀ¼���������飬�����ڱ����ļ���ʱ���ܹ����ļ������浽Ŀ¼��
	    			directory[canvas_control] = filechooser1.getCurrentDirectory().toString();
	    			try {
	    				canvases[canvas_control].SaveFile(directory[canvas_control] + "/" + filename);
	    				canvases[canvas_control].SaveJpg();
	    				ImageToJpeg.toJpeg(new File("ss.jpg"), canvases[canvas_control].offScreenImg);
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	            }

		}
	}

	/**
	 * �˳������¼�
	 */
	class Act_ExitEditor implements ActionListener{
		public void actionPerformed(ActionEvent e_ji3){
			//�˳�����
			exitPaintBoard();
		}
	}

	/**
	 * ��ʾ����Help�¼�
	 */
	class Act_Help implements ActionListener{
		public void actionPerformed(ActionEvent e_ji9){
			helpFrame.pack();
			helpFrame.setVisible(true);
			helpFrame.requestFocus();
			helpFrame.setLocation(200, 0);
		}
	}

	//�ָ�
	class Act_Redo implements ActionListener {
		public void actionPerformed(ActionEvent e_ji10) {
			canvases[canvas_control].redo();
		}
	}

	//����
	class Act_UndoAs implements ActionListener{
		public void actionPerformed(ActionEvent e_ji9){
			canvases[canvas_control].undo();
		}
	}



	class Act_Line implements ActionListener {
		public void actionPerformed(ActionEvent e_ji11) {
			lineButton.setSelected(true);
			canvases[canvas_control].setCommand(Command.LINE);
			statusTextArea.setText("The current draw tool: " + canvases[canvas_control].getCommandString(Command.LINE));
			serviceAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(STROKE_LINE)));
		}
	}


	class Act_Circle implements ActionListener{
		public void actionPerformed(ActionEvent e_ji9){
			circleButton.setSelected(true);
			canvases[canvas_control].setCommand(Command.CIRCLE);
			canvases[canvas_control].fill = fill;
			statusTextArea.setText("The current draw tool: " + canvases[canvas_control].getCommandString(Command.CIRCLE));
			serviceAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(STROKE_CIRCLE)));

		}
	}

	class Act_Rectangle implements ActionListener {
		public void actionPerformed(ActionEvent e_ji10) {
			rectangleButton.setSelected(true);
			canvases[canvas_control].setCommand(Command.RECTANGLE);
			canvases[canvas_control].fill = fill;
			statusTextArea.setText("The current draw tool: " + canvases[canvas_control].getCommandString(Command.RECTANGLE));
			serviceAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(STROKE_RECT)));
		}
	}

	class Item_FillChanged implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			JRadioButton b = (JRadioButton)e.getSource();
			if(b.isSelected()){
				if(b.getText().equals("Fill")){
					fill = true;
					canvases[canvas_control].fill = true;
				}

				else{
					fill = false;
					canvases[canvas_control].fill = false;
				}

			}
		}
	}



	class Act_Eraser implements ActionListener{
		public void actionPerformed(ActionEvent e_ji9){
			eraserButton.setSelected(true);
			canvases[canvas_control].setCommand(Command.ERASER);
			serviceAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(STROKE_ERASER)));
		}
	}



	/**
	 * ������ʾ�¼�
	 */
	class Act_timer implements ActionListener {
		public void actionPerformed(ActionEvent e_time) {
			//Flash��ʾ��һ����4��ͼƬ�ļ�����˳����ʾ
			if (timerControl > 4){
				timerControl = 0;
			}
			flashLabel.setIcon(new ImageIcon(loadImage("image/Juggler" + timerControl
					+ ".gif")));
			timerControl++;
		}
	}

	/**
	 * ֹͣ�����¼�
	 */
	class Act_StopFlash implements ActionListener {
		public void actionPerformed(ActionEvent E_stop) {
			//Flash���ơ�ֹͣ����
			timer.stop();
			startFlashMenuItem.setEnabled(true);
			stopFlashMenuItem.setEnabled(false);
		}
	}

	/**
	 * ���������¼�
	 */
	class Act_StartFlash implements ActionListener {
		public void actionPerformed(ActionEvent E_start) {
			//Flash���ơ���������
			timer.start();
			startFlashMenuItem.setEnabled(false);
			stopFlashMenuItem.setEnabled(true);
		}
	}

	/**
	 * �ļ���������ֻ֧�ֱ༭".jwd"�ļ�
	 */
	class Filter extends FileFilter{
		//����FileFilter��accept����
		public boolean accept(File file1){
			 if (file1.isDirectory())
	                return true;
			 if (file1.getName().endsWith(".jpg"))
	                return true;
	            return false;
		}

		public String getDescription(){
			return ("*.jpg");
		}
	}

	//��ʽΪjpg���ļ�������
    private FileFilter jdrawFilter = new FileFilter() {
        public boolean accept(File f) {
            if (f.isDirectory())
                return true;
            if (f.getName().endsWith(".jpg"))
                return true;
            return false;
        }
        public String getDescription() {
            return "(*.jpg)";
        }

    };


	/**
	 * ��jar���ж�ȡͼƬ�ļ�
	 * @param name
	 * @return	����һ��ͼƬ����
	 */
	private Image loadImage(String name) {
		try {
			java.net.URL url = getClass().getResource(name);
			//����URL�������½�һ��ͼƬ�ļ�
			return createImage((java.awt.image.ImageProducer) url.getContent());
		} catch (Exception ex) {
			return null;
		}
	}


	class Act_Selected implements ActionListener{
		public void actionPerformed(ActionEvent e){
			canvases[canvas_control].redo();
			serviceAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(REDO)));
		}
	}

	class Act_UnSelected implements ActionListener{
		public void actionPerformed(ActionEvent e){
			canvases[canvas_control].undo();
			serviceAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(UNDO)));
		}
	}


	class Act_Clear implements ActionListener{
		public void actionPerformed(ActionEvent e){
			canvases[canvas_control].clearBoard();
			serviceAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(CLEAR_BOARD)));
		}
	}

	class Act_Pencil implements ActionListener{
		public void actionPerformed(ActionEvent e){
			canvases[canvas_control].setCommand(Command.PENCIL);
			statusTextArea.setText("The current draw tool: " + canvases[canvas_control].getCommandString(Command.PENCIL));
			ServerAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(STROKE_PEN)));
		}
	}

	class Act_Palette implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Color tempColor = JColorChooser.showDialog(dialogFrame, "��ɫ��", pencilColor);
			if(tempColor!=null){
				pencilColor = tempColor;
				canvases[canvas_control].setForegroundColor(pencilColor);
				fgButton.setBackground(pencilColor);
				serviceAction.sendData(new DataPackage(DataPackage.DataType.COLOR,new SelectColor(pencilColor.getRed(),pencilColor.getGreen(),pencilColor.getBlue())));
			}
		}
	}

	//�����ı�
	class Act_TextInsert implements ActionListener {
		public void actionPerformed(ActionEvent e_ji11) {
			canvases[canvas_control].setCommand(Command.TEXTINSERT);
		}
	}


	class Act_Deleted implements ActionListener{
		public void actionPerformed(ActionEvent e){
			canvases[canvas_control].clearBoard();
			serviceAction.sendData(new DataPackage(DataPackage.DataType.COMMAND,new com.rock.drawboard.module.Command(CLEAR_BOARD)));
		}
	}

    private void fgButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_fgButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                "��ѡ�����ͼ�ε���ɫ", canvases[canvas_control].getForeground());
        if (color != null) {
            canvases[canvas_control].setForeground(color);
            canvases[canvas_control].setForegroundColor(color);
            fgButton.setBackground(color);
        }
    }//GEN-LAST:event_fgButtonActionPerformed

    private void bgButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_bgButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                "��ѡ��ͼ�εı���ɫ", canvases[canvas_control].getBackground());
        if (color != null) {
            canvases[canvas_control].setBackground(color);
            canvases[canvas_control].setBackgroundColor(color);
            bgButton.setBackground(color);
        }
    }//GEN-LAST:event_bgButtonActionPerformed

    private void brButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_bgButtonActionPerformed
        Color color = JColorChooser.showDialog(this,
                "Change Board Background Color", canvases[canvas_control].getBackground());
        if (color != null) {
            canvases[canvas_control].setBrushColor(color);
            brButton.setBackground(color);
        }
    }

    private void eraserComboActionPerformed(ActionEvent evt) {//GEN-FIRST:event_eraserComboActionPerformed
    	eraserButton.setSelected(true);
    	canvases[canvas_control].setEraserIndex(eraserCombo.getSelectedIndex());
		switch (weightCombo.getSelectedIndex()) {
			case 0:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(15,STROKE_ERASER)));
				break;
			case 1:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(20,STROKE_ERASER)));
				break;
			case 2:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(30,STROKE_ERASER)));
				break;
			case 3:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(50,STROKE_ERASER)));
				break;
			case 4:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(100,STROKE_ERASER)));
				break;
		}
    }//GEN-LAST:event_eraserComboActionPerformed

    private void weightComboActionPerformed(ActionEvent evt) {//GEN-FIRST:event_weightComboActionPerformed
        canvases[canvas_control].setStrokeIndex(weightCombo.getSelectedIndex());
        switch (weightCombo.getSelectedIndex()) {
			case 0:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(5,STROKE_PEN)));
				break;
			case 1:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(8,STROKE_PEN)));
				break;
			case 2:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(10,STROKE_PEN)));
				break;
			case 3:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(15,STROKE_PEN)));
				break;
			case 4:
				ServerAction.sendData(new DataPackage(DataPackage.DataType.STROKE,new StrokeWidth(20,STROKE_PEN)));
				break;
		}
    }//GEN-LAST:event_weightComboActionPerformed

    class Act_SaveAs implements ActionListener{
		public void actionPerformed(ActionEvent e_ji9){
			 if (filechooser2 == null) {
				 filechooser2 = new JFileChooser();
				 filechooser2.setFileFilter(jdrawFilter);
				 filechooser2.setMultiSelectionEnabled(false);
				 filechooser2.setAcceptAllFileFilterUsed(false);
				 filechooser2.setCurrentDirectory(new File("./"));
	            }
	            int retVal = filechooser2.showSaveDialog(canvases[canvas_control]);
	            if (retVal != JFileChooser.APPROVE_OPTION)
	                return;
	            File file = filechooser2.getSelectedFile();
	            if (!file.getName().toLowerCase().endsWith(".jpg")) {
	            	//��ȡ���򿪵��ļ���
	    			String filename = filechooser2.getSelectedFile().getName();
	    			canvasTabbedPane.setTitleAt(tb-1, filename);
	    			filename = filename+ ".jpg";
	    			//��ȡ���򿪵��ļ����ڵ�Ŀ¼����Ŀ¼���������飬�����ڱ����ļ���ʱ���ܹ����ļ������浽Ŀ¼��
	    			directory[canvas_control] = filechooser2.getCurrentDirectory().toString();
	    			try {	    				
	    				canvases[canvas_control].SaveJpg();
	    				ImageToJpeg.toJpeg(new File(directory[canvas_control] + "/" + filename), canvases[canvas_control].offScreenImg);
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	            }		
		}
	}


}
