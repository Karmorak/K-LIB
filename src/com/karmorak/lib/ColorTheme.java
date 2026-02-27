package com.karmorak.lib;

class ColorTheme {

   private final Color BACKGROUND, FONT, FONT_SELECTED, FONT_HOVERED;

   static final ColorTheme
            WHITE = new ColorTheme(ColorPreset.WHITE, ColorPreset.BLACK, ColorPreset.RED, ColorPreset.CYAN),
            BLACK = new ColorTheme(ColorPreset.BLACK, ColorPreset.WHITE, ColorPreset.RED, ColorPreset.CYAN);

    ColorTheme(Colorable background, Colorable font, Colorable fontSelected, Colorable fontHovered) {
        BACKGROUND = background.toColor();
        FONT = font.toColor();
        FONT_SELECTED = fontSelected.toColor();
        FONT_HOVERED = fontHovered.toColor();
    }
}
