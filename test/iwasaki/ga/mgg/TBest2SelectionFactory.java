package ga.mgg;

/**
 * 上位２個体を選択する生存選択器のファクトリ．
 * @since 2
 * @author isao
 */
public class TBest2SelectionFactory implements ISelectionForSurvivalFactory {
	
	/** 最小化か？ */
	private boolean fMinimization;

	/**
	 * コンストラクタ
	 * @param minimization 最小化：true, 最大化：false
	 */
	public TBest2SelectionFactory(boolean minimization) {
		fMinimization = minimization;
	}
	
	/**
	 * @see mgg.ISelectionForSurvivalFactory#create()
	 * @since 2
	 * @author isao
	 */
	public ISelectionForSurvival create() {
		return new TBest2Selection(fMinimization);
	}

}
