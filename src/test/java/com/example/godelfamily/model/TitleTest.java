package com.example.godelfamily.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleTest {

    @Test
    void testEnumValues() {
        Title[] titles = Title.values();

        assertEquals(4, titles.length);
        assertEquals(Title.JUNIOR, titles[0]);
        assertEquals(Title.MIDDLE, titles[1]);
        assertEquals(Title.SENIOR, titles[2]);
        assertEquals(Title.LEAD, titles[3]);
    }

    @Test
    void testValueOf() {
        assertEquals(Title.JUNIOR, Title.valueOf("JUNIOR"));
        assertEquals(Title.MIDDLE, Title.valueOf("MIDDLE"));
        assertEquals(Title.SENIOR, Title.valueOf("SENIOR"));
        assertEquals(Title.LEAD, Title.valueOf("LEAD"));
    }

    @Test
    void testValueOf_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Title.valueOf("INVALID");
        });
    }
}

