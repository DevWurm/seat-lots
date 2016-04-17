package net.devwurm.seatlots.location.configuration;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.devwurm.seatlots.location.DuplicateRoomException;


/**
 * Class for describing a room list configuration
 */
public class RoomListConfiguration {
    private StringProperty name;
    private ObservableList<RoomConfiguration> configuration = FXCollections.observableArrayList();

    public RoomListConfiguration(String name) {
        this.name = new SimpleStringProperty();
        this.name.setValue(name);
    }

    public RoomListConfiguration(StringProperty name) {
        this.name = name;
    }

    public void addRoom(Integer room, Integer capacity) {
        RoomConfiguration tempRoomConfiguration = new RoomConfiguration(room, capacity);

        if (!configuration.contains(tempRoomConfiguration)) {
            configuration.add(tempRoomConfiguration);

            tempRoomConfiguration.numberProperty().addListener((prop, oldValue, newValue) -> {
                if(newValue.equals(0)) {
                    configuration.remove(tempRoomConfiguration);
                }
            });

            tempRoomConfiguration.capacityProperty().addListener((prop, oldValue, newValue) -> {
                if(newValue.equals(0)) {
                    configuration.remove(tempRoomConfiguration);
                }
            });
        } else {
            throw new DuplicateRoomException("Room already included in configuration", room);
        }
    }

    public void addRoom(String room, String capacity) {
        Integer roomInt;
        Integer capacityInt;

        try {
            roomInt = Integer.parseInt(room);
        } catch (NumberFormatException e) {
            throw new IllegalRoomStringException(e.getMessage(), room);
        }

        try {
            capacityInt = Integer.parseInt(capacity);
        } catch (NumberFormatException e) {
            throw new IllegalCapacityStringException(e.getMessage(), capacity);
        }

        addRoom(roomInt, capacityInt);
    }

    public void removeRoom (RoomConfiguration room) {
        configuration.remove(room);
    }

    public void removeRoom(Integer room) {
        RoomConfiguration roomConf = new RoomConfiguration(room, 0);
        removeRoom(roomConf);
    }

    public void removeRoom(String room) {
        Integer roomInt;

        try {
            roomInt = Integer.parseInt(room);
        } catch (NumberFormatException e) {
            throw new IllegalRoomStringException(e.getMessage(), room);
        }

        removeRoom(roomInt);
    }

    public void updateRoom(Integer room, Integer newCapacity) {
        RoomConfiguration tempRoomConfiguration = new RoomConfiguration(room, newCapacity);

        if (configuration.contains(tempRoomConfiguration)) {
            configuration.remove(tempRoomConfiguration);
            configuration.add(tempRoomConfiguration);
        } else {
            throw new RoomNotPresentException("Room not in configuration", room);
        }
    }

    public void updateRoom(String room, String newCapacity) {
        Integer roomInt;
        Integer newCapacityInt;

        try {
            roomInt = Integer.parseInt(room);
        } catch (NumberFormatException e) {
            throw new IllegalRoomStringException(e.getMessage(), room);
        }

        try {
            newCapacityInt = Integer.parseInt(newCapacity);
        } catch (NumberFormatException e) {
            throw new IllegalCapacityStringException(e.getMessage(), newCapacity);
        }

        updateRoom(roomInt, newCapacityInt);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObservableList<RoomConfiguration> getConfiguration() {
        return configuration;
    }

}