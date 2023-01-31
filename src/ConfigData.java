import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ConfigData {

    public void readConfigFile(String pathname){
        try {
            File file = new File(pathname);
            System.out.println(file.getAbsolutePath());
            Scanner sc = new Scanner(file);
            sc.useDelimiter(",");
            while (sc.hasNext()){
                System.out.println(sc.next());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
