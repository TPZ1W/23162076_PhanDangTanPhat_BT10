package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.User;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    void save(User user);
    void deleteById(Long id);
    List<User> search(String keyword);
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
}
