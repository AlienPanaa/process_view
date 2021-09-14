package com.alien.process_view;

import android.os.Bundle;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alien.process_view.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private int type = 1;

    private int value, count;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initData();

        initEvent();
    }

    private void initData() {
        value = binding.progress.getProgress();
        count = binding.progress.getCount();
    }

    private void initEvent() {
        binding.add.setOnClickListener(v ->
                binding.progress.setProgress(++value));

        binding.reduce.setOnClickListener(v ->
                binding.progress.setProgress(--value));

        binding.addCount.setOnClickListener(v ->
                binding.progress.setCount(++count));

        binding.reduceCount.setOnClickListener(v ->
                binding.progress.setCount(--count));

        binding.changeType.setOnClickListener(v -> {

//            binding.progress.setArrowType(result);
        });

        binding.angleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.progress.setAngle(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}