package RMI;
import java.rmi.Naming;
public class Server {
    public static void main(String[] args) {
        try {
            Calculatorimpl obj = new Calculatorimpl();
            Naming.rebind("rmi://localhost/Calculator", obj);
            System.out.println("Server is running...");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}