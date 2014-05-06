/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.security.password.dictionary;

/**
 *
 * @author david.kuhner
 */
class Range {
    
    private Integer start;
    private int stop;
    private int current;
    private int step;
    
    /**
     * Range form is [start;stop]
     * @param start
     * @param stop
     * @param step 
     */
    public Range(int start, int stop, int step)
    {
        if ( start > stop )
            throw new Error("[Range] : start cannot be smaller than stop");
        
        this.start = start;
        this.stop = stop;
        this.current = start - step;
        this.step = step;
    }
    
    public int next()
    {
        current += step;
        return current;
    }
    
    public boolean hasNext()
    {
        return current < stop;
    }
    
}
