import java.awt.Color;
import java.util.HashMap;

/**
 * This class collects and provides some statistical data on the state 
 * of a field. It is flexible: it will create and maintain a counter 
 * for any class of object that is found within the field.
 * 
 * @author Spring Java 2024 Class Project
 * @version 2024.00
 * Reference David J. Barnes and Michael Kölling, version 2016.02.29
 */
public class FieldStats
{
    // Counters for each type of entity (deer, tree, grass) in the simulation.
    private HashMap<Class, Counter> counters;
    // Whether the counters are currently up to date.
    private boolean countsValid;

    /**
     * Construct a FieldStats object.
     */
    public FieldStats()
    {
        // Set up a collection for counters for each type of object that
        // we might find
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
     * Get details of what is in the field.
     * @return A string describing what is in the field.
     */
    public String getPopulationDetails(Field field)
    {
        StringBuffer buffer = new StringBuffer();
        if(!countsValid) {
            generateCounts(field);
        }
        for(Class key : counters.keySet()) {
            Counter info = counters.get(key);
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }
    
    /**
     * Invalidate the current set of statistics; reset all 
     * counts to zero.
     */
    public void reset()
    {
        countsValid = false;
        for(Class key : counters.keySet()) {
            Counter count = counters.get(key);
            count.reset();
        }
    }

    /**
     * Increment the count for one class of object.
     * @param objectClass The class of object to increment.
     */
    public void incrementCount(Class objectClass)
    {
        Counter count = counters.get(objectClass);
        if(count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            count = new Counter(objectClass.getName());
            counters.put(objectClass, count);
        }
        count.increment();
    }
    
    /**
     * Increment the count for one class of object.
     * @param objectClass The class of object to increment.
     */
    public void incrementCount(Object object)
    {
        Class objectClass = object.getClass();
        Counter count = counters.get(object.getClass());
        if(count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            count = new Counter(objectClass.getName());
            counters.put(objectClass, count);
        }
        
        if(object != null) {
            String type = object.getClass().getName();
            if (type.equals("Grass")) {
                Grass grass = (Grass) object;
                updateGrassCount(object.getClass(), grass.getLocation().getNumberOfPlants());
            } else {
                incrementCount(object.getClass());
            }
        }
    }

    /**
     * Increment the count for all of the Grass at a Location.
     * @param objectClass The class of object to increment.
     */
    public void updateGrassCount(Class objectClass, int num)
    {
        Counter count = counters.get(objectClass);
        if(count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            count = new Counter(objectClass.getName());
            counters.put(objectClass, count);
        }
        
        //Review: I wrote this, it's sloppy, but we're running out of time
        while(num > 0) {
            count.increment();
            num--;
        }
    }
    
    /**
     * Indicate that an object count has been completed.
     */
    public void countFinished()
    {
        countsValid = true;
    }

    /**
     * Determine whether the simulation is still viable.
     * I.e., should it continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        // How many counts are non-zero.
        int nonZero = 0;
        if(!countsValid) {
            generateCounts(field);
        }
        for(Class key : counters.keySet()) {
            Counter info = counters.get(key);
            if(info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }
    
    /**
     * Generate counts of objects.
     * These are not kept up to date as plants and animals
     * are placed in the field, but only when a request
     * is made for the information.
     * @param field The field to generate the stats for.
     */
    private void generateCounts(Field field)
    {
        reset();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object object = field.getObjectAt(row, col);
                if(object != null) {
                    String type = object.getClass().getName();
                    if (type.equals("Grass")) {
                        Grass grass = (Grass) object;
                        updateGrassCount(object.getClass(), grass.getLocation().getNumberOfPlants());
                    } else {
                        incrementCount(object.getClass());
                    }
                }
            }
        }
        countsValid = true;
    }
}
