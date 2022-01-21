package com.trolleyhut.toptrendy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IntroSlider extends Fragment {
  //Layout id
  private static final String ARG_LAYOUT_RES_ID = "layoutResId";

  public static IntroSlider newInstance(int layoutResId) {
    IntroSlider sampleSlide = new IntroSlider();

    Bundle bundleArgs = new Bundle();
    bundleArgs.putInt(ARG_LAYOUT_RES_ID, layoutResId);
    sampleSlide.setArguments(bundleArgs);

    return sampleSlide;
  }

  private int layoutResId;

  public IntroSlider() {

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
      layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(layoutResId, container, false);
  }

}
