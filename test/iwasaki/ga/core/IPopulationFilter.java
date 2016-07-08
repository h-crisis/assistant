package ga.core;


/**
 * 集団の中から特定の条件を満たす個体のみを選び出すために用いるフィルタ．
 * {@link #accept(int, IIndividual)}メソッドに，個体がフィルタを通過する条件を書き，コールバックして使用する．
 * 使用例はTPopulationStatisticsを参照してください．
 * @since 98
 * @author hmkz
 */
public interface IPopulationFilter {
	/**
	 * 引数で指定された個体がこのフィルタを通過するかどうかを判定する．
	 * @param index 個体のインデックス
	 * @param ind このフィルタを通過するかどうか判定を受ける個体．
	 * @return 個体がフィルタを通過する場合はtrue，そうでなければfalse．
	 * @since 98
	 */
	public boolean accept(int index, IIndividual ind);
}

