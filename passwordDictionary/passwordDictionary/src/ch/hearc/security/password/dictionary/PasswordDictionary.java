/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.security.password.dictionary;

import java.util.HashSet;
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
        HashSet<Character> dataSet = null;
        try {
            dataSetParsor = new DataSetParsor("[A-Z]", "[a-z]", "[0-9]", ".?!%&#");
            dataSet = (HashSet<Character>) dataSetParsor.getDataSet();
            for (Character ch : dataSet) {
                System.out.print(ch + "|");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        int pwdLenMin = 2;
        int pwdLenMax = 2;
        try {
            GeneratorController generatorController = new GeneratorController(dataSet, pwdLenMin, pwdLenMax);
            LinkedHashSet<StringBuilder> passwords = generatorController.generate();

            System.out.println("Password has been generated :");


            for (StringBuilder pwd : passwords) {
                System.out.println(pwd.toString());
            }

            System.out.println("Number of password : " + passwords.size());
        } catch (Exception ex) {
            Logger.getLogger(PasswordDictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
