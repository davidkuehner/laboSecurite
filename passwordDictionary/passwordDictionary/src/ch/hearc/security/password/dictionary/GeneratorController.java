/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.security.password.dictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
    private LinkedHashSet<StringBuilder> passwords;
    private ArrayList<Character> alphabet;
    private int pwdLenMin;
    private int pwdLenMax;
    private int nbDataPerThread;

    public GeneratorController(HashSet<Character> dataSet, int pwdLenMin, int pwdLenMax) throws Exception {
        if (dataSet != null) {
            if (dataSet.size() >= Runtime.getRuntime().availableProcessors()) {
                this.nbCores = Runtime.getRuntime().availableProcessors();
            } else {
                this.nbCores = 1;
            }
            this.pwdLenMin = pwdLenMin;
            this.pwdLenMax = pwdLenMax;
            this.passwords = new LinkedHashSet<>();
            this.dataOld = new LinkedHashSet<>();
            this.alphabet = new ArrayList<>(dataSet);
            initDataOld();
        } else {
            throw new Exception("[GeneratorController] : DataSet cannot be null"); 
        }
    }

    public LinkedHashSet<StringBuilder> generate() {
        ExecutorService executorService = Executors.newFixedThreadPool(nbCores);
        CyclicBarrier barrier = new CyclicBarrier(nbCores + 1);
        nbDataPerThread = (int) Math.floor(((double) alphabet.size()) / ((double) nbCores));
        // System.out.println("Data per thread : " + nbDataPerThread);

        int[] sliceIndex = null;

        if (pwdLenMin == 0) {
            passwords.add(new StringBuilder(""));
        }
        if (pwdLenMin <= 1) {
            passwords.addAll(dataOld);
        }

        for (int n = 1; n < pwdLenMax; ++n) {
            LinkedHashSet<StringBuilder>[] solTab = new LinkedHashSet[nbCores];

            for (int i = 0; i < nbCores; ++i) {
                solTab[i] = new LinkedHashSet<>();
                sliceIndex = getSliceIndex(i);
                //System.out.println("["+sliceIndex[0]+","+sliceIndex[1]+"]");
                Runnable generator = new Generator(dataOld, solTab[i], alphabet.subList(sliceIndex[0], sliceIndex[1]), barrier);
                executorService.execute(generator);
            }
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                Logger.getLogger(GeneratorController.class.getName()).log(Level.SEVERE, null, ex);
            }


            dataOld.clear();
            for (LinkedHashSet<StringBuilder> elem : solTab) {
                dataOld.addAll(elem);
            }
            if (n + 1 >= pwdLenMin) {
                passwords.addAll(dataOld);
            }

        }
        executorService.shutdown();
        return passwords;
    }

    private void initDataOld() {
        for (Character c : alphabet) {
            dataOld.add(new StringBuilder(c.toString()));
        }
    }

    private int[] getSliceIndex(int i) {
        int ends = (i + 1) * nbDataPerThread;
        if (i == nbCores - 1) {
            ends = (alphabet.size());
        }
        return new int[]{i * nbDataPerThread, ends};
    }
}
