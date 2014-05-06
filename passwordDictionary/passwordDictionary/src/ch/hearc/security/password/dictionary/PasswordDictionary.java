/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.security.password.dictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david.kuhner
 */
public class PasswordDictionary {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        // Range is form [A-Z],[a-z],[0-9]
        DataSetParsor dataSetParsor;
        try {
            dataSetParsor = new DataSetParsor("[A-Z]", "[a-z]", "[0-9]");
            ArrayList<Character> dataSetTest = dataSetParsor.getDataSet();
            for(Character ch : dataSetTest)
            {
                System.out.print(ch+"|");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        
        
        /*
        ArrayList<Character> dataSet = new ArrayList<>();
        dataSet.add('A');
        dataSet.add('B');
        dataSet.add('C');
        dataSet.add('D');
        dataSet.add('E');
                
        int pwdLenMin = 1;
        int pwdLenMax = 2;
        GeneratorController generatorController = new GeneratorController(dataSet, pwdLenMin, pwdLenMax);
        LinkedHashSet<StringBuilder> passwords = generatorController.generate();
        
        System.out.println("Password has been generated :");
        
        
        for(StringBuilder pwd : passwords)
            System.out.println(pwd.toString());
        
        System.out.println("Number of password : " + passwords.size());
        */
        
    }
}
