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
     * @author Vrushali Sonawane
     * @Apinote add user
     * @param userDto
     * @return userDto
     */
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        log.info("Initiating request to create user");
       UserDto createdUser= userServiceI.createUser(userDto);
        log.info("Completing request to create user");
       return new ResponseEntity<UserDto>(createdUser, HttpStatus.CREATED);
    }

    //updateUser

    /**
     * @Author Vrushali Sonawane
     * @Apinote update single user
     * @param userDto
     * @param userId
     * @return userDto
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> upadteUser(@Valid @RequestBody UserDto userDto,
                                              @PathVariable("userId") String userId){
        log.info("Initiating request to update user:{}"+userId);
      UserDto updatedUser=  userServiceI.updateUser(userDto,userId);
        log.info("Completing request  to update user:{}"+userId);
      return new ResponseEntity<>(updatedUser,HttpStatus.CREATED);

    }

    //getSingleUserByID

    /**
     * @Author Vrushali Sonawane
     * @apiNote get single user
     * @param userId
     * @return single user
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUSerById(@PathVariable("userId") String userId){
        log.info("Initiating request to get single user:{}"+userId);
        UserDto userDto=userServiceI.getUserById(userId);
        log.info("Completing request to update user:{}"+userId);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
    //getUserByEmail

    /**
     * @Author Vrushali Sonawane
     * @apiNote  get user by email
     *@param email
     * @return single user
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email){
        log.info("Initiating request to get single  user by email:{}"+email);
       UserDto userDto1= userServiceI.getUserByEmail(email);
        log.info("Completing request to get single  user by email:{}"+email);
       return new ResponseEntity<>(userDto1,HttpStatus.OK);
    }
    //deleteUser

    /**
     * @Author Vrushali Sonawane
     * @ApiNote delete Single user
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId){
        log.info("Initiating request to delete single user:{}"+userId);
        userServiceI.deleteUser(userId);

        log.info("Completing  request to delete single user:{}"+userId);
        return new ResponseEntity<>(AppConstants.DELETE_USER,HttpStatus.OK);
    }

    //searchUser

    /**
     * @author Vrushali Sonawane
     * @Apinote search users by keyword
     * @param keyword
     * @return list of users
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keyword")String keyword){
        log.info("Initiating request to get users:{}"+keyword);
     List<UserDto> userDtos=   userServiceI.searchUser(keyword);
        log.info("Initiating request to get users:{}"+keyword);
     return new ResponseEntity<>(userDtos,HttpStatus.OK);

    }

    //getAllUser

    /**
     * @Author Vrushali Sonawane
     * @ApiNote get All users record
     * @return List of users
     */
    @GetMapping("/")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false)int pageNumber,
            @RequestParam(value="pageSize",defaultValue ="10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        log.info("Initiating request to get All users");
      PageableResponse<UserDto> allUsers= userServiceI.getAllUser(pageNumber, pageSize,sortBy,sortDir);
        log.info("completing  request to get All users");
      return new ResponseEntity<>(allUsers,HttpStatus.OK);
    }

    //upload user Image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {

        String imageName = fileServiceI.uploadFile(image, imageUploadPath);

        //save the image of particular user
        UserDto user = userServiceI.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userServiceI.updateUser(user, userId);

        ImageResponse response=ImageResponse.builder()
                .imageName(imageName).message("Image is uploaded successfully").success(true).status(HttpStatus.CREATED).build();

        return  new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //serve user Image
    @GetMapping("image/{userId}")
    public void  serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        UserDto user = userServiceI.getUserById(userId);
        log.info("User image name :{} " ,user.getImageName());

        InputStream resource = fileServiceI.getResource(imageUploadPath, user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());

    }


}
