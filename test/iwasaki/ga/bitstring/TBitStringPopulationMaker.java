package ga.bitstring;

import ga.core.IIndividual;
import ga.core.IIndividualFactory;
import ga.core.IInitialPopulationMaker;
import ga.core.TPopulation;
import ga.util.TMyRandom;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * バイナリコーディング個体から構成される初期集団生成器
 * @since 2
 * @author isao
 */
public class TBitStringPopulationMaker implements IInitialPopulationMaker {
	
	/** 集団サイズ */
	private int fPopulationSize = 0;
	
	/** ビット数 */
	private int fNoOfBits = 0;

	/** 個体ファクトリ */
	private TBitStringIndividualFactory fIndividualFactory;
	
	/**
	 * コンストラクタ
	 * @param populationSize 集団サイズ
	 * @param noOfBits ビット数
	 * @since 2
	 */
	public TBitStringPopulationMaker(int populationSize, int noOfBits) {
		fPopulationSize = populationSize;
		fNoOfBits = noOfBits;
		fIndividualFactory = new TBitStringIndividualFactory(fNoOfBits);
	}
	
	/**
	 * ビット数を返す．
	 * @return ビット数
	 * @since 2
	 */
	public int getNoOfBits() {
		return fNoOfBits;
	}

	/**
	 * 集団サイズを返す．
	 * @return 集団サイズ
	 * @since 2
	 */
	public int getPopulationSize() {
		return fPopulationSize;
	}

	/**
	 * 個体をランダムに初期化する．
	 * @param ind 個体
	 * @since 2
	 */
	private void initInitIndividual(IIndividual ind) {
		TBitString bs = ((IBitCoding)ind).getBitString();
		bs.setLength(fNoOfBits);
		TMyRandom rand = TMyRandom.getInstance();
		for(int i = 0; i < bs.getLength(); i++) {
			bs.setData(i, rand.getInteger(0, 1));		
		}
		ind.setStatus(IIndividual.INVALID);
	}
	
	/**
	 * 初期集団を生成する．
	 * @since 2
	 */
	public TPopulation createInitialPopulation() {
		TPopulation result = new TPopulation(new TBitStringIndividualFactory(fNoOfBits), fPopulationSize);
		for (int i = 0; i < result.getSize(); ++i) {
			IIndividual ind = result.getIndividual(i);
			initInitIndividual(ind);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IInitialPopulationMaker#getIndividualFactory()
	 */
	public IIndividualFactory getIndividualFactory() {
		return fIndividualFactory;
	}
	
	/**
	 * 集団をファイルに出力する．
	 * @param pop 集団
	 * @param filename 出力先のファイル名
	 * @since 2
	 */
	private static void writePopulation(TPopulation pop, String filename) {
		try{
			BufferedWriter br1 = new BufferedWriter(new FileWriter(filename));
			PrintWriter pw = new PrintWriter(br1);
			pop.writeTo(pw);
			pw.close();
		} catch(Exception ex) {
		ex.printStackTrace();
		System.exit(5);
		}
	}

	/**
	 * メイン
	 * @param args 集団サイズ 次元数 １変数あたりのビット数 生成範囲の最小値 最大値 イルスケール性(true/false) ファイル名 開始番号 終了番号
	 * @since 2
	 */
	public static void main(String[] args) {
		if(args.length != 5) {
			System.err.println("usage:java ga.TBitStringPopulationFactory popuationSize noOfBits filename startIndex endIndex");
			System.exit(-1);
		}
		int popSize = Integer.parseInt(args[0]);
		int noOfBits = Integer.parseInt(args[1]);
		String filename = args[2];
		int startIndex = Integer.parseInt(args[3]);
		int endIndex = Integer.parseInt(args[4]);
		TBitStringPopulationMaker initPopMaker = new TBitStringPopulationMaker(popSize, noOfBits);
		for(int i = startIndex; i <= endIndex; ++i) {
			String filenameWithIndex = filename + i;
			System.out.print("Creating " + filenameWithIndex + "...   ");
			TPopulation pop = initPopMaker.createInitialPopulation();
			writePopulation(pop, filenameWithIndex);
			System.out.println("done.");
		}
	}
	
}
