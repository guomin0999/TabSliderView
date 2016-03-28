package cn.guomin0999.slidetab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.guomin0999.library.TabSliderView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private TabSliderView slider;

    private int[] rbIds = {R.id.rb_make_money, R.id.rb_invite, R.id.rb_message, R.id.rb_discover, R.id.rb_me};
    private RadioButton[] radioButton = new RadioButton[rbIds.length];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < rbIds.length; i++) {
            radioButton[i] = (RadioButton) findViewById(rbIds[i]);
            radioButton[i].setOnCheckedChangeListener(this);
        }
        slider = (TabSliderView) findViewById(R.id.slider);
        slider.setTabCount(5);
        slider.setOnMoveEnd(new TabSliderView.OnMoveEndListener() {
            public void onMoveEnd(int position) {
                Toast.makeText(MainActivity.this, "Item:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCheckedChangfed(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (int i = 0; i < rbIds.length; i++) {
                if (buttonView.getId() == rbIds[i]) {
                    slider.setPosition(i);
                } else {
                    radioButton[i].setChecked(false);
                }
            }
        }
    }
}
