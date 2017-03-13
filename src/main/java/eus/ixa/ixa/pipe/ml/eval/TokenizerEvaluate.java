package eus.ixa.ixa.pipe.ml.eval;

import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.*;
import java.io.IOException;
import opennlp.tools.util.eval.Mean;

//******************
// CHECK DOC
//******************

public class TokenizerEvaluate {

	private final Mean wordAccuracy = new Mean();
	private final Mean sentenceAccuracy = new Mean();
	private ArrayList < ArrayList < String > > test = new ArrayList < ArrayList < String > > ();
	private ArrayList < ArrayList < String > > pred = new ArrayList < ArrayList < String > > ();
	
	/**
	   * Construct an evaluator. It takes from the properties a prediction file and a testset.
	   * 
	   * @param props
	   *          the properties parameter
	   * @throws IOException
	   *           the io exception
	   */
  public TokenizerEvaluate(Properties props) throws IOException {
    final String lang = props.getProperty ( "language" );
    final String testSet = props.getProperty ( "testset" );
    final String prediction = props.getProperty ( "prediction" );

    try {
    	
    	File fileTest = new File ( testSet );
    	File filePred = new File ( prediction );
        Scanner scanTest = new Scanner ( fileTest );
        Scanner scanPred = new Scanner ( filePred );
        
        // This only works if test and prediction are equal length
        // Otherwise we need another loop
        while ( scanTest.hasNext() ) {
        	ArrayList < String > aux1 = new ArrayList < String > ( Arrays.asList ( scanTest.nextLine().split ( "\\s+" ) ) );
        	//ArrayList < String > aux2 = new ArrayList < String > ( Arrays.asList ( scanPred.nextLine().split ( "\\s+" ) ) );
        	test.add ( aux1 );
        	//pred.add ( aux2 );
        }
	while ( scanPred.hasNext() ) {
		ArrayList < String > aux2 = new ArrayList < String > ( Arrays.asList ( scanPred.nextLine().split ( "\\s+" ) ) );
		pred.add ( aux2 );
	}

	System.out.println (test.size());
	System.out.println (pred.size());
        
        scanTest.close();
        scanPred.close();
        
    } catch ( FileNotFoundException e ) {
    	System.out.println ( e );
    }    
  }
  
  /**
    * TEMPORAL
    * Maybe it should be generic?
    * However, easy changes. 
    */
  public void evaluateAccuracy() throws IOException {
	  int fails;
	  for ( int i = 0; i < test.size(); i++ ) {
		  fails = 0;
		  for ( int j = 0; j < test.get ( i ).size (); j++ ) {
			  if ( test.get ( i ).get ( j ).equals ( pred.get ( i ).get ( j ) ) )
				  this.wordAccuracy.add ( 1.0 );
			  else {
				  this.wordAccuracy.add ( 0.0 );
				  fails++;
			  }
		  }
		  if ( fails > 0 )
			  this.sentenceAccuracy.add ( 0.0 );
		  else
			  this.sentenceAccuracy.add ( 1.0 );
	  }
	  
	  // Show accuracy
	  System.out.println ();
	  System.out.println ( "Word accuracy: " + this.wordAccuracy.mean() );
	  System.out.println ( "Sentence accuracy: " + this.sentenceAccuracy.mean() );
  }

}
