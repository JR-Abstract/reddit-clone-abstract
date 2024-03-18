package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.UserResponseDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    User map(RegisterRequest registerRequest);

    @Mapping(target = "userId", source = "userId")
    @InheritInverseConfiguration
    UserResponseDto map(User user);

}
