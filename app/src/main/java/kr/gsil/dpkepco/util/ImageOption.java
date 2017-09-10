package kr.gsil.dpkepco.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageOption {

	public static ImageOption _instance = null;

	public static ImageOption getInstnace(){

		if ( _instance != null ) return _instance;

		return _instance = new ImageOption();
	}

	public DisplayImageOptions getRoundImageOptions()
	{
		return new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.profile_default_235)
        .resetViewBeforeLoading()
        .cacheInMemory()
        .cacheOnDisc()
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
        .displayer(new RoundedBitmapDisplayer(10))
        .build();
	}

	public DisplayImageOptions getRoundImageOptions2()
	{
		return new DisplayImageOptions.Builder()
        .cacheInMemory()
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
        .displayer(new RoundedBitmapDisplayer(5))
        .build();
	}

	public DisplayImageOptions getAdjustBoundFitOptions(){
		return new DisplayImageOptions.Builder()
		.resetViewBeforeLoading()
		.cacheInMemory()
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.build();
	}

	//image loader ?��?��브러�? ?��?��?��?��(1.7.0 to 1.8.2)�? ?��?�� 2013.03.20
	public ImageLoadingListener getImageLoadingListener(final ImageView imageView){

		return new ImageLoadingListener() {

			@Override
		    public void onLoadingStarted(String imageUri, View view) {}

			@Override
		    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				imageView.startAnimation(AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_in));
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub

			}
		};
	}
	
	public ImageLoadingListener getImageLoadingListener(final ImageView imageView,final Context context){

		return new ImageLoadingListener() {

			@Override
		    public void onLoadingStarted(String imageUri, View view) {}

			@Override
		    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				imageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub

			}
		};
	}
	
	public String checkImgUri(String imgUri, String altImgUri, String imgOwner)
	{
		if(imgUri==null || "".equals(imgUri) || "null".equals(imgUri)) {
			imgUri = altImgUri;
		}
		return imgUri;
	}

}
