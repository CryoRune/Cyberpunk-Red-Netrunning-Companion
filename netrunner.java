import java.util.Random;
import java.util.ArrayList;

class netrunner {

    private int interfaceLvl;

    public netrunner(int interfaceLvl) {
        this.interfaceLvl = interfaceLvl;
    }

    public void setLvl(int interfaceLvl) {
        this.interfaceLvl = interfaceLvl;
    }

    public int interfaceCheck() {

        Random r = new Random();
        return interfaceLvl + r.nextInt(10) + 1;
    }

    public boolean interfaceDV(int dv) {

        if(interfaceCheck() > dv) {
            return true;
        }
        return false;
    }

    public boolean backdoor(program p, boolean pass) {

        if(pass || interfaceDV(p.getDV())) {

            p.setName("Password (Unlocked)");
            p.setType("PasswordU");
            return true;
        }
        return false;
    }

    public boolean eyeDee(program p) {

        if(interfaceDV(p.getDV())) {

            p.setName("File (Unlocked)");
            p.setType("FileU");
            return true;
        }
        return false;
    }

    public String pathfinder(ArrayList<ArrayList<program>> n) {

        String map = "<html>";
        int floors = interfaceCheck();
        boolean vision = true;
        for(int i = 0; i < n.size(); i++) {

            ArrayList<program> p = n.get(i);
            map += "Floor " + (i+1) + ": ";
            String currentFloor = "";
            int count = 0;
            for(int j = 0; j < p.size(); j++) {

                if(floors < i) {

                    vision = false;
                    break;
                }
                else if(floors > p.get(j).getDV()) {

                    if(count != 0) {
                        currentFloor += " | ";
                    }
                    currentFloor += p;
                    count++;
                }
                else {
                    
                    vision = false;
                    break;
                }
            }
            if(!vision) {
                break;
            }
            map += currentFloor + "<br>";
        }
        map += "</html>";
        return map;
    }

    public int zap(program ice) {

        if(interfaceDV(ice.defCheck())) {

            Random r = new Random();
            return r.nextInt(6) + 1;
        }

        return 0;
    }

    public void activate(program p) {

    }
}