/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.security.password.dictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 *
 * @author david.kuhner
 */
public class PasswordDictionary {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello password dictionary ");
        
        ArrayList<Character> dataSet = new ArrayList<>();
        dataSet.add('A');
        dataSet.add('B');
        dataSet.add('C');
        dataSet.add('D');
        dataSet.add('E');
        int pwdLenMin = 2;
        int pwdLenMax = 4;
        GeneratorController generatorController = new GeneratorController(dataSet, pwdLenMin, pwdLenMax);
        LinkedHashSet<StringBuilder> passwords = generatorController.generate();
        
        System.out.println("Password has been generated :");
        
        
        for(StringBuilder pwd : passwords)
            System.out.println(pwd.toString());
        
        System.out.println("Number of password : " + passwords.size());
        
        
    }
}
