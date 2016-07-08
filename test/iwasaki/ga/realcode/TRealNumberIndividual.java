package ga.realcode;

import ga.core.IIndividual;
import ga.util.TComparator;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * 実数値ベクトルコーディングの個体
 * @since 2
 * @author yamhan
 * @author hmkz
 */
public class TRealNumberIndividual implements IIndividual, IRealNumberCoding {

	/** 状態（実行可能個体かどうか？） */
	private int fStatus;
	
	/** 評価値 */
	private double fEvaluationValue;
	
	/** 実数ベクトル */
	private TVector fRealVector;

	/**
	 * コンストラクタ
	 * @since 2
	 */
	public TRealNumberIndividual() {
		this(0);
	}
	
	/**
	 * コンストラクタ
	 * @param dimension 次元数
	 * @since 2
	 */
	public TRealNumberIndividual(int dimension) {
		fStatus = INVALID;
		fEvaluationValue = -1.0;
		fRealVector = new TVector(dimension);
	}
	
	/**
	 * コピーコンストラクタ
	 * @param src コピー元
	 * @since 62
	 */
	public TRealNumberIndividual(TRealNumberIndividual src) {
		fStatus = src.fStatus;
		fEvaluationValue = src.fEvaluationValue;
		fRealVector = new TVector(src.fRealVector);
	}

	/**
	 * @see IIndividual#clone()
	 * @since 2
	 */
	@Override
	public Object clone() {
		TRealNumberIndividual result = new TRealNumberIndividual(fRealVector.getDimension());
		result.copyFrom(this);
		return result;
	}

	/**
	 * @see IIndividual#copyFrom(IIndividual)
	 * @since 2
	 */
	public IIndividual copyFrom(IIndividual src) {
		TRealNumberIndividual ind = (TRealNumberIndividual) src;
		fEvaluationValue = ind.fEvaluationValue;
		fRealVector.copyFrom(ind.fRealVector);
		fStatus = ind.fStatus;
		return this;
	}

	/**
	 * @see IIndividual#readFrom(java.io.BufferedReader)
	 * @since 2
	 */
	public void readFrom(BufferedReader br) throws Exception {
		fStatus = Integer.parseInt(br.readLine().trim());
		fEvaluationValue = Double.parseDouble(br.readLine().trim());
		fRealVector.readFrom(br);
	}

	/**
	 * @see IIndividual#writeTo(java.io.PrintWriter)
	 * @since 2
	 */
	public void writeTo(PrintWriter pw) {
		pw.println(fStatus);
		pw.println(fEvaluationValue);
		fRealVector.writeTo(pw);
	}

	/**
	 * @see IIndividual#printOn()
	 * @since 2
	 */
	public void printOn() {
		PrintWriter pw = new PrintWriter(System.out, true);
		writeTo(pw);
	}

	/**
	 * @see IIndividual#getEvaluationValue()
	 * @since 2
	 */
	public double getEvaluationValue() {
		return fEvaluationValue;
	}

	/**
	 * @see IIndividual#setEvaluationValue(double)
	 * @since 2
	 */
	public void setEvaluationValue(double value) {
		fEvaluationValue = value;
	}

	/**
	 * @see IIndividual#getStatus()
	 * @since 2
	 */
	public int getStatus() {
		return fStatus;
	}

	/**
	 * @see IIndividual#setStatus(int)
	 * @since 2
	 */
	public void setStatus(int status) {
		fStatus = status;
	}

	/**
	 * 個体に含まれるベクトルを返す．
	 * @return 個体のベクトル
	 * @since 2
	 */
	public TVector getVector() {
		return fRealVector;
	}

	/**
	 * 個体のもつ評価値，状態，ベクトルがすべて等しい場合にtrueを返す．そうでなければfalse．
	 * 評価値とベクトルのチェックの際には誤差を許容する．
	 * 許容誤差の確認・設定には{@link util.TComparator}クラスを使う．
	 * @since 50
	 */
	@Override
	public boolean equals(Object o) {
		if (! (o instanceof TRealNumberIndividual)) {
			return false;
		}
		TRealNumberIndividual ind = (TRealNumberIndividual) o;
		if (!TComparator.equals(fEvaluationValue, ind.fEvaluationValue)) {
			return false;
		}
		if (fStatus != ind.fStatus) {
			return false;
		}
		
		return fRealVector.equals(ind.fRealVector);
	}
}
