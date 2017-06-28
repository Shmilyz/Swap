# Swap
UI方面基于Material Design进行搭建，并运用大量Material Design相关控件如drawerlayout等。

广播接收器接受系统广播，和使用本地广播提醒app网络的开启与关闭。以及碎片之间，服务于活动之间的交互。

利用内容提供器读取手机联系人，相册等。

开启服务，并创建定时任务，按照定时时间向服务器请求数据并保存至本地，达到缓存效果，减少用户等待时间。

使用ListeView显示搜索推荐选项。

为提升运行效率，大量使用RecyclerView，实现网格或瀑布等效果。

首页使用开源xRecyclerView控件，实现下拉刷新，上拉加载更过功能，通过改变sql语句limit实现限制加载数量。

大量使用碎片，动态加载碎片，以及碎片与activity，碎片与碎片之间通信。

大量使用SharedPreferences功能，带着数据在各活动之间传递。

采用开源LitePal对用户最近搜索，对省市区等进行保存。

使用运行时权限，对联网，访问通讯录，调用相机，定位等敏感权限进行管理，保证兼容。

访问用户的相册，并保存用户所选照片的路径。调用系统相机为用户提供拍照，为6.0以上用户调用相机并显示所提供的provider。

简单使用WebView展示网页，控件progressBar显示加载过程。

由于Swap所有数据图片来源云服务器，所以大量使用Xutils，ok3开源访问网络魔域服务器进行交互。

与服务器的交互得到大量Json数据，采用Gson开源，并配合GsonFormat 插件的使用，更方便容易的解析数据。

对于少量的json，采用原始JSONArray和JSONObject进行解析。

由于图片都保存到云服务器，大量使用采用性能更稳定的Glide进行网络图片的加载。

采用基于PhotoView的DragPhotoView开源实现仿微信查看图片。

采用鲁班将图片压缩至服务器可接受范围。

SmartTabLayout开源与ViewPager结合，使viewpager的ui更加炫丽。

使用百度地图的定位服务，为用户提供用户所在位置，供用户参考。

开启多线程，为广告欢迎等待页面，网络求求数据等需要长时间等待的功能开启副线程，防止主线程的阻塞。

重写Application,提供获取全局变量的方法，方便某些功能的初始化。

重新绘制一个数字键盘，但由于未提供付款功能，这个重绘控件没有用到。

由于要使用开源，采用了maven和Gradle构建。

使用插件 Android ButterKnife Zelezny使便利开发。

使用开源控件如安卓选择器类库，PictureSelector，SwipeRecyclerView，FloatingToolbar，Checkbox，Like Button等，增加swap的观赏性。

使用Mob为开发者提供的验证短信SDK，实现短信的验证登录。

使用阿里云为开发者提供的通知推送SDK，实现管理员在控制台发送推送通知为每一位Swap用户，每一位Swap用户会收到Notification，用户可点击Notification进入app。

服务器端的接收SQL语句并进行增删改查。

服务期端的接收用户上传图片等并保存到对应本地磁盘。


