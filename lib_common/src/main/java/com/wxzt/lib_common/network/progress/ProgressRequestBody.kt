package com.wxzt.lib_common.network.progress

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/11/14 10:49
 * Description: 包装的请求体，处理进度
 * History:
 */
class ProgressRequestBody(
        //实际的待包装请求体
        private val requestBody: RequestBody,
        progressListener: ProgressRequestListener?) : RequestBody() {
    //进度回调接口
    private val progressListener: ProgressRequestListener? = progressListener

    //包装完成的BufferedSink
    private var bufferedSink: BufferedSink? = null

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {

        if (bufferedSink == null) {
//            //包装
            bufferedSink = Okio.buffer(sink(sink))
        }
        //写入
        requestBody.writeTo(bufferedSink)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink?.flush()
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            var bytesWritten = 0L

            //总字节长度，避免多次调用contentLength()方法
            var contentLength = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                //                System.out.println("bytesWritten = " + bytesWritten + ", contentLength = " + contentLength);

                //回调
                progressListener?.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength)
            }
        }
    }

}