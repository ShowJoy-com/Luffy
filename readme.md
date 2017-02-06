# Luffy


Luffy is a tiny unobtrusive style kit for android.

TinyStyle changes the style of specific view with the configuration. The configuration should be gotten from your server by yourself.

The configuration contains the ids of views which you want to change their style and the style information to decide what style they will be.

You can find the id of specific view from /res/values/public.xml by name after you decode the apk file.

### The sample of configuration

```
[
    {
        "version": "1.9.1.1",
        "style": [
            {
                "page": "com.showjoy.shop.module.user.UserFragment",
                "views": [
                    {
                        "id": "0x7f0e02cb",
                        "properties": {
                            "type": "text",
                            "text": "test",
                            "textColor": "#ff0000",
                            "textSize": 20,
                            "image": "http://dsdsdd.png",
                            "visibility": 0
                        }
                    },
                    {
                        "id": "0x7f0e02cc",
                        "properties": {
                            "children": [
                                {
                                    "index": 1,
                                    "views": [
                                       {
                                            "id": "0x7f0e02cb",
                                             "properties": {
                                                "type": "text",
                                                "text": "test",
                                                "textColor": "#ff0000",
                                                "textSize": 20,
                                                "visibility": 0
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    }
                ]
            }
        ]
    }
]
```
- 1、 It is an array,each item for different versions as the view hierarchy is different in apk with different versions.
- 2、The field `style` is an array, each item for different pages.
	- `page`: the full path name of fragment of activity
	- `views`: an array, each item for different views.
		- `id`: the id of view in hex
		- `properties`: properties of views
			- `type`: the type of view, support "text" or "image" or "view"
			- `text`: the text of TextView
			- `textColor`: the color of TextView, such as "#ff0000"
			- `textSize`: the text size of TextView, such as '20'
            - `image`: the url of image, only support remote image
            - `visibility`: 0 for VISIBLE,  4 for INVISIBLE, 8 for GONE
            - `children`: the children of the view，you can use it to find views which does not have id.
	            - `index`: the position of  child
	            - `views`: same as `views` above
	            - `properties`: same as `properties` above
            - `parent`: the parent of the view, you also can use it to find views which does not have id.
	            - `views`: same as `views` above
	            - `properties`: same as `properties` above

### Using Luffy in your application

If you are building with Gradle, simply add the following line to the dependencies section of your build.gradle file:

	compile 'com.showjoy.android:luffy:1.0.0'

If you enable ProGuard in your project, please add the following line to you proguard file.

    -keep class com.showjoy.**.entities.** {*;}

- 1、set image adapter if you need change the image view
```
	Luffy.getInstance().setStyleImageAdapter(new IStyleImageAdapter() {
                @Override
                public void setImageUrl(View imageView, String imageUrl) {
                 //do sth like below...
                    if (imageView instanceof SHImageView) {
                        ((SHImageView)imageView).setImageUrl(imageUrl);
                    }else if (imageView instanceof ImageView) {
                        ((ImageView)imageView).setImageBitmap(ImageUtils.getNetBitmap(imageUrl));
                    }
                }
            });
```
- 2、call `parse` to parse the configuration
```
	Luffy.getInstance().parse(context, configuration);
```
- 3、call `doStyle` to change the style. You can call it at the end of onresume. Mostly baseFragment and baseActivity or something like them should exist. You can call `doStyle`in them.
```
	Luffy.getInstance().doStyle(activity);
	//or
	//Luffy.getInstance().doStyle(fragment);
```



