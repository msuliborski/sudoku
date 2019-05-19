package pl.comp.model;

import java.util.ListResourceBundle;

public class LanguagePack_pl extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"verifyButton", "Sprawdź"},
                {"newGame", "Nowa gra"},
                {"startEasy", "Łatwa"},
                {"startMedium", "Średnia"},
                {"startHard", "Trudna"},
                {"file", "Plik"},
                {"save", "Zapisz"},
                {"load", "Wczytaj"},
                {"language", "Język"}
        };
    }
}
