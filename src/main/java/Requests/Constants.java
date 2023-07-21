package Requests;

import lombok.Data;

@Data
public class Constants {
    public static final String Authorization = "auth/login";
    public static final String CancelDeparture = "session/cancel";
    public static final String ContiniumSessionTypeA = "session/continue/a";
    public static final String EventTrigger = "event/send";
    public static final String InformationAboutDistanceAndOther = "session/info";
    public static final String InformationAboutTheCurrentSession = "session";
    public static final String InformationAboutTheRouteOfTheCurrentSession = "session/route";
    public static final String ListOfDriversCars = "car";
    public static final String ListOfRoutesForTheSelectedCar = "car/";
    public static final String ListOfSessionTimesToGetStarted = "session/times";
    public static final String ScheduleList = "schedule";
    public static final String SendingLocation = "session/location";
    public static final String StartSessionTypeA = "session/start/a";
    public static final String StopSession = "session/stop";
}
