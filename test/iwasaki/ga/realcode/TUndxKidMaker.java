package ga.realcode;

import ga.core.IIndividual;
import ga.core.IKidMaker;

/**
 * UNDXによる子生成器
 * @since 2
 * @author isao
 */
public class TUndxKidMaker implements IKidMaker {
	
	/** UNDXが必要とする親個体の数 */
	private static final int NO_OF_PARENTS = 3;

	/** UNDX */
	private TUndx fUndx;
	
	/**
	 * コンストラクタ
	 * プロパティとして，undx.alpha, undx.betaが必要．
	 * @since 2
	 * @author isao
	 * @author hmkz αβの読み込みがコメントアウトされていた問題を修正
	 */
	public TUndxKidMaker(double alpha, double beta) {
		fUndx = new TUndx(alpha, beta);
	}
	
	/**
	 * システムパラメータαを設定する
	 * @param alpha システムパラメータα
	 * @since 2
	 * @author isao
	 */
	public void setAlpha(double alpha) {
		fUndx.setAlpha(alpha);
	}
	
	/**
	 * システムパラメータαを返す．
	 * @return システムパラメータα
	 * @since 2
	 * @author isao
	 */
	public double getAlpha() {
		return fUndx.getAlpha();
	}
	
	/**
	 * システムパラメータβを設定する．
	 * @param beta システムパラメータβ
	 * @since 2
	 * @author isao
	 */
	public void setBeta(double beta) {
		fUndx.setBeta(beta);
	}
	
	/**
	 * システムパラメータβを返す
	 * @return システムパラメータβ
	 * @since 2
	 * @author isao
	 */
	public double getBeta() {
		return fUndx.getBeta();
	}

	/**
	 * @since 2
	 * @author isao
	 */
	public int getNoOfParents() {
		return NO_OF_PARENTS;
	}

	/**
	 * @see IKidMaker#setParents(IIndividual[])
	 * @since 2
	 * @author isao
	 */
	public void setParents(IIndividual[] parents) throws IllegalArgumentException {
		if (parents.length != NO_OF_PARENTS) {
			throw new IllegalArgumentException("parents.length != NO_OF_PARENTS");
		}
		if (!(parents[0] instanceof IRealNumberCoding)) {
			throw new IllegalArgumentException("parents[0] is not instance of IRealNumberCoding");
		}
		if (!(parents[1] instanceof IRealNumberCoding)) {
			throw new IllegalArgumentException("parents[1] is not instance of IRealNumberCoding");
		}
		if (!(parents[2] instanceof IRealNumberCoding)) {
			throw new IllegalArgumentException("parents[2] is not instance of IRealNumberCoding");
		}
		TVector v1 = ((IRealNumberCoding)parents[0]).getVector();
		TVector v2 = ((IRealNumberCoding)parents[1]).getVector();
		TVector v3 = ((IRealNumberCoding)parents[2]).getVector();
		fUndx.setParents(v1, v2, v3);
	}
	
	/**
	 * @see IKidMaker#doIt(int, IIndividual[])
	 * @since 2
	 * @author isao
	 */
	public void doIt(int noOfKids, IIndividual[] kids) throws IllegalArgumentException {
		if (kids.length < noOfKids) {
			throw new IllegalArgumentException("kids.length < noOfKids");
		}
		if (noOfKids % 2 != 0) {
			throw new IllegalArgumentException("noOfKids % 2 != 0");			
		}
		for (int i = 0; i < noOfKids / 2; ++i) {
			IRealNumberCoding kid1 = (IRealNumberCoding)kids[2 * i];
			IRealNumberCoding kid2 = (IRealNumberCoding)kids[2 * i + 1];
			TVector v1 = kid1.getVector();
			TVector v2 = kid2.getVector();
			fUndx.doIt(v1, v2);
		}
	}
	
}
