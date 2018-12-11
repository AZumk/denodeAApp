package dbManager;

/**
 * Created by Ana Zumk on 18/10/2017.
 */

public class perfilUsu_table {
    int ID_USU;
    String NOME_USU;
    String EMAIL_USU;
    String SENHA_USU;
    int AVAT_USU;

    public perfilUsu_table(){}


    //setter
    public void setID_USU(int i) { this.ID_USU = ID_USU;}
    public void setNOME_USU(String NOME_USU){
        this.NOME_USU = NOME_USU;
    }
    public void setEMAIL_USU(String EMAIL_USU){
        this.EMAIL_USU = EMAIL_USU;
    }
    public void setSENHA_USU(String SENHA_USU){
        this.SENHA_USU = SENHA_USU;
    }
    public void setAVAT_USU(int AVAT_USU){
        this.AVAT_USU = AVAT_USU;
    }

    //getter
    public int getID_USU(){ return this.ID_USU;}
    public String getNOME_USU(){
        return this.NOME_USU;
    }
    public String getEMAIL_USU(){
        return this.EMAIL_USU;
    }
    public String getSENHA_USU(){
        return this.SENHA_USU;
    }
    public int getAVAT_USU(){ return this.AVAT_USU;}

}
