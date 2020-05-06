package tech.mcprison.prison.spigot.player;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

public class OnPlayerMoveEventData {

	private Long playerUUIDLSB;
	private long timestamp;
	
	private double initialY = 0.0;
	private double maxY = 0.0;
	private double boost = 0.0;
	private int boostCount = 0;
	private double velocityY = 0.0;
	private boolean jumping = false;
	
	private double recordHeight = 0.0;
	private double maxBoost = 0.0;
	
	private DecimalFormat fFmt = new DecimalFormat("#,##0.00");
//	private DecimalFormat f4Fmt = new DecimalFormat("#,##0.0000");
	
	public OnPlayerMoveEventData( Long playerUUIDLSB, double y ) {
		super();
		
		this.playerUUIDLSB = playerUUIDLSB;
		this.timestamp = System.currentTimeMillis();
		
		this.initialY = y;
		this.maxY = y;
		this.jumping = false;
		this.recordHeight = 0.0;
	}

	public void addJumpEvent( double y, double boost, double velocityY ) {
		
		long currentTime = System.currentTimeMillis();
		if ( !isJumping() ) { // && (currentTime - this.timestamp) > 1000 ) {
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
			
			if ( currentY >= 255 ) {
				player.sendMessage( "SlimeBlockFun: You jumped out of the world! " + 
							" y= " + currentY );
				
				atTopOfJump( player );
				
//				Vector velocity = player.getVelocity();
//				velocity.setY( 0 );
//				player.setVelocity( velocity );
			}
			else if ( currentY > getMaxY() ) {
				// Going up!  Keep recording height!
				setMaxY( currentY );
			}
			else if ( currentY < getMaxY() ) {
				// Just starting to go back down!!
				// Stop recording the jump:
				atTopOfJump( player );
			}
		}
	}

	private void atTopOfJump( Player player )
	{
		double height = getMaxY() - getInitialY();

		player.sendMessage( "SlimeBlockFun: h:" +  
				fFmt.format(height) + 
				" (y:" + fFmt.format(getMaxY()) + ") " +
				"boost:" + fFmt.format(getBoost()) + 
				" (" + getBoostCount() +
				") velY:" + fFmt.format(getVelocityY()) );
		
		if ( height > getRecordHeight() ) {
			setRecordHeight( height );
			
			player.sendMessage( 
					"Congrats! New height record! " +
							fFmt.format( getRecordHeight()) );
		}
		
		// Reset key values:
		setJumping( false );
		setBoostCount( 0 );
		setMaxY( getInitialY() );
	}
	
	public boolean hasLanded(Player p) {
		boolean jumping = false;
		
		long inAir = System.currentTimeMillis() - getTimestamp();
		
		if ( inAir <= (16 * 1000) ) {
			jumping = true;
			
			setJumping( false );
			setBoostCount( 0 );
			setBoost( 1.0 );
			setTimestamp( 0 );
		} 
		
		return jumping;
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

	public double getMaxBoost() {
		return maxBoost;
	}
	public void setMaxBoost( double maxBoost ) {
		this.maxBoost = maxBoost;
	}
	
}
