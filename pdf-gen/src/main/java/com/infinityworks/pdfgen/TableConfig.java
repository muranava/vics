package com.infinityworks.pdfgen;

public class TableConfig {
    static final int PAGE_MARGIN_LEFT = 30;
    static final int PAGE_MARGIN_RIGHT = 10;
    static final int PAGE_MARGIN_TOP = 100;
    static final int PAGE_MARGIN_BOTTOM = 40;

    static final int TABLE_WIDTH_PERCENTAGE = 95;
    static final int HORIZONTAL_TEXT_ANGLE = 0;
    static final int VERTICAL_TEXT_ANGLE = 90;

    static final int NUM_HEADER_ROWS = 1;

    static final int[] COLUMN_WIDTHS = {
            70, // house
            90, // name
            55, // tel
            40, // likelihood
            24, // iss 1
            45, // iss 2
            28, // iss 3
            37, // intention
            15, // has pv
            15, // wants pv
            15, // needs lift
            15, // poster
            15, // dead
            85  // ERN
    };
    static final int NUM_COLUMNS = COLUMN_WIDTHS.length;
}
