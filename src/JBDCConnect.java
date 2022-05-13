import java.sql.*;
import java.util.Scanner;

public class JBDCConnect {
    // План лекции
    //1. Что такое JDBC, подключение базы данных
    //2. SQL, CRUD-операции
    // CREATE (table, values) READ UPDATE DELETE (table, values)
    //3. CREATE TABLE,INSERT, SELECT, UPDATE, DELETE
    static String url = "jdbc:sqlite:mydb.db";
    static Connection connection;
    static Statement statement;
    // Connection
    // Statement PreparedStatement
    // ResultSet

    public static void connect() throws ClassNotFoundException, SQLException {
        // 1. Загрузка JDBC
        Class.forName("org.sqlite.JDBC");
        // 2. Установить соединение с базой данных
        connection = DriverManager.getConnection(url);
    }
    public static void disconnect() throws SQLException {
        if(!connection.isClosed()){
            connection.close();
        }
    }
    public static void createTablePeoples() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS peoples("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"name VARCHAR(50),"
                +"lastname VARCHAR(50))";
        statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }
    public  static  void updateData(String oldName, String newName)throws SQLException{
        String query = "UPDATE peoples SET name = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,newName);
        preparedStatement.setString(2,oldName);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public static void deleteRow(String name) throws SQLException {
        String query = "DELETE FROM  peoples WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,name);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public static void selectAll() throws SQLException {
        String query = "SELECT * FROM peoples";
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()){
            System.out.println("id " + rs.getInt("id")
                            + " name " + rs.getString("name")
                            + " lastname" + rs.getString(3));
        }
        rs.close();
        statement.close();
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        connect();
        System.out.println("Connect");
        createTablePeoples();
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < count; i++) {
            System.out.println("name");
            String name = sc.nextLine();
            System.out.println("lastname");
            String lastname = sc.nextLine();
            insertIntoTable(name, lastname);
        }
        selectAll();
        updateData("3","Jack");
        deleteRow("Jack");
        selectAll();

        disconnect();
    }

    private static void insertIntoTable(String name, String lastname) throws SQLException {
        String query = "INSERT INTO peoples (name,lastname) VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,lastname);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
