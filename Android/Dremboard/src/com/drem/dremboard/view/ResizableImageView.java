package com.drem.dremboard.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class ResizableImageView extends ImageView {

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
         Drawable d = getDrawable();

         if(d!=null){
        	 float imageSideRatio = (float)d.getIntrinsicWidth() / (float)d.getIntrinsicHeight();
             float viewSideRatio = (float)MeasureSpec.getSize(widthMeasureSpec) / (float)MeasureSpec.getSize(heightMeasureSpec);
             if (imageSideRatio >= viewSideRatio || heightMeasureSpec == 0) {
                 // Image is wider than the display (ratio)
                 int width = MeasureSpec.getSize(widthMeasureSpec);
                 int height = (int)(width / imageSideRatio);
                 setMeasuredDimension(width, height);
             } else {
                 // Image is taller than the display (ratio)
                 int height = MeasureSpec.getSize(heightMeasureSpec);
                 int width = (int)(height * imageSideRatio);
                 setMeasuredDimension(width, height);
             }
            
             
//                 // ceil not round - avoid thin vertical gaps along the left/right edges
//                 int width = MeasureSpec.getSize(widthMeasureSpec);
//                 int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
//                 
//                 
//                 
//                 setMeasuredDimension(width, height);
         }else{
                 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         }
    }

}
