package com.yar.iot.queryportal.repository.user;

import com.yar.iot.queryportal.domain.document.UserDocument;
import com.yar.iot.queryportal.repository.BaseRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * The user repository for database communication
 *
 * @see BaseRepository
 * */
@Repository
public interface UserRepository extends BaseRepository<UserDocument> {
    Optional<UserDocument> findByUsername(String username);
}
