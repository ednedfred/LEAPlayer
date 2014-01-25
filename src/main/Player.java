package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class Player extends JFrame {

	//ID for serialization
	private static final long serialVersionUID = 728259366498524039L;
	
	private JPanel bPanel = new JPanel();
	
	private JButton pp = new JButton("Play");
	private JButton stop = new JButton("Stop");
	private JButton next = new JButton("Next");
	private JButton prev = new JButton("Prev");
	
	private DefaultListModel music = new DefaultListModel();
	private JList musicList = new JList(music);
	private JScrollPane scroll = new JScrollPane(musicList);
	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu file = new JMenu("File");
	
	private JMenuItem importFolder = new JMenuItem("Import");
	
	public Player()
	{
		setLayout(new BorderLayout());
		bPanel.setLayout(new GridLayout(1, 4));
		
		bPanel.add(prev);
		bPanel.add(pp);
		bPanel.add(stop);
		bPanel.add(next);
		
		for (int x = 0; x < 300; x++)
			music.addElement("Item " + x);
		
		add(scroll, BorderLayout.CENTER);
		add(bPanel, BorderLayout.SOUTH);
		
		file.add(importFolder);
		menuBar.add(file);
		setJMenuBar(menuBar);
		
		setLocationRelativeTo(null);
		setTitle("LEAPlayer v1.0");
		setSize(700, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

}
