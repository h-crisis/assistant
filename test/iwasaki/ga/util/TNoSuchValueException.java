package ga.util;

/**
 * 要求された値が存在せず，代わりに返すことができる適切な値もない状況で投げられる例外．
 * <p>
 * この例外はRuntimeExceptionを継承しているため，キャッチしなくてもコンパイルエラーにはなりません．
 * <p>
 * この例外は，要求された値が論理的に存在しえないような状況を示唆します．
 * 例えば，要素数0の集合の平均値が要求された場合などにこの例外を投げます．
 * <p>
 * 一時的な障害やセキュリティ上の制限などによる要求拒否を示唆するためにこの例外を使ってはいけません．
 * 例えば，ネットワークの障害やファイルのアクセス権限などの理由により
 * 値が読み出せないような場合には，この例外を投げてはいけません．
 * それらの用途にはJDKにそれなりの例外が用意されていますのでそちらを利用してください．
 * @since 74
 * @author hmkz
 */
public class TNoSuchValueException extends RuntimeException {
	/**
	 * 空のメッセージをもった例外を生成する
	 * @since 74
	 * @author hmkz
	 */
	public TNoSuchValueException() {}

	/**
	 * 指定されたメッセージをもった例外を生成する
	 * @param message 例外メッセージ
	 * @since 74
	 * @author hmkz
	 */
	public TNoSuchValueException(String message) {
		super(message);
	}
}
