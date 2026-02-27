package com.karmorak.lib.ui.button;

import com.karmorak.lib.Color;
import com.karmorak.lib.ColorPreset;
import com.karmorak.lib.Input;
import com.karmorak.lib.KLIB;
import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.engine.graphic.flat.DrawMap;
import com.karmorak.lib.engine.graphic.flat.Texture;
import com.karmorak.lib.font.Text;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;
import com.karmorak.lib.ui.button.events.Button_onKey_Events;
import com.karmorak.lib.ui.button.events.Button_onTouchDown_Event;
import org.lwjgl.glfw.GLFW;

public class Textable extends Button implements Button_onTouchDown_Event, Button_onKey_Events {


    private final Texture outline;
    private final Texture highlighted;
    private final Texture marked;

    private String display_name;
    private final Hang cursor;
    private int cursor_pos;

    private static int line_thickness;
    private static float text_scale;
    private static float text_pos_y;

    boolean show_cursor = true;
    double anim_time;
    int cur_text_width = 0;

    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_DELAY = 300;
    boolean marked_b = false;

    public Textable(Vector2i box_size) {
        super(true);

        setName("|");
        setSelectable(true);
        Option_SelectableOptions(false, false);

        line_thickness = 2;
        text_scale = 0.8f;
        text_pos_y = 0.4f;

        DrawMap map = new DrawMap(box_size, ColorPreset.LIGHT_GRAY.toColor().setAlpha(175));
        map.drawOutline(ColorPreset.BLACK,line_thickness);
        outline = new Texture(map);
        outline.setPosition(getPosition());
        map.fill(ColorPreset.WHITE.toColor().setAlpha(175));
        map.drawOutline(ColorPreset.ALPHA, line_thickness);
        highlighted = new Texture(map);
        map.destroy();

        map = new DrawMap(2,2);
        map.fill(ColorPreset.LIGHT_BLUE.toColor().setAlpha(100));
        marked = new Texture(map);
        map.destroy();

        cursor = new Hang(this, "|");
        cursor.show(false);
        cursor.setInteractable(false);

        setHeight((box_size.getHeight()-line_thickness*2) * text_scale);
        setMaxTextWidth(box_size.getWidth() - line_thickness*2 - cursor.getWidth());
        text.setPosition(getX() + line_thickness*2, getY() + (box_size.getHeight()-text.getHeight()) * text_pos_y);
        cursor.setScale(text.getScale());

        selected_Buttons.add(this);

        registerEvent((Button_onTouchDown_Event) this);
//        registerEvent((Button_onTouchDown_Event) this);



        setAlignment(false);
//        updateCursor();
    }

    public void updateAlignment() {
        if(text.getAlignment() == Text.Text_Align.RIGHT_BOUND) {
            if(text.getShifting() == 0 && text.get_LastDrawnChar() == 0 ) {
                setTextAlign(Text.Text_Align.LEFT_BOUND);
            }
        } else {
            if(text.get_LastDrawnChar() < text.getNameLength()) {
                setTextAlign(Text.Text_Align.RIGHT_BOUND);
            }
        }

    }

    public void setAlignment(boolean right) {
        if(right) {
            setTextAlign(Text.Text_Align.RIGHT_BOUND);
            cursor_pos = display_name.length();

            updateCursor();
        } else {
            setTextAlign(Text.Text_Align.LEFT_BOUND);
            cursor_pos = 0;

            updateCursor();
        }



    }

    @Override
    public void setScale(float scale) {
        outline.setScale(scale);
        highlighted.setScale(scale);

        setHeight((outline.getHeight()-line_thickness*2)*text_scale);
        setMaxTextWidth((int) (text.getMaxWidth() * scale));
        setPosition(getPosition());
    }

    @Override
    public float getScale() {
        return outline.getScale();
    }

    @Override
    public Button setHeight(float height) {
        text.setHeight((int) height);
        cursor.setScale(text.getScale());
        cursor.setRelPosition(getMaxTextWidth(), - line_thickness);
        text.setPosition(getX() + line_thickness*2, getY() + (outline.getHeight()-text.getHeight()) * text_pos_y);
        updateCursor();
        return this;
    }

    @Override
    public Button setName(String name) {
        display_name = name;
        return super.setName(display_name);
    }

    @Override
    protected boolean isColliding() {
        return Button.isColliding((int) getX(), (int) getY(), (int) outline.getWidth(), (int) outline.getHeight());
    }

    @Override
    public Vector2 getPosition() {
        return outline.getPosition();
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
    public Button setPosition(float x, float y) {
        super.setPosition(x + line_thickness*2, y + (outline.getHeight()-text.getHeight()) * text_pos_y);
        outline.setPosition(x, y);
        highlighted.setPosition(x, y);
        updateCursor();
        marked.setPosition(text.getTotalPosition().getX() + text.getMaxWidth() - cur_text_width, text.getTotalPosition().getY());
        if(text.getAlignment() == Text.Text_Align.LEFT_BOUND) {
            marked.setPosition(text.getTotalPosition().getX(), text.getTotalPosition().getY());
            marked.setSize(text.getWidth(0, text.getNameLength()), text.getHeight());
        } else {
            marked.setPosition(text.getTotalPosition().getX() + text.getMaxWidth() - cur_text_width, text.getTotalPosition().getY());
            marked.setSize(cur_text_width, text.getHeight());
        }
        return this;
    }

    @Override
    public Button setPosition(Vector2 pos) {
        return setPosition(pos.getX(), pos.getY());
    }





    public void updateCursor(int cursor_pos) {
        this.cursor_pos = cursor_pos;
        if(getTextAlign() == Text.Text_Align.RIGHT_BOUND) {
            cur_text_width = text.getWidth(cursor_pos, display_name.length() - text.getShifting());
            if(cur_text_width <= 0) {
                cursor.setRelPosition(text.getWidth() - cur_text_width,-line_thickness);
            } else {
                cursor.setRelPosition(text.getWidth() - cur_text_width- text.getCharSpacing() + (text.getCharSpacing()-cursor.getWidth())*0.5f,-4);
            }

        } else {
            cur_text_width = text.getWidth(text.getShifting(), cursor_pos);
            if(cur_text_width <= 0) {
                cursor.setRelPosition(cur_text_width, -line_thickness);
            } else {
                cursor.setRelPosition(cur_text_width,-line_thickness);
            }
        }
    }

    public void updateCursor() {
        if(getTextAlign() == Text.Text_Align.RIGHT_BOUND) {
            cur_text_width = text.getWidth(cursor_pos, display_name.length() - text.getShifting());
            if(cur_text_width <= 0) {
                cursor.setRelPosition(text.getWidth() - cur_text_width,-line_thickness);
            } else {
                cursor.setRelPosition(text.getWidth() - cur_text_width- text.getCharSpacing() + (text.getCharSpacing()-cursor.getWidth())*0.5f,-4);
            }

        } else {
            cur_text_width = text.getWidth(text.getShifting(), cursor_pos);
            if(cur_text_width <= 0) {
                cursor.setRelPosition(cur_text_width, -line_thickness);
            } else {
                cursor.setRelPosition(cur_text_width,-line_thickness);
            }
        }
    }

    public int getCursorPos() {
        return cursor_pos;
    }

    public void update() {
        anim_time += KLIB.graphic.getDeltaTime();
        if(anim_time > 0.5f) {
            show_cursor = !show_cursor;
            anim_time = 0;
            cursor.show(show_cursor && isSelected());
        }
    }

    @Override
    public void draw(MasterRenderer renderer) {
        draw(renderer, 0);
    }

    @Override
    public void draw(MasterRenderer renderer, int layer) {
        if (show) {
            text.draw(renderer, layer + 1);
            for (Hang h : hangs) {
                h.draw(renderer, layer + 1);
            }

            if(marked_b)
                renderer.processTexture(marked, layer+2);

            renderer.processTexture(outline, layer);

            if (isHovered() || isSelected())
                renderer.processTexture(highlighted, layer);

        }
    }




    @Override
    public void onTouchDown() {


        long currentTime = System.currentTimeMillis();
        if(currentTime - lastClickTime < DOUBLE_CLICK_DELAY) {
//            setSelected(this.getID());
            if(marked_b) {
                marked_b = false;
            } else {
                marked_b = true;
                if(text.getAlignment() == Text.Text_Align.LEFT_BOUND) {
                    marked.setPosition(text.getTotalPosition().getX(), text.getTotalPosition().getY());
                    marked.setSize(text.getWidth(0, text.getNameLength()), text.getHeight());
                } else {
                    marked.setPosition(text.getTotalPosition().getX() + text.getMaxWidth() - cur_text_width, text.getTotalPosition().getY());
                    marked.setSize(cur_text_width, text.getHeight());
                }
            }
            lastClickTime = 0;
        } else {
            lastClickTime = currentTime;
        }

            int i = text.getHoveredChar();
            if (i != -1) {
                cursor_pos = i;
                updateCursor();
            }

        if(isSelected()) {
            cursor.show(true);
        }
    }

    @Override
    public void onKeyTyped(int glfw_key, char character) {
        if(isSelected()) {
            if(character != 0) {
                setName(display_name.substring(0, cursor_pos) + character + display_name.substring(cursor_pos));
//                text.removeShifting(1);
//                if(text.getAlignment() == Text.Text_Align.LEFT_BOUND) {
//                    text.
//                }

                cursor_pos++;
                if(text.getAlignment() == Text.Text_Align.LEFT_BOUND)
                    text.set_LastDrawnChar(text.get_LastDrawnChar()+1);
                updateAlignment();
                updateCursor();
            }
        }
    }

    @Override
    public void onKeyDown(int key, int action, int modifier) {
        if(key == GLFW.GLFW_KEY_V) {
            if(Input.keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
               String clipboard =  KLIB.io.getClipboardString();
               if(!clipboard.isBlank()) {
                   setName(display_name.substring(0, cursor_pos) + clipboard + display_name.substring(cursor_pos));
                   text.addShifting(clipboard.length());
                   if(text.getAlignment() == Text.Text_Align.LEFT_BOUND)
                       text.set_LastDrawnChar(text.get_LastDrawnChar()+clipboard.length());
                   updateAlignment();
                   updateCursor();

               }
               return;
            }
        }
        if(key == GLFW.GLFW_KEY_C) {
            if(Input.keys[GLFW.GLFW_KEY_LEFT_CONTROL] && marked_b) {
                KLIB.io.copyToClipboard(display_name);
                return;
            }
        }
        if(key == GLFW.GLFW_KEY_A) {
            if(Input.keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
                marked_b = true;
                marked.setPosition(text.getTotalPosition().getX() + text.getMaxWidth() - cur_text_width, text.getTotalPosition().getY());
                marked.setSize(cur_text_width, text.getHeight());
                return;
            };
        }


        if(key == GLFW.GLFW_KEY_RIGHT) {
             if(cursor_pos < text.getNameLength()) {
                cursor_pos++;

                if (cursor_pos > text.getNameLength() - text.getShifting()) {
                    text.removeShifting(1);
                }
                updateCursor();
            }
        }
        if(key == GLFW.GLFW_KEY_LEFT) {
            if(cursor_pos > 0) {
                cursor_pos--;

                if(text.getAlignment() == Text.Text_Align.RIGHT_BOUND) {
                    if (cursor_pos < text.get_LastDrawnChar()) {
                        text.addShifting(1);
                        text.updateState(0);

                    }
                } else {
                    if (text.getShifting() > 0 && cursor_pos < text.getShifting()) {
                        text.removeShifting(1);
                        text.updateState(0);
                    }
                }
                updateCursor();
            }
        }
        if(key == GLFW.GLFW_KEY_DELETE)  {
            if(cursor_pos < text.getNameLength()) {

                setName(display_name.substring(0, cursor_pos) + display_name.substring(cursor_pos+1));

                text.removeShifting(1);

                updateAlignment();
                updateCursor();
            }
        }
        if(key == GLFW.GLFW_KEY_BACKSPACE)  {

            if(cursor_pos > 0) {
                cursor_pos --;

                setName(display_name.substring(0, cursor_pos) + display_name.substring(cursor_pos+1));

                updateAlignment();
                updateCursor();
            }
        }
    }
}
