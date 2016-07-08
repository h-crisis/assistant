package ga.util;


/**
 * ランダムな組み合わせ生成器
 * @since 2
 * @author isao
 */
public class TRandomCombination {
	
	/** 作業領域．0～n-1までの整数が格納されている */
	private int[] fWork;
	
	/**
	 * コンストラクタ
	 * @since 2
	 * @author isao
	 */
	public TRandomCombination() {
		fWork = new int [1];
		fWork[0] = 0;
	}
	
	/**
	 * 0～n-1の整数のうち，重複を許さずにランダムにarray.length個を選択して，
	 * arrayに格納して返す．
	 * n == array.lengthの場合，arrayには，0～n-1のランダムな順列が格納される．
	 * @param n 入力パラメータ
	 * @param array 結果
	 * @since 2
	 * @author isao
	 */
	public void doIt(int n, int array[]) {
		setSize(n);
		TMyRandom rand = TMyRandom.getInstance();
		for (int i = 0; i < array.length; ++i) {
			int j = rand.getInteger(i, n - 1);
			int tmp = fWork[i];
			fWork[i] = fWork[j];
			fWork[j] = tmp;
			array[i] = fWork[i];
		}
	}
	
	/**
	 * 作業領域の大きさを変更する．
	 * @param size 作業領域の大きさ
	 * @since 2
	 * @author isao
	 */
	private void setSize(int size) {
		if (fWork.length == size)
			return;
		fWork = new int [size];
		for (int i = 0; i < size; ++i)
			fWork[i] = i;
	}

}
