package vn.com.gsoft.importmaster.service;


import vn.com.gsoft.importmaster.model.system.Profile;

import java.util.Optional;

public interface UserService {
    Optional<Profile> findUserByToken(String token);

}
