package pl.comp.model.lang;

import java.util.ListResourceBundle;

public class LanguagePack extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"verifyButton", "VERIFY"},
                {"newGame", "New game"},
                {"startEasy", "Easy"},
                {"startMedium", "Medium"},
                {"startHard", "Hard"},
                {"file", "File"},
                {"save", "Save"},
                {"load", "Load"},
                {"language", "Language"},

                {"correct", "CORRECT!"},
                {"nologicerrors", "NO LOGIC ERRORS!"},
                {"wrong", "WRONG!"},
                {"newgameb", "START NEW GAME FROM THE MENU ABOVE!"}

        };
    }
}
