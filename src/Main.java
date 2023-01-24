import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Transport> transport_route = new ArrayList<>();
        transport_route.add(new Transport(1,1,1,1,"20",1,1,1) {});
        transport_route.get(0).setName("20");
    }
}