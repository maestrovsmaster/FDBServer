package main;

public class Device {
	
	String name="";
	String id="";
	
	public Device(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof Device)
		{
			Device dev = (Device) obj;
			
			if(getId().equals(dev.getId())&&getName().equals(dev.getName())  ) return true;
			else return false;
		}
		else return false;
	}

}
