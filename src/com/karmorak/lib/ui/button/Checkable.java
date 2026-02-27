//v1.0
package com.karmorak.lib.ui.button;

import com.karmorak.lib.Color;
import com.karmorak.lib.Input;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;
import com.karmorak.lib.ui.button.events.Button_onTouchDown_Event;

public class Checkable extends Button {

    final Texture outline;
    final Texture background_highlighted;

    final Texture checkmark;

    Vector2i checkbox_pos = new Vector2i();
    private int checkbox_size;
    final int outline_thickness = 3;
    private final float text_scale = 0.7f;

    public boolean checkbox_right = false;

    public Checkable() {
        this(144);
    }

    public Checkable(String name) {
        this(name, 144);
    }

    public Checkable(int size) {
        this(" ", size);
    }

    public Checkable(String name, int size) {
        super(name);
        setSelectable(true);
        option_unselect_selected = true;
        option_keep_on_click_outside = true;

        DrawMap map = new DrawMap(getFontSize(), getFontSize());
        map.drawOutline(getColor(), new Vector2(getFontSize(), getFontSize()), new Vector2(outline_thickness, outline_thickness));
        //map.fill(Color.RED);
        outline = new Texture(map);
        //background.create();
        map.fill(Color.LIGHT_GRAY().setAlpha(75));
        background_highlighted = new Texture(map);
        map.destroy();
        checkmark = new Texture(KLIB.URL("assets/checkmark.png"));

        outline.setSize(size, size);
        background_highlighted.setSize(size, size);
        checkmark.setSize(size, size);
        this.checkbox_size = size;

        setScale(getScale());

    }

    public void setCheckbox_size(int checkbox_size) {
        this.checkbox_size = checkbox_size;
    }

    public void setBoxAlignment(boolean right) {
        checkbox_right = right;
    }

    @Override
    public Button setName(String name) {
        super.setName(name);
        setPosition(getPosition());
        return this;
    }

    @Override
    public void setX(float x) {
        outline.setX(x);
        background_highlighted.setX(x);
        checkmark.setX(x);

        if(checkbox_right)
            super.setX(x - (text.getWidth() + font.getCharSpacing()) * getScale());
        else
            super.setX(x + (checkbox_size + font.getCharSpacing()) * getScale());;
    }
    @Override
    public void setY(float y) {
        outline.setY(y);
        background_highlighted.setY(y);
        checkmark.setY(y);

        if(checkbox_right)
            super.setY(y);
        else
            super.setY(y);
    }
    @Override
    public Button setPosition(float x, float y) {
        outline.setPosition(x, y);
        background_highlighted.setPosition(x, y);
        checkmark.setPosition(x, y);

        if(checkbox_right)
            return super.setPosition(x - (text.getWidth() + font.getCharSpacing()) * getScale(), y + (checkbox_size*getScale()-text.getHeight()) * 0.5f);
        else
            return super.setPosition(x + (checkbox_size + font.getCharSpacing()) * getScale(), y + (checkbox_size*getScale()-text.getHeight()) * 0.5f);
    }
    @Override
    public Button setPosition(Vector2 pos) {
        return setPosition(pos.getX(), pos.getY());
    }
    @Override
    public float getX() {
        return outline.getX();
    }
    @Override
    public float getY() {
        return outline.getY();
    }
    @Override
    public Vector2 getPosition() {
        return outline.getPosition();
    }
    @Override
    public float getWidth() {
        return checkbox_size * getScale();
    }
    @Override
    public float getWidth(int line) {
        return checkbox_size *  getScale();
    }
    @Override
    public float getHeight() {
        return checkbox_size  *  getScale();
    }
    @Override
    public float getHeight(int line) {
        return checkbox_size  *  getScale();
    }
    @Override
    public void setScale(float scale) {
        outline.setScale(scale);
        background_highlighted.setScale(scale);
        checkmark.setScale(scale);
        setHeight(checkbox_size * scale * text_scale);

        setPosition(getPosition());
    }

    @Override
    public float getScale() {
        return outline.getScale();
    }

    @Override
    protected boolean isColliding() {

        float mouse_x = Input.mouse.getX();
        float mouse_y = Input.mouse.getY();

        return mouse_x > getX() && mouse_x < getX() + getWidth() && mouse_y > getY() && mouse_y < getY() + getHeight();
    }

    @Override
    public void draw(MasterRenderer renderer) {
        draw(renderer, 1);
    }
    @Override
    public void draw(MasterRenderer renderer, int layer) {
        if(show) {
            if(buttonBackground != null && buttonBackground.bg_show)
                renderer.processTexture(buttonBackground.texture, buttonBackground.getBGPosition(), buttonBackground.getBGBounds(), layer);

            text.draw(renderer, layer);

            for(Hang h : hangs) {
                h.draw(renderer, layer);
            }

            if(isHovered())
                renderer.processTexture(background_highlighted, layer);

            if(isSelected())
                renderer.processTexture(checkmark, layer+1);

            renderer.processTexture(outline, layer+2);
        }
    }
    @Override
    public void draw(MasterRenderer renderer, float x, float y) {
       draw(renderer, x, y, 1);
    }
    @Override
    public void draw(MasterRenderer renderer, float x, float y, int layer) {
        if(show) {
            if(buttonBackground != null && buttonBackground.bg_show)
                renderer.processTexture(buttonBackground.texture, buttonBackground.getBGPosition(), buttonBackground.getBGBounds(), layer);

            if(checkbox_right)
                text.draw(renderer, x - (text.getWidth() + font.getCharSpacing()) * getScale(), y, layer);
            else
                text.draw(renderer, x + (checkbox_size + font.getCharSpacing()) * getScale(), y, layer);

            for(Hang h : hangs) {
                h.draw(renderer, layer);
            }

            if(isHovered())
                renderer.processTexture(background_highlighted, new Vector2(x, y),new Vector2(checkbox_size * getScale(), checkbox_size * getScale()), layer);

            if(isSelected())
                renderer.processTexture(checkmark, new Vector2(x, y),new Vector2(checkbox_size * getScale(), checkbox_size * getScale()), layer+1);

            renderer.processTexture(outline, new Vector2(x, y),new Vector2(checkbox_size * getScale(), checkbox_size * getScale()), layer+2);
        }
    }

    public void addEvent(Button_onTouchDown_Event event) {
       registerEvent(event);
    }

}
