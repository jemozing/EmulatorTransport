import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpRequest {
    private HttpURLConnection con;
    private JsonObject jsonObject;
    //POST
    //Авторизация
    public JsonObject AuthorizationRequest(String phone, String pin_code) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/auth/login";
        var urlParameters = "{\"phone\":\""+ phone +"\",\"pin_code\":\""+ pin_code  +"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);

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
            System.out.println(jsonObject.toString());

        } finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //POST
    //Отправка тригера события
    public void EventTriggerRequest(String AuthorizationKey, String EventMessageOptional) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/event/send";
        var urlParameters = "{\"event_id\":\"" + EventMessageOptional + "\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);

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
            System.out.println(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
    //GET
    //Список расписаний
    public JsonObject ScheduleListRequest(String AuthorizationKey, String route_id) {
        var url = "https://devsrv.ru/api/v1/driver/schedule";
        var urlParameters = "{\"route_id\":\"" + route_id + "\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
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
            System.out.println(jsonObject.toString());

        }
        catch (IOException e) {
            System.out.println("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Список автомобилей
    public JsonObject ListOfDriversCarsRequest(String AuthorizationKey) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/car";

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
            System.out.println(jsonObject.toString());

        } finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Список маршрутов по выбранному автомобилю
    public JsonObject ListOfRoutesForTheSelectedCarRequest(String AuthorizationKey, String car_id) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/car/" + car_id + "/routes";

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
            System.out.println(jsonObject.toString());

        }
        catch (IOException e) {
            System.out.println("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Информация о текущий сессии
    public JsonObject InformationAboutTheCurrentSessionRequest(String AuthorizationKey) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session";

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
            System.out.println(jsonObject.toString());

        }
        catch (IOException e) {
            System.out.println("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Информация об оптимальной дистанции и другая статистика
    public JsonObject InformationAboutDistanceAndOtherStatistic(String AuthorizationKey) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session/info";

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
            System.out.println(jsonObject.toString());

        }
        catch (IOException e) {
            System.out.println("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Список доступных времен для начала работы
    public JsonObject ListOfSessionTimesToGetStartedRequest(String AuthorizationKey, String car_id, String route_id, String terminus_id) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session/times" +
                "?route_id=" + route_id +
                "&car_id=" + car_id +
                "&terminus_id=" + terminus_id;
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
            System.out.println(jsonObject.toString());

        }
        catch (IOException e) {
            System.out.println("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //GET
    //Информация о текущем маршруте
    public JsonObject InformationAboutTheRouteOfTheCurrentSessionRequest(String AuthorizationKey) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session/route";

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
            System.out.println(jsonObject.toString());

        }
        catch (IOException e) {
            System.out.println("Сервер не отвечает");
        }
        finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //POST
    //Отправка геолокации
    public void SendingLocationRequest(String AuthorizationKey, String latiude, String longtiude) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session/location";
        var urlParameters = "{\"lat\":\""+latiude+"\",\"lon\":\""+longtiude+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
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
            System.out.println(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
    //POST
    //Отмена выезда
    public void CancelDepartureRequest(String AuthorizationKey) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session/cancel";
        var urlParameters = "{}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
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
            System.out.println(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
    //POST
    //Остановка текущей сессии
    public void StopSessionRequest(String AuthorizationKey, String reason_optional) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session/stop";
        var urlParameters = "{\"reason\":\""+reason_optional+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
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
            System.out.println(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
    //POST
    //Старт сессии регулируемого маршрута
    public JsonObject StartSessionTypeARequest(String AuthorizationKey, String car_id, String route_id, String terminus_id, String time) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session/start/a";
        var urlParameters = "{\"car_id\":\""+car_id+"\",\"route_id\":\""+route_id+"\",\"terminus_id\":\""+terminus_id+"\",\"time\":"+time+"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                try (var wr = new DataOutputStream(con.getOutputStream())) {

                    wr.write(postData);
                }
            } else {
                String s = con.getErrorStream().toString();
            }
            status = con.getResponseCode();
            StringBuilder content = null;
            if (status == HttpURLConnection.HTTP_OK) {
                try (var br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {

                    String line;
                    content = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }
                }
            } else {
            }

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(content.toString());
            System.out.println(jsonObject.toString());

        } finally {

            con.disconnect();
        }
        return jsonObject;
    }
    //POST
    //Продолжить работу на регулируемом маршруте
    public void ContinuSessionTypeARequest(String AuthorizationKey, String car_id, String route_id, String terminus_id) throws IOException {
        var url = "https://devsrv.ru/api/v1/driver/session/continue/a";
        var urlParameters = "{\"car_id\":\""+car_id+"\",\"route_id\":\""+route_id+"\",\"terminus_id\":\""+terminus_id+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
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
            System.out.println(jsonObject.toString());

        } finally {

            con.disconnect();
        }
    }
}
