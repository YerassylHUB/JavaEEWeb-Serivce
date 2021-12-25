package com.example.systemcouse.repository;

import com.example.systemcouse.model.*;
import javax.ejb.Singleton;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserRepository {

    private static final String jdbcURL  = "jdbc:h2:~/CourseManagementSystem";
    private static final String jdbcUsername = "course";
    private static final String jdbcPass = "";

    private static final String INSERT_BASE_SQL = "insert into user_" +
            " (user_id, user_name, last_session, score_id) VALUES " + "(?, ?, ?, ?);";
    private static final String SELECT_ALL_BASE = "select * from user_";
    private static final String SELECT_BASE_BY_ID =
            "select user_name,  last_session, score.score_id, score_name FROM user_, score where user_.score_id = score.score_id and user_id = ?;";
    private static final String DELETE_BASE_SQL = "delete from user_ where user_id = ?;";
    private static final String UPDATE_BASE_SQL = "update user_ set user_name=?, last_session=?, score_id = ? where user_id = ?;";


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

    public List<Rank> selectRanksUsers() {
        List<Rank> ranks = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from rank")) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long rankId = rs.getLong("rank_id");
                String rankName = rs.getString("rank_name");
                ranks.add(new Rank(rankId, rankName));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ranks;
    }


    public User_ insertUser(User_ user){
        boolean check = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BASE_SQL)) {
            List<Rank> ranks = selectRanksUsers();
            Rank rank = null;
            for (Rank i: ranks) {
                if(i.getRankId() == user.getRank().getRankId())
                    rank = i;
            }
            if(rank != null) {
                preparedStatement.setLong(1, user.getUserId());
                preparedStatement.setString(2, user.getUserName());
                preparedStatement.setDate(3, Date.valueOf(user.getLastSession()));
                preparedStatement.setLong(4, user.getRank().getRankId());
                user.setRank(rank);
            }
            check = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(check)
            return user;
        else return new User_();
    }


    public List<User_> selectUsers() {
        List<User_> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BASE)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long userId = rs.getLong("user_id");
                String userName = rs.getString("user_name");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate lastSession = LocalDate.parse
                        (rs.getDate("last_session").toLocalDate().format(formatter),formatter);
                long rankId = rs.getLong("rank_id");
                List<Rank> ranks = selectRanksUsers();
                int index = 0;
                for (Rank i:ranks) {
                    int count = 0;
                    if(i.getRankId() == rankId)
                        index = count;
                    count++;
                }
                users.add(new User_(userId, userName, lastSession, ranks.get(index)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }


    public User_ selectUser(int userId)  {
        User_ user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BASE_BY_ID)) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String userName = rs.getString("user_name");
                LocalDate lastSession = rs.getDate("last_session").toLocalDate();
                long rank_id= rs.getLong("rank_id");
                String rankName = rs.getString("rank_name");
                Rank rank = new Rank(rank_id, rankName);
                user = new User_((long) userId, userName, lastSession, rank);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }


    public boolean deleteUser(int id) {
        boolean deleted = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BASE_SQL);) {
            statement.setLong(1, id);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return deleted;
    }

    public boolean updateUser(User_ user){
        boolean updated = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BASE_SQL);) {
            statement.setString(1, user.getUserName());
            statement.setDate(2, Date.valueOf(user.getLastSession()));
            statement.setLong(3, user.getRank().getRankId());
            statement.setLong(4, user.getUserId());

            updated = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return updated;
    }
}
