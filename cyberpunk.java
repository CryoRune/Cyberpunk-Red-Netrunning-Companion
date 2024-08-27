import java.util.ArrayList;
import java.io.*;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;

class Netrunning {
    
    static int netSize = 9;
    static int currentFloor = 0;
    static int displayFloor = currentFloor+1;
    static int[] maxNet = {2, 2, 2, 3, 3, 3, 4, 4, 4, 5};
    static int currentNet = 0;
    static int interfaceLvl = 0;
    static int[] interfaceNums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    static boolean[] slide = {false, false};
    static boolean cloaked = false;
    static boolean pathfinder = false;
    static boolean passKnown = false;
    static ArrayList<program> floorPrograms = new ArrayList<program>();

    public static void main(String[] args) throws Exception {
        
        // Imports list of all programs from file
        Queue<String> programData = csvInput("programs.csv");
        ArrayList<program> allPrograms = new ArrayList<program>();
        String[] temp = {"0", "0", "0", "0", "0", "0", "0"};
        int j = 0;
        while(!programData.isEmpty()) {
            
            temp[j] = programData.poll();
            j++;
            if(j == 7) {
                
                j = 0;
                program p = new program(temp[0], temp[1], Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), 
                Integer.parseInt(temp[5]), Integer.parseInt(temp[6]));
                allPrograms.add(p);
            }
        }

        // Creates net and base information
        JFrame init = new JFrame();
        ArrayList<ArrayList<program>> netArchitecture = generateNet(netSize);
        String[] allTypes = netArchitecture.get(0).get(0).getAllTypes();
        JLabel l = new JLabel("Choose Interface Level: ");
        netrunner n = new netrunner(interfaceLvl);
        init.setSize(500, 600);
        init.setLayout(new FlowLayout());
        init.add(l);
        
        // Sets up GUI with inital settings
        JFrame frame = new JFrame();
        JLabel floor = new JLabel("Floor " + (displayFloor) + ": " + netArchitecture.get(currentFloor));
        JLabel actions = new JLabel("Net Actions Remaining: " + currentNet);
        JLabel info = new JLabel("");
        JButton bDown = new JButton("Venture Deeper Down");
        JButton bUp = new JButton("Retreat Up");
        JButton bEnd = new JButton("End Turn");
        JButton bBackdoor = new JButton("Backdoor");
        JButton bCloak = new JButton("Cloak");
        JButton bControl = new JButton("Control");
        JButton bEyeDee = new JButton("Eye-Dee");
        JButton bPathfinder = new JButton("Pathfinder");
        JButton bSlide = new JButton("Slide");
        JButton bVirus = new JButton("Virus");
        JButton bZap = new JButton("Zap");
        JButton bPass = new JButton("Password: Unknown");
        // bUp.setBounds(150, 200, 220, 50);
        
        // Includes everything in the frame
        frame.add(floor);
        frame.add(actions);
        frame.add(bUp);
        frame.add(bDown);
        frame.add(bEnd);
        frame.add(bDown);
        frame.add(bUp);
        frame.add(bEnd);
        frame.add(bBackdoor);
        frame.add(bCloak);
        frame.add(bControl);
        frame.add(bEyeDee);
        frame.add(bPathfinder);
        frame.add(bSlide);
        frame.add(bVirus);
        frame.add(bZap);
        frame.add(bPass);
        frame.add(info);
        frame.setSize(500, 600);
        frame.setLayout(new FlowLayout());
        
        // Allows user to choose their Interface Level
        for(int i = 1; i <= 10; i++) {

            JButton bSubmit = new JButton("" + i);
            init.add(bSubmit);
            bSubmit.addActionListener(new ActionListener() {
            
                public void actionPerformed(ActionEvent e) {

                    interfaceLvl = interfaceNums[Integer.parseInt(bSubmit.getText())-1];
                    currentNet = maxNet[interfaceLvl-1];
                    n.setLvl(interfaceLvl);
                    actions.setText("Net Actions Remaining: " + currentNet);
                    init.setVisible(false);
                    frame.setVisible(true);
                }
            });
        }
        init.setVisible(true);

        // Defines the actions for every button
        bDown.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                if(currentNet != 0 && currentFloor != netArchitecture.size()-1) {

                    ArrayList<program> p = netArchitecture.get(currentFloor);
                    ArrayList<String> pType = getType(p.size(), p);
                    for(int i = 0; i < p.size(); i++) {

                        String s = pType.get(i);
                        if(s.equals(allTypes[3]) || s.equals(allTypes[4])) {
                            return;
                        }
                    }
                    if(!slide[0]) {
                        
                        for(int i = 0; i < p.size(); i++) {

                            String s = pType.get(i);
                            if(s.equals(allTypes[0]) || s.equals(allTypes[1])) {

                                netArchitecture.get(currentFloor+1).add(p.get(i));
                                netArchitecture.get(currentFloor).remove(p.get(i));
                                i--;
                            }
                        }
                    }
                    else if(slide[0]) {
                        slide[0] = false;
                    }
                    currentNet--;
                    currentFloor++;
                    displayFloor++;
                    floor.setText("Floor " + (displayFloor) + ": " + netArchitecture.get(currentFloor));
                    actions.setText("Net Actions Remaining: " + currentNet);
                }
            }
        });
        bUp.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                if(currentNet != 0 && currentFloor != 0) {

                    ArrayList<program> p = netArchitecture.get(currentFloor);
                    ArrayList<String> pType = getType(p.size(), p);
                    if(!slide[0]) {

                        for(int i = 0; i < p.size(); i++) {

                            String s = pType.get(i);
                            if(s.equals(allTypes[0]) || s.equals(allTypes[1])) {

                                netArchitecture.get(currentFloor-1).add(p.get(i));
                                netArchitecture.get(currentFloor).remove(p.get(i));
                                i--;
                            }
                        }
                    }
                    else if(slide[0]) {
                        slide[0] = false;
                    }
                    currentNet--;
                    currentFloor--;
                    displayFloor--;
                    floor.setText("Floor " + (displayFloor) + ": " + netArchitecture.get(currentFloor));
                    actions.setText("Net Actions Remaining: " + currentNet);
                }
            }
        });
        bBackdoor.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                if(currentNet != 0) {

                    ArrayList<program> p = netArchitecture.get(currentFloor);
                    ArrayList<String> pType = getType(p.size(), p);
                    for(int i = 0; i < p.size(); i++) {

                        if(pType.get(i).equals("Password")) {

                            if(n.backdoor(p.get(i), passKnown)) {

                                floor.setText("Floor " + (displayFloor) + ": " + netArchitecture.get(currentFloor));
                                bPass.setText("Password: Unknown");
                                passKnown = false;
                            }
                            currentNet--;
                            actions.setText("Net Actions Remaining: " + currentNet);
                            break;
                        }
                    }
                }
            }
        });
        bCloak.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                if(currentNet != 0 && !cloaked) {

                    n.interfaceCheck();
                    currentNet--;
                    actions.setText("Net Actions Remaining: " + currentNet);
                    cloaked = true;
                }
            }
        });
        bControl.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                if(currentNet != 0) {

                    currentNet--;
                    actions.setText("Net Actions Remaining: " + currentNet);
                }
            }
        });
        bEyeDee.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                if(currentNet != 0) {
                    ArrayList<program> p = netArchitecture.get(currentFloor);
                    ArrayList<String> pType = getType(p.size(), p);
                    for(int i = 0; i < p.size(); i++) {

                        if(pType.get(i).equals("File")) {

                            if(n.eyeDee(p.get(i))) {
                                floor.setText("Floor " + (displayFloor) + ": " + netArchitecture.get(currentFloor));
                            }
                            currentNet--;
                            actions.setText("Net Actions Remaining: " + currentNet);
                            break;
                        }
                    }
                }
            }
        });
        bPathfinder.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                if(currentNet != 0 && currentFloor == 0 && !pathfinder) {
                    info.setText(n.pathfinder(netArchitecture));
                    currentNet--;
                    actions.setText("Net Actions Remaining: " + currentNet);
                    pathfinder = true;
                }
            }
        });
        bSlide.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                int count = 0;
                int ice = 0;
                ArrayList<String> pType = getType(netArchitecture.get(currentFloor).size(), netArchitecture.get(currentFloor));
                // Checks to see if slide is available for this round
                if(!slide[1]) {
                    // Can only slide if there is exactly one Black ICE
                    for(int i = 0; i < netArchitecture.get(currentFloor).size(); i++) {
                        // Checks if program is Black ICE
                        if(pType.get(i).equals(allTypes[0]) || pType.get(i).equals(allTypes[1])) {

                            count++;
                            ice = i;
                        }
                    }
                }
                if(count == 1) {

                    // Compares an interface check with a perception check of the found Black ICE
                    if(n.interfaceDV(netArchitecture.get(currentFloor).get(ice).perCheck()) && currentNet != 0) {
                        slide[0] = true;
                    }
                    currentNet--;
                    actions.setText("Net Actions Remaining: " + currentNet);
                    slide[1] = true;
                }
            }
        });
        bVirus.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                if(currentNet != 0) {
                    currentNet--;
                    actions.setText("Net Actions Remaining: " + currentNet);
                }
            }
        });
        bZap.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                if(currentNet != 0) {

                    ArrayList<String> pType = getType(netArchitecture.get(currentFloor).size(), netArchitecture.get(currentFloor));
                    ArrayList<program> p = netArchitecture.get(currentFloor);
                    JFrame target = new JFrame();
                    target.setSize(500, 600);
                    target.setLayout(new FlowLayout());
                    // Checks to see which programs are Black ICE and able to be targeted
                    for(int i = 0; i < p.size(); i++) {

                        if(pType.get(i).equals(allTypes[0]) || pType.get(i).equals(allTypes[1])) {
                            // Creates selection for Black ICE target
                            JButton b = new JButton("" + p.get(i));
                            target.add(b);
                            frame.setVisible(false);
                            target.setVisible(true);
                            program ice = p.get(i);
                            b.addActionListener(new ActionListener() {
                            
                                public void actionPerformed(ActionEvent e) {
                
                                    ice.damage(n.zap(ice));
                                    floor.setText("Floor " + (displayFloor) + ": " + netArchitecture.get(currentFloor));
                                    currentNet--;
                                    actions.setText("Net Actions Remaining: " + currentNet);
                                    target.setVisible(false);
                                    frame.setVisible(true);
                                }
                            });
                        }
                    }
                }
            }
        });
        bEnd.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                currentNet = maxNet[interfaceLvl-1];
                actions.setText("Net Actions Remaining: " + currentNet);
                slide[1] = false;
            }
        });
        bPass.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                if(!passKnown) {

                    bPass.setText("Password: Acquired");
                    passKnown = true;
                }
                else {
                    
                    bPass.setText("Password: Unknown");
                    passKnown = false;
                }
            }
        });

        
        
    }

    public static ArrayList<ArrayList<program>> generateNet(int size) {

        ArrayList<ArrayList<program>> net = new ArrayList<ArrayList<program>>();
        for(int i = 0; i < size; i++) {
            net.add(new ArrayList<program>());
        }
        net.get(0).add(new program("Wisp", "Anti-Personnel Black ICE", 4, 4, 4, 2, 15));
        net.get(1).add(new program("File", 6));
        net.get(2).add(new program("Killer", "Anti-Program Black ICE", 4, 8, 6, 2, 20));
        net.get(3).add(new program("File", 8));
        net.get(4).add(new program("Scorpion", "Anti-Personnel Black ICE", 2, 6, 2, 2, 15));
        net.get(5).add(new program("Password", 8));
        net.get(6).add(new program("Sabertooth", "Anti-Program Black ICE", 8, 6, 6, 2, 25));
        net.get(7).add(new program("Asp", "Anti-Personnel Black ICE", 4, 6, 2, 2, 15));
        net.get(8).add(new program("Hellhound", "Anti-Personnel Black ICE", 6, 6, 6, 2, 20));
        return net;
    }

    public static ArrayList<program> floor(int size, ArrayList<program> programs) {

        ArrayList<program> floor = new ArrayList<program>();
        for(int i = 0; i < size; i++) {
            floor.add(programs.get(i));
        }
        return floor;
    }

    public static ArrayList<String> getType(int size, ArrayList<program> net) {

        ArrayList<String> t = new ArrayList<String>();
        for(int i = 0; i < size; i++) {
            t.add(net.get(i).getType());
        }
        return t;
    }

    // Can read any csv file, used to create each program
    public static Queue<String> csvInput(String file) {
        
        Queue<String> temp = new LinkedList<String>();
        try {
            String path = System.getProperty("user.dir") + "\\" + file;
            File f = new File(path);
            Scanner s = new Scanner(f);
            String DELIMITER = "\n";
            s.useDelimiter(DELIMITER);
            // Separates each line of data
            while(s.hasNext()) {
                temp.add(s.nextLine());
            }
            DELIMITER = ",";
            int lines = temp.size();
            // Divides each line into individual data
            for(int i = 0; i < lines; i++) {
                
                s = new Scanner(temp.poll());
                s.useDelimiter(DELIMITER);
                while(s.hasNext()) {
                    temp.add(s.next());
                }
            }
            s.close();
        }
        catch(Exception ex) {

        }
        return temp;
    }

    public static void csvOutput() {
        
    }
}