import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpRequest {
    Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private JsonObject jsonObject;
    //POST
    //Авторизация
    public JsonObject AuthorizationRequest(String phone, String pin_code) throws IOException {

        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/auth/login";
        var urlParameters = "{\"phone\":\""+ phone +"\",\"pin_code\":\""+ pin_code  +"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        logger.debug(url + "  " + urlParameters);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            try (var wr = new DataOutputStream(con.getOutputStream())) {

                wr.write(postData);
            }

            StringBuilder content = null;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            catch (IOException e){
                logger.error(e.getMessage());
            }
            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        } catch (UnknownHostException hostE){
            logger.error("Не удалость подключится к серверу: " + hostE.getMessage());
        }
        finally {

            assert con != null;
            con.disconnect();
        }
        return jsonObject;
    }
    //POST
    //Отправка тригера события
    public void EventTriggerRequest(String AuthorizationKey, String EventMessageOptional) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/event/send";
        var urlParameters = "{\"event_id\":\"" + EventMessageOptional + "\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        logger.debug(url + "  " + urlParameters);

        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            try (var wr = new DataOutputStream(con.getOutputStream())) {

                wr.write(postData);
            }

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
    //GET
    //Список расписаний
    public JsonObject ScheduleListRequest(String AuthorizationKey, String route_id) {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/schedule";
        var urlParameters = "{\"route_id\":\"" + route_id + "\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        logger.debug(url + "  " + urlParameters);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            StringBuilder content;

            try (var wr = new DataOutputStream(con.getOutputStream())) {

                wr.write(postData);
            }
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        }
        catch (IOException e) {
            logger.error("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Список автомобилей
    public JsonObject ListOfDriversCarsRequest(String AuthorizationKey) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/car";
        logger.debug(url);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                logger.debug(con.getResponseMessage());
                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        } finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Список маршрутов по выбранному автомобилю
    public JsonObject ListOfRoutesForTheSelectedCarRequest(String AuthorizationKey, String car_id) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/car/" + car_id + "/routes";
        logger.debug(url);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        }
        catch (IOException e) {
            logger.error("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Информация о текущий сессии
    public JsonObject InformationAboutTheCurrentSessionRequest(String AuthorizationKey) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session";
        logger.debug(url);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        }
        catch (IOException e) {
            logger.error("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Информация об оптимальной дистанции и другая статистика
    public JsonObject InformationAboutDistanceAndOtherStatistic(String AuthorizationKey) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session/info";
        logger.debug(url);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        }
        catch (IOException e) {
            logger.error("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Список доступных времен для начала работы
    public JsonObject ListOfSessionTimesToGetStartedRequest(String AuthorizationKey, String car_id, String route_id, String terminus_id) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session/times" +
                "?route_id=" + route_id +
                "&car_id=" + car_id +
                "&terminus_id=" + terminus_id;
        logger.debug(url);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        }
        catch (IOException e) {
            logger.error("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Информация о текущем маршруте
    public JsonObject InformationAboutTheRouteOfTheCurrentSessionRequest(String AuthorizationKey) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session/route";
        logger.debug(url);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        }
        catch (IOException e) {
            logger.error("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //POST
    //Отправка геолокации
    public void SendingLocationRequest(String AuthorizationKey, String latiude, String longtiude) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session/location";
        var urlParameters = "{\"lat\":\""+latiude+"\",\"lon\":\""+longtiude+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        logger.debug(url + "  " + urlParameters);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            try (var wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
    //POST
    //Отмена выезда
    public void CancelDepartureRequest(String AuthorizationKey) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session/cancel";
        var urlParameters = "{}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        logger.debug(url + "  " + urlParameters);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            try (var wr = new DataOutputStream(con.getOutputStream())) {

                wr.write(postData);
            }

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
    //POST
    //Остановка текущей сессии
    public void StopSessionRequest(String AuthorizationKey, String reason_optional) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session/stop";
        var urlParameters = "{\"reason\":\""+reason_optional+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        logger.debug(url + "  " + urlParameters);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            try (var wr = new DataOutputStream(con.getOutputStream())) {

                wr.write(postData);
            }

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
    //POST
    //Старт сессии нерегулируемого маршрута
    public JsonObject StartSessionTypeARequest(String AuthorizationKey, String car_id, String route_id, String terminus_id, String time) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session/start/a";
        var urlParameters = "{\"car_id\":\""+car_id+"\",\"route_id\":\""+route_id+"\",\"terminus_id\":\""+terminus_id+"\",\"time\":\""+time+"\"}";
        int status;
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        logger.debug(url + "  " + urlParameters);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            try (var wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            } catch (IOException e){
                logger.error(e.getMessage());
            }
            status = con.getResponseCode();
            logger.debug("Статус сервера: " + status);
            StringBuilder content = null;
            if(status == HttpURLConnection.HTTP_CREATED || status == HttpURLConnection.HTTP_OK) {
                try (var br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {

                    String line;
                    content = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }
                }
                logger.debug("Запрос на начало сессии прошел удачно");
            } else {
                // Ошибка сервера, получаем ошибочный поток ответа
                try (var errorStream = con.getErrorStream()) {
                    if (errorStream != null) {
                        try (var br = new BufferedReader(new InputStreamReader(errorStream))) {
                            String line;
                            content = new StringBuilder();
                            while ((line = br.readLine()) != null) {
                                content.append(line);
                                content.append(System.lineSeparator());
                            }
                        }
                    }
                }
                logger.error(content.toString());
                throw new MalformedURLException();
            }
            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug("Данные с сервера" + jsonObject.toString());

        } finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //POST
    //Продолжить работу на регулируемом маршруте
    public JsonObject ContiniumSessionTypeARequest(String AuthorizationKey, String car_id, String route_id, String terminus_id) throws IOException {
        HttpURLConnection con = null;
        var url = "https://devsrv.ru/api/v1/driver/session/continue/a";
        var urlParameters = "{\"car_id\":\"" + car_id + "\",\"route_id\":\"" + route_id + "\",\"terminus_id\":\"" + terminus_id + "\"}";
        int status;
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        logger.debug(url + "  " + urlParameters);
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            try (var wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            status = con.getResponseCode();
            logger.debug("Статус сервера: " + status);
            StringBuilder content = null;
            if (status == HttpURLConnection.HTTP_CREATED || status == HttpURLConnection.HTTP_OK) {
                try (var br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {

                    String line;
                    content = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }
                }
                logger.debug("Запрос на начало сессии прошел удачно");
            } else {
                // Ошибка сервера, получаем ошибочный поток ответа
                try (var errorStream = con.getErrorStream()) {
                    if (errorStream != null) {
                        try (var br = new BufferedReader(new InputStreamReader(errorStream))) {
                            String line;
                            content = new StringBuilder();
                            while ((line = br.readLine()) != null) {
                                content.append(line);
                                content.append(System.lineSeparator());
                            }
                        }
                    }
                }
                logger.error(content.toString());
                throw new MalformedURLException();
            }
            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            logger.debug("Данные с сервера" + jsonObject.toString());

        } finally {

            con.disconnect();
        }
        return jsonObject;
    }

}
