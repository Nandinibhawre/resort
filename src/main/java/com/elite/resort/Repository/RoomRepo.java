package com.elite.resort.Repository;

import com.elite.resort.Model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepo extends MongoRepository<Room, String> {}