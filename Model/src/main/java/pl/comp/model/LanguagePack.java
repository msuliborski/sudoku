package pl.comp.model;

import java.util.ListResourceBundle;

public class LanguagePack extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"verifyButton", "Verify"},
                {"newGame", "New game"},
                {"startEasy", "Easy"},
                {"startMedium", "Medium"},
                {"startHard", "Hard"},
                {"file", "File"},
                {"save", "Save"},
                {"load", "Load"},
                {"language", "Language"}
        };
    }
}
