package com.example.ruhmatoo2;

//hoiab endas ühte mängu, alustab mängu, suhtleb kasutajaga
import java.io.*;
import java.util.ArrayList;;
import java.util.List;
import java.util.Scanner;

public class Mäng {
    private List<Kasutaja> kasutajad;
    private List<Küsimustik> küsimustikud;
    private Kasutaja praeguneKasutaja; //kasutaja, kes hetkel sisse on logitud

    //mängu alustades loetakse logifailist sisse juba loodud kasutajad ja küsimustikud ning lisatakse need objektidena mängu
    public Mäng() {
        kasutajad = new ArrayList<>();
        küsimustikud = new ArrayList<>();
        try {
            File kasutajateFail = new File("kasutajad.txt");
            Scanner skännitudKasutajad = new Scanner(kasutajateFail);
            File küsimustikeFail = new File("küsimustikud.txt");
            Scanner skännitudKüsimustikud = new Scanner(küsimustikeFail);
            while (skännitudKasutajad.hasNextLine()) {
                //formaat: "username;password"
                String rida = skännitudKasutajad.nextLine();
                String[] tükid = rida.split(";");
                String kasutajanimi = tükid[0];
                String parool = tükid[1];
                String punktiskoor = tükid[2];
                Kasutaja uuskasutaja = new Kasutaja(kasutajanimi, parool, punktiskoor);
                kasutajad.add(uuskasutaja);
            }
            skännitudKasutajad.close();
            while (skännitudKüsimustikud.hasNextLine()) {
                //formaat: küsimustikunimi;küsimustikufail
                String rida = skännitudKüsimustikud.nextLine();
                String[] tükid = rida.split(";");
                String küsimustikunimi = tükid[0];
                Küsimustik uusküsimustik = new Küsimustik(küsimustikunimi);
                String küsimustikufail = tükid[1];
                uusküsimustik.koostaKüsimustik(küsimustikufail);
                küsimustikud.add(uusküsimustik);
            }
            skännitudKüsimustikud.close();
        } catch (FileNotFoundException e) {
            System.out.println("Sellist faili ei leidu");
            System.out.println();
        }
    }

    //kasutaja lisamine mängu ja logifaili (formaat: username;password;score)
    public void lisaKasutaja(Kasutaja kasutaja) {
        kasutajad.add(kasutaja);
        try {
            FileWriter kirjutaja = new FileWriter("kasutajad.txt", true);
            kirjutaja.write(kasutaja.getKasutajanimi() + ";" + kasutaja.getParool() + ";" + kasutaja.getPunktiskoor());
            kirjutaja.write("\r\n"); //reavahetus
            kirjutaja.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //küsimustiku lisamine mängu ja logifaili (formaat: nimi;failiNimi)
    public void lisaKüsimustik(Küsimustik küsimustik) {
        küsimustikud.add(küsimustik);
        try {
            FileWriter kirjutaja = new FileWriter("küsimustikud.txt");
            kirjutaja.write(küsimustik.getKüsimustikuNimi() + ";" + küsimustik.getKüsimustikuFailiNimi());
            kirjutaja.write("\r\n"); //reavahetus
            kirjutaja.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //mängu alustamine
    public void alustaMängu() {
        System.out.println("Tere tulemast vastama! Enjoy the game :)");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Kas sul on kasutaja olemas? (jah/ei) "); //laseb kasutajal kooloni järele vastuse kirjutada (tänu järgmisele reale)
        String kasutajaVastus = scanner.nextLine();
        if (kasutajaVastus.equals("jah")) logiSisse();
        else looKasutaja();
        System.out.println("Kas soovid kasutada olemasolevat küsimustikku (või luua uue)? (jah/ei) ");
        kasutajaVastus = scanner.nextLine();
        if (kasutajaVastus.equals("jah")) {
            boolean küsimustikEksisteerib = false;

            //väljastab loodud küsimustikud (nimedena)
            System.out.println("Olemasolevad küsimustikud:");
            for (Küsimustik küsimustik: küsimustikud) {
                System.out.println("    " + küsimustik.getKüsimustikuNimi());
            }
            while (!küsimustikEksisteerib) {
                System.out.println("Millist küsimustikku soovid kasutada? ");
                kasutajaVastus = scanner.nextLine();
                Küsimustik küsimustik = leiaKüsimustik(kasutajaVastus);
                if (küsimustik != null) {
                    //kui küsimustik eksisteerib, siis alustab selle küsimist
                    küsimustik.küsiKüsimustik(praeguneKasutaja);
                    küsimustikEksisteerib = true;
                }
                else {
                    //kui ei eksisteeri, siis palub uuesti nime
                    System.out.print("Sellist küsimustikku ei ole andmebaasis!");
                }
            }
        } else {
            //laseb koostada uue küsimustiku faili kaudu ja alustab selle küsimist
            System.out.println("Anna uuele küsimustikule nimi: ");
            kasutajaVastus = scanner.nextLine();
            Küsimustik küsimustik = new Küsimustik(kasutajaVastus);
            System.out.println("Sisesta faili nimi, kus küsimustik asub: ");
            kasutajaVastus = scanner.nextLine();
            küsimustik.koostaKüsimustik(kasutajaVastus);
            lisaKüsimustik(küsimustik);
            küsimustik.küsiKüsimustik(praeguneKasutaja);
        }

    }

    //võimaldab kasutajal oma olemasolevasse kasutajasse sisse logida
    public void logiSisse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Kasutajanimi: ");
        String kasutajaVastus = scanner.nextLine();
        if (leiaKasutaja(kasutajaVastus)!=null) {
            Kasutaja kasutaja = leiaKasutaja(kasutajaVastus);

            //kui kasutaja on olemas, siis küsib parooli (kuni sisestatakse õige parool)
            boolean õigeParool = false;
            while (!õigeParool) {
            Scanner parool = new Scanner(System.in);
            System.out.print("Parool: ");
            String kasutajaParool = parool.nextLine();
                if (kasutajaParool.equals(kasutaja.getParool())) {
                    System.out.println("Sisse logitud!");
                    praeguneKasutaja = kasutaja; //logib sisse
                    õigeParool = true;
                } else {
                    System.out.println("Vale parool!");
                }
            }
        }

    }

    //loob kasutajalt küsitud andmete abil uue kasutaja
    public Kasutaja looKasutaja() {
        Scanner kasutajaloomine = new Scanner(System.in);
        System.out.print("Kasutajanimi: ");
        String kasutajanimi = kasutajaloomine.nextLine();
        System.out.print("Parool: ");
        String parool = kasutajaloomine.nextLine();
        Kasutaja kasutaja = new Kasutaja(kasutajanimi, parool);
        praeguneKasutaja = kasutaja;
        lisaKasutaja(kasutaja);
        System.out.println("Kasutaja loodud! Naudi!");
        return kasutaja;
    }

    //tagastab kasutaja (objektina) otsides kasutajanime alusel kasutajate hulgast
    public Kasutaja leiaKasutaja(String kasutajanimi) {
        for (Kasutaja kasutaja : kasutajad) {
            if (kasutaja.getKasutajanimi().equals(kasutajanimi)) {
                return kasutaja;
            }
        }
        return null;
    }

    //tagastab küsimustiku (objektina) otsides küsimustiku nime alusel
    public Küsimustik leiaKüsimustik(String otsiKüsimustik) {
        for (Küsimustik küsimustik : küsimustikud) {
            if (küsimustik.getKüsimustikuNimi().equals(otsiKüsimustik)) {
                return küsimustik;
            }
        }
        return null;
    }
}
