package database;

import connection.User;
import exceptions.WrongArgumentException;
import labwork.*;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DBLabWork {
    Connection connection;
    private User user;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();
    private Lock readLock = lock.readLock();

    public DBLabWork(Connection connection) {
        this.connection = connection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void removeGreaterKey(String argument) {
        writeLock.lock();
        try (PreparedStatement statement = connection.prepareStatement(SQLQuery.REMOVE_GREATER_KEY.QUERY)) {
            statement.setString(1, argument);
            statement.setString(2, user.getLogin());
            statement.setString(3, DBUser.encryptString(user.getPassword()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
    }

    public void removeKey(String argument) throws WrongArgumentException{
        writeLock.lock();
        try (PreparedStatement statement = connection.prepareStatement(SQLQuery.REMOVE_BY_KEY.QUERY)) {
            statement.setString(1, argument);
            statement.setString(2, user.getLogin());
            statement.setString(3, DBUser.encryptString(user.getPassword()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new WrongArgumentException();
        }
        writeLock.unlock();
    }


    public void clear() {
        writeLock.lock();
        try (PreparedStatement statement = connection.prepareStatement(SQLQuery.CLEAR.QUERY)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, DBUser.encryptString(user.getPassword()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
    }

    public int getUserId(String login){
        try (PreparedStatement statement = connection.prepareStatement("SELECT * from \"user\" WHERE login=(?)")) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int create(LabWork labWork) throws WrongArgumentException {
        writeLock.lock();
        int result = 0;
        try (PreparedStatement statement = connection.prepareStatement("INSERT into \"labworks\"(name, lw_coord_x, lw_coord_y, creation_date, minimal_point, personal_q_min, average_point, difficulty, author_name, author_height, author_eye_color, author_hair_color, author_nationality, author_location_name, author_location_x, author_location_y, author_location_z, owner_id) values ((?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?), (?))")) {
            SetTOUpdate(labWork, statement);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongArgumentException();
        }
        writeLock.unlock();
        return result;
    }

    public HashMap<String, LabWork> read() {
        readLock.lock();
        HashMap<String, LabWork> labworks = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * from \"labworks\"")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LabWork labWork = getLabWork(resultSet);
                labworks.put(labWork.getName(), labWork);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return new HashMap<>();
        }
        readLock.unlock();
        return labworks;
    }

    public LabWork getLabWork(ResultSet resultSet) throws SQLException {
        try {
            Person author = null;
            Difficulty difficulty = null;
            if (resultSet.getString("author_name") != null) {
                author = new Person(resultSet.getString("author_name"),
                        resultSet.getInt("author_height"),
                        Color.valueOf(resultSet.getString("author_eye_color")),
                        Color.valueOf(resultSet.getString("author_hair_color")),
                        Country.valueOf(resultSet.getString("author_nationality")),
                        new Location(resultSet.getInt("author_location_x"), resultSet.getInt("author_location_y"), resultSet.getFloat("author_location_z"), resultSet.getString("author_location_name")));
            }
            if (resultSet.getString("difficulty") != null) {
                difficulty = Difficulty.valueOf(resultSet.getString("difficulty"));
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            LabWork labWork = new LabWork(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    new Coordinates(resultSet.getInt("lw_coord_x"), resultSet.getFloat("lw_coord_y")),
                    df.parse(resultSet.getString("creation_date")),
                    Long.parseLong(String.valueOf(resultSet.getInt("minimal_point"))),
                    resultSet.getInt("personal_q_min"),
                    resultSet.getFloat("average_point"),
                    difficulty,
                    author);
            labWork.setCreator_id(resultSet.getInt("owner_id"));
            return labWork;
        } catch (ParseException e) {
            System.out.println("Ошибка при парсинге даты");
        }
        return null;
    }

    public void update(LabWork labWork) throws WrongArgumentException{
        writeLock.lock();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE \"labworks\" SET name=(?), lw_coord_x=(?), lw_coord_y=(?),creation_date=(?), minimal_point=(?), personal_q_min=(?), average_point=(?), difficulty=(?), author_name=(?),author_height=(?), author_eye_color=(?), author_hair_color=(?), author_nationality=(?), author_location_name=(?), author_location_x=(?), author_location_y=(?), author_location_z=(?) WHERE name=(?) AND owner_id=(SELECT user_id from \"user\" where login=(?) and password=(?) limit 1)")) {
            SetTOUpdate(labWork, statement);
            statement.setString(18, labWork.getName());
            statement.setString(19, user.getLogin());
            statement.setString(20, DBUser.encryptString(user.getPassword()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongArgumentException();
        }
        writeLock.unlock();
    }

    public LabWork getByKey(String name) {

        LabWork labWork = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * from \"labworks\" WHERE name=(?)")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                labWork = getLabWork(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            readLock.unlock();
            return null;
        }
        return labWork;
    }

    private void SetTOUpdate(LabWork labWork, PreparedStatement statement) throws SQLException {
        statement.setString(1, labWork.getName());
        statement.setInt(2, labWork.getCoordinates().getX());
        statement.setFloat(3, labWork.getCoordinates().getY());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        statement.setString(4, df.format(labWork.getCreationDate()));
        statement.setLong(5, labWork.getMinimalPoint());
        statement.setLong(6, labWork.getPersonalQualitiesMinimum());
        statement.setFloat(7, labWork.getAveragePoint());
        if (labWork.getDifficulty() != null)
            statement.setString(8, labWork.getDifficulty().getName());
        else statement.setString(8, null);
        if (labWork.getAuthor() != null) {
            statement.setString(9, labWork.getAuthor().getName());
            statement.setLong(10, labWork.getAuthor().getHeight());
            statement.setString(11, labWork.getAuthor().getEyeColor().getName());
            statement.setString(12, labWork.getAuthor().getHairColor().getName());
            statement.setString(13, labWork.getAuthor().getNationality().getName());
            statement.setString(14, labWork.getAuthor().getLocation().getName());
            statement.setInt(15, labWork.getAuthor().getLocation().getX());
            statement.setInt(16, labWork.getAuthor().getLocation().getY());
            statement.setFloat(17, labWork.getAuthor().getLocation().getZ());
        } else {
            statement.setString(9, null);
            statement.setLong(10, 0);
            statement.setString(11, null);
            statement.setString(12, null);
            statement.setString(13, null);
            statement.setString(14, null);
            statement.setInt(15, 0);
            statement.setInt(16, 0);
            statement.setFloat(17, 0);
        }
        statement.setInt(18, labWork.getCreator_id());
    }


    private enum SQLQuery {
        CLEAR("DELETE FROM \"labworks\" WHERE owner_id=(SELECT user_id from \"user\" where login=(?) and password=(?) limit 1)"),
        REMOVE_BY_KEY("DELETE FROM \"labworks\" WHERE name=(?) AND owner_id=(SELECT user_id from \"user\" where login=(?) and password=(?) limit 1)"),
        REMOVE_GREATER_KEY("DELETE FROM \"labworks\" WHERE name>(?) AND owner_id=(SELECT user_id from \"user\" where login=(?) and password=(?) limit 1)");
        String QUERY;
        SQLQuery(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
