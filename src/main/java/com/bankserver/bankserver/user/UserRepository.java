package com.bankserver.bankserver.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { ;
}
