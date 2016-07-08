package ga.core;
/**
 * 単目的最適化用個体ファクトリ・インターフェース
 * @since 2
 * @author yamhan, isao
 */
public interface IIndividualFactory {
	
	/**
	 * 個体のインスタンスを生成する．
	 * @return IIndividual型のインスタンス
	 * @since 2
	 */
	IIndividual create();

}
