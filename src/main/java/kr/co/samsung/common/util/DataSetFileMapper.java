package kr.co.samsung.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.nexacro.xapi.data.DataSet;

public class DataSetFileMapper {
    public static boolean uploadFileFromDataSet(DataSet dataSet) throws IOException {
        int dsRowNum = dataSet.getRowCount();
        for(int index = 0; index < dsRowNum; index++) {
            byte[] buffer = dataSet.getBlob(index, "photo_file");
            
            if(buffer != null) {
                String fileName = dataSet.getString(index, "save_name");
                String filePath = "C:/Dev/Server/apache-httpd-2.2.25/htdocs/adios2/resources/photo/";
                FileOutputStream fos = new FileOutputStream(new File(filePath + fileName));
                fos.write(buffer);
                fos.flush();
                fos.close();
            }
        }
        return true;
    }
}
