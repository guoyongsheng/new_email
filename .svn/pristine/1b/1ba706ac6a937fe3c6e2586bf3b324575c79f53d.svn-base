package com.zfsoft.zf_new_email.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zfsoft.zf_new_email.R;

/**
 * Created by wesley on 2016/10/12.
 */
public class GlideUtils {

	private GlideUtils() {

	}

	/**
	 * 加载图片
	 * 
	 * @param path
	 *            图片路径 /storage/emulated/0/tempCropped.jpeg
	 * @param imageView
	 *            控件
	 * @param context
	 *            上下文对象
	 */
	public static void loadImage(String path, ImageView imageView, Context context) {
		Glide.with(context).load(path).centerCrop().placeholder(R.drawable.doc_lager).into(imageView);
	}
}
