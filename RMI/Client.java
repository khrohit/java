package RMI;
import java .rmi.Naming;
public class Client {
    public static void main(String[] args) {
        try{Calculator cal = (Calculator) Naming.lookup("rmi://localhost/Calculator");
            
            System.out.println("Adition: " + cal.add( 20,10));
            System.out.println("Subtraction: " + cal.subtract( 20,10));
        
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
