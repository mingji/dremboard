package com.drem.dremboard.view;


import com.drem.dremboard.R;
import com.drem.dremboard.utils.ImageProcessingCallback;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class WebCircularImgView extends RelativeLayout implements ImageProcessingCallback {

//	public ImageView imageView;
	private ProgressBar viewProgress;
	public CircularImageView imageView;
	public boolean flagShowProg = true;
	public Bitmap failedBp = null;

	public WebCircularImgView(Context context) {
		super(context);
		init();
	}

	public WebCircularImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.webcircularimgview, this);
//		imageView = (ImageView) findViewById(R.id.id_imageview_bitmap);
		imageView = (CircularImageView) findViewById(R.id.id_imageview_bitmap);
		viewProgress = (ProgressBar) findViewById(R.id.id_imageview_progress);

	}

	@Override
	public void onImagePreProcessing() {
		//imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bg_default));
		if (flagShowProg == true)
			this.viewProgress.setVisibility(View.VISIBLE);
		else
			this.viewProgress.setVisibility(View.GONE);
		this.invalidate();
	}

	@Override
	public void onImageProcessing(Bitmap bitmap, int w, int h) {
		this.viewProgress.setVisibility(View.GONE);
		imageView.setImageBitmap(bitmap);
		this.invalidate();
	}
	
	@Override
	public Bitmap onImageFailedLoad()
	{
		return failedBp;
	}
}
