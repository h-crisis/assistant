package ga.core;


/**
 * 集団の内部状態をモニタリングするためのモニタ・インターフェース
 * @since 2
 * @author yamhan
 */
public interface IMonitor {
	
	/**
	 * ロギングを開始する．必要に応じて，ストリーム等をオープンすること．
	 * @param baseDir ロギング先のフォルダ
	 * @since 2
	 */
	void startMonitoring(String baseDir);
	
	/**
	 * ログ出力．
	 * @param iteration 世代数
	 * @param evalCount 評価回数
	 * @param bestIndex 最良個体の配列番号
	 * @param average 集団の評価値の平均
	 * @param pop 集団
	 * @param parents この生成に参加した親
	 * @param kids 生成された全ての子
	 * @since 2
	 */
	void output(long iteration, long evalCount, int bestIndex,
							 double average, TPopulation pop, 
							 IIndividual[] parents, IIndividual[] kids);
	
	/**
	 * ロギングを終了する．
	 * @since 2
	 */
	void endMonitoring();
											
}
