package com.showjoy.android.luffy;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.showjoy.android.luffy.adapter.ILuffyImageAdapter;
import com.showjoy.android.luffy.entities.StyleEntity;
import com.showjoy.android.luffy.utils.JsonUtils;
import com.showjoy.android.luffy.utils.LogUtils;

import java.util.List;

/**
 * Created by lufei on 1/23/17.
 */

public class Luffy {

    StyleEntity styleEntity;

    String styleEntities;

    ILuffyImageAdapter styleImageAdapter;

    static Luffy instance = new Luffy();

    private Luffy() {
    }

    public static Luffy getInstance() {
        return instance;
    }

    public void setImageAdapter(ILuffyImageAdapter styleImageAdapter) {
        this.styleImageAdapter = styleImageAdapter;
    }

    public int hexString2Int(String hexStr) {
        if (TextUtils.isEmpty(hexStr)) {
            return 0;
        }
        if (hexStr.startsWith("0x")) {
            hexStr = hexStr.substring(2);
        }
        try {
            return Integer.parseInt(hexStr, 16);
        } catch (NumberFormatException e) {
            LogUtils.e(e);
            return 0;
        }
    }

    public void parse(Context context, String styleEntities) {

        if (null == styleEntities) {
            this.styleEntities = null;
            styleEntity = null;
            return;
        }
        if (styleEntities.equals(this.styleEntities)) {
            return;
        }

        this.styleEntities = styleEntities;

        styleEntity = null;

        List<StyleEntity> styleEntityList = JsonUtils.parseArray(styleEntities, StyleEntity.class);
        if (null == styleEntityList) {
            return;
        }

        String version = getVersion(context);

        for (StyleEntity entity : styleEntityList) {
            if (version.equals(entity.version)) {
                styleEntity = entity;
                break;
            }
        }
    }

    public void doStyle(Activity activity) {

        if (null == styleEntity) {
            return;
        }
        List<StyleEntity.StyleBean> styleBeanList = styleEntity.style;

        if (null == styleBeanList) {
            return;
        }

        setStyleList(activity.getClass().getName(), activity.getWindow().getDecorView(), styleBeanList);
    }

    public void doStyle(Fragment fragment) {

        if (null == styleEntity) {
            return;
        }
        List<StyleEntity.StyleBean> styleBeanList = styleEntity.style;

        if (null == styleBeanList) {
            return;
        }

        setStyleList(fragment.getClass().getName(), fragment.getView(), styleBeanList);
    }

    private void setStyleList(final String page, final View view, final List<StyleEntity.StyleBean> styleBeanList) {

        for (StyleEntity.StyleBean styleBean : styleBeanList) {
            if (styleBean.page.equals(page)) {
                if (null != styleBean.views) {
                    setStyle(page, view, styleBean.views);
                }
                if (null != styleBean.properties) {
                    setStyle(page, view, styleBean.properties);
                }
            }
        }
    }

    private void setStyle(final String page, final View target, final StyleEntity.ViewProperties properties) {
        if (null == target || null == properties) {
            return;
        }

        try {
            if (!TextUtils.isEmpty(properties.type)) {
                switch (properties.type) {
                    case ViewType.IMAGE:
                        if (null != styleImageAdapter) {
                            styleImageAdapter.setImageUrl(target, properties.image);
                        }
                        break;
                    case ViewType.TEXT:
                        if (!(target instanceof TextView)) {
                            break;
                        }
                        TextView textView = (TextView) target;
                        if (!TextUtils.isEmpty(properties.text)) {
                            textView.setText(properties.text);
                        }
                        if (!TextUtils.isEmpty(properties.textColor)) {
                            textView.setTextColor(Color.parseColor(properties.textColor));
                        }
                        if (properties.textSize > 0) {
                            textView.setTextSize(properties.textSize);
                        }
                        break;
                }
            }
            if (!TextUtils.isEmpty(properties.background)) {
                target.setBackgroundColor(Color.parseColor(properties.background));
            }
            target.setVisibility(properties.visibility);
        } catch (Exception e) {
            LogUtils.e(e);
        }


        if (target instanceof ViewGroup) {
            ViewGroup targetViewGroup = (ViewGroup) target;
            if (null != properties.children && properties.children.size() > 0) {
                for (StyleEntity.ChildBean childBean : properties.children) {
                    View childView = targetViewGroup.getChildAt(childBean.index);
                    setStyle(page, childView, childBean.views);
                    setStyle(page, childView, childBean.properties);
                }
            }
        }

        if (null != properties.parent && null != target.getParent()) {
            ViewGroup viewgroup = (ViewGroup) target.getParent();
            setStyle(page, viewgroup, properties.parent.views);
            setStyle(page, viewgroup, properties.parent.properties);
        }
    }

    private void setStyle(final String page, final View view, final List<StyleEntity.ViewBean> resources) {
        if (null == resources || null == view) {
            return;
        }

        for (StyleEntity.ViewBean viewBean : resources) {
            View target = view.findViewById(hexString2Int(viewBean.id));

            if (null == target) {
                continue;
            }

            setStyle(page, target, viewBean.properties);

        }

    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "1.0.0";
        }
    }
}
