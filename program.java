import java.util.Random;

class program {
 
    private String name;
    private String classType;
    private int per;
    private int spd;
    private int atk;
    private int def;
    private int rez;
    private int dv;
    private String[] allTypes = {"Anti-Personnel Black ICE", "Anti-Program Black ICE", "Demon", "Password", "File", "Booster", "Defender", "Attacker", "De-rezed"};

    public program(String name, String classType, int per, int spd, int atk, int def, int rez) {

        this.name = name;
        this.classType = classType;
        this.per = per;
        this.spd = spd;
        this.atk = atk;
        this.def = def;
        this.rez = rez;
        this.dv = 0;
    }

    // Passwords and Files
    public program(String name, int dv) {

        this.name = name + " (Locked)";
        this.classType = name;
        this.per = 0;
        this.spd = 0;
        this.atk = 0;
        this.def = 0;
        this.rez = 0;
        this.dv = dv;
    }
    
    public int perCheck() {
        
        Random r = new Random();
        return per + r.nextInt(10) + 1;
    }
    
    public int defCheck() {
        
        Random r = new Random();
        return def + r.nextInt(10) + 1;
    }
    
    public void damage(int dmg) {

        rez -= dmg;
        if(rez <= 0) {
            name += " (De-rezed)";
            classType = allTypes[8];
        }
    }

    public String getName() {
        return name;
    }
    
    public String getType() {
        return classType;
    }

    public int getDV() {
        return dv;
    }

    public String[] getAllTypes() {
        return allTypes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String classType) {
        this.classType = classType;
    }

    @Override
    public String toString() {

        return "" + this.name;
    }
}