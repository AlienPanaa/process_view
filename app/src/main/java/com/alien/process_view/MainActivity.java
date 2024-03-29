package com.alien.process_view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alien.process_view.databinding.ActivityMainBinding;
import com.view.alienlib.process_view.base.ProgressView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initEvent();
    }

    private void initEvent() {
        binding.add.setOnClickListener(v -> binding.progress.plus());

        binding.reduce.setOnClickListener(v -> binding.progress.reduce());

        binding.addCount.setOnClickListener(v -> binding.progress.plusCount());

        binding.reduceCount.setOnClickListener(v -> binding.progress.reduceCount());

        binding.changeType.setOnClickListener(v -> {
//            binding.progress.setArrowType(result);
        });

        binding.progress.setProcessViewListener(process -> Toast.makeText(this, "progress: " + process, Toast.LENGTH_SHORT).show());

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