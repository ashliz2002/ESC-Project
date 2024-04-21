import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of grass.
 * Grass - each unit of grass can produce a new unit of grass every two turns 
 * (that is every other turn) - 
 * 
 * it reproduces in its own square or in an adjacent square 
 * (which direction it spreads, including its own square, is random)
 * 
 * - it never ages - it only dies through a fire or being eaten - 
 * 
 * you can have a maximum of ten units of vegetable material 
 * (that is, grass and trees combined) on any given square -
 * 
 * in the event of a fire, any unit of grass has a 75% chance of dying.
 * 
 * @author Spring Java 2024 Class Project
 * @version 2024.00
 * Reference David J. Barnes and Michael Kölling, version 2016.02.29
 */
public class Grass extends Plant
{
    // Characteristics shared by all grass (class variables).
    
    // The rate of growth 
    // - grass grows when current step % GROWTH_RATE = 0 AND current step > 1
    private static final int GROWTH_RATE = 2;
    
    // in the event of a fire, any unit of Tree has a 75% chance of dying
    private static final double PROBABILITY_OF_DEATH = 0.75;
    
    // A shared random number generator to control growth.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    private boolean isOnFire;
    

    /**
     * Create a unit of grass.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Grass(Field field, Location location)
    {
        super(field, location);
        isOnFire = getLocation().isOnFire();
    }
    
    /**
     * Grass grows, producing new grass every two turns.
     * @param field The field currently occupied.
     * @param newGrass A list to return new grass.
     */
    public void exist(List<Plant> newGrass, int currentStep)
    {
        if(isAlive()) {
            if(getLocation().isOnFire()){
                //fire handling
                this.fireBehavior(PROBABILITY_OF_DEATH);
            }
            
            if(currentStep % GROWTH_RATE == 0){
                grow(newGrass);
            }
        }
    }
    
    /**
     * Check whether or not this grass grows at this step.
     * New grass will grow into free adjacent locations.
     * @param newGrass A list to return new grass growth.
     */
    private void grow(List<Plant> newGrasses)
    {
        // New trees grow into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        
        //Multiple plants can grow at the same location, 
        //so current location is fair game if not full
        Location current = this.getLocation();
        if(current.getNumberOfPlants() < 9) {
            free.add(current);
        }
        
        // Shuffle the list. Several other methods rely on the list
        // being in a random order.
        Collections.shuffle(free, rand);
        
        int amountOfNewGrass = 1;
        for(int b = 0; b < amountOfNewGrass && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass newGrass = new Grass(field, loc);
            loc.addPlant(amountOfNewGrass, newGrass);
            //loc.incrementNumberOfPlants();
            newGrasses.add(newGrass);
        }
    }
}