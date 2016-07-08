package ga.realcode;

import ga.core.IIndividual;
import ga.core.IIndividualFactory;
import ga.core.IInitialPopulationMaker;
import ga.core.TPopulation;
import ga.util.TMyRandom;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * 実数ベクトル個体から構成される初期集団生成器．
 * ただし，初期化の定義域は，[0.0, 1.0]である．
 * @since 2
 * @author isao
 */
public class TRealNumberPopulationMaker implements IInitialPopulationMaker {

	/** 集団サイズ */
	private int fPopulationSize = 0;

	/** 次元数 */
	private int fDimension = 0;
	
	/** 個体ファクトリ */
	private IIndividualFactory fIndividualFactory = null;
	
	/** 定義域の最小値 */
	public static final double MIN = 0.0;
	
	/** 定義域の最大値 */
	public static final double MAX = 1.0;
	
	/**
	 * コンストラクタ
	 * @since 2
	 * @author isao
	 * @author hmkz 実装を委譲した
	 */
	public TRealNumberPopulationMaker(int popSize, int dim) {
		fPopulationSize = popSize;
		fDimension = dim;
		fIndividualFactory = new TRealNumberIndividualFactory();
	}
	
	/**
	 * コピーコンストラクタ
	 * @param src コピー元
	 * @since 2
	 * @author isao
	 * @author hmkz 個体ファクトリと問題のコピー処理を追加
	 */
	public TRealNumberPopulationMaker(TRealNumberPopulationMaker src) {
		fPopulationSize = src.fPopulationSize;
		fIndividualFactory = src.fIndividualFactory;
		fDimension = src.fDimension;
	}
	
	/**
	 * コピー
	 * @param src コピー元
	 * @return コピー結果
	 * @since 2
	 * @author isao
	 * @author hmkz 個体ファクトリと問題のコピー処理を追加
	 */
	public TRealNumberPopulationMaker copyFrom(TRealNumberPopulationMaker src) {
		fPopulationSize = src.fPopulationSize;
		fIndividualFactory = src.fIndividualFactory;
		fDimension = src.fDimension;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ga.IInitialPopulationMaker#getIndividualFactory()
	 */
	public IIndividualFactory getIndividualFactory() {
		return fIndividualFactory;
	}
	
	/**
	 * 個体のランダム初期化を行う．
	 * @param ind 個体
	 * @since 2
	 * @author isao
	 * @author hmkz 確実に実行可能解を作るため，個体生成の後に評価も行うように変更した
	 */
	private void initInitIndividual(IIndividual ind) {
		TMyRandom rand = TMyRandom.getInstance();
		TVector v = ((IRealNumberCoding)ind).getVector();
		v.setDimension(fDimension);
		for(int i = 0; i < v.getDimension(); i++) {
			v.setData(i, rand.getDouble(MIN, MAX));		
		}
	}
	
	/**
	 * ランダムな集団を生成する．
	 * 問題が設定されている場合，実行可能解のみからなる集団が生成される．
	 * @return 集団
	 * @since 2
	 * @author isao
	 * @author hmkz 問題が設定されている場合，必ず実行可能解を生成するようにした．
	 */
	public TPopulation createInitialPopulation() {
		TPopulation result = new TPopulation(fIndividualFactory, fPopulationSize);
		for (int i = 0; i < result.getSize(); ++i) {
			IIndividual ind = result.getIndividual(i);
			initInitIndividual(ind);
		}
		return result;
	}
	
	/**
	 * 集団サイズを返す．
	 * @return 集団サイズ
	 * @since 2
	 * @author isao
	 */
	public int getPopulationSize() {
		return fPopulationSize;
	}

	/**
	 * 集団サイズを設定する．
	 * @param populationSize 集団サイズ
	 * @since 2
	 * @author isao
	 */
	public void setPopulationSize(int populationSize) {
		fPopulationSize = populationSize;
	}

	/**
	 * 次元数を返す．
	 * @return 次元数
	 * @since 2
	 * @author isao
	 */
	public int getDimension() {
		return fDimension;
	}

	/**
	 * 次元数を設定する．
	 * @param dim 次元数
	 * @since 2
	 * @author isao
	 */
	public void setDimension(int dim) {
		fDimension = dim;
	}
	
	/**
	 * 集団をファイルへ出力する．
	 * @param pop 集団
	 * @param filename ファイル名
	 * @since 2
	 * @author isao
	 */
	private static void writePopulation(TPopulation pop, String filename) {
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			pop.writeTo(pw);
			pw.close();
		} catch(Exception ex) {
		ex.printStackTrace();
		System.exit(5);
		}
	}

	/**
	 * メイン
	 * @param args 集団サイズ 次元数 ファイル名 開始番号 終了番号
	 * @since 2
	 * @author isao
	 */
	public static void main(String[] args) {
		if (args.length != 5) {
			System.err.println("usage:java funcOpt.TRealNumberPopulationMaker popuationSize Dimension filename startIndex endIndex");
			System.exit(-1);
		}
		int popSize = Integer.parseInt(args[0]);
		int dim = Integer.parseInt(args[1]);
		String filename = args[2];
		int startIndex = Integer.parseInt(args[3]);
		int endIndex = Integer.parseInt(args[4]);
		TRealNumberPopulationMaker initPopMaker = new TRealNumberPopulationMaker(popSize, dim);
		for(int i = startIndex; i <= endIndex; ++i) {
			String filenameWithIndex = filename + i;
			System.out.print("Creating " + filenameWithIndex + "...   ");
			TPopulation pop = initPopMaker.createInitialPopulation();
			writePopulation(pop, filenameWithIndex);
			System.out.println("done.");
		}
	}
	
}
