package Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Point;
import model.RouteBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class DataBaseRequests {
    static Logger logger = LoggerFactory.getLogger(DataBaseRequests.class);
    HashMap<String, RouteBase> baseData = new HashMap<>();

    public RouteBase readDataBase(String pathname) throws IOException {
        RouteBase routeBase = new RouteBase();
        File CSVfile = new File(pathname);
        Scanner sc = new Scanner(CSVfile);
        sc.nextLine();
        sc.useDelimiter(",");
        routeBase.setId(sc.nextInt());
        routeBase.setCity_id(sc.nextInt());
        routeBase.setUser_id(sc.nextInt());
        routeBase.setNumber(sc.nextInt());
        routeBase.setName(sc.next());

        sc.useDelimiter("}]\"");
        StringBuilder route_forward_buffer = new StringBuilder(sc.next());
        StringBuilder route_backward_buffer = new StringBuilder(sc.next());
        route_forward_buffer.delete(0, 2);
        route_forward_buffer.append("}]");
        route_backward_buffer.delete(0, 2);
        route_backward_buffer.append("}]");
        sc.close();

        JsonArray json_forward = (JsonArray) JsonParser.parseString(route_forward_buffer.toString().replace("\"\"", "\""));
        JsonArray json_backward = (JsonArray) JsonParser.parseString(route_backward_buffer.toString().replace("\"\"", "\""));
        //logger.debug(json_forward.toString());
        JsonObject object;
        String name;
        for (JsonElement element : json_forward) {
            object = element.getAsJsonObject();
            if (object.keySet().size() == 2) {
                name = object.get("name").getAsString();
            } else {
                name = null;
            }
            routeBase.addCoordinatesForward(new Point(
                    object.get("coordinates").getAsJsonArray().get(0).getAsBigDecimal(),
                    object.get("coordinates").getAsJsonArray().get(1).getAsBigDecimal(),
                    name
            ));
        }
        for (JsonElement element : json_backward) {
            object = element.getAsJsonObject();
            if (object.keySet().size() == 2) {
                name = object.get("name").getAsString();
            } else {
                name = null;
            }
            routeBase.addCoordinatesBackward(new Point(
                    object.get("coordinates").getAsJsonArray().get(0).getAsBigDecimal(),
                    object.get("coordinates").getAsJsonArray().get(1).getAsBigDecimal(),
                    name
            ));
        }
        return routeBase;
    }

    public void addData(String id, RouteBase routeBase) {
        baseData.put(id, routeBase);
    }

    public RouteBase getData(String id) {
        return baseData.get(id);
    }
}

