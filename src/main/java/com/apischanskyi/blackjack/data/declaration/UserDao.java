package com.apischanskyi.blackjack.data.declaration;

import com.apischanskyi.blackjack.entity.User;

public interface UserDao {

    User getUserById(long playerId);

    void update(User user);
}
