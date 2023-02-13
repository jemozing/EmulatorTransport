import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpRequest {
    private HttpURLConnection con;
    //POST
    public JsonObject AuthorizationRequest(String postURI, String phone, String pin_code) throws IOException {
        var url = "https://devsrv.ru/" + postURI;
        var urlParameters = "{\"phone\":\""+ phone +"\",\"pin_code\":\""+ pin_code  +"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
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
    //GET
    public void ListOfDriversCarsRequest(String postURI, String AuthorizationKey) throws IOException {
        var url = "https://devsrv.ru/" + postURI;

        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer" + AuthorizationKey);
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

            System.out.println(content.toString());

        } finally {

            con.disconnect();
        }

    }
    //GET
    public void ListOfRoutesForTheSelectedCarRequest(String postURI, String AuthorizationKey, String car_id) throws IOException {}
    //GET
    public void CurrentSessionRequest(String postURI, String AuthorizationKey) throws IOException {}
    //GET
    public void InformationAboutTheCurrentSessionRequest(String postURI, String AuthorizationKey) throws IOException {}
    //GET
    public void ListOfSessionTimesToGetStartedRequest(String postURI, String AuthorizationKey, String car_id, String route_id, String terminus_id) throws IOException {}
    //GET
    public void InformationAboutTheRouteOfTheCurrentSessionRequest(String postURI, String AuthorizationKey) throws IOException {}
    //POST
    public void SendingLocationRequest(String postURI, String AuthorizationKey, String latiude, String longtiude) throws IOException {
        var url = "https://devsrv.ru/" + postURI;
        var urlParameters = "{\"lat\":\""+latiude+"\",\"lon\":\""+longtiude+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer" + AuthorizationKey);
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
    public void CancelDepartureRequest(String postURI, String AuthorizationKey) throws IOException {
        var url = "https://devsrv.ru/" + postURI;
        var urlParameters = "{}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer" + AuthorizationKey);
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
    public void StopSessionRequest(String postURI, String AuthorizationKey, String reason_optional) throws IOException {
        var url = "https://devsrv.ru/" + postURI;
        var urlParameters = "{\"reason\":\""+reason_optional+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer" + AuthorizationKey);
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
    public void StartSessionTypeARequest(String postURI, String AuthorizationKey, String car_id, String route_id, String terminus_id, String time) throws IOException {
        var url = "https://devsrv.ru/" + postURI;
        var urlParameters = "{\"car_id\":\""+car_id+"\",\"route_id\":\""+route_id+"\",\"terminus_id\":\""+terminus_id+"\",\"time\":\""+time+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer" + AuthorizationKey);
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
    public void StopSessionTypeARequest(String postURI, String AuthorizationKey, String car_id, String route_id, String terminus_id) throws IOException {
        var url = "https://devsrv.ru/" + postURI;
        var urlParameters = "{\"car_id\":\""+car_id+"\",\"route_id\":\""+route_id+"\",\"terminus_id\":\""+terminus_id+"\"}";
        byte[] postData = urlParameters.getBytes(UTF_8);
        JsonObject jsonObject;
        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer" + AuthorizationKey);
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
