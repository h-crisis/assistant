package ga.core;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;
/**
 * 集団
 * @since 2
 * @author yamhan, isao
 * @author kinoshita
 * @author hmkz
 */
public class TPopulation {

	/** 個体の配列 */
	private IIndividual[] fArray = new IIndividual[0];
	
	/** 個体を生成する */
	private IIndividualFactory fIndividualFactory = null;

	/**
	 * コンストラクタ
	 * @since 2
	 */
	public TPopulation() {
	}
	
	/**
	 * コンストラクタ
	 * @param factory 個体ファクトリ
	 * @since 2
	 */
	public TPopulation(IIndividualFactory factory) {
		this(factory, 0);
	}

	/**
	 * コンストラクタ
	 * @param factory 個体ファクトリ
	 * @param size 集団サイズ
	 * @since 2
	 */
	public TPopulation(IIndividualFactory factory, int size) {
		fIndividualFactory = factory;
		fArray = new IIndividual[size];
		for(int i = 0; i < size; i++) {
			fArray[i] = fIndividualFactory.create();
		}	
	}
	
	/**
	 * コピーコンストラクタ
	 * @param src コピー元
	 * @since 2
	 */
	public TPopulation(TPopulation src) {
		fIndividualFactory = src.fIndividualFactory;
		if(fArray.length != src.fArray.length) {		
			fArray = new IIndividual[src.fArray.length];
			for(int i = 0; i < src.fArray.length; i++) {
				fArray[i] = fIndividualFactory.create();
			}
		}
		for(int i = 0; i < src.fArray.length; i++) {
			fArray[i].copyFrom(src.fArray[i]);	
		}	
	}
	
	/**
	 * 個体srcのパラメータを自分にコピーする．
	 * @param src コピー元
	 * @since 2
	 */
	public void copyFrom(TPopulation src) {
		fIndividualFactory = src.fIndividualFactory;
		if(fArray.length != src.fArray.length) {		
			fArray = new IIndividual[src.fArray.length];
			for(int i = 0; i < src.fArray.length; i++) {
				fArray[i] = fIndividualFactory.create();
			}
		}
		for(int i = 0; i < src.fArray.length; i++) {
			fArray[i].copyFrom(src.fArray[i]);	
		}	
	}

	/**
	 * 個体のパラメータを書き込む．
	 * @param pw 書き込み先
	 * @throws Exception 書き込み時のException
	 * @since 2
	 */
	public void writeTo(PrintWriter pw) throws Exception {
		pw.println(fIndividualFactory.getClass().getName());
		pw.println(fArray.length);
		for(int i = 0; i < fArray.length; i++) {
			fArray[i].writeTo(pw);
		}
	}
	
	/**
	 * 個体のパラメータを読み込む．
	 * @param br 読み込み元
	 * @throws Exception 読み込み時のException
	 * @since 2
	 */
	public void readFrom(BufferedReader br) throws Exception {
		String className = br.readLine();
		fIndividualFactory = (IIndividualFactory)Class.forName(className).newInstance();
		StringTokenizer st = new StringTokenizer(br.readLine());	
		int size = Integer.parseInt(st.nextToken());
		fArray = new IIndividual[size];
		for(int i = 0; i < size; i++) {
			fArray[i] = fIndividualFactory.create();	
			fArray[i].readFrom(br);
		}		
	}
	
	/**
	 * 集団サイズをセットする．
	 * @param size 集団サイズ
	 * @since 2
	 */
	public void setSize(int size) {
		if(fArray.length == size) {
			return;
		}
		fArray = new IIndividual[size];
		for(int i = 0; i < size; i++) {
			fArray[i] = fIndividualFactory.create();	
		}
	}
	
	/**
	 * 集団サイズを得る．
	 * @return 集団サイズ
	 * @since 2
	 */
	public int getSize() {
		return fArray.length;
	}
	
	/**
	 * 集団中に個体をセットする．
	 * @param index セットする添え字
	 * @param src セットする個体
	 * @since 2
	 */
	public void setIndividual(int index, IIndividual src) {
		fArray[index] = src;
	}

	/**
	 * 集団中の個体を得る．
	 * @param index 得たい個体の添え字
	 * @return 個体
	 * @since 2
	 */
	public IIndividual getIndividual(int index) {
		return fArray[index];
	}

	/**
	 * 個体ファクトリを返す．
	 * @return 個体ファクトリ
	 * @since 2
	 */
	public IIndividualFactory getIndividualFactory() {
		return fIndividualFactory;
	}

	/**
	 * 個体ファクトリを設定する．
	 * @param factory 個体ファクトリ
	 * @since 2
	 */
	public void setIndividualFactory(IIndividualFactory factory) {
		fIndividualFactory = factory;
	}

	/**
	 * 個体ファクトリと集団内の各個体のすべてが等しい場合はtrue，そうでなければfalse．
	 * @since 55
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TPopulation)) {
			return false;
		}
		TPopulation pop = (TPopulation) o;
		if (fIndividualFactory == null) {
			if (pop.fIndividualFactory != null) {
				return false;
			}
		} else {
			if (!fIndividualFactory.equals(pop.fIndividualFactory)) {
				return false;
			}
		}
		return Arrays.equals(fArray, pop.fArray);
	}
}
