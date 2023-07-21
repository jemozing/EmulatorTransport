package Response;

import Requests.model.Account;
import Response.model.AuthResponse;
import Response.model.Data;

public interface Responses {
    Data authorization(Requests.model.Data data);
    Data EventTrigger(Requests.model.Data data);
    Data ScheduleList(Requests.model.Data data);
    Data ListOfDriversCars(Requests.model.Data data);
    Data ListOfRoutesForTheSelectedCar(Requests.model.Data data);
    Data InformationAboutTheCurrentSession(Requests.model.Data data);
    Data InformationAboutDistanceAndOtherStatistic(Requests.model.Data data);
    Data ListOfSessionTimesToGetStarted(Requests.model.Data data);
    Data InformationAboutTheRouteOfTheCurrentSession(Requests.model.Data data);
    Data SendingLocation(Requests.model.Data data);
    Data StopSession(Requests.model.Data data);
    Data CancelDeparture(Requests.model.Data data);
    Data StartSessionTypeA(Requests.model.Data data);
    Data ContiniumSessionTypeA(Requests.model.Data data);
}
