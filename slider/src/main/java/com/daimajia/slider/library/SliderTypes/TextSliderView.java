package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView {
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text, null);
        RoundedImageView target = (RoundedImageView) v.findViewById(R.id.daimajia_slider_image);
        TextView textView_name = (TextView) v.findViewById(R.id.textViewName_render_type_text);
        textView_name.setText(getName());
        bindEventAndShow(v, target);
        return v;
    }
}
