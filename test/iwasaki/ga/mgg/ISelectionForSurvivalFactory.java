package ga.mgg;
/**
 * 生存選択器ファクトリ
 * @since 2
 * @author isao
 */
public interface ISelectionForSurvivalFactory {

	/**
	 * 生存選択器のインスタンスを生成する．
	 * @return 生存選択器のインスタンス
	 * @since 2
	 * @author isao
	 */
	ISelectionForSurvival create();

}
