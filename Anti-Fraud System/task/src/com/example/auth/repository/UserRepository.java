package com.example.auth.repository;

import com.example.auth.entity.User;
import com.example.auth.entity.Operation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsernameIgnoreCase(String username);

    @Override
    Optional<User> findById(Long aLong);

    List<User> findAllByOrderById();

    @Modifying
    @Query("update User u set u.operation =:operation where u.id=:id")
    @Transactional
    void updateOperation(@Param(value = "id") long id, @Param(value = "operation") Operation operation);

    @Modifying
    @Query("update User u set u.role =:role where u.id=:id")
    @Transactional
    void updateRole(@Param(value = "id") long id, @Param(value = "role") String role);

    Optional<User> findUserByUsernameIgnoreCase(String username);
}
