package com.lcwd.electronic.services;

import com.lcwd.electronic.dtos.PageableResponse;
import com.lcwd.electronic.dtos.UserDto;
import com.lcwd.electronic.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String userId);

    //delete
    void deleteUser(String userId);

    //get all user
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);

    //search
    List<UserDto> searchUser(String keyword);

    //other user specific features

    //for login With Google
    Optional<User> findUserByEmailOptional(String email);
}
