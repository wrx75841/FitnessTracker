package pl.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserProvider {

    /**
     * Retrieves a user based on their ID.
     * If the user with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param userId id of the user to be searched
     * @return An {@link Optional} containing the located user, or {@link Optional#empty()} if not found
     */
    Optional<User> getUser(Long userId);

    /**
     * Retrieves a user based on their email.
     * If the user with given email is not found, then {@link Optional#empty()} will be returned.
     *
     * @param email The email of the user to be searched
     * @return An {@link Optional} containing the located user, or {@link Optional#empty()} if not found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Retrieves a user based on their first name and last name.
     * If the user with given names is not found, then {@link Optional#empty()} will be returned.
     *
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @return An {@link Optional} containing the located user, or {@link Optional#empty()} if not found
     */
    Optional<User> getUserByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Retrieves all users.
     *
     * @return An {@link Optional} containing the all users,
     */
    List<User> findAllUsers();

    /**
     * Searches for users by email fragment, case-insensitive.
     *
     * @param emailFragment The fragment of the email to search for
     * @return A list of users whose email contains the fragment
     */
    List<User> searchUsersByEmailFragment(String emailFragment);

    /**
     * Retrieves users older than the specified date (born before the date).
     *
     * @param date The date to compare birthdays against
     * @return A list of users born before the specified date
     */
    List<User> getUsersOlderThan(LocalDate date);

}
