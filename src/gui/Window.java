package gui;

import java.awt.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.GroupLayout.Alignment;
import net.sourceforge.vietocr.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;
import net.sourceforge.tess4j.*;

public class Window {

	private JFrame mainFrame = new JFrame();
	private int noOfLanguages;
	float scaleX = 1f;
	float scaleY = 1f;
	static String selectedUILang = "en";
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
	//	private static final String strCurrentDirectory = "currentDirectory";
	//	private static final String strOutputDirectory = "outputDirectory";
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

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {

		imageLabel.setIcon(null);
		noOfLanguages=1;//to be made dynamic
		availableLanguages = new String[noOfLanguages];
		availableLanguagesCodes = new String[noOfLanguages];
		availableLanguages[0]="English";
		availableLanguagesCodes[0]="eng";

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
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);

		JMenu mnPageSegmentationMode = new JMenu("Page Segmentation Mode");
		mnSettings.add(mnPageSegmentationMode);

		JMenuItem menuItem = new JMenuItem("0");
		mnPageSegmentationMode.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem("1");
		mnPageSegmentationMode.add(menuItem_1);

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

		fileOpener.setDialogTitle(jButtonOpen.getToolTipText()); // NOI18N
		FileFilter allImageFilter = new SimpleFilter("bmp;gif;jpg;jpeg;jp2;png;pnm;pbm;pgm;ppm;tif;tiff", "All_Image_Files");
		FileFilter bmpFilter = new SimpleFilter("bmp", "Bitmap");
		FileFilter gifFilter = new SimpleFilter("gif", "GIF");
		FileFilter jpegFilter = new SimpleFilter("jpg;jpeg", "JPEG");
		FileFilter jpeg2000Filter = new SimpleFilter("jp2", "JPEG 2000");
		FileFilter pngFilter = new SimpleFilter("png", "PNG");
		FileFilter pnmFilter = new SimpleFilter("pnm;pbm;pgm;ppm", "PNM");
		FileFilter tiffFilter = new SimpleFilter("tif;tiff", "TIFF");

		FileFilter pdfFilter = new SimpleFilter("pdf", "PDF");
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
		//fileOpener.addChoosableFileFilter(pdfFilter);

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
			public void mouseClicked(MouseEvent e) {
				showHelp();
			}
		});

		mntmAboutJavaOcr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
			//			oldImageWidth = imageIcon.getIconWidth();
			//			oldImageHeight = imageIcon.getIconHeight();

			//			Dimension fitSize = fitImagetoContainer(oldImageWidth, oldImageHeight, scrollPane.getViewport().getWidth(), scrollPane.getViewport().getHeight());
			//			imageIcon.setScaledSize(fitSize.width, fitSize.height);
			//			scaleX=fitSize.width;
			//			scaleY=fitSize.height;

			//		imageLabel.setIcon(imageIcon);
			//		scrollPane.getViewport().setViewPosition(curScrollPos = new Point());
			//		imageLabel.revalidate();
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
				// TODO Auto-generated catch block
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
				doOCR(rect);
			} catch (RasterFormatException rfe) {
				JOptionPane.showMessageDialog(mainFrame, rfe.getMessage(), APP_NAME, JOptionPane.ERROR_MESSAGE);
				//                rfe.printStackTrace();
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
//		instance.setDatapath(new File(tessPath, TESSDATA).getPath());
//		System.out.println(new File(tessPath, TESSDATA).getPath());
//		instance.setLanguage("eng");
		instance.setPageSegMode(3);
		try {
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
