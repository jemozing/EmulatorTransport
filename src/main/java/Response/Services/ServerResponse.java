package Response.Services;

import Requests.model.Account;
import Requests.services.*;
import Response.Responses;
import Response.model.CarRoutes;
import Response.model.Data;

import java.util.ArrayList;

public class ServerResponse implements Responses {
    Authorization authorization = new Authorization();
    AuthData authData = new AuthData();
    CancelDeparture cancelDeparture = new CancelDeparture();
    ContiniumSessionTypeA continiumSessionTypeA = new ContiniumSessionTypeA();
    EventTrigger eventTrigger = new EventTrigger();
    InformationAboutDistanceAndOther informationAboutDistanceAndOther = new InformationAboutDistanceAndOther();
    InformationAboutTheCurrentSession informationAboutTheCurrentSession = new InformationAboutTheCurrentSession();
    InformationAboutTheRouteOfTheCurrentSession informationAboutTheRouteOfTheCurrentSession = new InformationAboutTheRouteOfTheCurrentSession();
    ListOfDriversCars listOfDriversCars = new ListOfDriversCars();
    CarData carData = new CarData();
    ListOfRoutesForTheSelectedCar listOfRoutesForTheSelectedCar = new ListOfRoutesForTheSelectedCar();
    CarRoutesData carRoutesData = new CarRoutesData();
    ListOfSessionTimesToGetStarted listOfSessionTimesToGetStarted = new ListOfSessionTimesToGetStarted();
    ScheduleList scheduleList = new ScheduleList();
    SendingLocation sendingLocation = new SendingLocation();
    StartSessionTypeA startSessionTypeA = new StartSessionTypeA();
    StopSession stopSession = new StopSession();

    @Override
    public Data authorization(Requests.model.Data data) {
        return authData.parseJsonData(authorization.request(data.getData()));
    }

    @Override
    public Data EventTrigger(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data ScheduleList(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data ListOfDriversCars(Requests.model.Data data) {
        return carData.parseJsonData(listOfDriversCars.request(data));
    }

    @Override
    public Data ListOfRoutesForTheSelectedCar(Requests.model.Data data) {
        ArrayList<CarRoutes> carRoutes = carRoutesData.parseJsonData(listOfRoutesForTheSelectedCar.request("",""));
        int id = Integer.parseInt(carRoutesData.searchJsonData(carRoutes,""));
        return carRoutes.get(id);
    }

    @Override
    public Data InformationAboutTheCurrentSession(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data InformationAboutDistanceAndOtherStatistic(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data ListOfSessionTimesToGetStarted(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data InformationAboutTheRouteOfTheCurrentSession(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data SendingLocation(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data StopSession(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data CancelDeparture(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data StartSessionTypeA(Requests.model.Data data) {
        return null;
    }

    @Override
    public Data ContiniumSessionTypeA(Requests.model.Data data) {
        return null;
    }
}
