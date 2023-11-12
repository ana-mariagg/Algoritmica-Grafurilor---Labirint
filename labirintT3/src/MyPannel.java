
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MyPannel extends JPanel {
    int rows = 0;
    int columns = 0;
    int iPozNodStart;
    int jPozNodStart;
    Node start;
    private int[][] matrix;

    //ca sa putem retine poz si sa nu imi fac eu un pair
    //retin in int-ul din fiecare hashmap un numar de forma
    // ab unde a=i*10 si b=j gen 24=linia 2 coloana 4

    //hash1=pos+lista de adiacenta a nodului(vecinii)
    HashMap<Integer, Vector<Node>> listaNoduriGraf = new HashMap();

    //hash2=poz+nodul propriu-zis
    HashMap<Integer, Node> listaNoduriGrafFaraAdiacenta = new HashMap();

    public void citireMatriceLabirint() {
        try {
            Scanner input = new Scanner(new File(".\\src\\input.txt"));
            this.iPozNodStart = input.nextInt();
            this.jPozNodStart = input.nextInt();
            this.rows = input.nextInt();
            this.columns = input.nextInt();
            this.matrix = new int[this.rows][this.columns];

            while(input.hasNextInt()) {
                for(int i = 0; i < this.rows; ++i) {
                    for(int j = 0; j < this.columns; ++j) {
                        this.matrix[i][j] = input.nextInt();
                    }
                }
            }
        } catch (FileNotFoundException var4) {
            System.out.println("EORARE LA CITIREA DIN FISIER!");
        }

    }


    //verifica daca nodul curent este iesire
    public boolean isMarginal(int i, int j) {

        if( i == 0 || i == this.rows - 1 || j == 0 || j == this.columns - 1){
            return true;
        }
        else{
            return false;
        }
    }

    //transforma propriu zis din matrice in nod
    public void tranformareDinMatriceInNoduriSiIndexare() {

        //doar pentru nodul de start il transf
        boolean margine = this.isMarginal(this.iPozNodStart, this.jPozNodStart);
        this.start = new Node(this.iPozNodStart, this.jPozNodStart, margine);
        this.start.predecessor = null;

        for(int i = 0; i < this.rows; ++i) {
            for(int j = 0; j < this.columns; ++j) {
                //verific sa nu fie zid
                if (this.matrix[i][j] != 1) {
                    //daca nu e ne facem un vector de tip lista de adiacenta
                    //si cream nodul efectiv si il adaugam in hash2
                    Vector<Node> listaAdiacenta = new Vector();
                    boolean marginal = this.isMarginal(i, j);
                    Node nod = new Node(i, j, marginal);
                    this.listaNoduriGrafFaraAdiacenta.put(i * 10 + j, nod);
                    Node nodeGraf;

                    //verifica vecinii de sus, jos, stg si dr in parte
                    //si ii adauga in lista de adiacenta daca indeplinesc criteriile
                    if (i > 0 && this.matrix[i - 1][j] != 1) {
                        marginal = this.isMarginal(i - 1, j);
                        nodeGraf = new Node(i - 1, j, marginal);
                        listaAdiacenta.add(nodeGraf);
                    }

                    if (i < this.rows - 1 && this.matrix[i + 1][j] != 1) {
                        marginal = this.isMarginal(i + 1, j);
                        nodeGraf = new Node(i + 1, j, marginal);
                        listaAdiacenta.add(nodeGraf);
                    }

                    if (j > 0 && this.matrix[i][j - 1] != 1) {
                        marginal = this.isMarginal(i, j - 1);
                        nodeGraf = new Node(i, j - 1, marginal);
                        listaAdiacenta.add(nodeGraf);
                    }

                    if (j < this.columns - 1 && this.matrix[i][j + 1] != 1) {
                        marginal = this.isMarginal(i, j + 1);
                        nodeGraf = new Node(i, j + 1, marginal);
                        listaAdiacenta.add(nodeGraf);
                    }

                    //adaugam lista de adiacenta fara nodul verificat in primul
                    // hash ca sa cont vecinii
                    this.listaNoduriGraf.put(i * 10 + j, listaAdiacenta);
                }
            }
        }

        //stergem nodul sursa ca nu trebuie retinut pt ca ii stim deja poz
        this.listaNoduriGrafFaraAdiacenta.remove(this.iPozNodStart * 10 + this.jPozNodStart);
    }

    //parcurgerea in latime
    public void algoritmulPBF() {

        //new hash pentru ca sa nu avem probleme la modificat din cauza referintei
        HashMap<Integer, Node> listaNoduriNeVizitateU = new HashMap();

        //am iterat hash2 si le-am facut la toate predecesorul null
        Iterator var2;
        Map.Entry mapElement;
        for(var2 = this.listaNoduriGrafFaraAdiacenta.entrySet().iterator(); var2.hasNext(); ((Node)mapElement.getValue()).predecessor = null) {
            mapElement = (Map.Entry)var2.next();
        }

        var2 = this.listaNoduriGrafFaraAdiacenta.entrySet().iterator();

        //creaza predecesorul
        while(var2.hasNext()) {
            mapElement = (Map.Entry)var2.next();
            listaNoduriNeVizitateU.put((Integer)mapElement.getKey(), (Node)mapElement.getValue());
        }

        Queue<Node> listaNoduriVizitateV = new LinkedList();
        listaNoduriVizitateV.add(this.start);

        while(listaNoduriVizitateV.size() != 0) {
            Node nodulCelMaiVechi = (Node)listaNoduriVizitateV.element();

            for(int i = 0; i < ((Vector)this.listaNoduriGraf.get(nodulCelMaiVechi.getRow() * 10 + nodulCelMaiVechi.getColumn())).size(); ++i) {
                Node nodul = (Node)((Vector)this.listaNoduriGraf.get(nodulCelMaiVechi.getRow() * 10 + nodulCelMaiVechi.getColumn())).elementAt(i);
                if (listaNoduriNeVizitateU.containsKey(nodul.getRow() * 10 + nodul.getColumn())) {
                    listaNoduriNeVizitateU.remove(nodul.getRow() * 10 + nodul.getColumn());
                    nodul.predecessor = nodulCelMaiVechi;
                    ((Node)this.listaNoduriGrafFaraAdiacenta.get(nodul.getRow() * 10 + nodul.getColumn())).predecessor = nodulCelMaiVechi;
                    listaNoduriVizitateV.add(nodul);
                }
            }

            listaNoduriVizitateV.remove();
        }

    }

    public MyPannel() {
        this.citireMatriceLabirint();
        this.tranformareDinMatriceInNoduriSiIndexare();
        this.algoritmulPBF();
        this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    //creez drumurile
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int indexLinie = 0; indexLinie < this.matrix.length; ++indexLinie) {
            for(int indexColoana = 0; indexColoana < this.matrix[indexLinie].length; ++indexColoana) {
                Color var10000;
                switch (this.matrix[indexLinie][indexColoana]) {
                    case 0:
                        var10000 = Color.white;
                        break;
                    case 1:
                        var10000 = Color.black;
                        break;
                    //57427=START
                    case 57427:
                        var10000 = Color.blue;
                        break;
                    default:
                        var10000 = Color.white;
                }

                Color color = var10000;
                g.setColor(color);
                g.fillRect(70 * indexColoana, 70 * indexLinie, 70, 70);
                g.setColor(Color.black);
                g.drawRect(70 * indexColoana, 70 * indexLinie, 70, 70);
            }
        }

        Iterator var5 = this.listaNoduriGrafFaraAdiacenta.entrySet().iterator();

        while(true) {
            Map.Entry mapElement;
            Node nodeGraf;
            do {
                if (!var5.hasNext()) {
                    return;
                }

                mapElement = (Map.Entry)var5.next();
                nodeGraf = (Node)mapElement.getValue();
            } while(!((Node)mapElement.getValue()).getMarginal());

            while(nodeGraf.predecessor != null) {
                g.setColor(Color.red);
                g.drawLine(nodeGraf.getColumn() * 70 + 35, nodeGraf.getRow() * 70 + 35, nodeGraf.predecessor.getColumn() * 70 + 35, nodeGraf.predecessor.getRow() * 70 + 35);
                nodeGraf = nodeGraf.predecessor;
            }
        }
    }
}
