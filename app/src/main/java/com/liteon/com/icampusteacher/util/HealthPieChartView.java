package com.liteon.com.icampusteacher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.liteon.com.icampusteacher.App;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.util.HealthHistogramView.OnHistogramChangeListener;
import com.liteon.com.icampusteacher.util.HealthyItem.TYPE;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HealthPieChartView extends View implements OnHistogramChangeListener{

	//Text size for graph
	private int mWidth;
	private int mHeight;
	private int textFontSize;
	private int textTargetSize;
	private Paint textPaint;
	private Paint textTargetPaint;
	private Paint arcPaintBackground;
	private Paint arcPaintPrimary;
	private Paint arcClockPaint;
	private Paint arcClockMinPaint;
	private Paint arcClockBackGroundPaint;
	private HealthyItem.TYPE mType;
	private int mTargetValue;
	private int mPieChartSize;
	private int mTypeIconSize;
	private Bitmap mBackground;
	private Bitmap mTypeIcon;
	private int mTypeIconLocationY;
	private int mTargetOffsetY;
	private int mDateOffsetY;
	private int mCurrentValue;
	private String mCurrentDate = "2017/06/20";
	private String mSettingTarget;
	private TargetItem mCurrentTargetItem;
	private Map<String, TargetItem> mTargetMap;
	//private List<JSONResponse.Student> mStudents;
	private int mCurrentStudentIdx;
	//private DBHelper mDbHelper;

	public HealthPieChartView(Context context) {
		super(context);
	}

	public HealthPieChartView(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.circularImageViewStyle);
	}

	public HealthPieChartView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	private void init(Context context, AttributeSet attrs, int defStyle)
	{
		// Initialize paint objects
		mCurrentValue = 0;
		//Text size
		textFontSize = getResources().getDimensionPixelSize(R.dimen.healthy_detail_histogram_font_size);
		textTargetSize = getResources().getDimensionPixelSize(R.dimen.healthy_detail_pie_chart_target_font_size);
		//Pie chart size
		mPieChartSize = getResources().getDimensionPixelSize(R.dimen.healthy_detail_pie_chart_size);
		//type icon size
		mTypeIconSize = getResources().getDimensionPixelSize(R.dimen.healthy_detail_pie_chart_icon_size);
		//type icon location Y
		mTypeIconLocationY = getResources().getDimensionPixelSize(R.dimen.healthy_detail_pie_chart_icon_location_y);
		//Target location offset from center Y
		mTargetOffsetY = getResources().getDimensionPixelSize(R.dimen.healthy_detail_pie_chart_target_offset_y);
		//Date location offset from center Y
		mDateOffsetY = getResources().getDimensionPixelSize(R.dimen.healthy_detail_pie_chart_date_offset_y);
		//Paint for text
		textPaint = new Paint();  
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(textFontSize);
		textPaint.setAntiAlias(true);
		textPaint.setStrokeCap(Cap.ROUND);
		textPaint.setStrokeWidth(1);
		//Paint for Target text
		textTargetPaint = new Paint();
		textTargetPaint.setColor(getResources().getColor(R.color.md_amber_700));
		textTargetPaint.setTextSize(textTargetSize);
		textTargetPaint.setAntiAlias(true);
		
		//for draw arc
		arcPaintBackground = new Paint(); 
		arcPaintBackground.setDither(true);
		arcPaintBackground.setStyle(Paint.Style.STROKE);
		arcPaintBackground.setStrokeCap(Cap.BUTT);
		arcPaintBackground.setStrokeJoin(Join.BEVEL);
		arcPaintBackground.setColor(getResources().getColor(R.color.md_grey_400));
		arcPaintBackground.setStrokeWidth(25);
		arcPaintBackground.setAntiAlias(true);
		  
		arcPaintPrimary = new Paint(); 
		arcPaintPrimary.setDither(true);
		arcPaintPrimary.setStyle(Paint.Style.STROKE);
		arcPaintPrimary.setStrokeCap(Cap.BUTT);
		arcPaintPrimary.setStrokeJoin(Join.BEVEL);
		arcPaintPrimary.setColor(getResources().getColor(R.color.md_amber_700));
		arcPaintPrimary.setStrokeWidth(25);
		arcPaintPrimary.setAntiAlias(true);
		//Paint for sleep
		arcClockPaint = new Paint();
		arcClockPaint.setDither(true);
		arcClockPaint.setStyle(Paint.Style.STROKE);
		arcClockPaint.setStrokeCap(Cap.BUTT);
		arcClockPaint.setColor(getResources().getColor(R.color.md_grey_500));
		arcClockPaint.setStrokeWidth(40);
		arcClockPaint.setAntiAlias(true);
		float radius = (float)(mPieChartSize * Math.PI / 12.f);
		float[] intervals = new float[] {14.0f, radius - 14.0f};
		float phase = 0.f;
		DashPathEffect effects = new DashPathEffect(intervals, phase);  
		arcClockPaint.setPathEffect(effects);  
		
		arcClockMinPaint = new Paint();
		arcClockMinPaint.setDither(true);
		arcClockMinPaint.setStyle(Paint.Style.STROKE);
		arcClockMinPaint.setStrokeCap(Cap.ROUND);
		arcClockMinPaint.setColor(getResources().getColor(R.color.md_grey_500));
		arcClockMinPaint.setStrokeWidth(10);
		arcClockMinPaint.setAntiAlias(true);
		radius = (float)(mPieChartSize * Math.PI / 60.f);
		intervals = new float[] { 0.f , radius + 1.f,
											1.f , radius - 1.0f,
											1.f , radius - 1.0f,
											1.f , radius - 1.0f,
											1.f , radius - 1.0f};
		phase = 0.f;
		DashPathEffect effects2 = new DashPathEffect(intervals, phase);  
		arcClockMinPaint.setPathEffect(effects2);  
		
		arcClockBackGroundPaint = new Paint(); 
		arcClockBackGroundPaint.setDither(true);
		arcClockBackGroundPaint.setStyle(Paint.Style.STROKE);
		arcClockBackGroundPaint.setStrokeCap(Cap.ROUND);
		arcClockBackGroundPaint.setColor(getResources().getColor(R.color.md_deep_purple_A700));
		arcClockBackGroundPaint.setStrokeWidth(60);
		arcClockBackGroundPaint.setAntiAlias(true);

		//mDbHelper = DBHelper.getInstance(getContext());
		//get child list
		//mStudents = mDbHelper.queryChildList(mDbHelper.getReadableDatabase());
		restoreTarget();
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
	    mWidth = MeasureSpec.getSize(widthMeasureSpec);
	    mHeight = MeasureSpec.getSize(heightMeasureSpec);

	    setMeasuredDimension(mWidth, mHeight);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		switch(mType) {
		case ACTIVITY:
			onDrawActivity(canvas);
			break;
		case CALORIES_BURNED:
			onDrawCalories(canvas);
			break;
		case CYCLING_TIME:
			onDrawCycling(canvas);
			break;
		case HEART_RATE:
			onDrawHeartRate(canvas);
			break;
		case RUNNING_TIME:
			onDrawRunning(canvas);
			break;
		case SLEEP_TIME:
			onDrawSleeping(canvas);
			break;
		case TOTAL_STEPS:
			onDrawSteps(canvas);
			break;
		case WALKING_TIME:
			onDrawWalking(canvas);
			break;
		default:
			break;
		}			
	}

	private void onDrawWalking(Canvas canvas) {
		if (mTypeIcon == null) {
			mTypeIcon = getBitmap(R.drawable.health_img_walk, mTypeIconSize, mTypeIconSize);
		}
		
		int cx = (mWidth - mTypeIcon.getWidth()) >> 1; // same as (...) / 2
	    int cy = mTypeIconLocationY;//(mHeight - mTypeIcon.getHeight()) >> 1;
	    canvas.drawBitmap(mTypeIcon, cx, cy, null);
	    
	    cx = (mWidth - mPieChartSize) >> 1; // same as (...) / 2
	    cy = (mHeight - mPieChartSize) >> 1;
	    RectF rect = new RectF(cx, cy, cx + mPieChartSize, cy + mPieChartSize);

		// background full circle arc
        canvas.drawArc(rect, 270, 360, false, arcPaintBackground);

        // draw starting at top of circle in the clockwise direction
        canvas.drawArc(rect, 270, (360 * ((float) mCurrentValue / mTargetValue)), false, arcPaintPrimary);
        
        drawCenter(canvas, textTargetPaint, Integer.toString(mCurrentValue) + " " + getResources().getString(R.string.healthy_minutes), mTargetOffsetY);
        drawCenter(canvas, textPaint, mCurrentDate, mDateOffsetY);			
	}

	private void onDrawSteps(Canvas canvas) {
		if (mTypeIcon == null) {
			mTypeIcon = getBitmap(R.drawable.health_img_step, mTypeIconSize, mTypeIconSize);
		}
		
		int cx = (mWidth - mTypeIcon.getWidth()) >> 1; // same as (...) / 2
	    int cy = mTypeIconLocationY;//(mHeight - mTypeIcon.getHeight()) >> 1;
	    canvas.drawBitmap(mTypeIcon, cx, cy, null);
	    
	    cx = (mWidth - mPieChartSize) >> 1; // same as (...) / 2
	    cy = (mHeight - mPieChartSize) >> 1;
	    RectF rect = new RectF(cx, cy, cx + mPieChartSize, cy + mPieChartSize);

		// background full circle arc
        canvas.drawArc(rect, 270, 360, false, arcPaintBackground);

        // draw starting at top of circle in the clockwise direction
        canvas.drawArc(rect, 270, (360 * ((float) mCurrentValue / mTargetValue)), false, arcPaintPrimary);
        
        drawCenter(canvas, textTargetPaint, Integer.toString(mCurrentValue) + " " + getResources().getString(R.string.healthy_step), mTargetOffsetY);
        drawCenter(canvas, textPaint, mCurrentDate, mDateOffsetY);		
	}

	private void onDrawSleeping(Canvas canvas) {
		if (mTypeIcon == null) {
			mTypeIcon = getBitmap(R.drawable.health_img_sleep, mTypeIconSize, mTypeIconSize);
		}
		
        int cx = (mWidth - mTypeIcon.getWidth()) >> 1; // same as (...) / 2
	    int cy = mTypeIconLocationY;//(mHeight - mTypeIcon.getHeight()) >> 1;
	    canvas.drawBitmap(mTypeIcon, cx, cy, null);
	    
	    cx = (mWidth - mPieChartSize) >> 1; // same as (...) / 2
	    cy = (mHeight - mPieChartSize) >> 1;
	    RectF rect = new RectF(cx, cy, cx + mPieChartSize, cy + mPieChartSize);

		// background full circle arc
	    canvas.drawArc(rect, 270, (360 * ((float) mCurrentValue / mTargetValue)), false, arcClockBackGroundPaint);
        //canvas.drawArc(rect, 270, 360, false, arcClockPaint);
        //canvas.drawArc(rect, 270, 360, false, arcClockMinPaint);
        if (mBackground == null) {
            mBackground = getBitmap(R.drawable.health_img_sleep0scale1, mPieChartSize + 65, mPieChartSize + 65);
        }

        cx = (mWidth - mBackground.getWidth()) >> 1;
        cy = (mHeight - mBackground.getHeight()) >> 1;

        canvas.drawBitmap(mBackground, cx, cy, null);

        SimpleDateFormat sdf = new SimpleDateFormat(App.getContext().getString(R.string.healthy_hour_mins));
		long hours = TimeUnit.MINUTES.toHours(mCurrentValue);
		long remainMinute = mCurrentValue - TimeUnit.HOURS.toMinutes(hours);
		Calendar d = Calendar.getInstance();
		d.set(Calendar.HOUR, (int)hours);
		d.set(Calendar.MINUTE, (int)remainMinute);

		String output = sdf.format(d.getTime());

        drawCenter(canvas, textTargetPaint, output, mTargetOffsetY);
        drawCenter(canvas, textPaint, mCurrentDate, mDateOffsetY + 100);
        sdf.applyPattern("yyyy/MM/dd");
        Date origin = null;
        try {
            origin = sdf.parse(mCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (origin != null) {
            Calendar d1 = Calendar.getInstance();
            d1.setTime(origin);
            Calendar d2 = Calendar.getInstance();
            d2.setTimeInMillis(d1.getTimeInMillis());
            d2.add(Calendar.MINUTE, mCurrentValue);
            sdf.applyPattern("MM/dd HH:mm");
            String sleep = sdf.format(d1.getTime());
            String wake = sdf.format(d2.getTime());
            canvas.drawText(getContext().getString(R.string.healthy_sleep), (float) (mWidth * 0.35), (float) (mHeight * 0.65) , textPaint);
			canvas.drawText(getContext().getString(R.string.healthy_wake), (float) (mWidth * 0.35), (float) (mHeight * 0.72), textPaint);
			textPaint.setColor(getResources().getColor(R.color.color_sleep));
			canvas.drawText(sleep, (float) (mWidth * 0.45), (float) (mHeight * 0.65) , textPaint);
			canvas.drawText(wake, (float) (mWidth * 0.45), (float) (mHeight * 0.72), textPaint);
			textPaint.setColor(getResources().getColor(android.R.color.black));

        }
        if (mCurrentValue >= 320) {
            canvas.drawText(getResources().getString(R.string.sleep_quality_good), (float) (mWidth * 0.75), (float) (mHeight * 0.9), textTargetPaint);
        } else if (mCurrentValue >=160) {
            canvas.drawText(getResources().getString(R.string.sleep_quality_normal), (float) (mWidth * 0.75), (float) (mHeight * 0.9), textTargetPaint);
        } else {
            canvas.drawText(getResources().getString(R.string.sleep_quality_bad), (float) (mWidth * 0.75), (float) (mHeight * 0.9), textTargetPaint);
        }
    }

	private void onDrawRunning(Canvas canvas) {
		if (mTypeIcon == null) {
			mTypeIcon = getBitmap(R.drawable.health_img_run, mTypeIconSize, mTypeIconSize);
		}
		
		int cx = (mWidth - mTypeIcon.getWidth()) >> 1; // same as (...) / 2
	    int cy = mTypeIconLocationY;//(mHeight - mTypeIcon.getHeight()) >> 1;
	    canvas.drawBitmap(mTypeIcon, cx, cy, null);
	    
	    cx = (mWidth - mPieChartSize) >> 1; // same as (...) / 2
	    cy = (mHeight - mPieChartSize) >> 1;
	    RectF rect = new RectF(cx, cy, cx + mPieChartSize, cy + mPieChartSize);

		// background full circle arc
        canvas.drawArc(rect, 270, 360, false, arcPaintBackground);

        // draw starting at top of circle in the clockwise direction
        canvas.drawArc(rect, 270, (360 * ((float) mCurrentValue / mTargetValue)), false, arcPaintPrimary);
        
        drawCenter(canvas, textTargetPaint, Integer.toString(mCurrentValue) + " " + getResources().getString(R.string.healthy_minutes), mTargetOffsetY);
        drawCenter(canvas, textPaint, mCurrentDate, mDateOffsetY);			
	}

	private void onDrawHeartRate(Canvas canvas) {
		if (mBackground == null) {
			int resId = R.drawable.activity_gnd_heartbeat;
			mBackground = getBitmap(resId, mPieChartSize, mPieChartSize);
		}
		if (mTypeIcon == null) {
			mTypeIcon = getBitmap(R.drawable.health_img_heartbeat, mTypeIconSize, mTypeIconSize);
		}
		
		int cx = (mWidth - mBackground.getWidth()) >> 1; 
	    int cy = (mHeight - mBackground.getHeight()) >> 1;
	    
	    canvas.drawBitmap(mBackground, cx, cy, null);
	    
		cx = (mWidth - mTypeIcon.getWidth()) >> 1;
	    cy = mTypeIconLocationY;
	    canvas.drawBitmap(mTypeIcon, cx, cy, null);
        
        drawCenter(canvas, textTargetPaint, Integer.toString(mCurrentValue) + " " + getResources().getString(R.string.healthy_bpm), mTargetOffsetY);
        drawCenter(canvas, textPaint, mCurrentDate, mDateOffsetY);			
	}

	private void onDrawCycling(Canvas canvas) {
		if (mTypeIcon == null) {
			mTypeIcon = getBitmap(R.drawable.health_img_bicycle, mTypeIconSize, mTypeIconSize);
		}
		
		int cx = (mWidth - mTypeIcon.getWidth()) >> 1; // same as (...) / 2
	    int cy = mTypeIconLocationY;//(mHeight - mTypeIcon.getHeight()) >> 1;
	    canvas.drawBitmap(mTypeIcon, cx, cy, null);
	    
	    cx = (mWidth - mPieChartSize) >> 1; // same as (...) / 2
	    cy = (mHeight - mPieChartSize) >> 1;
	    RectF rect = new RectF(cx, cy, cx + mPieChartSize, cy + mPieChartSize);

		// background full circle arc
        canvas.drawArc(rect, 270, 360, false, arcPaintBackground);

        // draw starting at top of circle in the clockwise direction
        canvas.drawArc(rect, 270, (360 * ((float) mCurrentValue / mTargetValue)), false, arcPaintPrimary);
        
        drawCenter(canvas, textTargetPaint, Integer.toString(mCurrentValue) + " " + getResources().getString(R.string.healthy_minutes), mTargetOffsetY);
        drawCenter(canvas, textPaint, mCurrentDate, mDateOffsetY);			
	}
	private static int currentResId;
	private void onDrawActivity(Canvas canvas) {
		float ratio = ((float) mCurrentValue / mTargetValue) * 100;
		int resId;
		if (ratio >= 80) {
			resId = R.drawable.activity_img_scale5;
		} else if (ratio >= 60) {
			resId = R.drawable.activity_img_scale4;
		} else if (ratio >= 40) {
			resId = R.drawable.activity_img_scale3;
		} else if (ratio >= 20) {
			resId = R.drawable.activity_img_scale2;
		} else if (ratio > 0) {
			resId = R.drawable.activity_img_scale1;
		} else {
			resId = R.drawable.activity_img_scale;
		}
		if (mBackground == null || resId != currentResId) {
			mBackground = getBitmap(resId, mPieChartSize, mPieChartSize);
			currentResId = resId;
		}
		
		if (mTypeIcon == null) {
			mTypeIcon = getBitmap(R.drawable.health_img_activity, mTypeIconSize, mTypeIconSize);
		}
		int cx = (mWidth - mBackground.getWidth()) >> 1; 
	    int cy = (mHeight - mBackground.getHeight()) >> 1;
	    
	    canvas.drawBitmap(mBackground, cx, cy, null);
		
	    cx = (mWidth - mTypeIcon.getWidth()) >> 1;
	    cy = mTypeIconLocationY;
	    canvas.drawBitmap(mTypeIcon, cx, cy, null);
	    
	    drawCenter(canvas, textTargetPaint, Integer.toString(mCurrentValue), mTargetOffsetY);
	    
	    drawCenter(canvas, textPaint, mCurrentDate, mDateOffsetY);
	    
	    canvas.drawText(getResources().getString(R.string.activity_very_bad), (float) (mWidth * 0.75), (float) (mHeight * 0.2), textPaint);
	    canvas.drawText(getResources().getString(R.string.activity_excellent), (float) (mWidth * 0.12), (float) (mHeight * 0.2), textPaint);
	    canvas.drawText(getResources().getString(R.string.activity_good), (float) (mWidth * 0.12), (float) (mHeight * 0.7), textPaint);
	    canvas.drawText(getResources().getString(R.string.activity_bad), (float) (mWidth * 0.8), (float)(mHeight * 0.7), textPaint);
	    canvas.drawText(getResources().getString(R.string.activity_normal), (float)(mWidth * 0.45), (float)(mHeight * 0.97), textPaint);
	}
	
	private void onDrawCalories(Canvas canvas) {
		if (mTypeIcon == null) {
			mTypeIcon = getBitmap(R.drawable.health_img_calories, mTypeIconSize, mTypeIconSize);
		}
		
		int cx = (mWidth - mTypeIcon.getWidth()) >> 1; // same as (...) / 2
	    int cy = mTypeIconLocationY;//(mHeight - mTypeIcon.getHeight()) >> 1;
	    canvas.drawBitmap(mTypeIcon, cx, cy, null);
	    
	    cx = (mWidth - mPieChartSize) >> 1; // same as (...) / 2
	    cy = (mHeight - mPieChartSize) >> 1;
	    RectF rect = new RectF(cx, cy, cx + mPieChartSize, cy + mPieChartSize);

		// background full circle arc
        canvas.drawArc(rect, 270, 360, false, arcPaintBackground);

        // draw starting at top of circle in the clockwise direction
        canvas.drawArc(rect, 270, (360 * ((float) mCurrentValue / mTargetValue)), false, arcPaintPrimary);
        
        drawCenter(canvas, textTargetPaint, Integer.toString(mCurrentValue) + " " + getResources().getString(R.string.healthy_cal), mTargetOffsetY);
        drawCenter(canvas, textPaint, mCurrentDate, mDateOffsetY);
	}
	
	private Bitmap getBitmap(int resId, int requestWidth, int requestHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), resId, options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		String imageType = options.outMimeType;
		
		final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > requestHeight || width > requestWidth) {

	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;

	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) >= requestHeight
	                && (halfWidth / inSampleSize) >= requestWidth) {
	            inSampleSize *= 2;
	        }
	    }
	    options.inSampleSize = inSampleSize;
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId, options);
	    return Bitmap.createScaledBitmap(bitmap, requestWidth, requestHeight, false);
		
	}
	
	private Rect drawCenter(Canvas canvas, Paint paint, String text, int yOffset) {
		Rect r = new Rect();
		canvas.getClipBounds(r);
	    int cHeight = r.height();
	    int cWidth = r.width();
	    paint.setTextAlign(Paint.Align.LEFT);
	    paint.getTextBounds(text, 0, text.length(), r);
	    float x = cWidth / 2f - r.width() / 2f - r.left;
	    float y = cHeight / 2f + r.height() / 2f - r.bottom + yOffset;
	    canvas.drawText(text, x, y, paint);
	    return r;
	}
	
	public void setDate(String date) {
		mCurrentDate = date;
	}
	
	public void setValue(int value) {
		mCurrentValue = value;
	}
	
	public void setType(TYPE type) {
		mType = type;
		arcPaintPrimary.setColor(getResources().getColor(type.getColorId()));
		textTargetPaint.setColor(getResources().getColor(type.getColorId()));
		mSettingTarget = getTarget();
		mTargetValue = !TextUtils.isEmpty(mSettingTarget) ? Integer.parseInt(mSettingTarget) : 80;
        if (type == TYPE.SLEEP_TIME) {
            mTargetValue *= 60;
        }
	}
	
	public void setTargetValue(int targetValue) {
		this.mTargetValue = targetValue;
	}

	@Override
	public void onHistogramChanged(int idx, int value, String date) {
		mCurrentValue = value;
		mCurrentDate = date;
		invalidate();
	}

	private void restoreTarget() {
//		SharedPreferences sp = getContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
//		String targetMap = sp.getString(Def.SP_TARGET_MAP, "");
//		mCurrentStudentIdx = sp.getInt(Def.SP_CURRENT_STUDENT, 0);
//		Type typeOfHashMap = new TypeToken<Map<String, TargetItem >>() { }.getType();
//		Gson gson = new GsonBuilder().create();
//		mTargetMap = gson.fromJson(targetMap, typeOfHashMap);
//		if (TextUtils.isEmpty(targetMap)) {
//			mTargetMap = new HashMap<String, TargetItem>();
//			for (JSONResponse.Student student : mStudents) {
//				String studentId = student.getStudent_id();
//				TargetItem item = new TargetItem();
//				item.setCarlos("2000");
//				item.setStep("10000");
//				item.setWalking("30");
//				item.setRunning("30");
//				item.setCycling("30");
//				item.setSleep("9");
//				mTargetMap.put(studentId, item);
//			}
//		}
//		if (mTargetMap.get(mStudents.get(mCurrentStudentIdx).getStudent_id()) == null) {
//			TargetItem item = new TargetItem();
//			item.setCarlos("2000");
//			item.setStep("10000");
//			item.setWalking("30");
//			item.setRunning("30");
//			item.setCycling("30");
//			item.setSleep("9");
//			mTargetMap.put(mStudents.get(mCurrentStudentIdx).getStudent_id(), item);
//		}
//		mCurrentTargetItem = mTargetMap.get(mStudents.get(mCurrentStudentIdx).getStudent_id());
		TargetItem item = new TargetItem();
		item.setCarlos("2000");
		item.setStep("10000");
		item.setWalking("30");
		item.setRunning("30");
		item.setCycling("30");
		item.setSleep("9");
		mCurrentTargetItem = item;
	}

	private String getTarget() {
		String target = "";
		switch(mType) {
			case ACTIVITY:
				target = "80";
				break;
			case CALORIES_BURNED:
				target = mCurrentTargetItem.getCarlos();
				break;
			case CYCLING_TIME:
				target = mCurrentTargetItem.getCycling();
				break;
			case HEART_RATE:
				target = "80";
				break;
			case RUNNING_TIME:
				target = mCurrentTargetItem.getRunning();
				break;
			case SLEEP_TIME:
				target = mCurrentTargetItem.getSleep();
				break;
			case TOTAL_STEPS:
				target = mCurrentTargetItem.getStep();
				break;
			case WALKING_TIME:
				target = mCurrentTargetItem.getWalking();
				break;
			default:
				break;
		}
		return target;
	}
}
