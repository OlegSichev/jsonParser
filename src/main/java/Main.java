import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        listToJson(list);

        List<Employee> list2 = parseXML("data.xml");
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

    public static List<Employee> parseXML (String fileName) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));

        Node root = doc.getDocumentElement();
        NodeList nodeListChildRoot = root.getChildNodes();
        List<String> employeeValues = new ArrayList<>();
        Employee employee = new Employee(Long.parseLong(employeeValues.get(0)), employeeValues.get(1), employeeValues.get(3), employeeValues.get(4), Integer.parseInt(employeeValues.get(5)));
        System.out.println(employee);
        for (int i = 0; i < nodeListChildRoot.getLength(); i++) {
            Node node_ = nodeListChildRoot.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()){
                Element element = (Element) node_;
                NamedNodeMap map = element.getAttributes();
                for (int a = 0; a < map.getLength(); a++) {
                    String attrName = map.item(a).getNodeName();
                    String attrValue = map.item(a).getNodeValue();
                    employeeValues.add(attrValue);
                }
                }
                // TODO не доделано, не считывает документ
            }
        return null;
    }

    public static void listToJson(List<Employee> staff){
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(staff, listType);
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
