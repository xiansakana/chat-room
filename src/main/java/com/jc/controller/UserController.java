package com.jc.controller;

import com.jc.entity.Result;
import com.jc.entity.User;
import com.jc.service.UserService;
import com.jc.utils.Md5Util;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^[a-zA-Z0-9)]{1,16}$") String username,
                           @Pattern(regexp = "^\\S{1,16}$") String password) {


        User u = userService.findByUsername(username);
        if (u == null) {

            userService.register(username, password);
            return Result.success();
        }else{
            return Result.error("username has been used");
        }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^[a-zA-Z0-9)]{1,16}$") String username,
                        @Pattern(regexp = "^\\S{1,16}$") String password){

        User loginUser = userService.findByUsername(username);

        if(loginUser==null){
            return Result.error("wrong username or password ");
        }


        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){

            return Result.success();
        }

        return Result.error("wrong username or password");
    }
}
