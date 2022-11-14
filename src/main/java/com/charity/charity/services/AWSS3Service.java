package com.charity.charity.services;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class AWSS3Service implements FileService{
	@Autowired
	private AmazonS3Client awsS3Client;

	@Value("${aws_s3.bucket_name}")
	String bucketName;


	@Override
	public String uploadFile(MultipartFile file) {


		String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

		String key = UUID.randomUUID().toString() + "." + filenameExtension;


		//For upload in heroku need to use path - "/tmp"
		//For upload in local server need to use path - "/static"
			File tempFile = new File("/tmp", key);
		try {
			//make file less
			Thumbnails.of(file.getInputStream()).size(800, 800).toFile(tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setContentLength(tempFile.length());
		metaData.setContentType(filenameExtension);

		try {
			awsS3Client.putObject(new PutObjectRequest(bucketName, key, new FileInputStream(tempFile), metaData));
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occured while uploading the file");
		}
		
		awsS3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);

		//delete temp file
		tempFile.delete();

		//return awsS3Client.getResourceUrl(bucketName, key); //return public url for file
		return key; //return name of file
	}


	@Override
	public String deleteFile(String fileName) {
		awsS3Client.deleteObject(bucketName, fileName);
		return "successful delete";
	}
}
