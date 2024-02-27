package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    RegisterRequest userToRegisterRequest(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "enabled", defaultValue = "false")
    User registerRequestToUser(RegisterRequest registerRequest);
}
