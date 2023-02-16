import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Transport> transport_route = new ArrayList<>();
        transport_route.add(new Transport(1,1,1,1,"20",1,1,1) {});
        transport_route.get(0).setName("20");
        ConfigData configData = new ConfigData();
        configData.readConfigFile("config.csv");
        HttpRequest request = new HttpRequest();
        request.AuthorizationRequest("7 000 000-00-01", "1111");
        DataBaseRequests dataBaseRequests = new DataBaseRequests();
        Runnable task = () -> {
            try {
                dataBaseRequests.readDataBase("C:\\Users\\ponch\\IdeaProjects\\EmulatorTransport\\out\\production\\EmulatorTransport/RouteFiles/mbus_421.csv");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        //System.out.println(timeDistance(73.3017573000000055571945267729461193084716796875,55.07409440899999708562972955405712127685546875,73.29325199999999540523276664316654205322265625,55.071810999999996738551999442279338836669921875,20));
        Thread driver = new Driver(task);
        driver.start();
    }

    public static class Driver extends Thread{
        private Runnable task;

        public Driver(Runnable task){
            this.task = task;
        }
        @Override
        public void run() {
                if(task!= null){
                    task.run();
                }

        }
    }
}