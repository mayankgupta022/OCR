import java.awt.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import net.sourceforge.vietocr.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.filechooser.FileFilter;
import net.sourceforge.tess4j.*;

public class Window {

	private JFrame mainFrame = new JFrame();
	private int noOfLanguages;
	float scaleX = 1f;
	float scaleY = 1f;
	int newImageHeight;
	int newImageWidth;
	Point curScrollPos;
	private String[] availableLanguages;
	private String[] availableLanguagesCodes;
	public String filename;
	BufferedImage image = null;
	private static final String APP_NAME = "Java OCR";
	public static final String TESSERACT_PATH = "tesseract-ocr";
	protected final File baseDir = Utilities.getBaseDir(Window.this);
	final String TESSDATA = "tessdata";
	protected String tessPath;
	int pagemode=3;
	private static final String helpMsg = "Help of App comes here";
	private static final String aboutMsg = "About msg comes here";
	final JImageLabel imageLabel = new JImageLabel();
	final JTextArea textArea = new JTextArea();
	JComboBox comboBox = new JComboBox();
	JSplitPane splitPane = new JSplitPane();
	JScrollPane scrollPane = new JScrollPane();
	final Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	final JFileChooser fileOpener = new JFileChooser();
	final JFileChooser fileSaver = new JFileChooser();

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public Window() {
		initialize();
	}

	

	private void initialize() {

		imageLabel.setIcon(null);
		noOfLanguages=2;//to be made dynamic
		availableLanguages = new String[noOfLanguages];
		availableLanguagesCodes = new String[noOfLanguages];
		availableLanguages[0]="English";
		availableLanguagesCodes[0]="eng";
		availableLanguages[1]="Hindi";
		availableLanguagesCodes[1]="hin";

		mainFrame.setTitle(APP_NAME);
		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/gui/icons/ocr.png")));
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setSize(screen.width, screen.height);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainFrame.getContentPane().setLayout(new java.awt.BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFocusTraversalKeysEnabled(true);
		mainFrame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open Image");
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save Text");
		mnFile.add(mntmSave);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		ButtonGroup groupPSM = new ButtonGroup();
		JMenu mnPageSegmentationMode = new JMenu("Page Segmentation Mode");
		menuBar.add(mnPageSegmentationMode);
		JRadioButton menuItem0 = new JRadioButton("0 = Orientation and script detection (OSD) only.");
		menuItem0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=0;
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem0);
		mnPageSegmentationMode.add(menuItem0);

		JRadioButton menuItem1 = new JRadioButton("1 = Automatic page segmentation with OSD.");
		menuItem1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=1;
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem1);
		mnPageSegmentationMode.add(menuItem1);

		JRadioButton menuItem2 = new JRadioButton("2 = Automatic page segmentation, but no OSD, or OCR");
		menuItem2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=2; 
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem2);
		mnPageSegmentationMode.add(menuItem2);

		JRadioButton menuItem3 = new JRadioButton("3 = Fully automatic page segmentation, but no OSD. (Default)");
		menuItem3.setSelected(true);
		menuItem3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=3; 
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem3);
		mnPageSegmentationMode.add(menuItem3);

		JRadioButton menuItem4 = new JRadioButton("4 = Assume a single column of text of variable sizes.");
		menuItem4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=4;
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem4);
		mnPageSegmentationMode.add(menuItem4);

		JRadioButton menuItem5 = new JRadioButton("5 = Assume a single uniform block of vertically aligned text.");
		menuItem5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=5;
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem5);
		mnPageSegmentationMode.add(menuItem5);

		JRadioButton menuItem6 = new JRadioButton("6 = Assume a single uniform block of text.");
		menuItem6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=6;
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem6);
		mnPageSegmentationMode.add(menuItem6);

		JRadioButton menuItem7 = new JRadioButton("7 = Treat the image as a single text line.");
		menuItem7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=7;
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem7);
		mnPageSegmentationMode.add(menuItem7);

		JRadioButton menuItem8 = new JRadioButton("8 = Treat the image as a single word.");
		menuItem8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=8;
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem8);
		mnPageSegmentationMode.add(menuItem8);

		JRadioButton menuItem9 = new JRadioButton("9 = Treat the image as a single word in a circle.");
		menuItem9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=9;
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem9);
		mnPageSegmentationMode.add(menuItem9);

		JRadioButton menuItem10 = new JRadioButton("10 = Treat the image as a single character.");
		menuItem10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				pagemode=10; 
				System.out.println(pagemode);
			}
		});
		groupPSM.add(menuItem10);
		mnPageSegmentationMode.add(menuItem10);

		;

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmJavaOcrHelp = new JMenuItem(APP_NAME + " Help");

		mnHelp.add(mntmJavaOcrHelp);

		JMenuItem mntmAboutJavaOcr = new JMenuItem("About " + APP_NAME);
		mnHelp.add(mntmAboutJavaOcr);

		JToolBar toolBar = new JToolBar();
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton jButtonOpen = new JButton("");
		toolBar.add(jButtonOpen);
		jButtonOpen.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/open.png")));
		jButtonOpen.setToolTipText("Open Image");

		JButton jButtonSave = new JButton("");
		toolBar.add(jButtonSave);
		jButtonSave.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/save.png")));
		jButtonSave.setToolTipText("Save Text");

		JButton jButtonOcr = new JButton("");
		toolBar.add(jButtonOcr);
		jButtonOcr.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/ocr.png")));
		jButtonOcr.setToolTipText("Perform OCR");

		toolBar.add(Box.createHorizontalGlue());

		JLabel lblSelectLangauge = new JLabel("Select Langauge");
		lblSelectLangauge.setAlignmentX(Component.RIGHT_ALIGNMENT);
		toolBar.add(lblSelectLangauge);

		DefaultComboBoxModel model = new DefaultComboBoxModel(availableLanguages);

		comboBox.setMaximumSize(new Dimension(200, 24));
		comboBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
		comboBox.setModel(model);
		toolBar.add(comboBox);
		mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);

		mainFrame.getContentPane().add(splitPane, BorderLayout.CENTER);


		splitPane.setRightComponent(textArea);

		splitPane.setLeftComponent(scrollPane);
		splitPane.setDividerLocation(screen.width/2);
		imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		scrollPane.setViewportView(imageLabel);

		fileOpener.setDialogTitle(jButtonOpen.getToolTipText());
		FileFilter allImageFilter = new SimpleFilter("bmp;gif;jpg;jpeg;jp2;png;pnm;pbm;pgm;ppm;tif;tiff", "All_Image_Files");
		FileFilter bmpFilter = new SimpleFilter("bmp", "Bitmap");
		FileFilter gifFilter = new SimpleFilter("gif", "GIF");
		FileFilter jpegFilter = new SimpleFilter("jpg;jpeg", "JPEG");
		FileFilter jpeg2000Filter = new SimpleFilter("jp2", "JPEG 2000");
		FileFilter pngFilter = new SimpleFilter("png", "PNG");
		FileFilter pnmFilter = new SimpleFilter("pnm;pbm;pgm;ppm", "PNM");
		FileFilter tiffFilter = new SimpleFilter("tif;tiff", "TIFF");

		FileFilter textFilter = new SimpleFilter("txt", "UTF-8_Text");

		fileOpener.setAcceptAllFileFilterUsed(false);
		fileOpener.addChoosableFileFilter(allImageFilter);
		fileOpener.addChoosableFileFilter(bmpFilter);
		fileOpener.addChoosableFileFilter(gifFilter);
		fileOpener.addChoosableFileFilter(jpegFilter);
		fileOpener.addChoosableFileFilter(jpeg2000Filter);
		fileOpener.addChoosableFileFilter(pngFilter);
		fileOpener.addChoosableFileFilter(pnmFilter);
		fileOpener.addChoosableFileFilter(tiffFilter);

		fileSaver.setAcceptAllFileFilterUsed(false);
		fileSaver.addChoosableFileFilter(textFilter);	



		mntmOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				fileOpen();
			}

		});

		mntmSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				fileSave();
			}
		});

		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				quit();
			}
		});

		mntmJavaOcrHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				showHelp();
			}
		});

		mntmAboutJavaOcr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				showAbout();
			}
		});

		jButtonOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				fileOpen();
			}
		});

		jButtonSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				fileSave();
			}
		});

		jButtonOcr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				performOcr();
			}
		});

	}

	public void fileOpen() {

		int rVal = fileOpener.showOpenDialog(mainFrame);
		File file = fileOpener.getSelectedFile();
		if(rVal == JFileChooser.APPROVE_OPTION) {
			try
			{
				image = ImageIO.read(file);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.exit(1);
			}
			ImageIconScalable imageIcon = new ImageIconScalable(image);

			newImageHeight=imageLabel.getHeight();
			newImageWidth=imageLabel.getWidth();
			newImageWidth=(imageIcon.getIconWidth()*newImageHeight)/imageIcon.getIconHeight();
			if(newImageWidth>imageLabel.getWidth())
			{	
				newImageWidth=imageLabel.getWidth();
				newImageHeight=(imageIcon.getIconHeight()*newImageWidth)/imageIcon.getIconWidth();
			}

			scaleX = (float) imageIcon.getIconWidth() / newImageWidth;
			scaleY=scaleX;
			imageIcon.setScaledSize(newImageWidth, newImageHeight);
			imageLabel.setIcon(imageIcon);

		}
	}


	public void fileSave() {
		int rVal = fileSaver.showSaveDialog(mainFrame);
		if(rVal == JFileChooser.APPROVE_OPTION) {
			File file = fileSaver.getSelectedFile();
			BufferedWriter out;
			try {
				if(file.getName().endsWith(".txt"))
					out = new BufferedWriter(new FileWriter(file));
				else
					out = new BufferedWriter(new FileWriter(file + ".txt"));
				out.write(textArea.getText());
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public void performOcr() {
		if (imageLabel.getIcon() == null) {
			JOptionPane.showMessageDialog(mainFrame ,"Please load an image.", APP_NAME, JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		Rectangle rect = ((JImageLabel) imageLabel).getRect();

		if (rect != null) {
			try {
				ImageIcon ii = (ImageIcon) this.imageLabel.getIcon();
				int offsetX = 0;
				int offsetY = 0;
				if (ii.getIconWidth() < this.scrollPane.getWidth()) {
					offsetX = (this.scrollPane.getViewport().getWidth() - ii.getIconWidth()) / 2;
				}
				if (ii.getIconHeight() < this.scrollPane.getHeight()) {
					offsetY = (this.scrollPane.getViewport().getHeight() - ii.getIconHeight()) / 2;
				}
				rect = new Rectangle((int) ((rect.x - offsetX) * scaleX), (int) ((rect.y - offsetY) * scaleY), (int) (rect.width * scaleX), (int) (rect.height * scaleY));
				if( rect.width<5 || rect.height<5)
					rect=null;
				doOCR(rect);
			} catch (RasterFormatException rfe) {
				JOptionPane.showMessageDialog(mainFrame, rfe.getMessage(), APP_NAME, JOptionPane.ERROR_MESSAGE);
				rfe.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			doOCR(null);
		}
	}

	public void doOCR(Rectangle rect)
	{
		Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
		// Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping
		tessPath = new File(baseDir, TESSERACT_PATH).getPath();
				instance.setDatapath(new File(tessPath, TESSDATA).getPath());
				System.out.println(new File(tessPath, TESSDATA).getPath());
		instance.setPageSegMode(pagemode);
		try {
			instance.setLanguage(availableLanguagesCodes[comboBox.getSelectedIndex()]);
			String result = instance.doOCR(image,rect);
			textArea.setText(result);
		} catch (TesseractException e) {
			System.err.println(e.getMessage());
		}
	}

	public void showHelp()
	{
		JOptionPane.showMessageDialog(mainFrame, helpMsg, APP_NAME + "Help", JOptionPane.PLAIN_MESSAGE);
		return;
	}

	public void showAbout()
	{
		JOptionPane.showMessageDialog(mainFrame, aboutMsg, "About" + APP_NAME, JOptionPane.PLAIN_MESSAGE);
		return;
	}

	void quit() {
		System.exit(0);
	}

}
