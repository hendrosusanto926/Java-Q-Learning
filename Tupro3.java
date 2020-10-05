import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Tupro3 {
    static int north = 0;
    static int south = 1;
    static int west = 2;
    static int east = 3;
    static boolean isGoal(int a, int b){
        return (a==0) && (b==14);
    }
    static double maxQ(double[][] Q, int a){
        return Math.max(Q[a][north], Math.max(Q[a][south], Math.max(Q[a][west], Q[a][east])));
    }
    static int indexMax(double[][] Q, int a){
        if ((Q[a][north]+Math.max(Q[a-15][north], Q[a-15][east]))>=(Q[a][east]+Math.max(Q[a+1][north], Q[a+1][east])))
            return north;
        else
            return east;
    }
    static void inputstate(int a, int[] state){
        int b=0;
        for (int i = 0; i < state.length; i++) {
            if (state[i]==-1){
                b=i;
                i=state.length;
            }
        }state[b]=a;
    }
    static void nextstate(int a, double[][] Q,int[] state){
            inputstate(a,state);
            if (a!=14){
                int b = indexMax(Q,a);
                if (b==north) a=a-15;
                else a=a+1;
                nextstate(a,Q,state);
            }
    }
    static void episode(int a, int b, double learning_rate, double discount_rate, double[][] Q, String[][] data){
        while (!isGoal(a,b)){
            Random rand = new Random();
            int r = rand.nextInt(4);
            if (r==north){
                if (a!=0){
                    Q[(15*a)+b][north] = Double.parseDouble(data[a][b]) + 
                                       learning_rate*(Double.parseDouble(data[a-1][b])+
                                       discount_rate*maxQ(Q,((15*(a-1))+b))-Double.parseDouble(data[a][b]));
                    a--;
                }
            }
            else if (r==south){
                if (a!=14){
                    Q[(15*a)+b][south] = Double.parseDouble(data[a][b]) + 
                                       learning_rate*(Double.parseDouble(data[a+1][b])+
                                       discount_rate*maxQ(Q,((15*(a+1))+b))-Double.parseDouble(data[a][b]));
                    a++;
                }
            }
            else if (r==west){
                if (b!=0){
                    Q[(15*a)+b][west] = Double.parseDouble(data[a][b]) + 
                                       learning_rate*(Double.parseDouble(data[a][b-1])+
                                       discount_rate*maxQ(Q,((15*a)+b-1))-Double.parseDouble(data[a][b]));
                    b--;
                }
            }
            else{
                if (b!=14){
                    Q[(15*a)+b][east] = Double.parseDouble(data[a][b]) + 
                                       learning_rate*(Double.parseDouble(data[a][b+1])+
                                       discount_rate*maxQ(Q,((15*a)+b+1))-Double.parseDouble(data[a][b]));
                    b++;
                }
            }
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        int[] state = new int[29];
        double learning_rate = 0.1;
        double discount_rate = 0.8;
        String[][] data = new String[15][15];
        double[][] Q = new double[225][4];
        File file= new File("DataTugas3ML2019.txt");
        Scanner scan= new Scanner(file);
        int i =0;
        while (scan.hasNext()){
            data[i]=scan.nextLine().split("\t");
            i++;
        }
        for (int j = 0; j < state.length; j++) {
            state[j]=-1;
        }
        for (int j = 0; j < Q.length; j++) {
            for (int k = 0; k < Q[j].length; k++) {
                Q[j][k]=-10;
            }
        }
        int n = 0;
        while (n<1000){
            int a = 14; int b = 0;
            episode(a,b,learning_rate,discount_rate,Q,data);
            System.out.println("episode "+(n+1)+" done");
            n++;
        }
        nextstate(210,Q,state);
        int total_reward = 0;
        for (int j = 0; j < state.length; j++) {
                System.out.println("indeks : "+"["+(state[j]/15)+"]"+"["+(state[j]%15)+"]"+
                " ->  Reward : "+Integer.parseInt(data[state[j]/15][state[j]%15]));
                total_reward = total_reward + Integer.parseInt(data[state[j]/15][state[j]%15]);
        }
        System.out.println("                             ----- +");
        System.out.println("      Total Reward     :      "+total_reward);
    }
}
