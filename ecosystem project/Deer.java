import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Deer - deer can reproduce once every four turns and they must be in perfect health 
 * (note, to simplify coding, we've made deer able to asexually reproduce)
 * - deer are not affected by fire
 * - each deer has health (up to a maximum of eight points - the deer starts with eight) based on eating; 
 * each turn that the deer eats (which it does whenever it can), 
 * its health goes up by one (until the maximum); 
 * if the deer has nothing to eat, its health goes down to eat - during any given move, 
 * first the deer should eat, then the deer may move 
 * (the deer moves to whatever square adjoining it has the most food, 
 * unless the square it is on has the most food); 
 * if there is no food in its own square or an adjacent square, the deer will move randomly - 
 * deer die once their health reaches zero.
 * 
 * @author Jon Q - Deer behavior
 * @version 2024.00
 * Reference David J. Barnes and Michael Kölling, version 2016.02.29
 */
public class Deer extends Animal
{
    // Characteristics shared by all deer (class variables).
    
    // The rate of growth - 
    // deer reproduce when current step % GROWTH_RATE = 0 AND current step > 1
    private static final int GROWTH_RATE = 4;
    // The maximum number of possible deer births.
    private static final int MAX_LITTER_SIZE = 1;
    // The deer's initial health.
    private static final int MAX_HEALTH = 8;
    
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // The deer's health, which is increased by eating grass.
    private int health;

    /**
     * Create a deer.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Deer(Field field, Location location)
    {
        super(field, location);
        health = MAX_HEALTH;
    }
    
    /**
     * The deer eats to survive and breeds once every four turns
     * only if completely healthy.
     * @param field The field currently occupied.
     * @param newDeer A list to return newly born deer.
     */
    @Override
    
    public void act(List<Animal> newDeer, int step)
    {
        incrementHealth(); // Deer always tries to eat to increase health
        Location currentLocation = getLocation(); // Check if there is food in the current location
        Field field = getField();
        Object foodAtCurrentLocation = field.getObjectAt(currentLocation);

        //Review: You have to write every class and method that you use, but also, this isn't necessary. 
        //Please look at the base project I shared and the eating behavior in the predator class in the 
        //sample project from the professor.
        if (foodAtCurrentLocation instanceof Grass || foodAtCurrentLocation instanceof Tree) {
            health++; // Increase health by 1 when eating food at the current location
            field.clear(currentLocation); // Clear the food from the current location after eating
        } else {
            List<Location> adjacentLocations = getAdjacentLocations(); // Get adjacent locations for possible movement
            Location foodLocation = findFood(adjacentLocations);
            if (foodLocation != null) {
                //Review: moveTo(...) method is missing, please add or update name
                //moveTo(foodLocation);
            } else {
                //Review: randomMove(...) method is missing, please add or update name
                //randomMove();
                decrementHealth();
            }
        }
        
        //if (canBreed()) { Review: canBreed() method is missing, please add or update name
            giveBirth(newDeer);
        //}
    }
    
    private void incrementHealth() {
        if (health < MAX_HEALTH - 1)  {
            health++; // Increase health by 1 if it's below MAX_HEALTH
        }
    }
    
    /**
     * Make this deer lose health. This could result in the deer's death.
     */
    
     private void decrementHealth()
    {
        health--; // Decrease health by 1
        if (health <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for plants adjacent to the current location.
     * Only the first live plant is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    
     private Location findFood(List<Location> locations) 
     {
         Field field = getField();
         for (Location loc : getAdjacentLocations()) {
             Object food = field.getObjectAt(loc);
             if (food instanceof Grass || food instanceof Tree) {
                 health++; // Increase health by 1 when eating food
                 field.clear(loc); // Clear the food from the location after eating
                 return loc; // Return the location where food was found
             }
         }
         return null; // Return null if no food was found in adjacent locations
     }

     private List<Location> getAdjacentLocations() {
        Field field = getField();
        
        return field.adjacentLocations(getLocation());
     }
     
    /**
     * Check whether or not this Deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDeer A list to return newly born deer.
    */
    private void giveBirth(List<Animal> newDeer)
    {
        // New deer are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        
        for (int i = 0; i < births && i < MAX_LITTER_SIZE && !free.isEmpty(); i++) {
        Location loc = free.remove(0); // Remove and use the first free location
        Deer newBorn = new Deer(field, loc);
        newDeer.add(newBorn);
        field.place(newBorn, loc);
        }
    }    
    
    /**
     * Generate a number representing the number of births if it can breed.
     * @return The number of births
     */
    
     private int breed()
    {
        return rand.nextInt(MAX_LITTER_SIZE) + 1; // Randomly birth 1 to MAX_LITTER_SIZE deer
    }
}