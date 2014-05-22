package ch.hearc.security.password.dictionary;

/**
 * @author david.kuhner
 * 
 * Range tool class
 */
class Range {
    
    /*------------------------------------------------------------------*\
    |*                          Attributes                              *|
    \*------------------------------------------------------------------*/
    
    private Integer start;
    private int stop;
    private int current;
    private int step;
    
    /*------------------------------------------------------------------*\
    |*                          Constructor                             *|
    \*------------------------------------------------------------------*/
    
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
    
    /*------------------------------------------------------------------*\
    |*                          Public Methods                          *|
    \*------------------------------------------------------------------*/
    
    /**
     * Gets next index
     * @return 
     */
    public int next()
    {
        current += step;
        return current;
    }
    
    /**
     * Returns true if current is smaller than stop, else false.
     * @return 
     */
    public boolean hasNext()
    {
        return current < stop;
    }
}
