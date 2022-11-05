package com.charity.charity.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	String uploadFile(MultipartFile file);

	String deleteFile(String fileName);
}
