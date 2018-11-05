package application;

enum Process{
	Chart,
	Fetch,
	Analysis,
	Trading
}

public class Progress {
	/**
	 * Progress is an object with the goal of returning the user the progress of a certain
	 * process. These objects are intended to be displayed at the bottom right corner of the
	 * program, and will display to the user how much time has elapsed for a certain process.
	 */
	Process type;
	String name;
	int percent;
	int timeElapsed;
	public Progress(Process type, String name, int percent, int timeElpased){
		this.type = type;
		this.name = name;
		this.percent = percent;
		this.timeElapsed = timeElapsed;
	}
	
	// get methods
	public Process getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public int getPercent(){
		return percent;
	}
	
	public int getTime(){
		return timeElapsed;
	}
	
	// set methods
	public void setType(Process type){
		this.type = type;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setPercent(int percent){
		this.percent = percent;
	}
	
	public void setTime(int timeElapsed){
		this.timeElapsed = timeElapsed;
	}
	
	public void addTime(int time){
		this.timeElapsed = this.timeElapsed + time;
	}
}