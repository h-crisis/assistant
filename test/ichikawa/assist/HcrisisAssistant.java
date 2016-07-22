package ichikawa.assist;

/**
 * Created by manabu on 2016/06/24.
 */
public class HcrisisAssistant {
    public static void main(String args[]) throws Exception {
        //doMedicalInstituteEMIS(args[0], args[1], args[2]);


        doMedicalInstituteEMIS("files/WorkingFiles/EMIS医療機関情報_kochi.csv",
                "files/WorkingFiles/medical_status_20160630143035.csv",
                "files/WorkingFiles/outFile_201607221045.csv");

        // doSiCombine();
    }

    /**
    public static void doSiCombine() {
        File file = new File("files/WorkingFiles/EMIS医療機関情報_kochi.csv");
        HashMap<String, Double> map = Si.getSiMap(file, "Shift_JIS");
        System.out.println(map);
    }
     */

    public static void doMedicalInstituteEMIS(String path1, String path2, String path3) throws Exception {
        MedicalInstituteEMIS mie = new MedicalInstituteEMIS(path1, path2, path3);
        mie.comineMedicalInstituteFiles();
        mie.createFeatures();
        mie.createShapeFile();
        mie.createGeoJsonFile();
    }
}
