package ichikawa.estimate;

import org.opengis.feature.simple.SimpleFeature;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by manabu on 2016/07/18.
 * 5次メッシュが被災地かを判別するスレッドクラス
 */
public class EstimateMesh5thDamageThread implements Callable<Boolean> {

    private SimpleFeature mesh;
    private HashMap<String, Double> siMap;
    private int siMeshLevel;

    /**
     * 5次メッシュが被災地かを判別するスレッドクラスのコンストラクタ
     * @param feature 5次メッシュのフィーチャ
     * @param map メッシュコードと震度のデータ
     * @param level メッシュコードのメッシュレベル
     */
    public EstimateMesh5thDamageThread(SimpleFeature feature, HashMap<String, Double> map, int level) {
        this.mesh = feature;
        this.siMap = map;
        this.siMeshLevel = level;
    }

    /**
     * 5次メッシュが被災地かを判別する
     * @return 被災地ならtrue、それ以外ならfalse
     * @throws Exception
     */
    public Boolean call() throws Exception {
        String code = "";
        switch (siMeshLevel) { // 震度情報のメッシュレベルに応じて判定コードを取得
            case 1:
                code = (String)mesh.getAttribute("MESH1ST"); // 1次メッシュコード取得
                break;
            case 2:
                code = (String)mesh.getAttribute("MESH2ND"); // 2次メッシュコード取得
                break;
            case 3:
                code = (String)mesh.getAttribute("MESH3RD"); // 3次メッシュコード取得
                break;
            case 4:
                code = (String)mesh.getAttribute("MESH4TH"); // 4次メッシュコード取得
                break;
            case 5:
                code = (String)mesh.getAttribute("MESH5TH"); // 5次メッシュコード取得
                break;
            default:
                break;
        }

        if(siMap.containsKey(code)) {
            if(siMap.get(code)>=5.0)
                return true; // 被災地で計測震度5.0以上あればtrue
            else
                return false;
        }
        else
            return false; // 非被災地であればfalse
    }
}
