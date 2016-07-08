package ga.bitstring;

import ga.util.TMyRandom;


/**
 * 一様交叉
 * @since 2
 * @author isao
 */
public class TUx {
	
	/** 親１ */
	private TBitString fParent1;
	
	/** 親２ */
	private TBitString fParent2;

	/**
	 * コンストラクタ
	 * @since 2
	 */
	public TUx() {
		fParent1 = null; 
		fParent2 = null;
	}
	
	/**
	 * 両親を設定する．
	 * @param p1 親１
	 * @param p2 親２
	 * @since 2
	 */
	public void setParents(TBitString p1, TBitString p2) {
		fParent1 = p1;
		fParent2 = p2;
	}
	
	/**
	 * 子個体を生成する．
	 * @param kid1 子１
	 * @param kid2 子２
	 * @since 2
	 */
	public void doIt(TBitString kid1, TBitString kid2) {
		TMyRandom rand = TMyRandom.getInstance();
		int length = fParent1.getLength();
		kid1.setLength(length);
		kid2.setLength(length);
		for (int i = 0; i < length; ++i) {
			if (rand.getInteger(0, 1) == 0) {
				kid1.setData(i, fParent1.getData(i));
				kid2.setData(i, fParent2.getData(i));
			} else {
				kid1.setData(i, fParent2.getData(i));
				kid2.setData(i, fParent1.getData(i));				
			}
		}
	}
	
}
