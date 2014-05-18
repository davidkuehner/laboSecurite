/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.security.password.dictionary;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 *
 * @author david.kuhner
 */
public class PasswordDictionary {

    private static int pwdLenMin;
    private static int pwdLenMax;
    private static String uppercase;
    private static String lowercase;
    private static String numbers;
    private static String specialChars;
    private static boolean verboseMode;
    private static boolean passwordPrint;

    public static void main(String[] args) {
        getProperty();
        generatePasswords();
    }

    private static void getProperty() {
        try {
            Integer pwdLen = Integer.parseInt(System.getProperty("lenght", "1"));
            pwdLenMin = Integer.parseInt(System.getProperty("lenghtMin", pwdLen.toString()));
            pwdLenMax = Integer.parseInt(System.getProperty("lenghtMax", pwdLen.toString()));
            uppercase = System.getProperty("uppercase", null);
            lowercase = System.getProperty("lowercase", null);
            numbers = System.getProperty("numbers", null);
            specialChars = System.getProperty("specialChars", null);
            verboseMode = Boolean.parseBoolean(System.getProperty("verbose", "False"));
            passwordPrint = Boolean.parseBoolean(System.getProperty("passwordPrint", "True"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void generatePasswords() {
        try {

            DataSetParsor dataSetParsor = new DataSetParsor(uppercase, lowercase, numbers, specialChars);
            HashSet<Character> dataSet = (HashSet<Character>) dataSetParsor.getDataSet();
            GeneratorController generatorController = new GeneratorController(dataSet, pwdLenMin, pwdLenMax);

            long start = System.currentTimeMillis();
            LinkedHashSet<StringBuilder> passwords = generatorController.generate();
            long duration = System.currentTimeMillis() - start;

            if (passwordPrint) {
                for (StringBuilder pwd : passwords) {
                    System.out.println(pwd.toString());
                }
            }

            if (verboseMode) {
                informations(dataSet, passwords, duration);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void informations(HashSet<Character> dataSet, LinkedHashSet<StringBuilder> passwords, long duration) {
        int nbCores = Runtime.getRuntime().availableProcessors();
        int nbThreads = 0;
        if (dataSet.size() >= Runtime.getRuntime().availableProcessors()) {
            nbThreads = Runtime.getRuntime().availableProcessors();
        } else {
            nbThreads = 1;
        }

        System.out.println("=======================");
        System.out.println("Informations");
        System.out.println("=======================");

        System.out.println("Number of cores : " + nbCores);
        System.out.println("Number of treads used : " + nbThreads);

        System.out.print("Alphabet : \n|");
        for (Character ch : dataSet) {
            System.out.print(ch + "|");
        }
        System.out.print("\n");

        System.out.println("Password lenght min : " + pwdLenMin);
        System.out.println("Password lenght max : " + pwdLenMax);
        System.out.println("Number of password : " + passwords.size());
        System.out.println("Generation duration : " + duration + "ms");
    }
}
