package ga.core;


/**
 * 個体生成器（交叉，突然変異，etc.）はこのインターフェースを実装すること．
 * @since 2
 * @author yamhan
 */
public interface IKidMaker {

	/**
	 * 個体生成に必要な親個体の数を返す．<BR>
	 * @return 個体生成に必要な親個体の数
	 * @since 2
	 */
	int getNoOfParents();
	
	/**
	 * 個体生成に使用される親個体をセットする．
	 * 渡すべき親個体の数は{@link #getNoOfParents()}で得ること．
	 * @param parents 親個体
	 * @throws IllegalArgumentException parentsの要素数がgetNoOfParentsの戻り値と異なるとき．parentsにnullが含まれるとき．
	 * このインターフェースの実装者はこの他にも例外を投げる条件を追加してよい．
	 * @since 2
	 */
	void setParents(IIndividual[] parents);
	
	/**
	 * setParentsで設定された親個体からnoOfKids個の子個体を生成する．
	 * 生成された子個体は，配列kidsへ代入されて返却される．
	 * @param noOfKids 生成すべき子個体の数
	 * @param kids 生成された子個体を受け取る配列．配列の各要素はIIndividualを実装したクラスのインスタンスで初期化されていなければならない．
	 * @throws IllegalArgumentException noOfKids > kids.lengthのとき．kidsにnullが含まれるとき．
	 * このインターフェースの実装者はこの他にも例外を投げる条件を追加してよい．
	 * @since 2
	 */
	void doIt(int noOfKids, IIndividual[] kids);
	
}
