package com.example.systemcouse.repository;
import com.example.systemcouse.model.*;
import javax.ejb.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RankRepository {
    private static final String jdbcURL  = "jdbc:h2:~/CourseManagementSystem";
    private static final String jdbcUsername = "course";
    private static final String jdbcPass = "";

    private static final String INSERT_BASE_SQL = "insert into rank" +
            " (rank_id, rank_name) VALUES (?, ?);";
    private static final String SELECT_ALL_BASE = "select * from rank";
    private static final String SELECT_BASE_BY_ID = "select rank_id, rank_name from rank where rank_id=?";
    private static final String DELETE_BASE_SQL = "delete from rank where rank_id = ?;";
    private static final String UPDATE_BASE_SQL = "update rank set rank_name=? where rank_id = ?;";


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


    public List<Rank> selectRanks() {
        List<Rank> ranks = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BASE)) {
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


    public Rank insertRank(Rank rank) {
        boolean check = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BASE_SQL)) {
            preparedStatement.setLong(1, rank.getRankId());
            preparedStatement.setString(2, rank.getRankName());
            check = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(check)
            return rank;
        else return new Rank();
    }


    public Rank selectRank(int rankId) {
        Rank rank = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BASE_BY_ID)) {
            preparedStatement.setInt(1, rankId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String rankName = rs.getString("rank_name");
                rank = new Rank((long) rankId, rankName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rank;
    }


    public boolean deleteRank(int id) {
        boolean deleted = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BASE_SQL);) {
            statement.setLong(1, id);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return deleted;
    }


    public boolean updateRank(Rank rank) {
        boolean updated = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BASE_SQL);) {
            statement.setString(1, rank.getRankName());
            statement.setLong(2, rank.getRankId());

            updated = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return updated;
    }
}
