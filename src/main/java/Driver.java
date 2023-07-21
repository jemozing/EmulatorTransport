import Requests.model.Account;
import Requests.model.Authorization;
import Response.Services.AuthData;
import Response.Services.ServerResponse;
import Response.model.AuthResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Driver implements Runnable{
    DriverData driverData = new DriverData();
    public Driver(Account account){
        this.driverData.setAccount(account);
    }

    ServerResponse serverResponse = new ServerResponse();
    @Override
    public void run() {
        //Как это все работает:
        //сначала производится задержка потока в зависимости от того когда водитель поедет от начала.
        //потом происходить авторизация водителя на сервере через определенный порядок запросов
        //загружается в класс водителя транспорт на котором он будет ездить, у него есть параметры:
        // начальные координаты (долгота и широта)
        // конечные координаты (долгота и широта)
        // название маршрута
        // интервал движения между маршрутами
        // время обновления (частота отправки данных на сервер)
        // скорость транспорта
        // После задается начальная координата транспорта
        // И начинается обход маршрута по точкам с обозначением промежуточных точек

        driverData.setToken(((AuthResponse) serverResponse.authorization(driverData.getAccount())).getToken());
        serverResponse.ListOfDriversCars(new Authorization(driverData.getToken()));
        // serverResponse.ListOfRoutesForTheSelectedCar();
    }
}
