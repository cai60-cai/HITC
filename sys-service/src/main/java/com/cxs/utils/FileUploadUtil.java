package com.cxs.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.cxs.config.AliyunOssConfig;
import com.cxs.vo.base.FileStructureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Component
public class FileUploadUtil {
    @Autowired
    private AliyunOssConfig oss;

    private static final String[] imgArr = new String[]{".jpg",".png",".jpeg",".gif"};

    /**
     * 文件上传
     * @param fileName 文件名
     * @param content 文件的字节数组
     * @return
     */
    public void upload(String fileName, byte[] content) throws Exception{
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(oss.getEndpoint(), oss.getAccessKeyId(), oss.getAccessKeySecret());
        //创建上传请求对象
        PutObjectRequest putObjectRequest = new PutObjectRequest(oss.getBucketName(), fileName, new ByteArrayInputStream(content));
        // 上传
        PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 上传文件返回地址
     * @param fileName
     * @param content
     * @return
     * @throws Exception
     */
    public String uploadAndUrl(String fileName, byte[] content)  throws Exception {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(oss.getEndpoint(), oss.getAccessKeyId(), oss.getAccessKeySecret());
        // 判断是否图片
        String postStr = fileName.substring(fileName.lastIndexOf("."));
        if (Arrays.asList(imgArr).contains(postStr)){
            // 创建上传文件的元信息，可以通过文件元信息设置HTTP header(设置了才能通过返回的链接直接访问)。
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");
            // 文件上传
            ossClient.putObject(oss.getBucketName(), fileName, new ByteArrayInputStream(content), objectMetadata);
        }else {
            ossClient.putObject(oss.getBucketName(), fileName, new ByteArrayInputStream(content));
        }
        // 设置URL过期时间为1小时。
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        //返回url地址
        String url = ossClient.generatePresignedUrl(oss.getBucketName(), fileName, expiration).toString();

        if (StringUtils.hasLength(url)){
            if (url.contains("?")){
                url = url.substring(0,url.indexOf("?"));
            }
        }
        //关闭OSSClient。
        ossClient.shutdown();
        return url;
    }

    /**
     * 删除文件
     * @param fileName
     */
    public void deleteFile(String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(oss.getEndpoint(), oss.getAccessKeyId(), oss.getAccessKeySecret());
        // 删除文件。如需删除文件夹，请将fileName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(oss.getBucketName(), fileName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * zip生成目录结构
     * @param ze
     * @param zin
     * @param arr
     * @param parentName
     * @return
     * @throws Exception
     */
    public static ZipEntry getZipTreeUtil(ZipEntry ze, ZipInputStream zin, List arr, String parentName)throws Exception{
        while (ze != null) {
            boolean isbrother = false;
            ZipEntry beforeze = null;
            FileStructureVO vo = new FileStructureVO();
            if (ze.isDirectory()) {
                if(!ze.getName().contains(parentName)){
                    //回退一格
                    return ze;
                }
                List<FileStructureVO> child = new ArrayList<>();
                String zeName = ze.getName();
                String cutZeName =  zeName.substring(0,zeName.lastIndexOf("/"));
                vo.setName(cutZeName.substring(cutZeName.lastIndexOf("/")+1));
                vo.setSize(ze.getSize());
                vo.setChildren(child);
                arr.add(vo);
                ze = zin.getNextEntry();
                beforeze = getZipTreeUtil(ze,zin, child,zeName);
                ze = beforeze;
            } else {
                if(!ze.getName().contains(parentName)){
                    //不属于此文件夹的文件退出一层文件夹
                    return ze;
                }
                beforeze = null;
                vo.setName(ze.getName().substring(ze.getName().lastIndexOf("/")+1));
                vo.setSize(ze.getSize());
                arr.add(vo);
            }
            if(beforeze == null) ze = zin.getNextEntry();
        }
        return null;
    }
}
