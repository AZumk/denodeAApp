package dbManager;

/**
 * Created by Ana Zumk on 18/10/2017.
 */

public class conteudo_app {
    int ID_CONT;
    String TIT_CONT;
    String CAT_CONT;
    String TEX_CONT;
    String DATA_CONT;
    Boolean LIDO_CONT;
    Boolean SALV_CONT;

    public conteudo_app(){}

    //setter
    public void setID_CONT(int ID_CONT){
        this.ID_CONT = ID_CONT;
    }
   public void setTIT_CONT(String TIT_CONT){this.TIT_CONT = TIT_CONT;}
    public void setCAT_CONT(String CAT_CONT){this.CAT_CONT = CAT_CONT;}
    public void setTEX_CONT(String TEX_CONT){this.TEX_CONT = TEX_CONT;}
    public void setDATA_CONT(String DATA_CONT){this.DATA_CONT = DATA_CONT;}
    public void setLIDO_CONT(Boolean LIDO_CONT){this.LIDO_CONT = LIDO_CONT;}
    public void setSALV_CONT(Boolean SALV_CONT){this.SALV_CONT = SALV_CONT;}

    //getter
    public int getID_CONT(){return this.ID_CONT;}
    public String getTIT_CONT(){return this.TIT_CONT;}
    public String getCAT_CONT(){return this.CAT_CONT;}
    public String getTEX_CONT(){return this.TEX_CONT;}
    public String getDATA_CONT(){return this.DATA_CONT;}
    public Boolean getLIDO_CONT(){return this.LIDO_CONT;}
    public Boolean getSALV_CONT(){return this.SALV_CONT;}


}
