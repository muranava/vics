package com.infinityworks.webapp.pdf;

import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;

import java.lang.reflect.Field;

/**
 * Test utility to access table properties for assertions
 */
public class TablePropertyAccessor {
    enum Column {
        HOUSE(0), NAME(1), TEL(2), LIKELIHOOD(3), COST(4), SOVEREIGNTY(5), BORDER(6), INTENTION(7),
        HAS_PV(8), WANTS_PV(9), LIFT(10), POSTER(11), NO_ACCESS(12), DEAD(13), ROLL_NUM(14);

        int column;

        Column(int column) {
            this.column = column;
        }

        public int getColumn() {
            return column;
        }
    }

    public static String getCellText(GeneratedPdfTable table, int row, int column) throws NoSuchFieldException, IllegalAccessException {
        ColumnText columnText = table.getTable().getRow(row).getCells()[column].getColumn();
        Field content = ColumnText.class.getDeclaredField("waitPhrase");
        content.setAccessible(true);
        Phrase phrase = (Phrase) content.get(columnText);
        return phrase.getContent();
    }
}
