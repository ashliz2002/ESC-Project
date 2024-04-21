import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Represent a location in a rectangular grid.
 * 
 * @author Brice S - fire behavior
 * @version 2024.00
 * Reference David J. Barnes and Michael Kölling, version 2016.02.29
 */
public class Location
{
    // Row and column positions.
    private int row;
    private int col;
    private int numberOfPlants;
    private Plant[] plants;
    
    // A shared random number generator to check fire status.
    private static final Random rand = Randomizer.getRandom();
    
    //probability of fire 
    //TODO verify how we check for fire && probability of fire.
    private static final double FIRE_PROBABILITY = 0.05;
    
    // Whether the location is on fire
    private boolean isOnFire;
    
    // Neighboring locations
    private List<Location> neighbors;
    
    /**
     * Represent a row and column.
     * @param row The row.
     * @param col The column.
     */
    public Location(int row, int col)
    {
        this.row = row;
        this.col = col;
        this.isOnFire = false;
        this.neighbors = new ArrayList<>();
        this.plants = new Plant[10];
    }
    
    /**
     * Implement content equality.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        }
        else {
            return false;
        }
    }
    
    /**
     * Return a string of the form row,column
     * @return A string representation of the location.
     */
    public String toString()
    {
        return row + "," + col;
    }
    
    /**
     * Use the top 16 bits for the row value and the bottom for
     * the column. Except for very big grids, this should give a
     * unique hash code for each (row, col) pair.
     * @return A hashcode for the location.
     */
    public int hashCode()
    {
        return (row << 16) + col;
    }
    
    /**
     * @return The row.
     */
    public int getRow()
    {
        return row;
    }
    
    /**
     * @return The column.
     */
    public int getCol()
    {
        return col;
    }
    
    /**
     * @return The current array of plants in that location.
     */
    public Plant[] getPlants()
    {
        return this.plants;
    }
    
    /**
     * @return The current plant in the current array of plants in that location.
     */
    public Plant getCurrentPlant()
    {
        return this.plants[numberOfPlants];
    }
    
    /**
     * @return Update the array of plants in this location.
     */
    public void removeCurrentPlant(Integer numberOfPlants, Plant newPlant)
    {        
        this.plants[numberOfPlants] = null;
        this.decrementNumberOfPlants();
    }
    
    /**
     * @return Update the array of plants in this location.
     */
    public void addPlant(Integer numberOfPlantsAsIndex, Plant newPlant)
    {        
        if(numberOfPlants < 10) {
            this.plants[numberOfPlantsAsIndex] = newPlant;
            this.incrementNumberOfPlants();
        }
    }
    
    /**
     * @return The count of plants in that location.
     */
    public int getNumberOfPlants()
    {
        return numberOfPlants;
    }
    
    /**
     * @return The updated count of plants in that location.
     */
    public int incrementNumberOfPlants()
    {
        return ++numberOfPlants;
    }
    
    /**
     * @return The updated count of plants in that location.
     */
    public int decrementNumberOfPlants()
    {
        return --numberOfPlants;
    }
    
    /**
     * Set the location on fire.
     */
    public void setOnFire() {
        this.isOnFire = true;
    }
    
    /**
     * @return Determine if a location is on fire
     */
    public boolean isOnFire() {
        return isOnFire;
    }
    
    /**
     * Add neighboring location.
     * @param neighbor The neighboring location to add.
     */
    public void addNeighbor(Location neighbor) {
        this.neighbors.add(neighbor);
    }
    
    /**
     * Spread fire to neighboring locations.
     */
    private void spreadFire() {
        for (Location neighbor : neighbors) {
            if (!neighbor.isOnFire() && rand.nextDouble() < FIRE_PROBABILITY) {
                neighbor.setOnFire();
            }
        }
    }
}
