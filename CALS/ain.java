import javax.swing.SwingUtilities;

public class ain {
    public static void main(String[]args) {
        SwingUtilities.invokeLater(() -> {
            Cmodels model = new Cmodels();
            Cviews view = new Cviews();
            new Ccontroller(model, view);
            view.setVisible(true);
            
        });
    }
}