package com.karmorak.lib.ui.button;

import com.karmorak.lib.engine.graphic.MasterRenderer;
import com.karmorak.lib.math.Vector2;
import com.karmorak.lib.math.Vector2i;
import com.karmorak.lib.ui.button.events.Button_onTouchDown_Event;

public class Cycleable extends Button implements Button_onTouchDown_Event {

    private final Hang prev, cycleLabel, next;

    private final Vector2i title_pos;
    private String[] cycles;
    private int cur_cycle;

    private boolean option_total_position;
    private int option_abs = 64;

    public Cycleable(String[] options) {
        this("", options, 0, false);
    }

    public Cycleable(String name, String[] options, int cur_option, boolean total_position) {
        super(name);

        this.cycles = options;
        this.cur_cycle = cur_option;
        this.option_total_position = total_position;

        prev = new Hang(this, "<");
        cycleLabel = new Hang(this, options[cur_option]);
        next = new Hang(this, ">");

        setHangLeft(prev, option_abs);
        setHangRight(next, option_abs);

        setInteractable(false);
        cycleLabel.setInteractable(false);

        events.put(prev, this);
        events.put(next, this);

        title_pos = new Vector2i(text.getWidth() - option_abs * getScale(), getPosition().getY());
    }
    public void set_Title_Name(String name) {
        text.setName(name);
    }

    public void set_Title_Position(int x, int y) {
        if(option_total_position) {
            text.setPosition(x, y);
        } else {
            title_pos.set(x, y);
        }
    }

    public void set_Prev_Position(int x, int y) {
        if(option_total_position) {
            prev.setPosition(x, y);
        } else {
            prev.setRelPosition(x, y);
        }
    }

    public void set_Next_Position(int x, int y) {
        if(option_total_position) {
            next.setPosition(x, y);
        } else {
            next.setRelPosition(x, y);
        }
    }

    public void Option_Total_Position(boolean use_total_position, int abs_x) {
        this.option_total_position = use_total_position;
        this.option_abs = abs_x;
    }

    public int getOption_abs() {
        return option_abs;
    }

    public void setCycles(String[] cycles) {
        this.cycles = cycles;
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        setPosition(getPosition());
    }

    public int  get_Current_Cycle() {
        return cur_cycle;
    }

    @Override
    public Vector2 getPosition() {
        return prev.getPosition();
    }

    @Override
    public Button setPosition(float x, float y) {
        if(option_total_position) {
            cycleLabel.setPosition(x, y);
        } else {
            text.setPosition(x + title_pos.getX(), y + title_pos.getY());

            prev.setPosition(x, y);
            float ax = x + prev.getWidth() + option_abs * getScale();
            cycleLabel.setPosition(ax, y);
            ax += cycleLabel.getWidth() + option_abs * getScale();
            next.setPosition(ax, y);
        }
        return this;
    }

    void nextOption() {
        cur_cycle++;
        if (cycles.length == cur_cycle)
            cur_cycle = 0;
        cycleLabel.setName(cycles[cur_cycle]);
        if(!option_total_position)
            setPosition(getPosition());
    }

    void previousOption() {
        cur_cycle--;
        if (cur_cycle == -1)
            cur_cycle = cycles.length - 1;
        cycleLabel.setName(cycles[cur_cycle]);
        if(!option_total_position)
            setPosition(getPosition());
    }

    @Override
    public void draw(MasterRenderer renderer) {
        draw(renderer, 1);
    }
    @Override
    public void draw(MasterRenderer renderer, int layer) {
        super.draw(renderer, layer);
    }
    @Override
    public void draw(MasterRenderer renderer, float x, float y) {}
    @Override
    public void draw(MasterRenderer renderer, float x, float y, int layer) {}


    @Override
    public void onTouchDown() {
        if(hovered_Button == prev) {
            previousOption();
        } else if(hovered_Button == next) {
            nextOption();
        }
    }
}

