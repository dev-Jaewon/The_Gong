/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.codestates.rtc;

import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class KurentoRoomManager {

  private final Logger log = LoggerFactory.getLogger(KurentoRoomManager.class);

  @Autowired
  private KurentoClient kurento;

  private final ConcurrentMap<String, KurentoRoom> rooms = new ConcurrentHashMap<>();

  /**
   * Looks for a room in the active room list.
   *
   * @param roomName
   *          the name of the room
   * @return the room if it was already created, or a new one if it is the first time this room is
   *         accessed
   */
  public KurentoRoom getRoom(String roomName) {
    log.debug("Searching for room {}", roomName);
    KurentoRoom kurentoRoom = rooms.get(roomName);

    if (kurentoRoom == null) {
      log.debug("Room {} not existent. Will create now!", roomName);
      kurentoRoom = new KurentoRoom(roomName, kurento.createMediaPipeline());
      rooms.put(roomName, kurentoRoom);
    }
    log.debug("Room {} found!", roomName);
    return kurentoRoom;
  }

  /**
   * Removes a room from the list of available rooms.
   *
   * @param kurentoRoom
   *          the room to be removed
   */
  public void removeRoom(KurentoRoom kurentoRoom) {
    this.rooms.remove(kurentoRoom.getName());
    kurentoRoom.close();
    log.info("Room {} removed and closed", kurentoRoom.getName());
  }

}
