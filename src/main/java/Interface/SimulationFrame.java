package Interface;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;

public class SimulationFrame extends JFrame
{
    //Pentru a testa daca intru in buton:
    private int TestButon;

    //Labeluri pentru primul view:
    private JLabel titlu = new JLabel("Introduce-ti datele de intrare:");
    private JLabel clientNumber = new JLabel("Numarul de clienti: ");
    private JLabel serverNumber = new JLabel("Numarul de servere: ");
    private JLabel minArr = new JLabel("Timpul minim de arrival: ");
    private JLabel maxArr = new JLabel("Timpul maxim de arrival: ");
    private JLabel minProc = new JLabel("Timpul minim de processing: ");
    private JLabel maxProc = new JLabel("Timpul maxim de processing: ");
    private JLabel maxTime = new JLabel("Timpul simularii: ");

    //Text fielduri pentru variabile primul view:
    private JTextField clientNumbert = new JTextField(10);
    private JTextField serverNumbert = new JTextField(10);
    private JTextField minArrt = new JTextField(10);
    private JTextField maxArrt = new JTextField(10);
    private JTextField minProct = new JTextField(10);
    private JTextField maxProct = new JTextField(10);
    private JTextField maxTimet = new JTextField(10);

    //Buton pentru a merge din view 1 in 2;
    private JButton IncepereSimulare = new JButton("Simulare");

    //Labelul de titlu si textArea pentru view 2:
    private JLabel titlu1 = new JLabel("Serverele si clientii:");
    private JTextArea GenerareServereClienti = new JTextArea("");

    //View 1:
    public void DateIntrare()
    {
        //Setez marime si font si size:
        titlu.setFont(new Font("Times New Roman", Font.BOLD, 50));
        clientNumber.setFont(new Font("Times New Roman", Font.BOLD, 30));
        serverNumber.setFont(new Font("Times New Roman", Font.BOLD, 30));
        minArr.setFont(new Font("Times New Roman", Font.BOLD, 30));
        maxArr.setFont(new Font("Times New Roman", Font.BOLD, 30));
        minProc.setFont(new Font("Times New Roman", Font.BOLD, 30));
        maxProc.setFont(new Font("Times New Roman", Font.BOLD, 30));
        maxTime.setFont(new Font("Times New Roman", Font.BOLD, 30));

        clientNumbert.setFont(new Font("Times New Roman", Font.BOLD, 20));
        serverNumbert.setFont(new Font("Times New Roman", Font.BOLD, 20));
        minArrt.setFont(new Font("Times New Roman", Font.BOLD, 20));
        maxArrt.setFont(new Font("Times New Roman", Font.BOLD, 20));
        minProct.setFont(new Font("Times New Roman", Font.BOLD, 20));
        maxProct.setFont(new Font("Times New Roman", Font.BOLD, 20));
        maxTimet.setFont(new Font("Times New Roman", Font.BOLD, 20));
        IncepereSimulare.setFont(new Font("Times New Roman", Font.BOLD, 20));

        clientNumbert.setPreferredSize(new Dimension(70, 40));
        serverNumbert.setPreferredSize(new Dimension(70, 40));
        minArrt.setPreferredSize(new Dimension(70, 40));
        maxArrt.setPreferredSize(new Dimension(70, 40));
        minProct.setPreferredSize(new Dimension(70, 40));
        maxProct.setPreferredSize(new Dimension(70, 40));
        maxTimet.setPreferredSize(new Dimension(70, 40));
        IncepereSimulare.setPreferredSize(new Dimension(200, 80));

        //Randurile unde adaug toate panelurile:
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setPreferredSize(new Dimension(1500, 750));
        c.setBackground(Color.blue);

        JPanel rand1 = new JPanel();
        rand1.setLayout(new FlowLayout());
        rand1.setBackground(Color.blue);

        JPanel rand2 = new JPanel();
        rand2.setLayout(new FlowLayout());
        rand2.setBackground(Color.blue);

        JPanel rand3 = new JPanel();
        rand3.setLayout(new FlowLayout());
        rand3.setBackground(Color.blue);

        JPanel rand4 = new JPanel();
        rand4.setLayout(new FlowLayout());
        rand4.setBackground(Color.blue);

        JPanel rand5 = new JPanel();
        rand5.setLayout(new FlowLayout());
        rand5.setBackground(Color.blue);

        //Aici adaug pe randuri:
        rand1.add(titlu);

        rand2.add(clientNumber);
        rand2.add(Box.createRigidArea(new Dimension(30, 0)));
        rand2.add(clientNumbert);
        rand2.add(Box.createRigidArea(new Dimension(30, 0)));
        rand2.add(serverNumber);
        rand2.add(Box.createRigidArea(new Dimension(30, 0)));
        rand2.add(serverNumbert);

        rand3.add(minArr);
        rand3.add(Box.createRigidArea(new Dimension(30, 0)));
        rand3.add(minArrt);
        rand3.add(Box.createRigidArea(new Dimension(30, 0)));
        rand3.add(maxArr);
        rand3.add(Box.createRigidArea(new Dimension(30, 0)));
        rand3.add(maxArrt);

        rand4.add(minProc);
        rand4.add(Box.createRigidArea(new Dimension(30, 0)));
        rand4.add(minProct);
        rand4.add(Box.createRigidArea(new Dimension(30, 0)));
        rand4.add(maxProc);
        rand4.add(Box.createRigidArea(new Dimension(30, 0)));
        rand4.add(maxProct);

        rand5.add(maxTime);
        rand5.add(Box.createRigidArea(new Dimension(30, 0)));
        rand5.add(maxTimet);
        rand5.add(Box.createRigidArea(new Dimension(30, 0)));
        rand5.add(IncepereSimulare);

        //Aici adaug toate randurile la produsul final:
        c.add(rand1);
        c.add(Box.createRigidArea(new Dimension(0, 50)));
        c.add(rand2);
        c.add(Box.createRigidArea(new Dimension(0, 50)));
        c.add(rand3);
        c.add(Box.createRigidArea(new Dimension(0, 50)));
        c.add(rand4);
        c.add(Box.createRigidArea(new Dimension(0, 50)));
        c.add(rand5);

        //Final view:
        this.setContentPane(c);
        this.pack();
        this.setTitle("Tema 2 Threads.");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Getteri pentru setare in SimulationManager;
    public String getclientNumber()
    {
        return clientNumbert.getText();
    }

    public String getserverNumber()
    {
        return serverNumbert.getText();
    }

    public String getminArr()
    {
        return minArrt.getText();
    }

    public String getmaxArr()
    {
        return maxArrt.getText();
    }

    public String getminProc()
    {
        return minProct.getText();
    }

    public String getmaxProc()
    {
        return maxProct.getText();
    }

    public String getmaxTime()
    {
        return maxTimet.getText();
    }

    //Pentru buton;
    void butonUnu(ActionListener be1)
    {
        IncepereSimulare.addActionListener(be1);
    }

    //View2:
    public void SimulareServere()
    {
        //Setez sa fie 0, si atunci cand intru prin buton vad ca sunt intrat si ca pot incepe threadul din SimulationManager
        TestButon = 0;

        //Analog aranjarea ca mai sus:
        titlu1.setFont(new Font("Times New Roman", Font.BOLD, 70));

        GenerareServereClienti.setPreferredSize(new Dimension(1400, 500));
        GenerareServereClienti.setFont(new Font("Times New Roman", Font.BOLD, 15));

        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setPreferredSize(new Dimension(1500, 750));
        c.setBackground(Color.green);

        JPanel rand1 = new JPanel();
        rand1.setLayout(new FlowLayout());
        rand1.setBackground(Color.green);

        JPanel rand2 = new JPanel();
        rand2.setLayout(new FlowLayout());
        rand2.setBackground(Color.green);

        rand1.add(titlu1);

        rand2.add(GenerareServereClienti);

        c.add(rand1);
        c.add(Box.createRigidArea(new Dimension(0, 20)));
        c.add(rand2);

        this.setContentPane(c);
        this.pack();
        this.setTitle("Simulare servere.");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Setter pentru text; Adaug la ce am adaugat deja, deci in continuare;
    public void setGenerareServereClienti(String ex)
    {
        GenerareServereClienti.append(ex);
    }

    //Pentru a reseta textul, dupa fiecare rulare de 1 secunda a threadurilor;
    public void resetGenerareServereClienti()
    {
        GenerareServereClienti.setText("");
    }

    //Controllerul unde descriu butonul;
    public void Controler()
    {
        butonUnu(new butonUnu1());
    }

    class butonUnu1 implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //Pentru a stii ca sunt intrat aici, dupa apasare de buton
            TestButon = 1;

            //Al doilea view devine cel vizibil acum
            SimulareServere();
        }
    }

    //Pentru a cunoaste daca am intrat sau nu pe buton;
    public int getTestButon() {
        return TestButon;
    }
}