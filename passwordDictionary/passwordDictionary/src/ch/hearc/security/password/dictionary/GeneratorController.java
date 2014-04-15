/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.security.password.dictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david.kuhner
 */
public class GeneratorController {
    
    private int nbCores;
    private LinkedHashSet<StringBuilder> dataOld;
    private LinkedHashSet<StringBuilder> dataNew;
    private ArrayList<Character> dataSet;
    private int passwordLenght;
    
    public GeneratorController(ArrayList<Character> dataSet, int passwordLenght) {
        if (dataSet.size() >= Runtime.getRuntime().availableProcessors()) {
            this.nbCores = Runtime.getRuntime().availableProcessors();
        } else {
            this.nbCores = 1;
        }
        this.passwordLenght = passwordLenght;
        this.dataNew = new LinkedHashSet<>();
        this.dataOld = new LinkedHashSet<>();
        this.dataSet = dataSet;
        initDataOld();
        System.out.println("Nb cores : " + nbCores);
    }
    
    public LinkedHashSet<StringBuilder> generate() {
        ExecutorService executorService = Executors.newFixedThreadPool(nbCores);
        CyclicBarrier barrier = new CyclicBarrier(nbCores+1);
        int nbDataPerThread = dataSet.size() / nbCores;
        
        
        for (int n = 1; n < passwordLenght; ++n) {
            LinkedHashSet<StringBuilder>[] solTab = new LinkedHashSet[nbCores];
            
            for (int i = 0; i < nbCores; ++i){
                solTab[i] = new LinkedHashSet<>();
                Runnable generator = new Generator(dataOld, solTab[i], dataSet.subList(i * nbDataPerThread, ((i + 1) * nbDataPerThread)), barrier);
                executorService.execute(generator);
            }
            try {
                barrier.await();
            } catch (    InterruptedException | BrokenBarrierException ex) {
                Logger.getLogger(GeneratorController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            dataOld.clear();
            for (LinkedHashSet<StringBuilder> elem : solTab) {
                dataOld.addAll(elem);
            }
            
        }
        executorService.shutdown();
        return dataOld;
    }
    
    private void initDataOld() {
        for (Character c : dataSet) {
            dataOld.add(new StringBuilder(c.toString()));
        }
    }
}
