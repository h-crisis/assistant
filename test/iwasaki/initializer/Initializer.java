/**
 * 
 */
package iwasaki.initializer;

import ga.bitstring.TBitStringIndividual;
import ga.bitstring.TUxMgg;
import ga.realcode.TRealNumberIndividual;
import ga.realcode.TUndxMgg;

import java.util.List;

/**
 * @author kurata
 *
 */
public class Initializer {

	/**
	 * 
	 */
	private static void execute1() {
		TUxMgg ga = new TUxMgg( false, 50, 50, 100, true);
		List<TBitStringIndividual> initialPopulation = ga.getInitialPopulation();
		for ( int i = 0; i < initialPopulation.size(); ++i) {
			String line = ( "false\tname" + String.valueOf( i + 1) + "\t");
			for ( int j = 0; j < initialPopulation.get( i).getBitString().getLength(); ++j)
				line += ( "\t" + initialPopulation.get( i).getBitString().getData( j));

			System.out.println( line);
		}
	}

	/**
	 * 
	 */
	private static void execute2() {
		TUndxMgg ga = new TUndxMgg( true, 20, 50, 100);
		List<TRealNumberIndividual> initialPopulation = ga.getInitialPopulation();
		for ( TRealNumberIndividual realNumberIndividual:initialPopulation)
			realNumberIndividual.printOn();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		execute1();
//		execute2();
	}
}
