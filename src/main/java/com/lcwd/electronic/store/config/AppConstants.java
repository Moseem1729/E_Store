package com.lcwd.electronic.store.config;

import org.springframework.http.HttpStatus;

public class AppConstants {

    public static final String USER_NOT_FOUND = "User resource not found on server with Id: ";
    public static final String USER_NOT_FOUND_EMAIL = "User resource not found on server with Email: ";
    public static final String DELETE_USER = "User is successfully deleted !!";

    // Category Controller
    public static final String DELETE_CATEGORY = "Category is successfully deleted !!";
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "10";
    public static final String SORT_BY_TITLE = "title";
    public static final String SORT_BY_NAME = "name";

    public static final String SORT_DIR = "asc";

    // Category Service
    public static final String SORT_DIR_ASC_CHECK = "asc";
    public static final String CATEGORY_NOT_FOUND = "Category resource not found on server with Id: ";


    //file upload
    public static final String IMAGE_UPLOAD = "Image uploaded successfully !!";
    public static final boolean IMAGE_UPLOAD_SUCCESS = true;
    public static final HttpStatus HTTP_STATUS = HttpStatus.CREATED;

    public static final String FILE_EXTENSION_PNG = ".png";
    public static final String FILE_EXTENSION_JPG = ".jpg";
    public static final String FILE_EXTENSION_JPEG = ".jpeg";
    public static final String EXTENSION_NOT_ALLOWED = "File not allowed with extension: ";





    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";


}
