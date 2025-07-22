package org.example.db;

import org.example.TraceResult;
import java.sql.*;
import java.util.*;

public class PersistenceLayerDB extends PersistenceLayer implements AutoCloseable {
    Connection conn;

    public PersistenceLayerDB() {
        super();

        try {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:db/traceip.db");

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (this.conn != null && !this.conn.isClosed()) {
            conn.close();
        }
    }

    @Override
    public Optional<Double> queryAverageDistanceToBuenosAires() {
        Statement statement = null;
        try {
            statement = this.conn.createStatement();
            ResultSet resultSet= statement.executeQuery("SELECT * FROM average_distance_to_bsas LIMIT 1;");

            if (resultSet.next()) {
                double distance_sum = resultSet.getDouble("distance_sum");
                int count = resultSet.getInt("trace_results_count");

                return Optional.of(truncate(distance_sum / count));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) { /* Log or handle exception */ }
        }
    }

    @Override
    protected void updateAverageDistanceToBsAs(TraceResult traceResult) {
        Statement statement = null;
        try {
            statement = this.conn.createStatement();
            ResultSet resultSet= statement.executeQuery("SELECT * FROM average_distance_to_bsas LIMIT 1;");

            if (resultSet.next()) {
                double distance_sum = resultSet.getDouble("distance_sum");
                int count = resultSet.getInt("trace_results_count");

                statement.executeUpdate("""
                UPDATE average_distance_to_bsas
                SET distance_sum=%s, trace_results_count=%s;
                """.formatted(distance_sum + traceResult.distanceKilometersToBuenosAires(), count + 1));
            } else {
                statement.executeUpdate("""
                INSERT INTO average_distance_to_bsas(distance_sum, trace_results_count)
                VALUES (%s, %s);
                """.formatted(traceResult.distanceKilometersToBuenosAires(), 1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) { /* Log or handle exception */ }
        }
    }
}
