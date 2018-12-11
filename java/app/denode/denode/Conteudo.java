package app.denode.denode;

/**
 * Created by Ana Zumk on 01/11/2017.
 */

public class Conteudo {

    private int id;
    private String titulo;
    private String categoria;
    private String texto;
    private String data;
    private String link;
    private int lido;
    private int salvo;

    public Conteudo(int id, String titulo, String categoria, String texto, String data, String link, int lido, int salvo) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.texto = texto;
        this.data = data;
        this.link = link;
        this.lido = lido;
        this.salvo = salvo;
    }

    public int getId() {
        return id;
    }
    public String getTitulo() {
        return this.titulo;
    }
    public String getCategoria() {
        return categoria;
    }
    public String getTexto(){return texto;}
    public String getData(){return data;}
    public String getLink(){return link;}
    public int getLido(){return lido;}
    public int getSalvo(){return salvo;}

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void setTexto(String texto){this.texto = texto;}
    public void setData(String data) {this.data = data;}
    public void setLink(String link){this.link = link;}
    public void setLido(int lido){this.lido = lido;}
    public void setSalvo(int salvo){this.salvo = salvo;}
}
