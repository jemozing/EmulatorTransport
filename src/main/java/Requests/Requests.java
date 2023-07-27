package Requests;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public abstract class Requests {
    protected JsonObject response;
    protected HttpURLConnection con;
    private String url;

    @Getter
    @Setter
    private String bodyParameters;

    @Getter
    @Setter
    private boolean useBodyParams;

    @Getter
    @Setter
    protected String AuthorizationKey;
    @Getter
    @Setter
    protected boolean useAuthKey = false;

    private int status;

    protected void setUrl(String url) {
        this.url = url;
    }

    protected JsonObject POST_request() {
        //String url = "https://devsrv.ru/api/v1/driver/auth/login";
        //bodyParameters = "{\"phone\":\""+ phone +"\",\"pin_code\":\""+ pin_code  +"\"}";
        byte[] postData = bodyParameters.getBytes(UTF_8);
        //log.debug(url + "  " + bodyParameters);
        try {
            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            if (useAuthKey) con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            try (var wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            StringBuilder content = null;
            status = con.getResponseCode();
            //log.debug("Статус сервера: " + status);
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
                log.debug(url + "  " + bodyParameters + "Запрос прошел удачно, статус сервера: " + status);
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
                JsonObject errResponse = (JsonObject) JsonParser.parseString(content.toString());
                log.error(errResponse.toString());
                throw new MalformedURLException();
            }
            response = (JsonObject) JsonParser.parseString(content.toString());
            log.debug("Ответ сервера: " + response.toString());

        } catch (UnknownHostException | MalformedURLException hostE) {
            log.error("Не удалость подключится к серверу: " + hostE.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            assert con != null;
            con.disconnect();
        }
        return response;
    }

    protected JsonObject GET_request() {

        //var bodyParameters = "{\"route_id\":\"" + route_id + "\"}";
        //log.debug(url);
        try {
            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
            con.setRequestMethod("GET");
            if (useAuthKey) con.setRequestProperty("Authorization", "Bearer " + AuthorizationKey);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            StringBuilder content = null;

            if (useBodyParams) {
                byte[] postData = bodyParameters.getBytes(UTF_8);
                try (var wr = new DataOutputStream(con.getOutputStream())) {

                    wr.write(postData);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            status = con.getResponseCode();
            //log.debug("Статус сервера: " + status);
            if (status == HttpURLConnection.HTTP_CREATED || status == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {

                    String line;
                    content = new StringBuilder();

                    while ((line = in.readLine()) != null) {

                        content.append(line);
                        content.append(System.lineSeparator());
                    }
                }
                log.debug(url + " Запрос прошел удачно, статус сервера: "+ status);
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
                JsonObject errResponse = (JsonObject) JsonParser.parseString(content.toString());
                log.error(errResponse.toString());
                throw new MalformedURLException();
            }
            response = (JsonObject) JsonParser.parseString(content.toString());
            log.debug("Ответ сервера: " + response.toString());
        } catch (UnknownHostException | MalformedURLException hostE) {
            log.error("Не удалость подключится к серверу: " + hostE.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            assert con != null;
            con.disconnect();
        }
        return response;
    }

}
