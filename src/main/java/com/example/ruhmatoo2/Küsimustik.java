package com.example.ruhmatoo2;

//Klass küsimustik, kuhu on koondatud kokku kõik ühe küsimustiku alla käivad küsimused (klassidena)
//Küsimustik loetakse sisse failist, formaat on kirjeldatud all pool
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class Küsimustik {
    private List<Küsimus> küsimused = new ArrayList<Küsimus>();
    private int praeguPunkte; //loeb selle küsimustiku jooksul kasutaja poolt kogutud punkte
    private String küsimustikuNimi; //nime annab kasutaja
    private String küsimustikuFailiNimi; //faili nimi, kus küsimustik asub

    //konstruktor
    public Küsimustik(String küsimustikuNimi) {
        this.küsimustikuNimi = küsimustikuNimi;
    }

    //get-meetodid
    public String getKüsimustikuNimi() {
        return küsimustikuNimi;
    }
    public String getKüsimustikuFailiNimi() {
        return küsimustikuFailiNimi;
    }

    public List<Küsimus> getKüsimused() {
        return küsimused;
    }

    //koostaKüsimustik võtab argumendiks failinime, kus küsimustiku küsimused ja vastused asuvad ning lisab kõik küsimused listi
    public void koostaKüsimustik(String failiNimi) { //fail, kus on küsimused ja vastusevariandid, õige vastus peab olema esimene pärast küsimust
        try {
            küsimustikuFailiNimi = failiNimi;
            File fail = new File(failiNimi);
            Scanner skännitudFail = new Scanner(fail);
            boolean küsimus = true; //et hoida järge, kas failist loetav rida on küsimus või vastusevariant
            boolean õige = false; //et hoida järge, millal tuleb failis õige vastusevariant
            int indeks = küsimused.size() - 1;

            //loome iga küsimus-vastuste ploki kohta ühe objekti ja lisame objekti küsimustikku
            while (skännitudFail.hasNextLine()) {
                //formaat: küsimus ühel real, siis teadmata hulk vastuseid, siis tühi rida
                String rida = skännitudFail.nextLine();
                if (küsimus) {
                    Küsimus praeguneKüsimus = new Küsimus(rida);
                    küsimused.add(praeguneKüsimus);
                    indeks += 1;
                    küsimus = false;
                    õige = true;
                } else if (rida == "") {
                    küsimus = true;
                } else if (õige) {
                    küsimused.get(indeks).lisaVastus(rida);
                    küsimused.get(indeks).määraÕigeVastus(rida);
                    õige = false;
                } else {
                    küsimused.get(indeks).lisaVastus(rida);
                }
            }
            skännitudFail.close();
        } catch (FileNotFoundException e) {
            System.out.println("Sellist faili ei leidu" + e);
        }
    }

    //väljastab järjest küsimused, küsib ja kontrollib vastuseid, hoiab skoori arvet
    public void küsiKüsimustik(Kasutaja kasutaja) {
        System.out.println("KÜSIMUSED");
        praeguPunkte = 0;

        //küsib iga küsimuse
        for (Küsimus küsimus : küsimused) {
            küsimus.väljastaKüsimus();
            küsimus.väljastaVastuseVariandid();
            boolean kasOliÕige = küsimus.kirjutaVastus();
            if (kasOliÕige) praeguPunkte += 1;
            System.out.println(""); //tühi rida küimuste vahel
        }

        //väljastab tulemuse ja salvestab uue skoori kasutaja andmetesse (punktiskoor: õigeid vastuseid / üldse kokku vastatud küsimustest)
        double protsent = (double)praeguPunkte/küsimused.size();
        System.out.println("Sinu punktiskoor: " + praeguPunkte + "/" + küsimused.size() + " (" + (Math.round(protsent*100)) + "%)");
        String punktid = kasutaja.getPunktiskoor();
        String[] tükid = punktid.split("/");
        tükid[0] = String.valueOf(parseInt(tükid[0])+praeguPunkte);
        tükid[1] = String.valueOf(parseInt(tükid[1])+küsimused.size());
        String uusSkoor = tükid.toString();
        kasutaja.setPunktiskoor(uusSkoor);
    }

    //küsimuse lisamiseks
    public void lisaKüsimus(String küsimus, List<String> vastused) {
        küsimused.add(new Küsimus(küsimus, vastused));
    }
}