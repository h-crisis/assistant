package ga.realcode;

import ga.util.TComparator;
import ga.util.TMyRandom;

/**
 * 単峰性正規分布交叉(UNDX)
 * @since 2
 * @author isao
 */
public class TUndx {

	/** αのデフォルト値 */	
	public static double DEFAULT_ALPHA = 0.5;
	
	/** ベータのデフォルト値 */
	public static double DEFAULT_BETA = 0.35;
	
	/** システムパラメータ α */
	private double fAlpha;
	
	/** システムパラメータ β */
	private double fBeta;
	
	/** 親１への参照 */
	private TVector fParent1;

	/** 親２への参照 */
	private TVector fParent2;

	/** 親３への参照 */
	private TVector fParent3;

	/** σ１ */
	private double fSigma1;
	
	/** σ２ */
	private double fSigma2;
	
	/** 両親の軸の単位ベクトル e */
	private TVector fEVector;
	
	/** 親１と親２の中点ベクトル */
	private TVector fMean;
	
	/** 親1と親2は同じか？ */
	private boolean isParent1EqualToParent2;
	
	/**
	 * コンストラクタ
	 * @since 2
	 * @author isao
	 * @author hmkz 実装をTUndx(double, double)に委譲した
	 */
	public TUndx() {
		this(DEFAULT_ALPHA, DEFAULT_BETA);
	}
	
	/**
	 * コンストラクタ
	 * @param alpha システムパラメータα
	 * @param beta システムパラメータβ
	 * @since 2
	 * @author isao
	 * @author hmkz 初期化時に生成するベクトルは0次元とした
	 */
	public TUndx(double alpha, double beta) {
		fAlpha = alpha;
		fBeta = beta;
		fEVector = new TVector();
		fMean = new TVector();
	}
	
	/**
	 * システムパラメータαを設定する
	 * @param alpha システムパラメータα
	 * @since 2
	 * @author isao
	 */
	public void setAlpha(double alpha) {
		fAlpha = alpha;
	}
	
	/**
	 * システムパラメータαを返す．
	 * @return システムパラメータα
	 * @since 2
	 * @author isao
	 */
	public double getAlpha() {
		return fAlpha;
	}
	
	/**
	 * システムパラメータβを設定する．
	 * @param beta システムパラメータβ
	 * @since 2
	 * @author isao
	 */
	public void setBeta(double beta) {
		fBeta = beta;
	}
	
	/**
	 * システムパラメータβを返す
	 * @return システムパラメータβ
	 * @since 2
	 * @author isao
	 */
	public double getBeta() {
		return fBeta;
	}

	/**
	 * 親を設定する．
	 * @param p1 親1．主軸に使われる
	 * @param p2 親2．主軸に使われる
	 * @param p3 親3．副軸に使われる
	 * @throws IllegalArgumentException 親の次元数が1つでも異なるとき
	 * @since 2
	 * @author isao
	 * @author hmkz 異常値でも例外が投げられなかったバグを修正
	 */
	public void setParents(TVector p1, TVector p2, TVector p3) {
		fParent1 = p1;
		fParent2 = p2;
		fParent3 = p3;
		int dimension = fParent1.getDimension();
		if (dimension != fParent2.getDimension() || dimension != fParent3.getDimension()) {
			throw new IllegalArgumentException("The dimensions of parent 1, 2 and 3 are different.");
		}
		calcMean(fParent1, fParent2);
		calcUnitVectorAndStandardDeviationForPrimaryComponent(fParent1, fParent2);
		calcUnitVectorAndStandardDeviationForSecondaryComponent(fParent1, fParent3);
	}
	
	/**
	 * 子を生成する．
	 * @param kid1 子1．このオブジェクトの値が生成された子のものに書き換えられる．
	 * @param kid2 子2．このオブジェクトの値が生成された子のものに書き換えられる．
	 * @since 2
	 * @author isao
	 */
	public void doIt(TVector kid1, TVector kid2) {
		int dimension = fParent1.getDimension();
		kid1.setDimension(dimension);
		kid2.setDimension(dimension);
		if (isParent1EqualToParent2) {
			kid1.copyFrom(fParent1);
			kid2.copyFrom(fParent2);
			return;
		}
		TMyRandom rand = TMyRandom.getInstance();
		// step.1
		// ベクトル t を生成
		TVector t = new TVector(dimension);
		for (int i = 0; i < dimension; i++) {
			t.setData(i, rand.getNormalDistributedNumber(0.0, fSigma2));
		}
		// step.2
		// t ← t - (t・e)e
		TVector tmpVector = new TVector(fEVector);
		tmpVector.scalarProduct(t.innerProduct(fEVector));
		t.sub(tmpVector);
		// step.3
		// t ← t + se
		tmpVector.copyFrom(fEVector);
		tmpVector.scalarProduct(rand.getNormalDistributedNumber(0.0, fSigma1));
		t.add(tmpVector);
		// step.4
		kid1.copyFrom(fMean);
		kid1.add(t);
		kid2.copyFrom(fMean);
		kid2.sub(t);
	}

	/**
	 * 親１と親２の中点を求める．
	 * @param v1 親１
	 * @param v2 親２
	 * @since 2
	 * @author isao
	 */
	private void calcMean(TVector v1, TVector v2) {
		fMean.copyFrom(v1);
		fMean.add(v2);
		fMean.scalarProduct(0.5);
	}

	/**
	 * 主軸（親１と親２を結ぶ直線）方向の単位ベクトルと標準偏差を求める
	 * @param v1 親１
	 * @param v2 親２
	 * @since 2
	 * @author isao
	 */
	private void calcUnitVectorAndStandardDeviationForPrimaryComponent(TVector v1, TVector v2) {
		fEVector.copyFrom(v2);
		fEVector.sub(v1);
		double d1 = fEVector.getLength(); // 親１と親２の間の距離
		fSigma1 = fAlpha * d1; //主軸方向の標準偏差
		if (TComparator.equals(d1, 0.0))
			isParent1EqualToParent2 = true;
		else
			isParent1EqualToParent2 = false;	
		fEVector.unitVector(); // 親１と親２を結ぶ直線の方向単位ベクトル
	}
	
	/**
	 * 副軸方向の標準偏差を求める
	 * @param v1 親１
	 * @param v3 親３
	 * @since 2
	 * @author isao
	 */
	private void calcUnitVectorAndStandardDeviationForSecondaryComponent(TVector v1, TVector v3) {
		TVector v1v3 = new TVector(v3); // 親１と親３を結ぶベクトル
		v1v3.sub(v1);
		TVector tmp = new TVector(fEVector);
		tmp.scalarProduct(fEVector.innerProduct(v1v3));
		TVector perpendicular = new TVector(v1v3); // 親３から親１と親２を結ぶ直線へ下ろした垂線ベクトル
		perpendicular.sub(tmp);
		double d2 = perpendicular.getLength();
		fSigma2 = fBeta * d2 / Math.sqrt((double)v1.getDimension()); // 副軸方向の標準偏差		
	}
	
}
