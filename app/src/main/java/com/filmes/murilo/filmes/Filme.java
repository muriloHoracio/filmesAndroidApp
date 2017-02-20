package com.filmes.murilo.filmes;

/**
 * Created by root on 17/02/17.
 */

public class Filme {
    private String title;
    private int number;
    private String genre;
    private boolean net;
    private boolean athome;

    public Filme(String title, int number, String genre, boolean net, boolean athome) {
        this.title = title;
        this.number = number;
        this.genre = genre;
        this.net = net;
        this.athome = athome;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isNet() {
        return net;
    }

    public void setNet(boolean net) {
        this.net = net;
    }

    public boolean isAthome() {
        return athome;
    }

    public void setAthome(boolean athome) {
        this.athome = athome;
    }
}
