package ga.mgg;
/**
 * TBestAndRankBasedRouletteSelectionのファクトリ
 * @since 2
 * @author isao
 */
public class TBestAndRankBasedRouletteSelectionFactory implements ISelectionForSurvivalFactory {
	
	/** 最小化か？ */
	private boolean fMinimization;

	public TBestAndRankBasedRouletteSelectionFactory(boolean isMinimization) {
		fMinimization = isMinimization;
	}
	
	/**
	 * @see ISelectionForSurvivalFactory#create()
	 * @since 2
	 * @author isao
	 */
	public ISelectionForSurvival create() {
		return new TBestAndRankBasedRouletteSelection(fMinimization);
	}

}
