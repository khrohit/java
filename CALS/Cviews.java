import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Cviews extends JFrame {

    final JTextField txta = new JTextField();
    final JTextField txtb = new JTextField();
    final JTextField txtresult = new JTextField();

    final JButton add = new JButton("+");
    final JButton sub = new JButton("-");
    final JButton mul = new JButton("*");
    final JButton div = new JButton("/");

    public Cviews() {
        setTitle("Calculator");
        setSize(430, 330);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel aLabel = new JLabel("Enter A:");
        aLabel.setBounds(40, 30, 100, 30);
        add(aLabel);

        txta.setBounds(150, 30, 200, 30);
        add(txta);

        JLabel bLabel = new JLabel("Enter B:");
        bLabel.setBounds(40, 80, 100, 30);
        add(bLabel);

        txtb.setBounds(150, 80, 200, 30);
        add(txtb);

        add.setBounds(40, 140, 70, 35);
        sub.setBounds(120, 140, 70, 35);
        mul.setBounds(200, 140, 70, 35);
        div.setBounds(280, 140, 70, 35);

        add(add);
        add(sub);
        add(mul);
        add(div);

        JLabel resultLabel = new JLabel("Result:");
        resultLabel.setBounds(40, 205, 100, 30);
        add(resultLabel);

        txtresult.setBounds(150, 205, 200, 30);
        txtresult.setEditable(false);
        add(txtresult);
    }
}