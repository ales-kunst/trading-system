package com.apssouza.mytrade.feed.price;

import com.apssouza.mytrade.common.misc.helper.time.DateTimeConverter;
import com.apssouza.mytrade.feed.PriceDto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlPriceDao implements PriceDao {

    private final Connection connection;

    public SqlPriceDao(Connection connection) {
        this.connection = connection;
    }


    private List<PriceDto> getList(String query) {
        ArrayList<PriceDto> prices = new ArrayList<>();
        try {
            Statement sta = this.connection.createStatement();
            ResultSet resultSet = sta.executeQuery(query);
            while (resultSet.next()) {
                prices.add(bindResultToDto(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prices;
    }

    private PriceDto bindResultToDto(ResultSet resultSet) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(2);
        return new PriceDto(
                timestamp.toLocalDateTime(),
                resultSet.getBigDecimal(3),
                resultSet.getBigDecimal(4),
                resultSet.getBigDecimal(5),
                resultSet.getBigDecimal(6),
                resultSet.getString(8)
        );
    }

    @Override
    public List<PriceDto> getPriceInterval(LocalDateTime start, LocalDateTime end) {
        String query = String.format("select * from price where timestamp >= '%s' and timestamp <='%s'",
                DateTimeConverter.getDatabaseFormat(start),
                DateTimeConverter.getDatabaseFormat(end)
        );
        return getList(query);
    }

    @Override
    public List<PriceDto> getClosestPrice(LocalDateTime datetime) {
        String query = String.format("select * from price where timestamp <= '%s' LIMIT 1", datetime);
        List<PriceDto> list = getList(query);
        String query2 = String.format("select * from price where timestamp = '%s' LIMIT 1", list.get(0).getTimestamp());
        return getList(query2);
    }

}
