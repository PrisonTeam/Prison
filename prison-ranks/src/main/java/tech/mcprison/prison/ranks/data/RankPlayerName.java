package tech.mcprison.prison.ranks.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RankPlayerName {

	private String name;
	private long date;
	
	public RankPlayerName( String name, long date ) {
		super();
		
		this.name = name;
		this.date = date;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return name + " " + sdf.format( new Date(date) );
	}
	
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}

	public long getDate() {
		return date;
	}
	public void setDate( long date ) {
		this.date = date;
	}
	
}
