package RMI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class Calculatorimpl extends UnicastRemoteObject implements Calculator {
     Calculatorimpl() throws RemoteException {
        super();
    }
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

    public int subtract(int a, int b) throws RemoteException {
        return a - b;
    }
    public int multiply(int a, int b) throws RemoteException {
        return a * b;
    }
    public int divide(int a, int b) throws RemoteException {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
    
}
