package com.lcwd.electronic.services.impl;

import com.lcwd.electronic.dtos.PageableResponse;
import com.lcwd.electronic.dtos.UserDto;
import com.lcwd.electronic.entities.Role;
import com.lcwd.electronic.entities.User;
import com.lcwd.electronic.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.helper.Helper;
import com.lcwd.electronic.repositories.RoleRepository;
import com.lcwd.electronic.repositories.UserRepository;
import com.lcwd.electronic.services.UserService;
import lombok.Builder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${user.profile.image.path}")
    private String imagePath;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${normal.role.id}")
    private String normalRoleId;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        //generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        //encoding password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        //    dto to->entity
        User user = dtoToEntity(userDto);

        //fetch role of normal and set it to user
        Role role = roleRepository.findById(normalRoleId).get();
        user.getRoles().add(role);

        User saveUser = userRepository.save(user);
    //    entity to->dto
        UserDto newDto = entityToDto(saveUser);
        return newDto;
    }
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));
        user.setName(userDto.getName());
        //email update as you wish
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());
        //save data
        User updateUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updateUser);

        return updatedDto;
    }
    @Override
    public void deleteUser(String userId) {
        //get user by id then delete user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));
    //    images/users/abc.png
        String fullPath=imagePath+user.getImageName();
        try {
            Path path= Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("User Image not found in folder");
            ex.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        //      page number default start 0
        Pageable pageable= PageRequest.of(pageNumber, pageSize,sort);

        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));
       // UserDto userDto = entityToDto(user);
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found with given id !!"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }


    //login with google
    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }


    private UserDto entityToDto(User saveUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(saveUser.getUserId())
//                .name(saveUser.getName())
//                .email(saveUser.getEmail())
//                .password(saveUser.getPassword())
//                .gender(saveUser.getGender())
//                .about(saveUser.getAbout())
//                .imageName(saveUser.getImageName())
//                .build();
        return mapper.map(saveUser,UserDto.class);
    }
    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .gender(userDto.getGender())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .build();
        return mapper.map(userDto, User.class);
    }
}
