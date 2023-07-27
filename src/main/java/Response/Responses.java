package Response;

import Requests.model.RequestsData;
import Response.model.Data;
import model.Point;

public interface Responses {
    Data authorization(String phone, String pin_code);
    Data EventTrigger(String token, String event_id);
    Data ScheduleList(String token, String route_id);
    Data ListOfDriversCars(String token);
    Data ListOfRoutesForTheSelectedCar(String token, String car_id, String route_number);
    Data InformationAboutTheCurrentSession(String token);
    Data InformationAboutDistanceAndOtherStatistic(String token);
    Data ListOfSessionTimesToGetStarted(String token, String car_id, String route_id, String terminus_id);
    Data InformationAboutTheRouteOfTheCurrentSession(String token);
    boolean SendingLocation(String token, Point curPoint);
    void StopSession(String token, String reason);
    Data CancelDeparture(String token);
    Data StartSessionTypeA(String token, String car_id, String route_id, String terminus_id, String time);
    Data ContiniumSessionTypeA(String token, String car_id, String route_id, String terminus_id);
}
