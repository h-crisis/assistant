package ga.mgg;


/**
 * ランダム複製選択器ファクトリ
 * @since 2
 * @author isao
 */
public class TRandomSelectionForReproductionFactory implements ISelectionForReproductionFactory {

	/**
	 * @see ISelectionForReproductionFactory#create()
	 * @since 2
	 * @author isao
	 */
	public ISelectionForReproduction create() {
		return new TRandomSelectionForReproduction();
	}

}
