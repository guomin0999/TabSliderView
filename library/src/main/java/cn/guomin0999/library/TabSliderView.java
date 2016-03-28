package cn.guomin0999.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * TODO: document your custom view class.
 */
public class TabSliderView extends View {

    private int color;
    private float lineHeight;
    private Paint paint;
    private int position = 0;
    private Scroller scroller;
    private int tabCount = 0;
    int contentWidth = 0;
    int prevNotifyPosition = -1;//通知过结束了 就不要重复了

    public interface OnMoveEndListener {
        void onMoveEnd(int position);
    }

    private OnMoveEndListener listener;

    public void setOnMoveEnd(OnMoveEndListener l) {
        listener = l;
    }

    public void setTabCount(int tabCount) {
        this.tabCount = tabCount;
    }

    public void setPosition(final int position) {
        if (this.position != position) {
            this.position = position;
            int dx = contentWidth * position / tabCount - scroller.getCurrX();
            scroller.startScroll(scroller.getCurrX(), 0, dx, 0, 400);
            invalidate();
        }
    }

    private Drawable indicator;

    public TabSliderView(Context context) {
        super(context);
        init(null, 0);
    }

    public TabSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TabSliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TabSliderView, defStyle, 0);

        if (a.hasValue(R.styleable.TabSliderView_indicator)) {
            indicator = a.getDrawable(
                    R.styleable.TabSliderView_indicator);
            indicator.setCallback(this);
            color = a.getColor(R.styleable.TabSliderView_barColor, 0x0);
            lineHeight = a.getDimension(R.styleable.TabSliderView_lineHeight, 0);
        }
        a.recycle();
        paint = new Paint();
        paint.setColor(color);

        scroller = new Scroller(getContext(), new Interpolator() {
            private float s = 1.70158f;

            public float getInterpolation(float t) {
                return calculate(t, 0, 1, 1);
            }

            public Float calculate(float t, float b, float c, float d) {
                return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
            }
        });
    }

    public void setInterpolator(Interpolator interpolator) {
        scroller = new Scroller(getContext(), interpolator);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (tabCount == 0) {
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        if (indicator != null) {
            int bgWidth = contentWidth / tabCount;
            indicator.setBounds(scroller.getCurrX() + paddingLeft - bgWidth / 10, paddingTop,
                    scroller.getCurrX() + paddingLeft + bgWidth + bgWidth / 10, paddingTop + contentHeight - (int) lineHeight);
            indicator.draw(canvas);
        }

        canvas.drawRect(paddingLeft, paddingTop + contentHeight - lineHeight, paddingLeft + contentWidth, paddingTop + contentHeight, paint);

        if (scroller.computeScrollOffset()) {
            invalidate();
        } else {
            if (prevNotifyPosition != position) {
                if (listener != null) {
                    prevNotifyPosition = position;
                    listener.onMoveEnd(position);
                }
            }
        }
    }

}
