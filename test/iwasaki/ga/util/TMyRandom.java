package ga.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 乱数クラス．シングルトンパターンが用いられている．
 * @since 2
 * @author yamhan, isao
 * @author hmkz
 */
// 2007/07/21 hmkz メソッド名はgetDoubleよりnext～が好みです．getterでないものをget～と呼ぶのは不自然では？
public class TMyRandom {

	/** 乱数ジェネレータ．1つしか存在しない(Singleton)．*/
	private static TMyRandom fInstance;
	
	/** 乱数生成器 */
	private Random fRandom;
	
	private TMyRandom() {
		fRandom = new TMersenneTwister19937();	
	}
	
	private TMyRandom(long seed){
		fRandom = new TMersenneTwister19937(seed);
	}
	
	/**
	 * 乱数の種をセットする．
	 * @param seed 乱数の種
	 * @since 2
	 * @author yamhan, isao
	 */
	public void setSeed(long seed) {
		fRandom.setSeed(seed);
	}
	
	/**
	 * TMyRandom型のインスタンスを得る．<BR>
	 * 乱数の種は，このメソッド実行時の時間により決定する．
	 * @return TMyRandomのインスタンス
	 * @since 2
	 * @author yamhan, isao
	 */
	public static TMyRandom getInstance() {
		if(fInstance == null) {
			fInstance = new TMyRandom();
		}
		return fInstance;	
	}
	
	/**
	 * TMyRandom型のインスタンスを得る．<BR>
	 * 乱数の種は，seedになる．
	 * @param seed 乱数の種
	 * @return TMyRandomのインスタンス
	 * @since 2
	 * @author yamhan, isao
	 */
	public static TMyRandom getInstance(long seed) {
		if(fInstance == null) {
			fInstance = new TMyRandom(seed);
		} else {
			fInstance.setSeed(seed);
		}
		return fInstance;	
	}

	/**
	 * 指定されたオブジェクトイメージから復元されたTMyRandom型のインスタンスを返す．
	 * @param filename オブジェクトイメージが格納されているファイル名
	 * @return TMyRandom型のインスタンス
	 * @since 2
	 * @author yamhan, isao
	 */
	public static TMyRandom getInstance(String filename) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
			fInstance = (TMyRandom)ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(5);
		}
		return fInstance;
	}
	
	/**
	 * オブジェクトイメージをファイルへ出力する．
	 * @param filename 出力先のファイル名
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @since 2
	 * @author yamhan, isao
	 */
	public static void writeObjectImage(String filename) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		writeObjectImage(fos);
		fos.close();
	}
	
	/**
	 * オブジェクトイメージをストリームへ出力する．
	 * @param os 出力先のストリーム
	 * @throws IOException
	 * @since 2
	 * @author yamhan, isao
	 */
	public static void writeObjectImage(OutputStream os) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(fInstance);
	}
	
	/**
	 * min < = randomDouble < = max となる実数乱数randomDoubleを返す．
	 * @param min 乱数の最小値
	 * @param max 乱数の最大値
	 * @return 実数乱数
	 * @since 2
	 * @author yamhan, isao
	 */
	public double getDouble(double min, double max) {
		double randomDouble = 0.0;
		randomDouble = fRandom.nextDouble();
		randomDouble *= (max - min);
		randomDouble += min;
		return randomDouble;	
	}	

	/**
	 * min <= randomInt <= max となる整数乱数randomIntを返す．
	 * @param min 乱数の最小値
	 * @param max 乱数の最大値
	 * @return 整数乱数
	 * @since 2
	 * @author yamhan, isao
	 */
	public int getInteger(int min, int max) {
		int randomInt = fRandom.nextInt(max-min+1);
		randomInt += min;
		return randomInt;
	}
	
	/**
	 * 正規乱数を返す．
	 * @param mean 平均値
	 * @param sigma 標準偏差
	 * @return 正規乱数
	 * @since 2
	 * @author yamhan, isao
	 */
	public double getNormalDistributedNumber(double mean, double sigma) {
		return mean + sigma * fRandom.nextGaussian();
	}

	/**
	 * trueまたはfalseを五分五分の確率で返す．
	 * @return trueまたはfalse
	 * @since 48
	 * @author hmkz
	 */
	public boolean getBoolean() {
		return fRandom.nextBoolean();
	}
	
	/**
	 * 配列をシャッフルする．
	 * 要素は一様に入れ替わる．
	 * @param buf シャッフルされる配列
	 * @since 19
	 * @author hmkz
	 */
	public void shuffle(int[] buf) {
		for (int i = 0; i < buf.length; i++) {
			int r = getInteger(i, buf.length - 1);
			int tmp = buf[i];
			buf[i] = buf[r];
			buf[r] = tmp;
		}
	}

	/**
	 * 配列をシャッフルする．
	 * 要素は一様に入れ替わる．
	 * @param buf シャッフルされる配列
	 * @since 48
	 * @author hmkz
	 */
	public void shuffle(double[] buf) {
		for (int i = 0; i < buf.length; i++) {
			int r = getInteger(i, buf.length - 1);
			double tmp = buf[i];
			buf[i] = buf[r];
			buf[r] = tmp;
		}
	}

	/**
	 * min <= n <= max となる整数乱数nでbufの要素をうめる．
	 * @param buf 整数乱数が代入される
	 * @param min 乱数の最小値（この値を含む）
	 * @param max 乱数の最大値（この値を含む）
	 * @since 48
	 * @author hmkz
	 */
	public void fill(int[] buf, int min, int max) {
		for (int i = 0; i < buf.length; i++) {
			buf[i] = getInteger(min, max);
		}
	}

	/**
	 * min <= d <= max となる乱数dでbufの要素をうめる．
	 * @param buf 整数乱数が代入される
	 * @param min 乱数の最小値（この値を含む）
	 * @param max 乱数の最大値（この値を含む）
	 * @since 48
	 * @author hmkz
	 */
	public void fill(double[] buf, double min, double max) {
		for (int i = 0; i < buf.length; i++) {
			buf[i] = getDouble(min, max);
		}
	}
}
