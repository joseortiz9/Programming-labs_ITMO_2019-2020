package ru.students.lab.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.students.lab.database.CollectionModel;
import ru.students.lab.database.Credentials;
import ru.students.lab.database.SQLQuery;
import ru.students.lab.database.UserModel;
import ru.students.lab.exceptions.AuthorizationException;
import ru.students.lab.models.Dragon;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Controls and organize all the database requests
 * @autor Хосе Ортис
 * @version 1.0
 */
public class CollectionController {

    protected static final Logger LOG = LogManager.getLogger(CollectionController.class);
    private final CollectionModel collectionModel;
    private final UserModel userModel;

    public CollectionController(CollectionModel collectionModel, UserModel userModel) {
        this.collectionModel = collectionModel;
        this.userModel = userModel;
    }

    /**
     * Fetch the collection from the database
     *
     * @return collection that will be used as the local representation of the database
     * @throws SQLException the database sent an error
     */
    public HashMap<Integer, Dragon> fetchCollectionFromDB() throws SQLException {
        HashMap<Integer, Dragon> collection = collectionModel.fetchCollection();
        if (collection == null)
            throw new SQLException("It was not possible to fetch the collection from database");
        return collection;
    }

    /**
     *
     * @param credentials to try in the database
     * @return the credentials if the user was checked successfully and a str if was failed
     */
    public Object login(Credentials credentials) {
        try {
            int id = userModel.checkUserAndGetID(credentials);
            if (id > 0)
                return new Credentials(id, credentials.username, credentials.password);
            else
                return "User/Password given not found or incorrect";
        } catch (SQLException | NoSuchAlgorithmException ex) {
            LOG.error("logging in", ex);
            return ex.getMessage();
        }
    }

    public Object register(Credentials credentials) {
        try {
            int id = userModel.registerUser(credentials);
            if (id > 0)
                return new Credentials(id, credentials.username, credentials.password);
            else
                return credentials;
        } catch (Throwable ex) {
            LOG.error("registering user", ex);
            return ex.getMessage();
        }
    }

    public String addDragon(int key, Dragon dragon, Credentials credentials) {
        try {
            if (assertUserNotExist(credentials))
                throw new AuthorizationException();

            return collectionModel.insert(key, dragon, credentials);
        } catch (Throwable ex) {
            LOG.error("inserting dragon in db", ex);
            return ex.getMessage();
        }
    }

    public String updateDragon(int id, Dragon dragon, Credentials credentials) {
        try {
            if (assertUserNotExist(credentials))
                throw new AuthorizationException();

            return collectionModel.update(id, dragon, credentials);
        } catch (Throwable ex) {
            LOG.error("updating dragon in db", ex);
            return ex.getMessage();
        }
    }

    public String deleteAllDragons(Credentials credentials) {
        try {
            if (assertUserNotExist(credentials))
                throw new AuthorizationException();

            return collectionModel.deleteAll(credentials);
        } catch (Throwable ex) {
            LOG.error("deleting all dragons in db", ex);
            return ex.getMessage();
        }
    }

    public String deleteDragon(int key, Credentials credentials) {
        try {
            if (assertUserNotExist(credentials))
                throw new AuthorizationException();

            return collectionModel.delete(key, credentials);
        } catch (Throwable ex) {
            LOG.error("deleting dragon in db", ex);
            return ex.getMessage();
        }
    }

    public int[] deleteDragonsGreaterThanKey(int key, Credentials credentials) throws SQLException, NoSuchAlgorithmException {
        if (assertUserNotExist(credentials))
            throw new AuthorizationException();

        return collectionModel.deleteOnKey(key, credentials, SQLQuery.Delete.DRAGONS_WITH_GREATER_KEY);
    }

    public int[] deleteDragonsLowerThanKey(int key, Credentials credentials) throws SQLException, NoSuchAlgorithmException {
        if (assertUserNotExist(credentials))
            throw new AuthorizationException();

        return collectionModel.deleteOnKey(key, credentials, SQLQuery.Delete.DRAGONS_WITH_LOWER_KEY);
    }

    public boolean assertUserNotExist(Credentials credentials) throws SQLException, NoSuchAlgorithmException {
        return userModel.checkUserAndGetID(credentials) == -1;
    }
}
