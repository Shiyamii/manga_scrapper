package mangaAPI;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SQLFileWriter {
    public static void writeFile(String name, ArrayList<String> sqlRequests){
        String content = String.join(";\n",sqlRequests);
        File file = new File("./SQL/");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            Files.writeString(Paths.get("./SQL/"+name+".sql"), content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeFilePW(String name, ArrayList<String> sqlRequests){
        String content = String.join(";\n",sqlRequests);
        File file = new File("./account/");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            Files.writeString(Paths.get("./account/"+name+".csv"), content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
