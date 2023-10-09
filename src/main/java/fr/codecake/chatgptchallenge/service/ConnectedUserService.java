package fr.codecake.chatgptchallenge.service;


import fr.codecake.chatgptchallenge.domain.User;
import fr.codecake.chatgptchallenge.flow.message.service.dto.FlowMessageQueryDTO;
import fr.codecake.chatgptchallenge.repository.ProfileRepository;
import fr.codecake.chatgptchallenge.security.SecurityUtils;
import fr.codecake.chatgptchallenge.service.dto.ProfileDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class ConnectedUserService {

    private final UserService userService;
    private final ProfileService profileService;

    public ConnectedUserService(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    public ProfileDTO getProfileConnectedUser() {
        User connectedUser = getConnectedUser();
        return profileService.findOneByUserEmail(connectedUser.getLogin())
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    format("no profile found in the DB for the following login : %s",
                        connectedUser.getLogin())));
    }

    private User getConnectedUser() {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new UsernameNotFoundException(
                "no connected user found"));

        return userService.getUserWithAuthoritiesByLogin(currentUserLogin).orElseThrow(() -> new UsernameNotFoundException(
            format("no user found in the DB for the following login %s", currentUserLogin)));
    }


}
