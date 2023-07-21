package Response.Services;

import Response.model.CarRoutes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

@Slf4j
public class CarRoutesData {

    public ArrayList<CarRoutes> parseJsonData(JsonObject jsonObject){
        ArrayList<CarRoutes> settings = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < jsonObject.getAsJsonArray("result").size(); i++){
            settings.add(gson.fromJson(jsonObject.getAsJsonArray("result").get(i), CarRoutes.class));
        }
        return settings;
    }
    public String searchJsonData(ArrayList<CarRoutes> carRoutesArrayList, String route_number){
        Iterator<CarRoutes> iterator = carRoutesArrayList.iterator();
        int i = 0;
        while (iterator.hasNext()){
            if(iterator.next().getNumber().equals(route_number)){
                return Integer.toString(i);
            } else i++;
        }
        return "NOT_FOUND";
    }

}
