package ch.hearc.security.password.dictionary;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author david.kuhner
 * 
 * Password generator
 */
public class Generator implements Runnable{
    
    /*------------------------------------------------------------------*\
    |*                          Attributes                              *|
    \*------------------------------------------------------------------*/
    
    private LinkedHashSet<StringBuilder> dataSource;
    private LinkedHashSet<StringBuilder> dataResult;
    private List<Character> dataSet;
    private CyclicBarrier barrier;
    
    /*------------------------------------------------------------------*\
    |*                          Constructor                             *|
    \*------------------------------------------------------------------*/
    
    /**
     * Construct a Generator
     * 
     * @param dataSource the prefix password
     * @param dataResult the generation result
     * @param dataSet the dataSet to add to the prefix
     * @param barrier the multi-thread syncronizer
     */
    public Generator(LinkedHashSet<StringBuilder> dataSource, 
            LinkedHashSet<StringBuilder> dataResult, 
            List<Character> dataSet,
            CyclicBarrier barrier){
        this.dataSource = dataSource;
        this.dataResult = dataResult;
        this.dataSet = dataSet;
        this.barrier = barrier;
        
    }
    
    /*------------------------------------------------------------------*\
    |*                          Public Methods                          *|
    \*------------------------------------------------------------------*/
    
    /**
     * Execute the generation algorithme and syncronized
     */
    @Override
    public void run() {
        StringBuilder value;
        
        for( StringBuilder sbSource : dataSource) {
            for(Character cData : dataSet) {
                value = new StringBuilder(sbSource);
                value.append(cData.toString());
                dataResult.add(value);
            }
        }

        try {
            this.barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
