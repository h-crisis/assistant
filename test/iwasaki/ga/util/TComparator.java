package ga.util;
/**
 * 計算誤差を考慮して実数値の比較を行う．
 * <p>
 * このクラスの比較メソッドでは，
 * 比較する2実数の差の絶対値が設定された許容誤差未満であれば等しいとみなします．
 * <p>
 * doubleやfloatの計算には誤差がつきものなので
 * ==, <, >, <=, >=演算子で比較せず，必ずこのクラスを使いましょう．
 * <p>
 * Doubleクラスの比較メソッド（{@link Double#compareTo(Double)}, {@link Double#equals(Object)}）
 * も許容誤差には対応していないことに注意してください．
 * <p>
 * EPS値を0に設定した場合，このクラスの比較メソッドは==, <, >, <=, >=演算子の動作と一致します．
 * @since 59
 * @author hmkz
 */
public class TComparator {
	
	/** 許容誤差のデフォルト値 */
	public static final double DEFAULT_EPS = 1e-15;
	
	/** 現在設定されている許容誤差 */
	private static double fEps = DEFAULT_EPS;
	
	/**
	 * 許容誤差を設定する．
	 * @param eps 許容誤差
	 * @since 59
	 * @author hmkz
	 */
	public static void setEps(double eps) {
		if (eps < 0) {
			throw new IllegalArgumentException("eps >= 0 is required.");
		}
		TComparator.fEps = eps;
	}
	
	/**
	 * 現在設定されている許容誤差を返す．
	 * @return 許容誤差
	 * @since 59
	 * @author hmkz
	 */
	public static double getEps() {
		return fEps;
	}
	
	/**
	 * 許容誤差つきのa == b．
	 * aとbの差が許容誤差以内であれば真とみなす．
	 * @param a 比較する実数１
	 * @param b 比較する実数２
	 * @return a, bの差の絶対値が許容誤差以内であればtrue，そうでなければfalse．
	 * @since 59
	 * @author hmkz
	 */
	public static boolean equals(double a, double b) {
		return Math.abs(a - b) <= fEps;
	}
	
	/**
	 * 許容誤差つきのa > b．
	 * aがbよりも許容誤差以上に大きければ真とする．
	 * 許容誤差のため，厳密にはa > bであってもこのメソッドがfalseを返しうることに注意すること．
	 * @param a 比較する実数１
	 * @param b 比較する実数２
	 * @return aがbより許容誤差以上大きければtrue，そうでなければfalse．
	 * @since 59
	 * @author hmkz
	 */
	public static boolean isAGreaterThanB(double a, double b) {
		return a > b + fEps;
	}

	/**
	 * 許容誤差つきのa >= b．
	 * aがbと等しいか大きければ真とする．
	 * 許容誤差のため，厳密にはa < bであってもこのメソッドがtrueを返しうることに注意すること．
	 * @param a 比較する実数１
	 * @param b 比較する実数２
	 * @return aがbと等しいか大きければtrue，そうでなければfalse．
	 * @since 59
	 * @author hmkz
	 */
	public static boolean isAGreaterEqualsB(double a, double b) {
		return a >= b - fEps;
	}

	/**
	 * 許容誤差つきのa < b．
	 * aがbよりも許容誤差以上に小さければ真とする．
	 * 許容誤差のため，厳密にはa < bであってもこのメソッドがfalseを返しうることに注意すること．
	 * @param a 比較する実数１
	 * @param b 比較する実数２
	 * @return aがbより許容誤差以上小さければtrue，そうでなければfalse．
	 * @since 59
	 * @author hmkz
	 */
	public static boolean isALessThanB(double a, double b) {
		return a < b - fEps;
	}

	/**
	 * 許容誤差つきのa <= b．
	 * aがbと等しいか小さければ真とする．
	 * 許容誤差のため，厳密にはa > bであってもこのメソッドがtrueを返しうることに注意すること．
	 * @param a 比較する実数１
	 * @param b 比較する実数２
	 * @return aがbと等しいか小さければtrue，そうでなければfalse．
	 * @since 59
	 * @author hmkz
	 */
	public static boolean isALessEqualsB(double a, double b) {
		return a <= b + fEps;
	}
	
	/**
	 * このクラスはインスタンスを作らない．
	 * @since 59
	 * @author hmkz
	 */
	protected TComparator() {}
}
