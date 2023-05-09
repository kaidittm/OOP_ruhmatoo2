package com.example.ruhmatoo2;
//Kasutaja klass hoiab endas infot kindla kasutaja nime, parool ja punktiskooride kohta
public class Kasutaja {
    private String kasutajanimi;
    private String parool;
    private String punktiskoor;

    //konstruktor kasutajanime ja parooliga uue kasutaja loomiseks
    public Kasutaja(String kasutajanimi, String parool) {
        this.kasutajanimi = kasutajanimi;
        this.parool = parool;
        this.punktiskoor = "0/0";
    }

    //konstruktor failist lugemiseks
    public Kasutaja(String kasutajanimi, String parool, String punktiskoor) {
        this.kasutajanimi = kasutajanimi;
        this.parool = parool;
        this.punktiskoor = punktiskoor;
    }

    //get- ja set-meetodid info kättesaamiseks ja -muutmiseks
    public String getKasutajanimi() {
        return kasutajanimi;
    }
    public String getParool() {
        return parool;
    }
    public String getPunktiskoor() {
        return punktiskoor;
    }
    public void setPunktiskoor(String punktiskoor) {
        this.punktiskoor = punktiskoor;
    }

    @Override
    public String toString() {
        return kasutajanimi;
    }
}