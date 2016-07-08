package ga.bitstring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ビットストリング
 * @since 2
 * @author isao
 */
public class TBitString {
	
	/** データ */
	private int[] fArray;
	
	/**
	 * コンストラクタ
	 * @since 2
	 */
	public TBitString() {
		this(1);
	}
	
	/**
	 * コンストラクタ
	 * @param length ビットストリング長
	 * @since 2
	 */
	public TBitString(int length) {
		fArray = new int [length];
	}
	
	/**
	 * コンストラクタ
	 * @param str 0と1からなる文字列
	 * @since 2
	 */
	public TBitString(String str) {
		fArray = new int [1];
		fromString(str);
	}
	
	/**
	 * コンストラクタ
	 * @param src コピー元
	 * @since 2
	 */
	public TBitString(TBitString src) {
		fArray = new int [src.fArray.length];
		for (int i = 0; i < src.fArray.length; ++i) {
			fArray[i] = src.fArray[i];
		}
	}
	
	/**
	 * コピー
	 * @param src コピー元
	 * @since 2
	 */
	public void copyFrom(TBitString src) {
		setLength(src.fArray.length);
		for (int i = 0; i < src.fArray.length; ++i) {
			fArray[i] = src.fArray[i];
		}
	}
	
	/**
	 * 標準出力へ出力する．
	 * @since 2
	 */
	public void printOn() {
		System.out.println(toString());
	}
	
	/**
	 * 入力ストリームからデータを読み込む
	 * @param br 入力ストリーム
	 * @throws IOException 読み込みに失敗した場合に投げられる．
	 * @since 2
	 */
	public void readFrom(BufferedReader br) throws IOException {
		fromString(br.readLine());
	}
	
	/**
	 * 出力ストリームへデータを書き出す．
	 * @param pw 出力ストリーム
	 * @throws IOException 書き出しに失敗した場合に投げられる．
	 * @since 2
	 */
	public void writeTo(PrintWriter pw) {
		pw.println(toString());
	}
	
	/**
	 * ビットストリング長を設定する．
	 * @param length ビットストリング長
	 * @since 2
	 */
	public void setLength(int length) {
		if (fArray.length == length) {
			return;			
		}
		fArray = new int [length];
	}
	
	/**
	 * ビットストリング長を返す．
	 * @return ビットストリング長
	 * @since 2
	 */
	public int getLength() {
		return fArray.length;
	}

	/**
	 * index番目のデータを返す．
	 * @param index 添え字
	 * @return データ (0 or 1)
	 * @since 2
	 */
	public int getData(int index) {
		return fArray[index];
	}
	
	/**
	 * index番目にdataを設定する．
	 * @param index 添え字
	 * @param data データ (0 or 1)
	 * @since 2
	 */
	public void setData(int index, int data) {
		fArray[index] = data;
	}

	/**
	 * 全てのビットにdataを設定する．
	 * @param data データ (0 or 1)
	 * @since 2
	 */
	public void setToAllBits(int data) {
		for (int i = 0; i < fArray.length; ++i) {
			fArray[i] = data;	
		}
	}
	
	/**
	 * index番目のdataを反転する．
	 * @param index 添え字
	 * @since 2
	 */
	public void flip(int index) {
		fArray[index] = (fArray[index] == 0 ? 1 : 0);
	}
	
	/**
	 * 全てのビットを反転する．
	 * @since 2
	 */
	public void flipAllBits() {
		for (int i = 0; i < fArray.length; ++i) {
			flip(i);
		}
	}
	
	/**
	 * xとの論理積をとる．
	 * @param x ビットストリング
	 * @since 2
	 */
	public void and(TBitString x) {
		for (int i = 0; i < fArray.length; ++i) {
			fArray[i] *= x.fArray[i];
		}
	}
	
	/**
	 * xとの論理和をとる．
	 * @param x ビットストリング
	 * @since 2
	 */
	public void or(TBitString x) {
		for (int i = 0; i < fArray.length; ++i) {
			int tmp = fArray[i] + x.fArray[i];
			fArray[i] = tmp > 0 ? 1 : 0;
		}
	}
	
	/**
	 * xとの排他的論理和をとる．
	 * @param x ビットストリング
	 * @since 2
	 */
	public void xor(TBitString x) {
		for (int i = 0; i < fArray.length; ++i) {
			if (fArray[i] == x.fArray[i]) {
				fArray[i] = 0;
			} else {
				fArray[i] = 1;
			}
		}		
	}
	
	/**
	 * 0と1からなる文字列でデータを初期化する．
	 * @param str 0と1からなる文字列
	 * @since 2
	 */
	public void fromString(String str) {
		setLength(str.length());
		for (int i = 0; i < str.length(); ++i) {
			fArray[i] = str.charAt(i) == '0' ? 0 : 1;
		}
	}

	/**
	 * 文字列に変換する．
	 * @return 0と1からなる文字列
	 * @since 2
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(fArray.length);
		for (int i = 0; i < fArray.length; ++i) {
			if (fArray[i] == 0) {
				sb.append('0');
			} else {
				sb.append('1');
			}
		}
		return sb.toString();
	}

}
