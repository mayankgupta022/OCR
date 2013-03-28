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

public class Window {

	private JFrame frame;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
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
	
	private int snap(final int ideal, final int min, final int max) {
        final int TOLERANCE = 0;
        return ideal < min + TOLERANCE ? min : (ideal > max - TOLERANCE ? max : ideal);
    }
	
	void quit() {
		return;
	}
	private void initialize() {

		JImageLabel imageLabel = new JImageLabel();
		final Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		
		frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(screen.width, screen.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFocusTraversalKeysEnabled(true);
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				quit();
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmJavaOcrHelp = new JMenuItem("Java OCR Help");
		mnHelp.add(mntmJavaOcrHelp);
		
		JMenuItem mntmAboutJavaOcr = new JMenuItem("About Java OCR");
		mnHelp.add(mntmAboutJavaOcr);
		
		frame.getContentPane().setLayout(new java.awt.BorderLayout());
		
		JToolBar toolBar = new JToolBar();
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JButton jButtonOpen = new JButton("");
		toolBar.add(jButtonOpen);
		jButtonOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		jButtonOpen.setToolTipText("Open Image");
		jButtonOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		jButtonOpen.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/open.png")));
		
		JButton jButtonSave = new JButton("");
		toolBar.add(jButtonSave);
		jButtonSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		jButtonSave.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/save.png")));
		jButtonSave.setToolTipText("Save Text");
		
		JButton jButtonOcr = new JButton("");
		toolBar.add(jButtonOcr);
		jButtonOcr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		jButtonOcr.setToolTipText("Perform OCR");
		jButtonOcr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		jButtonOcr.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/ocr.png")));
		
		toolBar.add(Box.createHorizontalGlue());
		
		JLabel lblSelectLangauge = new JLabel("Select Langauge");
		lblSelectLangauge.setAlignmentX(Component.RIGHT_ALIGNMENT);
		toolBar.add(lblSelectLangauge);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
		toolBar.add(comboBox);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		splitPane.setRightComponent(textArea);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		splitPane.setDividerLocation(screen.width/2);
		scrollPane.setViewportView(imageLabel);
	}
}
