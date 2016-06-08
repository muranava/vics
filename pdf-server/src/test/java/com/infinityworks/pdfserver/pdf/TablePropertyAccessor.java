package com.infinityworks.pdfserver.pdf;

import com.infinityworks.pdfserver.pdf.model.GeneratedPdfTable;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;

import java.lang.reflect.Field;

/**
 * Test utility to access table properties for assertions
 */
public class TablePropertyAccessor {
    enum Column {
        HOUSE(0), NAME(1), TEL(2), LIKELIHOOD(3), COST(4), SOVEREIGNTY(5), BORDER(6), INTENTION(7), VOTED(8),
        HAS_PV(9), WANTS_PV(10), LIFT(11), POSTER(12), NO_ACCESS(13), DEAD(14), ROLL_NUM(15);

        int column;

        Column(int column) {
            this.column = column;
        }

        public int getColumn() {
            return column;
        }
    }

    public static String getCellText(GeneratedPdfTable table, int row, int column) throws NoSuchFieldException, IllegalAccessException {
        ColumnText columnText = table.table().getRow(row).getCells()[column].getColumn();
        Field content = ColumnText.class.getDeclaredField("waitPhrase");
        content.setAccessible(true);
        Phrase phrase = (Phrase) content.get(columnText);
        return phrase.getContent();
    }
}
