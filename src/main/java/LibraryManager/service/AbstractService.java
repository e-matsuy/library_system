package LibraryManager.service;

import LibraryManager.util.DataBaseController;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.util.Objects.nonNull;

public class AbstractService {
    protected DataBaseController dataBaseController = null;
    protected PreparedStatement preparedStatement = null;

    public void close() {
        try {
            if (nonNull(preparedStatement)) {
                if (!preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        try {
            if (nonNull(dataBaseController)) {
                if (!dataBaseController.isClosed()) {
                    dataBaseController.close();
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
}
