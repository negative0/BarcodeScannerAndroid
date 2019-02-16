package com.example.negative0.drawover;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Creates the head layer view which is displayed directly on window manager.
 * It means that the view is above every application's view on your phone -
 * until another application does the same.
 */
public class HeadLayer extends View {

    private Context mContext;
    private FrameLayout mFrameLayout;
    private WindowManager mWindowManager;

    public HeadLayer(Context context) {
        super(context);

        mContext = context;
        mFrameLayout = new FrameLayout(mContext);

        addToWindowManager();
    }

    private void addToWindowManager() {
        final WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }else{
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        params.gravity = Gravity.LEFT;

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mFrameLayout, params);

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Here is the place where you can inject whatever layout you want.
        layoutInflater.inflate(R.layout.head, mFrameLayout);

        // Support dragging the image view
        final ImageView imageView = (ImageView) mFrameLayout.findViewById(R.id.imageView);
        imageView.setOnTouchListener(new OnTouchListener() {
            private int initX, initY;
            private int initTouchX, initTouchY;

            @Override public boolean onTouch(View v, MotionEvent event) {
                int x = (int)event.getRawX();
                int y = (int)event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initX = params.x;
                        initY = params.y;
                        initTouchX = x;
                        initTouchY = y;
                        return true;

                    case MotionEvent.ACTION_UP:
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initX + (x - initTouchX);
                        params.y = initY + (y - initTouchY);

                        // Invalidate layout
                        mWindowManager.updateViewLayout(mFrameLayout, params);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Removes the view from window manager.
     */
    public void destroy() {
        mWindowManager.removeView(mFrameLayout);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        Log.i("key pressed", String.valueOf(event.getKeyCode()));
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent)
    {
        int keyaction = KEvent.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = KEvent.getKeyCode();
            int keyunicode = KEvent.getUnicodeChar(KEvent.getMetaState() );
            char character = (char) keyunicode;

            System.out.println("DEBUG MESSAGE KEY=" + character + " KEYCODE=" +  keycode);
        }


        return super.dispatchKeyEvent(KEvent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int keyaction = event.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = event.getKeyCode();
            int keyunicode = event.getUnicodeChar(event.getMetaState() );
            char character = (char) keyunicode;

            System.out.println("DEBUG MESSAGE KEY ONKEYUP=" + character + " KEYCODE=" +  keycode);
        }
        return super.onKeyUp(keyCode, event);
    }
}