import javax.swing.*;
//constructor
public class Graf {
    public Graf(){
    }

    private static void initializare(){
        JFrame f=new JFrame ("Algoritmica grafurilor");
        f.setDefaultCloseOperation(3);
        f.add(new MyPannel());
        f.setSize(1900,1080);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Graf.initializare();
            }
        });
    }
}
