import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of plants.
 * 
 * @author Spring Java 2024 Class Project
 * @version 2024.00
 * Reference David J. Barnes and Michael Kölling, version 2016.02.29
 */
public abstract class Plant
{
    // Whether the plant is alive or not.
    private boolean alive;
    // The plant's field.
    private Field field;
    // The plant's position in the field.
    private Location location;
    //Whether the location (and the plant) is on fire.
    private boolean isOnFire;
    
    // A shared random number generator to control growth (fire, here).
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a new plant at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        isOnFire = location.isOnFire();
    }
    
    /**
     * Make this plant grow
     * @param newPlants A list to receive newly born plants.
     */
    abstract public void exist(List<Plant> newPlants, int step);

    /**
     * Check whether the plant is alive or not.
     * @return true if the plant is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the plant is no longer alive (in the event of fire or being eaten).
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            location.decrementNumberOfPlants();
            //field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the plant's location.
     * @return The plant's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the plant at the new location in the given field.
     * Used at initial setting of plant location.
     * @param newLocation The plant's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            location.incrementNumberOfPlants();
            //field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the plant's field.
     * @return The plant's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Return the plant's field.
     * @return The plant's field.
     */
    protected Plant fireBehavior(double probabilityOfDeath)
    {
        //From Tree
        if(this.getLocation().isOnFire()) {
            
	    if (this.isAlive() && rand.nextDouble() <= probabilityOfDeath) {
		this.setDead();
	    }
	}
        return this;
    }
}
