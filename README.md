Algorytm_Johnsona

Dane
♦ W pierwszej linii pliku In0502.txt znajduje się liczba naturalna n reprezentująca ilość wierzchołków grafu
ważonego, skierowanego, bez cykli ujemnych G=(V,E).
♦ W kolejnych liniach pliku podano reprezentację grafu G w postaci tablicy list incydencji.

Wyjście
♦ W pierwszej linii pliku Out0502.txt znajdują się wartości tablicy δ[] (reprezentujące długości najkrótszych ścieżek w
grafie G’ ze źródła s=0 do pozostałych wierzchołków grafu) wygenerowaną w wyniku realizacji algorytmu
Forda-Bellmana.
♦ W kolejnych n+1 liniach znajduje się tablica list incydencji (uwzględniająca wagi krawędzi) reprezentująca graf
G’=(V’,E’) z funkcją wagową wˆ : E’->R.
♦ W kolejnych n liniach wektory: δ
ˆ [s] (wektor długości najkrótszych ścieżek grafu G (z funkcją wagową
wˆ : E->R) ze źródła s do wszystkich pozostałych wierzchołków grafu) oraz D[s] (wektor długości
najkrótszych ścieżek grafu G (z funkcją wagową w: E->R) ze źródła s do wszystkich pozostałych wierzchołków
grafu) dla s=1,...,n.
