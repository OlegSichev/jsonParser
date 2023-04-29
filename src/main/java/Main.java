import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        listToJson(list);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName){
        try(CSVReader reader = new CSVReader(new FileReader(String.valueOf(fileName)))){
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void listToJson(List<Employee> staff){
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(staff, listType); // TODO вместо вывода, надо сделать сохранение в файл
        writeString(json);
    }

    public static void writeString(String json){
        try (FileWriter file = new FileWriter("new_json.json")){
            file.write(json);
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
