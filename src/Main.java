import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void wypisz_liste(ArrayList<Krawedz> lista){
        for(int i=0; i<lista.size();i++){
            Krawedz k = lista.get(i);
            System.out.println(k.poczatek +" - "+ k.koniec +"("+ k.waga +")");
        }
        return;
    }


    public static void main(String[] args) throws IOException {
        File plik_in = new File("In0502.txt");
        FileWriter plik_out = new FileWriter("Out0502.txt");
        Scanner input = new Scanner(plik_in);
        int n = input.nextInt();
        ArrayList<Krawedz> lista_krawedzi = new ArrayList<>();
        int infinity = -100;

        //DODANIE WIERZCHOLKA 0 I UTWORZENIE KRAWEDZI DO WSZYSTKICH WIERZCHOLKOW Z WAGA 0
        for(int i=1; i<=n; i++)
            lista_krawedzi.add(new Krawedz(0,i,0));//poczatek i waga krawedzi =0

        //WCZYTANIE LISTY LINCYDENCJI NA TABLICE
        input.nextLine();
        for(int i=1; i<=n; i++) {
            int poczatek = i;
            String linia;
            try {
                linia = input.nextLine();
            }
            catch (NoSuchElementException wyjatek){
                //OBSLUGA SYTUACJI GDY Z DANEGO WIERZCHOLKA NIE WYCHODZA ZADNE KRAWEDZIE (LINIA JEST PUSTA)
                // po prostu kontunuujemy przegladanie pliku
                continue;
            }
            Scanner skaner_linii = new Scanner(linia);
            while(skaner_linii.hasNext()) {
                int koniec = skaner_linii.nextInt();
                int waga = skaner_linii.nextInt();
                if(waga > infinity) infinity = waga;
                Krawedz k = new Krawedz(i,koniec,waga);
                lista_krawedzi.add(k);
            }
        }
        infinity++;


        //tworzenie tablicy delta[] czyli najkrotsze sciezki w grafie z wirzcholka 0 do pozostalych wierzcholkow
        //alg Forda-Bellmana
        boolean zmiana = true;
        int delta[] = new int[n+1];
        for(int i=0; i<delta.length; i++)
            delta[i] =0;

        while(zmiana == true){
            zmiana = false;
            for(int i=0; i< lista_krawedzi.size();i++){
                Krawedz k = lista_krawedzi.get(i);
                if(delta[k.koniec] > (delta[k.poczatek] + k.waga)) {
                    delta[k.koniec] = delta[k.poczatek] + k.waga;
                    zmiana = true;
                }
            }
        }

        //WPISANIE TABLICY DELTA DO PLIKU
        for(int i=0; i<delta.length; i++)
            plik_out.write(delta[i] +" ");
        plik_out.write("\n");

        //OBLICZANIE NOWYCH WAG DLA GRAFU ze wzoru waga^ = waga + delta[poczatek] - delta[koniec]
        for(int i=0;i<lista_krawedzi.size();i++){
            Krawedz k = lista_krawedzi.get(i);
            k.waga2 = k.waga + delta[k.poczatek] - delta[k.koniec];
        }

        //WPISANIE NOWEJ LISTY INCYDENCJI DO PLIKU
        for(int i =0;i<=n;i++){
            plik_out.write("["+ i +"]  ");
            for(int j=0; j<lista_krawedzi.size();j++){
                Krawedz k = lista_krawedzi.get(j);
                if(k.poczatek == i)
                    plik_out.write(k.koniec +"("+k.waga2+") ");
            }
            plik_out.write("\n");
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        //OBLICZANIE ALG DIJKSTRY DLA KAZDEGO WIERZCHOLKA
        for (int i=0; i<lista_krawedzi.size();i++){
            Krawedz k = lista_krawedzi.get(i);
            if(k.waga2 > infinity) infinity = k.waga2;
        }
        infinity++;

        int tab[][] = new int[n+1][n+1];
        int tab2[][] = new int[n+1][n+1];
        for(int i=0; i<n+1;i++)
            for(int j=0;j<n+1;j++) {
                tab[i][j] = infinity;
                tab2[i][j] = infinity;
            }

        for (int i=0; i<lista_krawedzi.size();i++){
            Krawedz k = lista_krawedzi.get(i);
            tab[k.poczatek][k.koniec] = k.waga2;
            tab2[k.poczatek][k.koniec] = k.waga2;
        }


        for(int s=1; s<n+1;s++) {  // s - zrodlo do alg Dijkstry
            int dist[] = new int[n+1];
            int dist2[] = new int[n+1];
            boolean tab_final[] = new boolean[n+1];
            for(int v=1; v<=n; v++) {
                dist[v] = infinity;
                dist2[v] = infinity;
                tab_final[v] = false;
            }
            dist[s] =0;
            tab_final[s] =true;
            int last = s;

            for(int i=1;i<n;i++){
                for(int v=1;v<=n; v++)
                    if(tab[last][v]< infinity && (!tab_final[v])){
                        if(dist[last]+tab[last][v]<dist[v])
                            dist[v] =dist[last] + tab[last][v];}

                int temp = infinity;
                int y = 1;
                for(int u=1;u<=n; u++)
                    if((!tab_final[u])&&(dist[u]<temp)){
                        y=u;
                        temp = dist[u];
                    }
                if(temp<infinity){
                    tab_final[y] =true;
                    last =y;
                }
            }

            // zmiana
            for(int i=1; i<n+1; i++) {
                if(dist[i] == infinity){
                    dist2[i] = -999;
                    continue;
                }
                dist2[i] = dist[i] + delta[i] - delta[s];
            }

            //WPISANIE DO PLIKU
            plik_out.write("Delta^["+ s + "][ ");
            for(int i=1; i<dist.length;i++){
                if(dist[i] == infinity){
                    plik_out.write("\u221E ");    //\u221E - znak nieskonczonosci
                    continue;
                }
                plik_out.write(dist[i]+ " ");
            }
            plik_out.write("], D["+ s + "][ ");
            for(int i=1; i<dist2.length;i++){
                if(dist2[i] == -999){
                    plik_out.write("\u221E ");
                    continue;
                }
                plik_out.write(dist2[i]+ " ");
            }
            plik_out.write("]\n");
        }



        plik_out.close();
    }
}
