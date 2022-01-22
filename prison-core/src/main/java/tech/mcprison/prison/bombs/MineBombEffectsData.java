package tech.mcprison.prison.bombs;

import java.util.Comparator;

/**
 * <p>The effectsName that is used should be one of the following, or it will not work. 
 * These effects depend upon the server's version too.  If what you enter does not work,
 * then try something else what would be compatible.  It's important to test to make
 * sure.</p>
 * 
 * <p>For Sound Effects:
 * </p>
 * 
 * 
 * 
 * <p>For Visual Effects:
 * </p>
 * https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html
 *
 */
public class MineBombEffectsData
	implements Comparator<MineBombEffectsData>,
				Comparable<MineBombEffectsData>
{

	private String effectName;
	
	private EffectState effectState;
	
	private int offsetTicks;
	
	private float volumne;
	private float pitch;
	
	public enum EffectState {
		placed,
		explode,
		finished;
	}
	
	protected MineBombEffectsData() {
		super();
	}
	
	public MineBombEffectsData( String effectName, EffectState effectState, 
			int offsetTicks ) {
		super();
		
		this.effectName = effectName;
		this.effectState = effectState;
		this.offsetTicks = offsetTicks;
		
		this.volumne = 1.0f;
		this.pitch = 1.0f;
	}
	
	
	public MineBombEffectsData( String effectName, EffectState effectState, 
			int offsetTicks, float volume, float pitch ) {
		this( effectName, effectState, offsetTicks );
		
		this.volumne = volume;
		this.pitch = pitch;
	}
	
	
	public MineBombEffectsData clone() {
		return new MineBombEffectsData( getEffectName(), getEffectState(), getOffsetTicks(),
				getVolumne(), getPitch() );
	}

	@Override
	public String toString() {
		return getEffectName() + " (state: " + getEffectState().name() + " offset: " + getOffsetTicks() +  
				" ticks  v: " + getVolumne() + "  p: " + getPitch() + ")";
	}
	public String toStringShort() {
		return getEffectName() + " (state: " + getEffectState().name() + " offset: " + getOffsetTicks() +  
				" ticks)";
	}
	
	@Override
	public int compare( MineBombEffectsData o1, MineBombEffectsData o2 )
	{
		int results = o1.getEffectState().compareTo( o2.getEffectState() );
		
		if ( results == 0 ) {
			results = Integer.compare( o1.getOffsetTicks(), o2.getOffsetTicks() );
			
			if ( results == 0 ) {
				results = o1.getEffectName().compareTo( o2.getEffectName() );
			}
		}
		return results;
	}

	@Override
	public int compareTo( MineBombEffectsData o )
	{
		return compare( this, o );
	}

	
	public String getEffectName() {
		return effectName;
	}
	public void setEffectName( String effectName ) {
		this.effectName = effectName;
	}

	public EffectState getEffectState() {
		return effectState;
	}
	public MineBombEffectsData setEffectState( EffectState effectState ) {
		this.effectState = effectState;
		
		return this;
	}

	public int getOffsetTicks() {
		return offsetTicks;
	}
	public MineBombEffectsData setOffsetTicks( int offsetTicks ) {
		this.offsetTicks = offsetTicks;
		
		return this;
	}

	public float getVolumne() {
		return volumne;
	}
	public MineBombEffectsData setVolumne( float volumne ) {
		this.volumne = volumne;
		
		return this;
	}

	public float getPitch() {
		return pitch;
	}
	public MineBombEffectsData setPitch( float pitch ) {
		this.pitch = pitch;
		
		return this;
	}

}
