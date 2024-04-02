import java.io.*;
import java.util.ArrayList;

public class LiftProgram {
    public static void main(String[] args) {
        LiftProgram l = new LiftProgram();
    }

    public LiftProgram() {
        File file = new File("LiftProgram.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String text;
            while ((text = reader.readLine()) != null) {
                String[] pieces = text.split(" ");
                int floors = Integer.parseInt(pieces[1]);
                ArrayList<ArrayList<Passenger>> q = new ArrayList<>();
                int totalppl = 0;

                for (int x = 0; x < floors; x++) {
                    text = reader.readLine();
                    ArrayList<Passenger> queue = new ArrayList<>();

                    if (!text.equals("")) {
                        String[] p = text.split(",");

                        for (int y = 0; y < p.length; y++) {
                            queue.add(new Passenger(x, Integer.parseInt(p[y])));
                            totalppl++;
                        }
                    }
                    q.add(queue);
                }
                text = reader.readLine();
                pieces = text.split(" ");
                int cap = Integer.parseInt(pieces[1]);

                Lift l = new Lift(q, cap, totalppl);
                l.liftOperation();

            }
        } catch (IOException e) {

        }
    }

    public class Lift {
        private ArrayList<ArrayList<Passenger>> q;
        private int capacity;
        private int totalppl;
        private int pplDroppedOff;
        private int currentFloor;
        private boolean liftGoingUp;
        private ArrayList<Passenger> elevator;
        private String visited;
        boolean floorStopped;

        public Lift(ArrayList<ArrayList<Passenger>> q, int c, int t) {
            this.q = q;
            this.capacity = c;
            this.totalppl = t;
            this.pplDroppedOff = 0;
            this.currentFloor = 0;
            this.liftGoingUp = true;
            this.elevator = new ArrayList<>();
            this.visited = "0";
            this.floorStopped = false;

        }

        public boolean liftWillGoUp(){
            return currentFloor == 0 || (liftGoingUp && currentFloor < q.size() - 1);
        }

        public boolean liftWillGoDown(){
            return currentFloor == q.size() - 1 || (!liftGoingUp && currentFloor > 0);
        }

        public void liftOperation() {

            while (pplDroppedOff < totalppl) {

                // check if passengers can get off elevator
                for (int x = 0; x < elevator.size(); x++) {
                    if (elevator.get(x).getGoalFloor() == currentFloor) {
                        System.out.println(elevator.get(x).getGoalFloor() + " got dropped off at floor "
                                + elevator.get(x).getGoalFloor());

                        elevator.remove(x);
                        x--;
                        pplDroppedOff++;
                        floorStopped = true;
                    }
                }

                // check if passengers can be added on to elevator
                for (int x = 0; x < q.get(currentFloor).size(); x++) {
                    if (q.get(currentFloor).get(x).isGoingUp() && liftWillGoUp()) {
                        floorStopped = true;
                        if(elevator.size() < capacity){
                            System.out.println(
                                q.get(currentFloor).get(x).getGoalFloor() + " got on at floor " + currentFloor);

                            elevator.add(q.get(currentFloor).remove(x));
                            x--;
                        }
                    } else if (q.get(currentFloor).get(x).isGoingDown() && liftWillGoDown()) {
                        floorStopped = true;
                        if(elevator.size() < capacity){
                            System.out.println(
                                q.get(currentFloor).get(x).getGoalFloor() + " got on at floor " + currentFloor);

                            elevator.add(q.get(currentFloor).remove(x));
                            x--;
                        }
                    }
                }

                // adding currentFloor to record of floors visited
                if(floorStopped){
                    if(!(visited.equals("0") && currentFloor == 0) && !(pplDroppedOff == totalppl && currentFloor == 0)){
                        visited += ", " + currentFloor;
                    }
                    floorStopped = false;
                }

                // if lift needs to go up
                if (liftWillGoUp()) {
                    currentFloor++;
                    liftGoingUp = true;
                }
                // if lift needs to go down
                else if (liftWillGoDown()) {
                    currentFloor--;
                    liftGoingUp = false;
                }

            }

            //bringing lift back to floor 0 at end of operation
            visited += ", 0";

            System.out.println(visited);

        }

    }

    public class Passenger {
        private int currentFloor;
        private int goalFloor;

        public Passenger(int currentFloor, int goalFloor) {
            this.currentFloor = currentFloor;
            this.goalFloor = goalFloor;

        }

        public int getCurrentFloor() {
            return currentFloor;
        }

        public int getGoalFloor() {
            return goalFloor;
        }

        public boolean isGoingUp() {
            return goalFloor > currentFloor;
        }

        public boolean isGoingDown() {
            return goalFloor < currentFloor;
        }
    }

}
