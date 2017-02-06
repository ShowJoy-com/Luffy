# Style 模块


## 原理

该模块用于动态配置view的style，通过下发配置，动态查找需要的view，设置相应的属性。查找view是通过id，即R.java文件里生成的id.
该id列表可以通过apktool反编译apk，/res/values/public.xml里可以通过id名称搜索到对应的id。

查找view和设置样式的时机放在BaseViewModel的 onresume结尾，不管是fragment还是activity的viewmodel都会继承于BaseViewModel.

## 配置格式示例：（以后有资源的话，会只下发对应版本的配置）

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
                                                "image": "http://dsdsdd.png",
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

整体是一个数组，每一项对应一个版本，因为每个版本的resource id 会有差异。有条件的话，可以做成每个版本请求的时候只下发对应版本的配置。
由于目前暂时采用在线参数的方式，所以需要区分版本号。

`style`里也是一个数组：

- `page`: fragment的路径名或者activity的路径名
- `views`：就是view的列表，是一个数组，每一项对应一个view，包含id和对应的properties
- `properties`: view的属性配置


`id`: 示例"0x7f0e02cb",//从public.xml里查找到resource id
`properties`: view的属性配置，可选包括

- "type": "text",//view类型，目前支持 text,image和普通view，默认为view
- "text": "test",//只对text类型的view生效，设置textview的text
- "textColor": "#ff0000",//只对text类型的view生效，设置textview的textcolor
- "textSize": 20,//只对text类型的view生效，设置textview的textsize
- "image": "http://dsdsdd.png",//只对image类型的view生效，设置imageview显示的图片，是一个图片链接
- "visibility": 0//是否可见，0表示visible，4表示invisible，8表示gone
- "children":支持子view的配置
- "parent":支持父view的配置


由于当同个页面含有多个结构一个的view时，通过id就不能找出唯一的view，比如listview这样的结构。
于是配置里支持子view，就是上面"properties"里的"children"
children里包含属性 index，views, properties

```
"children": [
   {
      "index": 1,
      "views": [
          {
              "id": "0x7f0e02cb",
              "properties": {...}
          }
      ],
      "properties": {...}
   }
]
```
如果properties含有children，就会判断该view是否是ViewGroup，只有是ViewGroup才会生效

其中`index`表示这个viewgroup的children里的第index个,通过getChildAt获取。


"views"里的配置同前面
"properties"里的配置同前面，也可以配置child.

这样就可以支持多个层级了。


