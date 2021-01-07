


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Collections;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class EightPuzzle {
    private char[][] arr = new char[3][3];
    private String[] moves;
    private Node endNode;
    private int maxNodes;
    private int numMoves;
    private int seed1 = 1;
    private int seed2 = 2;
    private int seed3 = 3;
    private int seed4 = 4;
    private int seed5 = 5;

    public class Node implements Comparable<Node> {
        private char[][] currState = new char[3][3];
        private int evalFunct;
        private int h;
        private int g;
        private int f;
        private String direction;
        private Node parent;
        private ArrayList<Node> childList = new ArrayList<>();
        private ArrayList<Node> randStates = new ArrayList<>();

        public Node(char[][] currState, int g, Node parent, String direction) {
            this.currState = currState;
            this.g = g;
            this.parent = parent;
            this.direction = direction;
        }

        

        /* sets h(n) value for given state in parameter */
        public void setH(char[][] arr, String heuristic) {
            char[][] temp = new char[3][3];
            setState("b12 345 678", temp);
            // heuristic 1 where you find the number of misplaced tiles
            if (heuristic.equals("h1")) {
                int h1 = 9;
                // loop through puzzle
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        // skip the blank spot (space)
                        if (i == 0 && j == 0) {
                            j++;
                        }
                        // tile is in correct position
                        if (arr[i][j] == temp[i][j]) {
                            h1 = h1 - 1;
                        }
                    }
                }
                this.h = h1;
            } else {
                int bI = 0;
                int bJ = 0;
                int oneI = 0;
                int oneJ = 0;
                int twoI = 0;
                int twoJ = 0;
                int threeI = 0;
                int threeJ = 0;
                int fourI = 0;
                int fourJ = 0;
                int fiveI = 0;
                int fiveJ = 0;
                int sixI = 0;
                int sixJ = 0;
                int sevenI = 0;
                int sevenJ = 0;
                int eightI = 0;
                int eightJ = 0;
                // loop through puzzle
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (arr[i][j] == 'b') {
                            bI = i;
                            bJ = j;
                        }
                        if (arr[i][j] == '1') {
                            oneI = i;
                            oneJ = j;
                        }
                        if (arr[i][j] == '2') {
                            twoI = i;
                            twoJ = j;
                        }
                        if (arr[i][j] == '3') {
                            threeJ = j;
                            threeI = i;
                        }
                        if (arr[i][j] == '4') {
                            fourJ = j;
                            fourI = i;
                        }
                        if (arr[i][j] == '5') {
                            fiveJ = j;
                            fiveI = i;
                        }
                        if (arr[i][j] == '6') {
                            sixJ = j;
                            sixI = i;
                        }
                        if (arr[i][j] == '7') {
                            sevenJ = j;
                            sevenI = i;
                        }
                        if (arr[i][j] == '8') {
                            eightJ = j;
                            eightI = i;
                        }
                    }
                }
                int b = bI + bJ;
                int one = oneI + Math.abs(oneJ - 1);
                int two = twoI + Math.abs(twoJ - 2);
                int three = Math.abs(threeI -1) + threeJ;
                int four = Math.abs(fourI - 1) + Math.abs(fourJ - 1);
                int five = Math.abs(fiveI - 1) + Math.abs(fiveJ - 2);
                int six = Math.abs(sixI - 2) + sixJ;
                int seven = Math.abs(sevenI - 2) + Math.abs(sevenJ -1);
                int eight = Math.abs(eightI - 2) + Math.abs(eightJ -2);
                this.h = b+one+two+three+four+five+six+seven+eight;
            }
        }

            // evaluation function to base beam search off of
    // for this beam search, the evaluation function is equal to h1
    public void setEvalFunct() {
        Node a = new Node(this.getState(), 0, null, "");
        a.setH(this.getState(), "h2");
        int a1 = a.getH();
        Node b = new Node(this.getState(), 0, null, "");
        b.setH(this.getState(), "h1");
        int b1 = b.getH();
        this.evalFunct = a1 + b1;
    }

    public int getEvalFunct(){
        return this.evalFunct;
    }

        @Override
        public int compareTo(Node node) {
            if (this.getEvalFunct() < node.getEvalFunct()) {
                return -1;
            } else if (this.getEvalFunct() == node.getEvalFunct()) {
                return 0;
            } else if (this.getEvalFunct() > node.getEvalFunct()) {
                return 1;
            } else {
                return -1;
            }
        }

        public int getH() {
            return this.h;
        }

        public void setF() {
            this.f = getG() + getH();
        }

        public Node getNode() {
            return this;
        }

        /*
         * generates children of Node with given heuristic type and puts them onto a
         * list
         */
        public ArrayList<Node> generateChildren(String heuristic) {
            char[][] state1 = new char[3][3];
            char[][] state2 = new char[3][3];
            char[][] state3 = new char[3][3];
            char[][] state4 = new char[3][3];

            for (int k = 0; k < 3; k++) {
                for (int j = 0; j < 3; j++) {
                    state1[k][j] = this.getState()[k][j];
                    state2[k][j] = this.getState()[k][j];
                    state3[k][j] = this.getState()[k][j];
                    state4[k][j] = this.getState()[k][j];
                }
            }

            for (int i = 0; i < 4; i++) {
                // left child state
                if (i == 0 && movePossible(state1, "left")) {
                    // make move
                    move("left", state1);
                    // create new node
                    Node left = new Node(state1, this.getG() + 1, this, "left");
                    // set h(n) and f(n)
                    left.setH(state1, heuristic);
                    left.setF();
                    left.setEvalFunct();
                    childList.add(left);
                }
                // same thing for right child
                if (i == 1 && movePossible(state2, "right")) {
                    move("right", state2);
                    Node right = new Node(state2, this.getG() + 1, this, "right");
                    right.setH(state2, heuristic);
                    right.setF();
                    right.setEvalFunct();
                    childList.add(right);
                }
                // same thing for up child
                if (i == 2 && movePossible(state3, "up")) {
                    move("up", state3);
                    Node up = new Node(state3, this.getG() + 1, this, "up");
                    up.setH(state3, heuristic);
                    up.setF();
                    up.setEvalFunct();
                    childList.add(up);
                }
                // same thing for down child
                if (i == 3 && movePossible(state4, "down")) {
                    move("down", state4);
                    Node down = new Node(state4, this.getG() + 1, this, "down");
                    down.setH(state4, heuristic);
                    down.setF();
                    down.setEvalFunct();
                    childList.add(down);
                }
            }
            return childList;
        }

        // randomizes the start state of beam search
        public ArrayList<Node> randState(int k, int seed) {
            Random rand = new Random(seed);
            int j = 0;
            generateChildren("h1");
            randStates = getChildrenList();
            int startSize = randStates.size();
            // if there are less children than given k states
            if (randStates.size() < k) {
                k = randStates.size();
            }
            // remove excess elements from list size so you only have k elements left
            while (j < (startSize - k)) {
                int rand1 = rand.nextInt(randStates.size());
                // remove item at the random index
                randStates.remove(rand1);
                j++;
            }
            return randStates;
        }

        // get the list of randomized generated states
        public ArrayList<Node> getRandStates() {
            return this.randStates;
        }

        // gets the list containing all children of the node
        public ArrayList<Node> getChildrenList() {
            return this.childList;
        }

        // gets direction that the node moved
        public String getDirection() {
            return this.direction;
        }

        // get g(n) value
        public int getG() {
            return this.g;
        }

        // get parent node
        public Node getParent() {
            return this.parent;
        }

        // set the parent node
        public void setParent(Node parent) {
            this.parent = parent;
        }

        // get the puzzle state of the node
        public char[][] getState() {
            return this.currState;
        }

        // get the f(n) value
        public int getF() {
            return this.f;
        }

    }

    /* class to sort nodes according to evaluation function for beamsearch */
    public class NodeSorter {
        ArrayList<Node> list = new ArrayList<>();

        public NodeSorter(ArrayList<Node> list) {
            this.list = list;
        }

        public ArrayList<Node> getSortedNodes() {
            Collections.sort(list);
            return list;
        }
    }


    public class OpenListQueue implements Comparator<Node>{
        public int compare(Node node1, Node node2){
            if(node1.getF() < node2.getF()){
                return -1;
            }
            else if(node1.getF() > node2.getF()){
                return 1;
            }
            return 0;
        }
    
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
       try {
            //assign input file to arguments
            if(args.length != 0){
            String inputFile = args[0];
            EightPuzzle puzzle = new EightPuzzle();
            File file = new File(inputFile);
            Scanner scan = new Scanner(file);
            ArrayList<String> commands = new ArrayList<>();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                commands.add(line);
            }
            System.out.print(commands.toString());
            for (int i = 0; i < commands.size(); i++) {
                if (commands.get(i).equals("setState")) {
                    puzzle.setState(commands.get(i+1), puzzle.arr);
                }
                else if(commands.get(i).equals("maxNodes")){
                    puzzle.maxNodes(Integer.parseInt(commands.get(i+1)));
                    System.out.print("Max nodes: "+puzzle.maxNodes+"\n");
                }
                else if(commands.get(i).equals("randomizeState")){
                    puzzle.randomizeState(Integer.parseInt(commands.get(i+1)), puzzle.seed1, puzzle.arr);
                    System.out.println("\nrandomizing "+commands.get(i+1)+" times\n");
                }
                else if(commands.get(i).equals("Start State:")){
                    System.out.println("\n\nStart State: \n" + puzzle.printState(puzzle.arr));
                }
                else if(commands.get(i).equals("Solving A* search with h1 heuristic:")){
                    System.out.println("\n\nSolving A* search with h1 heuristic: \n");
                }
                else if(commands.get(i).equals("Solving A* search with h2 heuristic:")){
                    System.out.println("\nSolving A* search with h2 heuristic: \n");
                }
                else if(commands.get(i).equals("solveAStar")){
                    puzzle.solveAStar(commands.get(i+1));
                }
                else if(commands.get(i).equals("Print State:")){
                    System.out.println("\nPrint State: \n" + puzzle.printState(puzzle.arr));
                }
                else if(commands.get(i).equals("Solving beam search with 5 states:")){
                    System.out.println("\nSolving beam search with 5 states:\n");
                }
                else if(commands.get(i).equals("solveBeam")){
                    puzzle.solveBeam(Integer.parseInt(commands.get(i+1)), puzzle.seed2);
                }
                else if(commands.get(i).equals("Solving beam search with 10 states:")){
                    System.out.println("\nSolving beam search with 10 states:\n");
                }
                else if(commands.get(i).equals("move")){
                    puzzle.move(commands.get(i+1), puzzle.arr);
                    System.out.println("move "+commands.get(i+1));
                }
            }
        }
        /*EightPuzzle experiment = new EightPuzzle(); //for testing the experiments
        System.out.print("fractional successes: \n");
        experiment.solveablePuzzlesH1();
        experiment.solveablePuzzlesH2();
        experiment.solveablePuzzlesBeam();
        experiment.solveAble();*/ 
    } 
        catch(FileNotFoundException e){
            System.out.println("file not found");
        }
    }

    // sets state of given array based off of given string
    public void setState(String state, char[][] arr) {
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = state.charAt(index);
                index++;
            }
            index++;
        }
    }

    // prints out state in String
    public String printState(char[][] arr) {
        StringBuilder builder = new StringBuilder();
        // loop through array
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                builder.append(arr[i][j]);
                if (j != arr[i].length - 1) {
                    builder.append(" ");
                }
            }
            builder.append("\n");
        }
        return (builder.toString());
    }

    // executes moving the blank space in specified direction
    public boolean move(String move, char[][] arr) {
        String move1 = move.toLowerCase();
        // find row and column where b is located
        int bRow = this.bRow(arr);
        int bCol = this.bCol(arr);

        // if left move is possible
        if (move1.equals("left") && (bCol == 1 || bCol == 2)) {
            // swap the elements
            char toLeft = arr[bRow][bCol - 1];
            arr[bRow][bCol - 1] = arr[bRow][bCol];
            arr[bRow][bCol] = toLeft;
        }
        // if right move is possible
        else if (move1.equals("right") && (bCol == 0 || bCol == 1)) {
            // swap the elements
            char b = arr[bRow][bCol];
            arr[bRow][bCol] = arr[bRow][bCol + 1];
            arr[bRow][bCol + 1] = b; // swap with right tile
        }
        // if up move is possible
        else if (move1.equals("up") && (bRow == 1 || bRow == 2)) {
            // swap the elements
            char onTop = arr[bRow - 1][bCol];
            arr[bRow - 1][bCol] = arr[bRow][bCol];
            arr[bRow][bCol] = onTop; // swap with top tile
        }
        // if down move is possible
        else if (move1.equals("down") && (bRow == 0 || bRow == 1)) {
            // swap the elements
            char b = arr[bRow][bCol];
            arr[bRow][bCol] = arr[bRow + 1][bCol];
            arr[bRow + 1][bCol] = b;
        } else {
            // move impossible
            return false;
        }
        return true;
    }

    // randomize the state n number of times
    public void randomizeState(int n, int seed, char[][] state) {
        StringBuilder builder = new StringBuilder();
        // generate random numbers
        Random rand = new Random(seed);
        // for n times, apply whichever random move the integer corresponds to
        for (int i = 0; i < n; i++) {
            int rand1 = rand.nextInt(4);
            if (rand1 == 0) {
                move("left", state);
                builder.append("left\n");
            }
            if (rand1 == 1) {
                move("right", state);
                builder.append("right\n");
            }
            if (rand1 == 2) {
                move("up", state);
                builder.append("up\n");
            }
            if (rand1 == 3) {
                move("down", state);
                builder.append("down\n");
            }
        }
    }

    // solves A* search with given heuristic parameter type
    public boolean solveAStar(String heuristic) {
        PriorityQueue<Node> open = new PriorityQueue<>();
        ArrayList<Node> closed = new ArrayList<>();
        char[][] currState = this.arr;
        char[][] goalState = new char[3][3];
        this.setGoalState(goalState);
        numMoves = 0;
        int nodesTraversed = 1;
        StringBuilder builder = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();

        // create a starting node at the current state
        Node node = new Node(currState, 0, null, "");
        node.setH(node.getState(), heuristic);
        node.setF();
        // add it to the open list
        open.add(node);

       // System.out.print("start state: \n"+printState(node.getState())+"\n");
        while (!open.isEmpty()) { // while there are nodes to traverse
            //System.out.print("Min state: \n"+printState(open.peek().getState())+"\n");
            Node minNode = open.peek();
            closed.add(open.peek());
            open.poll();
            if(Arrays.deepEquals(minNode.getState(), goalState)){
                numMoves = minNode.getG(); // gets you the number of moves it took to get to the goal node
                this.endNode = minNode; // set node as the goal so you can retrace the path
                open.clear();
            }
            else{
                minNode.generateChildren(heuristic); // sets f(n), g(n), h(n), state, and direction of children, and
                // puts the nodes onto a list
            }
            //loop through all child states
            for (int i = 0; i < minNode.getChildrenList().size(); i++) {
                minNode.getChildrenList().get(i).setParent(minNode);
                Node child = minNode.getChildrenList().get(i);

                //if state is already on the frontier
             /*   if(stateOnQueue(open, child.getState())){
                //loop until you get to that state
                PriorityQueue<Node> copy = new PriorityQueue<>(open);
                //poll lowest min val until you get to the common state as the head
                while(!copy.isEmpty() && !Arrays.deepEquals(copy.peek().getState(), child.getState())){
                    copy.poll();
                }
                //if child has a lower path cost
                //remove from frontier
                if(copy.peek() != null && copy.peek().getF() > child.getF()){
                    Node remove = copy.peek();
                    open.remove(remove);
                }
                }*/
                //add child node
                    //either it's a new state 
                    //or it replaces the removed duplicate state
                if(!stateOnList(closed, child.getState())){
                    open.add(child);
                }
            }
          /*  PriorityQueue<Node> a = new PriorityQueue<>(open);
            Iterator itr = a.iterator();
            System.out.print("start of queue\n");
            while(itr.hasNext()){
                System.out.print("fn val: "+a.peek().getF()+"\ngn val: "
                                                            +a.peek().getG()+"\nh2 val: "
                                                            +a.peek().getH()+"\nstate: \n"+printState(a.peek().getState())+"\n");
                a.poll();
            }
            System.out.print("queue done\n\n\n\n");*/
            if (nodesTraversed > this.maxNodes) {
                builder2.append("ERROR: More than maximum number of nodes (" + maxNodes
                        + ") have been considered during search.\n");
                System.out.print(builder2.toString());
                return false;
            }
        }
            builder.append("Number of tile moves needed to obtain solution: " + numMoves + "\n");
            builder.append("Sequence of moves: " + getMoveSequence());
            // apply moves to actual board
            doMoves(this.arr);
            System.out.println(builder.toString() + "\n");
            return true;
    }

    // sets max number of nodes considered during a search
    public void maxNodes(int n) {
        this.maxNodes = n;
    }


    // solve beam search with k parameter number of states generated each time
    public boolean solveBeam(int k, int seedRandStates) {
        ArrayList<Node> parentList;
        ArrayList<Node> successors = new ArrayList<>();
        char[][] currState = this.arr;
        char[][] goalState = new char[3][3];
        this.setGoalState(goalState);
        numMoves = 0;
        StringBuilder builder = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();

        // create a node to start at the current state
        Node node = new Node(currState, 0, null, "");
        this.endNode = node;
        // randomize which children of node are picked to start with, i.e. k randomly
        // generated states
        node.randState(k, seedRandStates);
        parentList = node.getRandStates(); // beginning list with all these states
        int nodesTraversed = parentList.size();
        // while the goal state has not been generated
        while (stateOnList(parentList, goalState) != true) {
            // otherwise generate successors for all states on list
            int indexL = parentList.size();
            // loop through the already generated states
            for (int l = 0; l < indexL; l++) {
                Node currNode = parentList.get(l);
                currNode.generateChildren("h1");
                int indexJ = currNode.getChildrenList().size();
                // go through the already generated list and add their successors to list of
                // successors (in the next round of generation)
                for (int j = 0; j < indexJ; j++) {
                    currNode.getChildrenList().get(j).setParent(currNode);
                    // add to array list of successors
                    successors.add(currNode.getChildrenList().get(j));
                }
            }
            parentList.clear(); // erase previous states
            parentList = duplicateList(successors); // add the list of successors to the parent list, now the list of
                                                    // generated successors
            successors.clear(); // erase successor states after they have been transferred to parentList
            // sort the nodes from lowest evaluation function value to highest
            NodeSorter list = new NodeSorter(parentList);
            ArrayList<Node> sortedList = list.getSortedNodes();
            parentList = sortedList;
            // only the best successors are generated, the first k states
            setBestSuccessors(parentList, k);
            if (nodesTraversed > this.maxNodes) {
                builder2.append("ERROR: More than maximum number of nodes (" + maxNodes
                        + ") have been considered during search.\n");
                System.out.print(builder2.toString());
                return false;
                    
            }
        }
        // goal state has been generated
        int indexI = parentList.size();
        for (int i = 0; i < indexI; i++) {
            // loop through to find it
            if (Arrays.deepEquals(parentList.get(i).getState(), goalState)) {
                numMoves = parentList.get(i).getG();
                this.endNode = parentList.get(i);
            }
        }
            builder.append("Number of tile moves needed to obtain solution: " + numMoves + "\n");
            builder.append("Sequence of moves: " + getMoveSequence());
            // apply moves to actual board
            doMoves(this.arr);
            System.out.println(builder.toString() + "\n");
            return true;
    }

    /*
     * helper methods below
     *
     */
    // checks to see if move is possible without executing move
    public boolean movePossible(char[][] arr, String direction) {
        int bRow = this.bRow(arr);
        int bCol = this.bCol(arr);
        // left move possible
        if (direction.equals("left") && (bCol == 1 || bCol == 2)) {
            return true;
        }
        // right move possible
        else if (direction.equals("right") && (bCol == 0 || bCol == 1)) {
            return true;
        }
        // up move possible
        else if (direction.equals("up") && (bRow == 1 || bRow == 2)) {
            return true;
        }
        // down move possible
        else if (direction.equals("down") && (bRow == 0 || bRow == 1)) {
            return true;
        } else {
            return false;
        }
    }

    // inputs the goal state into given arr
    public void setGoalState(char[][] arr) {
        String state = "b12345678";
        int k = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = state.charAt(k);
                k++;
            }
        }
    }

    // finds whether or not the given state is listed on the given arraylist
    public boolean stateOnList(ArrayList<Node> list, char[][] state) {
        for (int i = 0; i < list.size(); i++) {
            if (Arrays.deepEquals(list.get(i).getState(), state)) {
                return true;
            }
        }
        return false;
    }

    public boolean stateOnQueue(PriorityQueue<Node> list, char[][] state){
        PriorityQueue<Node> copy = new PriorityQueue<>(list);
        Iterator itr = copy.iterator();
        while(itr.hasNext()){
            if(Arrays.deepEquals(copy.peek().getState(), state)){
                return true;
            }
            copy.poll();
        }
        return false;
    }



    // lists the sequence of moves from parent node to current node
    public String getMoveSequence() {
        StringBuilder builder = new StringBuilder();
        moves = new String[numMoves];
        Node ndptr = endNode;
        for (int j = numMoves - 1; j > -1; j--) {
            moves[j] = ndptr.getDirection();
            ndptr = ndptr.getParent();
        }
        for (int k = 0; k < moves.length; k++) {
            builder.append(moves[k]);
            if (k != moves.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    // implements the moves listed within given array onto the global variable
    // puzzle array
    public void doMoves(char[][] arr) {
        for (int i = 0; i < moves.length; i++) {
            if (moves[i].equals("left")) {
                move("left", arr);
            } else if (moves[i].equals("right")) {
                move("right", arr);
            } else if (moves[i].equals("up")) {
                move("up", arr);
            } else if (moves[i].equals("down")) {
                move("down", arr);
            }
        }
    }

    // finds the index at where the minimum f(n) node is
    public int findMinFIndex(ArrayList<Node> open) {
        int minValIndex = 0;
        for (int i = 1; i < open.size(); i++) {
            if (open.get(minValIndex).getF() > open.get(i).getF()) {
                minValIndex = i;
            }
        }
        return minValIndex;
    }

    // finds the row value of the blank space
    public int bRow(char[][] arr) {
        int bRow = -1;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] == 'b') {
                    bRow = i;
                }
            }
        }
        return bRow;
    }

    // find the column value of the blank space
    public int bCol(char[][] arr) {
        int bCol = -1;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] == 'b') {
                    bCol = j;
                }
            }
        }
        return bCol;
    }

    // gets row of specified element in specified array
    public int getRowOf(char[][] arr, char a) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == a) {
                    return i;
                }
            }
        }
        return -1;
    }

    // get column of specified element in specified array
    public int getColOf(char[][] arr, char a) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == a) {
                    return j;
                }
            }
        }
        return -1;
    }

    // provides a duplicate of the given arraylist so that copying over can take
    // place
    public ArrayList<Node> duplicateList(ArrayList<Node> list) {
        ArrayList<Node> duplicate = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            duplicate.add(list.get(i));
        }
        return duplicate;
    }

    // sets the list of best successors, removing all values after the allotted k
    // states
    public void setBestSuccessors(ArrayList<Node> list, int k) {
        int i = k;
        while (i < list.size()) {
            list.remove(i);
        }
    }


   public void solveablePuzzlesH1(){
       Random rand = new Random();
        int successH1 = 0;
        this.maxNodes(0);
        
    for(int j = 0; j < 6; j++){
        for(int i = 0; i < 15; i++){
            this.setState("b12 345 678", arr);
            this.randomizeState(rand.nextInt(25), this.seed4, arr);
            if(solveAStar("h1") != false){
                successH1 = successH1 + 1;
            }
        }
        System.out.print("for max nodes: "+maxNodes+"\n");
        System.out.print("Fraction of solveable puzzles for A* h1: "+((double)successH1/15)+"\n");
        successH1 = 0;
        this.maxNodes = this.maxNodes + 2;
     }
    }
   

   public void solveablePuzzlesH2(){
    Random rand = new Random();
     int successH2 = 0;
     this.maxNodes(0);
     
 for(int j = 0; j < 6; j++){
     for(int i = 0; i < 15; i++){
         this.setState("b12 345 678", arr);
         this.randomizeState(rand.nextInt(25), this.seed4, arr);
         if(solveAStar("h2") != false){
             successH2 = successH2 + 1;
         }
     }
     System.out.print("for max nodes: "+maxNodes+"\n");
     System.out.print("Fraction of solveable puzzles for A* h2: "+((double)successH2/15)+"\n");
     successH2 = 0;
     this.maxNodes = this.maxNodes + 2;
  }
}


public void solveablePuzzlesBeam(){
    Random rand = new Random();
     int successBeam = 0;
     this.maxNodes(0);
     
 for(int j = 0; j < 7; j++){
     for(int k = 0; k < 15; k++){
         this.setState("b12 345 678", arr);
         this.randomizeState(rand.nextInt(25), this.seed4, arr);
         if(solveBeam(5, this.seed3) != false){
         successBeam = successBeam + 1;
         }
        }
     System.out.print("for max nodes: "+maxNodes+"\n");
     System.out.print("Fraction of solveable puzzles for beam search: "+((double)successBeam/15)+"\n");
     successBeam = 0;
     this.maxNodes = this.maxNodes + 2;
  }
 }

 public void solveAble(){
     Random rand = new Random();
     int successH1 = 0;
     int successH2 = 0;
     int successBeam = 0;
     this.maxNodes(3);

     for(int i = 0; i < 20; i++){
        this.setState("b12 345 678", arr);
        this.randomizeState(rand.nextInt(25), this.seed4, arr);
        if(solveBeam(5, this.seed3) != false){
            successBeam = successBeam + 1;
        }
        this.setState("b12 345 678", arr);
        this.randomizeState(rand.nextInt(25), this.seed4, arr);
        if(solveAStar("h2") != false){
            successH2 = successH2 + 1;
        }
        this.setState("b12 345 678", arr);
        this.randomizeState(rand.nextInt(25), this.seed4, arr);
        if(solveAStar("h1") != false){
            successH1 = successH1 + 1;
        }
     }
     System.out.print("Fraction of solveable puzzles for beam search: "+((double)successBeam/20)+"\n");
     System.out.print("Fraction of solveable puzzles for A* h2: "+((double)successH2/20)+"\n");
     System.out.print("Fraction of solveable puzzles for A* h1: "+((double)successH1/20)+"\n");
 }
}
