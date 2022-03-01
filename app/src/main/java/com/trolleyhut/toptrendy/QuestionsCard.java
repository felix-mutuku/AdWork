package com.trolleyhut.toptrendy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

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
    private TextView questionTextView;
    @SwipeView
    private android.view.View cardView;

    private Questions mQuestions;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private Activity mactivity;

    public QuestionsCard(Context context, Questions questions, SwipePlaceHolderView swipeView,
                         Activity activity) {
        mContext = context;
        mQuestions = questions;
        mSwipeView = swipeView;
        mactivity = activity;
    }

    @Resolve
    private void onResolved() {

//        MultiTransformation multi = new MultiTransformation(
//                new BlurTransformation(mContext, 30),
//                new RoundedCornersTransformation(
//                        mContext, Utils.dpToPx(7), 0,
//                        RoundedCornersTransformation.CornerType.TOP));
        //Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);

        //set data into the cards
        questionTextView.setText(mQuestions.getQuestion());
    }

    @SwipeOut
    private void onSwipedOut() {
        //red light
        Log.e("EVENT", "onSwipedOut");
        //mSwipeView.addView(this);

        //check internet connection
        Constants.checkInternet(mactivity);

        //add point and show ad after
        Constants.addAPoint(mactivity);
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.e("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn() {
        Log.e("EVENT", "onSwipedIn");
        // mSwipeView.addView(this);

        //check internet connection
        Constants.checkInternet(mactivity);

        //add point and show ad after
        Constants.addAPoint(mactivity);
    }

    @SwipeInState
    private void onSwipeInState() {
        Log.e("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState() {
        Log.e("EVENT", "onSwipeOutState");
    }

    @SwipeInDirectional
    private void onSwipeInDirectional() {
        //green light
        Log.e("EVENT", "onSwipedInDirectional");
        //mSwipeView.addView(this);

        //check internet connection
        Constants.checkInternet(mactivity);

        //add point and show ad after
        Constants.addAPoint(mactivity);
    }

    @SwipeOutDirectional
    private void onSwipedOutDirectional() {
        //red light
        Log.e("EVENT", "onSwipedOutDirectional");
        //mSwipeView.addView(this);

        //check internet connection
        Constants.checkInternet(mactivity);

        //add point and show ad after
        Constants.addAPoint(mactivity);
    }

    @Click(R.id.question)
    private void onClick() {
        Log.e("EVENT", "question click");
    }

}
