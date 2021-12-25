package com.example.systemcouse.repository;

import com.example.systemcouse.model.Tutor;

import javax.ejb.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TutorRepository {
    private static final String jdbcURL  = "jdbc:h2:~/CourseManagementSystem";
    private static final String jdbcUsername = "course";
    private static final String jdbcPass = "";

    private static final String INSERT_BASE_SQL = "insert into tutor" +
            " (tutor_id, tutor_name, description, specialization) VALUES " + "(?, ?, ?, ?);";
    private static final String SELECT_ALL_BASE = "select * from tutor";
    private static final String SELECT_BASE_BY_ID = "select tutor_name, description, specialization " +
            "FROM agent where agent_id = ?;";
    private static final String DELETE_BASE_SQL = "delete from tutor where tutor_id = ?;";
    private static final String UPDATE_BASE_SQL = "update tutor set tutor_name=?, description=?, " +
            "specialization=? where tutor_id = ?;";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPass);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Tutor insertTutor(Tutor tutor) {
        boolean check = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BASE_SQL)) {
            preparedStatement.setLong(1, tutor.getTutorId());
            preparedStatement.setString(2, tutor.getTutorName());
            preparedStatement.setString(3, tutor.getDescription());
            preparedStatement.setString(4, tutor.getSpecialization());
            check = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(check)
            return tutor;
        else return new Tutor();
    }

    public List<Tutor> selectTutors() {

        List<Tutor> tutors = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BASE)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long tutorId = rs.getLong("tutor_id");
                String tutorName = rs.getString("tutor_name");
                String description = rs.getString("description");
                String specialization = rs.getString("specialization");
                tutors.add(new Tutor(tutorId, tutorName, specialization, description));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tutors;
    }


    public Tutor selectTutor(int tutorId) {
        Tutor tutor = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BASE_BY_ID)) {
            preparedStatement.setInt(1, tutorId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String agentName = rs.getString("tutor_name");
                String specialization = rs.getString("specialization");
                String description = rs.getString("description");
                tutor = new Tutor((long) tutorId, agentName, specialization, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutor;
    }

    public boolean deleteTutor(int id) {
        boolean deleted = false;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BASE_SQL)) {
            statement.setLong(1, id);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return deleted;
    }

    public boolean updateTutor(Tutor tutor) {
        boolean updated = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BASE_SQL);) {
            statement.setString(1, tutor.getTutorName());
            statement.setString(2, tutor.getDescription());
            statement.setString(3, tutor.getSpecialization());
            statement.setLong(4, tutor.getTutorId());

            updated = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return updated;
    }
}
