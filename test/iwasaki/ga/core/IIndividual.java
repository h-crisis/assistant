package ga.core;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * 単目的最適化用個体インターフェース
 * @since 2
 * @author yamhan, isao
 */
public interface IIndividual {

	/** 個体が生存していることを表す． */
	int VALID = 1;

	/** 個体が死亡していることを表す． */
	int INVALID = 0;

	/**
	 * 自分自身の複製を返す．
	 * @return 自分自身の複製
	 * @since 2
	 */
	Object clone();

	/**
	 * srcを自分自身にコピーして，その結果を返す．
	 * @param src コピー元
	 * @return 自分自身
	 * @since 2
	 */
	IIndividual copyFrom(IIndividual src);

	/**
	 * 入力ストリームからデータを読み込む．
	 * @param br 入力ストリーム
	 * @throws Exception
	 * @since 2
	 */
	void readFrom(BufferedReader br) throws Exception;

	/**
	 * 出力ストリームへデータを書き出す．
	 * @param pw 出力ストリーム
	 * @since 2
	 */
	void writeTo(PrintWriter pw);

	/**
	 * 標準出力へデータを書き出す．
	 * @since 2
	 */
	void printOn();

	/**
	 * 評価値を返す．
	 * @return 評価値
	 * @since 2
	 */
	double getEvaluationValue();

	/**
	 * 評価値を設定する．
	 * @param value 評価値
	 * @since 2
	 */
	void setEvaluationValue(double value);

	/**
	 * 個体が生存しているか死亡しているかの値を返す． 
	 * @return IIndividual.VALID:生存, IIndividual.INVALID:死亡
	 * @since 2
	 */
	int getStatus();	

	/**
	 * 個体が生存しているか死亡しているかの値を設定する．
	 * @param status IIndividual.VALID:生存, IIndividual.INVALID:死亡
	 * @since 2
	 */
	void setStatus(int status);

}
