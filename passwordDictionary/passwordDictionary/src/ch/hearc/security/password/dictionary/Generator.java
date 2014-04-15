/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.security.password.dictionary;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david.kuhner
 */
public class Generator implements Runnable{
    
    private LinkedHashSet<StringBuilder> dataSource;
    private LinkedHashSet<StringBuilder> dataResult;
    private List<Character> dataSet;
    private CyclicBarrier barrier;
    
   
    
    public Generator(LinkedHashSet<StringBuilder> dataSource, 
            LinkedHashSet<StringBuilder> dataResult, 
            List<Character> dataSet,
            CyclicBarrier barrier){
        this.dataSource = dataSource;
        this.dataResult = dataResult;
        this.dataSet = dataSet;
        this.barrier = barrier;
        
    }
    
    @Override
    public void run() {
        Iterator itSource = dataSource.iterator();
        Iterator itData = dataSet.iterator();
        StringBuilder value;
        while(itSource.hasNext()) {
            StringBuilder begin = (StringBuilder)itSource.next();
            while(itData.hasNext()) {
                value = (begin.append(itData.next()));
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
