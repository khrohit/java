import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ccontroller implements ActionListener {

    private final Cmodels cm;
    private final Cviews cv;

    public Ccontroller(Cmodels cm, Cviews cv) {
        this.cm = cm;
        this.cv = cv;

        cv.add.addActionListener(this);
        cv.sub.addActionListener(this);
        cv.mul.addActionListener(this);
        cv.div.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double a = Double.parseDouble(cv.txta.getText().trim());
            double b = Double.parseDouble(cv.txtb.getText().trim());
            double result;

            if (e.getSource() == cv.add) {
                result = cm.add(a, b);
            } else if (e.getSource() == cv.sub) {
                result = cm.subtract(a, b);
            } else if (e.getSource() == cv.mul) {
                result = cm.multiply(a, b);
            } else if (e.getSource() == cv.div) {
                result = cm.div(a, b);
            } else {
                return;
            }

            cv.txtresult.setText(String.valueOf(result));

        } catch (NumberFormatException ex) {
            cv.txtresult.setText("Valid numbers enter karo");
        } catch (ArithmeticException ex) {
            cv.txtresult.setText(ex.getMessage());
        }
    }
}