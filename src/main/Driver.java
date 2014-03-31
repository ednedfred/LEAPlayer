package main;

import java.util.ArrayList;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Driver {

	public static MusicPlayer thePlayer;
	public static ArrayList<Song> theList;
	
	public static void main(String[] args)
	{
		theList = new ArrayList<Song>();
		
		checkFile();
		addSongs();
		thePlayer = new MusicPlayer(theList);
	}
	
	//Method that checks to see if the home file exists
	private static void checkFile()
	{
		String homepath = System.getProperty("user.home");
		
		File file = new File(homepath + "/.leaplayer");
		
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			//Throw Option Pane to let user know that a file has never been specified
			JOptionPane.showMessageDialog(null, "A root 'Music' folder has not been specified. Press 'OK' to specify.", "LEAPlayer v1.0", JOptionPane.PLAIN_MESSAGE);
			
			//Create a file chooser
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//In response to a button click:
			fc.showOpenDialog(null);
			
			String fullpath = fc.getCurrentDirectory() + "/" + fc.getSelectedFile().getName();
			
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pw.println(fullpath);
			pw.close();
		}
	}
	
	//Method that performs a BFS to add all songs in file tree to library
	private static void addSongs()
	{
		//Stack for BFS
		Stack<String> fStack = new Stack<String>();
		
		String homepath = System.getProperty("user.home");
		
		File file = new File(homepath + "/.leaplayer");
		
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		br = new BufferedReader(fr);
		
		String start = null;
		try {
			start = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fr.close();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fStack.push(start);
		
		File curDir = null;

		File allFiles[];

		while (!fStack.empty())
		{
			curDir = new File(fStack.pop());
			
			allFiles = curDir.listFiles();
			
			for (int x = 0; x < allFiles.length; x++)
			{
				if (allFiles[x].isFile() && allFiles[x].getName() != ".DS_Store")
					theList.add(new Song(allFiles[x].getName(), allFiles[x].getAbsolutePath()));
				else if (allFiles[x].getName() != "." && allFiles[x].getName() != "..")
					fStack.push(allFiles[x].getAbsolutePath());
			}
		}
		
		
		ArrayList<String> nonWavs = new ArrayList<String>();
		
		for (int x = 0; x < theList.size(); x++)
			if (!theList.get(x).getName().toLowerCase().contains(".wav"))
				nonWavs.add(theList.get(x).getName());
		
		for (String name : nonWavs)
		{
			for (int x = 0; x < theList.size(); x++)
				if (name == theList.get(x).getName())
				{
					theList.remove(x);
					break;
				}
		}
		
	}
}
