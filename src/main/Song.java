package main;

public class Song {

	private String name;
	private String path;
	
	public Song()
	{
		this.name = null;
		this.path = null;
	}
	
	public Song(String name, String path)
	{
		this.name = name;
		this.path = path;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
}
