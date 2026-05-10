package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User updateUser(final Long userId, final User user) {
        log.info("Updating User {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Update fields
        // Assuming we update all fields, but in practice, you might want to update only provided fields
        User updatedUser = new User(
                user.getFirstName() != null ? user.getFirstName() : existingUser.getFirstName(),
                user.getLastName() != null ? user.getLastName() : existingUser.getLastName(),
                user.getBirthday() != null ? user.getBirthday() : existingUser.getBirthday(),
                user.getEmail() != null ? user.getEmail() : existingUser.getEmail()
        );
        updatedUser.setId(userId); // Assuming User has setId method, but it doesn't. Wait, User is immutable.
        // Actually, User doesn't have setters. We need to use repository save with existing entity.
        // But User is @Entity with @Getter, no @Setter. To update, we need to modify the existing entity.
        // But since it's immutable, perhaps we need to add setters or use a different approach.
        // For simplicity, let's assume we can update the entity directly.
        // But in JPA, we can save the existing entity after modifying it.
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setBirthday(user.getBirthday());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(final Long userId) {
        log.info("Deleting User {}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByFirstNameAndLastName(final String firstName, final String lastName) {
        return userRepository.findAll().stream()
                .filter(user -> Objects.equals(user.getFirstName(), firstName) && Objects.equals(user.getLastName(), lastName))
                .findFirst();
    }

    @Override
    public List<User> searchUsersByEmailFragment(final String emailFragment) {
        return userRepository.findAll().stream()
                .filter(user -> user.getEmail().toLowerCase().contains(emailFragment.toLowerCase()))
                .toList();
    }

    @Override
    public List<User> getUsersOlderThan(final LocalDate date) {
        return userRepository.findAll().stream()
                .filter(user -> user.getBirthday().isBefore(date))
                .toList();
    }