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
        int passwordLenght = 4;
        GeneratorController generatorController = new GeneratorController(dataSet, passwordLenght);
        LinkedHashSet<StringBuilder> passwords = generatorController.generate();
        
        System.out.println("Password has been generated :");
        System.out.println("Number of password : " + passwords.size());
        Iterator it = passwords.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
