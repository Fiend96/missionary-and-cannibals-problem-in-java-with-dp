package edu.neu.mcquestion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import darkness.Drive;
import darkness.State;

import static edu.neu.mcquestion.R.mipmap.bg;
import static edu.neu.mcquestion.R.mipmap.boat;

/**
 * MC问题的呈现View
 *
 * @author lihanguang
 */

public class MCView extends View {
//    private static final String TAG = "MCView";
    private State mState;//原始状态
    private List<Drive> mDrives;//执行路径
    private Drive currentDrive;//当前船的状态
    private int index;// 执行路径的索引

    private int width;
    private int height;
    //画传教士
    private Paint mPaint;
    //画野人
    private Paint cPaint;
    //画船
    private Paint bPaint;
    //画背景
    private Paint backgroundPaint;
    //画数字
    private Paint nPaint;
    private Bitmap bgBitmap; // 背景
    private Bitmap boatBitmap;// 船
    private Bitmap yeBitmap;// 野人
    private Bitmap chuanBitmap; // 传教士

    //人的宽高
    private int peopleWidth;
    private int peopleHeight;

    //船的宽高
    private int boatWidth;
    private int boatHeight;
    private int boatLeft;//船的最左位置
    private int boatRight;// 船的最右位置

    private RectF bgRectF; // 背景大小
    private RectF boatRectF; // 船大小
    private RectF mRightRectF;//右边传教士位置
    private RectF mLeftRectF;//左边传教士位置
    private RectF cRightRectF;//右边野人位置
    private RectF cLeftRectF;//左边野人位置

    //人的数量
    private int cLeftNumber;
    private int mLeftNumber;
    private int cRightNumber;
    private int mRightNumber;
    private int cBoatNumber;
    private int mBoatNumber;

    private int mode;
    private static final int RIGHT = 1; //船在右边
    private static final int BOAT_LEFT = 2; //船在左边运行
    private static final int BOAT_RIGHT = 3; //船在右边运行
    private static final int LEFT = 4; //船在左边
    private static final int OVER = 5; //结束

    private static final int BOAT_TIME = 10;
    private static final int PEOPLE_TIME = 300;
    private static final int START_TIME = 1000;
    private static final int DISTANCE_BOAT = 3;

    public MCView(Context context) {
        super(context);
        init();
    }

    public MCView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MCView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void initWidthAndHeight(int width, int height) {
        peopleWidth = width / 15;
        peopleHeight = height / 3;

        boatWidth = width / 4;
        boatHeight = height / 5;

        boatLeft = width / 4;
        boatRight = width / 2;
    }

    private void init() {
        initWidthAndHeight(width, height);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);

        bPaint = new Paint();
        bPaint.setColor(Color.WHITE);

        cPaint = new Paint();
        cPaint.setColor(Color.WHITE);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);

        nPaint = new Paint();
        nPaint.setColor(Color.BLACK);
        nPaint.setTextSize((float) (0.5 * peopleWidth));
        nPaint.setTextAlign(Paint.Align.CENTER);
        nPaint.setStyle(Paint.Style.STROKE);
        bgBitmap = BitmapFactory
                .decodeResource(getResources(), bg);

        boatBitmap = BitmapFactory
                .decodeResource(getResources(), boat);

        yeBitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.ye);

        chuanBitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.chuan);
        bgRectF = new RectF(0, 0, width, height);
        boatRectF = new RectF(boatLeft, height - boatHeight - 60,
                boatLeft + boatWidth, height - 60);

        //野人最右边
        cRightRectF = new RectF(width - 50 - peopleWidth, height - peopleHeight - 60,
                width - 50, height - 60);
        //传教士比野人差一个身位
        mRightRectF = new RectF(cRightRectF.left - peopleWidth,
                height - peopleHeight - 60 - 30,
                cRightRectF.left, height - 60 - 30);
        cLeftRectF = new RectF(30, height - peopleHeight - 60,
                30 + peopleWidth, height - 60);
        mLeftRectF = new RectF(cLeftRectF.left + peopleWidth,
                height - peopleHeight - 60 - 30, cLeftRectF.left + 2 * peopleWidth,
                height - 60 - 30);

    }

    public void start(List<Drive> drives, State state) {
        this.mState = state;
        this.mDrives = drives;
        currentDrive = null;
        index = 0;
        mode = LEFT;
        mLeftNumber = mState.ml;
        mRightNumber = 0;
        mBoatNumber = 0;

        cLeftNumber = mState.cl;
        cRightNumber = 0;
        cBoatNumber = 0;

        //船在最左边
        boatRectF = new RectF(boatLeft, height - boatHeight - 60,
                boatLeft + boatWidth, height - 60);

        postInvalidateDelayed(START_TIME);
    }


    private void next() {
        switch (mode) {
            case LEFT:
                //上岸
                if (currentDrive != null) {
                    mState = mState.in(currentDrive.ml, currentDrive.cl);
                    currentDrive = null;
                    mBoatNumber = 0;
                    mLeftNumber = mState.ml;
                    mRightNumber = State.N - mState.ml;

                    cBoatNumber = 0;
                    cLeftNumber = mState.cl;
                    cRightNumber = State.N - mState.cl;
                    index++;//下一个路径
                    if (index >= mDrives.size()) {
                        //结束
                        mode = OVER;
                    }
                    postInvalidateDelayed(PEOPLE_TIME);
                    break;
                }

                //上船
                mode = BOAT_LEFT;
                currentDrive = mDrives.get(index);
                mBoatNumber = currentDrive.ml;
                mLeftNumber = mState.ml - mBoatNumber;
                mRightNumber = State.N - mState.ml;

                cBoatNumber = currentDrive.cl;
                cLeftNumber = mState.cl - cBoatNumber;
                cRightNumber = State.N - mState.cl;

                //船在最左边
                boatRectF = new RectF(boatLeft, height - boatHeight - 60,
                        boatLeft + boatWidth, height - 60);
                postInvalidateDelayed(PEOPLE_TIME);
                break;
            case BOAT_LEFT:
                float left = boatRectF.left + DISTANCE_BOAT;
                if (left >= boatRight) {
                    left = boatRight;
                    mode = RIGHT;
                }
                boatRectF = new RectF(left, height - boatHeight - 60,
                        left + boatWidth, height - 60);
                postInvalidateDelayed(BOAT_TIME);
                break;
            case RIGHT:
                //上岸
                if (currentDrive != null) {
                    mState = mState.out(currentDrive.ml, currentDrive.cl);
                    currentDrive = null;
                    mBoatNumber = 0;
                    mLeftNumber = mState.ml;
                    mRightNumber = State.N - mState.ml;

                    cBoatNumber = 0;
                    cLeftNumber = mState.cl;
                    cRightNumber = State.N - mState.cl;

                    index++;//下一个路径
                    if (index >= mDrives.size()) {
                        //结束
                        mode = OVER;
                    }
                    postInvalidateDelayed(PEOPLE_TIME);
                    break;
                }

                //上船
                mode = BOAT_RIGHT;
                currentDrive = mDrives.get(index);
                mBoatNumber = currentDrive.ml;
                mLeftNumber = mState.ml;
                mRightNumber = State.N - mState.ml - mBoatNumber;

                cBoatNumber = currentDrive.cl;
                cLeftNumber = mState.cl;
                cRightNumber = State.N - mState.cl - cBoatNumber;

                //船在最左边
                boatRectF = new RectF(boatRight, height - boatHeight - 60,
                        boatRight + boatWidth, height - 60);
                postInvalidateDelayed(PEOPLE_TIME);
                break;
            case BOAT_RIGHT:
                float right = boatRectF.left - DISTANCE_BOAT;
                if (right <= boatLeft) {
                    right = boatLeft;
                    mode = LEFT;
                }
                boatRectF = new RectF(right, height - boatHeight - 60,
                        right + boatWidth, height - 60);
                postInvalidateDelayed(BOAT_TIME);
                break;
            case OVER:
                break;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        init();
    }

    //获取船上野人的位置
    RectF getCRectFInBoat() {
        float right = boatRectF.right - (boatWidth - 2 * peopleWidth) / 2;
        float left = right - peopleWidth;
        float bottom = boatRectF.bottom - boatHeight / 3;
        float top = bottom - peopleHeight;
        return new RectF(left, top, right, bottom);
    }

    //获取船上传教士的位置
    RectF getMRectInBoat() {
        float left = boatRectF.left + (boatWidth - 2 * peopleWidth) / 2;
        float right = left + peopleWidth;
        float bottom = boatRectF.bottom - boatHeight / 3;
        float top = bottom - peopleHeight;
        return new RectF(left, top, right, bottom);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(bgBitmap, null, bgRectF, backgroundPaint);
    }

    public void drawBoat(Canvas canvas) {
        canvas.drawBitmap(boatBitmap, null, boatRectF, bPaint);
    }

    //画野人
    public void drawC(Canvas canvas) {

        //右边
        if (cRightNumber != 0) {
            canvas.drawBitmap(yeBitmap, null, cRightRectF, cPaint);
            String number = cRightNumber + "";
            drawText(canvas, number, cRightRectF.left, cRightRectF.top);
        }
        //左边
        if (cLeftNumber != 0) {
            canvas.drawBitmap(yeBitmap, null, cLeftRectF, cPaint);
            String number = cLeftNumber + "";
            drawText(canvas, number, cLeftRectF.left, cLeftRectF.top);
        }
        //船上
        if (cBoatNumber != 0) {
            RectF rectF = getCRectFInBoat();
            canvas.drawBitmap(yeBitmap, null, rectF, cPaint);
            String number = cBoatNumber + "";
            drawText(canvas, number, rectF.left, rectF.top);
        }
    }


    //画传教士
    public void drawM(Canvas canvas) {

        //右边
        if (mRightNumber != 0) {
            canvas.drawBitmap(chuanBitmap, null, mRightRectF, mPaint);
            String number = mRightNumber + "";
            drawText(canvas, number, mRightRectF.left, mRightRectF.top);
        }
        //左边
        if (mLeftNumber != 0) {
            canvas.drawBitmap(chuanBitmap, null, mLeftRectF, mPaint);
            String number = mLeftNumber + "";
            drawText(canvas, number, mLeftRectF.left, mLeftRectF.top);
        }
        //船上
        if (mBoatNumber != 0) {
            RectF rectF = getMRectInBoat();
            canvas.drawBitmap(chuanBitmap, null, rectF, mPaint);
            String number = mBoatNumber + "";
            drawText(canvas, number, rectF.left, rectF.top);
        }
    }

    private void drawText(Canvas canvas, String text, float x, float y) {
        Paint.FontMetrics fm = nPaint.getFontMetrics();
        float coorX = peopleWidth / 2;
        float coorY = -fm.ascent / 2;
        canvas.drawText(text, x + coorX, y - coorY, nPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawBoat(canvas);
        drawM(canvas);
        drawC(canvas);
        next();
    }
}
