package app.music.ui.widget.toolbar;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.databinding.DataBindingUtil;

import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import app.music.R;
import app.music.databinding.ToolBarAppBinding;
import app.music.utils.EmptyUtil;
import app.music.ui.widget.BaseRelativeLayout;

public class ToolBarApp extends BaseRelativeLayout<ToolBarAppBinding> {

    private Context mContext;
    private OnClickItemToolBar mOnClickItemToolBar;

    public ToolBarApp(Context context) {
        super(context);
        init(context, null);
    }

    public ToolBarApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToolBarApp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray mTypedArray = null;
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.tool_bar_app, this, true);
        try {
            mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolBarApp);
            initTitle(mTypedArray, dataBinding.tvToolBarRight);
            initTitle(mTypedArray, dataBinding.tvTitle);
            initListener();
        } finally {
            mTypedArray.recycle();
        }
    }

    private void initListener() {
        dataBinding.tvToolBarRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickItemToolBar != null) {
                    mOnClickItemToolBar.onItemRight();
                }
            }
        });
        dataBinding.ivToolbarRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickItemToolBar != null) {
                    mOnClickItemToolBar.onItemRight();
                }
            }
        });
        dataBinding.ivToolbarLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickItemToolBar != null) {
                    mOnClickItemToolBar.onItemLeft();
                }
            }
        });
    }

    private void initTitle(TypedArray typedArray, TextView mTitle) {
        String title = typedArray.getString(R.styleable.ToolBarApp_tbaTextTitle);
        boolean isActive = typedArray.getBoolean(R.styleable.ToolBarApp_tbaTitle, false);
        int size = typedArray.getDimensionPixelSize(R.styleable.ToolBarApp_tbaSizeTitle,
                getResources().getDimensionPixelSize(R.dimen.default_tba_title_text_size));
        int color = typedArray.getColor(R.styleable.ToolBarApp_tbaColorTitle,
                getResources().getColor(R.color.default_tba_title_text_color));
        int textStyle = typedArray.getInteger(
                R.styleable.ToolBarApp_tbaTextTitleStyle,
                1);
        mTitle.setTypeface(mTitle.getTypeface(), textStyle == 1 ? Typeface.BOLD : (textStyle == 0 ?
                Typeface.NORMAL : Typeface.ITALIC));
        mTitle.setText(title);
        mTitle.setTextColor(color);
        mTitle.setVisibility(isActive ? VISIBLE : GONE);
    }

    public void setOnClickItemIconToolBar(OnClickItemToolBar mOnClickItemToolBar) {
        this.mOnClickItemToolBar = mOnClickItemToolBar;
    }

    public void setTitleApp(int idTitle) {
        dataBinding.tvTitle.setVisibility(idTitle == 0 ? GONE : VISIBLE);
        if (idTitle != 0) {
            setTitleApp(mContext.getString(idTitle));
        }
    }

    public void setTitleApp(String title) {
        dataBinding.tvTitle.setVisibility(!EmptyUtil.isNotEmpty(title) ? GONE : VISIBLE);
        if (EmptyUtil.isNotEmpty(title)) {
            dataBinding.tvTitle.setText(title);
            dataBinding.tvTitle.setSelected(true);
        }
    }

    public void setIconToolBarLeft(int drawable) {
        if (EmptyUtil.isNotEmpty(drawable)) {
            dataBinding.ivToolbarLeft.setImageResource(drawable);
        }
    }

    public void setIconToolBarRight(int drawable) {
        if (EmptyUtil.isNotEmpty(drawable)) {
            dataBinding.ivToolbarRight.setImageResource(drawable);
        }
    }

    public void setTextCenterToolbar(int idTextCenter) {
        dataBinding.tvToolBarCenter.setVisibility(idTextCenter == 0 ? GONE : VISIBLE);
        if (idTextCenter != 0) {
            setTextCenterToolbar(mContext.getString(idTextCenter));
        }
    }

    public void setTextCenterToolbar(String textCenter) {
        dataBinding.tvToolBarCenter.setVisibility(EmptyUtil.isNotEmpty(textCenter) ? VISIBLE : GONE);
        if (EmptyUtil.isNotEmpty(textCenter)) {
            dataBinding.tvToolBarCenter.setText(textCenter);
            dataBinding.tvToolBarCenter.setSelected(true);
        }
    }

    public void setTextRightToolbar(int idTextRightToolbar) {
        dataBinding.tvToolBarRight.setVisibility(idTextRightToolbar == 0 ? GONE : VISIBLE);
        if (idTextRightToolbar != 0) {
            setTextRightToolbar(mContext.getString(idTextRightToolbar));
        }
    }

    public void setTextRightToolbar(String textRightToolbar) {
        dataBinding.tvToolBarRight.setVisibility(EmptyUtil.isNotEmpty(textRightToolbar) ? VISIBLE : GONE);
        if (EmptyUtil.isNotEmpty(textRightToolbar)) {
            dataBinding.tvToolBarRight.setText(textRightToolbar);
            dataBinding.tvToolBarRight.setSelected(true);
        }
    }

    public interface OnClickItemToolBar {
        void onItemRight();

        void onItemLeft();
    }
}
