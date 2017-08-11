package lecho.lib.hellocharts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import lecho.lib.hellocharts.BuildConfig;
import lecho.lib.hellocharts.listener.DummyLineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.renderer.LineChartRenderer;

/**
 * LineChart, supports cubic lines, filled lines, circle and square points. Point radius and stroke width can be
 * adjusted using LineChartData attributes.
 *
 * @author Leszek Wach
 */
public class CustomWaveLineChartView extends AbstractChartView implements LineChartDataProvider {
    private static final String TAG = "LineChartView";
    protected LineChartData data;
    protected Paint mPaint;

    public int getCurrentBmp() {
        return currentBmp;
    }

    public void setCurrentBmp(int currentBmp) {
        this.currentBmp = currentBmp;
        this.invalidate();//刷新心率
    }

    private int currentBmp;//当前心率
    protected LineChartOnValueSelectListener onValueTouchListener = new DummyLineChartOnValueSelectListener();

    public CustomWaveLineChartView(Context context) {
        this(context, null, 0);
    }

    public CustomWaveLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWaveLineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
        setChartRenderer(new LineChartRenderer(context, this, this));
        setLineChartData(LineChartData.generateDummyData());
    }

    @Override
    public LineChartData getLineChartData() {
        return data;
    }

    @Override
    public void setLineChartData(LineChartData data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Setting data for LineChartView");
        }

        if (null == data) {
            this.data = LineChartData.generateDummyData();
        } else {
            this.data = data;
        }

        super.onChartDataChange();
    }

    @Override
    public ChartData getChartData() {
        return data;
    }

    @Override
    public void callTouchListener() {
        SelectedValue selectedValue = chartRenderer.getSelectedValue();

        if (selectedValue.isSet()) {
            PointValue point = data.getLines().get(selectedValue.getFirstIndex()).getValues()
                    .get(selectedValue.getSecondIndex());
            onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), point);
        } else {
            onValueTouchListener.onValueDeselected();
        }
    }

    public LineChartOnValueSelectListener getOnValueTouchListener() {
        return onValueTouchListener;
    }

    public void setOnValueTouchListener(LineChartOnValueSelectListener touchListener) {
        if (null != touchListener) {
            this.onValueTouchListener = touchListener;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制文字
        drawText(canvas);
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(50);
    }

    protected void drawText(Canvas canvas){
        canvas.drawText("心率:"+currentBmp,100,100,mPaint);//float x, float y
    }

}
