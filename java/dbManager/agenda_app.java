package dbManager;

/**
 * Created by Ana Zumk on 18/10/2017.
 */

public class agenda_app {
    int ID_USU;
    int ID_AGE;
    String CAT_AGE;
    String TIT_AGE;
    String DIA_AGE;
    String PER_AGE;
    String ALA_AGE;
    String HOR_ALARME; //verificar como funciona data e horario

    public agenda_app(){}

    //setter
    public void setID_AGE(int CAT_AGE){
        this.ID_AGE = ID_AGE;
    }
    public void setCAT_AGE(String CAT_AGE){
        this.CAT_AGE = CAT_AGE;
    }
    public void setTIT_AGE(String TIT_AGE){
        this.TIT_AGE = TIT_AGE;
    }
    public void setDIA_AGE(String DIA_AGE){
        this.DIA_AGE = DIA_AGE;
    }
    public void setPER_AGE(String PER_AGE){
        this.PER_AGE = PER_AGE;
    }
    public void setALA_AGE(String ALA_AGE){
        this.ALA_AGE = ALA_AGE;
    }
    public void setALHOR_AGE(String HOR_ALARME){
        this.HOR_ALARME = HOR_ALARME;
    }


}
