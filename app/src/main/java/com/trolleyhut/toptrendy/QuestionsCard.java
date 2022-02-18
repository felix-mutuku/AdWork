package com.trolleyhut.toptrendy;


import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

@Layout(R.layout.question_card_view)
public class QuestionsCard {
    @View(R.id.question)
    TextView questionTextView;

    Questions mQuestions;
    Context mContext;
    SwipePlaceHolderView mSwipeView;

    public QuestionsCard(Context context, Questions questions, SwipePlaceHolderView swipeView) {
        mContext = context;
        mQuestions = questions;
        mSwipeView = swipeView;
    }

    @Resolve
    public void onResolved() {
        //Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        questionTextView.setText(mQuestions.getQuestion());
    }

    @SwipeOut
    public void onSwipedOut() {
        //red light
        Log.e("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    public void onSwipeCancelState() {
        Log.e("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    public void onSwipeIn() {
        //green light
        Log.e("EVENT", "onSwipedIn");
        mSwipeView.addView(this);
    }

    @SwipeInState
    public void onSwipeInState() {
        Log.e("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState() {
        Log.e("EVENT", "onSwipeOutState");
    }
}
