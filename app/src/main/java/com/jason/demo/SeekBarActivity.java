package com.jason.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SeekBarActivity extends AppCompatActivity {
    private static final String TAG = "SeekBarActivity";
    
    private SeekBar mSeekBar;
    private TextView progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);

        mSeekBar = findViewById(R.id.seekbar);
        progressIndicator = findViewById(R.id.progress_indicator);

        // 设置最小值和最大值
        int min = 20;
        int max = 100;
        
        // SeekBar 的范围是 0 到 (max - min)，实际值范围是 min 到 max
        mSeekBar.setMax(max - min);
        // 设置初始进度为 0（对应实际值 min）
        mSeekBar.setProgress(0);

        // 等待布局完成后再更新位置
        mSeekBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSeekBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // 计算实际值：SeekBar 进度 + 最小值
                int actualValue = mSeekBar.getProgress() + min;
                updateProgressIndicator(mSeekBar, progressIndicator, actualValue);
            }
        });

        // 设置 SeekBar 监听器
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 计算实际值：SeekBar 进度 + 最小值
                int actualValue = progress + min;
                Log.i(TAG, "onProgressChanged: " + progress + ",min:" + min);
                updateProgressIndicator(mSeekBar, progressIndicator, actualValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始拖动时显示指示器
//                progressIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止拖动时保持显示
//                progressIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateProgressIndicator(SeekBar seekBar, TextView textView, int actualValue) {
        // 更新文本，显示实际值
        textView.setText(String.valueOf(actualValue));

        // 确保 View 已经测量完成
        if (seekBar.getWidth() == 0 || textView.getWidth() == 0) {
            return;
        }

        // 计算 SeekBar 的进度位置（使用 SeekBar 的当前进度，不是实际值）
        int seekBarWidth = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
        int thumbOffset = seekBar.getThumbOffset();
        float progressRatio = (float) seekBar.getProgress() / seekBar.getMax();
        float thumbPos = seekBar.getPaddingLeft() + (seekBarWidth * progressRatio) - thumbOffset;
        Log.i(TAG, "updateProgressIndicator getPaddingLeft: " + seekBar.getPaddingLeft() + ",seekBarWidth:" + seekBarWidth);
        Log.i(TAG, "updateProgressIndicator progressRatio: " + progressRatio + ",thumbOffset:" + thumbOffset + ",thumbPos:" + thumbPos);

        // 设置指示器的位置，使其跟随 SeekBar 的滑块
        int indicatorWidth = textView.getWidth();
        int thumbWidth = seekBar.getThumb().getBounds().width();
        float indicatorX = thumbPos - (indicatorWidth / 2.0f) + (thumbWidth / 2.0f);
        Log.i(TAG, "updateProgressIndicator indicatorWidth: " + indicatorWidth + ",thumbWidth:" + thumbWidth);
        Log.i(TAG, "updateProgressIndicator indicatorX: " + indicatorX);
        
        // 更新指示器的位置
        textView.setX(indicatorX);
        
        // 设置指示器在 SeekBar 上方
        int seekBarTop = seekBar.getTop();
        textView.setY(seekBarTop - textView.getHeight() - 16);
    }
}

