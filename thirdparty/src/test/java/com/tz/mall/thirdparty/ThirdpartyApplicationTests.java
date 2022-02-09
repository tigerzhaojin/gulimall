package com.tz.mall.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.StorageClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThirdpartyApplicationTests {
    //    使用阿里云的start，可以将信息配置在yaml文件里。自动注入即可
    @Resource
    OSSClient ossClient;
    @Test
    public void contextLoads() {
    }
    @Test
    public void uploadTest(){
//         yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "oss-cn-shanghai.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。
        // 强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tRyeQLeXiXQZAabfRyh";
        String accessKeySecret = "TOEPFpXz8Enk0pwOXSK6XHn8noJ3yf";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        // 依次填写Bucket名称（例如examplebucket）、Object完整路径
        // （例如exampledir/exampleobject.txt）和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        PutObjectRequest putObjectRequest = new PutObjectRequest
                ("zhaojin-gulimall",
                        "20060302-丁丁026.jpg",
                        new File("/Users/zhaojin/Pictures/丁丁/20060302/20060302-丁丁026.jpg"));

//         如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
//         ObjectMetadata metadata = new ObjectMetadata();
//         metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
//         metadata.setObjectAcl(CannedAccessControlList.Private);
//         putObjectRequest.setMetadata(metadata);

    // 上传文件。
        ossClient.putObject(putObjectRequest);

    // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("文件上传成功");
    }

}
