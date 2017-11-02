

# TinyToggleButton



## Features

- **全代码绘制不需要任何图片资源**
- **支持设置多种不同的风格样式**
- **支持设置按钮的点击动画及时间**
- **支持自定义开关按钮的颜色值**


## Screenshots

![](http://on1xkrize.bkt.clouddn.com/tiny_toggle_button.JPG)


## Usages

第一步在你的XML布局文件中直接引用`me.wondertwo.tiny.TinyToggleButton`开关控件即可。activity_main.xml如下。当然，支持在XML文件中设置按钮颜色值、是否支持点击动画、点击动画持续时间等参数。

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tinyToggleBtn="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/holo_purple"
    android:gravity="center"
    android:orientation="vertical">

    <me.wondertwo.tiny.TinyToggleButton
        android:id="@+id/tiny_toggle_button_1"
        android:layout_width="180dp"
        android:layout_height="wrap_content"
        tinyToggleBtn:animator_enable="true"
        tinyToggleBtn:color_toggle_above_off="@color/color_toggle_off"
        tinyToggleBtn:color_toggle_above_on="@color/color_toggle_on" />

</LinearLayout>
```



第二步Activity中引用上述布局文件。按钮颜色值、是否支持动画、动画持续时间等参数依然可以在Java代码中动态设置。MainActivity.java代码如下

```java
/** Created by wondertwo on 2017.10.05. */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TinyToggleButton tinyToggleButton = (TinyToggleButton) findViewById(R.id.tiny_toggle_button_1);
        tinyToggleButton.setTinyStyle(TinyToggleButton.Style.TINY_STYLE_A);
        tinyToggleButton.setChecked(false, true);
        tinyToggleButton.setAnimatorDuration(200);
        tinyToggleButton.setOnToggleClickListener(new TinyToggleButton.OnToggleClickListener() {
            @Override
            public void onToggleClick(TinyToggleButton button, boolean isChecked) {
                // TODO: 2017.10.28
            }
        });
    }
}
```



