package com.khotixs.identity_service.mapper;

import com.khotixs.identity_service.domain.User;
import com.khotixs.identity_service.feature.user.dto.CustomerUserRegisterRequest;
import com.khotixs.identity_service.feature.user.dto.CustomerUserWithPhoneNumberRegisterRequest;
import com.khotixs.identity_service.feature.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    User fromUserRequest(CustomerUserRegisterRequest customerUserRegisterRequest);

    User fromUserWithPhoneRequest(CustomerUserWithPhoneNumberRegisterRequest customerUserRegisterRequest);

    UserResponse toUserResponse(User Shift);


//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateUserFromRequest(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
