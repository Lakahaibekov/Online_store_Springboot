package kz.shop.e_shop.services;

import kz.shop.e_shop.entities.Roles;
import kz.shop.e_shop.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    Users getUserByEmail(String email);
    Users addUser(Users user);
    List<Users> getAllUsers();
    Users getUser(Long id);
    void deleteUser(Users user);
    Users saveUser(Users user);


    Roles addRole(Roles role);
    List<Roles> getAllRoles();
    Roles getRole(Long id);
    void deleteRole(Roles role);
    Roles saveRole(Roles role);
}
