package Response.Services;

import Response.model.AuthResponse;
import Response.model.Car;
import Response.model.CarRoutes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CarData {
    public ArrayList<Car> parseJsonData(JsonObject serverResponse){
        ArrayList<Car> cars = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < serverResponse.getAsJsonArray("result").size(); i++){
            cars.add(gson.fromJson(serverResponse.getAsJsonArray("result").get(i), Car.class));
        }
        return cars;
    }
    public String searchJsonData(ArrayList<Car> carsArrayList, String state_number){
        Iterator<Car> iterator = carsArrayList.iterator();
        int i = 0;
        while (iterator.hasNext()){
            if(iterator.next().getState_number().equals(state_number)){
                return Integer.toString(i);
            } else i++;
        }
        return "NOT_FOUND";
    }
}
