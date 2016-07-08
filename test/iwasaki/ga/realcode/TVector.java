package ga.realcode;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Arrays;

import ga.util.TComparator;

/**
 * 実数ベクトル
 * @since 2
 * @author yamhan, isao
 * @author hmkz
 */
public class TVector {
	
	/** ベクトルの要素 */
	private double[] fArray;	

	/**
	 * コンストラクタ
	 * @since 2
	 * @author yamhan, isao
	 * @author hmkz {@link #TVector(int)}に委譲
	 */
	public TVector() {
		this(0);
	}
	
	/**
	 * コンストラクタ
	 * @param dimension 
	 * @since 2
	 * @author yamhan, isao
	 */
	public TVector(int dimension) {
		fArray = new double [dimension];
	}
	
	/**
	 * コンストラクタ
	 * @param array 実数値の配列
	 * @since 2
	 * @author yamhan, isao
	 * @author hmkz {@link Arrays#copyOf(double[], int)}を使って書き直す
	 */
	public TVector(double[] array) {
		fArray = Arrays.copyOf(array, array.length);
	}

	/**
	 * コピーコンストラクタ
	 * @param src コピー元
	 * @since 2
	 * @author yamhan, isao
	 * @author hmkz {@link #TVector(double[])}に委譲
	 */
	public TVector(TVector src) {
		this(src.fArray);
	}
	
	/**
	 * ベクトルsrcからパラメータをコピーする．
	 * @param src コピー元のベクトル
	 * @since 2
	 * @author yamhan, isao
	 * @author hmkz {@link System#arraycopy(Object, int, Object, int, int)}を使って書き直す
	 */
	public TVector copyFrom(TVector src) {
		if(fArray.length != src.fArray.length) {		
			fArray = new double[src.fArray.length];
		}
		System.arraycopy(src.fArray, 0, fArray, 0, src.fArray.length);
		return this;
	}
	
	/**
	 * ベクトルのパラメータを書き込む関数．
	 * @param pw 書き込み先
	 * @since 2
	 * @author yamhan, isao
	 */
	public void writeTo(PrintWriter pw) {
		pw.println(fArray.length);
		for(int i = 0; i < fArray.length; i++) {
			pw.print(fArray[i] + " ");
		}
		pw.println();			
	}
	
	/**
	 * ベクトルのパラメータを読み込む関数．
	 * @param br 読み込み元
	 * @throws Exception 読み込み時のException
	 * @since 2
	 * @author yamhan, isao
	 */
	public void readFrom(BufferedReader br) throws Exception {
		int dimension = Integer.parseInt(br.readLine().trim());
		String[] data = br.readLine().split("\\s");
		fArray = new double[dimension];
		for (int i = 0; i < fArray.length; i++) {
			fArray[i] = Double.parseDouble(data[i]);
		}
	}
	
	/**
	 * 標準出力へベクトルの内容を書き出す．
	 * @since 2
	 * @author yamhan
	 */
	public void printOn() {
		System.out.println(fArray.length);
		for (int i = 0; i < fArray.length; ++i)
			System.out.print(fArray[i] + " ");
		System.out.println();
	}
	
	/**
	 * 次元数をセットする．
	 * @param dimension 次元数
	 * @since 2
	 * @author yamhan, isao
	 */
	public void setDimension(int dimension) {
		if(fArray.length == dimension) {		
			return;		
		}
		fArray = new double[dimension];		
	}
		
	/**
	 * 次元数を得る．
	 * @return 次元数
	 * @since 2
	 * @author yamhan, isao
	 */	
	public int getDimension() {
		return fArray.length;
	}
	
	/**
	 * ペクトルにデータをセットする．
	 * @param index セットするデータの位置
	 * @param data データ
	 * @since 2
	 * @author yamhan, isao
	 */
	public void setData(int index, double data) {
		fArray[index] = data;
	}
	
	/**
	 * ベクトルのデータを得る．
	 * @param index 得たいデータの添え字
	 * @return 得たいデータ
	 * @since 2
	 * @author yamhan, isao
	 */
	public double getData(int index) {
		return fArray[index];
	}
	
	/**
	 * ベクトル同士を足す．
	 * @param src 足すベクトル
	 * @since 2
	 * @author yamhan, isao
	 */
	public TVector add(TVector src) {
		for(int i = 0; i < fArray.length; i++) {
			fArray[i] += src.fArray[i];
		}
		return this;
	}
	
	/**
	 * ベクトル同士を引く．
	 * @param src 引くベクトル
	 * @since 2
	 * @author yamhan, isao
	 */
	public TVector sub(TVector src) {
		for(int i = 0; i < fArray.length; i++) {
			fArray[i] -= src.fArray[i];
		}
		return this;
	}
	
	/**
	 * ベクトルを実数倍する．
	 * @param a ベクトルの倍率
	 * @since 2
	 * @author yamhan, isao
	 */
	public TVector scalarProduct(double a) {
		for(int i = 0; i < fArray.length; i++) {
			fArray[i] *= a;
		}
		return this;
	}
	
	/**
	 * ベクトルの内積を返す．
	 * @param y ベクトル
	 * @return yとの内積
	 * @since 2
	 * @author yamhan, isao
	 */
	public double innerProduct(TVector y) {
		double result = 0.0;
		for(int i = 0; i < fArray.length; i++) {
			result += fArray[i] * y.fArray[i];
		}
		return result;	
	}
	
	/**
	 * ベクトルの長さを返す．
	 * @return ベクトルの長さ．
	 * @since 2
	 * @author yamhan, isao
	 */
	public double getLength() {
		double result = 0.0;
		for(int i = 0; i < fArray.length; i++) {
			result += fArray[i] * fArray[i];
		}
		return Math.sqrt(result);
	}
	
	/**
	 * 単位ベクトル化する．
	 * @since 2
	 * @author yamhan, isao
	 */
	public TVector unitVector() {
		double length = getLength();
		for(int i = 0; i < fArray.length; i++) {	
			fArray[i] /= length;
		}
		return this;
	}
 
 	/**
 	 * ベクトルの各要素を0.0で初期化する．
	 * @since 2
	 * @author yamhan, isao
 	 */
	public TVector zeroVector() {
		for(int i = 0; i < fArray.length; i++) {
			fArray[i] = 0.0; 
		}
		return this;
	}
	
	/**
	 * ベクトルoとのユークリッド距離がTComparator.getEps()以下ならばtrue．そうでなければfalse
	 * 許容誤差はTComparatorクラスを用いて確認・設定できる．
	 * @see TComparator#getEps()
	 * @see TComparator#setEps(double)
	 * @since 49
	 * @author hmkz
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TVector)) {
			return false;
		}
		TVector v = (TVector) o;
		if (fArray.length != v.fArray.length) {
			return false;
		}
		return distance(v) <= TComparator.getEps();
	}

	/**
	 * このベクトルと指定されたベクトルvとのユークリッド距離を返す．
	 * ２つのベクトルの次元数が同じであること．
	 * @param v このベクトルと同じ次元数をもつベクトル
	 * @return ユークリッド距離
	 * @since 87
	 * @author hmkz
	 */
	public double distance(TVector v) {
		if (fArray.length != v.fArray.length) {
			throw new IllegalArgumentException(
					"Illegal dimension of v. expected: " + fArray.length + ", but: " + v.fArray.length);
		}

		double sum = 0.0;
		for (int i = 0; i < fArray.length; i++) {
			double xi = fArray[i] - v.fArray[i];
			sum += xi * xi;
		}
		return Math.sqrt(sum);
	}
}
