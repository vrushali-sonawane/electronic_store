package com.bikkadit.electronic_store.controller;

import com.bikkadit.electronic_store.dto.UserDto;
import com.bikkadit.electronic_store.entity.User;
import com.bikkadit.electronic_store.payload.AppConstants;
import com.bikkadit.electronic_store.payload.ImageResponse;
import com.bikkadit.electronic_store.payload.PageableResponse;
import com.bikkadit.electronic_store.service.FileServiceI;
import com.bikkadit.electronic_store.service.UserServiceI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceI userServiceI;

    @Autowired
    private FileServiceI fileServiceI;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //createUser

    /**
     * @param userDto
     * @return userDto
     * @author Vrushali Sonawane
     * @Apinote add user
     */
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Initiating request to create user");
        UserDto createdUser = userServiceI.createUser(userDto);
        log.info("Completing request to create user");
        return new ResponseEntity<UserDto>(createdUser, HttpStatus.CREATED);
    }

    //updateUser

    /**
     * @param userDto
     * @param userId
     * @return userDto
     * @Author Vrushali Sonawane
     * @Apinote update single user
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> upadteUser(@Valid @RequestBody UserDto userDto,
                                              @PathVariable("userId") String userId) {
        log.info("Initiating request to update user:{}" , userId);
        UserDto updatedUser = userServiceI.updateUser(userDto, userId);
        log.info("Completing request  to update user:{}" , userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);

    }

    //getSingleUserByID

    /**
     * @param userId
     * @return single user
     * @Author Vrushali Sonawane
     * @apiNote get single user
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUSerById(@PathVariable("userId") String userId) {
        log.info("Initiating request to get single user:{}" , userId);
        UserDto userDto = userServiceI.getUserById(userId);
        log.info("Completing request to update user:{}" , userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    //getUserByEmail

    /**
     * @param email
     * @return single user
     * @Author Vrushali Sonawane
     * @apiNote get user by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        log.info("Initiating request to get single  user by email:{}" , email);
        UserDto userDto1 = userServiceI.getUserByEmail(email);
        log.info("Completing request to get single  user by email:{}" , email);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
    }
    //deleteUser

    /**
     * @param userId
     * @return
     * @Author Vrushali Sonawane
     * @ApiNote delete Single user
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId) {
        log.info("Initiating request to delete single user:{}" , userId);
        userServiceI.deleteUser(userId);

        log.info("Completing  request to delete single user:{}" , userId);
        return new ResponseEntity<>(AppConstants.DELETE_USER, HttpStatus.OK);
    }

    //searchUser

    /**
     * @param keyword
     * @return list of users
     * @author Vrushali Sonawane
     * @Apinote search users by keyword
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<UserDto>> searchUser(@PathVariable("keyword") String keyword,
       @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)int pageNumber,
        @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)int pageSize,
        @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
        @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        log.info("Initiating request to get users:{}" , keyword);
        PageableResponse<UserDto> userDtos = userServiceI.searchUser(keyword,pageNumber,pageSize,sortBy,sortDir);
        log.info("Initiating request to get users:{}" , keyword);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);

    }

    //getAllUser

    /**
     * @return List of users
     * @Author Vrushali Sonawane
     * @ApiNote get All users record
     */
    @GetMapping("/")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        log.info("Initiating request to get All users");
        PageableResponse<UserDto> allUsers = userServiceI.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        log.info("completing  request to get All users");
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    //upload user Image

    /**
     * @param image
     * @param userId
     * @return
     * @throws IOException
     * @author Vrushali Sonawane
     * @apiNote upload user profile
     */
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        log.info("Initiating request to upload user image: {}", userId);
        String imageName = fileServiceI.uploadFile(image, imageUploadPath);

        //save the image of particular user
        UserDto user = userServiceI.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userServiceI.updateUser(user, userId);

        ImageResponse response = ImageResponse.builder()
                .imageName(imageName).message(AppConstants.IMAGE_UPLOAD).success(true).status(HttpStatus.CREATED).build();
        log.info("Initiating request to upload category image: {}", userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //serve user Image

    /**
     * @param userId
     * @param response
     * @throws IOException
     * @author Vrushali Sonawane
     * @apiNote download user image
     */
    @GetMapping("image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        log.info("Initiating request to download user image: {}", userId);
        UserDto user = userServiceI.getUserById(userId);
        log.info("User image name :{} ", user.getImageName());

        InputStream resource = fileServiceI.getResource(imageUploadPath, user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource, response.getOutputStream());
        log.info("completed request to download user image: {}", userId);

    }


}
