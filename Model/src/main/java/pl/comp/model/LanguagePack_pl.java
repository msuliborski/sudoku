package pl.comp.model;

import java.util.ListResourceBundle;

public class LanguagePack_pl extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"verifyButton", "SPRAWDŹ"},
                {"newGame", "Nowa gra"},
                {"startEasy", "Łatwa"},
                {"startMedium", "Średnia"},
                {"startHard", "Trudna"},
                {"file", "Plik"},
                {"save", "Zapisz"},
                {"load", "Wczytaj"},
                {"language", "Język"},

                {"correct", "DOBRZE!"},
                {"nologicerrors", "BRAK BŁĘDÓW!"},
                {"wrong", "ŹLE!"},
                {"newgameb", "ZACZNIJ NOWĄ GRĘ Z MENU NA GÓRZE!"}

        };
    }
}
