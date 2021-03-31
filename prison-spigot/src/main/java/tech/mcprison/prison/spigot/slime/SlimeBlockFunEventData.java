package tech.mcprison.prison.spigot.slime;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import tech.mcprison.prison.util.Text;

public class SlimeBlockFunEventData {

	private Long playerUUIDLSB;
	private long timestamp;
	
	private double initialY = 0.0;
	private double maxY = 0.0;
	private double boost = 0.0;
	private int boostCount = 0;
	private double velocityY = 0.0;
	private boolean jumping = false;
	
	private double recordHeight = 0.0;
	private double recordY = 0.0;
	private double recordBoost = 0.0;
	private double recordVelocity = 0.0;
	
	private DecimalFormat sFmt = new DecimalFormat("#,##0.0");
	private DecimalFormat dFmt = new DecimalFormat("#,##0.00");
	
	public SlimeBlockFunEventData( Long playerUUIDLSB, double y ) {
		super();
		
		this.playerUUIDLSB = playerUUIDLSB;
		this.timestamp = System.currentTimeMillis();
		
		this.initialY = y;
		this.maxY = y;
		this.jumping = false;
		this.recordHeight = 0.0;
		this.recordY = 0.0;
	}

	public void addJumpEvent( double y, double boost, double velocityY ) {
		
		long currentTime = System.currentTimeMillis();
		if ( !isJumping() ) {
			this.boostCount = 0;
			this.jumping = true;

			this.initialY = y;
		}
		
		this.timestamp = currentTime;
		
		this.maxY = y;
		this.boost = boost;
		this.velocityY = velocityY;
		
		this.boostCount++;
	}

	public void inAir( double currentY, Player player ) {
		if ( isJumping() ) {
			
			if ( currentY > getMaxY() ) {
				// Going up!  Keep recording height!
				setMaxY( currentY );
			}
			else if ( currentY < getMaxY() ) {
				
				if ( currentY >= 255 ) {
					player.sendMessage( "SlimeFun: You jumped out of the world! " + 
							" y= " + dFmt.format( currentY ));
				}

				// Just starting to go back down!!
				// Stop recording the jump:
				atTopOfJump( player );
			}
		}
	}

	private void atTopOfJump( Player player )
	{
		double height = getMaxY() - getInitialY();

		boolean recY = ( getMaxY() > getRecordY() );
		boolean recH = ( height > getRecordHeight() );
		boolean recB = ( getBoost() > getRecordBoost() );
		boolean recV = ( getVelocityY() > getRecordVelocity() );
		
		if ( recY ) {
			setRecordY( getMaxY() );
		}
		if ( recH ) {
			setRecordHeight( height );
		}
		if ( recB ) {
			setRecordBoost( getBoost() );
		}
		if ( recV ) {
			setRecordVelocity( getVelocityY() );
		}
		
		String message1 = Text.translateAmpColorCodes(  
				String.format("&a%s  &3Height: &7%s    &a%s      &3maxY: &7%s  &a%s",
						(recY || recH || recB || recV ? "&6.-=New=-." : "__Slime__"),
						sFmt.format(height), 
						(recH ? "&6" : "" ) + sFmt.format( getRecordHeight() ),
						sFmt.format(getMaxY()), 
						(recY ? "&6" : "" ) + sFmt.format( getRecordY())
						));
		
		String message2 = Text.translateAmpColorCodes(  
				String.format("&a%s  &3Boost: &7%s  &b%s  &a%s   &3Velocity: &7%s  &a%s",   
						(recY || recH || recB || recV ? "&6-Record!-" : "__Fun!!__"),
						
						dFmt.format(getBoost()), Integer.toString( getBoostCount()),
						(recB ? "&6" : "" ) + dFmt.format( getRecordBoost() ),
						dFmt.format(getVelocityY()),
						(recV ? "&6" : "" ) + dFmt.format(getRecordVelocity())
						));
		player.sendMessage( message1 );
		player.sendMessage( message2 );
		
		// Reset key values:
		setJumping( false );
		setBoostCount( 0 );
		setMaxY( getInitialY() );
	}
	
	public boolean hasLanded(Player p) {
		boolean wasJumping = false;
		
		long inAir = System.currentTimeMillis() - getTimestamp();
		
		if ( inAir <= (16 * 1000) ) {
			wasJumping = true;
			
			setJumping( false );
			setBoostCount( 0 );
			setBoost( 1.0 );
			//setTimestamp( 0 );
		} 
		
		return wasJumping;
	}
	
	public Long getPlayerUUIDLSB() {
		return playerUUIDLSB;
	}
	public void setPlayerUUIDLSB( Long playerUUIDLSB ) {
		this.playerUUIDLSB = playerUUIDLSB;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp( long timestamp ) {
		this.timestamp = timestamp;
	}

	public double getInitialY() {
		return initialY;
	}
	public void setInitialY( double initialY ) {
		this.initialY = initialY;
	}

	public double getMaxY() {
		return maxY;
	}
	public void setMaxY( double maxY ) {
		this.maxY = maxY;
	}

	public double getBoost() {
		return boost;
	}
	public void setBoost( double boost ) {
		this.boost = boost;
	}

	public int getBoostCount() {
		return boostCount;
	}
	public void setBoostCount( int boostCount ) {
		this.boostCount = boostCount;
	}

	public double getVelocityY() {
		return velocityY;
	}
	public void setVelocityY( double velocityY ) {
		this.velocityY = velocityY;
	}

	public boolean isJumping() {
		return jumping;
	}
	public void setJumping( boolean jumping ) {
		this.jumping = jumping;
	}

	public double getRecordHeight() {
		return recordHeight;
	}
	public void setRecordHeight( double recordHeight ) {
		this.recordHeight = recordHeight;
	}

	public double getRecordBoost() {
		return recordBoost;
	}
	public void setRecordBoost( double recordBoost ) {
		this.recordBoost = recordBoost;
	}

	public double getRecordY() {
		return recordY;
	}
	public void setRecordY( double recordY ) {
		this.recordY = recordY;
	}

	public double getRecordVelocity() {
		return recordVelocity;
	}
	public void setRecordVelocity( double recordVelocity ) {
		this.recordVelocity = recordVelocity;
	}
	
}
