package com.showjoy.android.style.entities;

import java.util.List;

/**
 * Created by lufei on 1/23/17.
 */

public class StyleEntity {

    public String version;
    public List<StyleBean> style;

    public static class StyleBean {

        public String page;
        public List<ViewBean> views;
        public ViewProperties properties;
    }

    public static class ViewBean {
        /**
         * id : 0x7f0e02cb
         */

        public String id;
        public ViewProperties properties;

    }

    public static class ChildBean {

        public int index;
        public List<ViewBean> views;
        public ViewProperties properties;
    }

    public static class ParentBean {

        public List<ViewBean> views;
        public ViewProperties properties;
    }

    public static class ViewProperties {

        /**
         * type : text
         * text : test
         * textColor : #ff0000
         * textSize : 20
         * image : http://dsdsdd.png
         * visibility : 0
         *
         */
        public String type;
        public String text;
        public String textColor;
        public int textSize;
        public String image;
        public String background;
        public int visibility;
        public List<ChildBean> children;
        public ParentBean parent;
    }
}
