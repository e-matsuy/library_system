package LibraryManager.service;

import LibraryManager.datamodel.AddPublisherRequest;
import LibraryManager.datamodel.Publisher;
import LibraryManager.util.DataBaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublisherService extends AbstractService {
    private static final String DATA_GET_QUERY = "select * from publishers;";
    private static final String DATA_INSERT_QUERY = "insert into publishers (name) values (?) RETURNING id;";

    public Integer addPublisher(AddPublisherRequest request) throws SQLException {
        Integer result = null;
        ResultSet resultSet = null;
        dataBaseController = new DataBaseController();

        try {
            preparedStatement = dataBaseController.preparedStatement(DATA_INSERT_QUERY);
            preparedStatement.setString(1, request.name());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("id");
                dataBaseController.commit();
                close();
            } else {
                dataBaseController.rollback();
                close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            close();
        }
        return result;
    }

    private Publisher parseDbData(ResultSet resultSet) throws SQLException {
        return new Publisher(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }

    private List<Publisher> makePublisherList(ResultSet resultSet) throws SQLException {
        List<Publisher> categoryList = new ArrayList<Publisher>();
        while (resultSet.next()) {
            categoryList.add(parseDbData(resultSet));
        }
        return categoryList;
    }

    public List<Publisher> getPuiblishers() throws SQLException {
        ResultSet resultSet = null;
        List<Publisher> targets = null;
        dataBaseController = new DataBaseController();

        try {
            preparedStatement = dataBaseController.preparedStatement(DATA_GET_QUERY);
            resultSet = preparedStatement.executeQuery();
            targets = makePublisherList(resultSet);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            close();
        }
        return targets;
    }
}
