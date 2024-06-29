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

	private EffectType effectType;
	
	private String effectName;
	
	private EffectState effectState;
	
	private int offsetTicks;
	
	private float volumne;
	private float pitch;
	
	private transient boolean valid;
	
	
	public enum EffectType {
		sounds,
		visuals;
	}
	
	public enum EffectState {
		placed,
		explode,
		finished;
	}
	
	protected MineBombEffectsData() {
		super();
		
		this.valid = true;
	}
	
	public MineBombEffectsData(  
			String effectName, EffectState effectState, 
			int offsetTicks ) {
		super();
		
		this.effectType = null;
		
		this.effectName = effectName;
		this.effectState = effectState;
		this.offsetTicks = offsetTicks;
		
		this.volumne = 1.0f;
		this.pitch = 1.0f;
		
		this.valid = true;
	}
	
	
	public MineBombEffectsData( 
			String effectName, EffectState effectState, 
			int offsetTicks, float volume, float pitch ) {
		this( effectName, effectState, offsetTicks );
		
		this.volumne = volume;
		this.pitch = pitch;
	}
	
	
	public MineBombEffectsData clone() {
		MineBombEffectsData cloned = new MineBombEffectsData( 
				getEffectName(), getEffectState(), getOffsetTicks(),
				getVolumne(), getPitch() );
		
		cloned.setEffectType( getEffectType() );
		cloned.setValid( isValid() );
		
		return cloned;
	}

	@Override
	public String toString() {
		return getEffectName() + " (" + getEffectType().name() + 
				" state: " + getEffectState().name() + " offset: " + getOffsetTicks() +  
				" ticks  v: " + getVolumne() + "  p: " + getPitch() + ")";
	}
	public String toStringShort() {
		return getEffectName() + " (" + getEffectType().name() + 
				" state: " + getEffectState().name() + " offset: " + getOffsetTicks() +  
				" ticks)";
	}
	
	@Override
	public int compare( MineBombEffectsData o1, MineBombEffectsData o2 )
	{
		int results = o1.getEffectType().compareTo( o2.getEffectType() );
		
		if ( results == 0 ) {
			results = o1.getEffectState().compareTo( o2.getEffectState() );
			
			if ( results == 0 ) {
				results = Integer.compare( o1.getOffsetTicks(), o2.getOffsetTicks() );
				
				if ( results == 0 ) {
					results = o1.getEffectName().compareTo( o2.getEffectName() );
				}
			}
		}
		return results;
	}

	@Override
	public int compareTo( MineBombEffectsData o )
	{
		return compare( this, o );
	}

	
	public EffectType getEffectType() {
		return effectType;
	}
	public void setEffectType(EffectType effectType) {
		this.effectType = effectType;
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

	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
