package ichikawa.setting;

import java.io.File;

import static ichikawa.setting.CreateBaseMesh5thShapeFile.createBaseMesh5th;

/**
 * Created by manabu on 2016/07/18.
 */
public class CreateBaseShapeFile {
    public static void main(String args[]) {
        createBaseMesh5th(new File("files/ShapeFiles/mesh/Mesh5th.shp"), new File("files/ShapeFiles/municipalities/municipalities_area.shp"), new File("files/CsvFiles/mesh4th_data.csv"), new File("files/ShapeFiles/mesh/Mesh5thBaseNew.shp"),
                "UTF-8", "UTF-8", "UTF-8", "UTF-8");
    }
}
