package com.view.alienlib.base;

public interface ViewInfo {

    void onRaw(int width, int height);

    void onUsefulSpace(int width, int height);

    void onPadding(int top, int right, int bottom, int left);

}
