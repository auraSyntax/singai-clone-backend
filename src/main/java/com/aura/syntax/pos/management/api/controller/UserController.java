package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.api.dto.UserDto;
import com.aura.syntax.pos.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173","https://singai-pos.onrender.com"})
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseDto saveUser(@RequestBody UserDto userDto){
        return userService.saveUser(userDto);
    }

    @GetMapping("/list")
    public List<UserDto> getListOfUsers(@RequestParam(value = "search",required = false) String search){
        return userService.getListOfUsers(search);
    }

    @GetMapping("/get-all")
    public PaginatedResponseDto<UserDto> getAllUsersPagination(@RequestParam(value = "page") Integer page,
                                                                 @RequestParam(value = "size") Integer size,
                                                                 @RequestParam(value = "search",required = false) String search){
        return userService.getAllUsersPagination(page,size,search);
    }

    @GetMapping("/get-by-id")
    public UserDto findUserById(@RequestParam(value = "id") Long id){
        return userService.findUserById(id);
    }

    @PutMapping
    public ResponseDto updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @PutMapping("/update-status")
    public ResponseDto updateStatus(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "status") Boolean status){
        return userService.updateStatus(id,status);
    }

    @DeleteMapping
    public ResponseDto deleteUser(@RequestParam(value = "id") Long id){
        return userService.deleteUser(id);
    }

}
