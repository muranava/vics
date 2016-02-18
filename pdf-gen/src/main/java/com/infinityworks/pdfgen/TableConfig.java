package com.infinityworks.pdfgen;

public class TableConfig {
    static final int PAGE_MARGIN_LEFT = 30;
    static final int PAGE_MARGIN_RIGHT = 10;
    static final int PAGE_MARGIN_TOP = 100;
    static final int PAGE_MARGIN_BOTTOM = 30;

    static final int TABLE_WIDTH_PERCENTAGE = 95;
    static final int HORIZONTAL_TEXT_ANGLE = 0;
    static final int VERTICAL_TEXT_ANGLE = 0;

    static final int NUM_HEADER_ROWS = 1;

    static final int[] COLUMN_WIDTHS = {
            40, // house
            75, // name
            65, // tel
            40, // likelihood
            30, // iss 1
            45, // iss 2
            30, // iss 3
            40, // intention
            20, // has pv
            20, // wants pv
            20, // needs lift
            20, // poster
            20, // dead
            85  // ERN
    };
    static final int NUM_COLUMNS = COLUMN_WIDTHS.length;
}
