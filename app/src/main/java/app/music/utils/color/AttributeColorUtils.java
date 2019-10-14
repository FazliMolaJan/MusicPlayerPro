package app.music.utils.color;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.ColorInt;
import android.util.TypedValue;

public class AttributeColorUtils {

    public static int getColor(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attributeId, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }
}
