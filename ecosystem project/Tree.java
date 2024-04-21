import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Trees - each tree can produce a new tree every five turns
 * - it reproduces in its own square or in an adjacent square
 * - it never ages - it only dies through a fire
 * - you can have a maximum of ten units of vegetable material 
 * (that is, grass and trees combined) on any given square
 * - if a tree wants to spread in a particular direction, 
 * it can eliminate the grass that is in that square if necessary 
 * to avoid violating the ten vegetable unit limitation
 * - in the event of a fire, any unit of grass has a 60% chance of dying.
 * 
 * @author Kameliia P - Tree behavior
 * @version 2024.00
 * Reference David J. Barnes and Michael Kölling, version 2016.02.29
 */
public class Tree extends Plant
{
    // Characteristics shared by all grass (class variables).
    
    // The rate of growth 
    // - trees grow when current step % GROWTH_RATE = 0 AND current step > 1
    private static final int GROWTH_RATE = 5;
    
    // A shared random number generator to control growth.
    private static final Random rand = Randomizer.getRandom();
    
    // in the event of a fire, any unit of Tree has a 60% chance of dying
    private static final double PROBABILITY_OF_DEATH = 0.6;
    
    // Individual characteristics (instance fields).
    private boolean isOnFire;
    
    List<Location> neighbors = this.getField().getFreeAdjacentLocations(this.getLocation());
    
    /**
     * Create a Tree.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Tree(Field field, Location location)
    {
        super(field, location);
        isOnFire = getLocation().isOnFire();
    }
    
    /**
     * Trees grow, producing a new tree every five turns.
     * @param field The field currently occupied.
     * @param newTrees A list to return newly born tree(s).
     */
    public void exist(List<Plant> newTrees, int currentStep)
    {
        if(this.isAlive()) {
	    if(currentStep > 1 && currentStep % GROWTH_RATE == 0) {
		this.grow(newTrees);
            }
        }
    }
    
    /**
     * Check whether or not this tree will reproduce at this step.
     * New tree will grow into the same location, if there is room, or into a random adjacent location.
     * @param newTrees A list to return new tree growth.
     */
    private void grow(List<Plant> newTrees)
    {
	int counter = this.getLocation().getNumberOfPlants();  //accessor method needs to be in Location
	Location loc = null;
	if(counter < 10) {
	    loc = this.getLocation();
	}
	else {
	    
	    //Trees have fields which have this method - see adjacent locations
	    //List<Location> neighbors = this.getLocation().getNeighbors(); //accessor method needs to be in Location
	    boolean foundLocation = false;
	    int attempts = neighbors.size();
	    while(!foundLocation && attempts > 0) 
	    {
		int randomIndex = rand.nextInt(neighbors.size());
	    	loc = neighbors.get(randomIndex);
	    	counter = loc.getNumberOfPlants();
	    	if(counter < 10) {
		    foundLocation = true;
		}
		else {  // location is full; find object of Grass at this location and kill it
        	    //Review: Update this to work with location	    
		    /*
        	    List<Plant> plants = loc.getPlants(); 	//accessor method needs to be in Location
	    	    Iterator<Plant> it = plants.iterator();
			boolean foundGrass = false;
			while(!foundGrass && it.hasNext()) 
			{
				Plant plant = it.next();
				if(plant instanceof Grass) {
        			    foundGrass = true;
        			    plant.setDead();
        			    loc.decrementNumberOfPlants();  //mutator method needs to be in Location
        			    foundLocation = true;
				}//endif
			}//endwhile
			*/
		}//endif    
		attempts--;
	    }//endwhile
	    if(!foundLocation) 
	    {  //unlikely event that ALL neighboring locations have 10 trees each
        	        //Review: Check what this will destroy
			loc = null;	  //no room to grow
	    }	
	}
	if(loc != null) {
    		Tree offspring = new Tree(this.getField(), loc);
    		loc.incrementNumberOfPlants();
    		newTrees.add(offspring);
		
    		//Review: this has to be made to work with the list of plants in location
    		//loc.updatePlants(offspring);
	}  
    }

    /*
     * Check whether or not this tree object dies in a fire - 
     * @return The plant
     */
    //Review - class moved to Plant because Grass is also affected by fire
    /*
    protected Plant fireBehavior()
    {
        //Review: update call to location to use accessor method inherited from superClass
        //original: if(this.location.isOnFire()) {
        if(this.getLocation().isOnFire()) {
            
	    if (this.isAlive() && rand.nextDouble() <= PROBABILITY_OF_DEATH) {
		this.setDead();
	    }
	}
        return this;
    }
    */
}
