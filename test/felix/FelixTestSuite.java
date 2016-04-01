package felix;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Classe de suite de tests unitaire pour Felix.
 *
 * @version 2.0.0.etu
 * @author Matthias Brun
 *
 */
public class FelixTestSuite
{
	/**
	 * Suite de tests pour Felix.
	 *
	 * @return la suite de test.
	 *
	 * @see junit.framework.TestSuite
	 *
	 */	
	public static Test suite() 
	{
		final TestSuite suite = new TestSuite("Suite de test pour Felix");
		suite.addTest(new TestSuite(ConnexionTest.class));

		return suite;
	}

}

