package ga.util;


/**
 * ルーレット
 * @since 2
 * @author yamhan, isao
 */
public class TRoulette {

	/** スロットの数 */
	private int fNoOfSlots;
	
	/** 現在のスロットの位置 */
	private int fCurrentSlotIndex;
	
	/** スロット */
	private double[] fSlots;

	/**
	 * コンストラクタ
	 * @since 2
	 * @author yamhan, isao
	 */
	public TRoulette() {
		fCurrentSlotIndex = 0;
		setNoOfSlots(0);
	}
	
	/**
	 * コンストラクタ
	 * @param noOfSlots ルーレットのスロットの数
	 * @since 2
	 * @author yamhan, isao
	 */
	public TRoulette(int noOfSlots) {
		fCurrentSlotIndex = 0;
		setNoOfSlots(noOfSlots);
	}
	
	/**
	 * ルーレットsrcのパラメータをコピーする．
	 * @param src コピー元のルーレット
	 * @since 2
	 * @author yamhan, isao
	 */
	public void copyFrom(TRoulette src) {
		setNoOfSlots(src.fNoOfSlots);
		fCurrentSlotIndex = src.fCurrentSlotIndex;
		for(int i = 0; i < fNoOfSlots; i++)
			fSlots[i] = src.fSlots[i];
	}	
	
	/**
	 * スロット数をセットする．
	 * @param noOfSlots スロット数
	 * @since 2
	 * @author yamhan, isao
	 */
	public void setNoOfSlots(int noOfSlots) {
		if(fNoOfSlots == noOfSlots)
			return;
		resetCurrentSlotIndex();
		fSlots = new double[noOfSlots];
		fNoOfSlots = noOfSlots;				
	}
	
	/**
	 * スロット数を返す．
	 * @return スロット数
	 * @since 2
	 * @author yamhan, isao
	 */
	public int getNoOfSlots() {
		return fNoOfSlots;
	}
		
	/**
	 * 現在のスロットの位置をリセットする．<BR>
	 * カレントスロットを0にする．
	 * @since 2
	 * @author yamhan, isao
	 */	
	public void resetCurrentSlotIndex() {
		fCurrentSlotIndex = 0;
	}
	
	/**
	 * 現在のスロットの位置を返す．
	 * @return 現在のスロットの位置
	 * @since 2
	 * @author yamhan, isao
	 */
	public int getCurrentSlotIndex() {
		return fCurrentSlotIndex;
	}
	
	/**
	 * スロットに値をセットする．
	 * @param value セットする値
	 * @since 2
	 * @author yamhan, isao
	 */
	public void setValueToSlot(double value) {
		if(fCurrentSlotIndex == 0) {
			fSlots[fCurrentSlotIndex] = value;
		} else{		
			fSlots[fCurrentSlotIndex] = 
				fSlots[fCurrentSlotIndex-1] + value;
		}
		fCurrentSlotIndex++;		
	}
	
	/**
	 * index番目のスロットの値を返す．
	 * @param index 値を得たいスロットの位置
	 * @return スロットの値
	 * @since 2
	 * @author yamhan, isao
	 */
	public double getSlotValue(int index) {
		return fSlots[index];
	}
	
	/**
	 * ルーレットを回して，ランダムに番号を返す
	 * @return 選ばれた番号
	 * @since 2
	 * @author yamhan, isao
	 */
	public int doIt() {
		int  selectedIndex;
		double randomNum = 0.0;
		randomNum = TMyRandom.getInstance().getDouble(0.0, fSlots[fCurrentSlotIndex-1]);
		for(selectedIndex = 0; selectedIndex < fNoOfSlots; selectedIndex++)
			if(fSlots[selectedIndex] > randomNum)
				return selectedIndex;	
		return selectedIndex;
	}
}
