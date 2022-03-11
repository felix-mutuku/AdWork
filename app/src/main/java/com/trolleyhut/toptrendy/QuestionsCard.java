package com.trolleyhut.toptrendy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.mindorks.placeholderview.SwipeDirection;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;

@Layout(R.layout.question_card_view)
public class QuestionsCard {
    @View(R.id.question)
    TextView questionTextView;
    @SwipeView
    android.view.View cardView;

    Questions mQuestions;
    Context mContext;
    SwipePlaceHolderView mSwipeView;
    Activity mactivity;

    public QuestionsCard(Context context, Questions questions, SwipePlaceHolderView swipeView,
                         Activity activity) {
        mContext = context;
        mQuestions = questions;
        mSwipeView = swipeView;
        mactivity = activity;
    }

    @Resolve
    public void onResolved() {
        //set data into the cards
        questionTextView.setText(mQuestions.getQuestion());
    }

    @SwipeOut
    public void onSwipedOut() {
        //red light
        Log.e("EVENT", "onSwipedOut");
        //mSwipeView.addView(this);

        //check internet connection
        Constants.checkInternet(mactivity);

        //add point and show ad after
        Constants.addAPoint(mactivity);
    }

    @SwipeCancelState
    public void onSwipeCancelState() {
        Log.e("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    public void onSwipeIn() {
        Log.e("EVENT", "onSwipedIn");
        // mSwipeView.addView(this);

        //check internet connection
        Constants.checkInternet(mactivity);

        //add point and show ad after
        Constants.addAPoint(mactivity);
    }

    @SwipeInState
    public void onSwipeInState() {
        Log.e("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState() {
        Log.e("EVENT", "onSwipeOutState");
    }

    @SwipeInDirectional
    public void onSwipeInDirectional(SwipeDirection direction) {
        //green light
        Log.e("EVENT", "SwipeInDirectional " + direction.name());
        //mSwipeView.addView(this);

        //check internet connection
        Constants.checkInternet(mactivity);

        //add point and show ad after
        Constants.addAPoint(mactivity);
    }

    @SwipeOutDirectional
    public void onSwipedOutDirectional(SwipeDirection direction) {
        //red light
        Log.e("EVENT", "SwipeOutDirectional " + direction.name());
        //mSwipeView.addView(this);

        //check internet connection
        Constants.checkInternet(mactivity);

        //add point and show ad after
        Constants.addAPoint(mactivity);
    }

    @Click(R.id.question)
    public void onClick() {
        Log.e("EVENT", "question click");
    }

}
