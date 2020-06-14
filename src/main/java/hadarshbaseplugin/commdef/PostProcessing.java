package hadarshbaseplugin.commdef;

/**
 * Data retrieve post processing
 *
 */
public enum PostProcessing {
    NONE("none"),                   // no post processing
    LASTSAMPLE("lastSample"),       // select last sample
    FIRSTSAMPLE("firstSample"),     // select first sample
    MEAN("mean"),                   // use the average value of the samples
    MIN("min"),                     // use the min value of the samples
    MAX("max");                     // use the max value of the samples

    private final String postProcessor;

    PostProcessing(final String postProcessor) {
        this.postProcessor = postProcessor;
    }

    /**
     * Returns description instead of enum name
     *
     * @see Enum#toString()
     */
    @Override
    public String toString() {
        return this.postProcessor;
    }
    
    public static PostProcessing parse(String val) {
    	PostProcessing rc = PostProcessing.NONE;
    	if (PostProcessing.FIRSTSAMPLE.toString().equals(val)) {
    		rc = PostProcessing.FIRSTSAMPLE;
    	}
    	else if (PostProcessing.LASTSAMPLE.toString().equals(val)) {
    		rc = PostProcessing.LASTSAMPLE;
    	}
    	else if (PostProcessing.MEAN.toString().equals(val)) {
    		rc = PostProcessing.MEAN;
    	}    
    	else if (PostProcessing.MIN.toString().equals(val)) {
    		rc = PostProcessing.MIN;
    	}       
    	else if (PostProcessing.MAX.toString().equals(val)) {
    		rc = PostProcessing.MAX;
    	}  
    	else if (PostProcessing.NONE.toString().equals(val)) {
    		rc = PostProcessing.NONE;
    	}    	
    	return rc;
    }
}
