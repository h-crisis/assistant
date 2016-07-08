package ga.bitstring;

import ga.core.IKidMaker;
import ga.core.IKidMakerFactory;

/**
 * 一様交叉ファクトリ
 * @since 2
 * @author isao
 */
public class TUxKidMakerFactory implements IKidMakerFactory {

	/**
	 * コンストラクタ
	 * @since 2
	 */
	public TUxKidMakerFactory() {
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IKidMakerFactory#create()
	 */
	public IKidMaker create() {
		return new TUxKidMaker();
	}

}
