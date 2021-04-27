package com.wxzt.kfly.ustils

import java.util.*

/**
 *  author : zuoz
 *  date : 2021/4/2 9:37
 *  description :
 */
object DefDataUtil {

    /**
     * 获取随机一张图片路径
     */
    fun randomImage(): String {
        Random().run {
            val red = nextInt(IMAGE_URL.size)
            return IMAGE_URL[red]
        }
    }

    //网络图片数据 给用户随机使用
    var IMAGE_URL = arrayListOf(
            "https://img2.woyaogexing.com/2019/08/30/abf3568adb8745ac9d5dc6cf9a3da895!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/246e7cea8e0849cc88140f1973fdb95d!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/516b60c208824015ab2fb736cea1eb8c!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/b6c6bce7acc84e7aabed05ccc5ec9f80!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/749c2d6c2f174a91b1f1f92c2de30004!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/38304054b90447b9bc21bb07176ab058!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/917e8cbef5344431a9b31c51a483f363!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/bdaae1c240af46ac98a95eaac3be843f!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/13908fcec57a4738917fec458d581bdf!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/7f9c86f6cbbf4f9892df28e50321f477!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/f6d7b8480a4b40298d7c9beb079ba8d4!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/f240d46657cf446eb0b5eb6290ff4528!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/a85407f91da24953a619df9315993e34!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/d5d365f1458a41d891b7ada030e74232!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/11217143b1ef433b8a914c16548fa50e!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/c8436d280f49418b8825fadfeeb0c0f8!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/81513514d2c04d3e901c33823ba3c492!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/1e300689c6a845b99e8daf32d72a1929!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/3adc12526ac541b5a4efd4493b4a3e85!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/a0c0493a57164cf9985cdbc972c22dd1!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/62884a8178f543fabcfb47db2ef7003d!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/9204eb1b05844d5c9723d37fcd76dc77!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/15937d014e13450c9e11ad5c3a015cf2!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/0366969e21b14d479696d444302be8a4!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/4739431c257643a29b3a3343a4f5fb2e!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/3a918ed489af4490884ebef741f8df91!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/9a4c7ca101434b46bf03751c88e378a4!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/4a1e7dde2b5c47f4b025c15388dc290f!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/626d23fe1c2b411fa33f92e6c94d7af0!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/667ebc1b9d7c4783bad801a2a3be199d!600x600.jpeg")

}