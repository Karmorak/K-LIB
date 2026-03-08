// GSAM v2.0
package com.karmorak.lib.gamestate;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.karmorak.lib.engine.graphic.MasterRenderer;

import java.util.ArrayList;

public abstract class StateManager {

    private static final boolean[] inits = new boolean[64];
    public static final String[] display_names = new String[64];

    //public static final ArrayList<String> display_names = new ArrayList<>();
    protected static final ArrayList<GameState> states = new ArrayList<>();
    protected static short currentstate = -1;
    public static short changto = -1;

    private static boolean skipUpdate_WhenNotInitialized = false;
    private static boolean skipUpdate = false, skipDraw = false;

    public static void addState(GameState gs, String displayName, short id) {
        states.add(id, gs);
        display_names[id] = displayName;
    }

    public static boolean init() {
        if (!states.isEmpty() && currentstate != -1) {
            if (states.get(currentstate) != null) {
                boolean result = states.get(currentstate).init();
                if (!result && skipUpdate_WhenNotInitialized) skipUpdate = true;
                return inits[currentstate] = result;
            } else {
                System.out.println("Couldn't init \"" + display_names[currentstate] + "\"(" + currentstate + "). its null");
            }
        } else {
            System.out.println("Couldn't init (" + currentstate + "). its null");
        }
        return false;
    }

    public static boolean init(int state) {
        if (states.size() > state) {
            if (states.get(state) != null) {
                boolean result = states.get(state).init();
                if (!result && skipUpdate_WhenNotInitialized) skipUpdate = true;
                return inits[state] = result;
            } else {
                System.out.println("Couldnt init \"" + display_names[state] + "\"(" + state + "). its null");
            }
        } else {
            System.out.println("Couldnt init \"" + display_names[state] + "\"(" + state + "). its null");
        }
        return false;
    }

    public static short update(double deltaTime) {
        if (skipUpdate) {
            skipUpdate = false;
            return currentstate;
        }
        if (!states.isEmpty()) {
            if (!isInit())
                init();
            states.get(currentstate).update(deltaTime);
        }
        return currentstate;
    }

    public static short draw(MasterRenderer renderer) {
        if (skipDraw) {
            skipDraw = false;
            return currentstate;
        }
        if (!states.isEmpty()) {
            if (isInit())
                states.get(currentstate).draw(renderer);
        }
        return currentstate;
    }


    public static short pause() {
        if (!states.isEmpty()) {
            states.get(currentstate).pause();
        }
        return currentstate;
    }

    public static short resume() {
        if (!states.isEmpty())
            states.get(currentstate).resume();
        return currentstate;
    }

    public static short resize(int width, int height) {
        if (!states.isEmpty())
            states.get(currentstate).resize(width, height);
        return currentstate;
    }

    public static int destroy() {
        if (!states.isEmpty()) {
            states.get(currentstate).dispose();
            inits[currentstate] = false;

            for (int i = 0; i < states.size(); i++) {
                if (states.get(i) != null && inits[i]) {
                    states.get(i).dispose();
                    inits[i] = false;
                }
            }
        }
        return currentstate;
    }

    @Deprecated
    /** replaced by changeState */
    public static void changestate(short state) {
        if (currentstate != -1 && inits[currentstate])
            states.get(currentstate).changeState(state);
        currentstate = state;
        if (!inits[state])
            init(state);
    }

    @Deprecated
    /** replaced by changeState */
    public static void changestate(int state) {
        changestate((short) state);
    }

    public static void changeState(short state) {
        if (currentstate != -1 && inits[currentstate])
            states.get(currentstate).changeState(state);
        currentstate = state;
        if (!inits[state])
            init(state);
    }

    public static void changeState(int state) {
        changeState((short) state);
    }

    public static int getStateInt() {
        return currentstate;
    }

    public static GameState getState() {
        return states.get(currentstate);
    }

    public static GameState getState(int i) {
        return states.get(i);
    }

    public static short tap(float x, float y, int count, int button) {
        if (!states.isEmpty())
            states.get(currentstate).tap(x, y, count, button);
        return currentstate;
    }

    public static short touchDown(float screenX, float screenY, int pointer, int button) {
        if (!states.isEmpty())
            states.get(currentstate).touchDown(screenX, screenY, pointer, button);
        return currentstate;
    }

    public static short touchUp(float screenX, float screenY, int pointer, int button) {
        if (!states.isEmpty())
            states.get(currentstate).touchUp(screenX, screenY, pointer, button);
        return currentstate;
    }

    public static short touchDragged(int screenX, int screenY, int pointer) {
        if (!states.isEmpty())
            states.get(currentstate).touchDragged(screenX, screenY, pointer);
        return currentstate;
    }

    public static short mouseMoved(int screenX, int screenY) {
        if (!states.isEmpty())
            states.get(currentstate).mouseMoved(screenX, screenY);
        return currentstate;
    }

    public static short scrolled(double amount_X, double amount_Y) {
        if (!states.isEmpty())
            states.get(currentstate).scrolled(amount_X, amount_Y);
        return currentstate;
    }

    public static short keyDown(int glfw_key, int action, int modifier) {
        if (!states.isEmpty())
            states.get(currentstate).keyDown(glfw_key, action, modifier);
        return currentstate;
    }

    public static short keyTyped(int glfw_key, char character) {
        if (!states.isEmpty())
            states.get(currentstate).keyTyped(glfw_key, character);
        return currentstate;
    }

    public static short keyUp(int glfw_key) {
        if (!states.isEmpty())
            states.get(currentstate).keyUp(glfw_key);
        return currentstate;
    }

    public static short globalkeyDown(NativeKeyEvent e) {
        if (!states.isEmpty())
            states.get(currentstate).globalkeyDown(e);
        return currentstate;
    }

    public static short globalkeyUp(NativeKeyEvent e) {
        if (!states.isEmpty())
            states.get(currentstate).globalkeyUp(e);
        return currentstate;
    }

    public static boolean isInit() {
        return inits[currentstate];
    }

    public static boolean isInit(int state) {
        return inits[state];
    }

    public static void setSkipUpdate_IfNotInit(boolean bool) {
        skipUpdate_WhenNotInitialized = bool;
    }

    public static void skipUpdate() {
        skipUpdate = true;
    }

    public static void skipDraw() {
        skipUpdate = true;
    }


}
