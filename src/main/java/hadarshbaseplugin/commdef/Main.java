//package hadarshbaseplugin.commdef;
//
//import com.hlsii.commdef.Constants;
//import com.hlsii.util.ConfigUtil;
//import hadarshbaseplugin.HadoopStorageHBaseImpl;
//import hadarshbaseplugin.api.IHadoopStorage;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.MessageFormat;
//
///**
// * @author ScXin
// * @date 5/8/2020 3:01 PM
// */
//public class Main {
//    public static void main(String[] args) {
//        String hbaseSettingFile = ConfigUtil.getConfigFilesDir() + File.separator + "hbaseSetting.json";
//        IHadoopStorage hadoopStorage=null;
//        if (hadoopStorage == null) {
//            hadoopStorage = new HadoopStorageHBaseImpl();
//        }
//        try {
//
//            System.out.println(hbaseSettingFile);
//            if (!hadoopStorage.initialize(hbaseSettingFile)) {
//                System.out.println(
//                        "Error: Initializing HadoopStorageHBaseImpl with the config file {0})");
//                hadoopStorage = null;
//            }
//        } catch (IOException ex) {
//           ex.printStackTrace();
//        }
//    }
//}
