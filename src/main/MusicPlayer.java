package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class MusicPlayer extends JFrame implements ActionListener, ChangeListener, DocumentListener {

	//ID for serialization
	private static final long serialVersionUID = 728259366498524039L;
	
	private ArrayList<Song> theList = null;
	private ArrayList<String> hiddenSongs = new ArrayList<String>();
	
	private JPanel bPanel = new JPanel();
	
	protected JButton pp = new JButton("Play");
	protected JButton stop = new JButton("Stop");
	protected JButton next = new JButton("Next");
	protected JButton prev = new JButton("Prev");
	
	private DefaultListModel music = new DefaultListModel();
	private JList musicList = new JList(music);
	private JScrollPane scroll = new JScrollPane(musicList);
	
	private JMenuBar menuBar = new JMenuBar();
	private JTextField search = new JTextField();
	
	private JMenu file = new JMenu("File");
	
	private JMenuItem importFolder = new JMenuItem("Import");

	private Player player = null;
	
	private JSlider volume = new JSlider(0, 20);
	
	//
	private int currentSong = 0;
	
	public MusicPlayer(ArrayList<Song> theList)
	{
		this.theList = theList;
		
		setLayout(new BorderLayout());
		bPanel.setLayout(new GridLayout(1, 5));
		
		bPanel.add(prev);
		bPanel.add(pp);
		bPanel.add(stop);
		bPanel.add(next);
		bPanel.add(volume);
		
		prev.addActionListener(this);
		pp.addActionListener(this);
		next.addActionListener(this);
		stop.addActionListener(this);
		
		//Add listener to search and volume
		search.getDocument().addDocumentListener(this);
		volume.addChangeListener(this);
		
		for (int x = 0; x < theList.size(); x++)
			music.addElement(theList.get(x).getName());
		
		musicList.setSelectedIndex(0);
		
		add(scroll, BorderLayout.CENTER);
		add(bPanel, BorderLayout.SOUTH);
		
		file.add(importFolder);
		menuBar.add(file);
		//To make the search bar right aligned
		menuBar.add(Box.createHorizontalStrut(500));
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(search);
		setJMenuBar(menuBar);
		
		setLocationRelativeTo(null);
		setTitle("LEAPlayer v1.0");
		setSize(700, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	@SuppressWarnings("deprecation")
	private void play(String pathName)
	{
		//Stops current song if playing
		if (player != null)
			player.stop();
		player = null;
		
		File f = new File(pathName);
		
		MediaLocator ml = null;
		try {
			ml = new MediaLocator(f.toURL());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			player = Manager.createPlayer(ml);
		} catch (NoPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.start();
	}

	//NOTE: The two most important methods will be 'play' and 'stop'
	//We need to work on those first
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == prev)
		{
			currentSong--;
			if (currentSong == 0)
				currentSong = music.size() - 1;
			
			String songName = music.get(currentSong).toString();
			String pathName = null;
			
			for (int x = 0; x < theList.size(); x++)
				if (theList.get(x).getName() == songName)
					pathName = theList.get(x).getPath();
			
			play(pathName);
		}
		
		if (arg0.getSource() == pp)
		{
			String songName = (String) musicList.getSelectedValue();
			String pathName = null;
			for (int x = 0; x < theList.size(); x++)
				if (theList.get(x).getName() == songName)
					pathName = theList.get(x).getPath();
			
			play(pathName);
		}
		
		if (arg0.getSource() == stop)
		{
			if (player != null)
				player.stop();
			player = null;
		}
		
		if (arg0.getSource() == next)
		{
			currentSong++;
			if (currentSong == music.size())
				currentSong = 0;
			
			musicList.setSelectedIndex(currentSong);
			
			String songName = music.get(currentSong).toString();
			String pathName = null;
			
			for (int x = 0; x < theList.size(); x++)
				if (theList.get(x).getName() == songName)
					pathName = theList.get(x).getPath();
			
			play(pathName);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		
		String searchText = search.getText();
		String songText = null;
		
		for (Object item : music.toArray()) {
			songText = item.toString();
		    if (!item.toString().toLowerCase().contains(searchText.toLowerCase()))
		    	hiddenSongs.add(songText);
		}
		
		for (String name : hiddenSongs)
		{
			for (int x = 0; x < music.size(); x++)
				if (name == music.get(x))
				{
					music.remove(x);
					break;
				}
		}
		
		musicList.setSelectedIndex(0);
		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		String searchText = search.getText();
		String songText = null;
		
		for (String name : hiddenSongs)
		{
			if (name.toLowerCase().contains(searchText.toLowerCase()) || searchText.isEmpty())
				music.addElement(name);
		}
		
		for (Object item : music.toArray()) {
			songText = item.toString();
			
			for (int x = 0; x < hiddenSongs.size(); x++)
				if (hiddenSongs.get(x) == songText)
				{
					hiddenSongs.remove(x);
					break;
				}
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		System.out.println(volume.getValue());
		float x = (float)volume.getValue();
		// slider min =0, slider max =20, any range can work
		  float volLevel = x/20.0f;
		// restrict volLevel to values from 0 to 1.
		  if (player!= null)                         
			  player.getGainControl().setLevel(volLevel);
		  musicList.setSelectedIndex(0);
	}


}
