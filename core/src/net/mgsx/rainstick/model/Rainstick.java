package net.mgsx.rainstick.model;

public class Rainstick {

	public String title = "";
	public String credits = "";
	public String path = "";
	public String preview = "";
	public String description = "";
	public void set(Rainstick rainstick) {
		this.title = rainstick.title;
		this.credits = rainstick.credits;
		this.path = rainstick.path;
		this.preview = rainstick.preview;
		this.description = rainstick.description;
	}
}
