package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.time.LocalDate;
import java.util.List;

/**
 * UserController is responsible for handling HTTP requests related to user operations.
 * It provides endpoints for retrieving and creating users.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    private final UserProvider userProvider;

    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) throws InterruptedException {
        User user = userMapper.toUser(userDto);
        User createdUser = userService.createUser(user);
        return userMapper.toUserDto(createdUser);
    }

    @GetMapping
    public List<UserDto> getUsers() throws InterruptedException {
       return this.userProvider.findAllUsers().stream()
                .map(this.userMapper::toUserDto)
                .toList();
    }

    @GetMapping("/simple")
    public List<UserDto> getSimpleUsers() {
        return this.userProvider.findAllUsers().stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(), null, null))
                .toList();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userProvider.getUser(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserDto(user);
    }

    @GetMapping("/email")
    public List<UserDto> searchUsersByEmail(@RequestParam String email) {
        return userProvider.searchUsersByEmailFragment(email).stream()
                .map(user -> new UserDto(user.getId(), null, null, null, user.getEmail()))
                .toList();
    }

    @GetMapping("/older/{date}")
    public List<UserDto> getUsersOlderThan(@PathVariable LocalDate date) {
        return userProvider.getUsersOlderThan(date).stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User updatedUser = userService.updateUser(userId, user);
        return userMapper.toUserDto(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}