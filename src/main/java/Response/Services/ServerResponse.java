package Response.Services;

import Requests.services.*;
import Response.Responses;
import Response.model.Car;
import Response.model.CarRoutes;
import Response.model.Data;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import model.Point;

import java.util.ArrayList;
import java.util.Iterator;
@Slf4j
public class ServerResponse implements Responses {
    Authorization authorization = new Authorization();
    AuthData authData = new AuthData();
    CancelDeparture cancelDeparture = new CancelDeparture();
    ContiniumSessionTypeA continiumSessionTypeA = new ContiniumSessionTypeA();
    ContiniunSessionData continiunSessionData = new ContiniunSessionData();
    EventTrigger eventTrigger = new EventTrigger();
    InformationAboutDistanceAndOther informationAboutDistanceAndOther = new InformationAboutDistanceAndOther();
    InformationAboutTheCurrentSession informationAboutTheCurrentSession = new InformationAboutTheCurrentSession();
    SessionData sessionData = new SessionData();
    InformationAboutTheRouteOfTheCurrentSession informationAboutTheRouteOfTheCurrentSession = new InformationAboutTheRouteOfTheCurrentSession();
    RouteData routeData = new RouteData();
    ListOfDriversCars listOfDriversCars = new ListOfDriversCars();
    CarData carData = new CarData();
    ListOfRoutesForTheSelectedCar listOfRoutesForTheSelectedCar = new ListOfRoutesForTheSelectedCar();
    CarRoutesData carRoutesData = new CarRoutesData();
    ListOfSessionTimesToGetStarted listOfSessionTimesToGetStarted = new ListOfSessionTimesToGetStarted();
    ListSessionTimeData listSessionTimeData = new ListSessionTimeData();
    ScheduleList scheduleList = new ScheduleList();
    SendingLocation sendingLocation = new SendingLocation();
    StartSessionTypeA startSessionTypeA = new StartSessionTypeA();
    StopSession stopSession = new StopSession();

    static ArrayList<String> usedCarsNum = new ArrayList<>();

    @Override
    public Data authorization(String phone, String pin_code) {
        return authData.parseJsonData(authorization.request(phone, pin_code));
    }

    @Override
    public Data EventTrigger(String token, String event_id) {
        return null;
    }

    @Override
    public Data ScheduleList(String token, String route_id) {
        return null;
    }

    @Override
    public Data ListOfDriversCars(String token) {
        ArrayList<Car> cars = carData.parseJsonData(listOfDriversCars.request(token));
        Iterator<Car> carIterator = cars.iterator();
        Car car = new Car();
        Iterator<String> usedCarsNumIter = usedCarsNum.iterator();
        boolean usedCar = false;
        int i = 0;
        if(usedCarsNumIter.hasNext()){
            while (carIterator.hasNext()){
                car = carIterator.next();
                while (usedCarsNumIter.hasNext()){
                    if(car.getState_number().equals(usedCarsNumIter.next())){
                        usedCar = true;
                        break;
                    }
                }
                if(!usedCar){
                    usedCarsNum.add(cars.get(i).getState_number());
                    return cars.get(i);
                }
                usedCar = false;
                i++;
            }
        }
        usedCarsNum.add(cars.get(i).getState_number());
        return cars.get(i);
    }

    @Override
    public Data ListOfRoutesForTheSelectedCar(String token, String car_id, String route_number) {
        ArrayList<CarRoutes> carRoutes = carRoutesData.parseJsonData(listOfRoutesForTheSelectedCar.request(token,car_id));
        int id = Integer.parseInt(carRoutesData.searchJsonData(carRoutes,route_number));
        return carRoutes.get(id);
    }

    @Override
    public Data InformationAboutTheCurrentSession(String token) {
        return sessionData.parseJsonData(informationAboutTheCurrentSession.request(token));
    }

    @Override
    public Data InformationAboutDistanceAndOtherStatistic(String token) {
        informationAboutDistanceAndOther.request(token);
        return null;
    }

    @Override
    public Data ListOfSessionTimesToGetStarted(String token, String car_id, String route_id, String terminus_id) {
        return listSessionTimeData.parseJsonData(listOfSessionTimesToGetStarted.request(token, car_id, route_id, terminus_id));
    }

    @Override
    public Data InformationAboutTheRouteOfTheCurrentSession(String token) {
        return routeData.parseJsonData(informationAboutTheRouteOfTheCurrentSession.request(token));
    }

    @Override
    public boolean SendingLocation(String token, Point curPoint) {
        return sendingLocation.request(token,Double.toString(curPoint.getLat()), Double.toString(curPoint.getLon())).get("result").getAsBoolean();
    }

    @Override
    public void StopSession(String token, String reason) {
        stopSession.request(token,reason);
    }

    @Override
    public Data CancelDeparture(String token) {
        return null;
    }

    @Override
    public Data StartSessionTypeA(String token, String car_id, String route_id, String terminus_id, String time) {
        return null;
    }

    @Override
    public Data ContiniumSessionTypeA(String token, String car_id, String route_id, String terminus_id) {
        return continiunSessionData.parseJsonData(continiumSessionTypeA.request(token,car_id,route_id,terminus_id));
    }
}
