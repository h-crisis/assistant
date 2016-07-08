package ga.bitstring;

import ga.core.IIndividual;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * ビットストリングの個体
 * @since 2
 * @author isao
 */
public class TBitStringIndividual implements IIndividual, IBitCoding {

	/** 状態（実行可能個体かどうか？） */
	private int fStatus;
	
	/** 評価値 */
	private double fEvaluationValue;
	
	/** ビットストリング */
	private TBitString fBitString;	

	/**
	 * コンストラクタ
	 * @since 2
	 */
	public TBitStringIndividual() {
		fStatus = IIndividual.INVALID;
		fEvaluationValue = Double.NaN;
		fBitString = new TBitString();
	}

	/**
	 * コンストラクタ
	 * @param noOfBits ビット数
	 * @since 2
	 */
	public TBitStringIndividual(int noOfBits) {
		fStatus = IIndividual.INVALID;
		fEvaluationValue = Double.NaN;
		fBitString = new TBitString(noOfBits);
	}

	/**
	 * コンストラクタ
	 * @param src コピー元
	 * @since 2
	 */
	public TBitStringIndividual(TBitStringIndividual src) {
		fStatus = src.fStatus;
		fEvaluationValue = src.fEvaluationValue;
		fBitString = new TBitString(src.fBitString);
	}

	/**
	 * @since 2
	 */
	@Override
	public Object clone() {
		return new TBitStringIndividual(this);		
	}

	/**
	 * コピー
	 * @param src コピー元
	 * @since 2
	 */
	public IIndividual copyFrom(IIndividual src) {
		TBitStringIndividual x = (TBitStringIndividual)src;
		fStatus = x.fStatus;
		fEvaluationValue = x.fEvaluationValue;
		fBitString.copyFrom(x.fBitString);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IIndividual#readFrom(java.io.BufferedReader)
	 */
	public void readFrom(BufferedReader br) throws Exception {
		fStatus = Integer.parseInt(br.readLine());
		fEvaluationValue = Double.parseDouble(br.readLine());
		fBitString.readFrom(br);
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IIndividual#writeTo(java.io.PrintWriter)
	 */
	public void writeTo(PrintWriter pw) {
		pw.println(fStatus);
		pw.println(fEvaluationValue);
		fBitString.writeTo(pw);
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IIndividual#printOn()
	 */
	public void printOn() {
		System.out.println(fStatus);
		System.out.println(fEvaluationValue);
		fBitString.printOn();
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IIndividual#getEvaluationValue()
	 */
	public double getEvaluationValue() {
		return fEvaluationValue;
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IIndividual#setEvaluationValue(double)
	 */
	public void setEvaluationValue(double value) {
		fEvaluationValue = value;
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IIndividual#getStatus()
	 */
	public int getStatus() {
		return fStatus;
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IIndividual#setStatus(int)
	 */
	public void setStatus(int status) {
		fStatus = status;
	}

	/*
	 * (non-Javadoc)
	 * @see ga.bitstring.IBitCoding#getBitString()
	 */
	public TBitString getBitString() {
		return fBitString;
	}

}
