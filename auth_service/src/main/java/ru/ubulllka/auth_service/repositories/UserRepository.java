package ru.ubulllka.auth_service.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.ubulllka.auth_service.models.UserDTO;
import ru.ubulllka.auth_service.models.UserImpl;

import java.util.List;

@Repository
public class UserRepository {

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate template;
    private final static String HASH_KEY = "User";

    public UserImpl save(UserImpl userImpl) {
        UserDTO userDto = new UserDTO(userImpl.getName(), userImpl.getPassword(), userImpl.getRole());
        template.opsForHash().put(HASH_KEY, userImpl.getName(), userDto);
        return userImpl;
    }

    public List<UserImpl> findAll() {
        return template.opsForHash().values(HASH_KEY);
    }

    public UserImpl findUserById(int id) {

        return (UserImpl) template.opsForHash().get(HASH_KEY, id);
    }

    public UserDTO findUserByUsername(String username) {
        Object object = template.opsForHash().get(HASH_KEY, username);
        if (object != null) {
            String parse = object.toString();
            System.out.println(parse);
            String[] temp = parse.split(",");
            String name = temp[0].substring(temp[0].indexOf("name=") + 5);
            String password = temp[1].substring(temp[1].indexOf("$"));
            String role = temp[2].substring(temp[2].indexOf("=") + 1, temp[2].length() - 1);
            return new UserDTO(name, password, role);
        } else {
            return null;
        }
    }

    public String deleteUser(String username) {
        template.opsForHash().delete(HASH_KEY, username);
        return "User removed";
    }
}
