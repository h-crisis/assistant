package iwasaki.ga.bitstring;

import iwasaki.ga.core.IKidMaker;
import iwasaki.ga.core.IKidMakerFactory;

/**
 * ��l�����t�@�N�g��
 * @since 2
 * @author isao
 */
public class TUxKidMakerFactory implements IKidMakerFactory {

	/**
	 * �R���X�g���N�^
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
