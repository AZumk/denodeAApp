package app.denode.denode;

import static android.R.attr.name;

/**
 * Created by Ana Zumk on 28/10/2017.
 */

    public class Tarefa {

        private int id;
        private String titulo;
        private String categoria;
        private boolean feita;

        public Tarefa(int id, String name, String description, boolean feito) {
            this.id = id;
            this.titulo = name;
            this.categoria = description;
            this.feita = feito;
        }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
            return this.titulo;
        }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
            return categoria;
        }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFeita(boolean feita){ this.feita = feita;}
    public boolean getFeita(){return this.feita;}
}

