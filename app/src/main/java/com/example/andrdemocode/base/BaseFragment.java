package com.example.andrdemocode.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author dengyan
 * @date 2022/8/8
 * @desc
 */
public class BaseFragment extends Fragment {
    protected Activity activity;
    protected String TAG = this.getClass().getSimpleName();

    private boolean mActivated;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        XLog.i("Fragment onAttach %s", this.getClass().getSimpleName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XLog.i("Fragment onCreate %s", this.getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        XLog.i("Fragment onCreateView %s", this.getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        XLog.i("Fragment onViewCreated %s", TAG);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        XLog.i("Fragment onStart %s", TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        XLog.i("Fragment onResume %s", this.getClass().getSimpleName());
    }

    @CallSuper
    protected void onActivate() {
        mActivated = true;
        XLog.i("Fragment onActivate %s", this.getClass().getSimpleName());
    }

    @CallSuper
    protected void onDeactivate() {
        mActivated = false;
        XLog.i("Fragment onDeactivate %s", this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        XLog.i("Fragment onPause %s", this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        XLog.i("Fragment onStop %s", this.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        XLog.i("Fragment onDestroyView %s", this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XLog.i("Fragment onDestroy %s", this.getClass().getSimpleName());
    }
}
