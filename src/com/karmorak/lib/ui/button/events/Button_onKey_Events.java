package com.karmorak.lib.ui.button.events;

public interface Button_onKey_Events extends ButtonEvent{

    void onKeyTyped(int glfw_key, char character);
    void onKeyDown(int glfw_key, int action, int modifier);

}
