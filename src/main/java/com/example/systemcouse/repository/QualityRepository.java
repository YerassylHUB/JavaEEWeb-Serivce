package com.example.systemcouse.repository;

import com.example.systemcouse.model.Quality;
import com.example.systemcouse.model.Tutor;

import javax.ejb.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class QualityRepository {
    private static final String jdbcURL  = "jdbc:h2:~/CourseManagementSystem";
    private static final String jdbcUsername = "course";
    private static final String jdbcPass = "";

    private static final String INSERT_BASE_SQL = "insert into quality" +
            "(quality_id, quality_name, tutor_id) VALUES " + "(?, ?, ?);";
    private static final String SELECT_ALL_BASE = "select * from quality";
    private static final String SELECT_BASE_BY_ID = "select quality_name, quality.tutor_id FROM quality, tutor " +
            "where quality.tutor_id = tutor.tutor_id and quality_id = ?;";
    private static final String DELETE_BASE_SQL = "delete from quality where quality_id = ?;";
    private static final String UPDATE_BASE_SQL = "update quality set quality_name=?, tutor_id = ? " +
            "where quality_id = ?;";

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

    public List<Tutor> selectTutorsQualities() {

        List<Tutor> tutors = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from tutor")) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long tutor_id = rs.getLong("tutor_id");
                String tutorName = rs.getString("tutor_name");
                String description = rs.getString("description");
                String specialization = rs.getString("specialization");
                tutors.add(new Tutor(tutor_id, tutorName, specialization, description));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tutors;
    }

    public Quality insertQualities(Quality quality) {
        boolean check = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BASE_SQL)) {
            List<Tutor> tutors = selectTutorsQualities();
            Tutor tutor = null;
            for (Tutor i: tutors) {
                if(i.getTutorId() == quality.getTutor().getTutorId())
                    tutor = i;
            }
            if(tutor != null) {
                preparedStatement.setLong(1, quality.getQualityId());
                preparedStatement.setString(2, quality.getQualityName());
                preparedStatement.setLong(3, tutor.getTutorId());
                quality.setTutor(tutor);
            }
            check = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(check)
            return quality;
        else return new Quality();
    }

    public List<Quality> selectQualities() {

        List<Quality> qualities = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BASE)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long qualityId = rs.getLong("quality_id");
                String qualityName = rs.getString("quality_name");
                long tutorId = rs.getLong("tutor_id");
                List<Tutor> tutors = selectTutorsQualities();
                int index = 0;
                for (Tutor i : tutors) {
                    int count = 0;
                    if (i.getTutorId() == tutorId)
                        index = count;
                    count++;
                }
                qualities.add(new Quality(qualityId, qualityName, tutors.get(index)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return qualities;
    }

    public Quality selectQuality(int qualityId) {
        Quality quality = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BASE_BY_ID)) {
            preparedStatement.setInt(1, qualityId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String qualityName = rs.getString("quality_name");
                long tutorId = rs.getLong("tutor_id");
                List<Tutor> tutors = selectTutorsQualities();
                int index = 0;
                for (Tutor i : tutors) {
                    int count = 0;
                    if (i.getTutorId() == tutorId)
                        index = count;
                    count++;
                }
                quality = new Quality((long) qualityId, qualityName, tutors.get(index));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quality;
    }

    public boolean deleteQuality(int id) {
        boolean deleted = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BASE_SQL);) {
            statement.setLong(1, id);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return deleted;
    }

    public boolean updateQuality(Quality quality) {
        boolean updated = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BASE_SQL);) {
            statement.setString(1, quality.getQualityName());
            statement.setLong(2, quality.getTutor().getTutorId());
            statement.setLong(3, quality.getQualityId());

            updated = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return updated;
    }
}
