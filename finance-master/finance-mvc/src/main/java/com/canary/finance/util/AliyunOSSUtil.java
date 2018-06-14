package com.canary.finance.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;

public class AliyunOSSUtil {
	public static final String BUCKETNAME = "jinzhujinfu";
	public static final String ALIYUN_OSS = "https://jinzhujinfu.oss-cn-hangzhou.aliyuncs.com";
	public static String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
	public static String KEY = "LTAIlkhjTlvO9wFD";
	public static String SECRET = "uGd2kFfinpqlq4RjnUH2oFFaxLFuks";
	public static OSSClient client = new OSSClient(ENDPOINT, KEY, SECRET);
	
	public static void uploadFile(String bucketName, String key, String filename) throws OSSException, ClientException, IOException {
		File file = new File(filename);
		if(file.exists()) {
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(file.length());
			try(InputStream input = new FileInputStream(file)) {
				client.putObject(bucketName, key, input, objectMeta);
			}
		}
	}
	
	public static void uploadFile(String bucketName, String key, InputStream stream) throws OSSException, ClientException, IOException {
		if(stream != null && stream.available() > 0) {
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(stream.available());
			client.putObject(bucketName, key, stream, objectMeta);
		}
	}
	
	public static void ensureBucket(String bucketName) throws OSSException, ClientException {
		client.createBucket(bucketName);
	}

	public static void deleteBucket(String bucketName) throws OSSException, ClientException {
		ObjectListing objects = client.listObjects(bucketName);
		if(objects != null) {
			List<OSSObjectSummary> listDeletes = objects.getObjectSummaries();
			for (OSSObjectSummary listDelete : listDeletes) {
				String objectName = listDelete.getKey();
				client.deleteObject(bucketName, objectName);
			}
		}
		client.deleteBucket(bucketName);
	}
	
	public static void deleteObject(String bucketName, String key) throws OSSException, ClientException {
		client.deleteObject(bucketName, key);
	}

	public static void setBucketPublicReadable(String bucketName) throws OSSException, ClientException {
		client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
	}
}