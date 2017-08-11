package com.example.zq.linechartdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.zq.linechartdemo.utils.Dbug;
import com.example.zq.linechartdemo.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.CustomWaveLineChartView;

public class MainActivity extends AppCompatActivity {

    private CustomWaveLineChartView mChartView;
    private List<PointValue> values;
    private List<Line> lines;
    private LineChartData lineChartData;
    private CustomWaveLineChartView lineChartView;
    private List<Line> linesList;
    private List<PointValue> pointValueList;
    private List<PointValue> points;
    private int position = 0;
    private Timer timer;
    private boolean isFinish = true;
    private Axis axisY, axisX;
    private Random random = new Random();
    private int pointWidth = 0;//波形显示到右边的区域

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChartView = (CustomWaveLineChartView) findViewById(R.id.chart);
        pointWidth = ScreenUtils.getScreenHeightWithoutStatiusBar(MainActivity.this);
        Dbug.d(getClass().getSimpleName(), "====" + pointWidth);
        initView();
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时添加新的点
                PointValue value1 = new PointValue(position * 5, random.nextInt(100) + 40);
                value1.setLabel("00:00");
                pointValueList.add(value1);

                float x = value1.getX();
                final float y = value1.getY();
                //根据新的点的集合画出新的线
                Line line = new Line(pointValueList);
                line.setColor(Color.RED);
                line.setShape(ValueShape.CIRCLE);
                line.setHasPoints(false);//设置是否有小圆帽显示
                line.setStrokeWidth(1);//线条粗细
                line.setCubic(true);//曲线是否平滑，即是曲线还是折线

                linesList.clear();
                linesList.add(line);
                lineChartData = initDatas(linesList);
                lineChartView.setLineChartData(lineChartData);
                //根据点的横坐实时变幻坐标的视图范围
                Viewport port;
                int arg = 50;
                if (x > arg) {
                    port = initViewPort(x - arg, x);
                } else {
                    port = initViewPort(0, arg);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lineChartView.setCurrentBmp((int) y);//刷新屏幕上显示的心率
                    }
                });
                lineChartView.setCurrentViewport(port);//当前窗口

                Viewport maPort = initMaxViewPort(x);
                lineChartView.setMaximumViewport(maPort);//最大窗口
                position++;
                if (position == 200) {//指定时间取消
                    timer.cancel();
                }

            }
        }, 300, 300);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        lineChartView = (CustomWaveLineChartView) findViewById(R.id.chart);
        pointValueList = new ArrayList<>();
        linesList = new ArrayList<>();

        //初始化坐标轴
        axisY = new Axis();
        //添加坐标轴的名称
        axisY.setLineColor(Color.parseColor("#aab2bd"));
        axisY.setTextColor(Color.parseColor("#aab2bd"));
        axisY.setHasLines(false);//是否显示纵坐标
        axisX = new Axis();
        axisX.setLineColor(Color.parseColor("#aab2bd"));
        axisX.setHasLines(false);//每个点的竖直线

        lineChartData = initDatas(null);
        lineChartView.setLineChartData(lineChartData);

        Viewport port = initViewPort(0, pointWidth);
        lineChartView.setCurrentViewportWithAnimation(port);
        lineChartView.setInteractive(false);
        lineChartView.setScrollEnabled(true);
        lineChartView.setValueTouchEnabled(true);
        lineChartView.setFocusableInTouchMode(true);
        lineChartView.setViewportCalculationEnabled(false);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.startDataAnimation();
        points = new ArrayList<>();
    }


    private LineChartData initDatas(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }

    /**
     * 当前显示区域
     *
     * @param left
     * @param right
     * @return
     */
    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 150;
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }

    /**
     * 最大显示区域
     *
     * @param right
     * @return
     */
    private Viewport initMaxViewPort(float right) {
        Viewport port = new Viewport();
        port.top = 150;
        port.bottom = 0;
        port.left = 0;
        port.right = right + 50;
        return port;
    }
}