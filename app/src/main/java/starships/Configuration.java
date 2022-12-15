package starships;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    public List<String> getLines(String s){
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(s))){
            String line;
            while ((line = reader.readLine()) != null){
                lines.add(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }
}
