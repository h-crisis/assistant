package ga.core;
/**
 * 個体生成器ファクトリ・インターフェース
 * @since 2
 * @author yamhan
 */
public interface IKidMakerFactory {

	/**
	 * 個体生成器のインスタンスを新しく作り，返す．
	 * @return 作られた個体生成器のインスタンス
	 * @since 2
	 */
	IKidMaker create();
	
}
