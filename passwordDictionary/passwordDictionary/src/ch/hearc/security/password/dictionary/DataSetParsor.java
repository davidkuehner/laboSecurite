package ch.hearc.security.password.dictionary;

import java.util.HashSet;
import java.util.Set;

/**
 * @author david.kuhner
 * 
 * DataSetParsor converts input properties like [a-z] to a set of charaters
 */
public class DataSetParsor {

    /*------------------------------------------------------------------*\
    |*                          Attributes                              *|
    \*------------------------------------------------------------------*/
    
    private Range uppercaseRange;
    private Range lowercaseRange;
    private Range numberRange;
    private String specialCharacters;
    private Set<Character> dataSet;
    private static final Character RANGE_OPPENER = '[';
    private static final Character RANGE_CLOSER = ']';
    private static final Character RANGE_SEPARATOR = '-';
    private static final int RANGE_START_POS = 1;
    private static final int RANGE_STOP_POS = 3;
    private static final int RANGE_STEP = 1;
    private static final int UPPERCASE_OFFSET = 55;
    private static final int LOWERCASE_OFFSET = 87;
    private static final int NUMERICAL_OFFSET = 48;
    private static final int RANGE_ALPHABET_SIZE = 26;
    private static final int RANGE_NUMERICAL_SIZE = 10;
    private static final int ALPHABET_A_SHIFT = 10;

    /*------------------------------------------------------------------*\
    |*                          Constructor                             *|
    \*------------------------------------------------------------------*/
    
    /**
     * Construc a DataSetParsor
     * 
     * @param uppercaseRange the uppercase range to convert to a set
     * @param lowercaseRange the lowercase range to convert to a set
     * @param numberRangethe the number range to convert to a set
     * @param specialCharacters the special characters to add to a set
     */
    public DataSetParsor(String uppercaseRange, String lowercaseRange, String numberRange, String specialCharacters) throws Exception {

        checkRanges(uppercaseRange, lowercaseRange, numberRange, specialCharacters);

        if (uppercaseRange != null) {
            this.uppercaseRange = new Range(Character.getNumericValue(uppercaseRange.charAt(RANGE_START_POS)) + UPPERCASE_OFFSET, Character.getNumericValue(uppercaseRange.charAt(RANGE_STOP_POS)) + UPPERCASE_OFFSET, RANGE_STEP);
        } else {
            this.uppercaseRange = null;
        }
        if (lowercaseRange != null) {
            this.lowercaseRange = new Range(Character.getNumericValue(lowercaseRange.charAt(RANGE_START_POS)) + LOWERCASE_OFFSET, Character.getNumericValue(lowercaseRange.charAt(RANGE_STOP_POS)) + LOWERCASE_OFFSET, RANGE_STEP);
        } else {
            this.lowercaseRange = null;
        }
        if (numberRange != null) {
            this.numberRange = new Range(Character.getNumericValue(numberRange.charAt(RANGE_START_POS)) + NUMERICAL_OFFSET, Character.getNumericValue(numberRange.charAt(RANGE_STOP_POS)) + NUMERICAL_OFFSET, RANGE_STEP);
        } else {
            this.numberRange = null;
        }
        if ( specialCharacters != null) {
            this.specialCharacters = specialCharacters;
        } else {
            this.specialCharacters = null;
        }

        this.dataSet = new HashSet<>();
        constructDataSet();
    }

    /*------------------------------------------------------------------*\
    |*                          Public Methods                          *|
    \*------------------------------------------------------------------*/
    
    /**
     * Returns a dataSet containing the passwords alphabet
     * @return 
     */
    public Set<Character> getDataSet() {
        return dataSet;
    }

    /*------------------------------------------------------------------*\
    |*                          Private Methods                         *|
    \*------------------------------------------------------------------*/
    
    private void constructDataSet() {
        if (uppercaseRange != null) {
            while (uppercaseRange.hasNext()) {
                dataSet.add(Character.toChars(uppercaseRange.next())[0]);
            }
        }
        if (lowercaseRange != null) {
            while (lowercaseRange.hasNext()) {
                dataSet.add(Character.toChars(lowercaseRange.next())[0]);
            }
        }
        if (numberRange != null) {
            while (numberRange.hasNext()) {
                dataSet.add(Character.toChars(numberRange.next())[0]);
            }
        }
        if (specialCharacters != null) {
            for ( char c : specialCharacters.toCharArray())
            {
                dataSet.add(c);
            }
        }
    }

    private boolean isRangeFormatValid(String range) {
        boolean flag = true;
        flag &= range.charAt(0) == RANGE_OPPENER;
        flag &= range.charAt(2) == RANGE_SEPARATOR;
        flag &= range.charAt(4) == RANGE_CLOSER;
        return flag;
    }

    private boolean isUppercaseRangeValid(String uppercaseRange) {
        boolean flag = true;
        flag &= Character.getNumericValue(uppercaseRange.charAt(RANGE_START_POS)) + UPPERCASE_OFFSET >= UPPERCASE_OFFSET;
        flag &= Character.getNumericValue(uppercaseRange.charAt(RANGE_STOP_POS)) + UPPERCASE_OFFSET <= ALPHABET_A_SHIFT + UPPERCASE_OFFSET + RANGE_ALPHABET_SIZE;
        flag &= uppercaseRange.charAt(RANGE_START_POS) <= uppercaseRange.charAt(RANGE_STOP_POS);
        return flag;
    }

    private boolean isLowercaseRangeValid(String lowercaseRange) {
        boolean flag = true;
        flag &= (Character.getNumericValue(lowercaseRange.charAt(RANGE_START_POS)) + LOWERCASE_OFFSET) >= LOWERCASE_OFFSET;
        flag &= Character.getNumericValue(lowercaseRange.charAt(RANGE_STOP_POS)) + LOWERCASE_OFFSET <= ALPHABET_A_SHIFT + LOWERCASE_OFFSET + RANGE_ALPHABET_SIZE;
        flag &= lowercaseRange.charAt(RANGE_START_POS) <= lowercaseRange.charAt(RANGE_STOP_POS);
        return flag;
    }

    private boolean isNumberRangeValid(String numberRange) {
        boolean flag = true;
        flag &= Character.getNumericValue(numberRange.charAt(RANGE_START_POS)) + NUMERICAL_OFFSET >= NUMERICAL_OFFSET;
        flag &= Character.getNumericValue(numberRange.charAt(RANGE_STOP_POS)) + NUMERICAL_OFFSET <= NUMERICAL_OFFSET + RANGE_NUMERICAL_SIZE;
        flag &= numberRange.charAt(RANGE_START_POS) <= numberRange.charAt(RANGE_STOP_POS);
        return flag;
    }

    private boolean isRangeLenghtValid(String range) {
        return range.length() == 5;
    }

    private void checkRanges(String uppercaseRange, String lowercaseRange, String numberRange, String specialCharacters) throws Exception {
        
        if (uppercaseRange == null && lowercaseRange == null && numberRange == null && specialCharacters == null)
            throw new Exception("[DataSetParsor] : At minimum one range must be defined");

        if (uppercaseRange != null) {
            if (!isRangeLenghtValid(uppercaseRange)) {
                throw new Exception("[DataSetParsor] : Invalid uppercase range lenght, should be like [x-y]");
            }

            if (!isRangeFormatValid(uppercaseRange)) {
                throw new Exception("[DataSetParsor] : Invalid uppercase range format, should be like [start-stop]");
            }

            if (!isUppercaseRangeValid(uppercaseRange)) {
                throw new Exception("[DataSetParsor] : Invalid uppercase range, should be like [A-Z]");
            }
        }

        if (lowercaseRange != null) {
            if (!isRangeLenghtValid(lowercaseRange)) {
                throw new Exception("[DataSetParsor] : Invalid lowercase range lenght, should be like [x-y]");
            }

            if (!isRangeFormatValid(lowercaseRange)) {
                throw new Exception("[DataSetParsor] : Invalid lowercase range format, should be like [start-stop]");
            }

            if (!isLowercaseRangeValid(lowercaseRange)) {
                throw new Exception("[DataSetParsor] : Invalid lowercase range, should be like [a-z]");
            }
        }

        if (numberRange != null) {
            if (!isRangeLenghtValid(numberRange)) {
                throw new Exception("[DataSetParsor] : Invalid numerical range lenght, should be like [x-y]");
            }

            if (!isRangeFormatValid(numberRange)) {
                throw new Exception("[DataSetParsor] : Invalid numerical range format, should be like [start-stop]");
            }

            if (!isNumberRangeValid(numberRange)) {
                throw new Exception("[DataSetParsor] : Invalid numerical range, should be like [0-9]");
            }
        }
    }
}
