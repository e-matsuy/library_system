package LibraryManager.service;

import LibraryManager.datamodel.AddCategoryRequest;
import LibraryManager.datamodel.Category;
import LibraryManager.util.DataBaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService extends AbstractService {
    private static final String DATA_GET_QUERY = "select * from categories;";
    private static final String DATA_INSERT_QUERY = "insert into categories (name) values (?) RETURNING id;";

    public Integer addCategory(AddCategoryRequest request) throws SQLException {
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

    private Category parseDbData(ResultSet resultSet) throws SQLException {
        return new Category(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }

    private List<Category> makeCategoryList(ResultSet resultSet) throws SQLException {
        List<Category> categoryList = new ArrayList<Category>();
        while (resultSet.next()) {
            categoryList.add(parseDbData(resultSet));
        }
        return categoryList;
    }

    public List<Category> getCategories() throws SQLException {
        ResultSet resultSet = null;
        List<Category> targets = null;
        dataBaseController = new DataBaseController();

        try {
            preparedStatement = dataBaseController.preparedStatement(DATA_GET_QUERY);
            resultSet = preparedStatement.executeQuery();
            targets = makeCategoryList(resultSet);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            close();
        }
        return targets;
    }
}
