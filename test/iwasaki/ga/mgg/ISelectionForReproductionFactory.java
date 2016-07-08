package ga.mgg;
/**
 * 複製選択器ファクトリ
 * @since 2
 * @author isao
 */
public interface ISelectionForReproductionFactory {
	
	/**
	 * 複製選択器を作成する．
	 * @return 複製選択器
	 * @since 2
	 * @author isao
	 */
	public ISelectionForReproduction create();

}
