package org.shweta.docassistant.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shweta.docassistant.dto.UserDTO;
import org.shweta.docassistant.models.User;
import org.shweta.docassistant.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final ModelMapper modelMapper;


    public UserDTO registerUser(UserDTO userDTO){

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser, UserDTO.class);


    }


    public void loginUser(UserDTO userDTO) {
    }
}
